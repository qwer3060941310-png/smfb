/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.desertstormfront.command.GameCommand;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.game.World;
import com.desertstormfront.game.WorldSimulator;
import com.desertstormfront.session.impl.ServerClientSession;
import com.desertstormfront.session.impl.ServerState;
import com.desertstormfront.session.impl.ServerTickThread;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.net.match.AddressProvider;
import com.noblemaster.lib.net.match.MatchRecord;
import com.noblemaster.lib.net.match.PortMappingCallback;
import java.util.ArrayList;
import java.util.List;

public strictfp final class GameServer
implements Runnable {
    private World world;
    private ServerClientSession[] sessions;
    private List pendingCommands;
    private List commandsToApply;
    private MatchRecord matchRecord;
    private ServerSocket serverSocket;
    private List clients;
    private ServerState state;
    private final Object lock = new Object();
    private long startTime;
    private Thread tickThread;
    private Thread acceptThread;

    private GameServer(World world) {
        this.world = world;
        this.sessions = new ServerClientSession[world.getPlayers().size()];
    }

    public static GameServer create(World world) {
        return new GameServer(world);
    }

    public void start(AddressProvider addressProvider, PortMappingCallback portMappingCallback) {
        String string = GameConfig.getProductId();
        String string2 = "Game: " + System.getProperty("user.name");
        String string3 = this.buildMatchLabel(this.world, 1);
        int i6 = UserConfig.getMultiplayerPort();
        this.matchRecord = GameConfig.getMatchMakingClient().createMatch(string, string2, string3, i6, addressProvider, portMappingCallback);
        this.clients = new ArrayList();
        ServerSocketHints serverSocketHints = new ServerSocketHints();
        serverSocketHints.backlog = 16;
        serverSocketHints.performancePrefConnectionTime = 0;
        serverSocketHints.performancePrefLatency = 1;
        serverSocketHints.performancePrefBandwidth = 0;
        serverSocketHints.reuseAddress = true;
        serverSocketHints.acceptTimeout = 5000;
        serverSocketHints.receiveBufferSize = 16384;
        this.serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, i6, serverSocketHints);
        this.state = ServerState.LOBBY;
        this.pendingCommands = new ArrayList(128);
        this.commandsToApply = new ArrayList(128);
        this.startTime = System.currentTimeMillis();
        this.tickThread = new ServerTickThread(this);
        this.tickThread.start();
        this.acceptThread = new Thread(this);
        this.acceptThread.setPriority(1);
        this.acceptThread.start();
    }

    public void shareTerrain(World world) {
        this.world.shareTerrainFrom(world);
    }

    public MatchRecord getMatchRecord() {
        return this.matchRecord;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        SocketHints socketHints = new SocketHints();
        socketHints.connectTimeout = 5000;
        socketHints.performancePrefConnectionTime = 0;
        socketHints.performancePrefLatency = 1;
        socketHints.performancePrefBandwidth = 0;
        socketHints.trafficClass = 20;
        socketHints.keepAlive = true;
        socketHints.tcpNoDelay = true;
        socketHints.sendBufferSize = 4096;
        socketHints.receiveBufferSize = 4096;
        socketHints.linger = false;
        socketHints.lingerDuration = 0;
        Thread thread = Thread.currentThread();
        while (thread == this.acceptThread) {
            int n;
            Object object;
            block22: {
                try {
                    OsfLog.info("MultiplayerServer: waiting for socket connection");
                    object = this.serverSocket.accept(socketHints);
                    OsfLog.info("MultiplayerServer: socket connected");
                    Object object2 = this.lock;
                    synchronized (object2) {
                        this.clients.add(new ServerClientSession(this, (Socket)object, null));
                    }
                }
                catch (Exception exception) {
                    if (!GameConfig.isDebugEnabled()) break block22;
                    OsfLog.info("Accept error: " + exception);
                }
            }
            if (this.state == ServerState.LOBBY) {
                try {
                    int n2 = 0;
                    n = 0;
                    while (n < this.sessions.length) {
                        if (this.sessions[n] != null) {
                            ++n2;
                        }
                        ++n;
                    }
                    if (n2 == 0) {
                        n2 = 1;
                    }
                    this.matchRecord.setLabel(this.buildMatchLabel(this.world, n2));
                    GameConfig.getMatchMakingClient().updateMatch(this.matchRecord);
                }
                catch (Exception exception) {
                    OsfLog.info("Match broadcasting error: " + exception);
                    OsfLog.logException(exception);
                }
            }
            object = this.lock;
            synchronized (object) {
                n = 0;
                while (n < this.clients.size()) {
                    if (!((ServerClientSession)this.clients.get(n)).isOpen()) {
                        this.clients.remove(n);
                        continue;
                    }
                    ++n;
                }
                if (this.clients.size() == 0) {
                    this.acceptThread = null;
                }
            }
        }
        this.tickThread = null;
        int n = 0;
        while (n < this.clients.size()) {
            ((ServerClientSession)this.clients.get(n)).stop();
            ++n;
        }
        this.clients.clear();
        if (this.serverSocket != null) {
            try {
                this.serverSocket.dispose();
            }
            catch (Exception exception) {}
            this.serverSocket = null;
        }
        this.world = null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void tick() {
        boolean i1;
        int i3;
        this.commandsToApply.clear();
        Object object = this.lock;
        synchronized (object) {
            i3 = 0;
            while (i3 < this.pendingCommands.size()) {
                this.commandsToApply.add((GameCommand)this.pendingCommands.get(i3));
                ++i3;
            }
            this.pendingCommands.clear();
            i1 = this.state == ServerState.RUNNING;
            i3 = 0;
            while (i3 < this.clients.size()) {
                ServerClientSession serverClientSession = (ServerClientSession)this.clients.get(i3);
                serverClientSession.send((Object)this.state);
                ++i3;
            }
        }
        if (i1) {
            int n = 0;
            while (n < this.commandsToApply.size()) {
                ((GameCommand)this.commandsToApply.get(n)).execute(this.world);
                ++n;
            }
            World world = this.world;
            if (world != null) {
                i3 = 0;
                while (i3 < 3) {
                    WorldSimulator.tick(world, 33);
                    ++i3;
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void broadcastCommand(GameCommand gameCommand) {
        if (this.state == ServerState.RUNNING) {
            Object object = this.lock;
            synchronized (object) {
                int i3 = 0;
                while (i3 < this.clients.size()) {
                    ServerClientSession serverClientSession = (ServerClientSession)this.clients.get(i3);
                    serverClientSession.send(gameCommand);
                    ++i3;
                }
                this.pendingCommands.add(gameCommand);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void broadcast(String string) {
        Object object = this.lock;
        synchronized (object) {
            int i3 = 0;
            while (i3 < this.clients.size()) {
                ServerClientSession serverClientSession = (ServerClientSession)this.clients.get(i3);
                serverClientSession.send(string);
                ++i3;
            }
        }
    }

    public void stop() {
        this.acceptThread = null;
    }

    private String buildMatchLabel(World world, int i2) {
        return "4|" + world.getName() + ", " + i2 + "/" + world.getPlayers().size() + " Players, " + (world.getPlayers().countAliveTeams() > 0 ? String.valueOf(world.getPlayers().countAliveTeams()) + " Teams, " : "") + Gdx.app.getType().toString();
    }

    private String getLogPrefix() {
        int i1 = (int)((System.currentTimeMillis() - this.startTime) / 1000L);
        int i2 = i1 / 60;
        int i3 = i1 % 60;
        return String.valueOf(i3 < 10 ? String.valueOf(i2) + ":0" + i3 : String.valueOf(i2) + ":" + i3) + ": ";
    }

    static /* synthetic */ Object getLock(GameServer gameServer) {
        return gameServer.lock;
    }

    static /* synthetic */ ServerClientSession[] getSessions(GameServer gameServer) {
        return gameServer.sessions;
    }

    static /* synthetic */ String getLogPrefix(GameServer gameServer) {
        return gameServer.getLogPrefix();
    }

    static /* synthetic */ void broadcast(GameServer gameServer, String string) {
        gameServer.broadcast(string);
    }

    static /* synthetic */ List getClients(GameServer gameServer) {
        return gameServer.clients;
    }

    static /* synthetic */ World getWorld(GameServer gameServer) {
        return gameServer.world;
    }

    static /* synthetic */ ServerState getState(GameServer gameServer) {
        return gameServer.state;
    }

    static /* synthetic */ void setState(GameServer gameServer, ServerState serverState) {
        gameServer.state = serverState;
    }

    static /* synthetic */ void broadcastCommand(GameServer gameServer, GameCommand gameCommand) {
        gameServer.broadcastCommand(gameCommand);
    }

    static /* synthetic */ Thread getTickThread(GameServer gameServer) {
        return gameServer.tickThread;
    }

    static /* synthetic */ void tick(GameServer gameServer) {
        gameServer.tick();
    }
}


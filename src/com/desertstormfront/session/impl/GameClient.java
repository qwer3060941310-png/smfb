/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.desertstormfront.command.GameCommand;
import com.desertstormfront.game.GameEventListener;
import com.desertstormfront.game.World;
import com.desertstormfront.game.WorldSimulator;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.io.WorldSerializer;
import com.desertstormfront.session.impl.NetworkOpcode;
import com.desertstormfront.session.impl.ServerState;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import com.noblemaster.lib.io.stream.impl.StreamDataReader;
import com.noblemaster.lib.io.stream.impl.StreamDataWriter;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.net.match.AddressProvider;
import com.noblemaster.lib.net.match.MatchRecord;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public strictfp final class GameClient
implements Runnable {
    private AddressProvider addressProvider;
    private MatchRecord matchRecord;
    private Socket socket;
    private DataReader reader;
    private DataWriter writer;
    private int pendingStateCount;
    private ServerState serverState;
    private List incomingMessages;
    private List commandsToExecute;
    private int ticksRemaining;
    private List chatTimes;
    private List chatMessages;
    private String chatText;
    private int playerCount;
    private String[] playerNames;
    private final Object lock = new Object();
    private long nextTickTime;
    private Thread readerThread;
    private World world;
    private int playerIndex;

    private GameClient(AddressProvider addressProvider, MatchRecord matchRecord) {
        this.addressProvider = addressProvider;
        this.matchRecord = matchRecord;
    }

    public static GameClient create(AddressProvider addressProvider, MatchRecord matchRecord) {
        return new GameClient(addressProvider, matchRecord);
    }

    public void start()  throws IOException {
        this.readerThread = new Thread(this);
        this.readerThread.start();
        while (this.readerThread != null && this.world == null) {
            try {
                Thread.sleep(500L);
            }
            catch (InterruptedException interruptedException) {}
        }
        if (this.world == null) {
            throw new IOException(Messages.get("CountNotConnectETC[i18n]: Could not connect to game. Possible causes include:\n\n(1) Game Full\n(2) Host Closed\n(3) Bad Connection"));
        }
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public String[] getPlayerNames() {
        return this.playerNames;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean update(GameEventListener gameEventListener) {
        long l;
        while (this.ticksRemaining == 0 && this.pendingStateCount > 0) {
            this.commandsToExecute.clear();
            Object object = this.lock;
            synchronized (object) {
                Object e;
                while ((e = this.incomingMessages.remove(0)) instanceof GameCommand) {
                    this.commandsToExecute.add((GameCommand)e);
                }
                this.serverState = (ServerState)((Object)e);
                --this.pendingStateCount;
            }
            if (this.serverState != ServerState.RUNNING) continue;
            int n = 0;
            while (n < this.commandsToExecute.size()) {
                ((GameCommand)this.commandsToExecute.get(n)).execute(this.world);
                ++n;
            }
            this.ticksRemaining = 3;
            long l2 = System.currentTimeMillis();
            if (this.nextTickTime < l2) {
                this.nextTickTime = l2;
                continue;
            }
            if (this.pendingStateCount < 2) continue;
            this.nextTickTime -= (long)(2 * (this.pendingStateCount - 1));
        }
        if (this.ticksRemaining > 0 && (l = this.nextTickTime - System.currentTimeMillis()) < 12L) {
            WorldSimulator.tick(this.world, gameEventListener, 33);
            --this.ticksRemaining;
            if (l > 0L) {
                try {
                    Thread.sleep(l);
                }
                catch (InterruptedException interruptedException) {}
            }
            this.nextTickTime += 33L;
        }
        return this.serverState == ServerState.RUNNING;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getChatText() {
        Object object = this.lock;
        synchronized (object) {
            if (this.chatMessages.size() > 0) {
                long l2 = System.currentTimeMillis() - 20000L;
                if ((Long)this.chatTimes.get(0) < l2) {
                    while (this.chatMessages.size() > 0 && (Long)this.chatTimes.get(0) < l2) {
                        this.chatTimes.remove(0);
                        this.chatMessages.remove(0);
                    }
                    this.chatText = "";
                    int i4 = 0;
                    while (i4 < this.chatMessages.size()) {
                        this.chatText = String.valueOf((String)this.chatMessages.get(i4)) + "\n" + this.chatText;
                        ++i4;
                    }
                }
            }
            return this.chatText;
        }
    }

    public void claimSlot(int i1) {
        try {
            this.writer.writeByte(NetworkOpcode.SLOTS.getCode());
            this.writer.writeInt(i1);
            this.writer.writeString(System.getProperty("user.name"));
            this.writer.flush();
        }
        catch (Exception exception) {
            OsfLog.info("I/O error: " + exception);
            OsfLog.logException(exception);
            this.readerThread = null;
        }
    }

    public void setReady(boolean bl) {
        try {
            this.writer.writeByte(NetworkOpcode.READY.getCode());
            this.writer.writeBoolean(bl);
            this.writer.flush();
        }
        catch (Exception exception) {
            OsfLog.info("I/O error: " + exception);
            OsfLog.logException(exception);
            this.readerThread = null;
        }
    }

    public void sendCommand(GameCommand gameCommand) {
        try {
            this.writer.writeByte(NetworkOpcode.COMMAND.getCode());
            gameCommand.write(this.writer);
            this.writer.flush();
        }
        catch (Exception exception) {
            OsfLog.info("I/O error: " + exception);
            OsfLog.logException(exception);
            this.readerThread = null;
        }
    }

    public World getWorld() {
        return this.world;
    }

    public Player getPlayer() {
        if (this.playerIndex < 0) {
            return null;
        }
        return (Player)this.world.getPlayers().get(this.playerIndex);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        try {
            try {
                this.connect();
                this.pendingStateCount = 0;
                this.serverState = ServerState.LOBBY;
                this.nextTickTime = 0L;
                this.incomingMessages = new ArrayList(128);
                this.commandsToExecute = new ArrayList(128);
                this.ticksRemaining = 0;
                this.chatTimes = new ArrayList(128);
                this.chatMessages = new ArrayList(128);
                this.chatText = "";
                this.writer.writeByte(NetworkOpcode.JOIN.getCode());
                this.writer.writeByte(NetworkOpcode.WORLD.getCode());
                this.writer.writeString(System.getProperty("user.name"));
                this.writer.flush();
                Thread thread = Thread.currentThread();
                while (thread == this.readerThread) {
                    NetworkOpcode networkOpcode = NetworkOpcode.fromCode(this.reader.readByte());
                    switch (networkOpcode) {
                        case WORLD: {
                            this.playerIndex = -1;
                            World object = WorldSerializer.read(this.reader);
                            this.playerCount = 0;
                            this.playerNames = new String[((World)object).getPlayers().size()];
                            int n = 0;
                            while (n < this.playerNames.length) {
                                this.playerNames[n] = this.reader.readString();
                                ++n;
                            }
                            this.writer.writeByte(NetworkOpcode.ACK.getCode());
                            this.writer.flush();
                            this.world = object;
                            break;
                        }
                        case COMMAND: {
                            Object object = GameCommand.read(this.reader);
                            Object object2 = this.lock;
                            synchronized (object2) {
                                this.incomingMessages.add(object);
                                break;
                            }
                        }
                        case CHAT: {
                            Object object = this.reader.readString();
                            Object object3 = this.lock;
                            synchronized (object3) {
                                this.chatTimes.add(System.currentTimeMillis());
                                this.chatMessages.add(object);
                                this.chatText = String.valueOf(object) + "\n" + this.chatText;
                                break;
                            }
                        }
                        case SLOTS: {
                            int n = this.reader.readInt();
                            Object object = this.lock;
                            synchronized (object) {
                                this.playerIndex = n;
                                break;
                            }
                        }
                        case STATUS: {
                            int n;
                            Object object = ServerState.fromCode(this.reader.readByte());
                            if (object != ServerState.RUNNING) {
                                this.playerCount = this.reader.readInt();
                                n = 0;
                                while (n < this.playerNames.length) {
                                    this.playerNames[n] = this.reader.readString();
                                    ++n;
                                }
                            }
                            Object object4 = this.lock;
                            synchronized (object4) {
                                this.incomingMessages.add(object);
                                ++this.pendingStateCount;
                                break;
                            }
                        }
                        default: {
                            OsfLog.error("Response handling not implemented: " + (Object)((Object)networkOpcode));
                        }
                    }
                    try {
                        Thread.sleep(20L);
                    }
                    catch (InterruptedException interruptedException) {}
                }
            }
            catch (IOException iOException) {
                OsfLog.info("Disconnected from server.");
                this.readerThread = null;
                this.close();
            }
        }
        finally {
            this.close();
        }
    }

    public boolean isConnected() {
        return this.readerThread != null;
    }

    public void disconnect() {
        this.readerThread = null;
    }

    private void connect() {
        int i3;
        String string;
        String string2 = this.addressProvider.getExternalAddress();
        if (string2 != null && string2.equals(this.matchRecord.getExternalAddress())) {
            string = this.matchRecord.getLocalAddress();
            i3 = this.matchRecord.getLocalPort();
        } else {
            string = this.matchRecord.getExternalAddress();
            i3 = this.matchRecord.getExternalPort();
        }
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
        this.socket = Gdx.net.newClientSocket(Net.Protocol.TCP, string, i3, socketHints);
        this.writer = new StreamDataWriter(this.socket.getOutputStream());
        this.reader = new StreamDataReader(this.socket.getInputStream());
    }

    private void close() {
        if (this.writer != null) {
                this.writer.close();
            this.writer = null;
        }
        if (this.reader != null) {
                this.reader.close();
            this.reader = null;
        }
        if (this.socket != null) {
            try {
                this.socket.dispose();
            }
            catch (Exception exception) {}
            this.socket = null;
        }
    }
}


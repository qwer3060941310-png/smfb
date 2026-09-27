/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.badlogic.gdx.net.Socket;
import com.desertstormfront.command.GameCommand;
import com.desertstormfront.io.WorldSerializer;
import com.desertstormfront.session.impl.ClientWriterThread;
import com.desertstormfront.session.impl.GameServer;
import com.desertstormfront.session.impl.NetworkOpcode;
import com.desertstormfront.session.impl.ServerState;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import com.noblemaster.lib.io.stream.impl.StreamDataReader;
import com.noblemaster.lib.io.stream.impl.StreamDataWriter;
import com.noblemaster.lib.log.OsfLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

strictfp final class ServerClientSession
implements Runnable {
    private Socket socket;
    private DataReader reader;
    private DataWriter writer;
    private String playerName;
    private boolean ready;
    private boolean playing;
    private List pendingMessages;
    private List sendBatch;
    private Thread readerThread;
    private Thread writerThread;
    final /* synthetic */ GameServer server;

    private ServerClientSession(GameServer gameServer, Socket socket) {
        this.server = gameServer;
        this.socket = socket;
        this.reader = new StreamDataReader(socket.getInputStream());
        this.writer = new StreamDataWriter(socket.getOutputStream());
        NetworkOpcode networkOpcode = NetworkOpcode.fromCode(this.reader.readByte());
        switch (networkOpcode) {
            case HANDSHAKE: {
                try {
                        this.writer.writeByte((byte)0);
                        this.writer.flush();
                        break;
                }
                finally {
                    this.close();
                }
            }
            case JOIN: {
                this.playing = false;
                this.pendingMessages = new ArrayList(256);
                this.sendBatch = new ArrayList(256);
                this.readerThread = new Thread(this);
                this.readerThread.start();
                this.writerThread = new ClientWriterThread(this);
                this.writerThread.start();
                break;
            }
            default: {
                OsfLog.error("Request not implemented: " + (Object)((Object)networkOpcode));
            }
        }
    }

    public void send(Object object) {
        if (this.isReady()) {
            this.pendingMessages.add(object);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     */
    @Override
    public void run() {
        Thread var1_1 = null;
        NetworkOpcode var2_2 = null;
        String var3_3 = null;
        int var3_4 = 0;
        Object var3_5 = null;
        int var4_6 = 0;
        Object var4_7 = null;
        Object var5_8 = null;
        int var5_9 = 0;
        int var5_10 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i10 = 0;
        block58: {
            block57: {
                try {
                    try {
                        var1_1 = Thread.currentThread();
                        while (var1_1 == this.readerThread) {
                            var2_2 = NetworkOpcode.fromCode(this.reader.readByte());
                            switch (var2_2) {
                                case JOIN: {
                                    var3_3 = this.reader.readString();
                                    var5_8 = GameServer.getLock(this.server);
                                    synchronized (var5_8) {
                                        i6 = 0;
                                        i7 = 0;
                                        while (i7 < GameServer.getClients(this.server).size()) {
                                            if (((ServerClientSession)GameServer.getClients(this.server).get(i7)).isOpen()) {
                                                ++i6;
                                            }
                                            ++i7;
                                        }
                                        var4_6 = i6 <= GameServer.getWorld(this.server).getPlayers().countAlive() ? 1 : 0;
                                        if (var4_6 != 0) {
                                            if (GameServer.getState(this.server) == ServerState.RUNNING) {
                                                GameServer.setState(this.server, ServerState.PAUSED);
                                            }
                                        } else {
                                            this.readerThread = null;
                                            if (GameServer.getState(this.server) == ServerState.PAUSED) {
                                                --i6;
                                                i7 = 0;
                                                i8 = 0;
                                                while (i8 < GameServer.getClients(this.server).size()) {
                                                    if (((ServerClientSession)GameServer.getClients(this.server).get(i8)).isPlaying()) {
                                                        ++i7;
                                                    }
                                                    ++i8;
                                                }
                                                if (i6 == i7) {
                                                    GameServer.setState(this.server, ServerState.RUNNING);
                                                }
                                            }
                                        }
                                    }
                                    if (var4_6 == 0) break;
                                    GameServer.broadcast(this.server, String.valueOf(GameServer.getLogPrefix(this.server)) + var3_3 + " is connecting...");
                                    this.writer.writeByte(NetworkOpcode.WORLD.getCode());
                                    WorldSerializer.write(this.writer, GameServer.getWorld(this.server));
                                    var5_9 = 0;
                                    while (var5_9 < GameServer.getSessions(this.server).length) {
                                        if (GameServer.getSessions(this.server)[var5_9] != null) {
                                            this.writer.writeString(GameServer.getSessions(this.server)[var5_9].getPlayerName());
                                        } else {
                                            this.writer.writeString(null);
                                        }
                                        ++var5_9;
                                    }
                                    this.writer.flush();
                                    GameServer.broadcast(this.server, String.valueOf(GameServer.getLogPrefix(this.server)) + var3_3 + " is connected.");
                                    break;
                                }
                                case COMMAND: {
                                    GameServer.broadcastCommand(this.server, GameCommand.read(this.reader));
                                    break;
                                }
                                case CHAT: {
                                    GameServer.broadcast(this.server, this.reader.readString());
                                    break;
                                }
                                case SLOTS: {
                                    var3_4 = this.reader.readInt();
                                    this.playerName = this.reader.readString();
                                    var4_7 = GameServer.getLock(this.server);
                                    synchronized (var4_7) {
                                        if (GameServer.getSessions(this.server)[var3_4] == null) {
                                            GameServer.getSessions((GameServer)this.server)[var3_4] = this;
                                        } else {
                                            var5_10 = 0;
                                            while (var5_10 < GameServer.getSessions(this.server).length) {
                                                if (GameServer.getSessions(this.server)[var5_10] == null) {
                                                    var3_4 = var5_10;
                                                    GameServer.getSessions((GameServer)this.server)[var3_4] = this;
                                                    break;
                                                }
                                                ++var5_10;
                                            }
                                        }
                                        this.send(var3_4);
                                        break;
                                    }
                                }
                                case READY: {
                                    this.ready = true;
                                    break;
                                }
                                case STATUS: {
                                    this.playing = this.reader.readBoolean();
                                    var3_5 = GameServer.getLock(this.server);
                                    synchronized (var3_5) {
                                        var4_6 = 0;
                                        var5_10 = 0;
                                        while (var5_10 < GameServer.getClients(this.server).size()) {
                                            if (((ServerClientSession)GameServer.getClients(this.server).get(var5_10)).isOpen()) {
                                                ++var4_6;
                                            }
                                            ++var5_10;
                                        }
                                        var5_10 = 0;
                                        i6 = 0;
                                        while (i6 < GameServer.getClients(this.server).size()) {
                                            if (((ServerClientSession)GameServer.getClients(this.server).get(i6)).isPlaying()) {
                                                ++var5_10;
                                            }
                                            ++i6;
                                        }
                                        if (GameServer.getState(this.server) == ServerState.LOBBY) {
                                            if (var5_10 == GameServer.getWorld(this.server).getPlayers().countAlive()) {
                                                GameServer.setState(this.server, ServerState.RUNNING);
                                            }
                                        } else if (GameServer.getState(this.server) == ServerState.RUNNING) {
                                            if (var5_10 < var4_6) {
                                                GameServer.setState(this.server, ServerState.PAUSED);
                                            }
                                        } else if (GameServer.getState(this.server) == ServerState.PAUSED && var5_10 == var4_6) {
                                            GameServer.setState(this.server, ServerState.RUNNING);
                                        }
                                    }
                                    GameServer.broadcast(this.server, String.valueOf(GameServer.getLogPrefix(this.server)) + this.playerName + " is ready.");
                                    break;
                                }
                                default: {
                                    OsfLog.error("Request handling not implemented: " + (Object)var2_2);
                                }
                            }
                            try {
                                Thread.sleep(20L);
                            }
                            catch (InterruptedException v4) {}
                        }
                        break block57;
                    }
                    catch (IOException v5) {
                        OsfLog.info("Client disconnected.");
                        this.readerThread = null;
                        this.writerThread = null;
                        i10 = 0;
                        while (i10 < GameServer.getSessions((GameServer)this.server).length) {
                            if (GameServer.getSessions(this.server)[i10] == this) {
                                GameServer.getSessions((GameServer)this.server)[i10] = null;
                            }
                            ++i10;
                        }
                        this.close();
                        GameServer.broadcast(this.server, String.valueOf(GameServer.getLogPrefix(this.server)) + this.playerName + " has disconnected.");
                        break block58;
                    }
                }
                catch (Throwable var9_17) {
                    this.writerThread = null;
                    i10 = 0;
                    while (i10 < GameServer.getSessions((GameServer)this.server).length) {
                        if (GameServer.getSessions(this.server)[i10] == this) {
                            GameServer.getSessions((GameServer)this.server)[i10] = null;
                        }
                        ++i10;
                    }
                    this.close();
                    GameServer.broadcast(this.server, String.valueOf(GameServer.getLogPrefix(this.server)) + this.playerName + " has disconnected.");
                    throw var9_17;
                }
            }
            this.writerThread = null;
            i10 = 0;
            while (i10 < GameServer.getSessions(this.server).length) {
                if (GameServer.getSessions(this.server)[i10] == this) {
                    GameServer.getSessions((GameServer)this.server)[i10] = null;
                }
                ++i10;
            }
            this.close();
            GameServer.broadcast(this.server, String.valueOf(GameServer.getLogPrefix(this.server)) + this.playerName + " has disconnected.");
        }
    }

    public boolean isOpen() {
        return this.readerThread != null;
    }

    public boolean isReady() {
        return this.isOpen() && this.ready;
    }

    public boolean isPlaying() {
        return this.isOpen() && this.playing;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void stop() {
        this.readerThread = null;
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

    static /* synthetic */ Thread getWriterThread(ServerClientSession serverClientSession) {
        return serverClientSession.writerThread;
    }

    static /* synthetic */ List getSendBatch(ServerClientSession serverClientSession) {
        return serverClientSession.sendBatch;
    }

    static /* synthetic */ List getPendingMessages(ServerClientSession serverClientSession) {
        return serverClientSession.pendingMessages;
    }

    static /* synthetic */ DataWriter getWriter(ServerClientSession serverClientSession) {
        return serverClientSession.writer;
    }

    /* synthetic */ ServerClientSession(GameServer gameServer, Socket socket, ServerClientSession serverClientSession) {
        this(gameServer, socket);
    }

    static /* synthetic */ GameServer getServer(ServerClientSession serverClientSession) {
        return serverClientSession.server;
    }
}


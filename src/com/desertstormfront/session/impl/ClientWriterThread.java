/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.desertstormfront.command.GameCommand;
import com.desertstormfront.session.impl.GameServer;
import com.desertstormfront.session.impl.NetworkOpcode;
import com.desertstormfront.session.impl.ServerClientSession;
import com.desertstormfront.session.impl.ServerState;
import com.noblemaster.lib.log.OsfLog;
import java.io.IOException;

strictfp class ClientWriterThread
extends Thread {
    final /* synthetic */ ServerClientSession session;

    ClientWriterThread(ServerClientSession serverClientSession) {
        this.session = serverClientSession;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        try {
            Thread thread = Thread.currentThread();
            while (ServerClientSession.getWriterThread(this.session) == thread) {
                ServerClientSession.getSendBatch(this.session).clear();
                Object object = GameServer.getLock(ServerClientSession.getServer(this.session));
                synchronized (object) {
                    int n = 0;
                    while (n < ServerClientSession.getPendingMessages(this.session).size()) {
                        ServerClientSession.getSendBatch(this.session).add(ServerClientSession.getPendingMessages(this.session).get(n));
                        ++n;
                    }
                    ServerClientSession.getPendingMessages(this.session).clear();
                }
                int n = 0;
                while (n < ServerClientSession.getSendBatch(this.session).size()) {
                    Object e = ServerClientSession.getSendBatch(this.session).get(n);
                    if (e instanceof ServerState) {
                        ServerState serverState = (ServerState)((Object)e);
                        ServerClientSession.getWriter(this.session).writeByte(NetworkOpcode.STATUS.getCode());
                        ServerClientSession.getWriter(this.session).writeByte(serverState.getCode());
                        if (serverState != ServerState.RUNNING) {
                            int i5 = 0;
                            int i6 = 0;
                            while (i6 < GameServer.getSessions(ServerClientSession.getServer(this.session)).length) {
                                if (GameServer.getSessions(ServerClientSession.getServer(this.session))[i6] != null) {
                                    ++i5;
                                }
                                ++i6;
                            }
                            ServerClientSession.getWriter(this.session).writeInt(i5);
                            i6 = 0;
                            while (i6 < GameServer.getSessions(ServerClientSession.getServer(this.session)).length) {
                                if (GameServer.getSessions(ServerClientSession.getServer(this.session))[i6] != null) {
                                    ServerClientSession.getWriter(this.session).writeString(GameServer.getSessions(ServerClientSession.getServer(this.session))[i6].getPlayerName());
                                } else {
                                    ServerClientSession.getWriter(this.session).writeString(null);
                                }
                                ++i6;
                            }
                        }
                        ServerClientSession.getWriter(this.session).flush();
                    } else if (e instanceof GameCommand) {
                        ServerClientSession.getWriter(this.session).writeByte(NetworkOpcode.COMMAND.getCode());
                        ((GameCommand)e).write(ServerClientSession.getWriter(this.session));
                        ServerClientSession.getWriter(this.session).flush();
                    } else if (e instanceof String) {
                        ServerClientSession.getWriter(this.session).writeByte(NetworkOpcode.CHAT.getCode());
                        ServerClientSession.getWriter(this.session).writeString((String)e);
                        ServerClientSession.getWriter(this.session).flush();
                    } else if (e instanceof Integer) {
                        ServerClientSession.getWriter(this.session).writeByte(NetworkOpcode.SLOTS.getCode());
                        ServerClientSession.getWriter(this.session).writeInt((Integer)e);
                        ServerClientSession.getWriter(this.session).flush();
                    }
                    ++n;
                }
                try {
                    Thread.sleep(20L);
                }
                catch (InterruptedException interruptedException) {}
            }
        }
        catch (RuntimeException runtimeException) {
            OsfLog.info("Closing sending thread for client.");
        }
    }
}


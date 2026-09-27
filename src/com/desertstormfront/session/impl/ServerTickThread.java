/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.desertstormfront.session.impl.GameServer;

strictfp class ServerTickThread
extends Thread {
    final /* synthetic */ GameServer server;

    ServerTickThread(GameServer gameServer) {
        this.server = gameServer;
    }

    @Override
    public void run() {
        long l1 = System.currentTimeMillis();
        Thread thread = Thread.currentThread();
        while (GameServer.getTickThread(this.server) == thread) {
            long l4 = System.currentTimeMillis();
            if (l4 < l1) {
                try {
                    Thread.sleep(l1 - l4);
                }
                catch (InterruptedException interruptedException) {}
            }
            l1 += 99L;
            GameServer.tick(this.server);
        }
    }
}


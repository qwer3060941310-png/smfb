/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

public enum ServerState {
    LOBBY,
    PAUSED,
    RUNNING;


    public byte getCode() {
        return (byte)this.ordinal();
    }

    public static ServerState fromCode(byte by) {
        return ServerState.values()[by];
    }
}


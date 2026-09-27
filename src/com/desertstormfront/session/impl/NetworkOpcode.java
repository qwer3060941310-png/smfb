/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

public enum NetworkOpcode {
    HANDSHAKE,
    JOIN,
    WORLD,
    COMMAND,
    CHAT,
    SLOTS,
    ACK,
    READY,
    STATUS;


    public byte getCode() {
        return (byte)this.ordinal();
    }

    public static NetworkOpcode fromCode(byte by) {
        return NetworkOpcode.values()[by];
    }
}


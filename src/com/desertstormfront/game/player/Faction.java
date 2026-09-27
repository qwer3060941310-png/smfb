/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

public strictfp final class Faction {
    private int a;
    private String b;
    private String c;

    public Faction(int i1, String string, String string2) {
        this.a = i1;
        this.b = string;
        this.c = string2;
    }

    public final int getId() {
        return this.a;
    }

    public final String getName() {
        return this.b;
    }

    public final String getKey() {
        return this.c;
    }

    public final String toString() {
        return this.b;
    }
}


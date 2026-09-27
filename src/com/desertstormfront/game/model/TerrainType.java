/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

public strictfp class TerrainType {
    private int a;
    private String b;
    private String c;
    private char d;
    private int e;

    public TerrainType(int i1, String string, String string2, char c, int i5) {
        this.a = i1;
        this.b = string;
        this.c = string2;
        this.d = c;
        this.e = i5;
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

    public final char getCode() {
        return this.d;
    }

    public final int getSpriteCount() {
        return this.e;
    }

    public String toString() {
        return this.b;
    }
}


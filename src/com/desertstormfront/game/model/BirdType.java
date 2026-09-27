/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

public enum BirdType {
    Sparrows("Sparrows[i18n]: Sparrows", 0.28f),
    Seagulls("Seagulls[i18n]: Seagulls", 0.24f);

    private String c;
    private float d;

    /*
     * WARNING - void declaration
     */
    private BirdType(String var3_1, float var4_2) {
        this.c = var3_1;
        this.d = var4_2;
    }

    public final float getSpeed() {
        return this.d;
    }

    public final String toString() {
        return this.c;
    }
}


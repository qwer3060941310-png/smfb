/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

public enum Difficulty {
    Casual("Casual[i18n]: Casual"),
    Normal("Normal[i18n]: Normal"),
    Hard("Hard[i18n]: Hard"),
    Extreme("Extreme[i18n]: Extreme");

    private String e;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private Difficulty(String var3_1) {
        this.e = var3_1;
    }

    public final String getName() {
        return this.e;
    }

    public final String toString() {
        return this.e;
    }
}


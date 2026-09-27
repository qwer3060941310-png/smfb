/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

public enum Layer {
    Under("Under[i18n]: Under"),
    Base("Base[i18n]: Base"),
    Upper("Upper[i18n]: Upper");

    private String d;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private Layer(String var3_1) {
        this.d = var3_1;
    }

    public final String toString() {
        return this.d;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

public enum Domain {
    Ground("Ground[i18n]: Ground"),
    Water("Water[i18n]: Water"),
    Air("Air[i18n]: Air"),
    Amphibian("Amphibian[i18n]: Amphibian");

    private String e;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private Domain(String var3_1) {
        this.e = var3_1;
    }

    public final String toString() {
        return this.e;
    }
}


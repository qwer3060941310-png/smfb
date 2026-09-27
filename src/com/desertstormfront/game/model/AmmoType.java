/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

public enum AmmoType {
    Bullets("Bullets[i18n]: Bullets", "BULLETS", 5.0f, 6283.1855f, 0, 0.0f),
    Shell("Shell[i18n]: Shell", "SHELL", 4.0f, 7.853982f, 0, 0.0f),
    Missile("Missile[i18n]: Missile", "MISSILE", 1.3f, (float)Math.PI, 10, 0.13f),
    Cannonball("Cannonball[i18n]: Cannonball", "CANNONBALL", 3.6f, 6.5973444f, 0, 0.0f),
    Torpedo("Torpedo[i18n]: Torpedo", "TORPEDO", 0.4f, 1.5707964f, 5, 0.34f);

    private String g;
    private String h;
    private float i;
    private float j;
    private int k;
    private float l;
    public static final int f;

    static {
        int i0 = 0;
        int i1 = 0;
        while (i1 < AmmoType.values().length) {
            int i2 = AmmoType.values()[i1].getSubProjectileCount();
            if (i2 > i0) {
                i0 = i2;
            }
            ++i1;
        }
        f = i0;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private AmmoType(String var7_5, String var8_6, float f, float f2, int n, float f3) {
        this.g = var7_5;
        this.h = var8_6;
        this.i = f;
        this.j = f2;
        this.k = n;
        this.l = f3;
    }

    public final String getKey() {
        return this.h;
    }

    public final float getSpeed() {
        return this.i;
    }

    public final float getTurnRate() {
        return this.j;
    }

    public final boolean hasSubProjectiles() {
        return this.k > 0;
    }

    public final int getSubProjectileCount() {
        return this.k;
    }

    public final float getAnimationDuration() {
        return this.l;
    }

    public final String toString() {
        return this.g;
    }
}


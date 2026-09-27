/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

public enum Direction {
    East(1, 1, 0, 0.0f),
    NorthEast(2, 1, 1, 0.7853982f),
    North(3, 0, 1, 1.5707964f),
    NorthWest(4, -1, 1, 2.3561945f),
    West(5, -1, 0, (float)Math.PI),
    SouthWest(6, -1, -1, 3.9269907f),
    South(7, 0, -1, 4.712389f),
    SouthEast(0, 1, -1, 5.497787f);

    private int i;
    private int j;
    private int k;
    private float l;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private Direction(int var5_3, int var6_4, int var7_5, float var8_6) {
        this.i = var5_3;
        this.j = var6_4;
        this.k = var7_5;
        this.l = var8_6;
    }

    public final int getIndex() {
        return this.i;
    }

    public final int getDx() {
        return this.j;
    }

    public final int getDy() {
        return this.k;
    }

    public final float getAngle() {
        return this.l;
    }

    public final Direction getOpposite() {
        return Direction.values()[(this.ordinal() + Direction.values().length / 2) % Direction.values().length];
    }
}


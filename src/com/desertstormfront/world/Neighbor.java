/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.desertstormfront.world.NeighborTable;

public enum Neighbor {
    None(Integer.MAX_VALUE, Integer.MAX_VALUE),
    SouthWest(-1, -1),
    West(-1, 0),
    NorthWest(-1, 1),
    South(0, -1),
    Center(0, 0),
    North(0, 1),
    SouthEast(1, -1),
    East(1, 0),
    NorthEast(1, 1);

    private int k;
    private int l;
    private float m;
    private float n;
    private boolean o;
    private int p;

    /*
     * WARNING - void declaration
     */
    private Neighbor(int var3_1, int var4_2) {
        this.k = var3_1;
        this.l = var4_2;
        this.m = var3_1 == -1 ? 0.1f : (var3_1 == 1 ? 0.9f : 0.5f);
        this.n = var4_2 == -1 ? 0.1f : (var4_2 == 1 ? 0.9f : 0.5f);
        this.o = var3_1 != Integer.MAX_VALUE && var4_2 != Integer.MAX_VALUE;
        this.p = this.o ? Neighbor.toIndex((int)var3_1, (int)var4_2) : 0;
        NeighborTable.getTable()[this.p] = this;
    }

    public final int getDx() {
        return this.k;
    }

    public final int getDy() {
        return this.l;
    }

    public final float getEdgeOffsetX() {
        return this.m;
    }

    public final float getEdgeOffsetY() {
        return this.n;
    }

    public final boolean isValid() {
        return this.o;
    }

    final int getIndex() {
        return this.p;
    }

    private static final int toIndex(int i0, int i1) {
        return (i1 + 1 << 2) + (i0 + 2);
    }

    static final Neighbor fromIndex(int i0) {
        return NeighborTable.getTable()[i0];
    }

    public static final Neighbor of(int i0, int i1) {
        return NeighborTable.getTable()[Neighbor.toIndex(i0, i1)];
    }
}


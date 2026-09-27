/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

import com.desertstormfront.game.model.Unit;
import com.desertstormfront.world.UnitPosition;

public strictfp final class FogOfWar {
    private boolean[] a;
    private boolean[] b;
    private int c;
    private int d;

    public FogOfWar(int i1, int i2) {
        this.c = i1 + 1;
        this.d = i2 + 1;
        this.a = new boolean[this.d * this.c];
        this.b = new boolean[this.d * this.c];
    }

    public final boolean isUnitVisible(Unit unit) {
        return this.isUnitPositionExplored(unit) && (unit.isImmobile() || this.isUnitPositionVisible(unit));
    }

    public final boolean isUnitCurrentlyVisible(Unit unit) {
        return this.isUnitPositionVisible(unit);
    }

    private final boolean isUnitPositionExplored(Unit unit) {
        return this.isPositionExplored(unit.getPosition());
    }

    public final boolean isPositionExplored(UnitPosition unitPosition) {
        return this.isExplored((int)unitPosition.getX(), (int)unitPosition.getY());
    }

    public final boolean isExplored(int i1, int i2) {
        return this.a[i2 * this.c + i1] || this.a[i2 * this.c + (i1 + 1)] || this.a[(i2 + 1) * this.c + i1] || this.a[(i2 + 1) * this.c + (i1 + 1)];
    }

    public final void setExplored(int i1, int i2, boolean bl) {
        this.a[i2 * this.c + i1] = bl;
        this.a[i2 * this.c + (i1 + 1)] = bl;
        this.a[(i2 + 1) * this.c + i1] = bl;
        this.a[(i2 + 1) * this.c + i1 + 1] = bl;
    }

    private final boolean isUnitPositionVisible(Unit unit) {
        return this.isPositionVisible(unit.getPosition());
    }

    public final boolean isPositionVisible(UnitPosition unitPosition) {
        return this.isVisible((int)unitPosition.getX(), (int)unitPosition.getY());
    }

    public final boolean isVisible(int i1, int i2) {
        return this.b[i2 * this.c + i1] || this.b[i2 * this.c + (i1 + 1)] || this.b[(i2 + 1) * this.c + i1] || this.b[(i2 + 1) * this.c + i1 + 1];
    }

    public final void setVisible(int i1, int i2, boolean bl) {
        this.b[i2 * this.c + i1] = bl;
        this.b[i2 * this.c + (i1 + 1)] = bl;
        this.b[(i2 + 1) * this.c + i1] = bl;
        this.b[(i2 + 1) * this.c + i1 + 1] = bl;
    }

    public final void setAllExplored(boolean bl) {
        int i2 = 0;
        while (i2 < this.d) {
            int i3 = 0;
            while (i3 < this.c) {
                this.a[i2 * this.c + i3] = bl;
                ++i3;
            }
            ++i2;
        }
    }

    public final void setAllVisible(boolean bl) {
        int i2 = 0;
        while (i2 < this.d) {
            int i3 = 0;
            while (i3 < this.c) {
                this.b[i2 * this.c + i3] = bl;
                ++i3;
            }
            ++i2;
        }
    }

    public final boolean isCornerExplored(int i1, int i2) {
        return this.a[i2 * this.c + i1];
    }

    public final void setCornerExplored(int i1, int i2, boolean bl) {
        this.a[i2 * this.c + i1] = bl;
    }

    public final boolean isCornerVisible(int i1, int i2) {
        return this.b[i2 * this.c + i1];
    }

    public final void setCornerVisible(int i1, int i2, boolean bl) {
        this.b[i2 * this.c + i1] = bl;
    }

    public int getWidth() {
        return this.c - 1;
    }

    public int getHeight() {
        return this.d - 1;
    }
}


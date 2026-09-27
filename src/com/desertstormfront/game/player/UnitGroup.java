/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

import com.desertstormfront.game.model.UnitList;

public strictfp final class UnitGroup {
    private int a;
    private UnitList b;

    UnitGroup(int i1) {
        this.a = i1;
        this.b = new UnitList(64);
    }

    public int getId() {
        return this.a;
    }

    public UnitList getUnits() {
        return this.b;
    }
}


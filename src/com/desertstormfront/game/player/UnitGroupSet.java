/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

import com.desertstormfront.game.player.UnitGroup;
import java.util.ArrayList;
import java.util.List;

public strictfp final class UnitGroupSet {
    private List a;

    public UnitGroupSet() {
        this(8);
    }

    public UnitGroupSet(int i1) {
        this.a = new ArrayList(i1);
        int i2 = 0;
        while (i2 < i1) {
            this.a.add(new UnitGroup(i2));
            ++i2;
        }
    }

    public UnitGroup get(int i1) {
        return (UnitGroup)this.a.get(i1);
    }

    public int size() {
        return this.a.size();
    }

    public UnitGroup getGroup(int i1) {
        return (UnitGroup)this.a.get(i1);
    }
}


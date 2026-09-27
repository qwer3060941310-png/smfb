/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.Projectile;
import java.util.ArrayList;

public strictfp final class ProjectileList
extends ArrayList {
    public ProjectileList() {
    }

    public ProjectileList(int i1) {
        super(i1);
    }

    public final void clearAll() {
        int i1 = 0;
        while (i1 < this.size()) {
            ((Projectile)this.get(i1)).clear();
            ++i1;
        }
    }
}


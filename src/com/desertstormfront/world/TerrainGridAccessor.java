/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.desertstormfront.game.model.Domain;

public interface TerrainGridAccessor {
    public int getWidth();

    public int getHeight();

    public boolean canDomainMoveBetween(Domain var1, int var2, int var3, int var4, int var5);

    public int getMovementCost(int var1, int var2, int var3, int var4);
}


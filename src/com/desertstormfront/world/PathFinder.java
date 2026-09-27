/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.desertstormfront.app.support.ProgressCallback;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.world.Neighbor;
import com.desertstormfront.world.TerrainGridAccessor;

public interface PathFinder {
    public void build(TerrainGridAccessor var1, ProgressCallback var2);

    public Neighbor findDirection(Domain var1, int var2, int var3, int var4, int var5);
}


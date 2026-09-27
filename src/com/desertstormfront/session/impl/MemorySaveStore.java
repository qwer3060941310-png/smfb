/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.desertstormfront.game.World;
import com.desertstormfront.session.SaveGameStore;

public final class MemorySaveStore
implements SaveGameStore {
    private World world;

    @Override
    public World loadWorld() {
        return this.world;
    }

    @Override
    public boolean saveWorld(World world) {
        this.world = world;
        return true;
    }

    @Override
    public boolean hasSavedWorld() {
        return this.world != null;
    }

    @Override
    public boolean deleteSavedWorld() {
        this.world = null;
        return true;
    }
}


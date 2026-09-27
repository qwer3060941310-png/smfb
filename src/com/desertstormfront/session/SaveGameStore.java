/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session;

import com.desertstormfront.game.World;

public interface SaveGameStore {
    public World loadWorld();

    public boolean saveWorld(World var1);

    public boolean hasSavedWorld();

    public boolean deleteSavedWorld();
}


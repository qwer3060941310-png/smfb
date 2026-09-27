/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session;

import com.desertstormfront.game.World;
import com.desertstormfront.game.player.PlayerStatistics;

public interface SessionMode {
    public boolean hasSavedGame();

    public World loadWorld();

    public void saveWorld(World var1);

    public void restoreStartWorld();

    public String getVictoryMessage(PlayerStatistics var1);

    public String getDefeatMessage(PlayerStatistics var1);
}


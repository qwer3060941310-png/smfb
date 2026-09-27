/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game;

import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.player.Player;

public interface GameEventListener {
    public void onPlayerEliminated(Player var1);

    public void onUnitConstructed(Unit var1);

    public void onStructureCaptured(Unit var1);

    public void onUnitDestroyed(Unit var1, Player var2, Player var3);

    public void onUnitHit(Unit var1, Player var2);

    public void onVolleyFired(Unit var1);

    public void onFlagTaken(Player var1);

    public void onFlagLost(Player var1);

    public void onGameTick();
}


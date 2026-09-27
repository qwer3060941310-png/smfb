/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game;

import com.desertstormfront.game.GameEventListener;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.player.Player;

strictfp class NullGameEventListener
implements GameEventListener {
    NullGameEventListener() {
    }

    @Override
    public void onPlayerEliminated(Player player) {
    }

    @Override
    public void onUnitConstructed(Unit unit) {
    }

    @Override
    public void onStructureCaptured(Unit unit) {
    }

    @Override
    public void onUnitDestroyed(Unit unit, Player player, Player player2) {
    }

    @Override
    public void onUnitHit(Unit unit, Player player) {
    }

    @Override
    public void onVolleyFired(Unit unit) {
    }

    @Override
    public void onFlagTaken(Player player) {
    }

    @Override
    public void onFlagLost(Player player) {
    }

    @Override
    public void onGameTick() {
    }
}


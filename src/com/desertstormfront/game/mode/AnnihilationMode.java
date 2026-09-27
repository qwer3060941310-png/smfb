/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.mode;

import com.desertstormfront.game.mode.GameMode;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerList;
import com.noblemaster.lib.i18n.Messages;

public strictfp final class AnnihilationMode
extends GameMode {
    private long b;

    public static final AnnihilationMode create() {
        AnnihilationMode annihilationMode = new AnnihilationMode();
        annihilationMode.setHealthThreshold(2000L);
        return annihilationMode;
    }

    public long getHealthThreshold() {
        return this.b;
    }

    public void setHealthThreshold(long l1) {
        this.b = l1;
    }

    @Override
    public final boolean isObjectiveComplete() {
        int i1 = 0;
        PlayerList playerList = this.world.getPlayers();
        UnitList unitList = this.world.getUnits();
        int i4 = unitList.size();
        int i5 = 0;
        while (i5 < playerList.size()) {
            Player player = (Player)playerList.get(i5);
            if (player.isAlive()) {
                long l7 = 0L;
                int i9 = 0;
                while (i9 < i4) {
                    Unit unit = (Unit)unitList.get(i9);
                    if (unit.getOwner() == player && !unit.isDestroyed()) {
                        l7 += unit.getUnitType().getHealth();
                    }
                    ++i9;
                }
                if (l7 >= this.b) {
                    ++i1;
                }
            }
            ++i5;
        }
        return i1 <= 1;
    }

    @Override
    public final PlayerList getPlayers() {
        PlayerList playerList = new PlayerList();
        PlayerList playerList2 = this.world.getPlayers();
        UnitList unitList = this.world.getUnits();
        int i4 = unitList.size();
        int i5 = 0;
        while (i5 < playerList2.size()) {
            Player player = (Player)playerList2.get(i5);
            if (player.isAlive()) {
                long l7 = 0L;
                int i9 = 0;
                while (i9 < i4) {
                    Unit unit = (Unit)unitList.get(i9);
                    if (unit.getOwner() == player && !unit.isDestroyed()) {
                        l7 += unit.getUnitType().getHealth();
                    }
                    ++i9;
                }
                if (l7 >= this.b) {
                    playerList.add((Player)playerList2.get(i5));
                }
            }
            ++i5;
        }
        return playerList;
    }

    @Override
    public final String getName() {
        return Messages.get("Annihilation[i18n]: Annihilation");
    }

    @Override
    public final String getObjectiveText(Player player) {
        return Messages.get("AnnihilationGoalETC[i18n]: Defeat all enemy players.");
    }
}


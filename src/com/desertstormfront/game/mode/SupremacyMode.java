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

public strictfp final class SupremacyMode
extends GameMode {
    private float b;

    public static final SupremacyMode create() {
        SupremacyMode supremacyMode = new SupremacyMode();
        supremacyMode.setStructureRatio(0.7f);
        return supremacyMode;
    }

    public final float getStructureRatio() {
        return this.b;
    }

    public final void setStructureRatio(float f1) {
        this.b = f1;
    }

    @Override
    public final boolean isObjectiveComplete() {
        PlayerList playerList = this.world.getPlayers();
        int i2 = this.getRequiredStructures();
        UnitList unitList = this.world.getUnits();
        int i4 = unitList.size();
        int i5 = 0;
        while (i5 < playerList.size()) {
            Player player = (Player)playerList.get(i5);
            int i7 = 0;
            int i8 = 0;
            while (i8 < i4) {
                Unit unit = (Unit)unitList.get(i8);
                if (unit.isImmobile() && unit.getOwner() == player) {
                    ++i7;
                }
                ++i8;
            }
            if (i7 >= i2) {
                return true;
            }
            ++i5;
        }
        return false;
    }

    @Override
    public final PlayerList getPlayers() {
        PlayerList playerList = new PlayerList();
        int i2 = this.getRequiredStructures();
        PlayerList playerList2 = this.world.getPlayers();
        UnitList unitList = this.world.getUnits();
        int i5 = 0;
        while (i5 < playerList2.size()) {
            Player player = (Player)playerList2.get(i5);
            int i7 = 0;
            int i8 = 0;
            while (i8 < unitList.size()) {
                Unit unit = (Unit)unitList.get(i8);
                if (unit.isImmobile() && unit.getOwner() == player) {
                    ++i7;
                }
                ++i8;
            }
            if (i7 >= i2) {
                playerList.add(player);
            }
            ++i5;
        }
        if (playerList.size() == 0) {
            i5 = 0;
            while (i5 < playerList2.size()) {
                if (((Player)playerList2.get(i5)).isAlive()) {
                    playerList.add((Player)playerList2.get(i5));
                }
                ++i5;
            }
        }
        return playerList;
    }

    @Override
    public final String getName() {
        return Messages.get("Supremacy[i18n]: Supremacy");
    }

    @Override
    public final String getObjectiveText(Player player) {
        return Messages.format("SupremacyGoalETC[i18n]: Defeat all enemy players or take over {0} structures.", this.getRequiredStructures());
    }

    private final int getRequiredStructures() {
        UnitList unitList = this.world.getUnits();
        int i2 = unitList.size();
        int i3 = 0;
        int i4 = 0;
        while (i4 < i2) {
            if (((Unit)unitList.get(i4)).isImmobile()) {
                ++i3;
            }
            ++i4;
        }
        return Math.round((float)i3 * this.b);
    }
}


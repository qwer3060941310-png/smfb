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

public strictfp final class SurvivalMode
extends GameMode {
    private PlayerList b;
    private int c;

    public static final SurvivalMode create() {
        SurvivalMode survivalMode = new SurvivalMode();
        survivalMode.setBaseStationCount(1);
        survivalMode.setTimeLimit(1800.0f);
        return survivalMode;
    }

    public PlayerList getSurvivors() {
        return this.b;
    }

    public void setSurvivors(PlayerList playerList) {
        this.b = playerList;
    }

    public int getBaseStationCount() {
        return this.c;
    }

    public void setBaseStationCount(int i1) {
        this.c = i1;
    }

    @Override
    public final boolean isObjectiveComplete() {
        int i1 = 0;
        UnitList unitList = this.world.getUnits();
        int i3 = 0;
        while (i3 < unitList.size()) {
            Unit unit = (Unit)unitList.get(i3);
            if (unit.getUnitType() == this.world.getMapDefinition().getUnitTypeSlots().getHumvee().getProducer() && unit.getOwner() != null && this.b.contains(unit.getOwner())) {
                ++i1;
            }
            ++i3;
        }
        return i1 < this.c;
    }

    @Override
    public final PlayerList getPlayers() {
        PlayerList playerList = new PlayerList();
        int i2 = 0;
        UnitList unitList = this.world.getUnits();
        int n = 0;
        while (n < unitList.size()) {
            Unit unit = (Unit)unitList.get(n);
            if (unit.getUnitType() == this.world.getMapDefinition().getUnitTypeSlots().getHumvee().getProducer() && unit.getOwner() != null && this.b.contains(unit.getOwner())) {
                ++i2;
            }
            ++n;
        }
        if (i2 < this.c) {
            PlayerList playerList2 = this.world.getPlayers();
            int n2 = 0;
            while (n2 < playerList2.size()) {
                if (!this.b.contains(playerList2.get(n2)) && ((Player)playerList2.get(n2)).isAlive()) {
                    playerList.add((Player)playerList2.get(n2));
                }
                ++n2;
            }
        } else {
            n = 0;
            while (n < this.b.size()) {
                if (((Player)this.b.get(n)).isAlive()) {
                    playerList.add((Player)this.b.get(n));
                }
                ++n;
            }
        }
        return playerList;
    }

    @Override
    public final String getName() {
        return Messages.get("Survival[i18n]: Survival");
    }

    @Override
    public final String getObjectiveText(Player player) {
        if (this.b.contains(player)) {
            if (this.c == 0) {
                return Messages.get("SurvivalGoalETC[i18n]: Stay alive.");
            }
            if (this.c == 1) {
                return Messages.get("SurvivalGoalExtra1ETC[i18n]: Safeguard at least 1 base station.");
            }
            return Messages.format("SurvivalGoalExtraXETC[i18n]: Safeguard at least {0} base stations.", this.c);
        }
        if (this.c == 0) {
            switch (this.b.size()) {
                case 1: {
                    return Messages.format("SurvivalGoalAttacker1ETC[i18n]: Defeat {0}.", Messages.get(((Player)this.b.get(0)).getName()));
                }
                case 2: {
                    return Messages.format("SurvivalGoalAttacker2ETC[i18n]: Defeat {0} and {1}.", Messages.get(((Player)this.b.get(0)).getName()), Messages.get(((Player)this.b.get(1)).getName()));
                }
                case 3: {
                    return Messages.format("SurvivalGoalAttacker3ETC[i18n]: Defeat {0}, {1} and {2}.", Messages.get(((Player)this.b.get(0)).getName()), Messages.get(((Player)this.b.get(1)).getName()), Messages.get(((Player)this.b.get(2)).getName()));
                }
                case 4: {
                    return Messages.format("SurvivalGoalAttacker4ETC[i18n]: Defeat {0}, {1}, {2} and {3}.", Messages.get(((Player)this.b.get(0)).getName()), Messages.get(((Player)this.b.get(1)).getName()), Messages.get(((Player)this.b.get(2)).getName()), Messages.get(((Player)this.b.get(3)).getName()));
                }
            }
            return "ERROR: Invalid number of survivors: " + this.b.size();
        }
        switch (this.b.size()) {
            case 1: {
                return Messages.format("SurvivalGoalAttacker1ExtraETC[i18n]: Defeat {0} and leave them with no more than {1} base stations.", Messages.get(((Player)this.b.get(0)).getName()), this.c - 1);
            }
            case 2: {
                return Messages.format("SurvivalGoalAttacker2ExtraETC[i18n]: Defeat {0} and {1} and leave them with no more than {2} base stations.", Messages.get(((Player)this.b.get(0)).getName()), Messages.get(((Player)this.b.get(1)).getName()), this.c - 1);
            }
            case 3: {
                return Messages.format("SurvivalGoalAttacker3ExtraETC[i18n]: Defeat {0}, {1} and {2} and leave them with no more than {3} base stations.", Messages.get(((Player)this.b.get(0)).getName()), Messages.get(((Player)this.b.get(1)).getName()), Messages.get(((Player)this.b.get(2)).getName()), this.c - 1);
            }
            case 4: {
                return Messages.format("SurvivalGoalAttacker4ExtraETC[i18n]: Defeat {0}, {1}, {2} and {3} and leave them with no more than {4} base stations.", Messages.get(((Player)this.b.get(0)).getName()), Messages.get(((Player)this.b.get(1)).getName()), Messages.get(((Player)this.b.get(2)).getName()), Messages.get(((Player)this.b.get(3)).getName()), this.c - 1);
            }
        }
        return "ERROR: Invalid number of survivors: " + this.b.size();
    }
}


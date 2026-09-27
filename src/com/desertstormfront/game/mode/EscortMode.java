/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.mode;

import com.desertstormfront.game.mode.GameMode;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerList;
import com.desertstormfront.world.Vec2;
import com.noblemaster.lib.i18n.Messages;

public strictfp final class EscortMode
extends GameMode {
    private PlayerList b;
    private int c;
    private Vec2 d;

    public static final EscortMode create() {
        EscortMode escortMode = new EscortMode();
        return escortMode;
    }

    public PlayerList getEscortPlayers() {
        return this.b;
    }

    public void setEscortPlayers(PlayerList playerList) {
        this.b = playerList;
    }

    public int getTruckCount() {
        return this.c;
    }

    public void setTruckCount(int i1) {
        this.c = i1;
    }

    public Vec2 getTargetArea() {
        return this.d;
    }

    public void setTargetArea(Vec2 vec2) {
        this.d = vec2;
    }

    @Override
    public final boolean isObjectiveComplete() {
        float f1 = 3.61f;
        int i2 = 0;
        int i3 = 0;
        UnitList unitList = this.world.getUnits();
        int i5 = unitList.size();
        int i6 = 0;
        while (i6 < i5) {
            Unit unit = (Unit)unitList.get(i6);
            if (unit.getUnitType().isTruck()) {
                ++i2;
                if (unit.getPosition().distanceSquaredTo(this.d) < f1) {
                    ++i3;
                }
            }
            ++i6;
        }
        return i2 < this.c || i3 >= this.c;
    }

    @Override
    public final PlayerList getPlayers() {
        PlayerList playerList = new PlayerList();
        float f2 = 6.25f;
        int i3 = 0;
        int i4 = 0;
        UnitList unitList = this.world.getUnits();
        int i6 = unitList.size();
        int n = 0;
        while (n < i6) {
            Unit unit = (Unit)unitList.get(n);
            if (unit.getUnitType().isTruck()) {
                ++i3;
                if (unit.getPosition().distanceSquaredTo(this.d) < f2) {
                    ++i4;
                }
            }
            ++n;
        }
        if (i3 < this.c) {
            PlayerList playerList2 = this.world.getPlayers();
            int n2 = 0;
            while (n2 < playerList2.size()) {
                if (!this.b.contains(playerList2.get(n2))) {
                    playerList.add((Player)playerList2.get(n2));
                }
                ++n2;
            }
        } else if (i4 >= this.c) {
            playerList.addAll(this.b);
        } else if (this.world.getPlayers().countActiveSides() == 1) {
            PlayerList playerList3 = this.world.getPlayers();
            int n3 = 0;
            while (n3 < playerList3.size()) {
                if (((Player)playerList3.get(n3)).isAlive()) {
                    playerList.add((Player)playerList3.get(n3));
                }
                ++n3;
            }
        } else {
            PlayerList playerList4 = this.world.getPlayers();
            int n4 = 0;
            while (n4 < playerList4.size()) {
                if (!this.b.contains(playerList4.get(n4)) && ((Player)playerList4.get(n4)).isAlive()) {
                    playerList.add((Player)playerList4.get(n4));
                }
                ++n4;
            }
        }
        return playerList;
    }

    @Override
    public final String getName() {
        return Messages.get("Escort[i18n]: Escort");
    }

    @Override
    public final String getObjectiveText(Player player) {
        if (this.b.contains(player)) {
            return Messages.format("EscortGoalTeamTruckETC[i18n]: Escort {0} trucks to the target area (blue) or destroy all enemies.", this.c);
        }
        return Messages.format("EscortGoalTeamEnemyETC[i18n]: Do not allow more than {0} trucks to reach the target area (blue) or destroy all enemies.", this.c);
    }
}


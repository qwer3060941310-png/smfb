/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.Team;
import java.util.ArrayList;
import java.util.Collection;

public final class PlayerList
extends ArrayList {
    public PlayerList() {
    }

    public PlayerList(Collection collection) {
        super(collection);
    }

    public final int countAlive() {
        int i1 = 0;
        int i2 = 0;
        while (i2 < this.size()) {
            if (((Player)this.get(i2)).isAlive()) {
                ++i1;
            }
            ++i2;
        }
        return i1;
    }

    public final boolean hasTeams() {
        return this.size() > 0 ? ((Player)this.get(0)).getTeam() != null : false;
    }

    public final int countAliveTeams() {
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        while (i3 < this.size()) {
            Team team;
            if (((Player)this.get(i3)).isAlive() && (team = ((Player)this.get(i3)).getTeam()) != null) {
                i2 |= 1 << team.ordinal();
            }
            ++i3;
        }
        i3 = 0;
        while (i3 < Team.values().length) {
            if ((i2 & 1) != 0) {
                ++i1;
            }
            i2 >>= 1;
            ++i3;
        }
        return i1;
    }

    public final int countActiveSides() {
        if (this.hasTeams()) {
            return this.countAliveTeams();
        }
        return this.countAlive();
    }

    public Player getById(int i1) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((Player)this.get(i2)).getId() == i1) {
                return (Player)this.get(i2);
            }
            ++i2;
        }
        return null;
    }
}


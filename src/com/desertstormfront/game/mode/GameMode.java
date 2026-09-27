/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.mode;

import com.desertstormfront.app.support.TextFormatter;
import com.desertstormfront.game.World;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerList;
import com.noblemaster.lib.i18n.Messages;
import java.io.Serializable;

public strictfp abstract class GameMode
implements Serializable {
    protected World world;
    private float timeLimit;

    public void setWorld(World world) {
        this.world = world;
    }

    public float getTimeLimit() {
        return this.timeLimit;
    }

    public void setTimeLimit(float f1) {
        this.timeLimit = f1;
    }

    public boolean hasTimeLimit() {
        return this.timeLimit > 0.0f;
    }

    public final boolean isGameOver() {
        if (this.timeLimit > 0.0f && this.world.getGameTime() >= this.timeLimit) {
            return true;
        }
        if (this.world.getPlayers().countActiveSides() <= 1) {
            return true;
        }
        return this.isObjectiveComplete();
    }

    public abstract boolean isObjectiveComplete();

    public abstract PlayerList getPlayers();

    public abstract String getName();

    protected abstract String getObjectiveText(Player var1);

    public String getObjective(Player player) {
        String string = this.getObjectiveText(player);
        if (this.hasTimeLimit()) {
            string = String.valueOf(string) + Messages.getWordSeparator() + Messages.format("TimeLimitETC[i18n]: The time limit is {0} minutes.", TextFormatter.formatTime(this.getTimeLimit()));
        }
        return string;
    }

    public final String toString() {
        return this.getName();
    }
}


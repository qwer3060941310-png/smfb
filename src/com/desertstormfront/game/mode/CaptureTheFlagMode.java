/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.mode;

import com.desertstormfront.game.mode.GameMode;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerList;
import com.noblemaster.lib.i18n.Messages;

public strictfp final class CaptureTheFlagMode
extends GameMode {
    private Unit b;
    private float c;
    private float d;

    public static final CaptureTheFlagMode create() {
        CaptureTheFlagMode captureTheFlagMode = new CaptureTheFlagMode();
        captureTheFlagMode.setHoldSeconds(600.0f);
        return captureTheFlagMode;
    }

    public final Unit getFlag() {
        return this.b;
    }

    public final void setFlag(Unit unit) {
        this.b = unit;
    }

    public final float getHoldSeconds() {
        return this.c;
    }

    public final void setHoldSeconds(float f1) {
        this.c = f1;
    }

    public final float getHoldStartTime() {
        return this.d;
    }

    public final void setHoldStartTime(float f1) {
        this.d = f1;
    }

    @Override
    public final boolean isObjectiveComplete() {
        return this.b.getOwner() != null && this.world.getGameTime() - this.d >= this.c;
    }

    @Override
    public final PlayerList getPlayers() {
        PlayerList playerList = new PlayerList();
        if (this.b.getOwner() != null) {
            Player player = this.b.getOwner();
            PlayerList playerList2 = this.world.getPlayers();
            int i4 = 0;
            while (i4 < playerList2.size()) {
                if (!player.isEnemyOf((Player)playerList2.get(i4))) {
                    playerList.add((Player)playerList2.get(i4));
                }
                ++i4;
            }
        } else {
            PlayerList playerList3 = this.world.getPlayers();
            int n = 0;
            while (n < playerList3.size()) {
                if (((Player)playerList3.get(n)).isAlive()) {
                    playerList.add((Player)playerList3.get(n));
                }
                ++n;
            }
        }
        return playerList;
    }

    @Override
    public final String getName() {
        return Messages.get("CaptureTheFlag[i18n]: Capture the Flag");
    }

    @Override
    public final String getObjectiveText(Player player) {
        return Messages.format("CaptureTheFlagGoalETC[i18n]: Hold the flag for {0} seconds.", (int)this.c);
    }
}


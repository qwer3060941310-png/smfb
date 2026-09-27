/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.config.UserConfig;
import com.desertstormfront.game.GameEventListener;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.Volley;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.screen.GameScreen;

class GameEventSoundListener
implements GameEventListener {
    final /* synthetic */ GameScreen screen;

    GameEventSoundListener(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    @Override
    public void onPlayerEliminated(Player player) {
        if (GameScreen.getUnitCommander(this.screen).getWorld().getPlayers().countAlive() >= 2 && UserConfig.isRenderDetails()) {
            this.screen.getAudio().playPlayerEliminated();
        }
    }

    @Override
    public void onUnitConstructed(Unit unit) {
        if (unit.getOwner() == GameScreen.getUnitCommander(this.screen).getPlayer()) {
            this.screen.getAudio().playUnitConstructed();
        }
    }

    @Override
    public void onStructureCaptured(Unit unit) {
        if (unit.getOwner() == GameScreen.getUnitCommander(this.screen).getPlayer()) {
            this.screen.getAudio().playStructureCaptured();
        }
    }

    @Override
    public void onUnitDestroyed(Unit unit, Player player, Player player2) {
        Player player3 = GameScreen.getUnitCommander(this.screen).getPlayer();
        if (unit.isImmobile()) {
            if (player2 == player3) {
                this.screen.getAudio().playStructureLost();
            }
        } else if (unit.getOwner() == player3 || player2 == player3 || player == player3) {
            this.screen.getAudio().playExplosion();
            if (unit.getOwner() == player3 && UserConfig.isRenderDetails()) {
                this.screen.getAudio().playUnitLost();
            }
        }
    }

    @Override
    public void onUnitHit(Unit unit, Player player) {
        Player player2 = GameScreen.getUnitCommander(this.screen).getPlayer();
        if (unit.getOwner() == player2 && UserConfig.isRenderDetails()) {
            this.screen.getAudio().playUnderAttack(GameScreen.getUnitCommander(this.screen).getWorld().getGameTime());
        }
        if (unit.getOwner() == player2 || player == player2) {
            this.screen.getAudio().playImpact();
        }
    }

    @Override
    public void onVolleyFired(Unit unit) {
        Volley volley = unit.getActiveVolley();
        Player player = GameScreen.getUnitCommander(this.screen).getPlayer();
        if (unit.getOwner() == player || volley != null && volley.getTargetUnit() != null && volley.getTargetUnit().getOwner() == player) {
            this.screen.getAudio().playProjectile(volley.getAmmoType());
        }
    }

    @Override
    public void onFlagTaken(Player player) {
        this.screen.getAudio().playFlagTaken();
    }

    @Override
    public void onFlagLost(Player player) {
        this.screen.getAudio().playFlagLost();
    }

    @Override
    public void onGameTick() {
        GameScreen.setLastTickTime(this.screen, GameScreen.getGameTimeNanos(this.screen));
        if (UserConfig.isRenderDetails() && GameScreen.getUnitCommander(this.screen).getPlayer().getIncomePerBuilding() > 0L && GameScreen.getUnitCommander(this.screen).getWorld().getUnits().hasBuilding(GameScreen.getUnitCommander(this.screen).getPlayer())) {
            this.screen.getAudio().playIncome();
        }
    }
}

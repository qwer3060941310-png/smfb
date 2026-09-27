/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.desertstormfront.game.World;
import com.desertstormfront.game.player.Difficulty;
import com.desertstormfront.game.player.PlayerStatistics;
import com.desertstormfront.io.WorldSerializer;
import com.desertstormfront.session.SaveGameStore;
import com.desertstormfront.session.SessionMode;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.io.GameFile;

public final class TutorialGameMode
implements SessionMode {
    private SaveGameStore saveStore;
    private boolean started;

    public TutorialGameMode(GameFile gameFile, SaveGameStore saveGameStore) {
        this.saveStore = saveGameStore;
        this.e();
    }

    private void e() {
    }

    private void f() {
    }

    public void createStartWorld() {
        this.started = true;
        this.f();
        String string = "tutorial_001.world";
        World world = WorldSerializer.load(string);
        world.activateHumanPlayer();
        world.setupDefaultPlayers(Difficulty.Normal);
        this.saveStore.saveWorld(world);
    }

    @Override
    public boolean hasSavedGame() {
        return this.saveStore.hasSavedWorld();
    }

    @Override
    public World loadWorld() {
        return this.saveStore.loadWorld();
    }

    @Override
    public void saveWorld(World world) {
        if (this.saveStore.hasSavedWorld()) {
            this.saveStore.saveWorld(world);
        }
    }

    @Override
    public void restoreStartWorld() {
        this.createStartWorld();
    }

    @Override
    public String getVictoryMessage(PlayerStatistics playerStatistics) {
        this.saveStore.deleteSavedWorld();
        return Messages.get("TutorialSuccessETC[i18n]: You have successfully completed the tutorial. Good luck with your future endeavors!");
    }

    @Override
    public String getDefeatMessage(PlayerStatistics playerStatistics) {
        this.saveStore.deleteSavedWorld();
        return Messages.get("TutorialFailureETC[i18n]: Oh-Oh, you have failed the tutorial! Better try again?");
    }
}


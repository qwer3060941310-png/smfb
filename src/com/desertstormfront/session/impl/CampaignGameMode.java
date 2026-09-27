/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.badlogic.gdx.files.FileHandle;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.World;
import com.desertstormfront.game.player.Difficulty;
import com.desertstormfront.game.player.PlayerStatistics;
import com.desertstormfront.io.WorldSerializer;
import com.desertstormfront.session.SaveGameStore;
import com.desertstormfront.session.SessionMode;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import com.noblemaster.lib.io.stream.impl.StreamDataReader;
import com.noblemaster.lib.io.stream.impl.StreamDataWriter;
import com.noblemaster.lib.log.OsfLog;
import java.io.IOException;

/**
 * Campaign session mode: carries the player's campaign progress (which mission, how many attempts,
 * score and best score) and turns a mission selection into a playable {@link com.desertstormfront.game.World}.
 *
 * <p>Progress is persisted through a {@link SaveGameStore}, so a campaign survives restarts;
 * {@link #loadProgress}/{@link #saveProgress} are the only IO this class performs. The selected
 * mission's world is built by {@link #createMissionWorld}.
 */
public final class CampaignGameMode
implements SessionMode {
    private GameFile campaignFile;
    private SaveGameStore saveStore;
    private boolean started;
    private Difficulty difficulty;
    private int currentMission;
    private int attemptCount;
    private int selectedMission;
    private int score;
    private int bestScore;

    public CampaignGameMode(GameFile gameFile, SaveGameStore saveGameStore) {
        this.campaignFile = gameFile;
        this.saveStore = saveGameStore;
        this.loadProgress();
    }

    private boolean p() {
        return GameConfig.isLiteMode() || GameConfig.isMacBuild();
    }

    public int getMissionCount() {
        if (this.p()) {
            if (GameConfig.isMacBuild()) {
                return 1;
            }
            return GameConfig.getCampaignMissionIndices().length;
        }
        return this.getTotalMissions();
    }

    public int getTotalMissions() {
        return GameConfig.getMissionDescriptions().length;
    }

    public String getMissionDescription(int i1) {
        return Messages.get(GameConfig.getMissionDescriptions()[this.p() ? GameConfig.getCampaignMissionIndices()[i1 - 1] - 1 : i1 - 1]);
    }

    public boolean isStarted() {
        return this.started;
    }

    public String getCampaignStory() {
        return Messages.format(GameConfig.getCampaignStory(), this.score);
    }

    public boolean isComplete() {
        return this.currentMission > this.getMissionCount();
    }

    public String getCampaignCompleteText() {
        return Messages.format(GameConfig.getCampaignCompleteText(), this.score);
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public int getMissionNumber() {
        return this.currentMission;
    }

    public int getSelectedMission() {
        return this.selectedMission;
    }

    public int getScore() {
        return this.score;
    }

    public int getHighScore() {
        return this.bestScore;
    }

    private void loadProgress() {
        block15: {
            DataReader dataReader = null;
            try {
                try {
                    FileHandle fileHandle = this.campaignFile.getFileHandle();
                    if (fileHandle.exists()) {
                        dataReader = new StreamDataReader(fileHandle.read());
                        int i3 = dataReader.readInt();
                        this.started = dataReader.readBoolean();
                        this.difficulty = Difficulty.values()[dataReader.readInt()];
                        this.currentMission = dataReader.readInt();
                        this.attemptCount = dataReader.readInt();
                        this.score = dataReader.readInt();
                        this.bestScore = dataReader.readInt();
                        this.selectedMission = i3 >= 2 ? dataReader.readInt() : this.currentMission;
                    } else {
                        this.started = false;
                        this.difficulty = Difficulty.Normal;
                        this.currentMission = 1;
                        this.attemptCount = 0;
                        this.score = 0;
                        this.bestScore = 0;
                        this.selectedMission = 1;
                    }
                }
                catch (Exception exception) {
                    OsfLog.error("Error loading from file: " + exception);
                    OsfLog.logException(exception);
                    this.started = false;
                    this.difficulty = Difficulty.Normal;
                    this.currentMission = 1;
                    this.attemptCount = 0;
                    this.score = 0;
                    this.bestScore = 0;
                    this.selectedMission = 1;
                    if (dataReader == null) break block15;
                        dataReader.close();
                    dataReader = null;
                    break block15;
                }
            }
            catch (Throwable throwable) {
                if (dataReader != null) {
                        dataReader.close();
                    dataReader = null;
                }
                throw throwable;
            }
            if (dataReader != null) {
                    dataReader.close();
                dataReader = null;
            }
        }
    }

    private void saveProgress() {
        block13: {
            DataWriter dataWriter = null;
            try {
                try {
                    FileHandle fileHandle = this.campaignFile.getFileHandle();
                    dataWriter = new StreamDataWriter(fileHandle.write(false));
                    dataWriter.writeInt(2);
                    dataWriter.writeBoolean(this.started);
                    dataWriter.writeInt(this.difficulty.ordinal());
                    dataWriter.writeInt(this.currentMission);
                    dataWriter.writeInt(this.attemptCount);
                    dataWriter.writeInt(this.score);
                    dataWriter.writeInt(this.bestScore);
                    dataWriter.writeInt(this.selectedMission);
                }
                catch (Exception exception) {
                    OsfLog.error("Error saving to file: " + exception);
                    OsfLog.logException(exception);
                    if (dataWriter == null) break block13;
                        dataWriter.close();
                    dataWriter = null;
                    break block13;
                }
            }
            catch (Throwable throwable) {
                if (dataWriter != null) {
                        dataWriter.close();
                    dataWriter = null;
                }
                throw throwable;
            }
            if (dataWriter != null) {
                    dataWriter.close();
                dataWriter = null;
            }
        }
    }

    public void startCampaign(Difficulty difficulty) {
        this.started = true;
        this.difficulty = difficulty;
        this.saveProgress();
    }

    public void resetCampaign() {
        this.started = false;
        this.difficulty = Difficulty.Normal;
        this.currentMission = 1;
        this.attemptCount = 0;
        this.score = 0;
        this.selectedMission = 1;
        this.saveProgress();
        this.saveStore.deleteSavedWorld();
    }

    public void selectMission(int i1) {
        this.selectedMission = i1;
        this.createMissionWorld();
    }

    private void createMissionWorld() {
        if (this.selectedMission == this.currentMission) {
            ++this.attemptCount;
        }
        this.saveProgress();
        int i1 = this.p() ? GameConfig.getCampaignMissionIndices()[this.selectedMission - 1] : this.selectedMission;
        String string = String.valueOf(i1);
        while (string.length() < 3) {
            string = "0" + string;
        }
        String string2 = "campaign_" + string + ".world";
        World world = WorldSerializer.load(string2);
        world.activateHumanPlayer();
        world.setupDefaultPlayers(this.difficulty);
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
        this.createMissionWorld();
    }

    @Override
    public String getVictoryMessage(PlayerStatistics playerStatistics) {
        int i2;
        if (this.selectedMission == this.currentMission) {
            i2 = (int)Math.round((double)((this.difficulty.ordinal() + 1) * 500) / Math.sqrt(this.attemptCount)) + playerStatistics.getUnitsRating() * 50 + playerStatistics.getDamageRating() * 25;
            this.score += i2;
            if (this.score > this.bestScore) {
                this.bestScore = this.score;
            }
            ++this.currentMission;
            this.attemptCount = 0;
            this.saveProgress();
        } else {
            i2 = 0;
        }
        this.selectedMission = this.currentMission;
        this.saveStore.deleteSavedWorld();
        return Messages.format("CampaignSuccessETC[i18n]: You have successfully completed the mission! Points awarded: {0}.", i2);
    }

    @Override
    public String getDefeatMessage(PlayerStatistics playerStatistics) {
        this.selectedMission = this.currentMission;
        this.saveStore.deleteSavedWorld();
        return Messages.get("CampaignFailureETC[i18n]: You have failed the mission! Try again...");
    }
}


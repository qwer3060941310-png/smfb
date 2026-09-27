/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.badlogic.gdx.files.FileHandle;
import com.desertstormfront.game.World;
import com.desertstormfront.game.player.PlayerStatistics;
import com.desertstormfront.session.SaveGameStore;
import com.desertstormfront.session.StartableSessionMode;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import com.noblemaster.lib.io.stream.impl.StreamDataReader;
import com.noblemaster.lib.io.stream.impl.StreamDataWriter;
import com.noblemaster.lib.log.OsfLog;
import java.io.IOException;

public final class SkirmishGameMode
implements StartableSessionMode {
    private GameFile counterFile;
    private SaveGameStore saveStore;
    private SaveGameStore startSaveStore;
    private int gamesPlayed;
    private int wins;

    public SkirmishGameMode(GameFile gameFile, SaveGameStore saveGameStore, SaveGameStore saveGameStore2) {
        this.counterFile = gameFile;
        this.saveStore = saveGameStore;
        this.startSaveStore = saveGameStore2;
        this.loadCounters();
    }

    public int getGamesPlayed() {
        return this.gamesPlayed;
    }

    public int getWins() {
        return this.wins;
    }

    private void loadCounters() {
        block15: {
            DataReader dataReader = null;
            try {
                try {
                    FileHandle fileHandle = this.counterFile.getFileHandle();
                    if (fileHandle.exists()) {
                        dataReader = new StreamDataReader(fileHandle.read());
                        dataReader.readInt();
                        this.gamesPlayed = dataReader.readInt();
                        this.wins = dataReader.readInt();
                    } else {
                        this.gamesPlayed = 0;
                        this.wins = 0;
                    }
                }
                catch (Exception exception) {
                    OsfLog.error("Error loading from file: " + exception);
                    OsfLog.logException(exception);
                    this.gamesPlayed = 0;
                    this.wins = 0;
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

    private void saveCounters() {
        block13: {
            DataWriter dataWriter = null;
            try {
                try {
                    FileHandle fileHandle = this.counterFile.getFileHandle();
                    dataWriter = new StreamDataWriter(fileHandle.write(false));
                    dataWriter.writeInt(1);
                    dataWriter.writeInt(this.gamesPlayed);
                    dataWriter.writeInt(this.wins);
                }
                catch (Exception exception) {
                    OsfLog.info("Error saving to file: " + exception);
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

    @Override
    public void startSession(World world) {
        ++this.gamesPlayed;
        this.saveCounters();
        this.saveStore.saveWorld(world);
        this.startSaveStore.saveWorld(world);
    }

    @Override
    public void clearSaves() {
        this.saveStore.deleteSavedWorld();
        this.startSaveStore.deleteSavedWorld();
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
        this.saveStore.saveWorld(this.startSaveStore.loadWorld());
    }

    @Override
    public String getVictoryMessage(PlayerStatistics playerStatistics) {
        ++this.wins;
        this.saveCounters();
        this.saveStore.deleteSavedWorld();
        this.startSaveStore.deleteSavedWorld();
        return Messages.get("SkirmishSuccessETC[i18n]: You have successfully completed the objective. Good luck with your future endeavors!");
    }

    @Override
    public String getDefeatMessage(PlayerStatistics playerStatistics) {
        this.saveStore.deleteSavedWorld();
        this.startSaveStore.deleteSavedWorld();
        return Messages.get("SkirmishFailureETC[i18n]: You have been defeated!");
    }
}


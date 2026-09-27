/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.badlogic.gdx.files.FileHandle;
import com.desertstormfront.game.World;
import com.desertstormfront.game.player.PlayerStatistics;
import com.desertstormfront.session.SaveGameStore;
import com.desertstormfront.session.StartableSessionMode;
import com.desertstormfront.session.impl.GameClient;
import com.desertstormfront.session.impl.GameServer;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import com.noblemaster.lib.io.stream.impl.StreamDataReader;
import com.noblemaster.lib.io.stream.impl.StreamDataWriter;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.net.match.AddressProvider;
import com.noblemaster.lib.net.match.MatchCodec;
import com.noblemaster.lib.net.match.MatchRecord;
import com.noblemaster.lib.net.match.PortMappingCallback;
import java.io.IOException;

public strictfp final class MultiplayerGameMode
implements StartableSessionMode {
    private GameFile gameFile;
    private SaveGameStore saveStore;
    private MatchRecord matchRecord;
    private GameClient client;
    private GameServer server;

    public MultiplayerGameMode(GameFile gameFile, SaveGameStore saveGameStore) {
        this.gameFile = gameFile;
        this.saveStore = saveGameStore;
        this.loadMatchRecord();
    }

    public GameClient getClient() {
        return this.client;
    }

    public GameServer getServer() {
        return this.server;
    }

    public MatchRecord getMatchRecord() {
        return this.matchRecord;
    }

    private void loadMatchRecord() {
        block15: {
            DataReader dataReader = null;
            try {
                try {
                    FileHandle fileHandle = this.gameFile.getFileHandle();
                    if (fileHandle.exists()) {
                        dataReader = new StreamDataReader(fileHandle.read());
                        dataReader.readInt();
                        this.matchRecord = MatchCodec.read(dataReader);
                    } else {
                        this.matchRecord = null;
                    }
                }
                catch (Exception exception) {
                    OsfLog.error("Error loading from file: " + exception);
                    OsfLog.logException(exception);
                    this.matchRecord = null;
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

    private void saveMatchRecord() {
        block13: {
            DataWriter dataWriter = null;
            try {
                try {
                    FileHandle fileHandle = this.gameFile.getFileHandle();
                    dataWriter = new StreamDataWriter(fileHandle.write(false));
                    dataWriter.writeInt(1);
                    MatchCodec.write(dataWriter, this.matchRecord);
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

    public void startFromSave() {
        this.startSession(this.saveStore.loadWorld());
    }

    @Override
    public void startSession(World world) {
        if (this.getServer() != null) {
            throw new RuntimeException("Cannot host new game: server is still running.");
        }
        this.server = GameServer.create(world);
    }

    public void joinGame(AddressProvider addressProvider, MatchRecord matchRecord) {
        this.matchRecord = matchRecord;
        this.client = GameClient.create(addressProvider, matchRecord);
    }

    @Override
    public void clearSaves() {
        this.saveStore.deleteSavedWorld();
        this.matchRecord = null;
        this.saveMatchRecord();
    }

    public void hostGame(AddressProvider addressProvider, PortMappingCallback portMappingCallback) {
        try {
            if (this.server != null) {
                this.server.start(addressProvider, portMappingCallback);
                this.matchRecord = this.server.getMatchRecord();
                this.client = GameClient.create(addressProvider, this.matchRecord);
            }
            this.client.start();
        }
        catch (IOException iOException) {
            this.disconnect();
            throw new RuntimeException(iOException);
        }
        this.saveStore.saveWorld(this.client.getWorld());
        this.saveMatchRecord();
    }

    public void disconnect() {
        if (this.client != null) {
            this.client.disconnect();
            this.client = null;
        }
        if (this.server != null) {
            this.server.stop();
            this.server = null;
        }
    }

    @Override
    public boolean hasSavedGame() {
        return this.saveStore.hasSavedWorld();
    }

    @Override
    public World loadWorld() {
        return this.client.getWorld();
    }

    @Override
    public void saveWorld(World world) {
        if (this.saveStore.hasSavedWorld()) {
            this.saveStore.saveWorld(world);
        }
    }

    @Override
    public void restoreStartWorld() {
        throw new RuntimeException("Internal error. User needs to be redirected to lobby.");
    }

    @Override
    public String getVictoryMessage(PlayerStatistics playerStatistics) {
        this.saveStore.deleteSavedWorld();
        this.matchRecord = null;
        this.saveMatchRecord();
        return Messages.get("MultiplayerSuccessETC[i18n]: You have successfully defeated your opponents. Good luck with your future endeavors!");
    }

    @Override
    public String getDefeatMessage(PlayerStatistics playerStatistics) {
        this.saveStore.deleteSavedWorld();
        this.matchRecord = null;
        this.saveMatchRecord();
        return Messages.get("MultiplayerFailureETC[i18n]: You have been defeated by a better skilled opponent!");
    }
}


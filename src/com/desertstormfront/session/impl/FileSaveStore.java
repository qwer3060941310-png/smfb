/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session.impl;

import com.badlogic.gdx.files.FileHandle;
import com.desertstormfront.game.World;
import com.desertstormfront.io.WorldSerializer;
import com.desertstormfront.session.SaveGameStore;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import com.noblemaster.lib.io.stream.impl.StreamDataReader;
import com.noblemaster.lib.io.stream.impl.StreamDataWriter;
import com.noblemaster.lib.log.OsfLog;
import java.io.IOException;

public final class FileSaveStore
implements SaveGameStore {
    private GameFile saveFile;

    public FileSaveStore(GameFile gameFile) {
        this.saveFile = gameFile;
    }

    @Override
    public World loadWorld() {
        World world;
        DataReader dataReader = null;
        try {
            FileHandle fileHandle = this.saveFile.getFileHandle();
            dataReader = new StreamDataReader(fileHandle.read());
            world = WorldSerializer.read(dataReader);
            return world;
        }
        finally {
            if (dataReader != null) {
                    dataReader.close();
                dataReader = null;
            }
        }
    }

    @Override
    public boolean saveWorld(World world) {
        DataWriter dataWriter = null;
        try {
            FileHandle fileHandle = this.saveFile.getFileHandle();
            dataWriter = new StreamDataWriter(fileHandle.write(false));
            WorldSerializer.write(dataWriter, world);
            return true;
        }
        finally {
            if (dataWriter != null) {
                    dataWriter.close();
                dataWriter = null;
            }
        }
    }

    @Override
    public boolean hasSavedWorld() {
        return this.saveFile.getFileHandle().exists();
    }

    @Override
    public boolean deleteSavedWorld() {
        return this.saveFile.getFileHandle().delete();
    }
}


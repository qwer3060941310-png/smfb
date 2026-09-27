/*
 * Resolves relative resource names against the two roots the game uses: the bundled data/
 * directory (internal) and the writable user directory (external).
 *
 * Deobfuscation: the field a became externalDir and a(String)/b(String)/a() became
 * getInternal/getExternal/isExternalStorageAvailable. Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.io;

import com.noblemaster.lib.io.FileLocation;
import com.noblemaster.lib.io.GameFile;

public class DataFileResolver {
    private String externalDir;

    public DataFileResolver(String string) {
        this.externalDir = string;
    }

    public GameFile getInternal(String string) {
        return new GameFile(FileLocation.INTERNAL, string);
    }

    public GameFile getExternal(String string) {
        return new GameFile(FileLocation.EXTERNAL, String.valueOf(this.externalDir) + string);
    }

    public boolean isExternalStorageAvailable() {
        return GameFile.isExternalStorageAvailable();
    }
}


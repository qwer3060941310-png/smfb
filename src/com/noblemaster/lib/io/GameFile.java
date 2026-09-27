/*
 * One resource path plus where it lives (bundled data/, classpath or the external user directory).
 * Every resource the game loads goes through this class, including the v5 mod override below.
 *
 * Deobfuscation: the fields a/b became location/name, and a()/b()/c()/d()/e() became
 * exists/getFileType/getPath/getFileHandle/isExternalStorageAvailable - the last two reuse
 * libGDX's own vocabulary. Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.io;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.noblemaster.lib.io.FileLocation;

public class GameFile {
    private FileLocation location;
    private String name;

    GameFile(FileLocation fileLocation, String string) {
        this.location = fileLocation;
        this.name = string;
        if (this.name.indexOf(92) >= 0) {
            this.name.replace('\\', '/');
        }
    }

    public boolean exists() {
        return this.getFileHandle().exists();
    }

    public Files.FileType getFileType() {
        // v5 resource mod: an override under mod/data/<name> always wins and is absolute.
        if (modFile(this.name) != null) {
            return Files.FileType.Absolute;
        }
        if (this.location == FileLocation.INTERNAL) {
            String string = System.getProperty("getdown.runner.install.path");
            if (string != null) {
                return Files.FileType.Absolute;
            }
            return Files.FileType.Internal;
        }
        if (this.location == FileLocation.CLASSPATH) {
            return Files.FileType.Classpath;
        }
        if (this.location == FileLocation.EXTERNAL) {
            return Files.FileType.External;
        }
        throw new RuntimeException("File type not supported: " + (Object)((Object)this.location));
    }

    public String getPath() {
        java.io.File mod = modFile(this.name);
        if (mod != null) {
            return mod.getAbsolutePath();
        }
        if (this.location == FileLocation.INTERNAL) {
            String string = System.getProperty("getdown.runner.install.path");
            if (string != null) {
                return String.valueOf(string) + "data/" + this.name;
            }
            return "data/" + this.name;
        }
        if (this.location == FileLocation.CLASSPATH) {
            return this.name;
        }
        if (this.location == FileLocation.EXTERNAL) {
            return this.name;
        }
        throw new RuntimeException("File type not supported: " + (Object)((Object)this.location));
    }

    public FileHandle getFileHandle() {
        java.io.File mod = modFile(this.name);
        if (mod != null) {
            return Gdx.files.absolute(mod.getAbsolutePath());
        }
        if (this.location == FileLocation.INTERNAL) {
            String string = System.getProperty("getdown.runner.install.path");
            if (string != null) {
                return Gdx.files.absolute(String.valueOf(string) + "data/" + this.name);
            }
            return Gdx.files.internal("data/" + this.name);
        }
        if (this.location == FileLocation.CLASSPATH) {
            return Gdx.files.classpath(this.name);
        }
        if (this.location == FileLocation.EXTERNAL) {
            return Gdx.files.external(this.name);
        }
        throw new RuntimeException("File type not supported: " + (Object)((Object)this.location));
    }

    static boolean isExternalStorageAvailable() {
        return Gdx.files.isExternalStorageAvailable();
    }

    /**
     * v5 resource mod: returns mod/data/<name> when it exists, otherwise null.
     * Placing the lookup here makes the override apply to every resource type at once
     * (sprites, audio, maps, i18n properties, icons) since all of them resolve via GameFile.
     * Uses plain java.io so it stays safe before/independent of extra Gdx state.
     */
    private static final java.util.Set<String> LOGGED = new java.util.HashSet<String>();

    private static java.io.File modFile(String name) {
        try {
            java.io.File file = new java.io.File("mod" + java.io.File.separator + "data" + java.io.File.separator + name);
            if (!file.exists()) {
                return null;
            }
            synchronized (LOGGED) {
                if (LOGGED.add(name)) {
                    System.out.println("[GameFile] mod override: " + name);
                }
            }
            return file;
        } catch (Throwable t) {
            return null;
        }
    }
}


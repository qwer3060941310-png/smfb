/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.log.OsfLog;

public final class AudioClip {
    private static float a = 1.0f;
    private Sound b;
    private Music c;

    public AudioClip(GameFile gameFile) {
        this(gameFile, true);
    }

    public AudioClip(GameFile gameFile, boolean bl) {
        if (bl) {
            try {
                this.b = Gdx.audio.newSound(gameFile.getFileHandle());
            }
            catch (Exception exception) {
                OsfLog.info("Cannot create sound: " + exception);
                OsfLog.logException(exception);
                this.b = null;
            }
            this.c = null;
        } else {
            this.b = null;
            try {
                this.c = Gdx.audio.newMusic(gameFile.getFileHandle());
                this.c.setLooping(false);
            }
            catch (Exception exception) {
                OsfLog.info("Cannot create music: " + exception);
                OsfLog.logException(exception);
                this.c = null;
            }
        }
    }

    public static final void setMasterVolume(float f0) {
        a = f0;
    }

    public final void play() {
        this.play(a);
    }

    public final void play(float f1) {
        if (f1 > 0.0f) {
            if (this.b != null) {
                try {
                    this.b.play(f1);
                }
                catch (Exception exception) {
                    OsfLog.info("Cannot play sound: " + exception);
                    OsfLog.logException(exception);
                }
            } else if (this.c != null) {
                try {
                    this.c.setVolume(f1);
                    this.c.play();
                }
                catch (Exception exception) {
                    OsfLog.info("Cannot play music: " + exception);
                    OsfLog.logException(exception);
                }
            }
        }
    }

    public final void dispose() {
        if (this.b != null) {
            try {
                this.b.stop();
                this.b.dispose();
            }
            catch (Exception exception) {
                OsfLog.info("Cannot dispose sound: " + exception);
                OsfLog.logException(exception);
            }
            this.b = null;
        } else if (this.c != null) {
            try {
                this.c.stop();
                this.c.dispose();
            }
            catch (Exception exception) {
                OsfLog.info("Cannot dispose music: " + exception);
                OsfLog.logException(exception);
            }
            this.c = null;
        }
    }
}


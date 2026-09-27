/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.math.MathHelper;
import java.util.Arrays;
import java.util.List;

public final class MusicPlaylist {
    private static float a = 0.5f;
    private List b;
    private boolean c;
    private int d;
    private Music e;
    private float f;

    public MusicPlaylist(GameFile gameFile) {
        this(Arrays.asList(gameFile));
    }

    public MusicPlaylist(List list) {
        this.b = list;
        this.c = true;
        this.d = 0;
    }

    public static final void setMasterVolume(float f0) {
        a = f0;
    }

    public final void shuffle() {
        if (this.b != null) {
            int i1 = 0;
            while (i1 < 3) {
                int i2 = 0;
                while (i2 < this.b.size()) {
                    int i3 = (int)(MathHelper.randomFloat() * (float)this.b.size());
                    GameFile gameFile = (GameFile)this.b.get(i2);
                    this.b.set(i2, (GameFile)this.b.get(i3));
                    this.b.set(i3, gameFile);
                    ++i2;
                }
                ++i1;
            }
        }
    }

    public final void resume() {
        if (this.b.size() > 0) {
            this.c = false;
            if (this.e != null) {
                try {
                    this.e.play();
                }
                catch (Exception exception) {
                    OsfLog.info("Cannot play audio: " + exception);
                    OsfLog.logException(exception);
                }
            }
        }
    }

    public final void pause() {
        this.c = true;
        if (this.e != null) {
            try {
                this.e.pause();
            }
            catch (Exception exception) {
                OsfLog.info("Cannot pause audio: " + exception);
                OsfLog.logException(exception);
            }
        }
    }

    public final void update() {
        if (!this.c) {
            if (this.e == null) {
                if (a > 0.0f) {
                    try {
                        this.e = Gdx.audio.newMusic(((GameFile)this.b.get(this.d)).getFileHandle());
                        this.e.setVolume(a);
                        this.f = a;
                        this.e.play();
                    }
                    catch (Exception exception) {
                        OsfLog.info("Cannot load music: " + this.b.get(this.d));
                        this.e = null;
                        this.d = (this.d + 1) % this.b.size();
                    }
                }
            } else if (!this.e.isPlaying()) {
                try {
                    this.e.stop();
                    this.e.dispose();
                }
                catch (Exception exception) {
                    OsfLog.info("Cannot dispose music: " + this.b.get(this.d));
                    this.e = null;
                }
                this.e = null;
                this.d = (this.d + 1) % this.b.size();
            }
        }
        if (this.e != null && this.f != a) {
            if (a == 0.0f) {
                try {
                    this.e.stop();
                    this.e.dispose();
                }
                catch (Exception exception) {
                    OsfLog.info("Error closing music player: " + exception);
                    OsfLog.logException(exception);
                }
                this.e = null;
            } else {
                this.e.setVolume(a);
                this.f = a;
            }
        }
    }

    public final void dispose() {
        this.pause();
        if (this.e != null) {
            try {
                this.e.stop();
                this.e.dispose();
            }
            catch (Exception exception) {
                OsfLog.info("Error closing music player: " + exception);
                OsfLog.logException(exception);
            }
            this.e = null;
        }
    }
}


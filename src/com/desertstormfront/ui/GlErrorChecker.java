/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.noblemaster.lib.log.OsfLog;

public final class GlErrorChecker {
    public static final void check() {
        GlErrorChecker.check("unknown");
    }

    public static final void check(String string) {
        GL20 gL20 = Gdx.gl20;
        int i2 = gL20.glGetError();
        switch (i2) {
            case 1280: {
                OsfLog.error("OpenGL Error: INVALID_ENUM [at " + string + "]");
                break;
            }
            case 1286: {
                OsfLog.error("OpenGL Error: INVALID_FRAMEBUFFER_OPERATION [at " + string + "]");
                break;
            }
            case 1281: {
                OsfLog.error("OpenGL Error: INVALID_VALUE [at " + string + "]");
                break;
            }
            case 1282: {
                OsfLog.error("OpenGL Error: INVALID_OPERATION [at " + string + "]");
                break;
            }
            case 1285: {
                OsfLog.error("OpenGL Error: OUT_OF_MEMORY [at " + string + "]");
                break;
            }
            case 0: {
                break;
            }
            default: {
                OsfLog.error("OpenGL Error: " + i2 + " (unknown error number)" + " [at " + string + "]");
            }
        }
    }
}


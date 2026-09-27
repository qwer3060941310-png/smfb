/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

public strictfp final class EffectTimer {
    private float a;

    static final EffectTimer create() {
        EffectTimer effectTimer = new EffectTimer();
        return effectTimer;
    }

    public final float getTime() {
        return this.a;
    }

    public final void setTime(float f1) {
        this.a = f1;
    }
}


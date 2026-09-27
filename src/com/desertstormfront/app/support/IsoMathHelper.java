/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.app.support;

public final class IsoMathHelper {
    public static final strictfp float toScreenX(float f0, float f1, int i2) {
        return (f0 - f1) * (float)(i2 >> 1);
    }

    public static final strictfp float toScreenY(float f0, float f1, int i2) {
        return (f0 + f1) * (float)(i2 >> 1);
    }

    public static final strictfp float toTileX(float f0, float f1, int i2, int i3) {
        return f0 / (float)i2 + f1 / (float)i3;
    }

    public static final strictfp float toTileY(float f0, float f1, int i2, int i3) {
        return f1 / (float)i3 - f0 / (float)i2;
    }
}


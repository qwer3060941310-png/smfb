/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.math;

import com.badlogic.gdx.utils.NumberUtils;
import com.noblemaster.lib.util.FastRandom;

public final class MathHelper {
    private static final float[] sineTable = new float[720];
    private static final int atanTableSize = (int)Math.sqrt(16384.0);
    private static final float atanTableMax = atanTableSize - 1;
    private static final float[] atanTable = new float[16384];
    private static final double maxDoubleBelow16385 = NumberUtils.longBitsToDouble(NumberUtils.doubleToLongBits(16385.0) - 1L);
    private static final FastRandom random = new FastRandom();

    static {
        int i0 = 0;
        while (i0 < 720) {
            MathHelper.sineTable[i0] = (float)Math.sin(Math.PI * 2 * (double)i0 / 720.0);
            ++i0;
        }
        i0 = 0;
        while (i0 < atanTableSize) {
            int i1 = 0;
            while (i1 < atanTableSize) {
                float f2 = (float)i0 / (float)atanTableSize;
                float f3 = (float)i1 / (float)atanTableSize;
                MathHelper.atanTable[i1 * MathHelper.atanTableSize + i0] = (float)Math.atan2(f3, f2);
                ++i1;
            }
            ++i0;
        }
    }

    public static final strictfp float sqrt(float f0) {
        return (float)Math.sqrt(f0);
    }

    public static final strictfp float sin(float f0) {
        int i1 = (int)(f0 / ((float)Math.PI * 2) * 720.0f) % 720;
        if (i1 < 0) {
            i1 += 720;
        }
        return sineTable[i1];
    }

    public static final strictfp float cos(float f0) {
        return MathHelper.sin(f0 + 1.5707964f);
    }

    public static final strictfp float atan2(float f0, float f1) {
        float f2;
        float f3;
        if (f1 < 0.0f) {
            if (f0 < 0.0f) {
                f1 = -f1;
                f0 = -f0;
                f3 = 1.0f;
            } else {
                f1 = -f1;
                f3 = -1.0f;
            }
            f2 = (float)(-Math.PI);
        } else {
            if (f0 < 0.0f) {
                f0 = -f0;
                f3 = -1.0f;
            } else {
                f3 = 1.0f;
            }
            f2 = 0.0f;
        }
        float f4 = atanTableMax / (f1 < f0 ? f0 : f1);
        int i5 = (int)(f1 * f4);
        int i6 = (int)(f0 * f4);
        return (atanTable[i6 * atanTableSize + i5] + f2) * f3;
    }

    public static final strictfp float angleDifference(float f0, float f1) {
        float f2 = f0 - f1 + (float)Math.PI;
        f2 /= (float)Math.PI * 2;
        f2 = (f2 - (float)MathHelper.floor(f2)) * ((float)Math.PI * 2) - (float)Math.PI;
        return f2;
    }

    public static strictfp int floor(float f0) {
        return (int)((double)f0 + 16384.0) - 16384;
    }

    public static strictfp int round(float f0) {
        return (int)((double)f0 + 16384.5) - 16384;
    }

    public static strictfp float randomFloat() {
        return random.nextFloat();
    }

    public static strictfp float abs(float f0) {
        return f0 >= 0.0f ? f0 : -f0;
    }

    public static strictfp int abs(int i0) {
        return i0 >= 0 ? i0 : -i0;
    }

    public static strictfp long abs(long l0) {
        return l0 >= 0L ? l0 : -l0;
    }

    public static strictfp int sign(int i0) {
        return i0 == 0 ? 0 : (i0 > 0 ? 1 : -1);
    }
}


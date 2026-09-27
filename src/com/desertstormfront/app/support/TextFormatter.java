/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.app.support;

public final class TextFormatter {
    public static final String formatAmount(long l0) {
        return String.valueOf(l0);
    }

    public static final String formatTime(float f0) {
        int i1 = Math.abs((int)f0);
        int i2 = i1 / 60;
        int i3 = i1 % 60;
        return i3 < 10 ? String.valueOf(i2) + ":0" + i3 : String.valueOf(i2) + ":" + i3;
    }

    public static final String formatTenths(int i0) {
        int i1 = i0 / 1000;
        int i2 = i0 / 100 % 10;
        return String.valueOf(i1) + "." + i2;
    }
}


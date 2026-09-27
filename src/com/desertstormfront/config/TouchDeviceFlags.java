/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.config;

public final class TouchDeviceFlags {
    private static boolean touchDevice = false;

    public static void setTouchDevice(boolean bl) {
        touchDevice = bl;
    }

    public static boolean isTouchDevice() {
        return touchDevice;
    }
}


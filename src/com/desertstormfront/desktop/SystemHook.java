/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.desktop;

import com.desertstormfront.desktop.SystemHook$User32;
import com.sun.jna.Native;

public final class SystemHook {
    private static boolean a = false;
    private static SystemHook$User32 b;
    private static boolean c;
    private static boolean d;
    private static boolean e;

    static {
        c = false;
        d = false;
    }

    public static boolean isTabletMode() {
        SystemHook$User32 systemHook$User32;
        if (SystemHook.isWindows8OrLater() && (systemHook$User32 = SystemHook.getUser32()) != null) {
            try {
                return systemHook$User32.GetSystemMetrics(8195) == 0;
            }
            catch (Throwable throwable) {
                return false;
            }
        }
        return false;
    }

    public static void setDisplayOrientationPreference(int i0) {
        SystemHook$User32 systemHook$User32;
        if (SystemHook.isWindows8OrLater() && (systemHook$User32 = SystemHook.getUser32()) != null) {
            try {
                systemHook$User32.SetDisplayAutoRotationPreferences(i0);
                System.out.println("Running with display orientation set to 0x" + Integer.toHexString(i0) + ".");
            }
            catch (Throwable throwable) {
                System.out.println("Cannot set display orientation preference: " + throwable);
            }
        }
    }

    private static synchronized SystemHook$User32 getUser32() {
        if (!a) {
            try {
                b = (SystemHook$User32)Native.loadLibrary("user32", SystemHook$User32.class);
            }
            catch (Throwable throwable) {
                System.out.println("Error obtaining user32 library: " + throwable);
            }
            a = true;
        }
        return b;
    }

    private static synchronized void detectWindowsVersion() {
        if (!d) {
            String string = System.getProperty("os.name");
            String string2 = System.getProperty("os.version");
            int[] nArray = null;
            try {
                String[] stringArray = string2.split("\\.");
                nArray = new int[stringArray.length];
                int i4 = 0;
                while (i4 < stringArray.length) {
                    try {
                        nArray[i4] = Integer.parseInt(stringArray[i4]);
                    }
                    catch (Exception exception) {
                        nArray[i4] = -1;
                    }
                    ++i4;
                }
            }
            catch (Exception exception) {
                nArray = new int[]{-1};
            }
            e = string.toLowerCase().startsWith("win") && (nArray[0] > 6 || nArray[0] == 6 && nArray[1] >= 2);
            d = true;
        }
    }

    private static boolean isWindows8OrLater() {
        SystemHook.detectWindowsVersion();
        return e;
    }
}


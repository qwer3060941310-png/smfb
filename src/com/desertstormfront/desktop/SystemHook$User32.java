/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.desktop;

import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface SystemHook$User32
extends StdCallLibrary {
    public int GetSystemMetrics(int var1);

    public void SetDisplayAutoRotationPreferences(int var1);

    public Pointer FindWindow(String var1, String var2);

    public Pointer FindWindowA(String var1, String var2);

    public Pointer FindWindowExA(Pointer var1, Pointer var2, String var3, String var4);

    public int RegisterTouchWindow(Pointer var1, int var2);

    public Pointer SetWindowLongA(Pointer var1, int var2, StdCallLibrary.StdCallCallback var3);

    public int CallWindowProcA(Pointer var1, Pointer var2, int var3, int var4, int var5);

    public int GetTouchInputInfo(int var1, int var2, Pointer var3, int var4);

    public int CloseTouchInputHandle(int var1);
}


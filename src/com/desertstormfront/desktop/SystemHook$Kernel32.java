/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.desktop;

import com.sun.jna.win32.StdCallLibrary;

public interface SystemHook$Kernel32
extends StdCallLibrary {
    public int GetLastError();
}


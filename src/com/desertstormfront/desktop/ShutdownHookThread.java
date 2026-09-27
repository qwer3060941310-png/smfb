/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.desktop;

import com.desertstormfront.desktop.DesktopLauncher;

class ShutdownHookThread
extends Thread {
    ShutdownHookThread() {
    }

    @Override
    public void run() {
        DesktopLauncher.setExitRequested(true);
    }
}


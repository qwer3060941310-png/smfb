/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.app.support;

import com.desertstormfront.app.support.ProgressCallback;
import com.desertstormfront.game.World;

public final class BackgroundTask
implements ProgressCallback,
Runnable {
    private World a;
    private Thread b;
    private float c;

    public BackgroundTask(World world) {
        this.a = world;
        this.c = 0.0f;
        this.b = new Thread(this);
        this.b.start();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final float getProgress() {
        World world = this.a;
        synchronized (world) {
            return this.c;
        }
    }

    public final boolean isDone() {
        return this.b == null;
    }

    @Override
    public final void run() {
        this.a.buildTerrain(this);
        this.b = null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final boolean onProgress(float f1) {
        World world = this.a;
        synchronized (world) {
            this.c = f1;
        }
        return true;
    }
}


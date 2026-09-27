/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.world.Vec3;

public strictfp final class Projectile {
    private float a;
    private boolean b;
    private Vec3 c;

    static final Projectile create() {
        Projectile projectile = new Projectile();
        projectile.setLaunchTime(0.0f);
        projectile.setActive(false);
        projectile.setPosition(new Vec3());
        return projectile;
    }

    public final void clear() {
        this.setLaunchTime(0.0f);
        this.setActive(false);
    }

    public final float getLaunchTime() {
        return this.a;
    }

    public final void setLaunchTime(float f1) {
        this.a = f1;
    }

    public final boolean isActive() {
        return this.b;
    }

    public final void setActive(boolean bl) {
        this.b = bl;
    }

    public final Vec3 getPosition() {
        return this.c;
    }

    public final void setPosition(Vec3 vec3) {
        this.c = vec3;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.AmmoType;
import com.desertstormfront.game.model.Projectile;
import com.desertstormfront.game.model.ProjectileList;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.world.Vec3;

public strictfp final class Volley {
    private AmmoType ammoType;
    private Unit targetUnit;
    private Vec3 origin;
    private Vec3 direction;
    private float intensity;
    private ProjectileList projectiles;

    static final Volley create() {
        Volley volley = new Volley();
        volley.setOrigin(new Vec3());
        volley.setDirection(new Vec3());
        ProjectileList projectileList = new ProjectileList(AmmoType.f);
        int i2 = 0;
        while (i2 < AmmoType.f) {
            projectileList.add(Projectile.create());
            ++i2;
        }
        volley.setProjectiles(projectileList);
        return volley;
    }

    public final AmmoType getAmmoType() {
        return this.ammoType;
    }

    public final void setAmmoType(AmmoType ammoType) {
        this.ammoType = ammoType;
    }

    public final Unit getTargetUnit() {
        return this.targetUnit;
    }

    public final void setTargetUnit(Unit unit) {
        this.targetUnit = unit;
    }

    public final Vec3 getOrigin() {
        return this.origin;
    }

    public final void setOrigin(Vec3 vec3) {
        this.origin = vec3;
    }

    public final Vec3 getDirection() {
        return this.direction;
    }

    public final void setDirection(Vec3 vec3) {
        this.direction = vec3;
    }

    public final ProjectileList getProjectiles() {
        return this.projectiles;
    }

    public final void setProjectiles(ProjectileList projectileList) {
        this.projectiles = projectileList;
    }

    public final float getIntensity() {
        return this.intensity;
    }

    public final void setIntensity(float f1) {
        this.intensity = f1;
    }
}


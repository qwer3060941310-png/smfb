/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.BirdType;
import com.desertstormfront.world.Vec3;

public strictfp final class Bird {
    private BirdType type;
    private Vec3 position;
    private float angle;
    private float distance;

    public static final Bird create(BirdType birdType) {
        Bird bird = new Bird();
        bird.setType(birdType);
        bird.setPosition(new Vec3());
        bird.setAngle(0.0f);
        bird.setDistance(0.0f);
        return bird;
    }

    public final BirdType getType() {
        return this.type;
    }

    public final void setType(BirdType birdType) {
        this.type = birdType;
    }

    public final Vec3 getPosition() {
        return this.position;
    }

    public final void setPosition(Vec3 vec3) {
        this.position = vec3;
    }

    public final float getAngle() {
        return this.angle;
    }

    public final void setAngle(float f1) {
        this.angle = f1;
    }

    public final float getDistance() {
        return this.distance;
    }

    public final void setDistance(float f1) {
        this.distance = f1;
    }

    public final String toString() {
        return this.type.toString();
    }
}


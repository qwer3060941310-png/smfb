/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.noblemaster.lib.math.MathHelper;

public strictfp final class Vec3 {
    private float x;
    private float y;
    private float z;

    public Vec3() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Vec3(float f1, float f2, float f3) {
        this.x = f1;
        this.y = f2;
        this.z = f3;
    }

    public final void normalize() {
        float f1 = 1.0f / MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        this.x *= f1;
        this.y *= f1;
        this.z *= f1;
    }

    public final float getX() {
        return this.x;
    }

    public final void setX(float f1) {
        this.x = f1;
    }

    public final float getY() {
        return this.y;
    }

    public final void setY(float f1) {
        this.y = f1;
    }

    public final float getZ() {
        return this.z;
    }

    public final void setZ(float f1) {
        this.z = f1;
    }

    public final void set(Vec3 vec3) {
        this.set(vec3.getX(), vec3.getY(), vec3.getZ());
    }

    public final void set(float f1, float f2, float f3) {
        this.x = f1;
        this.y = f2;
        this.z = f3;
    }

    public final void add(float f1, float f2, float f3) {
        this.x += f1;
        this.y += f2;
        this.z += f3;
    }

    public final int hashCode() {
        return Float.valueOf(this.x).hashCode() * Float.valueOf(this.y).hashCode() * Float.valueOf(this.z).hashCode();
    }

    public final boolean equals(Object object) {
        if (object != null && object instanceof Vec3) {
            Vec3 vec3 = (Vec3)object;
            return this.equalsComponents(vec3.getX(), vec3.getY(), vec3.getZ());
        }
        return false;
    }

    public final boolean equalsComponents(float f1, float f2, float f3) {
        return this.x == f1 && this.y == f2 && this.z == f3;
    }

    public final String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public final Vec3 copy() {
        return new Vec3(this.x, this.y, this.z);
    }

    public /* synthetic */ Object clone() {
        return this.copy();
    }
}


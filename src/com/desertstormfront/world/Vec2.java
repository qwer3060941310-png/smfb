/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.noblemaster.lib.math.MathHelper;

public strictfp class Vec2 {
    private float x;
    private float y;

    public Vec2() {
        this(0.0f, 0.0f);
    }

    public Vec2(float f1, float f2) {
        this.x = f1;
        this.y = f2;
    }

    public float getX() {
        return this.x;
    }

    protected void setX(float f1) {
        this.x = f1;
    }

    public float getY() {
        return this.y;
    }

    protected void setY(float f1) {
        this.y = f1;
    }

    public final float distanceTo(Vec2 vec2) {
        float f2 = this.getX() - vec2.getX();
        float f3 = this.getY() - vec2.getY();
        return MathHelper.sqrt(f2 * f2 + f3 * f3);
    }

    public final float distanceTo(float f1, float f2) {
        float f3 = this.getX() - f1;
        float f4 = this.getY() - f2;
        return MathHelper.sqrt(f3 * f3 + f4 * f4);
    }

    public final float distanceSquaredTo(Vec2 vec2) {
        float f2 = this.getX() - vec2.getX();
        float f3 = this.getY() - vec2.getY();
        return f2 * f2 + f3 * f3;
    }

    public final float distanceSquaredTo(float f1, float f2) {
        float f3 = this.getX() - f1;
        float f4 = this.getY() - f2;
        return f3 * f3 + f4 * f4;
    }

    public final boolean equalsInt(Vec2 vec2) {
        return (int)this.x == (int)vec2.getX() && (int)this.y == (int)vec2.getY();
    }

    public final boolean equalsInt(float f1, float f2) {
        return (int)this.x == (int)f1 && (int)this.y == (int)f2;
    }

    public int hashCode() {
        return Float.valueOf(this.getX()).hashCode() * Float.valueOf(this.getY()).hashCode();
    }

    public boolean equals(Object object) {
        if (object != null && object instanceof Vec2) {
            Vec2 vec2 = (Vec2)object;
            return this.getX() == vec2.getX() && this.getY() == vec2.getY();
        }
        return false;
    }

    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ")";
    }
}


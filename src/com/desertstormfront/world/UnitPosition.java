/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.desertstormfront.game.model.Unit;
import com.desertstormfront.world.Vec2;

public strictfp final class UnitPosition
extends Vec2 {
    private Unit a;

    public final void bindTo(Unit unit) {
        this.a = unit;
    }

    public final Unit getUnit() {
        return this.a;
    }

    @Override
    public final float getX() {
        return this.a != null ? this.a.getPosition().getX() : super.getX();
    }

    @Override
    public final void setX(float f1) {
        if (this.a != null) {
            this.a = null;
        }
        super.setX(f1);
    }

    @Override
    public final float getY() {
        return this.a != null ? this.a.getPosition().getY() : super.getY();
    }

    @Override
    public final void setY(float f1) {
        if (this.a != null) {
            this.a = null;
        }
        super.setY(f1);
    }

    @Override
    public final int hashCode() {
        return Float.valueOf(this.getX()).hashCode() * Float.valueOf(this.getY()).hashCode();
    }

    @Override
    public final boolean equals(Object object) {
        if (object != null && object instanceof UnitPosition) {
            UnitPosition unitPosition = (UnitPosition)object;
            if (this.a != null) {
                return this.a == unitPosition.a;
            }
            return super.equals(unitPosition);
        }
        return false;
    }

    @Override
    public final String toString() {
        if (this.a != null) {
            return this.a.getUnitType() + super.toString();
        }
        return super.toString();
    }
}


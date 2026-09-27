/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.Sortable;
import com.desertstormfront.world.UnitPosition;

public abstract class PositionedSortable
implements Sortable {
    @Override
    public boolean isSite() {
        return false;
    }

    @Override
    public boolean isMoveTargetMarker() {
        return true;
    }

    @Override
    public float getSortValue() {
        UnitPosition unitPosition = this.getPosition();
        return unitPosition.getX() + unitPosition.getY();
    }

    public abstract UnitPosition getPosition();
}


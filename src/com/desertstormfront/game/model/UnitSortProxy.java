/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.PositionedSortable;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.world.UnitPosition;

strictfp class UnitSortProxy
extends PositionedSortable {
    final /* synthetic */ Unit a;

    UnitSortProxy(Unit unit) {
        this.a = unit;
    }

    @Override
    public UnitPosition getPosition() {
        return Unit.k(this.a);
    }
}


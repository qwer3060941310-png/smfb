/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.desertstormfront.game.model.Site;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.world.RoadTile;

strictfp final class GridCell {
    private RoadTile a;
    private Site b;
    private Unit c;
    private UnitList d = new UnitList(8);

    GridCell() {
    }

    public RoadTile getRoadTile() {
        return this.a;
    }

    public void setRoadTile(RoadTile roadTile) {
        this.a = roadTile;
    }

    final Site getSite() {
        return this.b;
    }

    final void setSite(Site site) {
        this.b = site;
    }

    final Unit getUnit() {
        return this.c;
    }

    final void setUnit(Unit unit) {
        this.c = unit;
    }

    final UnitList getUnits() {
        return this.d;
    }

    final void addUnit(Unit unit) {
        this.d.add(unit);
    }

    final void removeUnit(Unit unit) {
        this.d.remove(unit);
    }

    final boolean isBlocked() {
        return this.b != null || this.c != null;
    }
}


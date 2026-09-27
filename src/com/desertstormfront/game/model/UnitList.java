/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.Player;
import java.util.ArrayList;
import java.util.Collection;

public strictfp final class UnitList
extends ArrayList {
    private int nextId = 0;

    public UnitList() {
    }

    public UnitList(int i1) {
        super(i1);
    }

    @Override
    public boolean addAll(Collection collection) {
        UnitList unitList = (UnitList)collection;
        int i3 = unitList.size();
        int i4 = 0;
        while (i4 < i3) {
            this.add((Unit)unitList.get(i4));
            ++i4;
        }
        return true;
    }

    public int getNextId() {
        return this.nextId;
    }

    public void setNextId(int i1) {
        this.nextId = i1;
    }

    public final Unit addUnit(UnitType unitType, Player player) {
        Unit unit = Unit.create(this.nextId++, unitType, player);
        this.add(unit);
        return unit;
    }

    public final boolean hasGeneral(Player player) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((Unit)this.get(i2)).getUnitType().isGeneral() && ((Unit)this.get(i2)).getOwner() == player) {
                return true;
            }
            ++i2;
        }
        return false;
    }

    public final boolean hasCapturingUnit() {
        int i1 = 0;
        while (i1 < this.size()) {
            if (((Unit)this.get(i1)).getUnitType().canCapture()) {
                return true;
            }
            ++i1;
        }
        return false;
    }

    public final boolean containsType(UnitType unitType) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((Unit)this.get(i2)).getUnitType() == unitType) {
                return true;
            }
            ++i2;
        }
        return false;
    }

    public final boolean hasBuilding(Player player) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((Unit)this.get(i2)).getUnitType().isBuilding() && ((Unit)this.get(i2)).getOwner() == player) {
                return true;
            }
            ++i2;
        }
        return false;
    }

    public final int getActiveCount() {
        int i1 = 0;
        int i2 = 0;
        while (i2 < this.size()) {
            if (!((Unit)this.get(i2)).isCountZero() || !((Unit)this.get(i2)).isDeployed()) {
                ++i1;
            }
            ++i2;
        }
        return i1;
    }

    public final long getTotalHealth() {
        long l1 = 0L;
        int i3 = 0;
        while (i3 < this.size()) {
            l1 += ((Unit)this.get(i3)).getUnitType().getHealth();
            ++i3;
        }
        return l1;
    }

    public final int getGeneralCount() {
        int i1 = 0;
        int i2 = 0;
        while (i2 < this.size()) {
            if (((Unit)this.get(i2)).getUnitType().isGeneral()) {
                ++i1;
            }
            ++i2;
        }
        return i1;
    }

    public final int getBuildingCount() {
        int i1 = 0;
        int i2 = 0;
        while (i2 < this.size()) {
            if (((Unit)this.get(i2)).getUnitType().isBuilding()) {
                ++i1;
            }
            ++i2;
        }
        return i1;
    }

    public final int countOfType(UnitType unitType) {
        int i2 = 0;
        int i3 = 0;
        while (i3 < this.size()) {
            if (((Unit)this.get(i3)).getUnitType() == unitType) {
                ++i2;
            }
            ++i3;
        }
        return i2;
    }

    public final UnitList getUnitsOfType(UnitType unitType) {
        UnitList unitList = new UnitList();
        int i3 = 0;
        while (i3 < this.size()) {
            if (((Unit)this.get(i3)).getUnitType() == unitType) {
                unitList.add((Unit)this.get(i3));
            }
            ++i3;
        }
        return unitList;
    }

    public Unit getUnitById(int i1) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((Unit)this.get(i2)).getId() == i1) {
                return (Unit)this.get(i2);
            }
            ++i2;
        }
        return null;
    }
}


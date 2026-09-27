/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.intent;

import com.desertstormfront.ai.intent.IntentGroupMode;
import com.desertstormfront.ai.intent.UnitRequest;
import com.desertstormfront.ai.intent.UnitRequestList;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;

/**
 * 意图分组，聚合同类意图统一调度
 */
public strictfp final class IntentGroup {
    private static final Domain[] a = new Domain[]{Domain.Water, Domain.Air, Domain.Ground, Domain.Amphibian};
    private UnitRequestList b;
    private UnitList c;
    private boolean d;
    private IntentGroupMode e;
    private UnitPosition f;
    private boolean g;

    public static IntentGroup create() {
        IntentGroup intentGroup = new IntentGroup();
        intentGroup.setRequests(new UnitRequestList());
        intentGroup.setUnits(new UnitList());
        intentGroup.setHasMoveOrder(false);
        intentGroup.setMode(IntentGroupMode.a);
        intentGroup.f = new UnitPosition();
        intentGroup.g = false;
        return intentGroup;
    }

    public Unit getHighestSortWeightUnit() {
        if (this.c.size() >= 1) {
            Unit unit = (Unit)this.c.get(0);
            int i2 = unit.getUnitType().getSortWeight();
            int i3 = 1;
            while (i3 < this.c.size()) {
                Unit unit2 = (Unit)this.c.get(i3);
                int i5 = unit2.getUnitType().getSortWeight();
                if (i5 > i2) {
                    unit = unit2;
                    i2 = i5;
                }
                ++i3;
            }
            return unit;
        }
        return null;
    }

    public Domain getDomain() {
        int i1 = 0;
        while (i1 < a.length) {
            Domain domain = a[i1];
            int i3 = 0;
            while (i3 < this.c.size()) {
                if (((Unit)this.c.get(i3)).getUnitType().getDomain() == domain) {
                    return domain;
                }
                ++i3;
            }
            i3 = 0;
            while (i3 < this.b.size()) {
                if (((UnitRequest)this.b.get(i3)).getUnitType().getDomain() == domain) {
                    return domain;
                }
                ++i3;
            }
            ++i1;
        }
        return null;
    }

    public Vec2 getAnchorPosition() {
        Domain domain = this.getDomain();
        int i2 = 0;
        while (i2 < this.c.size()) {
            if (((Unit)this.c.get(i2)).getUnitType().getDomain() == domain) {
                return ((Unit)this.c.get(i2)).getPosition();
            }
            ++i2;
        }
        i2 = 0;
        while (i2 < this.b.size()) {
            if (((UnitRequest)this.b.get(i2)).getUnitType().getDomain() == domain) {
                return ((UnitRequest)this.b.get(i2)).getUnit().getPosition();
            }
            ++i2;
        }
        return null;
    }

    public boolean hasRequests() {
        return this.b.size() > 0;
    }

    public boolean hasRequestFor(UnitType unitType) {
        return this.b.hasRequestFor(unitType);
    }

    public boolean hasUnits() {
        return this.c.size() > 0;
    }

    public boolean hasUnitOfType(UnitType unitType) {
        return this.c.containsType(unitType);
    }

    public boolean hasCapturingUnit() {
        return this.c.hasCapturingUnit();
    }

    public UnitRequestList getRequests() {
        return this.b;
    }

    public void setRequests(UnitRequestList unitRequestList) {
        this.b = unitRequestList;
    }

    public UnitList getUnits() {
        return this.c;
    }

    public void setUnits(UnitList unitList) {
        this.c = unitList;
    }

    public Vec2 getTargetPosition() {
        if (this.g) {
            return this.f;
        }
        return null;
    }

    public Unit getTargetUnit() {
        if (this.g) {
            return this.f.getUnit();
        }
        return null;
    }

    public void setTargetPosition(float f1, float f2) {
        this.g = true;
        this.f.setX(f1);
        this.f.setY(f2);
    }

    public void setTargetUnit(Unit unit) {
        this.g = true;
        this.f.bindTo(unit);
    }

    public IntentGroupMode getMode() {
        return this.e;
    }

    public void setMode(IntentGroupMode intentGroupMode) {
        this.e = intentGroupMode;
    }

    public boolean hasMoveOrder() {
        return this.d;
    }

    public void setHasMoveOrder(boolean bl) {
        this.d = bl;
    }
}


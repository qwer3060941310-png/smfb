/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.intent;

import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;

/**
 * 单位请求，描述意图对单位的需求
 */
public strictfp final class UnitRequest {
    private Unit unit;
    private UnitType unitType;

    public static UnitRequest of(Unit unit, UnitType unitType) {
        UnitRequest unitRequest = new UnitRequest();
        unitRequest.setUnit(unit);
        unitRequest.setUnitType(unitType);
        return unitRequest;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }
}


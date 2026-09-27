/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.ActionNode;
import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.world.UnitPosition;

/**
 * 动作：攻击射程内最近的敌方单位
 */
public strictfp final class AttackNearestNode
extends ActionNode {
    @Override
    public void reset() {
    }

    @Override
    public NodeStatus run(UnitBlackboard unitBlackboard) {
        Unit unit = unitBlackboard.getUnit();
        UnitType unitType = unit.getUnitType();
        Unit unit2 = null;
        UnitList unitList = unitBlackboard.getContext().getVisibleEnemyMobileUnits();
        UnitPosition unitPosition = unit.getPosition();
        int i7 = unit.getUnitType().getRangeInTiles();
        int i8 = i7 * i7;
        float f9 = i8;
        int i10 = 0;
        while (i10 < unitList.size()) {
            Unit unit3 = (Unit)unitList.get(i10);
            float f12 = unitPosition.distanceSquaredTo(unit3.getPosition());
            if (f12 <= f9 && unitType.isStrongAgainst(unit3.getUnitType())) {
                unit2 = unit3;
                f9 = f12;
            }
            ++i10;
        }
        if (unit2 != null) {
            if (unit.getGuardPositionOrNull() == null || unit.getGuardPositionOrNull().getUnit() != unit2) {
                unitBlackboard.targetUnit(unit2);
            }
            return NodeStatus.a;
        }
        return NodeStatus.b;
    }
}


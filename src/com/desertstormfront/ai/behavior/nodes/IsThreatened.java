/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.ConditionNode;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.world.UnitPosition;

/**
 * 条件：是否正受到威胁
 */
public strictfp final class IsThreatened
extends ConditionNode {
    public IsThreatened(BehaviorNode behaviorNode) {
        super(behaviorNode);
    }

    @Override
    public boolean isSatisfied(UnitBlackboard unitBlackboard) {
        if (unitBlackboard.refreshIntentGroup() && unitBlackboard.getIntentGroup().hasMoveOrder()) {
            return false;
        }
        Unit unit = unitBlackboard.getUnit();
        UnitType unitType = unit.getUnitType();
        if (!unitType.isFlying() && unit.getPosition().getUnit() == null) {
            boolean i4 = false;
            UnitList unitList = unitBlackboard.getContext().getVisibleEnemyMobileUnits();
            UnitPosition unitPosition = unit.getPosition();
            int i7 = 0;
            while (i7 < unitList.size()) {
                Unit unit2 = (Unit)unitList.get(i7);
                int i9 = unit2.getUnitType().getRangeInTiles();
                int i10 = i9 * i9;
                if (unitPosition.distanceSquaredTo(unit2.getPosition()) <= (float)i10 && unitType.isWeakAgainst(unit2.getUnitType())) {
                    i4 = true;
                    break;
                }
                ++i7;
            }
            return i4;
        }
        return false;
    }
}


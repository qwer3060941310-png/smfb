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
 * 条件：射程内是否存在可打击目标
 */
public strictfp final class HasTargetInRange
extends ConditionNode {
    public HasTargetInRange(BehaviorNode behaviorNode) {
        super(behaviorNode);
    }

    @Override
    public boolean isSatisfied(UnitBlackboard unitBlackboard) {
        Unit unit = unitBlackboard.getUnit();
        UnitType unitType = unit.getUnitType();
        if (!unit.getUnitType().isFlying() && unit.getPosition().getUnit() == null) {
            boolean i4 = false;
            UnitList unitList = unitBlackboard.getContext().getVisibleEnemyMobileUnits();
            UnitPosition unitPosition = unit.getPosition();
            int i7 = unit.getUnitType().getRangeInTiles();
            int i8 = i7 * i7;
            int i9 = 0;
            while (i9 < unitList.size()) {
                Unit unit2 = (Unit)unitList.get(i9);
                if (unitPosition.distanceSquaredTo(unit2.getPosition()) <= (float)i8 && unitType.isStrongAgainst(unit2.getUnitType())) {
                    i4 = true;
                    break;
                }
                ++i9;
            }
            return i4;
        }
        return false;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.ActionNode;
import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.world.UnitPosition;

/**
 * 动作：返回基地
 */
public strictfp final class ReturnToBaseNode
extends ActionNode {
    @Override
    public void reset() {
    }

    @Override
    public NodeStatus run(UnitBlackboard unitBlackboard) {
        Unit unit = unitBlackboard.getUnit();
        if (unit.getPosition().getUnit() == null) {
            Unit unit2 = null;
            float f4 = Float.MAX_VALUE;
            UnitPosition unitPosition = unit.getPosition();
            UnitList unitList = unitBlackboard.getContext().getOwnImmobileUnits();
            int i7 = 0;
            while (i7 < unitList.size()) {
                float f9;
                Unit unit3 = (Unit)unitList.get(i7);
                if (unit3.canEmbark(unit) && unitBlackboard.getContext().hasPathToUnit(unit, unit3) && (f9 = unit3.getPosition().distanceSquaredTo(unitPosition)) < f4) {
                    unit2 = unit3;
                    f4 = f9;
                }
                ++i7;
            }
            if (unit2 != null) {
                if (unit.getGuardPositionOrNull() == null || unit.getGuardPositionOrNull().getUnit() != unit2) {
                    unitBlackboard.targetUnit(unit2);
                }
                return null;
            }
            return NodeStatus.b;
        }
        return NodeStatus.a;
    }
}


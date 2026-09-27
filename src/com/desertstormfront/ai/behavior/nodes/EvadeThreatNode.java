/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.ActionNode;
import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.world.UnitPosition;

/**
 * 动作：规避受到的威胁
 */
public strictfp final class EvadeThreatNode
extends ActionNode {
    @Override
    public void reset() {
    }

    @Override
    public NodeStatus run(UnitBlackboard unitBlackboard) {
        float f10;
        Unit unit = unitBlackboard.getUnit();
        UnitType unitType = unit.getUnitType();
        Unit unit2 = null;
        UnitList unitList = unitBlackboard.getContext().getVisibleEnemyMobileUnits();
        UnitPosition unitPosition = unit.getPosition();
        float f7 = Float.MAX_VALUE;
        int n = 0;
        while (n < unitList.size()) {
            int n2;
            int n3;
            Unit unit3 = (Unit)unitList.get(n);
            f10 = unitPosition.distanceSquaredTo(unit3.getPosition());
            if (f10 < f7 && f10 <= (float)(n3 = (n2 = unit3.getUnitType().getRangeInTiles()) * n2) && unitType.isWeakAgainst(unit3.getUnitType())) {
                unit2 = unit3;
                f7 = f10;
            }
            ++n;
        }
        if (unit2 != null) {
            UnitPosition unitPosition2 = unit2.getPosition();
            float f = unitPosition.getX() - unitPosition2.getX();
            f10 = unitPosition.getY() - unitPosition2.getY();
            float f2 = (float)Math.sqrt(f7);
            if (f2 < 1.0E-4f) {
                f = unitBlackboard.getContext().getRandom().nextBoolean() ? 1 : -1;
                f10 = unitBlackboard.getContext().getRandom().nextBoolean() ? 1 : -1;
                f2 = (float)Math.sqrt(f * f + (f10 + f10));
            }
            f /= f2;
            f10 /= f2;
            AIContext aIContext = unitBlackboard.getContext();
            int i13 = 4;
            while (i13 > 0) {
                int i14 = (int)((float)i13 * f);
                int i15 = (int)((float)i13 * f10);
                if (aIContext.isInsideGrid((float)i14, (float)i15) && aIContext.hasPathToPosition(unit, i14, i15)) {
                    if (unit.getGuardPositionOrNull() == null || unit.getGuardPositionOrNull().getX() != (float)i14 || unit.getGuardPositionOrNull().getY() != (float)i15) {
                        unitBlackboard.moveUnitTo((float)i14 + 0.5f, (float)i15 + 0.5f);
                    }
                    return NodeStatus.a;
                }
                --i13;
            }
        }
        return NodeStatus.b;
    }
}


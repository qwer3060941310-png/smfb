/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.ConditionNode;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.game.model.Unit;

/**
 * 条件：是否需要维修
 */
public strictfp final class NeedsRepair
extends ConditionNode {
    public NeedsRepair(BehaviorNode behaviorNode) {
        super(behaviorNode);
    }

    @Override
    public boolean isSatisfied(UnitBlackboard unitBlackboard) {
        Unit unit = unitBlackboard.getUnit();
        if (!unit.getUnitType().isFlying()) {
            int i4;
            int i3 = unit.getHealth();
            return i3 <= (i4 = unit.getUnitType().getHealthScale()) >> 2 || unit.getPosition().getUnit() != null && i3 < i4;
        }
        return false;
    }
}


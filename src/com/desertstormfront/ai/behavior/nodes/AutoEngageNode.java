/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.ActionNode;
import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.ai.behavior.nodes.TargetSelector;

/**
 * 动作：进入自动交战状态
 */
public strictfp final class AutoEngageNode
extends ActionNode {
    @Override
    public void reset() {
    }

    @Override
    public NodeStatus run(UnitBlackboard unitBlackboard) {
        if (TargetSelector.isFlyingUnit(unitBlackboard)) {
            return TargetSelector.selectTarget(unitBlackboard);
        }
        if (unitBlackboard.getUnit().getGuardPositionOrNull() != null && unitBlackboard.canStop()) {
            unitBlackboard.stopUnit();
        }
        return null;
    }
}


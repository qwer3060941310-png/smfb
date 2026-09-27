/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.ConditionNode;
import com.desertstormfront.ai.behavior.UnitBlackboard;

/**
 * 条件：单位是否已分配意图分组
 */
public strictfp final class HasIntentGroup
extends ConditionNode {
    public HasIntentGroup(BehaviorNode behaviorNode) {
        super(behaviorNode);
    }

    @Override
    public boolean isSatisfied(UnitBlackboard unitBlackboard) {
        return unitBlackboard.refreshIntentGroup();
    }
}


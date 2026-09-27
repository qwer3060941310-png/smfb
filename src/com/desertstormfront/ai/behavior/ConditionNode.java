/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.ConditionNodeBase;
import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;

/**
 * 行为树条件节点，按黑板状态返回成功/失败
 */
public strictfp abstract class ConditionNode
extends ConditionNodeBase {
    protected ConditionNode(BehaviorNode behaviorNode) {
        super(behaviorNode);
    }

    @Override
    public final boolean canRun(UnitBlackboard unitBlackboard) {
        return this.isSatisfied(unitBlackboard);
    }

    @Override
    public final NodeStatus run(UnitBlackboard unitBlackboard) {
        if (this.child.canRun(unitBlackboard)) {
            return this.child.run(unitBlackboard);
        }
        return NodeStatus.b;
    }
}


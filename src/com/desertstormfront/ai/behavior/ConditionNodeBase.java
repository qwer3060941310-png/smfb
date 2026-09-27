/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.UnitBlackboard;

/**
 * 条件节点基础实现，封装判定逻辑
 */
public strictfp abstract class ConditionNodeBase
extends BehaviorNode {
    protected BehaviorNode child;

    ConditionNodeBase(BehaviorNode behaviorNode) {
        this.child = behaviorNode;
    }

    public abstract boolean isSatisfied(UnitBlackboard var1);

    @Override
    public final void reset() {
        this.child.reset();
    }

    @Override
    public final BehaviorNode getActiveNode(UnitBlackboard unitBlackboard) {
        if (this.child.canRun(unitBlackboard)) {
            return this.child.getActiveNode(unitBlackboard);
        }
        return this;
    }
}


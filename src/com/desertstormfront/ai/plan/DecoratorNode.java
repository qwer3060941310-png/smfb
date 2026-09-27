/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan;

import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;

/**
 * HTN 装饰器节点，包装并修饰子规划
 */
public strictfp abstract class DecoratorNode
extends PlanNode {
    protected PlanNode child;

    DecoratorNode(PlanNode planNode) {
        this.child = planNode;
    }

    public abstract boolean isSatisfied(PlanContext var1);

    @Override
    public final void reset() {
        this.child.reset();
    }

    @Override
    public final PlanNode getActiveNode(PlanContext planContext) {
        if (this.child.canRun(planContext)) {
            return this.child.getActiveNode(planContext);
        }
        return this;
    }
}


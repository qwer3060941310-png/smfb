/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan;

import com.desertstormfront.ai.plan.DecoratorNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;
import com.desertstormfront.ai.plan.PlanResult;

/**
 * HTN 条件规划节点，判定前置条件
 */
public strictfp abstract class ConditionPlanNode
extends DecoratorNode {
    protected ConditionPlanNode(PlanNode planNode) {
        super(planNode);
    }

    @Override
    public final boolean canRun(PlanContext planContext) {
        return this.isSatisfied(planContext);
    }

    @Override
    public final PlanResult run(PlanContext planContext) {
        if (this.child.canRun(planContext)) {
            return this.child.run(planContext);
        }
        return PlanResult.FAILURE;
    }
}


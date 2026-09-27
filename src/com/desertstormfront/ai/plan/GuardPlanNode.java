/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan;

import com.desertstormfront.ai.plan.DecoratorNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;
import com.desertstormfront.ai.plan.PlanResult;

/**
 * HTN 守卫规划节点，带前置条件校验
 */
public strictfp abstract class GuardPlanNode
extends DecoratorNode {
    protected GuardPlanNode(PlanNode planNode) {
        super(planNode);
    }

    @Override
    public final boolean canRun(PlanContext planContext) {
        return true;
    }

    @Override
    public final PlanResult run(PlanContext planContext) {
        if (this.isSatisfied(planContext)) {
            if (this.child.canRun(planContext)) {
                return this.child.run(planContext);
            }
            return PlanResult.FAILURE;
        }
        return PlanResult.SUCCESS;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.plan.GuardPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;

/**
 * 节点：要求具备作战目标
 */
public strictfp final class RequireObjectiveNode
extends GuardPlanNode {
    public RequireObjectiveNode(PlanNode planNode) {
        super(planNode);
    }

    @Override
    public String getName() {
        return "OTAG";
    }

    @Override
    public boolean isSatisfied(PlanContext planContext) {
        return planContext.getIntent().getTargetPosition() != null;
    }
}


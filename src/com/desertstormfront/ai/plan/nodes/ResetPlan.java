/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.plan.ActionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;

/**
 * 规划：重置当前规划状态
 */
public strictfp final class ResetPlan
extends ActionPlanNode {
    @Override
    public String getName() {
        return "RSET";
    }

    @Override
    public void reset() {
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        Intent intent = planContext.getIntent();
        intent.clearTarget();
        intent.getGroupList().clear();
        return PlanResult.SUCCESS;
    }
}


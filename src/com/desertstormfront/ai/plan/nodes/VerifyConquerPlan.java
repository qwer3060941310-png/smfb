/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.plan.ActionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;

/**
 * 规划：验证攻占是否完成
 */
public strictfp final class VerifyConquerPlan
extends ActionPlanNode {
    @Override
    public String getName() {
        return "VCAP";
    }

    @Override
    public void reset() {
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        IntentGroupList intentGroupList = planContext.getIntent().getGroupList();
        return intentGroupList.hasCapturingUnit() ? PlanResult.SUCCESS : PlanResult.FAILURE;
    }
}


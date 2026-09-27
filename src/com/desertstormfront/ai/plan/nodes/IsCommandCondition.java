/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.IntentType;
import com.desertstormfront.ai.plan.ConditionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;

/**
 * 条件：是否存在待执行指令
 */
public strictfp final class IsCommandCondition
extends ConditionPlanNode {
    public IsCommandCondition(PlanNode planNode) {
        super(planNode);
    }

    @Override
    public String getName() {
        return "CCOM";
    }

    @Override
    public boolean isSatisfied(PlanContext planContext) {
        return planContext.getIntent().getType() == IntentType.Command;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.IntentType;
import com.desertstormfront.ai.plan.ConditionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;

/**
 * 条件：是否满足防御前提
 */
public strictfp final class IsDefendCondition
extends ConditionPlanNode {
    public IsDefendCondition(PlanNode planNode) {
        super(planNode);
    }

    @Override
    public String getName() {
        return "CDEF";
    }

    @Override
    public boolean isSatisfied(PlanContext planContext) {
        return planContext.getIntent().getType() == IntentType.Defend;
    }
}


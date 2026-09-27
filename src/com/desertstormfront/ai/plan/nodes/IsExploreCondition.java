/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.IntentType;
import com.desertstormfront.ai.plan.ConditionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;

/**
 * 条件：是否满足探索前提
 */
public strictfp final class IsExploreCondition
extends ConditionPlanNode {
    public IsExploreCondition(PlanNode planNode) {
        super(planNode);
    }

    @Override
    public String getName() {
        return "CEXP";
    }

    @Override
    public boolean isSatisfied(PlanContext planContext) {
        return planContext.getIntent().getType() == IntentType.Explore;
    }
}


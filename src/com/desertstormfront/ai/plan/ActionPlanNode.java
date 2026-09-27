/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan;

import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;

/**
 * HTN 规划动作节点，表示可执行原子任务
 */
public strictfp abstract class ActionPlanNode
extends PlanNode {
    @Override
    public final boolean canRun(PlanContext planContext) {
        return true;
    }

    public final ActionPlanNode asAction(PlanContext planContext) {
        return this;
    }

    @Override
    public /* synthetic */ PlanNode getActiveNode(PlanContext planContext) {
        return this.asAction(planContext);
    }
}


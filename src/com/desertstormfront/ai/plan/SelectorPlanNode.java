/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan;

import com.desertstormfront.ai.plan.CompositePlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;
import com.desertstormfront.ai.plan.PlanResult;

/**
 * HTN 选择规划节点，选取首个可行子规划
 */
public strictfp final class SelectorPlanNode
extends CompositePlanNode {
    @Override
    public String getName() {
        return "SELE";
    }

    @Override
    public final PlanResult run(PlanContext planContext) {
        int i2 = 0;
        boolean i3 = false;
        while (!i3 && i2 < this.children.size()) {
            if (i2 != this.activeIndex) {
                ((PlanNode)this.children.get(i2)).reset();
            }
            if (((PlanNode)this.children.get(i2)).canRun(planContext)) {
                i3 = true;
                continue;
            }
            ++i2;
        }
        if (i3) {
            this.activeIndex = i2;
            PlanNode planNode = (PlanNode)this.children.get(this.activeIndex);
            PlanResult planResult = planNode.run(planContext);
            if (planResult == null) {
                return null;
            }
            if (planResult == PlanResult.SUCCESS) {
                this.activeIndex = -1;
                return PlanResult.SUCCESS;
            }
            this.activeIndex = -1;
            return PlanResult.FAILURE;
        }
        this.activeIndex = -1;
        return PlanResult.FAILURE;
    }

    @Override
    public final PlanNode getActiveNode(PlanContext planContext) {
        int i2 = 0;
        boolean i3 = false;
        while (!i3 && i2 < this.children.size()) {
            if (i2 != this.activeIndex) {
                ((PlanNode)this.children.get(i2)).reset();
            }
            if (((PlanNode)this.children.get(i2)).canRun(planContext)) {
                i3 = true;
                continue;
            }
            ++i2;
        }
        if (i3) {
            return ((PlanNode)this.children.get(i2)).getActiveNode(planContext);
        }
        return this;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan;

import com.desertstormfront.ai.plan.CompositePlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;
import com.desertstormfront.ai.plan.PlanResult;

/**
 * HTN 顺序规划节点，依次完成子规划
 */
public strictfp final class SequencePlanNode
extends CompositePlanNode {
    @Override
    public String getName() {
        return "SEQU";
    }

    @Override
    public final PlanResult run(PlanContext planContext) {
        PlanNode planNode;
        if (this.activeIndex == -1) {
            this.activeIndex = 0;
            planNode = (PlanNode)this.children.get(this.activeIndex);
            planNode.reset();
        } else {
            planNode = (PlanNode)this.children.get(this.activeIndex);
        }
        if (planNode.canRun(planContext)) {
            PlanResult planResult = planNode.run(planContext);
            if (planResult == null) {
                return null;
            }
            if (planResult == PlanResult.SUCCESS) {
                ++this.activeIndex;
                if (this.activeIndex >= this.children.size()) {
                    this.activeIndex = -1;
                    return PlanResult.SUCCESS;
                }
                ((PlanNode)this.children.get(this.activeIndex)).reset();
                return null;
            }
            this.activeIndex = -1;
            return PlanResult.FAILURE;
        }
        this.activeIndex = -1;
        return PlanResult.FAILURE;
    }

    @Override
    public final PlanNode getActiveNode(PlanContext planContext) {
        PlanNode planNode;
        if (this.activeIndex == -1) {
            this.activeIndex = 0;
            planNode = (PlanNode)this.children.get(this.activeIndex);
            planNode.reset();
        } else {
            planNode = (PlanNode)this.children.get(this.activeIndex);
        }
        if (planNode.canRun(planContext)) {
            return planNode.getActiveNode(planContext);
        }
        return this;
    }
}


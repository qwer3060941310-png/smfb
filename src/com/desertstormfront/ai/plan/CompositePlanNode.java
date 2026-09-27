/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan;

import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;
import com.desertstormfront.ai.plan.PlanNodeList;

/**
 * HTN 组合规划节点，聚合子规划
 */
public strictfp abstract class CompositePlanNode
extends PlanNode {
    protected PlanNodeList children = new PlanNodeList();
    protected int activeIndex;

    CompositePlanNode() {
        this.reset();
    }

    public void addChild(PlanNode planNode) {
        this.children.add(planNode);
    }

    @Override
    public final boolean canRun(PlanContext planContext) {
        return this.children.size() > 0;
    }

    @Override
    public final void reset() {
        this.activeIndex = -1;
    }
}


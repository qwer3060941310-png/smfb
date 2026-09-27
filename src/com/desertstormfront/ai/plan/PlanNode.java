/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan;

import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;

/**
 * HTN 规划节点基类，定义规划分解与执行
 */
public strictfp abstract class PlanNode {
    public abstract String getName();

    public abstract void reset();

    public abstract boolean canRun(PlanContext var1);

    public abstract PlanResult run(PlanContext var1);

    public abstract PlanNode getActiveNode(PlanContext var1);
}


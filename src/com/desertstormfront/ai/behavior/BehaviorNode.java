/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior;

import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;

/**
 * 行为树节点基类，定义节点执行与子节点管理
 */
public strictfp abstract class BehaviorNode {
    public abstract void reset();

    public abstract boolean canRun(UnitBlackboard var1);

    public abstract NodeStatus run(UnitBlackboard var1);

    public abstract BehaviorNode getActiveNode(UnitBlackboard var1);
}


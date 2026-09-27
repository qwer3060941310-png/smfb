/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.UnitBlackboard;

/**
 * 行为树动作节点基类（叶子节点），执行具体单位动作
 */
public strictfp abstract class ActionNode
extends BehaviorNode {
    @Override
    public final boolean canRun(UnitBlackboard unitBlackboard) {
        return true;
    }

    @Override
    public final BehaviorNode getActiveNode(UnitBlackboard unitBlackboard) {
        return this;
    }
}


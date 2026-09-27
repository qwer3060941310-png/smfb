/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.BehaviorTreePool;
import com.desertstormfront.ai.behavior.UnitBlackboard;

/**
 * 行为树组合节点基类，持有并调度子节点
 */
public strictfp abstract class CompositeNode
extends BehaviorNode {
    protected BehaviorTreePool children = new BehaviorTreePool();
    protected int activeIndex;

    CompositeNode() {
        this.reset();
    }

    public void addChild(BehaviorNode behaviorNode) {
        this.children.add(behaviorNode);
    }

    @Override
    public final boolean canRun(UnitBlackboard unitBlackboard) {
        return this.children.size() > 0;
    }

    @Override
    public final void reset() {
        this.activeIndex = -1;
    }
}


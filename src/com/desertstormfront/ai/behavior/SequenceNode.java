/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.CompositeNode;
import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;

/**
 * 行为树顺序节点，依次执行子节点直至失败
 */
public strictfp final class SequenceNode
extends CompositeNode {
    @Override
    public final NodeStatus run(UnitBlackboard unitBlackboard) {
        BehaviorNode behaviorNode;
        if (this.activeIndex == -1) {
            this.activeIndex = 0;
            behaviorNode = (BehaviorNode)this.children.get(this.activeIndex);
            behaviorNode.reset();
        } else {
            behaviorNode = (BehaviorNode)this.children.get(this.activeIndex);
        }
        if (behaviorNode.canRun(unitBlackboard)) {
            NodeStatus nodeStatus = behaviorNode.run(unitBlackboard);
            if (nodeStatus == null) {
                return null;
            }
            if (nodeStatus == NodeStatus.a) {
                ++this.activeIndex;
                if (this.activeIndex >= this.children.size()) {
                    this.activeIndex = -1;
                    return NodeStatus.a;
                }
                ((BehaviorNode)this.children.get(this.activeIndex)).reset();
                return null;
            }
            this.activeIndex = -1;
            return NodeStatus.b;
        }
        this.activeIndex = -1;
        return NodeStatus.b;
    }

    @Override
    public final BehaviorNode getActiveNode(UnitBlackboard unitBlackboard) {
        BehaviorNode behaviorNode;
        if (this.activeIndex == -1) {
            this.activeIndex = 0;
            behaviorNode = (BehaviorNode)this.children.get(this.activeIndex);
            behaviorNode.reset();
        } else {
            behaviorNode = (BehaviorNode)this.children.get(this.activeIndex);
        }
        if (behaviorNode.canRun(unitBlackboard)) {
            return behaviorNode.getActiveNode(unitBlackboard);
        }
        return this;
    }
}


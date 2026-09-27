/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.CompositeNode;
import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;

/**
 * 行为树选择节点，依次尝试子节点直至成功
 */
public strictfp final class SelectorNode
extends CompositeNode {
    @Override
    public final NodeStatus run(UnitBlackboard unitBlackboard) {
        int i2 = 0;
        boolean i3 = false;
        while (!i3 && i2 < this.children.size()) {
            if (i2 != this.activeIndex) {
                ((BehaviorNode)this.children.get(i2)).reset();
            }
            if (((BehaviorNode)this.children.get(i2)).canRun(unitBlackboard)) {
                i3 = true;
                continue;
            }
            ++i2;
        }
        if (i3) {
            this.activeIndex = i2;
            BehaviorNode behaviorNode = (BehaviorNode)this.children.get(this.activeIndex);
            NodeStatus nodeStatus = behaviorNode.run(unitBlackboard);
            if (nodeStatus == null) {
                return null;
            }
            if (nodeStatus == NodeStatus.a) {
                this.activeIndex = -1;
                return NodeStatus.a;
            }
            this.activeIndex = -1;
            return NodeStatus.b;
        }
        this.activeIndex = -1;
        return NodeStatus.b;
    }

    @Override
    public final BehaviorNode getActiveNode(UnitBlackboard unitBlackboard) {
        int i2 = 0;
        boolean i3 = false;
        while (!i3 && i2 < this.children.size()) {
            if (i2 != this.activeIndex) {
                ((BehaviorNode)this.children.get(i2)).reset();
            }
            if (((BehaviorNode)this.children.get(i2)).canRun(unitBlackboard)) {
                i3 = true;
                continue;
            }
            ++i2;
        }
        if (i3) {
            return ((BehaviorNode)this.children.get(i2)).getActiveNode(unitBlackboard);
        }
        return this;
    }
}


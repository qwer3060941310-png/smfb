/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.ConditionNode;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.ai.intent.IntentGroupMode;

/**
 * 条件：是否处于登乘模式
 */
public final class IsEmbarkMode
extends ConditionNode {
    public IsEmbarkMode(BehaviorNode behaviorNode) {
        super(behaviorNode);
    }

    @Override
    public boolean isSatisfied(UnitBlackboard unitBlackboard) {
        return unitBlackboard.getIntentGroup().getMode() == IntentGroupMode.c;
    }
}


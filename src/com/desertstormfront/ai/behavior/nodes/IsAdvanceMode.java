/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.ConditionNode;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.ai.intent.IntentGroupMode;

/**
 * 条件：是否处于推进模式
 */
public strictfp final class IsAdvanceMode
extends ConditionNode {
    public IsAdvanceMode(BehaviorNode behaviorNode) {
        super(behaviorNode);
    }

    @Override
    public boolean isSatisfied(UnitBlackboard unitBlackboard) {
        return unitBlackboard.getIntentGroup().getMode() == IntentGroupMode.b;
    }
}


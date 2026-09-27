/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.ActionNode;
import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.game.model.Unit;

/**
 * 动作：登上运输载具
 */
public strictfp final class EmbarkTransportNode
extends ActionNode {
    @Override
    public void reset() {
    }

    @Override
    public NodeStatus run(UnitBlackboard unitBlackboard) {
        IntentGroup intentGroup = unitBlackboard.getIntentGroup();
        Unit unit = unitBlackboard.getUnit();
        if (unit.getUnitType().canCarry()) {
            if (!(unit.getPosition().equalsInt(intentGroup.getTargetPosition()) || unit.getGuardPositionOrNull() != null && unit.getGuardPositionOrNull().equalsInt(intentGroup.getTargetPosition()))) {
                unitBlackboard.moveUnitTo(intentGroup.getTargetPosition().getX(), intentGroup.getTargetPosition().getY());
            }
        } else {
            Unit unit2 = intentGroup.getHighestSortWeightUnit();
            if (unit2 != null && unit2.canEmbark(unit) && unit.getPosition().getUnit() != unit2 && unit2.getGuardPositionOrNull() == null && unit2.getPosition().equalsInt(intentGroup.getTargetPosition()) && (unit.getGuardPositionOrNull() == null || unit.getGuardPositionOrNull().getUnit() != unit2)) {
                unitBlackboard.targetUnit(unit2);
            }
        }
        return null;
    }
}


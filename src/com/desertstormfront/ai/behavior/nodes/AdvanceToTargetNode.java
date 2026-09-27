/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.ActionNode;
import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.ai.behavior.nodes.TargetSelector;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.map.MapDefinition;

/**
 * 动作：向当前目标推进
 */
public strictfp final class AdvanceToTargetNode
extends ActionNode {
    @Override
    public void reset() {
    }

    @Override
    public NodeStatus run(UnitBlackboard unitBlackboard) {
        if (TargetSelector.isFlyingUnit(unitBlackboard)) {
            return TargetSelector.selectTarget(unitBlackboard);
        }
        MapDefinition mapDefinition = unitBlackboard.getContext().getMapDefinition();
        IntentGroup intentGroup = unitBlackboard.getIntentGroup();
        Unit unit = unitBlackboard.getUnit();
        UnitType unitType = unit.getUnitType();
        Unit unit2 = unit.getPosition().getUnit();
        if (unit2 == null || unit2.getUnitType().isImmobile()) {
            if (intentGroup.hasMoveOrder()) {
                Unit unit3 = intentGroup.getHighestSortWeightUnit();
                if (unit == unit3) {
                    if (!(unit.getPosition().equals(intentGroup.getTargetPosition()) || unit.getGuardPositionOrNull() != null && unit.getGuardPositionOrNull().equals(intentGroup.getTargetPosition()))) {
                        Unit unit4 = intentGroup.getTargetUnit();
                        if (unit4 != null) {
                            if (unit.canInteractWith(unit4)) {
                                unitBlackboard.targetUnit(unit4);
                            }
                        } else {
                            unitBlackboard.moveUnitTo(intentGroup.getTargetPosition().getX(), intentGroup.getTargetPosition().getY());
                        }
                    }
                } else if (unit.getGuardPositionOrNull() == null || unit.getGuardPositionOrNull().getUnit() != unit3) {
                    unitBlackboard.targetUnit(unit3);
                }
            } else if (!(unit.getPosition().equals(intentGroup.getTargetPosition()) || unit.getGuardPositionOrNull() != null && unit.getGuardPositionOrNull().equals(intentGroup.getTargetPosition()))) {
                if (unitType == mapDefinition.getUnitTypeSlots().getCarrier() || unitType == mapDefinition.getUnitTypeSlots().getCruiser()) {
                    Unit unit5 = intentGroup.getTargetUnit();
                    if (unit5 == null || !unit.getOwner().isEnemyOf(unit5.getOwner())) {
                        if (unit5 != null) {
                            if (unit.canInteractWith(unit5)) {
                                unitBlackboard.targetUnit(unit5);
                            }
                        } else {
                            unitBlackboard.moveUnitTo(intentGroup.getTargetPosition().getX(), intentGroup.getTargetPosition().getY());
                        }
                    }
                } else {
                    Unit unit6 = intentGroup.getTargetUnit();
                    if (unit6 != null) {
                        if (unit.canInteractWith(unit6)) {
                            unitBlackboard.targetUnit(unit6);
                        }
                    } else {
                        unitBlackboard.moveUnitTo(intentGroup.getTargetPosition().getX(), intentGroup.getTargetPosition().getY());
                    }
                }
            }
        }
        return null;
    }
}


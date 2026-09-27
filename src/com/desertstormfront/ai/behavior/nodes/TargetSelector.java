/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.Difficulty;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.UnitPosition;
import com.noblemaster.lib.math.MathHelper;

/**
 * 目标选择：为单位挑选最优攻击目标
 */
public final class TargetSelector {
    public static final strictfp boolean isFlyingUnit(UnitBlackboard unitBlackboard) {
        return unitBlackboard.getUnit().getUnitType().isFlying();
    }

    public static final strictfp NodeStatus selectTarget(UnitBlackboard unitBlackboard) {
        UnitPosition unitPosition;
        Unit unit = unitBlackboard.getUnit();
        UnitType unitType = unit.getUnitType();
        if (unit.getGuardPositionOrNull() == null && unit.isCountZero() && unit.isDeployed() && (unitPosition = unit.getPosition()).getUnit() != null && unitPosition.getUnit().getPosition().getUnit() == null) {
            Unit unit2;
            Player player = unitBlackboard.getContext().getPlayer();
            IntentGroup intentGroup = unitBlackboard.getIntentGroup();
            Unit unit3 = unit2 = intentGroup != null ? intentGroup.getTargetUnit() : null;
            if (unit2 == null || unit2.isDestroyed() || unit2.getOwner() == null || !player.isEnemyOf(unit2.getOwner())) {
                Object object;
                boolean i7 = unitBlackboard.getContext().getPlayer().getController().getDifficulty() == Difficulty.Casual;
                UnitList unitList = unitBlackboard.getContext().collectVisibleEnemyUnits();
                int i9 = (int)(unitType.getAltitude() - unitType.getVerticalOffset());
                int i10 = i9 * i9;
                int i11 = 0;
                int i12 = i10;
                Object object2 = null;
                int i14 = unitList.size();
                int n = 0;
                while (n < i14) {
                    object = (Unit)unitList.get(n);
                    int n2 = (int)unitPosition.distanceSquaredTo(((Unit)object).getPosition());
                    if (n2 <= i10) {
                        int n3;
                        int n4 = n3 = !i7 && ((Unit)object).isHighValue() ? 1000 : unitType.getDamageAgainst(((Unit)object).getUnitType());
                        if ((n3 > i11 || n3 == i11 && n2 <= i12) && unit.canAttackUnit((Unit)object)) {
                            object2 = object;
                            i11 = n3;
                            i12 = n2;
                        }
                    }
                    ++n;
                }
                MapDefinition mapDefinition = unitBlackboard.getContext().getMapDefinition();
                if (object2 != null) {
                    if (((Unit)object2).getUnitType() == mapDefinition.getUnitTypeSlots().getFighterPlane()) {
                        object = ((Unit)object2).getPosition();
                        unitBlackboard.moveUnitTo(((UnitPosition)object).getX(), ((UnitPosition)object).getY());
                    } else {
                        unitBlackboard.targetUnit((Unit)object2);
                    }
                } else if (unitType == mapDefinition.getUnitTypeSlots().getFighterPlane() && (player.isExplorationEnabled() || player.isFogEnabled())) {
                    object = unit.getPosition();
                    float f = ((UnitPosition)object).getX();
                    float f2 = ((UnitPosition)object).getY();
                    float f19 = unitBlackboard.getContext().getGameTime();
                    float f20 = (float)((int)(f19 * 10000.0f) % 360) / 360.0f * ((float)Math.PI * 2);
                    float f21 = MathHelper.cos(f20) * (float)i9;
                    float f22 = MathHelper.sin(f20) * (float)i9;
                    unitBlackboard.moveUnitTo(f + f21, f2 + f22);
                }
            } else if (unit.canInteractWith(unit2)) {
                unitBlackboard.targetUnit(unit2);
            }
        }
        return null;
    }
}


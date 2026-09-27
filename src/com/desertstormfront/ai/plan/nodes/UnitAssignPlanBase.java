/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.plan.ActionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;

/**
 * 单位分配规划基类
 */
public strictfp abstract class UnitAssignPlanBase
extends ActionPlanNode {
    protected final Unit findUnitForTypes(PlanContext planContext, UnitTypeList unitTypeList, Vec2 vec2, float f4) {
        int i5 = 0;
        while (i5 < unitTypeList.size()) {
            Unit unit = this.findUnitForType(planContext, (UnitType)unitTypeList.get(i5), vec2, f4);
            if (unit != null) {
                return unit;
            }
            ++i5;
        }
        return null;
    }

    protected final Unit findUnitForType(PlanContext planContext, UnitType unitType, Vec2 vec2) {
        return this.findUnitForType(planContext, unitType, vec2, Float.MAX_VALUE);
    }

    protected final Unit findUnitForType(PlanContext planContext, UnitType unitType, Vec2 vec2, float f4) {
        AIContext aIContext = planContext.getAiContext();
        UnitList unitList = planContext.getAiContext().getUnassignedUnits();
        Unit unit = null;
        float f8 = f4;
        int i9 = 0;
        while (i9 < unitList.size()) {
            float f12;
            UnitPosition unitPosition;
            Unit unit2 = (Unit)unitList.get(i9);
            if (unit2.getUnitType() == unitType && ((unitPosition = unit2.getPosition()).getUnit() == null || unitPosition.getUnit().isImmobile()) && aIContext.hasPathForDomain(unitType.getDomain(), vec2.getX(), vec2.getY(), unitPosition.getX(), unitPosition.getY()) && (f12 = vec2.distanceSquaredTo(unitPosition)) < f8) {
                unit = unit2;
                f8 = f12;
            }
            ++i9;
        }
        return unit;
    }

    protected final Unit findUnitForDomain(PlanContext planContext, Domain domain, Vec2 vec2, UnitType unitType) {
        AIContext aIContext = planContext.getAiContext();
        UnitList unitList = planContext.getAiContext().getOwnImmobileUnits();
        Unit unit = null;
        float f8 = Float.MAX_VALUE;
        int i9 = 0;
        while (i9 < unitList.size()) {
            float f11;
            Unit unit2 = (Unit)unitList.get(i9);
            if (unit2.getUnitType() == unitType && aIContext.hasPathForDomain(domain, vec2.getX(), vec2.getY(), unit2.getPosition().getX(), unit2.getPosition().getY()) && (f11 = vec2.distanceSquaredTo(unit2.getPosition())) < f8) {
                unit = unit2;
                f8 = f11;
            }
            ++i9;
        }
        return unit;
    }
}


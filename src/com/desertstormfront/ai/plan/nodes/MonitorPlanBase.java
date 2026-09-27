/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.plan.ActionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.map.MapDefinition;

/**
 * 监视类规划基类
 */
public strictfp abstract class MonitorPlanBase
extends ActionPlanNode {
    @Override
    public final PlanResult run(PlanContext planContext) {
        int i10;
        UnitList unitList;
        IntentGroup intentGroup;
        MapDefinition mapDefinition = planContext.getAiContext().getMapDefinition();
        Intent intent = planContext.getIntent();
        IntentGroupList intentGroupList = intent.getGroupList();
        boolean i5 = false;
        boolean i6 = false;
        int i7 = 0;
        while (i7 < intentGroupList.size()) {
            intentGroup = (IntentGroup)intentGroupList.get(i7);
            unitList = intentGroup.getUnits();
            i10 = 0;
            while (i10 < unitList.size()) {
                if (((Unit)unitList.get(i10)).isDestroyed()) {
                    UnitType unitType = ((Unit)unitList.get(i10)).getUnitType();
                    if (unitType == mapDefinition.getUnitTypeSlots().getAirTransport() || unitType == mapDefinition.getUnitTypeSlots().getTransport()) {
                        i5 = true;
                    } else if (unitType.canCapture()) {
                        i6 = true;
                    }
                    if (unitType.canCarry()) {
                        unitList.clear();
                        continue;
                    }
                    unitList.remove(i10);
                    continue;
                }
                ++i10;
            }
            ++i7;
        }
        i7 = 0;
        while (i7 < intentGroupList.size()) {
            intentGroup = (IntentGroup)intentGroupList.get(i7);
            unitList = intentGroup.getUnits();
            i10 = 0;
            boolean bl = false;
            int i12 = 0;
            while (i12 < unitList.size()) {
                UnitType unitType = ((Unit)unitList.get(i12)).getUnitType();
                if (unitType == mapDefinition.getUnitTypeSlots().getCarrier()) {
                    i10 = 1;
                } else if (unitType == mapDefinition.getUnitTypeSlots().getFighterPlane() || unitType == mapDefinition.getUnitTypeSlots().getHelicopter()) {
                    bl = true;
                }
                ++i12;
            }
            if (i10 != 0 && !bl) {
                unitList.clear();
            }
            ++i7;
        }
        i7 = 0;
        while (i7 < intentGroupList.size()) {
            if (!((IntentGroup)intentGroupList.get(i7)).hasUnits()) {
                intentGroupList.remove(i7);
                continue;
            }
            ++i7;
        }
        if (i5 && !intentGroupList.hasUnitOfType(mapDefinition.getUnitTypeSlots().getAirTransport()) && !intentGroupList.hasUnitOfType(mapDefinition.getUnitTypeSlots().getTransport())) {
            return PlanResult.FAILURE;
        }
        if (i6 && !intentGroupList.hasCapturingUnit()) {
            return PlanResult.FAILURE;
        }
        if (!intentGroupList.hasUnits()) {
            return PlanResult.FAILURE;
        }
        return this.monitor(planContext);
    }

    public abstract PlanResult monitor(PlanContext var1);
}


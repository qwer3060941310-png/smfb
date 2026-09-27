/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.UnitRequest;
import com.desertstormfront.ai.plan.ActionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.map.MapDefinition;
import java.util.ArrayList;

/**
 * 规划：处理游离单位
 */
public strictfp final class InitRoguePlan
extends ActionPlanNode {
    private UnitList a = new UnitList();

    @Override
    public String getName() {
        return "IROG";
    }

    @Override
    public void reset() {
        this.a.clear();
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        Object object;
        Object object2 = null;
        MapDefinition mapDefinition = planContext.getAiContext().getMapDefinition();
        UnitType unitType = planContext.getAiContext().getRandom().nextInt(100) < 70 ? mapDefinition.getUnitTypeSlots().getSubmarine() : mapDefinition.getUnitTypeSlots().getGunboat();
        UnitList unitList = planContext.getAiContext().getUnassignedUnits();
        int i6 = 0;
        while (i6 < unitList.size()) {
            object = (Unit)unitList.get(i6);
            if (((Unit)object).getUnitType() == unitType) {
                object2 = object;
                break;
            }
            ++i6;
        }
        if (object2 == null) {
            object = planContext.getAiContext().getOwnImmobileUnits();
            int n = 0;
            while (n < ((ArrayList)object).size()) {
                if (((Unit)((ArrayList)object).get(n)).getUnitType() == planContext.getAiContext().getMapDefinition().getUnitTypeSlots().getShipyard()) {
                    this.a.add((Unit)((ArrayList)object).get(n));
                }
                ++n;
            }
            if (this.a.size() > 0) {
                Unit unit = (Unit)this.a.get(planContext.getAiContext().getRandom().nextInt(this.a.size()));
                if (planContext.canBuild(unit, unitType)) {
                    planContext.build(unit, unitType);
                    IntentGroup intentGroup = IntentGroup.create();
                    intentGroup.getRequests().add(UnitRequest.of(unit, unitType));
                    planContext.getIntent().getGroupList().add(intentGroup);
                    return PlanResult.SUCCESS;
                }
                return PlanResult.FAILURE;
            }
            return PlanResult.FAILURE;
        }
        object = IntentGroup.create();
        planContext.getIntent().getGroupList().add(object);
        planContext.getAiContext().claimUnits(((IntentGroup)object).getUnits(), (Unit)object2);
        return PlanResult.SUCCESS;
    }
}


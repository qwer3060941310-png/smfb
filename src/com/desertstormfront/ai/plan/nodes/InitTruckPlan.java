/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.plan.ActionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.game.mode.EscortMode;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;

/**
 * 规划：调度卡车补给
 */
public strictfp final class InitTruckPlan
extends ActionPlanNode {
    @Override
    public String getName() {
        return "ITRU";
    }

    @Override
    public void reset() {
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        AIContext aIContext = planContext.getAiContext();
        UnitList unitList = aIContext.getUnassignedUnits();
        Unit unit = null;
        int i5 = 0;
        while (i5 < unitList.size()) {
            Unit unit2 = (Unit)unitList.get(i5);
            if (unit2.getUnitType().isTruck()) {
                unit = unit2;
                IntentGroup intentGroup = IntentGroup.create();
                planContext.getIntent().getGroupList().add(intentGroup);
                planContext.getAiContext().claimUnits(intentGroup.getUnits(), unit);
                EscortMode escortMode = (EscortMode)aIContext.getGameMode();
                planContext.getIntent().setTargetPosition(escortMode.getTargetArea().getX(), escortMode.getTargetArea().getY());
                return PlanResult.SUCCESS;
            }
            ++i5;
        }
        return null;
    }
}


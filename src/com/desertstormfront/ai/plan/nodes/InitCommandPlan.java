/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.intent.InfluenceMap;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.plan.ActionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.player.FogOfWar;

/**
 * 规划：执行玩家/指挥官指令
 */
public strictfp final class InitCommandPlan
extends ActionPlanNode {
    @Override
    public String getName() {
        return "ICOM";
    }

    @Override
    public void reset() {
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        Object object;
        AIContext aIContext = planContext.getAiContext();
        UnitList unitList = planContext.getAiContext().getUnassignedUnits();
        Unit unit = null;
        int n = 0;
        while (n < unitList.size()) {
            Unit unit2 = (Unit)unitList.get(n);
            if (unit2.getUnitType().isGeneral()) {
                unit = unit2;
                object = IntentGroup.create();
                planContext.getIntent().getGroupList().add(object);
                planContext.getAiContext().claimUnits(((IntentGroup)object).getUnits(), unit);
                break;
            }
            ++n;
        }
        if (unit == null) {
            return null;
        }
        InfluenceMap influenceMap = aIContext.getInfluenceMap();
        float f = influenceMap.getInfluence(unit.getPosition());
        object = aIContext.getPlayer().getFogOfWar();
        int i8 = influenceMap.getCellSize();
        int i9 = (int)unit.getPosition().getX();
        int i10 = (int)unit.getPosition().getY();
        int i11 = i9 - 2 * i8;
        int i12 = i10 + 2 * i8;
        int i13 = i10 - 2 * i8;
        int i14 = i10 + 2 * i8;
        boolean i15 = false;
        float f16 = 0.0f;
        float f17 = 0.0f;
        float f18 = f;
        int n2 = i13;
        while (n2 <= i14) {
            int i20 = i11;
            while (i20 <= i12) {
                float f21;
                if (aIContext.isInsideGrid((float)i20, (float)n2) && (f21 = influenceMap.getInfluence(i20, n2)) < f18 && ((FogOfWar)object).isVisible(i20, n2) && !aIContext.isAllWaterTile(i20, n2) && aIContext.hasPathToPosition(unit, i20, n2)) {
                    i15 = true;
                    f16 = (float)i20 + 0.5f;
                    f17 = (float)n2 + 0.5f;
                    f18 = f21;
                }
                i20 += i8;
            }
            n2 += i8;
        }
        if (i15) {
            planContext.getIntent().setTargetPosition(f16, f17);
            IntentGroup intentGroup = IntentGroup.create();
            planContext.getIntent().getGroupList().add(intentGroup);
            planContext.getAiContext().claimUnits(intentGroup.getUnits(), unit);
            return PlanResult.SUCCESS;
        }
        return null;
    }
}


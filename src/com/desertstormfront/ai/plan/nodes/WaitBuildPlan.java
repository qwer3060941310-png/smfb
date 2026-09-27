/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.intent.UnitRequest;
import com.desertstormfront.ai.intent.UnitRequestList;
import com.desertstormfront.ai.plan.ActionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.Player;
import com.noblemaster.lib.log.OsfLog;

/**
 * 规划：等待单位建造完成
 */
public strictfp final class WaitBuildPlan
extends ActionPlanNode {
    @Override
    public String getName() {
        return "WBLD";
    }

    @Override
    public void reset() {
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        if (!planContext.getIntent().getGroupList().hasRequests()) {
            return PlanResult.SUCCESS;
        }
        Player player = planContext.getAiContext().getPlayer();
        IntentGroupList intentGroupList = planContext.getIntent().getGroupList();
        int i4 = 0;
        while (i4 < intentGroupList.size()) {
            UnitRequestList unitRequestList = ((IntentGroup)intentGroupList.get(i4)).getRequests();
            if (unitRequestList.size() >= 1) {
                UnitRequest unitRequest = (UnitRequest)unitRequestList.get(0);
                if (unitRequest.getUnit().getOwner() != player) {
                    return PlanResult.FAILURE;
                }
                UnitType unitType = unitRequest.getUnitType();
                UnitList unitList = unitRequest.getUnit().getSubUnits();
                int i9 = 0;
                while (i9 < unitList.size()) {
                    Unit unit = (Unit)unitList.get(i9);
                    if (unit.getUnitType() == unitType && !planContext.getAiContext().isUnitAssigned(unit)) {
                        return null;
                    }
                    ++i9;
                }
                return PlanResult.FAILURE;
            }
            ++i4;
        }
        OsfLog.error("Coding error - shouldn't get here!");
        return PlanResult.FAILURE;
    }
}


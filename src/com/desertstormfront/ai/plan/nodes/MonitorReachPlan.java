/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.intent.IntentGroupMode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.ai.plan.nodes.MonitorPlanBase;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.player.Player;

/**
 * 规划：监视单位是否抵达目标
 */
public strictfp final class MonitorReachPlan
extends MonitorPlanBase {
    private int a;
    private float b;
    private int c;

    @Override
    public String getName() {
        return "MREA";
    }

    @Override
    public void reset() {
        this.a = 0;
    }

    @Override
    public PlanResult monitor(PlanContext planContext) {
        Player player = planContext.getAiContext().getPlayer();
        Intent intent = planContext.getIntent();
        Unit unit = intent.getTargetUnit();
        if (this.a == 0) {
            this.b = planContext.getAiContext().getGameTime();
            this.c = 0;
            if (unit != null && (unit.getOwner() == null || player.isEnemyOf(unit.getOwner()))) {
                return PlanResult.FAILURE;
            }
            IntentGroupList intentGroupList = intent.getGroupList();
            int i6 = 0;
            while (i6 < intentGroupList.size()) {
                IntentGroup intentGroup = (IntentGroup)intentGroupList.get(i6);
                if (unit != null) {
                    intentGroup.setTargetUnit(unit);
                } else {
                    intentGroup.setTargetPosition(intent.getTargetPosition().getX(), intent.getTargetPosition().getY());
                }
                intentGroup.setMode(IntentGroupMode.b);
                intentGroup.setHasMoveOrder(false);
                ++i6;
            }
            this.a = 1;
            return null;
        }
        ++this.c;
        if (unit == null) {
            IntentGroupList intentGroupList = intent.getGroupList();
            boolean i6 = true;
            int n = 0;
            while (n < intentGroupList.size()) {
                IntentGroup intentGroup = (IntentGroup)intentGroupList.get(n);
                Unit unit2 = intentGroup.getHighestSortWeightUnit();
                if (unit2.getUnitType().isTruck()) {
                    if (unit2.getPosition().distanceSquaredTo(intentGroup.getTargetPosition()) >= 0.2f) {
                        i6 = false;
                        break;
                    }
                } else if (unit2.getPosition().distanceSquaredTo(intentGroup.getTargetPosition()) >= 7.0f) {
                    i6 = false;
                    break;
                }
                ++n;
            }
            if (i6) {
                return PlanResult.SUCCESS;
            }
        } else {
            if (unit.getOwner() == null || player.isEnemyOf(unit.getOwner())) {
                return PlanResult.FAILURE;
            }
            IntentGroupList intentGroupList = intent.getGroupList();
            boolean i6 = true;
            int n = 0;
            while (n < intentGroupList.size()) {
                IntentGroup intentGroup = (IntentGroup)intentGroupList.get(n);
                UnitList unitList = intentGroup.getUnits();
                int i10 = 0;
                while (i10 < unitList.size()) {
                    Unit unit3 = (Unit)unitList.get(i10);
                    if (unit.canEmbark(unit3) && unit3.getPosition().getUnit() != unit) {
                        i6 = false;
                        break;
                    }
                    ++i10;
                }
                ++n;
            }
            if (i6) {
                return PlanResult.SUCCESS;
            }
        }
        if ((double)this.b + 180.0 < (double)planContext.getAiContext().getGameTime() && this.c >= 5) {
            return PlanResult.FAILURE;
        }
        return null;
    }
}


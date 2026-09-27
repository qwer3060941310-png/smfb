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
import com.desertstormfront.game.player.Player;

/**
 * 规划：监视交战过程
 */
public strictfp final class MonitorEngagePlan
extends MonitorPlanBase {
    private int a;
    private float b;
    private int c;

    @Override
    public String getName() {
        return "MENG";
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
            if (unit != null && unit.getOwner() != null && !player.isEnemyOf(unit.getOwner())) {
                return PlanResult.SUCCESS;
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
                if (unit2.getPosition().distanceSquaredTo(intentGroup.getTargetPosition()) >= 7.0f) {
                    i6 = false;
                    break;
                }
                ++n;
            }
            if (i6) {
                return PlanResult.SUCCESS;
            }
        } else {
            Player player2 = unit.getOwner();
            boolean i6 = intent.getGroupList().hasCapturingUnit();
            boolean bl = unit.getUnitType().isCapturable();
            if (i6 && bl ? player2 != null && !player.isEnemyOf(player2) : unit.isDestroyed() || player2 == null || !player.isEnemyOf(player2)) {
                return PlanResult.SUCCESS;
            }
        }
        if ((double)this.b + 180.0 < (double)planContext.getAiContext().getGameTime() && this.c >= 5) {
            return PlanResult.FAILURE;
        }
        return null;
    }
}


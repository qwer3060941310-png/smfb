/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.intent.IntentGroupMode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.ai.plan.nodes.MonitorPlanBase;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.world.Vec2;

/**
 * 规划：移动游离单位
 */
public strictfp final class MoveRoguePlan
extends MonitorPlanBase {
    @Override
    public String getName() {
        return "MROG";
    }

    @Override
    public void reset() {
    }

    @Override
    public PlanResult monitor(PlanContext planContext) {
        int i8;
        boolean bl;
        Object object;
        Intent intent = planContext.getIntent();
        IntentGroupList intentGroupList = intent.getGroupList();
        if (intent.getTargetPosition() != null) {
            object = intent.getTargetPosition();
            bl = true;
            int n = 0;
            while (n < intentGroupList.size()) {
                UnitList unitList = ((IntentGroup)intentGroupList.get(n)).getUnits();
                i8 = 0;
                while (i8 < unitList.size()) {
                    if (!((Unit)unitList.get(i8)).getPosition().equalsInt((Vec2)object)) {
                        bl = false;
                        break;
                    }
                    ++i8;
                }
                if (!bl) break;
                ++n;
            }
            if (bl) {
                intent.clearTarget();
            }
        }
        if (intent.getTargetPosition() == null) {
            object = planContext.getAiContext();
            bl = false;
            float f = 0.0f;
            float f2 = 0.0f;
            i8 = 0;
            while (i8 < 32 && !bl) {
                int i9 = ((AIContext)object).getRandom().nextInt(((AIContext)object).getTerrainWidth());
                int i10 = ((AIContext)object).getRandom().nextInt(((AIContext)object).getTerrainHeight());
                Unit unit = (Unit)((IntentGroup)intentGroupList.get(0)).getUnits().get(0);
                if (((AIContext)object).isAllWaterTile(i9, i10) && ((AIContext)object).hasPathToPosition(unit, i9, i10)) {
                    bl = true;
                    f = (float)i9 + 0.5f;
                    f2 = (float)i10 + 0.5f;
                }
                ++i8;
            }
            if (bl) {
                intent.setTargetPosition(f, f2);
            } else {
                return null;
            }
        }
        int n = 0;
        while (n < intentGroupList.size()) {
            IntentGroup intentGroup = (IntentGroup)intentGroupList.get(n);
            intentGroup.setTargetPosition(intent.getTargetPosition().getX(), intent.getTargetPosition().getY());
            intentGroup.setMode(IntentGroupMode.b);
            intentGroup.setHasMoveOrder(false);
            ++n;
        }
        return null;
    }
}


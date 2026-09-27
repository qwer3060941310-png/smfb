/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentList;
import com.desertstormfront.ai.intent.IntentType;
import com.desertstormfront.ai.intent.UnitRequest;
import com.desertstormfront.ai.plan.ActionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.game.mode.CaptureTheFlagMode;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.world.UnitPosition;
import com.noblemaster.lib.math.MathHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * 规划：攻占据点
 */
public strictfp final class InitConquerPlan
extends ActionPlanNode {
    private int a;
    private UnitList b = new UnitList();
    private List c = new ArrayList();

    @Override
    public String getName() {
        return "ICAP";
    }

    @Override
    public void reset() {
        this.a = 0;
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        if (this.a == 0) {
            this.b.clear();
            this.c.clear();
            Unit unit = planContext.getAiContext().getGameMode() instanceof CaptureTheFlagMode ? ((CaptureTheFlagMode)planContext.getAiContext().getGameMode()).getFlag() : null;
            UnitType unitType = planContext.getAiContext().getMapDefinition().getUnitTypeSlots().getHumvee().getProducer();
            UnitType unitType2 = planContext.getAiContext().getMapDefinition().getUnitTypeSlots().getOilField();
            UnitList unitList = planContext.getAiContext().getVisibleEnemyOrNeutralUnits();
            UnitList unitList2 = planContext.getAiContext().getOwnUnits();
            int n = 0;
            while (n < unitList.size()) {
                Unit unit2 = (Unit)unitList.get(n);
                int n2 = unit2 == unit ? 9 : (unit2.getUnitType() == unitType ? 3 : (unit2.getUnitType() == unitType2 ? 3 : 0));
                UnitPosition unitPosition = unit2.getPosition();
                int n3 = Integer.MAX_VALUE;
                int n4 = 0;
                while (n4 < unitList2.size()) {
                    Unit unit3 = (Unit)unitList2.get(n4);
                    int i14 = (int)unitPosition.distanceSquaredTo(unit3.getPosition());
                    if (i14 < n3) {
                        n3 = i14;
                    }
                    ++n4;
                }
                n2 -= (int)MathHelper.sqrt((float)n3);
                n4 = 0;
                while (n4 < this.b.size() && (Integer)this.c.get(n4) > n2) {
                    ++n4;
                }
                this.b.add(n4, unit2);
                this.c.add(n4, n2);
                ++n;
            }
            this.a = 1;
            return null;
        }
        IntentList intentList = planContext.getAiContext().getIntents();
        int n = this.b.size() == 0 ? 0 : (intentList.count(IntentType.Conquer) - 1) / this.b.size() + 1;
        while (this.b.size() > 0) {
            Object object;
            Unit unit = (Unit)this.b.remove(0);
            if (intentList.countTargeting(IntentType.Conquer, unit) >= n) continue;
            UnitPosition unitPosition = unit.getPosition();
            UnitList unitList = planContext.getAiContext().getUnassignedUnits();
            Object object2 = null;
            int n5 = Integer.MAX_VALUE;
            int n6 = 0;
            while (n6 < unitList.size()) {
                int n7;
                object = (Unit)unitList.get(n6);
                if (((Unit)object).getUnitType().canCapture() && (n7 = (int)unitPosition.distanceSquaredTo(((Unit)object).getPosition())) < n5) {
                    object2 = object;
                    n5 = n7;
                }
                ++n6;
            }
            if (object2 != null) {
                planContext.getIntent().setTargetUnit(unit);
                IntentGroup intentGroup = IntentGroup.create();
                planContext.getIntent().getGroupList().add(intentGroup);
                planContext.getAiContext().claimUnits(intentGroup.getUnits(), (Unit)object2);
                return PlanResult.SUCCESS;
            }
            UnitType unitType = planContext.getAiContext().getMapDefinition().getUnitTypeSlots().getHumvee();
            object = planContext.getAiContext().getOwnImmobileUnits();
            Unit unit4 = null;
            n5 = Integer.MAX_VALUE;
            int n8 = 0;
            while (n8 < ((ArrayList)object).size()) {
                int i14;
                Unit unit5 = (Unit)((ArrayList)object).get(n8);
                if (unit5.isImmobile() && unit5.getSubUnits().size() < unit5.getUnitType().getCapacity() && planContext.canBuild(unit5, unitType) && (i14 = (int)unitPosition.distanceSquaredTo(unit5.getPosition())) < n5) {
                    unit4 = unit5;
                    n5 = i14;
                }
                ++n8;
            }
            if (unit4 != null) {
                planContext.build(unit4, unitType);
                planContext.getIntent().setTargetUnit(unit);
                IntentGroup intentGroup = IntentGroup.create();
                planContext.getIntent().getGroupList().add(intentGroup);
                intentGroup.getRequests().add(UnitRequest.of(unit4, unitType));
                return PlanResult.SUCCESS;
            }
            return null;
        }
        return PlanResult.FAILURE;
    }
}


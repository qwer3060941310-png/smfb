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
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;
import java.util.ArrayList;
import java.util.List;

/**
 * 规划：发起攻击任务
 */
public strictfp final class InitAttackPlan
extends ActionPlanNode {
    private int a;
    private UnitList b = new UnitList();
    private List c = new ArrayList();

    @Override
    public String getName() {
        return "IATT";
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
            UnitList unitList = planContext.getAiContext().collectVisibleEnemyUnits();
            UnitList unitList2 = planContext.getAiContext().getOwnUnits();
            int n = 0;
            while (n < unitList.size()) {
                Unit unit2 = (Unit)unitList.get(n);
                if (unit2.getOwner() != null && unit2.getPosition().getUnit() == null) {
                    Object object;
                    int i9;
                    int n2 = unit2 == unit ? 5200000 : (unit2.isHighValue() ? 5100000 : (unit2.getUnitType().isTruck() ? 5050000 : ((i9 = (int)(((UnitType)(object = unit2.getUnitType())).isImmobile() ? 1 : 0)) != 0 ? 51000 : (int)((UnitType)object).getHealth() * 100) + (i9 == 0 && ((UnitType)object).canCarry() ? 20000 : 0) + (((UnitType)object).isFlying() ? -5000000 : 0)));
                    object = unit2.getPosition();
                    i9 = Integer.MAX_VALUE;
                    int n3 = 0;
                    while (n3 < unitList2.size()) {
                        Unit unit3 = (Unit)unitList2.get(n3);
                        int n4 = (int)((Vec2)object).distanceSquaredTo(unit3.getPosition());
                        if (n4 < i9) {
                            i9 = n4;
                        }
                        ++n3;
                    }
                    n2 -= i9;
                    n3 = 0;
                    while (n3 < this.b.size() && (Integer)this.c.get(n3) > n2) {
                        ++n3;
                    }
                    this.b.add(n3, unit2);
                    this.c.add(n3, n2);
                }
                ++n;
            }
            this.a = 1;
            return null;
        }
        IntentList intentList = planContext.getAiContext().getIntents();
        int n = this.b.size() == 0 ? 0 : (intentList.count(IntentType.Attack) - 1) / this.b.size() + 1;
        while (this.b.size() > 0) {
            Unit unit = (Unit)this.b.remove(0);
            if (intentList.countTargeting(IntentType.Attack, unit) >= n) continue;
            UnitType unitType = unit.getUnitType();
            UnitPosition unitPosition = unit.getPosition();
            UnitList unitList = planContext.getAiContext().getUnassignedUnits();
            Unit unit4 = null;
            int i9 = Integer.MIN_VALUE;
            int n5 = 0;
            while (n5 < unitList.size()) {
                int n6;
                int n7;
                Unit unit5 = (Unit)unitList.get(n5);
                int n8 = unit5.getUnitType().getDamageAgainst(unitType);
                if (n8 >= 50 && (n7 = n8 - (n6 = (int)unitPosition.distanceTo(unit5.getPosition())) * 10) > i9) {
                    unit4 = unit5;
                    i9 = n7;
                }
                ++n5;
            }
            if (unit4 != null) {
                planContext.getIntent().setTargetUnit(unit);
                IntentGroup intentGroup = IntentGroup.create();
                planContext.getIntent().getGroupList().add(intentGroup);
                planContext.getAiContext().claimUnits(intentGroup.getUnits(), unit4);
                return PlanResult.SUCCESS;
            }
            long l = planContext.getAiContext().getPlayer().getResources() / 3L;
            UnitList unitList3 = planContext.getAiContext().getOwnImmobileUnits();
            Unit unit6 = null;
            UnitType unitType2 = null;
            i9 = Integer.MIN_VALUE;
            int n9 = 0;
            while (n9 < unitList3.size()) {
                Unit unit7 = (Unit)unitList3.get(n9);
                if (unit7.getUnitType().canCarry() && unit7.getSubUnits().size() < unit7.getUnitType().getCapacity()) {
                    int i17 = (int)unitPosition.distanceTo(unit7.getPosition());
                    UnitTypeList unitTypeList = unit7.getUnitType().getProducedBy();
                    int i19 = 0;
                    while (i19 < unitTypeList.size()) {
                        int i22;
                        int i21;
                        UnitType unitType3 = (UnitType)unitTypeList.get(i19);
                        if ((unitType3.getHealth() < 1000L || unitType3.getHealth() < l) && planContext.canBuild(unit7, unitType3) && (i21 = unitType3.getDamageAgainst(unitType)) >= 50 && (i22 = i21 - i17 * 10) > i9) {
                            unit6 = unit7;
                            unitType2 = unitType3;
                            i9 = i22;
                        }
                        ++i19;
                    }
                }
                ++n9;
            }
            if (unit6 != null) {
                planContext.build(unit6, unitType2);
                planContext.getIntent().setTargetUnit(unit);
                IntentGroup intentGroup = IntentGroup.create();
                planContext.getIntent().getGroupList().add(intentGroup);
                intentGroup.getRequests().add(UnitRequest.of(unit6, unitType2));
                return PlanResult.SUCCESS;
            }
            return null;
        }
        return PlanResult.FAILURE;
    }
}


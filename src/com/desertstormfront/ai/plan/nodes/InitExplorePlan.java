/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentList;
import com.desertstormfront.ai.intent.IntentType;
import com.desertstormfront.ai.intent.UnitRequest;
import com.desertstormfront.ai.plan.ActionPlanNode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.FogOfWar;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.Vec2;

/**
 * 规划：探索未知区域
 */
public strictfp final class InitExplorePlan
extends ActionPlanNode {
    @Override
    public String getName() {
        return "IEXP";
    }

    @Override
    public void reset() {
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        int i2 = 0;
        while (i2 < 5) {
            Object object;
            Object object2;
            int i17;
            Object object3;
            ++i2;
            MapDefinition mapDefinition = planContext.getAiContext().getMapDefinition();
            IntentList intentList = planContext.getAiContext().getIntents();
            FogOfWar fogOfWar = planContext.getAiContext().getPlayer().getFogOfWar();
            AIContext aIContext = planContext.getAiContext();
            boolean i7 = false;
            float f8 = 0.0f;
            float f9 = 0.0f;
            int i10 = 0;
            while (i10 < 32 && !i7) {
                float f;
                float f2 = (float)aIContext.getRandom().nextInt(aIContext.getTerrainWidth()) + 0.5f;
                if (!fogOfWar.isExplored((int)f2, (int)(f = (float)aIContext.getRandom().nextInt(aIContext.getTerrainHeight()) + 0.5f)) && intentList.countAt(IntentType.Explore, f2, f) == 0) {
                    i7 = true;
                    f8 = f2;
                    f9 = f;
                }
                ++i10;
            }
            if (!i7) break;
            Domain domain = aIContext.isAllLandTile((int)f8, (int)f9) ? Domain.Ground : Domain.Water;
            UnitList unitList = planContext.getAiContext().getUnassignedUnits();
            Unit unit = null;
            int i14 = Integer.MAX_VALUE;
            int n = 0;
            while (n < unitList.size()) {
                object3 = (Unit)unitList.get(n);
                if (((Unit)object3).getUnitType().getDomain() == domain && (i17 = (int)((Unit)object3).getPosition().distanceSquaredTo(f8, f9)) < i14 && aIContext.hasPathToPosition((Unit)object3, f8, f9)) {
                    unit = (Unit)object3;
                    i14 = i17;
                }
                ++n;
            }
            if (unit != null) {
                planContext.getIntent().setTargetPosition(f8, f9);
                IntentGroup intentGroup = IntentGroup.create();
                planContext.getIntent().getGroupList().add(intentGroup);
                planContext.getAiContext().claimUnits(intentGroup.getUnits(), unit);
                return PlanResult.SUCCESS;
            }
            UnitList unitList2 = planContext.getAiContext().getOwnImmobileUnits();
            object3 = null;
            i14 = Integer.MAX_VALUE;
            i17 = 0;
            while (i17 < unitList2.size()) {
                int i20;
                object2 = (Unit)unitList2.get(i17);
                if (((Unit)object2).getSubUnits().size() < ((Unit)object2).getUnitType().getCapacity() && (i20 = (int)((Vec2)(object = ((Unit)object2).getPosition())).distanceSquaredTo(f8, f9)) < i14 && (domain == Domain.Ground && ((Unit)object2).getUnitType() == mapDefinition.getUnitTypeSlots().getBaseStation() || domain == Domain.Water && ((Unit)object2).getUnitType() == mapDefinition.getUnitTypeSlots().getShipyard()) && aIContext.hasPathForDomain(domain, ((Vec2)object).getX(), ((Vec2)object).getY(), f8, f9)) {
                    object3 = object2;
                    i14 = i20;
                }
                ++i17;
            }
            if (object3 == null) continue;
            if (domain == Domain.Ground && ((Unit)object3).getUnitType() == mapDefinition.getUnitTypeSlots().getBaseStation()) {
                if (planContext.canBuild((Unit)object3, mapDefinition.getUnitTypeSlots().getMissileTank())) {
                    object2 = mapDefinition.getUnitTypeSlots().getMissileTank();
                    planContext.build((Unit)object3, (UnitType)object2);
                    i17 = 1;
                } else {
                    object2 = null;
                    i17 = 0;
                }
            } else if (planContext.canBuild((Unit)object3, mapDefinition.getUnitTypeSlots().getCruiser())) {
                object2 = mapDefinition.getUnitTypeSlots().getCruiser();
                planContext.build((Unit)object3, (UnitType)object2);
                i17 = 1;
            } else if (planContext.canBuild((Unit)object3, mapDefinition.getUnitTypeSlots().getGunboat())) {
                object2 = mapDefinition.getUnitTypeSlots().getGunboat();
                planContext.build((Unit)object3, (UnitType)object2);
                i17 = 1;
            } else {
                object2 = null;
                i17 = 0;
            }
            if (i17 == 0) continue;
            planContext.getIntent().setTargetPosition(f8, f9);
            object = IntentGroup.create();
            planContext.getIntent().getGroupList().add(object);
            ((IntentGroup)object).getRequests().add(UnitRequest.of((Unit)object3, (UnitType)object2));
            return PlanResult.SUCCESS;
        }
        return null;
    }
}


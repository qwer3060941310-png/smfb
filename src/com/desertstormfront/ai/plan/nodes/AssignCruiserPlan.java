/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.intent.UnitRequest;
import com.desertstormfront.ai.intent.UnitRequestList;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.ai.plan.nodes.UnitAssignPlanBase;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.Vec2;
import java.util.ArrayList;

/**
 * 规划：分配巡洋舰
 */
public strictfp final class AssignCruiserPlan
extends UnitAssignPlanBase {
    private int a;

    @Override
    public String getName() {
        return "ACRU";
    }

    @Override
    public void reset() {
        this.a = 0;
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        Object object;
        Vec2 vec2;
        Object object2;
        boolean i3;
        MapDefinition mapDefinition = planContext.getAiContext().getMapDefinition();
        IntentGroupList intentGroupList = planContext.getIntent().getGroupList();
        if (intentGroupList.size() == 0) {
            i3 = true;
            object2 = planContext.getIntent().getTargetPosition();
            vec2 = !planContext.getAiContext().isAllLandTileAtPosition((Vec2)object2) ? planContext.getIntent().getTargetPosition() : null;
        } else {
            object2 = (IntentGroup)intentGroupList.get(0);
            if (((IntentGroup)object2).getDomain() == Domain.Water) {
                UnitType unitType;
                object = ((IntentGroup)object2).getUnits();
                UnitRequestList unitRequestList = ((IntentGroup)object2).getRequests();
                i3 = false;
                int n = 0;
                while (n < ((ArrayList)object).size()) {
                    unitType = ((Unit)((ArrayList)object).get(n)).getUnitType();
                    if (unitType == mapDefinition.getUnitTypeSlots().getCarrier() || unitType == mapDefinition.getUnitTypeSlots().getTransport()) {
                        i3 = true;
                        break;
                    }
                    ++n;
                }
                n = 0;
                while (n < unitRequestList.size()) {
                    unitType = ((UnitRequest)unitRequestList.get(n)).getUnitType();
                    if (unitType == mapDefinition.getUnitTypeSlots().getCarrier() || unitType == mapDefinition.getUnitTypeSlots().getTransport()) {
                        i3 = true;
                        break;
                    }
                    ++n;
                }
            } else {
                i3 = false;
            }
            vec2 = ((IntentGroup)object2).getAnchorPosition();
        }
        if (i3) {
            long l;
            if (this.a == 0) {
                object2 = this.findUnitForType(planContext, mapDefinition.getUnitTypeSlots().getCruiser(), vec2);
                if (object2 != null) {
                    object = IntentGroup.create();
                    planContext.getIntent().getGroupList().add(object);
                    planContext.getAiContext().claimUnits(((IntentGroup)object).getUnits(), (Unit)object2);
                    return PlanResult.SUCCESS;
                }
                this.a = 1;
                return null;
            }
            long l2 = planContext.getAiContext().getPlayer().getResources();
            int n = 0;
            UnitList unitList = planContext.getAiContext().getUnassignedUnits();
            int n2 = 0;
            while (n2 < unitList.size()) {
                UnitType unitType = ((Unit)unitList.get(n2)).getUnitType();
                if (unitType == mapDefinition.getUnitTypeSlots().getGunboat()) {
                    ++n;
                }
                ++n2;
            }
            n2 = 2;
            if (n > n2) {
                n = n2;
            }
            if (l2 >= (l = mapDefinition.getUnitTypeSlots().getCruiser().getHealth() + (long)(n2 - n) * mapDefinition.getUnitTypeSlots().getGunboat().getHealth())) {
                Unit unit = this.findUnitForDomain(planContext, Domain.Water, vec2, mapDefinition.getUnitTypeSlots().getCruiser().getProducer());
                if (unit != null && planContext.canBuild(unit, mapDefinition.getUnitTypeSlots().getCruiser())) {
                    planContext.build(unit, mapDefinition.getUnitTypeSlots().getCruiser());
                    IntentGroup intentGroup = IntentGroup.create();
                    intentGroupList.add(intentGroup);
                    intentGroup.getRequests().add(UnitRequest.of(unit, mapDefinition.getUnitTypeSlots().getCruiser()));
                    return PlanResult.SUCCESS;
                }
                return PlanResult.SUCCESS;
            }
            return PlanResult.SUCCESS;
        }
        return PlanResult.SUCCESS;
    }
}


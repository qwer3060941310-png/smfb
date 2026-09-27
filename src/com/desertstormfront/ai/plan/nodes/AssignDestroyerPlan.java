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
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.Vec2;

/**
 * 规划：分配驱逐舰
 */
public strictfp final class AssignDestroyerPlan
extends UnitAssignPlanBase {
    private int a;
    private int b;

    @Override
    public String getName() {
        return "ADES";
    }

    @Override
    public void reset() {
        this.a = 0;
        this.b = 0;
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        MapDefinition mapDefinition = planContext.getAiContext().getMapDefinition();
        IntentGroupList intentGroupList = planContext.getIntent().getGroupList();
        if (this.a < intentGroupList.size()) {
            Object object;
            int n;
            int i10;
            IntentGroup intentGroup = (IntentGroup)intentGroupList.get(this.a);
            UnitList unitList = intentGroup.getUnits();
            UnitRequestList unitRequestList = intentGroup.getRequests();
            Domain domain = intentGroup.getDomain();
            Vec2 vec2 = intentGroup.getAnchorPosition();
            int i9 = 0;
            if (domain == Domain.Water) {
                i10 = 0;
                n = 0;
                while (n < unitList.size()) {
                    object = ((Unit)unitList.get(n)).getUnitType();
                    if (object == mapDefinition.getUnitTypeSlots().getCarrier() || object == mapDefinition.getUnitTypeSlots().getCruiser() || object == mapDefinition.getUnitTypeSlots().getTransport()) {
                        i10 = 2;
                        break;
                    }
                    ++n;
                }
                n = 0;
                while (n < unitRequestList.size()) {
                    object = ((UnitRequest)unitRequestList.get(n)).getUnitType();
                    if (object == mapDefinition.getUnitTypeSlots().getCarrier() || object == mapDefinition.getUnitTypeSlots().getCruiser() || object == mapDefinition.getUnitTypeSlots().getTransport()) {
                        i10 = 2;
                        break;
                    }
                    ++n;
                }
                n = 0;
                while (n < unitList.size()) {
                    object = (Unit)unitList.get(n);
                    if (((Unit)object).getUnitType() == mapDefinition.getUnitTypeSlots().getGunboat()) {
                        ++i9;
                    }
                    ++n;
                }
                n = 0;
                while (n < unitRequestList.size()) {
                    object = (UnitRequest)unitRequestList.get(n);
                    if (((UnitRequest)object).getUnitType() == mapDefinition.getUnitTypeSlots().getGunboat()) {
                        ++i9;
                    }
                    ++n;
                }
            } else {
                i10 = 0;
            }
            if (this.b < i10) {
                n = 0;
                if (i9 < i10 && (object = this.findUnitForType(planContext, mapDefinition.getUnitTypeSlots().getGunboat(), vec2)) != null) {
                    i9 += planContext.getAiContext().claimUnits(unitList, (Unit)object);
                    n = 1;
                }
                this.b = n != 0 ? ++this.b : i10;
            } else {
                Unit unit = this.findUnitForDomain(planContext, Domain.Water, vec2, mapDefinition.getUnitTypeSlots().getGunboat().getProducer());
                if (unit != null) {
                    int n2 = i9;
                    while (n2 < i10) {
                        if (planContext.canBuild(unit, mapDefinition.getUnitTypeSlots().getGunboat())) {
                            planContext.build(unit, mapDefinition.getUnitTypeSlots().getGunboat());
                            intentGroup.getRequests().add(UnitRequest.of(unit, mapDefinition.getUnitTypeSlots().getGunboat()));
                            ++i9;
                        }
                        ++n2;
                    }
                }
                ++this.a;
                this.b = 0;
            }
        }
        if (this.a >= intentGroupList.size()) {
            return PlanResult.SUCCESS;
        }
        return null;
    }
}


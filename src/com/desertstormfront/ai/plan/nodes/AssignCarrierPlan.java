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

/**
 * 规划：分配航母
 */
public strictfp final class AssignCarrierPlan
extends UnitAssignPlanBase {
    private int a;

    @Override
    public String getName() {
        return "ACAR";
    }

    @Override
    public void reset() {
        this.a = 0;
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        Object object;
        int n;
        UnitList unitList;
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
                unitList = ((IntentGroup)object2).getUnits();
                UnitRequestList unitRequestList = ((IntentGroup)object2).getRequests();
                i3 = false;
                n = 0;
                while (n < unitList.size()) {
                    object = ((Unit)unitList.get(n)).getUnitType();
                    if (object == mapDefinition.getUnitTypeSlots().getCruiser() || object == mapDefinition.getUnitTypeSlots().getTransport()) {
                        i3 = true;
                        break;
                    }
                    ++n;
                }
                n = 0;
                while (n < unitRequestList.size()) {
                    object = ((UnitRequest)unitRequestList.get(n)).getUnitType();
                    if (object == mapDefinition.getUnitTypeSlots().getCruiser() || object == mapDefinition.getUnitTypeSlots().getTransport()) {
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
            long l15;
            int i14;
            int n2;
            if (this.a == 0) {
                Object object3;
                boolean bl = false;
                unitList = planContext.getAiContext().getUnassignedUnits();
                int n3 = 0;
                while (n3 < unitList.size()) {
                    Unit unit = (Unit)unitList.get(n3);
                    if (unit.getUnitType() == mapDefinition.getUnitTypeSlots().getFighterPlane() || unit.getUnitType() == mapDefinition.getUnitTypeSlots().getHelicopter()) {
                        bl = true;
                        break;
                    }
                    ++n3;
                }
                if (!bl) {
                    UnitList unitList2 = planContext.getAiContext().getOwnImmobileUnits();
                    int n4 = 0;
                    while (n4 < unitList2.size()) {
                        object = (Unit)unitList2.get(n4);
                        if (((Unit)object).getUnitType() == planContext.getAiContext().getMapDefinition().getUnitTypeSlots().getAirfield()) {
                            bl = true;
                            break;
                        }
                        ++n4;
                    }
                }
                if (!bl) {
                    return PlanResult.SUCCESS;
                }
                UnitList unitList3 = planContext.getAiContext().getOwnImmobileUnits();
                Vec2 vec22 = planContext.getIntent().getTargetPosition();
                int n5 = 0;
                while (n5 < unitList3.size()) {
                    object3 = (Unit)unitList3.get(n5);
                    if (((Unit)object3).getUnitType() == planContext.getAiContext().getMapDefinition().getUnitTypeSlots().getAirfield() && vec22.distanceTo(((Unit)object3).getPosition()) < mapDefinition.getUnitTypeSlots().getHelicopter().getVerticalOffset() - 2.0f) {
                        return PlanResult.SUCCESS;
                    }
                    ++n5;
                }
                Unit unit = this.findUnitForType(planContext, mapDefinition.getUnitTypeSlots().getCarrier(), vec2);
                if (unit != null) {
                    object3 = IntentGroup.create();
                    planContext.getIntent().getGroupList().add(object3);
                    planContext.getAiContext().claimUnits(((IntentGroup)object3).getUnits(), unit);
                    return PlanResult.SUCCESS;
                }
                this.a = 1;
                return null;
            }
            long l = planContext.getAiContext().getPlayer().getResources();
            int n6 = 0;
            n = 0;
            int n7 = 0;
            UnitList unitList4 = planContext.getAiContext().getUnassignedUnits();
            int i12 = 0;
            while (i12 < unitList4.size()) {
                UnitType unitType = ((Unit)unitList4.get(i12)).getUnitType();
                if (unitType == mapDefinition.getUnitTypeSlots().getGunboat()) {
                    ++n6;
                } else if (unitType == mapDefinition.getUnitTypeSlots().getFighterPlane()) {
                    ++n;
                } else if (unitType == mapDefinition.getUnitTypeSlots().getHelicopter()) {
                    ++n7;
                }
                ++i12;
            }
            i12 = 2;
            if (n6 > i12) {
                n6 = i12;
            }
            if (n > (n2 = 2)) {
                n = n2;
            }
            if (n7 > (i14 = 2)) {
                n7 = i14;
            }
            if (l >= (l15 = mapDefinition.getUnitTypeSlots().getCarrier().getHealth() + (long)(i12 - n6) * mapDefinition.getUnitTypeSlots().getGunboat().getHealth() + (long)(n2 - n) * mapDefinition.getUnitTypeSlots().getFighterPlane().getHealth() + (long)(i14 - n7) * mapDefinition.getUnitTypeSlots().getHelicopter().getHealth())) {
                Unit unit = this.findUnitForDomain(planContext, Domain.Water, vec2, mapDefinition.getUnitTypeSlots().getCarrier().getProducer());
                if (unit != null && planContext.canBuild(unit, mapDefinition.getUnitTypeSlots().getCarrier())) {
                    planContext.build(unit, mapDefinition.getUnitTypeSlots().getCarrier());
                    IntentGroup intentGroup = IntentGroup.create();
                    intentGroupList.add(intentGroup);
                    intentGroup.getRequests().add(UnitRequest.of(unit, mapDefinition.getUnitTypeSlots().getCarrier()));
                    return PlanResult.SUCCESS;
                }
                return PlanResult.SUCCESS;
            }
            return PlanResult.SUCCESS;
        }
        return PlanResult.SUCCESS;
    }
}


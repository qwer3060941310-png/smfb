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
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;
import java.util.ArrayList;

/**
 * 规划：分配空中单位
 */
public strictfp final class AssignAirPlan
extends UnitAssignPlanBase {
    private int a;
    private int b;

    @Override
    public String getName() {
        return "AAIR";
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
            Object object2;
            int n;
            IntentGroup intentGroup = (IntentGroup)intentGroupList.get(this.a);
            UnitList unitList = intentGroup.getUnits();
            UnitRequestList unitRequestList = intentGroup.getRequests();
            int n2 = 0;
            UnitPosition unitPosition = null;
            if (intentGroup.hasUnitOfType(mapDefinition.getUnitTypeSlots().getCarrier()) || intentGroup.hasRequestFor(mapDefinition.getUnitTypeSlots().getCarrier())) {
                n = 4;
                object2 = null;
                int n3 = 0;
                while (n3 < unitList.size()) {
                    if (((Unit)unitList.get(n3)).getUnitType() == mapDefinition.getUnitTypeSlots().getCarrier()) {
                        object2 = (Unit)unitList.get(n3);
                        break;
                    }
                    ++n3;
                }
                if (object2 != null) {
                    unitPosition = ((Unit)object2).getPosition();
                } else {
                    n3 = 0;
                    while (n3 < unitRequestList.size()) {
                        object = (UnitRequest)unitRequestList.get(n3);
                        if (((UnitRequest)object).getUnitType() == mapDefinition.getUnitTypeSlots().getCarrier()) {
                            unitPosition = ((UnitRequest)object).getUnit().getPosition();
                        }
                        ++n3;
                    }
                }
                n3 = 0;
                while (n3 < unitList.size()) {
                    object = (Unit)unitList.get(n3);
                    if (((Unit)object).getUnitType().getDomain() == Domain.Air) {
                        ++n2;
                    }
                    ++n3;
                }
                n3 = 0;
                while (n3 < unitRequestList.size()) {
                    object = (UnitRequest)unitRequestList.get(n3);
                    if (((UnitRequest)object).getUnitType().getDomain() == Domain.Air) {
                        ++n2;
                    }
                    ++n3;
                }
            } else {
                n = 0;
            }
            if (this.b < n) {
                boolean bl = false;
                if (n2 < n) {
                    Vec2[] vec2Array;
                    UnitType unitType = (this.b & 1) == 0 ? mapDefinition.getUnitTypeSlots().getHelicopter() : mapDefinition.getUnitTypeSlots().getFighterPlane();
                    object = planContext.getAiContext().getUnassignedUnits();
                    Unit unit = null;
                    float f = Float.MAX_VALUE;
                    int n4 = 0;
                    while (n4 < ((ArrayList)object).size()) {
                        float f2;
                        Unit unit2 = (Unit)((ArrayList)object).get(n4);
                        if (unit2.getUnitType() == unitType && unit2.getPosition().getUnit() != null && unit2.getPosition().getUnit().isImmobile() && (f2 = unitPosition.distanceSquaredTo(unit2.getPosition())) < f) {
                            unit = unit2;
                            f = f2;
                        }
                        ++n4;
                    }
                    if (unit != null && (vec2Array = planContext.getAiContext().getSurroundingTiles(unit.getPosition())) != null) {
                        boolean bl2 = false;
                        int n5 = 0;
                        while (n5 < vec2Array.length) {
                            Vec2 vec2 = vec2Array[n5];
                            if (vec2.distanceTo(unit.getPosition()) < mapDefinition.getUnitTypeSlots().getHelicopter().getAltitude() - 6.0f && planContext.getAiContext().hasPathForDomain(Domain.Water, unitPosition.getX(), unitPosition.getY(), vec2.getX(), vec2.getY())) {
                                bl2 = true;
                                break;
                            }
                            ++n5;
                        }
                        if (bl2) {
                            n2 += planContext.getAiContext().claimUnits(intentGroup.getUnits(), unit);
                            bl = true;
                        }
                    }
                }
                this.b = bl ? ++this.b : n;
                return null;
            }
            if (n > 0) {
                Vec2[] vec2Array;
                object2 = planContext.getAiContext().getOwnImmobileUnits();
                Unit unit = null;
                float f = Float.MAX_VALUE;
                int n6 = 0;
                while (n6 < ((ArrayList)object2).size()) {
                    float f3;
                    Unit unit3 = (Unit)((ArrayList)object2).get(n6);
                    if (unit3.getUnitType() == planContext.getAiContext().getMapDefinition().getUnitTypeSlots().getAirfield() && (f3 = unitPosition.distanceSquaredTo(unit3.getPosition())) < f) {
                        unit = unit3;
                        f = f3;
                    }
                    ++n6;
                }
                if (unit != null && (vec2Array = planContext.getAiContext().getSurroundingTiles(unit.getPosition())) != null) {
                    Object object3;
                    boolean bl = false;
                    int n7 = 0;
                    while (n7 < vec2Array.length) {
                        object3 = vec2Array[n7];
                        if (((Vec2)object3).distanceTo(unit.getPosition()) < mapDefinition.getUnitTypeSlots().getHelicopter().getAltitude() - 6.0f && planContext.getAiContext().hasPathForDomain(Domain.Water, unitPosition.getX(), unitPosition.getY(), ((Vec2)object3).getX(), ((Vec2)object3).getY())) {
                            bl = true;
                            break;
                        }
                        ++n7;
                    }
                    if (bl) {
                        n7 = n2;
                        while (n7 < n) {
                            Object object4 = object3 = (n7 - n2 & 1) == 0 ? mapDefinition.getUnitTypeSlots().getHelicopter() : mapDefinition.getUnitTypeSlots().getFighterPlane();
                            if (planContext.canBuild(unit, (UnitType)object3)) {
                                planContext.build(unit, (UnitType)object3);
                                intentGroup.getRequests().add(UnitRequest.of(unit, (UnitType)object3));
                                ++n2;
                            }
                            ++n7;
                        }
                    }
                }
            }
            ++this.a;
            this.b = 0;
            return null;
        }
        if (!intentGroupList.hasUnitOfType(mapDefinition.getUnitTypeSlots().getCarrier())) {
            Vec2 vec2 = planContext.getIntent().getTargetPosition();
            UnitList unitList = planContext.getAiContext().getOwnImmobileUnits();
            Unit unit = null;
            float f = mapDefinition.getUnitTypeSlots().getHelicopter().getVerticalOffset() - 1.0f;
            int n = 0;
            while (n < unitList.size()) {
                float f4;
                Unit unit4 = (Unit)unitList.get(n);
                if (unit4.getUnitType() == planContext.getAiContext().getMapDefinition().getUnitTypeSlots().getAirfield() && (f4 = vec2.distanceSquaredTo(unit4.getPosition())) < f) {
                    unit = unit4;
                    f = f4;
                }
                ++n;
            }
            if (unit != null) {
                IntentGroup intentGroup = null;
                int n8 = 0;
                int n9 = 4;
                UnitList unitList2 = planContext.getAiContext().getUnassignedUnits();
                int n10 = 0;
                while (n10 < unitList2.size()) {
                    boolean bl;
                    Unit unit5 = (Unit)unitList2.get(n10);
                    boolean bl3 = unit.getSubUnits().contains(unit5);
                    boolean bl4 = unit5.getGuardPositionOrNull() != null && unit5.getGuardPositionOrNull().getUnit() == unit;
                    boolean bl5 = bl = unit5.getPosition().getUnit() != null && unit5.getPosition().getUnit().isImmobile() && unit.getPosition().distanceTo(unit5.getPosition()) < f;
                    if (bl3 || bl4 || bl) {
                        if (intentGroup == null) {
                            intentGroup = IntentGroup.create();
                            intentGroupList.add(intentGroup);
                        }
                        n8 += planContext.getAiContext().claimUnits(intentGroup.getUnits(), unit5);
                        continue;
                    }
                    ++n10;
                }
                int n11 = n8;
                while (n11 < n9) {
                    UnitType unitType;
                    UnitType unitType2 = unitType = (n11 - n8 & 1) == 0 ? mapDefinition.getUnitTypeSlots().getHelicopter() : mapDefinition.getUnitTypeSlots().getFighterPlane();
                    if (planContext.canBuild(unit, unitType)) {
                        planContext.build(unit, unitType);
                        if (intentGroup == null) {
                            intentGroup = IntentGroup.create();
                            intentGroupList.add(intentGroup);
                        }
                        intentGroup.getRequests().add(UnitRequest.of(unit, unitType));
                        ++n8;
                    }
                    ++n11;
                }
            }
        }
        return PlanResult.SUCCESS;
    }
}


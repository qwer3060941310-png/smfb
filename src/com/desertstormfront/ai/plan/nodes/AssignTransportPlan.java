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
import com.noblemaster.lib.log.OsfLog;

/**
 * 规划：分配运输单位
 */
public strictfp final class AssignTransportPlan
extends UnitAssignPlanBase {
    private int a;
    private int b;

    @Override
    public String getName() {
        return "ATRA";
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
            IntentGroup intentGroup = (IntentGroup)intentGroupList.get(this.a);
            UnitList unitList = intentGroup.getUnits();
            UnitRequestList unitRequestList = intentGroup.getRequests();
            Domain domain = intentGroup.getDomain();
            Vec2 vec2 = intentGroup.getAnchorPosition();
            if (domain == Domain.Ground || domain == Domain.Air) {
                if (this.b == 0) {
                    if (planContext.getAiContext().hasPathForDomain(domain, vec2.getX(), vec2.getY(), planContext.getIntent().getTargetPosition().getX(), planContext.getIntent().getTargetPosition().getY())) {
                        boolean bl;
                        if (domain == Domain.Air) {
                            UnitType unitType;
                            float f = 0.0f;
                            int n = 0;
                            while (n < unitList.size()) {
                                unitType = ((Unit)unitList.get(n)).getUnitType();
                                if (unitType.getDomain() == Domain.Air) {
                                    if (!unitType.isFlying()) {
                                        f = Float.MAX_VALUE;
                                    } else if (unitType.getVerticalOffset() > f) {
                                        f = unitType.getVerticalOffset();
                                    }
                                }
                                ++n;
                            }
                            n = 0;
                            while (n < unitRequestList.size()) {
                                unitType = ((UnitRequest)unitRequestList.get(n)).getUnitType();
                                if (unitType.getDomain() == Domain.Air) {
                                    if (!unitType.isFlying()) {
                                        f = Float.MAX_VALUE;
                                    } else if (unitType.getVerticalOffset() > f) {
                                        f = unitType.getVerticalOffset();
                                    }
                                }
                                ++n;
                            }
                            bl = f >= vec2.distanceTo(planContext.getIntent().getTargetPosition());
                        } else {
                            bl = true;
                        }
                        if (bl) {
                            ++this.a;
                            this.b = 0;
                        } else {
                            this.b = 1;
                        }
                    } else {
                        this.b = 1;
                    }
                } else if (this.b == 1) {
                    UnitList unitList2 = planContext.getAiContext().getUnassignedUnits();
                    Vec2 vec22 = planContext.getIntent().getTargetPosition();
                    Unit unit = null;
                    float f = 2.14748365E9f;
                    int n = 0;
                    while (n < unitList2.size()) {
                        float f16;
                        Unit unit2 = (Unit)unitList2.get(n);
                        UnitType unitType = unit2.getUnitType();
                        if ((domain == Domain.Ground && (unitType == mapDefinition.getUnitTypeSlots().getTransport() || unitType == mapDefinition.getUnitTypeSlots().getAirTransport()) || domain == Domain.Air && unitType == mapDefinition.getUnitTypeSlots().getCarrier()) && (f16 = vec22.distanceSquaredTo(unit2.getPosition())) < f) {
                            unit = unit2;
                            f = f16;
                        }
                        ++n;
                    }
                    if (unit != null) {
                        planContext.getAiContext().claimUnits(unitList, unit);
                        ++this.a;
                        this.b = 0;
                    } else {
                        this.b = 2;
                    }
                } else {
                    UnitList unitList3 = planContext.getAiContext().getOwnImmobileUnits();
                    Vec2 vec23 = planContext.getIntent().getTargetPosition();
                    Unit unit = null;
                    UnitType unitType = null;
                    float f = 2.14748365E9f;
                    int n = 0;
                    while (n < unitList3.size()) {
                        float f16;
                        Unit unit3 = (Unit)unitList3.get(n);
                        if (unit3.getUnitType().canCarry() && unit3.getSubUnits().size() < unit3.getUnitType().getCapacity() && (f16 = vec23.distanceSquaredTo(unit3.getPosition())) < f) {
                            UnitType unitType2;
                            if (domain == Domain.Ground) {
                                unitType2 = unit3.getUnitType() == mapDefinition.getUnitTypeSlots().getAirfield() ? mapDefinition.getUnitTypeSlots().getAirTransport() : mapDefinition.getUnitTypeSlots().getTransport();
                            } else if (domain == Domain.Air) {
                                unitType2 = mapDefinition.getUnitTypeSlots().getCarrier();
                            } else {
                                OsfLog.error("Transport for type not implemented: " + (Object)((Object)domain));
                                return PlanResult.FAILURE;
                            }
                            if (planContext.canBuild(unit3, unitType2)) {
                                unit = unit3;
                                unitType = unitType2;
                                f = f16;
                            }
                        }
                        ++n;
                    }
                    if (unit != null) {
                        planContext.build(unit, unitType);
                        intentGroup.getRequests().add(UnitRequest.of(unit, unitType));
                    } else if (this.a == 0) {
                        return PlanResult.FAILURE;
                    }
                    ++this.a;
                    this.b = 0;
                }
            } else {
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


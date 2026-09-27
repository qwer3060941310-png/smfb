/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.AIContext;
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
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.game.model.UnitTypeSlots;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;

/**
 * 规划：分配守卫单位
 */
public strictfp final class AssignGuardPlan
extends UnitAssignPlanBase {
    private UnitTypeList combatTypes;
    private int groupIndex;
    private int assignedCount;

    @Override
    public String getName() {
        return "AGRD";
    }

    @Override
    public void reset() {
        this.groupIndex = 0;
        this.assignedCount = 0;
    }

    @Override
    public PlanResult run(PlanContext planContext) {
        MapDefinition mapDefinition = planContext.getAiContext().getMapDefinition();
        IntentGroupList intentGroupList = planContext.getIntent().getGroupList();
        if (this.groupIndex < intentGroupList.size()) {
            Object object;
            int i9;
            int n;
            IntentGroup intentGroup = (IntentGroup)intentGroupList.get(this.groupIndex);
            UnitList unitList = intentGroup.getUnits();
            UnitRequestList unitRequestList = intentGroup.getRequests();
            Domain domain = intentGroup.getDomain();
            int i8 = 0;
            Unit unit = null;
            UnitPosition unitPosition = null;
            if (domain == Domain.Water) {
                Unit unit2 = null;
                n = 0;
                while (n < unitList.size()) {
                    if (((Unit)unitList.get(n)).getUnitType() == mapDefinition.getUnitTypeSlots().getTransport()) {
                        unit2 = (Unit)unitList.get(n);
                        break;
                    }
                    ++n;
                }
                if (unit2 != null) {
                    i9 = unit2.getUnitType().getCapacity();
                    n = 0;
                    while (n < unitList.size()) {
                        object = (Unit)unitList.get(n);
                        if (((Unit)object).getUnitType().getDomain() == Domain.Ground) {
                            if (((Unit)object).getPosition().getUnit() != null && ((Unit)object).getPosition().getUnit().isImmobile()) {
                                unit = ((Unit)object).getPosition().getUnit();
                            }
                            unitPosition = ((Unit)object).getPosition();
                            ++i8;
                        }
                        ++n;
                    }
                    n = 0;
                    while (n < unitRequestList.size()) {
                        object = (UnitRequest)unitRequestList.get(n);
                        if (((UnitRequest)object).getUnitType().getDomain() == Domain.Ground) {
                            unit = ((UnitRequest)object).getUnit();
                            ++i8;
                        }
                        ++n;
                    }
                } else {
                    i9 = 0;
                }
            } else if (domain == Domain.Ground) {
                float f = planContext.getAiContext().getInfluenceMap().getInfluence(planContext.getIntent().getTargetPosition());
                i9 = (int)(f / 2.3f);
                if (i9 >= 7) {
                    i9 = 6;
                }
                n = 0;
                while (n < unitList.size()) {
                    object = (Unit)unitList.get(n);
                    if (((Unit)object).getUnitType().getDomain() == Domain.Ground) {
                        if (((Unit)object).getPosition().getUnit() != null && ((Unit)object).getPosition().getUnit().isImmobile()) {
                            unit = ((Unit)object).getPosition().getUnit();
                        }
                        unitPosition = ((Unit)object).getPosition();
                        ++i8;
                    }
                    ++n;
                }
                n = 0;
                while (n < unitRequestList.size()) {
                    object = (UnitRequest)unitRequestList.get(n);
                    if (((UnitRequest)object).getUnitType().getDomain() == Domain.Ground) {
                        unit = ((UnitRequest)object).getUnit();
                        ++i8;
                    }
                    ++n;
                }
            } else {
                i9 = 0;
            }
            if (i9 > 0 && unitPosition == null && unit == null) {
                ++this.groupIndex;
                this.assignedCount = 0;
            } else if (this.assignedCount < i9) {
                boolean bl = false;
                if (i8 < i9) {
                    Unit unit3 = null;
                    if ((this.assignedCount & 1) == 0) {
                        object = mapDefinition.getUnitTypeSlots().getHumvee();
                        unit3 = this.findUnitForType(planContext, (UnitType)object, unitPosition != null ? unitPosition : unit.getPosition());
                    }
                    if (unit3 == null) {
                        if (this.combatTypes == null) {
                            object = mapDefinition.getUnitTypeSlots();
                            this.combatTypes = new UnitTypeList(8);
                            this.combatTypes.add(((UnitTypeSlots)object).getBattleTank());
                            this.combatTypes.add(((UnitTypeSlots)object).getArtillery());
                            if (((UnitTypeSlots)object).getMechanic() != null) {
                                this.combatTypes.add(((UnitTypeSlots)object).getMechanic());
                            }
                            if (((UnitTypeSlots)object).getFourByFour() != null) {
                                this.combatTypes.add(((UnitTypeSlots)object).getFourByFour());
                            }
                        }
                        unit3 = this.findUnitForTypes(planContext, this.combatTypes, (Vec2)(unitPosition != null ? unitPosition : unit.getPosition()), 169.0f);
                    }
                    if (unit3 != null) {
                        i8 += planContext.getAiContext().claimUnits(unitList, unit3);
                        bl = true;
                    }
                }
                this.assignedCount = bl ? ++this.assignedCount : i9;
            } else {
                if (unit == null && unitPosition != null) {
                    unit = this.findUnitForDomain(planContext, Domain.Ground, unitPosition, mapDefinition.getUnitTypeSlots().getBaseStation());
                }
                if (unit != null) {
                    UnitType unitType;
                    int n2 = i8;
                    while (n2 < i9 - 1) {
                        if (planContext.canBuild(unit, mapDefinition.getUnitTypeSlots().getHumvee())) {
                            planContext.build(unit, mapDefinition.getUnitTypeSlots().getHumvee());
                            intentGroup.getRequests().add(UnitRequest.of(unit, mapDefinition.getUnitTypeSlots().getHumvee()));
                            ++i8;
                        }
                        ++n2;
                    }
                    if (i8 < i9 && planContext.canBuild(unit, unitType = this.pickCombatUnitType(planContext.getAiContext()))) {
                        planContext.build(unit, unitType);
                        intentGroup.getRequests().add(UnitRequest.of(unit, unitType));
                        ++i8;
                    }
                }
                ++this.groupIndex;
                this.assignedCount = 0;
            }
        }
        if (this.groupIndex >= intentGroupList.size()) {
            return PlanResult.SUCCESS;
        }
        return null;
    }

    private UnitType pickCombatUnitType(AIContext aIContext) {
        UnitTypeSlots unitTypeSlots = aIContext.getMapDefinition().getUnitTypeSlots();
        int i3 = aIContext.getRandom().nextInt(4);
        if (i3 < 2) {
            return unitTypeSlots.getBattleTank();
        }
        if (unitTypeSlots.getMechanic() != null) {
            if (i3 < 3) {
                return unitTypeSlots.getArtillery();
            }
            return unitTypeSlots.getMechanic();
        }
        return unitTypeSlots.getArtillery();
    }
}


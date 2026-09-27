/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.intent.IntentGroupMode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.ai.plan.nodes.MonitorPlanBase;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.map.MapDefinition;
import com.noblemaster.lib.log.OsfLog;

/**
 * 规划：卸载单位
 */
public strictfp final class UnloadPlan
extends MonitorPlanBase {
    private int a;

    @Override
    public String getName() {
        return "ULOD";
    }

    @Override
    public void reset() {
        this.a = 0;
    }

    @Override
    public PlanResult monitor(PlanContext planContext) {
        MapDefinition mapDefinition = planContext.getAiContext().getMapDefinition();
        Intent intent = planContext.getIntent();
        IntentGroupList intentGroupList = intent.getGroupList();
        if (this.a == 0) {
            int i5 = 0;
            while (i5 < intentGroupList.size()) {
                IntentGroup intentGroup = (IntentGroup)intentGroupList.get(i5);
                Unit unit = intentGroup.getHighestSortWeightUnit();
                if (unit.getUnitType().canCarry()) {
                    UnitType unitType = unit.getUnitType();
                    if (unitType == mapDefinition.getUnitTypeSlots().getTransport()) {
                        intentGroup.setTargetPosition(unit.getPosition().getX(), unit.getPosition().getY());
                        intentGroup.setMode(IntentGroupMode.d);
                        intentGroup.setHasMoveOrder(false);
                    } else if (unitType == mapDefinition.getUnitTypeSlots().getAirTransport()) {
                        intentGroup.setTargetPosition(unit.getPosition().getX(), unit.getPosition().getY());
                        intentGroup.setMode(IntentGroupMode.d);
                        intentGroup.setHasMoveOrder(false);
                    } else if (unitType == mapDefinition.getUnitTypeSlots().getCarrier()) {
                        Unit unit2;
                        Unit unit3 = intent.getTargetUnit();
                        Unit unit4 = unit2 = unit.getSubUnits().size() > 0 ? (Unit)unit.getSubUnits().get(0) : null;
                        if (unit3 != null && unit2 != null && unit3.canEmbark(unit2)) {
                            intentGroup.setTargetUnit(unit3);
                            intentGroup.setMode(IntentGroupMode.d);
                            intentGroup.setHasMoveOrder(false);
                        }
                    } else {
                        OsfLog.info("Unknown host encountered: " + unit);
                    }
                }
                ++i5;
            }
            this.a = 1;
            return null;
        }
        int i5 = 0;
        while (i5 < intentGroupList.size()) {
            IntentGroup intentGroup = (IntentGroup)intentGroupList.get(i5);
            UnitList unitList = intentGroup.getUnits();
            Unit unit = intentGroup.getHighestSortWeightUnit();
            if (unit.getUnitType().canCarry()) {
                UnitType unitType = unit.getUnitType();
                if (unitType == mapDefinition.getUnitTypeSlots().getTransport()) {
                    if (unit.getSubUnits().size() > 0) {
                        return null;
                    }
                    unitList.remove(unit);
                    int n = 0;
                    while (n < unitList.size()) {
                        Unit unit5 = (Unit)unitList.get(n);
                        if (!unit.canEmbark(unit5)) {
                            unitList.remove(n);
                            continue;
                        }
                        ++n;
                    }
                } else if (unitType == mapDefinition.getUnitTypeSlots().getAirTransport()) {
                    if (unit.getSubUnits().size() > 0) {
                        return null;
                    }
                    intentGroup.getUnits().remove(unit);
                } else if (unitType == mapDefinition.getUnitTypeSlots().getCarrier()) {
                    Unit unit6 = intent.getTargetUnit();
                    if (unit6 != null && unit6.getOwner() == planContext.getAiContext().getPlayer() && unit6.getUnitType().getCanProduce().contains(mapDefinition.getUnitTypeSlots().getFighterPlane())) {
                        if (unit.getSubUnits().size() > 0) {
                            return null;
                        }
                        unitList.remove(unit);
                        int n = 0;
                        while (n < unitList.size()) {
                            Unit unit7 = (Unit)unitList.get(n);
                            if (!unit.canEmbark(unit7)) {
                                unitList.remove(n);
                                continue;
                            }
                            ++n;
                        }
                    }
                } else {
                    OsfLog.info("Unknown host encountered: " + unit);
                }
            }
            ++i5;
        }
        return PlanResult.SUCCESS;
    }
}


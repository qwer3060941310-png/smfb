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
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.Neighbor;
import com.desertstormfront.world.Vec2;
import com.noblemaster.lib.log.OsfLog;

/**
 * 规划：装载单位上载具
 */
public strictfp final class LoadPlan
extends MonitorPlanBase {
    private int a;
    private float b;
    private int c;

    @Override
    public String getName() {
        return "LOAD";
    }

    @Override
    public void reset() {
        this.a = 0;
    }

    @Override
    public PlanResult monitor(PlanContext planContext) {
        int i5;
        MapDefinition mapDefinition = planContext.getAiContext().getMapDefinition();
        Intent intent = planContext.getIntent();
        IntentGroupList intentGroupList = intent.getGroupList();
        if (this.a == 0) {
            this.c = 0;
            i5 = 0;
            while (i5 < intentGroupList.size()) {
                ((IntentGroup)intentGroupList.get(i5)).setMode(IntentGroupMode.a);
                ++i5;
            }
            this.a = 1;
        }
        i5 = 1;
        int i6 = 0;
        while (i6 < intentGroupList.size()) {
            IntentGroup intentGroup = (IntentGroup)intentGroupList.get(i6);
            Unit unit = intentGroup.getHighestSortWeightUnit();
            if (unit.getUnitType().canCarry()) {
                UnitList unitList = intentGroup.getUnits();
                UnitTypeList unitTypeList = unit.getUnitType().getCanProduce();
                Unit unit2 = null;
                int i12 = 0;
                while (i12 < unitList.size()) {
                    Unit unit3 = (Unit)unitList.get(i12);
                    if (unitTypeList.contains(unit3.getUnitType()) && unit3.getPosition().getUnit() != unit) {
                        unit2 = unit3;
                        break;
                    }
                    ++i12;
                }
                if (unit2 != null) {
                    i5 = 0;
                    if (intentGroup.getMode() == IntentGroupMode.a) {
                        i12 = 0;
                        float f = 0.0f;
                        float f14 = 0.0f;
                        UnitType unitType = unit.getUnitType();
                        if (unitType == mapDefinition.getUnitTypeSlots().getTransport()) {
                            Vec2[] vec2Array = planContext.getAiContext().getSurroundingTiles(unit2.getPosition());
                            if (vec2Array != null) {
                                float f17 = Float.MAX_VALUE;
                                Vec2 vec2 = null;
                                int i19 = 0;
                                while (i19 < vec2Array.length) {
                                    Vec2 vec22 = vec2Array[i19];
                                    float f21 = vec22.distanceTo(unit.getPosition()) + vec22.distanceTo(unit2.getPosition());
                                    if (f21 < f17 && planContext.getAiContext().hasPathToPosition(unit, vec22.getX(), vec22.getY()) && !planContext.getAiContext().getIntents().hasTargetAt(vec22)) {
                                        vec2 = vec22;
                                        f17 = f21;
                                    }
                                    ++i19;
                                }
                                if (vec2 != null) {
                                    i12 = 1;
                                    f = vec2.getX();
                                    f14 = vec2.getY();
                                }
                            }
                        } else if (unitType == mapDefinition.getUnitTypeSlots().getAirTransport()) {
                            i12 = 1;
                            f = unit2.getPosition().getX();
                            f14 = unit2.getPosition().getY();
                        } else if (unitType == mapDefinition.getUnitTypeSlots().getCarrier()) {
                            Vec2[] vec2Array = planContext.getAiContext().getSurroundingTiles(unit2.getPosition());
                            if (vec2Array != null) {
                                Vec2 object;
                                float f17 = mapDefinition.getUnitTypeSlots().getHelicopter().getAltitude() - 4.0f;
                                Vec2 vec2 = null;
                                int i19 = 0;
                                while (i19 < vec2Array.length) {
                                    object = vec2Array[i19];
                                    float f21 = object.distanceTo(unit.getPosition()) + object.distanceTo(unit2.getPosition());
                                    if (f21 < f17 && planContext.getAiContext().hasPathToPosition(unit, object.getX(), object.getY())) {
                                        vec2 = object;
                                        f17 = f21;
                                    }
                                    ++i19;
                                }
                                if (vec2 != null) {
                                    i12 = 1;
                                    f = vec2.getX();
                                    f14 = vec2.getY();
                                    i19 = 0;
                                    while (i19 < 3) {
                                        Neighbor neighbor = planContext.getAiContext().findNeighbor(Domain.Water, f, f14, unit.getPosition().getX(), unit.getPosition().getY());
                                        if (neighbor.isValid()) {
                                            f += (float)neighbor.getDx();
                                            f14 += (float)neighbor.getDy();
                                        }
                                        ++i19;
                                    }
                                }
                            }
                        } else {
                            OsfLog.error("Unknown host encountered: " + unit);
                            i12 = 0;
                        }
                        if (i12 != 0) {
                            intentGroup.setTargetPosition(f, f14);
                            intentGroup.setMode(IntentGroupMode.c);
                            intentGroup.setHasMoveOrder(false);
                        } else {
                            intentGroupList.remove(i6);
                        }
                        return null;
                    }
                }
            }
            ++i6;
        }
        if (i5 != 0) {
            return PlanResult.SUCCESS;
        }
        if (this.c == 0) {
            this.b = planContext.getAiContext().getGameTime();
        }
        ++this.c;
        if ((double)this.b + 180.0 < (double)planContext.getAiContext().getGameTime() && this.c >= 5) {
            return PlanResult.FAILURE;
        }
        return null;
    }
}


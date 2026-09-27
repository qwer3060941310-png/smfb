/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan.nodes;

import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.intent.IntentGroupMode;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.ai.plan.nodes.MonitorPlanBase;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.Neighbor;
import com.desertstormfront.world.Vec2;

/**
 * 规划：移动至目标位置
 */
public strictfp final class MoveToTargetPlan
extends MonitorPlanBase {
    private int a;

    @Override
    public void reset() {
        this.a = 0;
    }

    @Override
    public String getName() {
        return "M2TA";
    }

    @Override
    public PlanResult monitor(PlanContext planContext) {
        int n;
        MapDefinition mapDefinition = planContext.getAiContext().getMapDefinition();
        Intent intent = planContext.getIntent();
        IntentGroupList intentGroupList = intent.getGroupList();
        if (this.a == 0) {
            n = 0;
            while (n < intentGroupList.size()) {
                ((IntentGroup)intentGroupList.get(n)).setMode(IntentGroupMode.a);
                ++n;
            }
            this.a = 1;
        }
        if (this.a == 1) {
            Vec2 vec2 = intent.getTargetPosition();
            int n2 = 0;
            while (n2 < intentGroupList.size()) {
                IntentGroup intentGroup = (IntentGroup)intentGroupList.get(n2);
                if (intentGroup.getMode() == IntentGroupMode.a) {
                    boolean i8 = false;
                    float f9 = 0.0f;
                    float f10 = 0.0f;
                    Unit unit = intentGroup.getHighestSortWeightUnit();
                    UnitType unitType = unit.getUnitType();
                    if (unitType == mapDefinition.getUnitTypeSlots().getTransport()) {
                        Vec2[] vec2Array = planContext.getAiContext().getSurroundingTiles(intent.getTargetPosition());
                        if (vec2Array != null) {
                            float f = Float.MAX_VALUE;
                            Vec2 vec22 = null;
                            int i16 = 0;
                            while (i16 < vec2Array.length) {
                                Vec2 vec23 = vec2Array[i16];
                                float f18 = vec23.distanceTo(unit.getPosition()) + vec23.distanceTo(intent.getTargetPosition());
                                if (f18 < f && planContext.getAiContext().hasPathToPosition(unit, vec23.getX(), vec23.getY()) && !planContext.getAiContext().getIntents().hasTargetAt(vec23)) {
                                    vec22 = vec23;
                                    f = f18;
                                }
                                ++i16;
                            }
                            if (vec22 != null) {
                                i8 = true;
                                f9 = vec22.getX();
                                f10 = vec22.getY();
                            }
                        }
                    } else if (unitType == mapDefinition.getUnitTypeSlots().getAirTransport()) {
                        int n3 = this.findNearestGroundTile(planContext.getAiContext(), unit.getPosition(), (int)vec2.getX(), (int)vec2.getY());
                        if (n3 > 0) {
                            int n4 = planContext.getAiContext().getTerrainWidth();
                            i8 = true;
                            f9 = (float)(n3 % n4) + 0.5f;
                            f10 = (float)(n3 / n4) + 0.5f;
                        }
                    } else if (unitType == mapDefinition.getUnitTypeSlots().getCruiser()) {
                        int n5;
                        Vec2[] vec2Array = planContext.getAiContext().getSurroundingTiles(intent.getTargetPosition());
                        if (vec2Array != null) {
                            Vec2 object;
                            float f = Float.MAX_VALUE;
                            Vec2 vec24 = null;
                            int i16 = 0;
                            while (i16 < vec2Array.length) {
                                object = vec2Array[i16];
                                float f18 = object.distanceTo(intent.getTargetPosition());
                                if (f18 < f && planContext.getAiContext().hasPathToPosition(unit, object.getX(), object.getY())) {
                                    vec24 = object;
                                    f = f18;
                                }
                                ++i16;
                            }
                            if (vec24 != null) {
                                i8 = true;
                                f9 = vec24.getX();
                                f10 = vec24.getY();
                                i16 = 0;
                                while (i16 < 2) {
                                    Neighbor neighbor = planContext.getAiContext().findNeighbor(Domain.Water, f9, f10, unit.getPosition().getX(), unit.getPosition().getY());
                                    if (neighbor.isValid()) {
                                        f9 += (float)neighbor.getDx();
                                        f10 += (float)neighbor.getDy();
                                    }
                                    ++i16;
                                }
                            }
                        } else if (intent.getTargetUnit() != null && intent.getTargetUnit().getUnitType().getDomain() == Domain.Water && (n5 = this.findNearestWaterTile(planContext.getAiContext(), unit.getPosition(), (int)vec2.getX(), (int)vec2.getY())) > 0) {
                            int n6 = planContext.getAiContext().getTerrainWidth();
                            i8 = true;
                            f9 = (float)(n5 % n6) + 0.5f;
                            f10 = (float)(n5 / n6) + 0.5f;
                        }
                    } else if (unitType == mapDefinition.getUnitTypeSlots().getCarrier()) {
                        int n7;
                        Vec2[] vec2Array = planContext.getAiContext().getSurroundingTiles(intent.getTargetPosition());
                        if (vec2Array != null) {
                            Vec2 object;
                            float f = Float.MAX_VALUE;
                            Vec2 vec25 = null;
                            int i16 = 0;
                            while (i16 < vec2Array.length) {
                                object = vec2Array[i16];
                                float f18 = object.distanceTo(intent.getTargetPosition());
                                if (f18 < f && planContext.getAiContext().hasPathToPosition(unit, object.getX(), object.getY())) {
                                    vec25 = object;
                                    f = f18;
                                }
                                ++i16;
                            }
                            if (vec25 != null) {
                                i8 = true;
                                f9 = vec25.getX();
                                f10 = vec25.getY();
                                i16 = 0;
                                while (i16 < 4) {
                                    Neighbor neighbor = planContext.getAiContext().findNeighbor(Domain.Water, f9, f10, unit.getPosition().getX(), unit.getPosition().getY());
                                    if (neighbor.isValid()) {
                                        f9 += (float)neighbor.getDx();
                                        f10 += (float)neighbor.getDy();
                                    }
                                    ++i16;
                                }
                            }
                        } else if (intent.getTargetUnit() != null && intent.getTargetUnit().getUnitType().getDomain() == Domain.Water && (n7 = this.findNearestWaterTile(planContext.getAiContext(), unit.getPosition(), (int)vec2.getX(), (int)vec2.getY())) > 0) {
                            int n8 = planContext.getAiContext().getTerrainWidth();
                            i8 = true;
                            f9 = (float)(n7 % n8) + 0.5f;
                            f10 = (float)(n7 / n8) + 0.5f;
                        }
                    } else {
                        i8 = true;
                        f9 = intent.getTargetPosition().getX();
                        f10 = intent.getTargetPosition().getY();
                        int n9 = 0;
                        while (n9 < 4) {
                            Neighbor neighbor = planContext.getAiContext().findNeighbor(unit.getUnitType().getDomain(), f9, f10, unit.getPosition().getX(), unit.getPosition().getY());
                            if (neighbor.isValid()) {
                                f9 += (float)neighbor.getDx();
                                f10 += (float)neighbor.getDy();
                            }
                            ++n9;
                        }
                    }
                    if (i8) {
                        intentGroup.setTargetPosition(f9, f10);
                        intentGroup.setMode(IntentGroupMode.b);
                        intentGroup.setHasMoveOrder(true);
                    } else {
                        intentGroupList.remove(n2);
                    }
                    return null;
                }
                ++n2;
            }
            this.a = 2;
            return null;
        }
        n = 0;
        while (n < intentGroupList.size()) {
            IntentGroup intentGroup = (IntentGroup)intentGroupList.get(n);
            Unit unit = intentGroup.getHighestSortWeightUnit();
            if (!unit.getPosition().equalsInt(intentGroup.getTargetPosition())) {
                return null;
            }
            ++n;
        }
        return PlanResult.SUCCESS;
    }

    private final int findNearestGroundTile(AIContext aIContext, Vec2 vec2, int i3, int i4) {
        int i5 = -1;
        float f6 = Float.MAX_VALUE;
        int i7 = 4;
        while (i7 >= 1) {
            float f10;
            int i9;
            int i8 = i3 - i7;
            while (i8 <= i3 + i7) {
                i9 = i4 - i7;
                while (i9 <= i4 + i7) {
                    if (aIContext.isInsideGrid((float)i8, (float)i9) && aIContext.hasPathForDomain(Domain.Ground, i8, i9, i3, i4) && (f10 = vec2.distanceSquaredTo(i8, i9)) < f6) {
                        i5 = i9 * aIContext.getTerrainWidth() + i8;
                        f6 = f10;
                    }
                    ++i9;
                }
                i8 += i7 + i7;
            }
            i8 = i4 - i7;
            while (i8 <= i4 + i7) {
                i9 = i3 - i7;
                while (i9 <= i3 + i7) {
                    if (aIContext.isInsideGrid((float)i9, (float)i8) && aIContext.hasPathForDomain(Domain.Ground, i9, i8, i3, i4) && (f10 = vec2.distanceSquaredTo(i9, i8)) < f6) {
                        i5 = i8 * aIContext.getTerrainWidth() + i9;
                        f6 = f10;
                    }
                    ++i9;
                }
                i8 += i7 + i7;
            }
            --i7;
        }
        return i5;
    }

    private final int findNearestWaterTile(AIContext aIContext, Vec2 vec2, int i3, int i4) {
        int i5 = -1;
        float f6 = Float.MAX_VALUE;
        int i7 = 4;
        while (i7 >= 1) {
            float f10;
            int i9;
            int i8 = i3 - i7;
            while (i8 <= i3 + i7) {
                i9 = i4 - i7;
                while (i9 <= i4 + i7) {
                    if (aIContext.isInsideGrid((float)i8, (float)i9) && aIContext.hasPathForDomain(Domain.Water, i8, i9, i3, i4) && (f10 = vec2.distanceSquaredTo(i8, i9)) < f6) {
                        i5 = i9 * aIContext.getTerrainWidth() + i8;
                        f6 = f10;
                    }
                    ++i9;
                }
                i8 += i7 + i7;
            }
            i8 = i4 - i7;
            while (i8 <= i4 + i7) {
                i9 = i3 - i7;
                while (i9 <= i3 + i7) {
                    if (aIContext.isInsideGrid((float)i9, (float)i8) && aIContext.hasPathForDomain(Domain.Water, i9, i8, i3, i4) && (f10 = vec2.distanceSquaredTo(i9, i8)) < f6) {
                        i5 = i8 * aIContext.getTerrainWidth() + i9;
                        f6 = f10;
                    }
                    ++i9;
                }
                i8 += i7 + i7;
            }
            --i7;
        }
        return i5;
    }
}


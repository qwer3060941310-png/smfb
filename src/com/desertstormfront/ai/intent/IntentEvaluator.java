/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.intent;

import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.intent.InfluenceMap;
import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentList;
import com.desertstormfront.ai.intent.IntentType;
import com.desertstormfront.ai.intent.UnitRequest;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.mode.EscortMode;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.Difficulty;
import com.desertstormfront.game.player.FogOfWar;
import com.desertstormfront.game.player.Player;
import com.noblemaster.lib.log.OsfLog;
import java.util.ArrayList;

/**
 * 意图评估器，对候选意图做效用打分
 */
public strictfp final class IntentEvaluator {
    private AIContext context;
    private int phase;
    private float lastUpdateTime;

    public IntentEvaluator(AIContext aIContext) {
        this.context = aIContext;
        this.phase = 1;
        this.lastUpdateTime = (float)System.currentTimeMillis() / 1000.0f;
    }

    public boolean evaluate() {
        switch (this.phase) {
            case 1: {
                InfluenceMap influenceMap = this.context.getInfluenceMap();
                int n = this.context.getTerrainWidth();
                int n2 = this.context.getTerrainHeight();
                Player player = this.context.getPlayer();
                FogOfWar fogOfWar = player.getFogOfWar();
                float f = (float)System.currentTimeMillis() / 1000.0f;
                float f2 = f - this.lastUpdateTime;
                float f3 = f2 * 0.01f;
                float f4 = f2 * 0.003f;
                this.lastUpdateTime = f;
                int n3 = 0;
                while (n3 < n2) {
                    int n4 = 0;
                    while (n4 < n) {
                        if (fogOfWar.isVisible(n4, n3)) {
                            int n5 = this.context.getVisibleEnemyUnitsAtTile(n4, n3).size();
                            Unit unit = this.context.getUnitAtTile(n4, n3);
                            if (unit != null && unit.getOwner() != null && unit.getOwner().isEnemyOf(player)) {
                                n5 += 3;
                            }
                            // n5 is the visible-enemy count and must be written as a float:
                            // without the cast, overload resolution picks the static factory
                            // InfluenceMap.a(int,int,int) (int is more specific than float), which
                            // rebuilds the map with the enemy count as the cell size and divides
                            // by zero whenever a tile has no enemies.
                            influenceMap.setValue(n4, n3, (float)n5);
                        } else {
                            float f5 = influenceMap.getValue(n4, n3);
                            f5 = this.context.isAllWaterTile(n4, n3) ? (f5 -= f3) : (f5 -= f4);
                            if (f5 < 0.0f) {
                                f5 = 0.0f;
                            }
                            influenceMap.setValue(n4, n3, f5);
                        }
                        ++n4;
                    }
                    ++n3;
                }
                ++this.phase;
                return true;
            }
            case 2: {
                InfluenceMap influenceMap = this.context.getInfluenceMap();
                influenceMap.rebuild();
                ++this.phase;
                return true;
            }
            case 3: {
                int n;
                ArrayList arrayList;
                IntentGroup intentGroup;
                int n6;
                Object object;
                UnitList unitList = this.context.getUnassignedUnits();
                unitList.clear();
                IntentList intentList = this.context.getIntents();
                UnitList unitList2 = this.context.getOwnMobileUnits();
                int n7 = 0;
                while (n7 < unitList2.size()) {
                    object = (Unit)unitList2.get(n7);
                    if (((Unit)object).isCountZero() && ((Unit)object).isDeployed() && intentList.findIntent((Unit)object) == null) {
                        unitList.add(object);
                    }
                    ++n7;
                }
                n7 = 0;
                while (n7 < intentList.size()) {
                    object = ((Intent)intentList.get(n7)).getGroupList();
                    n6 = 0;
                    while (n6 < ((ArrayList)object).size()) {
                        intentGroup = (IntentGroup)((ArrayList)object).get(n6);
                        arrayList = intentGroup.getRequests();
                        n = 0;
                        while (n < arrayList.size()) {
                            UnitRequest unitRequest = (UnitRequest)arrayList.get(n);
                            Unit unit = unitRequest.getUnit();
                            UnitType unitType = unitRequest.getUnitType();
                            Unit unit2 = null;
                            int i14 = 0;
                            while (i14 < unitList.size()) {
                                Unit unit3 = (Unit)unitList.get(i14);
                                if (unit3.getPosition().getUnit() == unit && unit3.getUnitType() == unitType) {
                                    unit2 = unit3;
                                    unitList.remove(i14);
                                    break;
                                }
                                ++i14;
                            }
                            if (unit2 != null) {
                                arrayList.remove(n);
                                intentGroup.getUnits().add(unit2);
                                continue;
                            }
                            ++n;
                        }
                        ++n6;
                    }
                    ++n7;
                }
                if (GameConfig.isDebugEnabled()) {
                    n7 = 0;
                    while (n7 < intentList.size()) {
                        object = ((Intent)intentList.get(n7)).getGroupList();
                        n6 = 0;
                        while (n6 < ((ArrayList)object).size()) {
                            intentGroup = (IntentGroup)((ArrayList)object).get(n6);
                            arrayList = intentGroup.getUnits();
                            n = 0;
                            int n8 = 0;
                            while (n8 < arrayList.size()) {
                                if (((Unit)arrayList.get(n8)).getUnitType().canCarry()) {
                                    ++n;
                                }
                                ++n8;
                            }
                            if (n >= 2) {
                                OsfLog.info("INTENT " + (Object)((Object)((Intent)intentList.get(n7)).getType()) + ": 2+ hosts in a group!");
                            }
                            ++n6;
                        }
                        ++n7;
                    }
                }
                ++this.phase;
                return true;
            }
            case 4: {
                Player player = this.context.getPlayer();
                int n = (int)player.getResources() + (int)((long)this.context.getOwnImmobileUnits().getBuildingCount() * player.getIncomePerBuilding()) + (int)this.context.getOwnMobileUnits().getTotalHealth();
                if ((n /= 750) < 1) {
                    n = 1;
                }
                this.context.a(n);
                IntentList intentList = this.context.getIntents();
                if (player.isAlive()) {
                    int i25;
                    int i24;
                    int i23;
                    int i22;
                    int i21;
                    int i19;
                    int i18;
                    float f;
                    float f6;
                    float f7;
                    float f8;
                    float f9;
                    Difficulty difficulty = player.getController().getDifficulty();
                    float f10 = this.context.getOwnImmobileUnits().size();
                    float f11 = this.context.getVisibleEnemyOrNeutralUnits().size();
                    float f12 = f9 = difficulty == Difficulty.Casual ? 0.3f : 0.75f;
                    if (!this.context.hasProducer()) {
                        f11 = 0.0f;
                    }
                    if (f11 == 0.0f) {
                        f8 = 0.0f;
                        f7 = f9;
                    } else {
                        f6 = f10 / (f10 + f11);
                        f8 = f9 * (0.3f + 0.4f * (1.0f - f6));
                        f7 = f9 * (0.3f + 0.4f * f6);
                    }
                    if (f10 == 0.0f) {
                        f6 = 0.0f;
                        f = 1.0f - f9;
                    } else {
                        f6 = (1.0f - f9) * 0.7f;
                        f = (1.0f - f9) * 0.3f;
                    }
                    int n9 = intentList.count(IntentType.Conquer);
                    int n10 = (int)Math.ceil((float)n * f8);
                    if (n10 == 0 && f11 > 0.0f) {
                        n10 = 1;
                    }
                    if (n9 < n10) {
                        intentList.add(Intent.create(IntentType.Conquer));
                    } else if (n9 > n10 + 1) {
                        intentList.takeWeakest(IntentType.Conquer);
                    }
                    int i14 = intentList.count(IntentType.Attack);
                    int n11 = Math.round((float)n * f7);
                    if (n11 == 0 && f11 == 0.0f) {
                        n11 = 1;
                    }
                    if (i14 < n11) {
                        intentList.add(Intent.create(IntentType.Attack));
                    } else if (i14 > n11 + 1) {
                        intentList.takeWeakest(IntentType.Attack);
                    }
                    int i16 = intentList.count(IntentType.Command);
                    int i17 = this.context.getOwnMobileUnits().getGeneralCount();
                    if (i16 < i17) {
                        intentList.add(Intent.create(IntentType.Command));
                    } else if (i16 > i17 + 1) {
                        intentList.takeWeakest(IntentType.Command);
                    }
                    if (this.context.getGameMode() instanceof EscortMode) {
                        i18 = intentList.count(IntentType.Truck);
                        if (i18 < (i19 = this.context.getOwnMobileUnits().countOfType(this.context.getMapDefinition().getUnitTypeSlots().getTruck()))) {
                            intentList.add(Intent.create(IntentType.Truck));
                        } else if (i16 > i17 + 1) {
                            intentList.takeWeakest(IntentType.Truck);
                        }
                    }
                    i18 = intentList.count(IntentType.Explore);
                    if (player.isExplorationEnabled()) {
                        FogOfWar fogOfWar = player.getFogOfWar();
                        i21 = 0;
                        i22 = fogOfWar.getWidth();
                        i23 = fogOfWar.getHeight();
                        i24 = 0;
                        while (i24 < i23) {
                            i25 = 0;
                            while (i25 < i22) {
                                if (fogOfWar.isExplored(i25, i24)) {
                                    ++i21;
                                }
                                ++i25;
                            }
                            ++i24;
                        }
                        i24 = i22 * i23;
                        i19 = i21 >= i24 ? 0 : (n10 <= (i25 = this.context.getVisibleEnemyOrNeutralUnits().size()) ? 0 : (n10 - i25) / 3 + 1);
                    } else {
                        i19 = 0;
                    }
                    if (i18 < i19) {
                        intentList.add(Intent.create(IntentType.Explore));
                    } else if (i18 > i19 + 1) {
                        intentList.takeWeakest(IntentType.Explore);
                    }
                    int n12 = intentList.count(IntentType.Defend);
                    i21 = Math.round((float)n * f6);
                    if (n12 < i21) {
                        intentList.add(Intent.create(IntentType.Defend));
                    } else if (n12 > i21 + 1) {
                        intentList.takeWeakest(IntentType.Defend);
                    }
                    i22 = intentList.count(IntentType.Patrol);
                    i23 = Math.round((float)n * f);
                    if (i22 < i23) {
                        intentList.add(Intent.create(IntentType.Patrol));
                    } else if (i22 > i23 + 1) {
                        intentList.takeWeakest(IntentType.Patrol);
                    }
                    i24 = intentList.count(IntentType.Rogue);
                    i25 = Math.round(n / 15);
                    if (i24 < i25) {
                        intentList.add(Intent.create(IntentType.Rogue));
                    } else if (i24 > i25 + 1) {
                        intentList.takeWeakest(IntentType.Rogue);
                    }
                } else {
                    intentList.clear();
                }
                this.phase = 1;
                return false;
            }
        }
        OsfLog.error("Evaluator mode not implemented: " + this.phase);
        this.phase = 1;
        return false;
    }
}


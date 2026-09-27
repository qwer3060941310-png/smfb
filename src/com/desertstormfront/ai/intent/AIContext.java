/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.intent;

import com.desertstormfront.ai.intent.InfluenceMap;
import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.intent.IntentList;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.game.World;
import com.desertstormfront.game.mode.GameMode;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.FogOfWar;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.Neighbor;
import com.desertstormfront.world.Vec2;
import com.noblemaster.lib.util.FastRandom;
import java.util.Collection;

/**
 * AI 上下文，承载全局态势与评估输入
 */
public strictfp final class AIContext {
    private Player player;
    private World world;
    private UnitList c;
    private UnitList d;
    private UnitList e;
    private UnitList f;
    private UnitList g;
    private UnitList h;
    private UnitList i;
    private UnitList ownImmobileUnits;
    private UnitList visibleEnemyUnits;
    private UnitList l;
    private IntentList intents;
    private int n;
    private InfluenceMap influenceMap;
    private UnitList unassignedUnits;

    public static AIContext create(UnitCommander unitCommander) {
        AIContext aIContext = new AIContext();
        aIContext.player = unitCommander.getPlayer();
        aIContext.world = unitCommander.getWorld();
        aIContext.intents = new IntentList();
        aIContext.c = new UnitList(128);
        UnitList unitList = aIContext.world.getUnits();
        int i3 = unitList.size();
        int i4 = 0;
        while (i4 < i3) {
            Unit unit = (Unit)unitList.get(i4);
            if (unit.isImmobile()) {
                aIContext.c.add(unit);
            }
            ++i4;
        }
        aIContext.d = new UnitList(128);
        aIContext.e = new UnitList(128);
        aIContext.f = new UnitList(128);
        aIContext.g = new UnitList(128);
        aIContext.h = new UnitList(512);
        aIContext.i = new UnitList(512);
        aIContext.ownImmobileUnits = new UnitList(512);
        aIContext.visibleEnemyUnits = new UnitList(512);
        aIContext.l = new UnitList(512);
        aIContext.n = 0;
        aIContext.influenceMap = InfluenceMap.create(aIContext.world.getTerrainGrid().getWidth(), aIContext.world.getTerrainGrid().getHeight(), 3);
        aIContext.unassignedUnits = new UnitList(512);
        return aIContext;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final IntentList getIntents() {
        return this.intents;
    }

    public final int c() {
        return this.n;
    }

    public final void a(int i1) {
        this.n = i1;
    }

    public final InfluenceMap getInfluenceMap() {
        return this.influenceMap;
    }

    public final UnitList getUnassignedUnits() {
        return this.unassignedUnits;
    }

    public final boolean isUnitAssigned(Unit unit) {
        int i2 = 0;
        while (i2 < this.intents.size()) {
            IntentGroupList intentGroupList = ((Intent)this.intents.get(i2)).getGroupList();
            int i4 = 0;
            while (i4 < intentGroupList.size()) {
                if (((IntentGroup)intentGroupList.get(i4)).getUnits().contains(unit)) {
                    return true;
                }
                ++i4;
            }
            ++i2;
        }
        return false;
    }

    public final int claimUnits(UnitList unitList, Unit unit) {
        if (!unitList.contains(unit) && this.unassignedUnits.contains(unit)) {
            int i3 = 0;
            unitList.add(unit);
            this.unassignedUnits.remove(unit);
            ++i3;
            int i4 = 0;
            while (i4 < unit.getSubUnits().size()) {
                i3 += this.claimUnits(unitList, (Unit)unit.getSubUnits().get(i4));
                ++i4;
            }
            if (unit.getUnitType().canCarry()) {
                i4 = 0;
                while (i4 < this.unassignedUnits.size()) {
                    Unit unit2 = (Unit)this.unassignedUnits.get(i4);
                    if (unit.canEmbark(unit2) && (unit2.getGuardPositionOrNull() != null && unit2.getGuardPositionOrNull().getUnit() == unit || unit2.getRallyPositionOrNull() != null && unit2.getRallyPositionOrNull().getUnit() == unit)) {
                        int i6 = this.unassignedUnits.size();
                        i3 += this.claimUnits(unitList, unit2);
                        i4 -= i6 - this.unassignedUnits.size();
                    }
                    ++i4;
                }
            }
            if (unit.getPosition().getUnit() != null && !unit.getPosition().getUnit().isImmobile()) {
                return this.claimUnits(unitList, unit.getPosition().getUnit());
            }
            return i3;
        }
        return 0;
    }

    public MapDefinition getMapDefinition() {
        return this.world.getMapDefinition();
    }

    public float getGameTime() {
        return this.world.getGameTime();
    }

    public FastRandom getRandom() {
        return this.world.getRandom();
    }

    public GameMode getGameMode() {
        return this.world.getGameMode();
    }

    public final int getTerrainWidth() {
        return this.world.getTerrainGrid().getWidth();
    }

    public final int getTerrainHeight() {
        return this.world.getTerrainGrid().getHeight();
    }

    public final boolean isInsideGrid(float f1, float f2) {
        return this.world.getTerrainGrid().isInsideGrid(f1, f2);
    }

    public final boolean isAllLandTile(int i1, int i2) {
        return this.world.getTerrainGrid().isAllLandTile(i1, i2);
    }

    public final boolean isAllLandTileAtPosition(Vec2 vec2) {
        return this.world.getTerrainGrid().isAllLandTileAtPosition(vec2);
    }

    public final boolean isAllWaterTile(int i1, int i2) {
        return this.world.getTerrainGrid().isAllWaterTile(i1, i2);
    }

    public final Vec2[] getSurroundingTiles(Vec2 vec2) {
        return this.world.getTerrainGrid().getSurroundingTiles(vec2);
    }

    public final boolean hasPathToUnit(Unit unit, Unit unit2) {
        return this.world.getTerrainGrid().hasPathToUnit(unit, unit2);
    }

    public final boolean hasPathToPosition(Unit unit, float f2, float f3) {
        return this.world.getTerrainGrid().hasPathToPosition(unit, f2, f3);
    }

    public final boolean hasPathForDomain(Domain domain, float f2, float f3, float f4, float f5) {
        return this.world.getTerrainGrid().hasPathForDomain(domain, f2, f3, f4, f5);
    }

    public final Neighbor findNeighbor(Domain domain, float f2, float f3, float f4, float f5) {
        return this.world.getTerrainGrid().findNeighbor(domain, f2, f3, f4, f5);
    }

    public final Unit getUnitAtTile(int i1, int i2) {
        return this.world.getTerrainGrid().getUnitAtTile(i1, i2);
    }

    public final UnitList getOwnImmobileUnits() {
        this.e.clear();
        UnitList unitList = this.c;
        int i2 = unitList.size();
        int i3 = 0;
        while (i3 < i2) {
            Unit unit = (Unit)unitList.get(i3);
            if (unit.getOwner() == this.player) {
                this.e.add(unit);
            }
            ++i3;
        }
        return this.e;
    }

    public final UnitList getVisibleEnemyUnits() {
        this.f.clear();
        FogOfWar fogOfWar = this.player.getFogOfWar();
        UnitList unitList = this.c;
        int i3 = unitList.size();
        int i4 = 0;
        while (i4 < i3) {
            Unit unit = (Unit)unitList.get(i4);
            if (unit.getOwner() != null && unit.getOwner().isEnemyOf(this.player) && fogOfWar.isUnitVisible(unit)) {
                this.f.add(unit);
            }
            ++i4;
        }
        return this.f;
    }

    public final UnitList getVisibleEnemyOrNeutralUnits() {
        this.g.clear();
        FogOfWar fogOfWar = this.player.getFogOfWar();
        UnitList unitList = this.c;
        int i3 = unitList.size();
        int i4 = 0;
        while (i4 < i3) {
            Unit unit = (Unit)unitList.get(i4);
            if (unit.getOwner() == null || unit.getOwner().isEnemyOf(this.player) && fogOfWar.isUnitVisible(unit)) {
                this.g.add(unit);
            }
            ++i4;
        }
        return this.g;
    }

    public final boolean hasProducer() {
        UnitList unitList = this.getOwnMobileUnits();
        int i2 = unitList.size();
        int n = 0;
        while (n < i2) {
            if (((Unit)unitList.get(n)).getUnitType().canCapture()) {
                return true;
            }
            ++n;
        }
        UnitType unitType = this.world.getMapDefinition().getUnitTypeSlots().getHumvee().getProducer();
        unitList = this.getOwnImmobileUnits();
        i2 = unitList.size();
        int i4 = 0;
        while (i4 < i2) {
            if (((Unit)unitList.get(i4)).getUnitType() == unitType) {
                return true;
            }
            ++i4;
        }
        return false;
    }

    public final UnitList getOwnMobileUnits() {
        this.h.clear();
        UnitList unitList = this.world.getUnits();
        int i2 = unitList.size();
        int i3 = 0;
        while (i3 < i2) {
            Unit unit = (Unit)unitList.get(i3);
            if (!unit.isImmobile() && unit.getOwner() == this.player) {
                this.h.add(unit);
            }
            ++i3;
        }
        return this.h;
    }

    public final UnitList getVisibleEnemyUnitsAtTile(int i1, int i2) {
        this.i.clear();
        FogOfWar fogOfWar = this.player.getFogOfWar();
        UnitList unitList = this.world.getTerrainGrid().getUnitsAtTile(i1, i2);
        int i5 = unitList.size();
        int i6 = 0;
        while (i6 < i5) {
            Unit unit = (Unit)unitList.get(i6);
            // Neutral units have no owner; without the guard every influence-map update
            // crashed on the first map with a neutral unit (see getVisibleEnemyUnits below).
            if (unit.getPosition().getUnit() == null && unit.getOwner() != null && unit.getOwner().isEnemyOf(this.player) && fogOfWar.isUnitVisible(unit)) {
                this.i.add(unit);
            }
            ++i6;
        }
        return this.i;
    }

    public final UnitList getVisibleEnemyMobileUnits() {
        this.i.clear();
        FogOfWar fogOfWar = this.player.getFogOfWar();
        UnitList unitList = this.world.getUnits();
        int i3 = unitList.size();
        int i4 = 0;
        while (i4 < i3) {
            Unit unit = (Unit)unitList.get(i4);
            if (!unit.isImmobile() && unit.getPosition().getUnit() == null && unit.getOwner() != null && unit.getOwner().isEnemyOf(this.player) && fogOfWar.isUnitVisible(unit)) {
                this.i.add(unit);
            }
            ++i4;
        }
        return this.i;
    }

    public final UnitList getOwnUnits() {
        this.ownImmobileUnits.clear();
        this.ownImmobileUnits.addAll((Collection)this.getOwnMobileUnits());
        this.ownImmobileUnits.addAll((Collection)this.getOwnImmobileUnits());
        return this.ownImmobileUnits;
    }

    public final UnitList collectVisibleEnemyUnits() {
        this.visibleEnemyUnits.clear();
        this.visibleEnemyUnits.addAll((Collection)this.getVisibleEnemyMobileUnits());
        this.visibleEnemyUnits.addAll((Collection)this.getVisibleEnemyUnits());
        return this.visibleEnemyUnits;
    }
}


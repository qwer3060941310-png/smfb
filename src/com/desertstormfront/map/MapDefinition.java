/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.map;

import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.World;
import com.desertstormfront.game.model.TerrainType;
import com.desertstormfront.game.model.TerrainTypeList;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.game.model.UnitTypeSlots;
import com.desertstormfront.game.player.FactionList;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.map.DsfRuleset;
import com.desertstormfront.map.TsfRuleset;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.util.HashUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public strictfp abstract class MapDefinition {
    private static Map a = new HashMap();

    static {
        MapDefinition mapDefinition = new TsfRuleset();
        mapDefinition.initUnitTypes();
        com.desertstormfront.mod.ModLoader.apply(mapDefinition);
        a.put(mapDefinition.getId(), mapDefinition);
        mapDefinition = new DsfRuleset();
        mapDefinition.initUnitTypes();
        com.desertstormfront.mod.ModLoader.apply(mapDefinition);
        a.put(mapDefinition.getId(), mapDefinition);
        com.desertstormfront.mod.ModLoader.registerCustomRulesets();
    }

    public static MapDefinition getRuleset(String string) {
        return (MapDefinition)a.get(string);
    }

    /** Register an additional ruleset; used by the mod loader for custom rulesets (v4). */
    public static void register(MapDefinition mapDefinition) {
        mapDefinition.initUnitTypes();
        a.put(mapDefinition.getId(), mapDefinition);
    }

    private void initUnitTypes() {
        UnitTypeList unitTypeList = this.getUnitTypes();
        int[][] nArray = this.getCostMatrix();
        int i3 = 0;
        while (i3 < unitTypeList.size()) {
            ((UnitType)unitTypeList.get(i3)).initialize(unitTypeList, nArray);
            ++i3;
        }
    }

    public abstract String getId();

    public abstract FactionList getFactions();

    public abstract TerrainTypeList getTerrainTypes();

    public abstract UnitTypeList getUnitTypes();

    public abstract UnitTypeSlots getUnitTypeSlots();

    public abstract int[][] getCostMatrix();

    public abstract UnitTypeList getCargoUnits(int var1);

    public abstract int getUnitTypeCount();

    public abstract String[][] getPlayerStartTemplates();

    public abstract String[][] getCaptureTheFlagTemplates();

    public abstract String[][] getSurvivalTemplates();

    public abstract String[][] getPlayerBaseTemplates();

    public abstract String[][] getRarePropTemplates();

    public abstract String[][] getCommonPropTemplates();

    public final void paintSite(World world, int i2, int i3, boolean bl, boolean bl2, char c, char c2) {
        TerrainType terrainType = this.getTerrainTypes().getByCode(c);
        if (terrainType != null) {
            int i9;
            if (c2 == ' ') {
                i9 = (HashUtils.hash(i2, i3) & Integer.MAX_VALUE) % terrainType.getSpriteCount();
            } else if (c2 >= '0' && c2 <= '9') {
                i9 = c2 - 48;
            } else if (c2 >= 'A' && c2 <= 'Z') {
                i9 = 10 + c2 - 65;
            } else {
                OsfLog.error("Site index not defined: " + c2);
                i9 = 0;
            }
            world.getTerrainGrid().setSite(i2, i3, terrainType, i9);
        } else if (c != ' ') {
            OsfLog.error("Site character not defined: " + c);
        }
    }

    public final Unit paintUnit(World world, int i2, int i3, boolean bl, boolean bl2, char c, char c2, char c3) {
        UnitType unitType = this.getUnitTypes().getByCode(c);
        if (unitType != null) {
            int i13;
            int i11 = c3 - 48;
            Player player = i11 >= 0 && i11 < world.getPlayers().size() ? (Player)world.getPlayers().get(i11) : null;
            Unit unit = world.spawnUnit(unitType, (float)i2 + 0.5f, (float)i3 + 0.5f, player);
            if (c2 >= '0' && c2 <= '9') {
                i13 = c2 - 48;
            } else if (c2 >= 'A' && c2 <= 'Z') {
                i13 = 10 + c2 - 65;
            } else {
                OsfLog.error("Unit index not defined: " + c2);
                i13 = 0;
            }
            int i14 = i13 / 4 - 1;
            if (i14 >= 0) {
                Object object;
                if (unit.getUnitType().canCarry()) {
                    object = this.getCargoUnits(i14);
                    int n = 0;
                    while (n < ((ArrayList)object).size()) {
                        world.createHostedUnit((UnitType)((ArrayList)object).get(n), unit, player);
                        ++n;
                    }
                } else {
                    object = "Trying to host within a unit that cannot host at (" + i2 + ", " + i3 + "): " + unitType;
                    if (GameConfig.isDebugEnabled()) {
                        throw new RuntimeException((String)object);
                    }
                    OsfLog.error((String)object);
                }
            }
            int n = i13 % 4;
            if (bl) {
                if (n == 1) {
                    n = 3;
                } else if (n == 3) {
                    n = 1;
                }
            }
            if (bl2) {
                if (n == 0) {
                    n = 2;
                } else if (n == 2) {
                    n = 0;
                }
            }
            float f = (float)(n - 1) * ((float)Math.PI * 2) / 4.0f;
            unit.setAngle(f);
            return unit;
        }
        if (c != ' ') {
            OsfLog.error("Unit character not defined: " + c2 + " (" + i2 + ", " + i3 + ")");
        }
        return null;
    }
}


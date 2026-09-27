/*
 * Custom ruleset built from mod/ruleset.json.
 *
 * Design: delegates everything that the JSON does not define to a built-in ruleset.
 * This keeps the parts whose semantics are not yet confirmed (the String[][] tables h()-m(),
 * the unit group array a(int)/g()) on proven values, so a partial JSON can never produce
 * out-of-bounds or wrongly-guessed data.
 */
package com.desertstormfront.mod;

import com.badlogic.gdx.utils.JsonValue;
import com.desertstormfront.game.model.TerrainType;
import com.desertstormfront.game.model.TerrainTypeList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.game.model.UnitTypeSlots;
import com.desertstormfront.game.player.Faction;
import com.desertstormfront.game.player.FactionList;
import com.desertstormfront.map.MapDefinition;

import java.util.ArrayList;
import java.util.List;

public final class ModRuleset extends MapDefinition {

    private final MapDefinition delegate;
    private final String name;
    private final FactionList factions;
    private final TerrainTypeList terrains;
    private final UnitTypeList units;
    private final UnitTypeSlots slots;
    private final int[][] costs;

    ModRuleset(MapDefinition delegate, JsonValue root, String name, List<UnitType> units) {
        this.delegate = delegate;
        this.name = name;
        this.units = new UnitTypeList(units);
        this.factions = readFactions(root, delegate);
        this.terrains = readTerrains(root, delegate);
        this.slots = readSlots(root, delegate, this.units);
        this.costs = readCosts(root, delegate, this.units);
    }

    @Override
    public String getId() {
        return this.name;
    }

    @Override
    public FactionList getFactions() {
        return this.factions != null ? this.factions : this.delegate.getFactions();
    }

    @Override
    public TerrainTypeList getTerrainTypes() {
        return this.terrains != null ? this.terrains : this.delegate.getTerrainTypes();
    }

    @Override
    public UnitTypeList getUnitTypes() {
        return this.units;
    }

    @Override
    public UnitTypeSlots getUnitTypeSlots() {
        return this.slots != null ? this.slots : this.delegate.getUnitTypeSlots();
    }

    @Override
    public int[][] getCostMatrix() {
        return this.costs;
    }

    // The following keep the delegate's proven values (semantics not fully confirmed yet).

    @Override
    public UnitTypeList getCargoUnits(int index) {
        return this.delegate.getCargoUnits(index);
    }

    @Override
    public int getUnitTypeCount() {
        return this.delegate.getUnitTypeCount();
    }

    @Override
    public String[][] getPlayerStartTemplates() {
        return this.delegate.getPlayerStartTemplates();
    }

    @Override
    public String[][] getCaptureTheFlagTemplates() {
        return this.delegate.getCaptureTheFlagTemplates();
    }

    @Override
    public String[][] getSurvivalTemplates() {
        return this.delegate.getSurvivalTemplates();
    }

    @Override
    public String[][] getPlayerBaseTemplates() {
        return this.delegate.getPlayerBaseTemplates();
    }

    @Override
    public String[][] getRarePropTemplates() {
        return this.delegate.getRarePropTemplates();
    }

    @Override
    public String[][] getCommonPropTemplates() {
        return this.delegate.getCommonPropTemplates();
    }

    // ------------------------------------------------------------------ data readers

    private static FactionList readFactions(JsonValue root, MapDefinition delegate) {
        JsonValue array = root == null ? null : root.get("factions");
        if (array == null) {
            return null;
        }
        List<Faction> list = new ArrayList<Faction>();
        int index = 0;
        for (JsonValue item = array.child; item != null; item = item.next) {
            list.add(new Faction(
                    item.getInt("id", index),
                    item.getString("name", "Faction" + index),
                    item.getString("key", "FACTION_" + index)));
            ++index;
        }
        return list.isEmpty() ? null : new FactionList(list);
    }

    private static TerrainTypeList readTerrains(JsonValue root, MapDefinition delegate) {
        JsonValue array = root == null ? null : root.get("terrains");
        if (array == null) {
            return null;
        }
        List<TerrainType> list = new ArrayList<TerrainType>();
        int index = 0;
        for (JsonValue item = array.child; item != null; item = item.next) {
            String code = item.getString("code", "X");
            list.add(new TerrainType(
                    item.getInt("id", index),
                    item.getString("name", "Terrain" + index),
                    item.getString("key", "TERRAIN_" + index),
                    code.isEmpty() ? 'X' : code.charAt(0),
                    item.getInt("count", 1)));
            ++index;
        }
        return list.isEmpty() ? null : new TerrainTypeList(list);
    }

    private static UnitTypeSlots readSlots(JsonValue root, MapDefinition delegate, UnitTypeList units) {
        JsonValue array = root == null ? null : root.get("slots");
        if (array == null) {
            return null;
        }
        List<UnitType> resolved = new ArrayList<UnitType>();
        for (JsonValue item = array.child; item != null; item = item.next) {
            if (!item.isString()) {
                continue;
            }
            UnitType found = units.getByKey(item.asString());
            resolved.add(found);
        }
        while (resolved.size() < 21) {
            resolved.add(null);
        }
        return new UnitTypeSlots(
                resolved.get(0), resolved.get(1), resolved.get(2), resolved.get(3),
                resolved.get(4), resolved.get(5), resolved.get(6), resolved.get(7),
                resolved.get(8), resolved.get(9), resolved.get(10), resolved.get(11),
                resolved.get(12), resolved.get(13), resolved.get(14), resolved.get(15),
                resolved.get(16), resolved.get(17), resolved.get(18), resolved.get(19),
                resolved.get(20));
    }

    private static int[][] readCosts(JsonValue root, MapDefinition delegate, UnitTypeList units) {
        int size = units.size();
        int[][] matrix = new int[size][size];
        JsonValue array = root == null ? null : root.get("costs");
        if (array == null) {
            return matrix;
        }
        int row = 0;
        for (JsonValue rowValue = array.child; rowValue != null && row < size; rowValue = rowValue.next) {
            int col = 0;
            for (JsonValue cell = rowValue.child; cell != null && col < size; cell = cell.next) {
                matrix[row][col] = cell.asInt();
                ++col;
            }
            ++row;
        }
        return matrix;
    }
}

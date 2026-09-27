/*
 * Sprite-table row selector: tables[unit type id] holds one UnitSpriteAnimation per facing;
 * getAnimation(unit) returns the entry for the queried unit.
 *
 * Deobfuscation: the array field a became tables and a(Unit) became getAnimation; rows are indexed
 * by the unit type id and columns by the unit's facing. Evidence: run\map-graphics-table.tsv.
 */
package com.desertstormfront.graphics.table;

import com.desertstormfront.game.model.Unit;
import com.desertstormfront.graphics.table.UnitSpriteAnimation;
import java.util.List;

public final class UnitSpriteTable {
    private List[] tables;

    public UnitSpriteTable(List[] listArray) {
        this.tables = listArray;
    }

    public UnitSpriteAnimation getAnimation(Unit unit) {
        return (UnitSpriteAnimation)this.tables[unit.getUnitType().getId()].get(unit.getDirection().getIndex());
    }
}


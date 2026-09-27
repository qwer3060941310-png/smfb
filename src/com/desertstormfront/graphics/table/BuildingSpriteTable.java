/*
 * Sprite-table row selector: tables[terrain type index] holds one SpriteAnimationSet per site
 * variant; getAnimationSet(site) returns the entry for the queried site.
 *
 * Deobfuscation: the array field a became tables and a(Site) became getAnimationSet; the row index
 * is the site's terrain type, the column its variant index. Evidence: run\map-graphics-table.tsv.
 */
package com.desertstormfront.graphics.table;

import com.desertstormfront.game.model.Site;
import com.desertstormfront.graphics.table.SpriteAnimationSet;
import java.util.List;

public final class BuildingSpriteTable {
    private List[] tables;

    public BuildingSpriteTable(List[] listArray) {
        this.tables = listArray;
    }

    public SpriteAnimationSet getAnimationSet(Site site) {
        return (SpriteAnimationSet)this.tables[site.getTerrainType().getId()].get(site.getVariantIndex());
    }
}


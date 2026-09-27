/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game;

import com.desertstormfront.game.World;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.world.TerrainGrid;

strictfp final class MapTemplatePainter {
    private World a;

    MapTemplatePainter(World world) {
        this.a = world;
    }

    private UnitList paintTemplate(int i1, int i2, String[] stringArray) {
        TerrainGrid terrainGrid = this.a.getTerrainGrid();
        UnitList unitList = new UnitList();
        int i6 = (stringArray.length - 1) / 2;
        boolean i7 = this.a.getRandom().nextBoolean();
        boolean i8 = this.a.getRandom().nextBoolean();
        int i9 = 0;
        while (i9 < i6) {
            int i10 = 0;
            while (i10 < i6) {
                int i11 = i1 + (i7 ? i6 - i10 - 1 : i10);
                int i12 = i2 + (i8 ? i6 - i9 - 1 : i9);
                terrainGrid.setCornerLand(i11 + (i7 ? 1 : 0), i12 + (i8 ? 1 : 0), stringArray[2 * i9].charAt(4 * i10) != ' ');
                terrainGrid.setCornerLand(i11 + (i7 ? 0 : 1), i12 + (i8 ? 1 : 0), stringArray[2 * i9].charAt(4 * (i10 + 1)) != ' ');
                terrainGrid.setCornerLand(i11 + (i7 ? 1 : 0), i12 + (i8 ? 0 : 1), stringArray[2 * (i9 + 1)].charAt(4 * i10) != ' ');
                terrainGrid.setCornerLand(i11 + (i7 ? 0 : 1), i12 + (i8 ? 0 : 1), stringArray[2 * (i9 + 1)].charAt(4 * (i10 + 1)) != ' ');
                char i13 = stringArray[2 * i9 + 1].charAt(4 * i10 + 1);
                char i14 = stringArray[2 * i9 + 1].charAt(4 * i10 + 2);
                char i15 = stringArray[2 * i9 + 1].charAt(4 * i10 + 3);
                if (i13 == ' ') {
                    this.a.getMapDefinition().paintSite(this.a, i11, i12, i7, i8, i14, i15);
                } else {
                    Unit unit = this.a.getMapDefinition().paintUnit(this.a, i11, i12, i7, i8, i13, i14, i15);
                    if (unit != null) {
                        unitList.add(unit);
                    }
                }
                ++i10;
            }
            ++i9;
        }
        return unitList;
    }

    static /* synthetic */ UnitList paintTemplate(MapTemplatePainter mapTemplatePainter, int i1, int i2, String[] stringArray) {
        return mapTemplatePainter.paintTemplate(i1, i2, stringArray);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.io;

import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.model.TerrainType;
import com.desertstormfront.game.model.TerrainTypeList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.graphics.table.BuildingSpriteTable;
import com.desertstormfront.graphics.table.ProgressFrame;
import com.desertstormfront.graphics.table.SpriteAnimationSet;
import com.desertstormfront.graphics.table.SpriteRegion;
import com.desertstormfront.graphics.table.TimedFrame;
import com.desertstormfront.graphics.table.UnitSpriteAnimation;
import com.desertstormfront.graphics.table.UnitSpriteTable;
import com.desertstormfront.graphics.table.WeightedFrame;
import com.desertstormfront.map.MapDefinition;
import com.noblemaster.lib.log.OsfLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class SpriteSheetLoader {
    public static final BuildingSpriteTable loadBuildingSpriteTable(MapDefinition mapDefinition) {
        BuildingSpriteTable buildingSpriteTable;
        BufferedReader bufferedReader = null;
        try {
        block33: {
            List[] listArray;
            List frames;
            String string;
            String siteName;
            block31: {
                block32: {
                    bufferedReader = null;
                    // Same charset rule as WorldSerializer: the config contains Latin-1 symbols
                    // and must not be decoded with the platform default (e.g. GBK).
                    bufferedReader = new BufferedReader(new InputStreamReader(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "graphic_site.cfg").getFileHandle().read(), StandardCharsets.ISO_8859_1));
                    string = bufferedReader.readLine();
                    if (string.startsWith("OSF Site V")) break block31;
                    OsfLog.error("Invalid site config: " + string);
                    if (bufferedReader == null) break block32;
                    try {
                        bufferedReader.close();
                    }
                    catch (IOException iOException) {}
                    bufferedReader = null;
                }
                return null;
            }
            TerrainTypeList terrainTypeList = mapDefinition.getTerrainTypes();
            List[] listArray2 = new List[terrainTypeList.size()];
            int n = 0;
            while (n < listArray2.length) {
                listArray2[n] = new ArrayList();
                ++n;
            }
            List list = null;
            int i6 = 0;
            int i7 = 0;
            int i8 = 0;
            int i9 = 0;
            while ((string = bufferedReader.readLine()) != null) {
                if (string.length() == 0 || string.charAt(0) == ' ' || string.charAt(0) == '#') continue;
                if (string.startsWith("site:")) {
                    String[] stringArray = string.substring(string.indexOf(":") + 1).trim().split("\\s+");
                    siteName = stringArray[0].trim();
                    list = listArray2[terrainTypeList.getByKey(siteName).getId()];
                    i6 = Integer.parseInt(stringArray[1].trim());
                    i7 = Integer.parseInt(stringArray[2].trim());
                    i8 = Integer.parseInt(stringArray[3].trim());
                    i9 = Integer.parseInt(stringArray[4].trim());
                    continue;
                }
                if (string.startsWith("site-item:")) {
                    if (list != null) {
                        string = string.substring(string.indexOf(":") + 1);
                        int n2 = 1;
                        int n3 = 0;
                        while (string.indexOf("|", n3) >= 0) {
                            ++n2;
                            n3 = string.indexOf("|", n3) + 1;
                        }
                        listArray = new List[n2];
                        int i13 = 0;
                        while (i13 < listArray.length) {
                            int i22;
                            int i21;
                            int i20;
                            int i19;
                            int i18;
                            int i17;
                            String object2;
                            String string2;
                            listArray[i13] = new ArrayList();
                            if (string.indexOf("|") >= 0) {
                                string2 = string.substring(0, string.indexOf("|"));
                                string = string.substring(string.indexOf("|") + 1);
                            } else {
                                string2 = string;
                                string = null;
                            }
                            string2 = string2.trim();
                            if (string2.indexOf(":") >= 0) {
                                while (string2 != null) {
                                    if (string2.indexOf(":") >= 0) {
                                        object2 = string2.substring(0, string2.indexOf(":"));
                                        string2 = string2.substring(string2.indexOf(":") + 1);
                                    } else {
                                        object2 = string2;
                                        string2 = null;
                                    }
                                    object2 = ((String)object2).trim();
                                    String[] stringArray = ((String)object2).split("\\s+");
                                    i17 = Integer.parseInt(stringArray[6].substring(1));
                                    i18 = Integer.parseInt(stringArray[0]) - Integer.parseInt(stringArray[4]) / 2;
                                    i19 = Integer.parseInt(stringArray[1]) - Integer.parseInt(stringArray[5]) / 2;
                                    i20 = i8 + Integer.parseInt(stringArray[2]);
                                    i21 = i9 + Integer.parseInt(stringArray[3]);
                                    i22 = Integer.parseInt(stringArray[4]);
                                    int i23 = Integer.parseInt(stringArray[5]);
                                    listArray[i13].add(new WeightedFrame(i17, new SpriteRegion(i18, i19, i20, i21, i22, i23)));
                                }
                            } else {
                                String[] parts = string2.split("\\s+");
                                int n4 = 0;
                                i17 = Integer.parseInt(parts[0]) - Integer.parseInt(parts[4]) / 2;
                                i18 = Integer.parseInt(parts[1]) - Integer.parseInt(parts[5]) / 2;
                                i19 = i6 + Integer.parseInt(parts[2]);
                                i20 = i7 + Integer.parseInt(parts[3]);
                                i21 = Integer.parseInt(parts[4]);
                                i22 = Integer.parseInt(parts[5]);
                                listArray[i13].add(new WeightedFrame(n4, new SpriteRegion(i17, i18, i19, i20, i21, i22)));
                            }
                            ++i13;
                        }
                        list.add(new SpriteAnimationSet(listArray));
                        continue;
                    }
                    OsfLog.error("No reference defined for: " + string);
                    continue;
                }
                OsfLog.error("Site sprite option not defined: " + string);
            }
            int n5 = 0;
            while (n5 < listArray2.length) {
                frames = listArray2[n5];
                TerrainType terrainType = (TerrainType)terrainTypeList.get(n5);
                if (frames.size() != terrainType.getSpriteCount()) {
                    OsfLog.error("Sprite count wrong for \"" + terrainType.getKey() + "\": " + frames.size() + " (should be: " + terrainType.getSpriteCount() + ")");
                    while (frames.size() < terrainType.getSpriteCount()) {
                        frames.add((SpriteAnimationSet)frames.get(0));
                    }
                }
                ++n5;
            }
            buildingSpriteTable = new BuildingSpriteTable(listArray2);
            if (bufferedReader == null) break block33;
            try {
                bufferedReader.close();
            }
            catch (IOException iOException) {}
            bufferedReader = null;
        }
        return buildingSpriteTable;
        }
        catch (Exception exception) {
            OsfLog.error("Error loading site sprites: " + exception);
            OsfLog.logException(exception);
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException iOException) {}
                bufferedReader = null;
            }
            return null;
        }
    }

    public static final UnitSpriteTable loadUnitSpriteTable(MapDefinition mapDefinition) {
        UnitSpriteTable unitSpriteTable;
        BufferedReader bufferedReader = null;
        try {
        block32: {
            List frames;
            String string;
            String siteName;
            block30: {
                block31: {
                    bufferedReader = null;
                    bufferedReader = new BufferedReader(new InputStreamReader(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "graphic_unit.cfg").getFileHandle().read(), StandardCharsets.ISO_8859_1));
                    string = bufferedReader.readLine();
                    if (string.startsWith("OSF Unit V")) break block30;
                    OsfLog.error("Invalid unit config: " + string);
                    if (bufferedReader == null) break block31;
                    try {
                        bufferedReader.close();
                    }
                    catch (IOException iOException) {}
                    bufferedReader = null;
                }
                return null;
            }
            UnitTypeList unitTypeList = mapDefinition.getUnitTypes();
            List[] listArray = new List[unitTypeList.size()];
            int n = 0;
            while (n < listArray.length) {
                listArray[n] = new ArrayList();
                ++n;
            }
            List list = null;
            int i6 = 0;
            int i7 = 0;
            int i8 = 0;
            int i9 = 0;
            while ((string = bufferedReader.readLine()) != null) {
                String[] stringArray;
                if (string.length() == 0 || string.charAt(0) == ' ' || string.charAt(0) == '#') continue;
                if (string.startsWith("unit:")) {
                    stringArray = string.substring(string.indexOf(":") + 1).trim().split("\\s+");
                    siteName = stringArray[0].trim();
                    list = listArray[unitTypeList.getByKey(siteName).getId()];
                    i6 = Integer.parseInt(stringArray[1].trim());
                    i7 = Integer.parseInt(stringArray[2].trim());
                    i8 = Integer.parseInt(stringArray[3].trim());
                    i9 = Integer.parseInt(stringArray[4].trim());
                    continue;
                }
                if (string.startsWith("unit-item:")) {
                    if (list != null) {
                        int i26;
                        int i25;
                        int i24;
                        int i23;
                        int i22;
                        int i21;
                        String[] stringArray2;
                        int i19;
                        String[] stringArray3;
                        if (string.contains("none")) {
                            list.add(null);
                            continue;
                        }
                        stringArray = string.substring(string.indexOf(":") + 1).split("\\|");
                        SpriteRegion spriteRegion = null;
                        ArrayList<ProgressFrame> arrayList = null;
                        ArrayList<TimedFrame> arrayList2 = null;
                        int i16 = 0;
                        String[] stringArray4 = stringArray[i16].trim().split("\\s+");
                        int n2 = Integer.parseInt(stringArray4[0]);
                        int n3 = Integer.parseInt(stringArray4[1]);
                        if (!stringArray[++i16].contains("T")) {
                            if (stringArray[i16].indexOf(":") >= 0) {
                                stringArray3 = stringArray[i16].trim().split(":");
                                arrayList = new ArrayList<ProgressFrame>();
                                i19 = 0;
                                while (i19 < stringArray3.length) {
                                    stringArray2 = stringArray3[i19].trim().split("\\s+");
                                    i21 = Integer.parseInt(stringArray2[0]) - Integer.parseInt(stringArray2[4]) / 2;
                                    i22 = Integer.parseInt(stringArray2[1]) - Integer.parseInt(stringArray2[5]) / 2;
                                    i23 = i6 + Integer.parseInt(stringArray2[2]);
                                    i24 = i7 + Integer.parseInt(stringArray2[3]);
                                    i25 = Integer.parseInt(stringArray2[4]);
                                    i26 = Integer.parseInt(stringArray2[5]);
                                    if (stringArray2[6].charAt(0) == 'D') {
                                        float f = Float.parseFloat(stringArray2[6].substring(1));
                                        arrayList.add(new ProgressFrame(f, new SpriteRegion(i21, i22, i23, i24, i25, i26)));
                                    } else {
                                        spriteRegion = new SpriteRegion(i21, i22, i23, i24, i25, i26);
                                    }
                                    ++i19;
                                }
                            } else {
                                stringArray3 = stringArray[i16].trim().split("\\s+");
                                i19 = Integer.parseInt(stringArray3[0]) - Integer.parseInt(stringArray3[4]) / 2;
                                int n4 = Integer.parseInt(stringArray3[1]) - Integer.parseInt(stringArray3[5]) / 2;
                                i21 = i6 + Integer.parseInt(stringArray3[2]);
                                i22 = i7 + Integer.parseInt(stringArray3[3]);
                                i23 = Integer.parseInt(stringArray3[4]);
                                i24 = Integer.parseInt(stringArray3[5]);
                                spriteRegion = new SpriteRegion(i19, n4, i21, i22, i23, i24);
                            }
                            ++i16;
                        }
                        if (stringArray.length > i16) {
                            stringArray3 = stringArray[i16].trim().split(":");
                            arrayList2 = new ArrayList<TimedFrame>();
                            i19 = 0;
                            while (i19 < stringArray3.length) {
                                stringArray2 = stringArray3[i19].trim().split("\\s+");
                                i21 = Integer.parseInt(stringArray2[0]) - Integer.parseInt(stringArray2[4]) / 2;
                                i22 = Integer.parseInt(stringArray2[1]) - Integer.parseInt(stringArray2[5]) / 2;
                                i23 = i8 + Integer.parseInt(stringArray2[2]);
                                i24 = i9 + Integer.parseInt(stringArray2[3]);
                                i25 = Integer.parseInt(stringArray2[4]);
                                i26 = Integer.parseInt(stringArray2[5]);
                                int n5 = Integer.parseInt(stringArray2[6].substring(1));
                                arrayList2.add(new TimedFrame(n5, new SpriteRegion(i21, i22, i23, i24, i25, i26)));
                                ++i19;
                            }
                            ++i16;
                        }
                        list.add(new UnitSpriteAnimation(n2, n3, spriteRegion, arrayList, arrayList2));
                        continue;
                    }
                    OsfLog.error("No reference defined for: " + string);
                    continue;
                }
                OsfLog.error("Site sprite option not defined: " + string);
            }
            int n6 = 0;
            while (n6 < listArray.length) {
                frames = listArray[n6];
                UnitType unitType = (UnitType)unitTypeList.get(n6);
                int n7 = 8;
                if (frames.size() != n7) {
                    // One entry per facing (8). A unit type with no sprite definition at all - e.g.
                    // one appended by mod/new_units.json - used to abort the whole load here, which
                    // left GameScreen with a null table and crashed on the first unit drawn.
                    // Report and normalise instead: keep exactly 8 slots, null meaning "no sprite",
                    // which is the same value a "none" entry in the config already produces.
                    OsfLog.error("Sprite count wrong for \"" + unitType.getKey() + "\": " + frames.size() + " (should be: " + n7 + ")");
                    if (frames.isEmpty()) {
                        while (frames.size() < n7) {
                            frames.add(null);
                        }
                    } else {
                        while (frames.size() < n7) {
                            frames.add(frames.get(0));
                        }
                        while (frames.size() > n7) {
                            frames.remove(frames.size() - 1);
                        }
                    }
                }
                ++n6;
            }
            unitSpriteTable = new UnitSpriteTable(listArray);
            if (bufferedReader == null) break block32;
            try {
                bufferedReader.close();
            }
            catch (IOException iOException) {}
            bufferedReader = null;
        }
        return unitSpriteTable;
        }
        catch (Exception exception) {
            OsfLog.error("Error loading unit sprites: " + exception);
            OsfLog.logException(exception);
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException iOException) {}
                bufferedReader = null;
            }
            return null;
        }
    }
}


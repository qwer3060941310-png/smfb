/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.io;

import com.badlogic.gdx.files.FileHandle;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.game.World;
import com.desertstormfront.game.mode.AnnihilationMode;
import com.desertstormfront.game.mode.CaptureTheFlagMode;
import com.desertstormfront.game.mode.EscortMode;
import com.desertstormfront.game.mode.GameMode;
import com.desertstormfront.game.mode.SupremacyMode;
import com.desertstormfront.game.mode.SurvivalMode;
import com.desertstormfront.game.model.AmmoType;
import com.desertstormfront.game.model.Bird;
import com.desertstormfront.game.model.BirdList;
import com.desertstormfront.game.model.BirdType;
import com.desertstormfront.game.model.EffectTimer;
import com.desertstormfront.game.model.Projectile;
import com.desertstormfront.game.model.ProjectileList;
import com.desertstormfront.game.model.Site;
import com.desertstormfront.game.model.TerrainType;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitOrderMode;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.game.model.Volley;
import com.desertstormfront.game.player.Controller;
import com.desertstormfront.game.player.Faction;
import com.desertstormfront.game.player.FogOfWar;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerList;
import com.desertstormfront.game.player.PlayerStatistics;
import com.desertstormfront.game.player.Team;
import com.desertstormfront.game.player.UnitGroup;
import com.desertstormfront.game.player.UnitGroupSet;
import com.desertstormfront.io.WorldSaveInfo;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.RoadMask;
import com.desertstormfront.world.RoadTile;
import com.desertstormfront.world.TerrainGrid;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;
import com.desertstormfront.world.Vec3;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.util.FastRandom;
import com.noblemaster.lib.util.HashUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public final class WorldSerializer {
    public static final WorldSaveInfo[] listSaves(String string) {
        WorldSaveInfo[] worldSaveInfoArray;
        int i1 = 0;
        FileHandle[] fileHandleArray = GameConfig.getDataFileResolver().getInternal(GameConfig.getConfigDir()).getFileHandle().list();
        int i3 = 0;
        while (i3 < fileHandleArray.length) {
            FileHandle worldFile = fileHandleArray[i3];
            if ((string == null || worldFile.name().startsWith(string)) && worldFile.name().endsWith(".world")) {
                ++i1;
            }
            ++i3;
        }
        i3 = 0;
        worldSaveInfoArray = new WorldSaveInfo[i1];
        int i5 = 0;
        while (i5 < fileHandleArray.length) {
            block19: {
                FileHandle fileHandle = fileHandleArray[i5];
                if ((string == null || fileHandle.name().startsWith(string)) && fileHandle.name().endsWith(".world")) {
                    WorldSaveInfo worldSaveInfo = new WorldSaveInfo();
                    worldSaveInfo.a = fileHandle.name();
                    BufferedReader bufferedReader = null;
                    try {
                        try {
                            String string2;
                            bufferedReader = new BufferedReader(new InputStreamReader(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + worldSaveInfo.a).getFileHandle().read(), StandardCharsets.ISO_8859_1));
                            while ((string2 = bufferedReader.readLine()) != null) {
                                if (string2.startsWith("name =")) {
                                    worldSaveInfo.b = string2.substring(string2.indexOf("=") + 1).trim();
                                }
                                if (!string2.startsWith("info =")) continue;
                                worldSaveInfo.c = string2.substring(string2.indexOf("=") + 1).trim();
                                break;
                            }
                            worldSaveInfoArray[i3] = worldSaveInfo;
                            ++i3;
                        }
                        catch (IOException iOException) {
                            OsfLog.error("Error parsing file: " + worldSaveInfo.a);
                            if (bufferedReader == null) break block19;
                            try {
                                bufferedReader.close();
                            }
                            catch (IOException iOException2) {}
                            bufferedReader = null;
                            break block19;
                        }
                    }
                    catch (Throwable throwable) {
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            }
                            catch (IOException iOException) {}
                            bufferedReader = null;
                        }
                        throw throwable;
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        }
                        catch (IOException iOException) {}
                        bufferedReader = null;
                    }
                }
            }
            ++i5;
        }
        return worldSaveInfoArray;
    }

    public static final World load(String string) {
        World world;
        BufferedReader bufferedReader = null;
        try {
        block99: {
            MapDefinition mapDefinition;
            String string2;
            String[] stringArray;
            int i3;
            block97: {
                block98: {
                    bufferedReader = null;
                    // The map format uses Latin-1 symbols (0xA6 cell borders, 0xB0 degrees).
                    // With the platform default charset (e.g. GBK on Chinese Windows) those bytes
                    // become lead bytes that swallow the following ASCII char, so every grid column
                    // shifts: units are dropped ("Unit character not defined"), the CAPTURE_FLAG
                    // structure is never found and the saved world ends up with flag index -1,
                    // which then crashes the reload. Pin the charset the files were written with.
                    bufferedReader = new BufferedReader(new InputStreamReader(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + string).getFileHandle().read(), StandardCharsets.ISO_8859_1));
                    i3 = -1;
                    stringArray = new String[2];
                    string2 = bufferedReader.readLine();
                    mapDefinition = MapDefinition.getRuleset(string2.substring(0, 3).toLowerCase());
                    if (mapDefinition != null) break block97;
                    OsfLog.error("Invalid map: " + string2);
                    if (bufferedReader == null) break block98;
                    try {
                        bufferedReader.close();
                    }
                    catch (IOException iOException) {}
                    bufferedReader = null;
                }
                return null;
            }
            String string3 = null;
            String string4 = null;
            GameMode gameMode = null;
            Vec2 vec2 = null;
            ArrayList<Integer> arrayList = null;
            ArrayList<Integer> arrayList2 = null;
            Vec2 vec22 = null;
            World world2 = null;
            TerrainGrid terrainGrid = null;
            int i15 = 0;
            int i16 = 0;
            int i17 = 0;
            PlayerList playerList = new PlayerList();
            ArrayList<Character> arrayList3 = new ArrayList<Character>();
            ArrayList<String> arrayList4 = new ArrayList<String>();
            ArrayList<UnitTypeList> arrayList5 = new ArrayList<UnitTypeList>();
            ArrayList<Float> arrayList6 = new ArrayList<Float>();
            block19: while ((string2 = bufferedReader.readLine()) != null) {
                if (string2.length() == 0 || string2.startsWith("  ") || string2.charAt(0) == '#') continue;
                if (string2.startsWith("setup:")) {
                    i3 = 0;
                    continue;
                }
                if (string2.startsWith("nations:")) {
                    i3 = 1;
                    continue;
                }
                if (string2.startsWith("units:")) {
                    i3 = 2;
                    continue;
                }
                if (string2.startsWith("map:")) {
                    i3 = 3;
                    if (string3 == null) {
                        throw new IOException("Name is missing.");
                    }
                    if (terrainGrid == null) {
                        throw new IOException("Terrain not defined.");
                    }
                    if (gameMode == null) {
                        gameMode = SupremacyMode.create();
                    }
                    if (playerList.size() == 0) {
                        throw new IOException("No players defined.");
                    }
                    world2 = new World();
                    world2.setMapDefinition(mapDefinition);
                    world2.setName(string3);
                    world2.setDescription(string4);
                    world2.setRandom(new FastRandom());
                    world2.setGameMode(gameMode);
                    world2.setSpeedFactor(UserConfig.getWorldSpeedFactor());
                    world2.setUnits(new UnitList());
                    world2.setBirds(new BirdList());
                    world2.setTerrainGrid(terrainGrid);
                    world2.setPlayers(playerList);
                    continue;
                }
                switch (i3) {
                    case 0: {
                        WorldSerializer.parseProperty(string2, stringArray);
                        String string5 = stringArray[0];
                        String string6 = stringArray[1];
                        if (string5.startsWith("#")) continue block19;
                        if (string5.equals("size")) {
                            String[] stringArray2 = string6.split("x");
                            i15 = Integer.parseInt(stringArray2[0]);
                            i16 = Integer.parseInt(stringArray2[1]);
                            terrainGrid = new TerrainGrid(i15, i16);
                            break;
                        }
                        if (string5.equals("name")) {
                            string3 = string6;
                            break;
                        }
                        if (string5.equals("info")) {
                            string4 = string6;
                            break;
                        }
                        if (string5.equals("goal")) {
                            if (string6.startsWith("SUPREMACY")) {
                                SupremacyMode supremacyMode = SupremacyMode.create();
                                String string7 = string6.substring(string6.indexOf("(") + 1, string6.indexOf(")")).trim();
                                supremacyMode.setStructureRatio((float)Integer.parseInt(string7) * 0.01f);
                                gameMode = supremacyMode;
                                break;
                            }
                            if (string6.startsWith("SURVIVAL")) {
                                SurvivalMode survivalMode = SurvivalMode.create();
                                String[] stringArray3 = string6.substring(string6.indexOf("(") + 1, string6.indexOf(")")).split(",");
                                arrayList = new ArrayList<Integer>();
                                if (stringArray3[0].contains("+")) {
                                    String[] stringArray4 = stringArray3[0].trim().split("\\+");
                                    int n = 0;
                                    while (n < stringArray4.length) {
                                        arrayList.add(Integer.parseInt(stringArray4[n].trim()));
                                        ++n;
                                    }
                                } else {
                                    arrayList.add(Integer.parseInt(stringArray3[0].trim()));
                                }
                                survivalMode.setBaseStationCount(Integer.parseInt(stringArray3[1].trim()));
                                gameMode = survivalMode;
                                break;
                            }
                            if (string6.startsWith("ANNIHILATION")) {
                                AnnihilationMode annihilationMode = AnnihilationMode.create();
                                String string8 = string6.substring(string6.indexOf("(") + 1, string6.indexOf(")")).trim();
                                annihilationMode.setHealthThreshold(Long.parseLong(string8));
                                gameMode = annihilationMode;
                                break;
                            }
                            if (string6.startsWith("CAPTURE_FLAG")) {
                                CaptureTheFlagMode captureTheFlagMode = CaptureTheFlagMode.create();
                                String[] stringArray5 = string6.substring(string6.indexOf("(") + 1, string6.indexOf(")")).split(",");
                                captureTheFlagMode.setHoldSeconds(Integer.parseInt(stringArray5[0].trim()));
                                String[] stringArray6 = stringArray5[1].trim().split(":");
                                vec2 = new Vec2((float)Integer.parseInt(stringArray6[0]) + 0.5f, (float)Integer.parseInt(stringArray6[1]) + 0.5f);
                                gameMode = captureTheFlagMode;
                                break;
                            }
                            if (!string6.startsWith("ESCORT")) continue block19;
                            EscortMode escortMode = EscortMode.create();
                            String[] stringArray7 = string6.substring(string6.indexOf("(") + 1, string6.indexOf(")")).split(",");
                            arrayList2 = new ArrayList<Integer>();
                            if (stringArray7[0].contains("+")) {
                                String[] stringArray8 = stringArray7[0].trim().split("\\+");
                                int n = 0;
                                while (n < stringArray8.length) {
                                    arrayList2.add(Integer.parseInt(stringArray8[n].trim()));
                                    ++n;
                                }
                            } else {
                                arrayList2.add(Integer.parseInt(stringArray7[0].trim()));
                            }
                            escortMode.setTruckCount(Integer.parseInt(stringArray7[1].trim()));
                            String[] stringArray9 = stringArray7[2].trim().split(":");
                            vec22 = new Vec2((float)Integer.parseInt(stringArray9[0]) + 0.5f, (float)Integer.parseInt(stringArray9[1]) + 0.5f);
                            gameMode = escortMode;
                            break;
                        }
                        if (string5.equals("option")) {
                            OsfLog.error("Key not supported: \"option\" (became obsolete on 2011/11/06)");
                            break;
                        }
                        if (string5.equals("time_limit")) {
                            if (string6.equals("")) continue block19;
                            gameMode.setTimeLimit(Integer.parseInt(string6));
                            break;
                        }
                        throw new IOException("Parameter not defined: " + string2);
                    }
                    case 1: {
                        WorldSerializer.parseProperty(string2, stringArray);
                        String string9 = stringArray[0];
                        String string10 = stringArray[1];
                        String[] stringArray10 = string10.split(" ");
                        Faction faction = mapDefinition.getFactions().getByKey(stringArray10[0]);
                        Team team = Team.fromKey(stringArray10[1]);
                        long l = Long.parseLong(stringArray10[2]);
                        long l2 = Long.parseLong(stringArray10[3]);
                        int i32 = stringArray10[4].equals("YES") ? 1 : 0;
                        boolean i33 = stringArray10[5].equals("YES");
                        int i34 = Integer.parseInt(stringArray10[6]);
                        UnitTypeList unitTypeList = new UnitTypeList((Collection)mapDefinition.getUnitTypes());
                        if (stringArray10.length > 7) {
                            if (stringArray10.length > 8) {
                                unitTypeList.remove(unitTypeList.getByKey(stringArray10[7].substring(stringArray10[7].indexOf("(") + 1, stringArray10[7].indexOf(",")).trim()));
                                int n = 8;
                                while (n < stringArray10.length - 1) {
                                    unitTypeList.remove(unitTypeList.getByKey(stringArray10[n].substring(0, stringArray10[n].indexOf(",")).trim()));
                                    ++n;
                                }
                                unitTypeList.remove(unitTypeList.getByKey(stringArray10[stringArray10.length - 1].substring(0, stringArray10[stringArray10.length - 1].indexOf(")")).trim()));
                            } else {
                                unitTypeList.remove(unitTypeList.getByKey(stringArray10[7].substring(stringArray10[7].indexOf("(") + 1, stringArray10[7].indexOf(")")).trim()));
                            }
                        }
                        FogOfWar fogOfWar = new FogOfWar(i15, i16);
                        Player player = Player.create(playerList.size(), faction, team, l, l2, i33, i32 != 0, unitTypeList, fogOfWar);
                        player.b(i34);
                        playerList.add(player);
                        arrayList3.add(Character.valueOf(string9.charAt(0)));
                        break;
                    }
                    case 2: {
                        float f;
                        WorldSerializer.parseProperty(string2, stringArray);
                        String string11 = stringArray[0];
                        String string12 = stringArray[1];
                        int n = string12.indexOf("-");
                        if (n >= 0) {
                            f = (float)(Integer.parseInt(string12.substring(n + 1).trim()) - 90) * ((float)Math.PI * 2) / 360.0f;
                            string12 = string12.substring(0, n);
                        } else {
                            f = 0.0f;
                        }
                        n = string12.indexOf("(");
                        UnitTypeList unitTypeList = new UnitTypeList();
                        if (n >= 0) {
                            unitTypeList.add(mapDefinition.getUnitTypes().getByKey(string12.substring(0, n)));
                            String[] stringArray11 = string12.substring(n + 1, string12.indexOf(")")).split(",");
                            int n2 = 0;
                            while (n2 < stringArray11.length) {
                                unitTypeList.add(mapDefinition.getUnitTypes().getByKey(stringArray11[n2].trim()));
                                ++n2;
                            }
                        } else {
                            unitTypeList.add(mapDefinition.getUnitTypes().getByKey(string12));
                        }
                        arrayList4.add(string11);
                        arrayList5.add(unitTypeList);
                        arrayList6.add(Float.valueOf(f));
                        break;
                    }
                    case 3: {
                        int i32;
                        int n = i17 >> 1;
                        int i42 = i15 * 4 + 1;
                        if (string2.length() < i42) {
                            StringBuilder stringBuilder2 = new StringBuilder(i42);
                            stringBuilder2.append(string2);
                            while (stringBuilder2.length() < i42) {
                                stringBuilder2.append(' ');
                            }
                            string2 = stringBuilder2.toString();
                        }
                        if ((i17 & 1) == 0) {
                            if (n > i16) {
                                OsfLog.error("Map height too large.");
                            } else {
                                int n3 = 0;
                                while (n3 <= i15) {
                                    int i40 = n3 * 4;
                                    terrainGrid.setCornerLand(n3, n, i40 < string2.length() && string2.charAt(i40) != ' ');
                                    ++n3;
                                }
                                n3 = 0;
                                while (n3 < i15) {
                                    char c = string2.charAt(n3 * 4 + 2);
                                    if (c == '+' || c == '#') {
                                        RoadTile roadTile = null;
                                        if (n < i16) {
                                            roadTile = terrainGrid.getRoadTile(n3, n);
                                            if (roadTile == null) {
                                                roadTile = RoadTile.create(RoadMask.a, false);
                                                terrainGrid.setRoadTile(n3, n, roadTile);
                                            }
                                            roadTile.setRoadMask(RoadMask.values()[roadTile.getRoadMask().ordinal() | 1]);
                                            if (c == '#') {
                                                roadTile.a(true);
                                            }
                                        }
                                        if (n > 0) {
                                            roadTile = terrainGrid.getRoadTile(n3, n - 1);
                                            if (roadTile == null) {
                                                roadTile = RoadTile.create(RoadMask.a, false);
                                                terrainGrid.setRoadTile(n3, n - 1, roadTile);
                                            }
                                            roadTile.setRoadMask(RoadMask.values()[roadTile.getRoadMask().ordinal() | 4]);
                                            if (c == '#') {
                                                roadTile.a(true);
                                            }
                                        }
                                    }
                                    ++n3;
                                }
                            }
                        } else if (n >= i16) {
                            OsfLog.error("Map height too large.");
                        } else {
                            int n4 = 0;
                            while (n4 < i15) {
                                char c = string2.charAt(n4 * 4 + 1);
                                char c2 = string2.charAt(n4 * 4 + 2);
                                char c3 = string2.charAt(n4 * 4 + 3);
                                if (c == ' ') {
                                    mapDefinition.paintSite(world2, n4, n, false, false, c2, c3);
                                } else {
                                    Object object;
                                    ArrayList arrayList7 = null;
                                    float f = 0.0f;
                                    int n5 = 0;
                                    while (n5 < arrayList4.size()) {
                                        object = (String)arrayList4.get(n5);
                                        if (((String)object).charAt(0) == c && ((String)object).charAt(1) == c2) {
                                            arrayList7 = (UnitTypeList)arrayList5.get(n5);
                                            f = ((Float)arrayList6.get(n5)).floatValue();
                                        }
                                        ++n5;
                                    }
                                    if (arrayList7 == null) {
                                        world2.getMapDefinition().paintUnit(world2, n4, n, false, false, c, c2, c3);
                                    } else {
                                        Player player = null;
                                        int n6 = 0;
                                        while (n6 < arrayList3.size()) {
                                            if (((Character)arrayList3.get(n6)).charValue() == c3) {
                                                player = (Player)playerList.get(n6);
                                            }
                                            ++n6;
                                        }
                                        object = world2.spawnUnit((UnitType)arrayList7.get(0), (float)n4 + 0.5f, (float)n + 0.5f, player);
                                        ((Unit)object).setAngle(f);
                                        if (player != null) {
                                            i32 = 1;
                                            while (i32 < arrayList7.size()) {
                                                world2.createHostedUnit((UnitType)arrayList7.get(i32), (Unit)object, player);
                                                ++i32;
                                            }
                                        }
                                    }
                                }
                                ++n4;
                            }
                            n4 = 0;
                            while (n4 <= i15) {
                                int i41 = n4 * 4;
                                char c = i41 < string2.length() ? string2.charAt(i41) : ' ';
                                if (c == '+' || c == '#') {
                                    RoadTile roadTile = null;
                                    if (n4 < i15) {
                                        roadTile = terrainGrid.getRoadTile(n4, n);
                                        if (roadTile == null) {
                                            roadTile = RoadTile.create(RoadMask.a, false);
                                            terrainGrid.setRoadTile(n4, n, roadTile);
                                        }
                                        roadTile.setRoadMask(RoadMask.values()[roadTile.getRoadMask().ordinal() | 8]);
                                        if (c == '#') {
                                            roadTile.a(true);
                                        }
                                    }
                                    if (n4 > 0) {
                                        roadTile = terrainGrid.getRoadTile(n4 - 1, n);
                                        if (roadTile == null) {
                                            roadTile = RoadTile.create(RoadMask.a, false);
                                            terrainGrid.setRoadTile(n4 - 1, n, roadTile);
                                        }
                                        roadTile.setRoadMask(RoadMask.values()[roadTile.getRoadMask().ordinal() | 2]);
                                        if (c == '#') {
                                            roadTile.a(true);
                                        }
                                    }
                                }
                                ++n4;
                            }
                        }
                        ++i17;
                        break;
                    }
                    default: {
                        OsfLog.error("Invalid section: " + i3);
                    }
                }
            }
            if (mapDefinition.getId().equals("tsf")) {
                int n = 0;
                while (n < i16) {
                    int n7 = 0;
                    while (n7 < i15) {
                        Site site = terrainGrid.getSite(n7, n);
                        if (site != null && site.getTerrainType().getCode() == 'X') {
                            terrainGrid.setSite(n7, n, site.getTerrainType(), (HashUtils.hash(n7, n) & Integer.MAX_VALUE) % 8 + (terrainGrid.isCoastTile(n7, n) ? 8 : 0));
                        }
                        ++n7;
                    }
                    ++n;
                }
            }
            if (vec2 != null) {
                CaptureTheFlagMode captureTheFlagMode = (CaptureTheFlagMode)gameMode;
                captureTheFlagMode.setFlag(world2.getTerrainGrid().getUnitAtPosition(vec2));
            }
            if (arrayList != null) {
                SurvivalMode survivalMode = (SurvivalMode)gameMode;
                survivalMode.setSurvivors(new PlayerList());
                int n = 0;
                while (n < arrayList.size()) {
                    survivalMode.getSurvivors().add((Player)playerList.get((Integer)arrayList.get(n)));
                    ++n;
                }
            }
            if (arrayList2 != null) {
                EscortMode escortMode = (EscortMode)gameMode;
                escortMode.setEscortPlayers(new PlayerList());
                int n = 0;
                while (n < arrayList2.size()) {
                    escortMode.getEscortPlayers().add((Player)playerList.get((Integer)arrayList2.get(n)));
                    ++n;
                }
                escortMode.setTargetArea(world2.getTerrainGrid().getCenter((int)vec22.getX(), (int)vec22.getY()));
            }
            world = world2;
            if (bufferedReader == null) break block99;
            try {
                bufferedReader.close();
            }
            catch (IOException iOException) {}
            bufferedReader = null;
        }
        return world;
        }
        catch (Exception exception) {
            OsfLog.error("Error loading world: " + exception);
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

    private static final void parseProperty(String string, String[] stringArray)  throws IOException {
        int i2 = string.indexOf("=");
        if (i2 < 0) {
            throw new IOException("Not a property: " + string);
        }
        stringArray[0] = string.substring(0, i2).trim();
        stringArray[1] = string.substring(i2 + 1).trim();
    }

    /**
     * Validates an index read from the stream. The decompiled reader indexed enum arrays and lists
     * directly, so a corrupt save surfaced as an opaque ArrayIndexOutOfBoundsException /
     * IndexOutOfBoundsException; this turns it into a readable message naming the offending field.
     * Note: the round-trip test (WorldSerializerRoundTripTest) pins that valid saves keep loading.
     */
    private static int readIndex(DataReader dataReader, int size, String what) {
        return WorldSerializer.checkIndex(dataReader.readInt(), size, what);
    }

    private static int checkIndex(int value, int size, String what) {
        if (value < 0 || value >= size) {
            throw new IllegalArgumentException("Corrupt save: " + what + " index " + value
                    + " out of range [0, " + size + ")");
        }
        return value;
    }

    public static final World read(DataReader dataReader) {
        if (dataReader.readBoolean()) {
            World world = new World();
            WorldSerializer.readBody(dataReader, world);
            return world;
        }
        return null;
    }

    public static final void write(DataWriter dataWriter, World world) {
        if (world != null) {
            dataWriter.writeBoolean(true);
            WorldSerializer.writeBody(dataWriter, world);
        } else {
            dataWriter.writeBoolean(false);
        }
    }

    public static final void readBody(DataReader dataReader, World world) {
        int n;
        GameMode gameMode;
        Object object;
        int i22;
        int n2;
        Object object2;
        int n3;
        Object object3;
        int n4;
        int n5;
        Object object4;
        int i8;
        int i2 = dataReader.readInt();
        MapDefinition mapDefinition = i2 >= 3 ? MapDefinition.getRuleset(dataReader.readString()) : MapDefinition.getRuleset("tsf");
        world.setMapDefinition(mapDefinition);
        world.setName(dataReader.readString());
        world.setDescription(dataReader.readString());
        world.setRandom(new FastRandom(dataReader.readLong()));
        world.setGameTime(dataReader.readFloat());
        world.setTurn(dataReader.readInt());
        PlayerList playerList = new PlayerList();
        world.setPlayers(playerList);
        int i5 = dataReader.readInt();
        int n6 = 0;
        while (n6 < i5) {
            Player player = new Player();
            playerList.add(player);
            player.setController(Controller.values()[WorldSerializer.readIndex(dataReader, Controller.values().length, "controller")]);
            player.a(dataReader.readLong());
            player.setAlive(dataReader.readBoolean());
            player.setFaction((Faction)mapDefinition.getFactions().get(WorldSerializer.readIndex(dataReader, mapDefinition.getFactions().size(), "faction")));
            i8 = dataReader.readInt();
            if (i8 >= 0) {
                player.setTeam(Team.values()[WorldSerializer.checkIndex(i8, Team.values().length, "team")]);
            }
            player.setResources(dataReader.readLong());
            player.setIncomePerBuilding(dataReader.readLong());
            object4 = new UnitTypeList();
            player.setAvailableUnitTypes((UnitTypeList)object4);
            n5 = dataReader.readInt();
            int n7 = 0;
            while (n7 < n5) {
                ((ArrayList)object4).add((UnitType)mapDefinition.getUnitTypes().get(WorldSerializer.readIndex(dataReader, mapDefinition.getUnitTypes().size(), "available unit type")));
                ++n7;
            }
            n7 = dataReader.readInt();
            n4 = dataReader.readInt();
            object3 = new FogOfWar(n7, n4);
            player.setFogOfWar((FogOfWar)object3);
            int n8 = 0;
            while (n8 <= n4) {
                n3 = 0;
                while (n3 <= n7) {
                    ((FogOfWar)object3).setCornerExplored(n3, n8, dataReader.readBoolean());
                    ((FogOfWar)object3).setCornerVisible(n3, n8, dataReader.readBoolean());
                    ++n3;
                }
                ++n8;
            }
            player.setExplorationEnabled(dataReader.readBoolean());
            player.setFogEnabled(dataReader.readBoolean());
            player.b(dataReader.readInt());
            object2 = new PlayerStatistics();
            player.setStatistics((PlayerStatistics)object2);
            ((PlayerStatistics)object2).setDestroyedUnits(dataReader.readInt());
            ((PlayerStatistics)object2).setLostUnits(dataReader.readInt());
            ((PlayerStatistics)object2).setDamageInflicted(dataReader.readInt());
            ((PlayerStatistics)object2).setDamageReceived(dataReader.readInt());
            ((PlayerStatistics)object2).setBasesCaptured(dataReader.readInt());
            ((PlayerStatistics)object2).setBasesLost(dataReader.readInt());
            ++n6;
        }
        UnitList unitList = new UnitList(2048);
        world.setUnits(unitList);
        int n9 = dataReader.readInt();
        i8 = 0;
        while (i8 < n9) {
            unitList.add(new Unit());
            ++i8;
        }
        i8 = 0;
        while (i8 < n9) {
            int n10;
            object4 = (Unit)unitList.get(i8);
            ((Unit)object4).setUnitType((UnitType)mapDefinition.getUnitTypes().get(WorldSerializer.readIndex(dataReader, mapDefinition.getUnitTypes().size(), "unit type")));
            n5 = dataReader.readInt();
            if (n5 >= 0) {
                ((Unit)object4).setOwner((Player)playerList.get(WorldSerializer.checkIndex(n5, playerList.size(), "owner")));
            }
            UnitPosition unitPosition = new UnitPosition();
            ((Unit)object4).setPosition(unitPosition);
            if (dataReader.readBoolean()) {
                unitPosition.bindTo((Unit)unitList.get(WorldSerializer.readIndex(dataReader, unitList.size(), "unit position target")));
            } else {
                unitPosition.setX(dataReader.readFloat());
                unitPosition.setY(dataReader.readFloat());
            }
            ((Unit)object4).setOrderMode(UnitOrderMode.values()[WorldSerializer.readIndex(dataReader, UnitOrderMode.values().length, "order mode")]);
            UnitPosition unitPosition2 = new UnitPosition();
            ((Unit)object4).b(unitPosition2);
            if (dataReader.readBoolean()) {
                int n11 = dataReader.readInt();
                if (n11 >= 0) {
                    unitPosition2.bindTo((Unit)unitList.get(WorldSerializer.checkIndex(n11, unitList.size(), "move target unit")));
                } else {
                    unitPosition2.setX(unitPosition.getX());
                    unitPosition2.setY(unitPosition.getY());
                }
            } else {
                unitPosition2.setX(dataReader.readFloat());
                unitPosition2.setY(dataReader.readFloat());
            }
            ((Unit)object4).setGuarding(dataReader.readBoolean());
            object3 = new UnitPosition();
            ((Unit)object4).setRallyPosition((UnitPosition)object3);
            if (dataReader.readBoolean()) {
                int n12 = dataReader.readInt();
                if (n12 >= 0) {
                    ((UnitPosition)object3).bindTo((Unit)unitList.get(WorldSerializer.checkIndex(n12, unitList.size(), "rally unit")));
                } else {
                    ((UnitPosition)object3).setX(unitPosition.getX());
                    ((UnitPosition)object3).setY(unitPosition.getY());
                }
            } else {
                ((UnitPosition)object3).setX(dataReader.readFloat());
                ((UnitPosition)object3).setY(dataReader.readFloat());
            }
            ((Unit)object4).setRally(dataReader.readBoolean());
            ((Unit)object4).setAngle(dataReader.readFloat());
            ((Unit)object4).setMoveProgress(dataReader.readFloat());
            ((Unit)object4).setCount(dataReader.readInt());
            ((Unit)object4).setProgress(dataReader.readFloat());
            ((Unit)object4).setHealth(dataReader.readInt());
            ((Unit)object4).setSalvoCount(dataReader.readInt());
            object2 = new UnitList();
            ((Unit)object4).setSubUnits((UnitList)object2);
            n3 = dataReader.readInt();
            int n13 = 0;
            while (n13 < n3) {
                ((ArrayList)object2).add((Unit)unitList.get(WorldSerializer.readIndex(dataReader, unitList.size(), "sub unit")));
                ++n13;
            }
            ((Unit)object4).setHostedAuto(dataReader.readBoolean());
            UnitList unitList2 = new UnitList();
            ((Unit)object4).setGuardFollowers(unitList2);
            n2 = dataReader.readInt();
            int n14 = 0;
            while (n14 < n2) {
                unitList2.add((Unit)unitList.get(WorldSerializer.readIndex(dataReader, unitList.size(), "guard follower")));
                ++n14;
            }
            Volley volley = new Volley();
            ((Unit)object4).setVolley(volley);
            int n15 = dataReader.readInt();
            if (n15 >= 0) {
                volley.setAmmoType(AmmoType.values()[WorldSerializer.checkIndex(n15, AmmoType.values().length, "ammo type")]);
            }
            if ((n10 = dataReader.readInt()) >= 0) {
                volley.setTargetUnit((Unit)unitList.get(WorldSerializer.checkIndex(n10, unitList.size(), "volley target")));
            }
            volley.setOrigin(new Vec3());
            volley.getOrigin().setX(dataReader.readFloat());
            volley.getOrigin().setY(dataReader.readFloat());
            volley.getOrigin().setZ(dataReader.readFloat());
            volley.setDirection(new Vec3());
            volley.getDirection().setX(dataReader.readFloat());
            volley.getDirection().setY(dataReader.readFloat());
            volley.getDirection().setZ(dataReader.readFloat());
            volley.setIntensity(dataReader.readFloat());
            ProjectileList projectileList = new ProjectileList();
            volley.setProjectiles(projectileList);
            i22 = dataReader.readInt();
            int n16 = 0;
            while (n16 < i22) {
                Projectile projectile = new Projectile();
                projectileList.add(projectile);
                projectile.setLaunchTime(dataReader.readFloat());
                projectile.setActive(dataReader.readBoolean());
                projectile.setPosition(new Vec3());
                projectile.getPosition().setX(dataReader.readFloat());
                projectile.getPosition().setY(dataReader.readFloat());
                projectile.getPosition().setZ(dataReader.readFloat());
                ++n16;
            }
            ((Unit)object4).setVolleyActive(dataReader.readBoolean());
            ((Unit)object4).setReadyTime(dataReader.readFloat());
            object = new EffectTimer();
            ((EffectTimer)object).setTime(dataReader.readFloat());
            ((Unit)object4).setEffectTimer((EffectTimer)object);
            ((Unit)object4).setEffectActive(dataReader.readBoolean());
            ++i8;
        }
        i8 = dataReader.readInt();
        int n17 = dataReader.readInt();
        TerrainGrid terrainGrid = new TerrainGrid(i8, n17);
        world.setTerrainGrid(terrainGrid);
        int n18 = 0;
        while (n18 <= n17) {
            n4 = 0;
            while (n4 <= i8) {
                terrainGrid.setCornerLand(n4, n18, dataReader.readBoolean());
                ++n4;
            }
            ++n18;
        }
        n18 = 0;
        while (n18 < n17) {
            n4 = 0;
            while (n4 < i8) {
                if (dataReader.readBoolean()) {
                    terrainGrid.setSite(n4, n18, (TerrainType)mapDefinition.getTerrainTypes().get(dataReader.readInt()), dataReader.readInt());
                }
                if (dataReader.readBoolean()) {
                    terrainGrid.setUnitAtTile(n4, n18, (Unit)unitList.get(dataReader.readInt()));
                }
                object3 = terrainGrid.getUnitsAtTile(n4, n18);
                int n19 = dataReader.readInt();
                n3 = 0;
                while (n3 < n19) {
                    ((ArrayList)object3).add((Unit)unitList.get(dataReader.readInt()));
                    ++n3;
                }
                ++n4;
            }
            ++n18;
        }
        BirdList birdList = new BirdList(16);
        world.setBirds(birdList);
        n4 = dataReader.readInt();
        int n20 = 0;
        while (n20 < n4) {
            Bird bird = new Bird();
            birdList.add(bird);
            bird.setType(BirdType.values()[dataReader.readInt()]);
            bird.setPosition(new Vec3());
            bird.getPosition().setX(dataReader.readFloat());
            bird.getPosition().setY(dataReader.readFloat());
            bird.getPosition().setZ(dataReader.readFloat());
            bird.setAngle(dataReader.readFloat());
            bird.setDistance(dataReader.readFloat());
            ++n20;
        }
        int n21 = dataReader.readInt();
        if (n21 == 0) {
            SupremacyMode supremacyMode = new SupremacyMode();
            supremacyMode.setStructureRatio(dataReader.readFloat());
            gameMode = supremacyMode;
        } else if (n21 == 1) {
            AnnihilationMode annihilationMode = new AnnihilationMode();
            annihilationMode.setHealthThreshold(dataReader.readLong());
            gameMode = annihilationMode;
        } else if (n21 == 2) {
            CaptureTheFlagMode captureTheFlagMode = new CaptureTheFlagMode();
            // Older saves written while the flag structure was missing (e.g. due to the charset
            // bug below) store -1 here; keep the flag empty instead of crashing the load.
            int n8 = dataReader.readInt();
            if (n8 >= 0 && n8 < unitList.size()) {
                captureTheFlagMode.setFlag((Unit)unitList.get(n8));
            } else {
                OsfLog.error("Flag unit index out of range: " + n8 + " (units: " + unitList.size() + ")");
            }
            captureTheFlagMode.setHoldSeconds(dataReader.readFloat());
            captureTheFlagMode.setHoldStartTime(dataReader.readFloat());
            gameMode = captureTheFlagMode;
        } else if (n21 == 3) {
            SurvivalMode survivalMode = new SurvivalMode();
            survivalMode.setSurvivors(new PlayerList());
            int n22 = dataReader.readInt();
            n2 = 0;
            while (n2 < n22) {
                survivalMode.getSurvivors().add((Player)playerList.get(dataReader.readInt()));
                ++n2;
            }
            survivalMode.setBaseStationCount(dataReader.readInt());
            gameMode = survivalMode;
        } else if (n21 == 4) {
            EscortMode escortMode = new EscortMode();
            escortMode.setEscortPlayers(new PlayerList());
            int n23 = dataReader.readInt();
            n2 = 0;
            while (n2 < n23) {
                escortMode.getEscortPlayers().add((Player)playerList.get(dataReader.readInt()));
                ++n2;
            }
            escortMode.setTruckCount(dataReader.readInt());
            escortMode.setTargetArea(terrainGrid.getCenter(dataReader.readInt(), dataReader.readInt()));
            gameMode = escortMode;
        } else {
            gameMode = new SupremacyMode();
            OsfLog.error("Goal type not defined: " + n21);
        }
        gameMode.setTimeLimit(dataReader.readFloat());
        world.setGameMode(gameMode);
        if (i2 >= 2) {
            dataReader.readString();
            int n24 = 0;
            while (n24 < n17) {
                int n25 = 0;
                while (n25 < i8) {
                    if (dataReader.readBoolean()) {
                        RoadTile roadTile = new RoadTile();
                        roadTile.setRoadMask(RoadMask.values()[dataReader.readInt()]);
                        terrainGrid.setRoadTile(n25, n24, roadTile);
                    }
                    ++n25;
                }
                ++n24;
            }
        }
        if (i2 >= 4) {
            n = 0;
            while (n < playerList.size()) {
                int n26 = dataReader.readInt();
                UnitGroupSet unitGroupSet = new UnitGroupSet(n26);
                ((Player)playerList.get(n)).setUnitGroups(unitGroupSet);
                int n27 = 0;
                while (n27 < unitGroupSet.size()) {
                    UnitGroup unitGroup = unitGroupSet.get(n27);
                    UnitList unitList3 = unitGroup.getUnits();
                    int n28 = dataReader.readInt();
                    i22 = 0;
                    while (i22 < n28) {
                        object = (Unit)unitList.get(dataReader.readInt());
                        ((Unit)object).setUnitGroup(unitGroup);
                        unitList3.add(object);
                        ++i22;
                    }
                    ++n27;
                }
                ++n;
            }
        } else {
            n = 0;
            while (n < playerList.size()) {
                UnitGroupSet unitGroupSet = new UnitGroupSet();
                ((Player)playerList.get(n)).setUnitGroups(unitGroupSet);
                ++n;
            }
        }
        if (i2 >= 5) {
            n = 0;
            while (n < unitList.size()) {
                Unit unit = (Unit)unitList.get(n);
                unit.setMoveTargetActive(dataReader.readBoolean());
                UnitPosition unitPosition = new UnitPosition();
                unit.setMoveTargetPosition(unitPosition);
                unitPosition.setX(dataReader.readFloat());
                unitPosition.setY(dataReader.readFloat());
                if (dataReader.readBoolean()) {
                    unitPosition.bindTo((Unit)unitList.get(dataReader.readInt()));
                }
                unit.setRallySpreadIndex(dataReader.readInt());
                ++n;
            }
        } else {
            n = 0;
            while (n < unitList.size()) {
                Unit unit = (Unit)unitList.get(n);
                unit.setMoveTargetActive(false);
                unit.setMoveTargetPosition(new UnitPosition());
                unit.setRallySpreadIndex(0);
                ++n;
            }
        }
        if (i2 >= 6) {
            n = 0;
            while (n < playerList.size()) {
                ((Player)playerList.get(n)).setId(dataReader.readInt());
                ++n;
            }
            n = 0;
            while (n < unitList.size()) {
                ((Unit)unitList.get(n)).setId(dataReader.readInt());
                ++n;
            }
            unitList.setNextId(dataReader.readInt());
        } else {
            n = 0;
            while (n < playerList.size()) {
                ((Player)playerList.get(n)).setId(n);
                ++n;
            }
            n = 0;
            while (n < unitList.size()) {
                ((Unit)unitList.get(n)).setId(n);
                ++n;
            }
            unitList.setNextId(unitList.size());
        }
        if (i2 >= 7) {
            n = 0;
            while (n < unitList.size()) {
                Unit unit = (Unit)unitList.get(n);
                unit.setKillCount(dataReader.readInt());
                unit.setRank(dataReader.readInt());
                ++n;
            }
        } else {
            n = 0;
            while (n < unitList.size()) {
                Unit unit = (Unit)unitList.get(n);
                unit.setKillCount(0);
                unit.setRank(0);
                ++n;
            }
        }
        if (i2 >= 8) {
            n = 0;
            while (n < n17) {
                int n29 = 0;
                while (n29 < i8) {
                    RoadTile roadTile = terrainGrid.getRoadTile(n29, n);
                    if (roadTile != null) {
                        roadTile.a(dataReader.readBoolean());
                    }
                    ++n29;
                }
                ++n;
            }
        }
        if (i2 >= 9) {
            world.setSpeedFactor(dataReader.readFloat());
        } else {
            world.setSpeedFactor(UserConfig.getWorldSpeedFactor());
        }
    }

    public static final void writeBody(DataWriter dataWriter, World world) {
        Object object;
        Object object2;
        Object object3;
        int n;
        int n2;
        Object object4;
        Object object5;
        dataWriter.writeInt(9);
        MapDefinition mapDefinition = world.getMapDefinition();
        dataWriter.writeString(mapDefinition.getId());
        dataWriter.writeString(world.getName());
        dataWriter.writeString(world.getDescription());
        dataWriter.writeLong(world.getRandom().getSeed());
        dataWriter.writeFloat(world.getGameTime());
        dataWriter.writeInt(world.getTurn());
        PlayerList playerList = world.getPlayers();
        dataWriter.writeInt(playerList.size());
        int n3 = 0;
        while (n3 < playerList.size()) {
            Player player = (Player)playerList.get(n3);
            dataWriter.writeInt(player.getController().ordinal());
            dataWriter.writeLong(player.c());
            dataWriter.writeBoolean(player.isAlive());
            dataWriter.writeInt(player.getFaction().getId());
            dataWriter.writeInt(player.getTeam() == null ? -1 : player.getTeam().ordinal());
            dataWriter.writeLong(player.getResources());
            dataWriter.writeLong(player.getIncomePerBuilding());
            object5 = player.getAvailableUnitTypes();
            dataWriter.writeInt(((ArrayList)object5).size());
            int n4 = 0;
            while (n4 < ((ArrayList)object5).size()) {
                dataWriter.writeInt(mapDefinition.getUnitTypes().indexOf(((ArrayList)object5).get(n4)));
                ++n4;
            }
            object4 = player.getFogOfWar();
            n2 = ((FogOfWar)object4).getWidth();
            n = ((FogOfWar)object4).getHeight();
            dataWriter.writeInt(n2);
            dataWriter.writeInt(n);
            int n5 = 0;
            while (n5 <= n) {
                int n6 = 0;
                while (n6 <= n2) {
                    dataWriter.writeBoolean(((FogOfWar)object4).isCornerExplored(n6, n5));
                    dataWriter.writeBoolean(((FogOfWar)object4).isCornerVisible(n6, n5));
                    ++n6;
                }
                ++n5;
            }
            dataWriter.writeBoolean(player.isExplorationEnabled());
            dataWriter.writeBoolean(player.isFogEnabled());
            dataWriter.writeInt(player.o());
            object3 = player.getStatistics();
            dataWriter.writeInt(((PlayerStatistics)object3).getDestroyedUnits());
            dataWriter.writeInt(((PlayerStatistics)object3).getLostUnits());
            dataWriter.writeInt(((PlayerStatistics)object3).getDamageInflicted());
            dataWriter.writeInt(((PlayerStatistics)object3).getDamageReceived());
            dataWriter.writeInt(((PlayerStatistics)object3).getBasesCaptured());
            dataWriter.writeInt(((PlayerStatistics)object3).getBasesLost());
            ++n3;
        }
        UnitList unitList = world.getUnits();
        dataWriter.writeInt(unitList.size());
        int n7 = 0;
        while (n7 < unitList.size()) {
            object5 = (Unit)unitList.get(n7);
            dataWriter.writeInt(mapDefinition.getUnitTypes().indexOf(((Unit)object5).getUnitType()));
            dataWriter.writeInt(((Unit)object5).getOwner() == null ? -1 : playerList.indexOf(((Unit)object5).getOwner()));
            object4 = ((Unit)object5).Y();
            if (((UnitPosition)object4).getUnit() != null) {
                dataWriter.writeBoolean(true);
                dataWriter.writeInt(unitList.indexOf(((UnitPosition)object4).getUnit()));
            } else {
                dataWriter.writeBoolean(false);
                dataWriter.writeFloat(((UnitPosition)object4).getX());
                dataWriter.writeFloat(((UnitPosition)object4).getY());
            }
            dataWriter.writeInt(((Unit)object5).getOrderMode().ordinal());
            UnitPosition unitPosition = ((Unit)object5).getGuardPosition();
            if (unitPosition.getUnit() != null) {
                dataWriter.writeBoolean(true);
                dataWriter.writeInt(unitList.indexOf(unitPosition.getUnit()));
            } else {
                dataWriter.writeBoolean(false);
                dataWriter.writeFloat(unitPosition.getX());
                dataWriter.writeFloat(unitPosition.getY());
            }
            dataWriter.writeBoolean(((Unit)object5).Z());
            UnitPosition unitPosition2 = ((Unit)object5).getRallyPosition();
            if (unitPosition2.getUnit() != null) {
                dataWriter.writeBoolean(true);
                dataWriter.writeInt(unitList.indexOf(unitPosition2.getUnit()));
            } else {
                dataWriter.writeBoolean(false);
                dataWriter.writeFloat(unitPosition2.getX());
                dataWriter.writeFloat(unitPosition2.getY());
            }
            dataWriter.writeBoolean(((Unit)object5).hasRally());
            dataWriter.writeFloat(((Unit)object5).getAngle());
            dataWriter.writeFloat(((Unit)object5).getMoveProgress());
            dataWriter.writeInt(((Unit)object5).getCount());
            dataWriter.writeFloat(((Unit)object5).getProgress());
            dataWriter.writeInt(((Unit)object5).getHealth());
            dataWriter.writeInt(((Unit)object5).getSalvoCount());
            object3 = ((Unit)object5).ad();
            dataWriter.writeInt(((ArrayList)object3).size());
            int n8 = 0;
            while (n8 < ((ArrayList)object3).size()) {
                dataWriter.writeInt(unitList.indexOf(((ArrayList)object3).get(n8)));
                ++n8;
            }
            dataWriter.writeBoolean(((Unit)object5).isHostedAuto());
            UnitList unitList2 = ((Unit)object5).ag();
            dataWriter.writeInt(unitList2.size());
            int n9 = 0;
            while (n9 < unitList2.size()) {
                dataWriter.writeInt(unitList.indexOf(unitList2.get(n9)));
                ++n9;
            }
            object2 = ((Unit)object5).getVolley();
            dataWriter.writeInt(((Volley)object2).getAmmoType() == null ? -1 : ((Volley)object2).getAmmoType().ordinal());
            dataWriter.writeInt(((Volley)object2).getTargetUnit() == null ? -1 : unitList.indexOf(((Volley)object2).getTargetUnit()));
            dataWriter.writeFloat(((Volley)object2).getOrigin().getX());
            dataWriter.writeFloat(((Volley)object2).getOrigin().getY());
            dataWriter.writeFloat(((Volley)object2).getOrigin().getZ());
            dataWriter.writeFloat(((Volley)object2).getDirection().getX());
            dataWriter.writeFloat(((Volley)object2).getDirection().getY());
            dataWriter.writeFloat(((Volley)object2).getDirection().getZ());
            dataWriter.writeFloat(((Volley)object2).getIntensity());
            object = ((Volley)object2).getProjectiles();
            dataWriter.writeInt(((ArrayList)object).size());
            int n10 = 0;
            while (n10 < ((ArrayList)object).size()) {
                Projectile projectile = (Projectile)((ArrayList)object).get(n10);
                dataWriter.writeFloat(projectile.getLaunchTime());
                dataWriter.writeBoolean(projectile.isActive());
                dataWriter.writeFloat(projectile.getPosition().getX());
                dataWriter.writeFloat(projectile.getPosition().getY());
                dataWriter.writeFloat(projectile.getPosition().getZ());
                ++n10;
            }
            dataWriter.writeBoolean(((Unit)object5).ai());
            dataWriter.writeFloat(((Unit)object5).getReadyTime());
            dataWriter.writeFloat(((Unit)object5).getEffectTimer().getTime());
            dataWriter.writeBoolean(((Unit)object5).ak());
            ++n7;
        }
        TerrainGrid terrainGrid = world.getTerrainGrid();
        int n11 = terrainGrid.getWidth();
        int n12 = terrainGrid.getHeight();
        dataWriter.writeInt(n11);
        dataWriter.writeInt(n12);
        n2 = 0;
        while (n2 <= n12) {
            n = 0;
            while (n <= n11) {
                dataWriter.writeBoolean(terrainGrid.isCornerLand(n, n2));
                ++n;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < n12) {
            n = 0;
            while (n < n11) {
                object3 = terrainGrid.getSite(n, n2);
                if (object3 != null) {
                    dataWriter.writeBoolean(true);
                    dataWriter.writeInt(mapDefinition.getTerrainTypes().indexOf(((Site)object3).getTerrainType()));
                    dataWriter.writeInt(((Site)object3).getVariantIndex());
                } else {
                    dataWriter.writeBoolean(false);
                }
                Unit unit = terrainGrid.getUnitAtTile(n, n2);
                if (unit != null) {
                    dataWriter.writeBoolean(true);
                    dataWriter.writeInt(unitList.indexOf(unit));
                } else {
                    dataWriter.writeBoolean(false);
                }
                object2 = terrainGrid.getUnitsAtTile(n, n2);
                dataWriter.writeInt(((ArrayList)object2).size());
                int n13 = 0;
                while (n13 < ((ArrayList)object2).size()) {
                    dataWriter.writeInt(unitList.indexOf(((ArrayList)object2).get(n13)));
                    ++n13;
                }
                ++n;
            }
            ++n2;
        }
        BirdList birdList = world.getBirds();
        dataWriter.writeInt(birdList.size());
        n = 0;
        while (n < birdList.size()) {
            object3 = (Bird)birdList.get(n);
            dataWriter.writeInt(((Bird)object3).getType().ordinal());
            dataWriter.writeFloat(((Bird)object3).getPosition().getX());
            dataWriter.writeFloat(((Bird)object3).getPosition().getY());
            dataWriter.writeFloat(((Bird)object3).getPosition().getZ());
            dataWriter.writeFloat(((Bird)object3).getAngle());
            dataWriter.writeFloat(((Bird)object3).getDistance());
            ++n;
        }
        GameMode gameMode = world.getGameMode();
        if (gameMode instanceof SupremacyMode) {
            dataWriter.writeInt(0);
            object3 = (SupremacyMode)gameMode;
            dataWriter.writeFloat(((SupremacyMode)object3).getStructureRatio());
        } else if (gameMode instanceof AnnihilationMode) {
            dataWriter.writeInt(1);
            object3 = (AnnihilationMode)gameMode;
            dataWriter.writeLong(((AnnihilationMode)object3).getHealthThreshold());
        } else if (gameMode instanceof CaptureTheFlagMode) {
            dataWriter.writeInt(2);
            object3 = (CaptureTheFlagMode)gameMode;
            int n13 = world.getUnits().indexOf(((CaptureTheFlagMode)object3).getFlag());
            if (n13 < 0) {
                OsfLog.error("Flag unit is not part of the world (saving index " + n13 + ")");
            }
            dataWriter.writeInt(n13);
            dataWriter.writeFloat(((CaptureTheFlagMode)object3).getHoldSeconds());
            dataWriter.writeFloat(((CaptureTheFlagMode)object3).getHoldStartTime());
        } else if (gameMode instanceof SurvivalMode) {
            dataWriter.writeInt(3);
            object3 = (SurvivalMode)gameMode;
            dataWriter.writeInt(((SurvivalMode)object3).getSurvivors().size());
            int n14 = 0;
            while (n14 < ((SurvivalMode)object3).getSurvivors().size()) {
                dataWriter.writeInt(playerList.indexOf(((SurvivalMode)object3).getSurvivors().get(n14)));
                ++n14;
            }
            dataWriter.writeInt(((SurvivalMode)object3).getBaseStationCount());
        } else if (gameMode instanceof EscortMode) {
            dataWriter.writeInt(4);
            object3 = (EscortMode)gameMode;
            dataWriter.writeInt(((EscortMode)object3).getEscortPlayers().size());
            int n15 = 0;
            while (n15 < ((EscortMode)object3).getEscortPlayers().size()) {
                dataWriter.writeInt(playerList.indexOf(((EscortMode)object3).getEscortPlayers().get(n15)));
                ++n15;
            }
            dataWriter.writeInt(((EscortMode)object3).getTruckCount());
            dataWriter.writeInt((int)((EscortMode)object3).getTargetArea().getX());
            dataWriter.writeInt((int)((EscortMode)object3).getTargetArea().getY());
        }
        dataWriter.writeFloat(gameMode.getTimeLimit());
        dataWriter.writeString(null);
        int n16 = 0;
        while (n16 < n12) {
            int n17 = 0;
            while (n17 < n11) {
                object2 = terrainGrid.getRoadTile(n17, n16);
                if (object2 != null) {
                    dataWriter.writeBoolean(true);
                    dataWriter.writeInt(((RoadTile)object2).getRoadMask().ordinal());
                } else {
                    dataWriter.writeBoolean(false);
                }
                ++n17;
            }
            ++n16;
        }
        n16 = 0;
        while (n16 < playerList.size()) {
            UnitGroupSet unitGroupSet = ((Player)playerList.get(n16)).getUnitGroups();
            dataWriter.writeInt(unitGroupSet.size());
            int n18 = 0;
            while (n18 < unitGroupSet.size()) {
                object = unitGroupSet.get(n18);
                UnitList unitList3 = ((UnitGroup)object).getUnits();
                dataWriter.writeInt(unitList3.size());
                int n19 = 0;
                while (n19 < unitList3.size()) {
                    dataWriter.writeInt(unitList.indexOf(unitList3.get(n19)));
                    ++n19;
                }
                ++n18;
            }
            ++n16;
        }
        n16 = 0;
        while (n16 < unitList.size()) {
            Unit unit = (Unit)unitList.get(n16);
            dataWriter.writeBoolean(unit.ae());
            UnitPosition unitPosition = unit.af();
            dataWriter.writeFloat(unitPosition.getX());
            dataWriter.writeFloat(unitPosition.getY());
            if (unitPosition.getUnit() != null) {
                dataWriter.writeBoolean(true);
                dataWriter.writeInt(unitList.indexOf(unitPosition.getUnit()));
            } else {
                dataWriter.writeBoolean(false);
            }
            dataWriter.writeInt(unit.getRallySpreadIndex());
            ++n16;
        }
        n16 = 0;
        while (n16 < playerList.size()) {
            dataWriter.writeInt(((Player)playerList.get(n16)).getId());
            ++n16;
        }
        n16 = 0;
        while (n16 < unitList.size()) {
            dataWriter.writeInt(((Unit)unitList.get(n16)).getId());
            ++n16;
        }
        dataWriter.writeInt(unitList.getNextId());
        n16 = 0;
        while (n16 < unitList.size()) {
            Unit unit = (Unit)unitList.get(n16);
            dataWriter.writeInt(unit.getKillCount());
            dataWriter.writeInt(unit.getRank());
            ++n16;
        }
        n16 = 0;
        while (n16 < n12) {
            int n20 = 0;
            while (n20 < n11) {
                RoadTile roadTile = terrainGrid.getRoadTile(n20, n16);
                if (roadTile != null) {
                    dataWriter.writeBoolean(roadTile.b());
                }
                ++n20;
            }
            ++n16;
        }
        dataWriter.writeFloat(world.getSpeedFactor());
    }
}


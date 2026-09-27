/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game;

import com.desertstormfront.app.support.ProgressCallback;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.game.MapTemplatePainter;
import com.desertstormfront.game.ScenarioType;
import com.desertstormfront.game.mode.CaptureTheFlagMode;
import com.desertstormfront.game.mode.GameMode;
import com.desertstormfront.game.mode.SupremacyMode;
import com.desertstormfront.game.mode.SurvivalMode;
import com.desertstormfront.game.model.BirdList;
import com.desertstormfront.game.model.Site;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.game.player.Controller;
import com.desertstormfront.game.player.Difficulty;
import com.desertstormfront.game.player.Faction;
import com.desertstormfront.game.player.FogOfWar;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerList;
import com.desertstormfront.game.player.Team;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.TerrainGrid;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.util.FastRandom;
import com.noblemaster.lib.util.HashUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The live game state: the terrain, the players, every unit on the map, and the game clock.
 *
 * <p>What changes it are the commands ({@code GameCommand} subclasses, issued through
 * {@code UnitCommander}) and the fixed-step simulation in {@code WorldSimulator}; screens read it
 * to render, and {@code WorldSerializer} reads it to save and load. Setup builds the terrain grid
 * first, then {@code setupPlayers} creates each player together with its fog of war.
 */
public strictfp final class World {
    private MapDefinition mapDefinition;
    private String name;
    private String description;
    private FastRandom random;
    private float gameTime;
    private int turn;
    private GameMode gameMode;
    private float speedFactor;
    private TerrainGrid terrainGrid;
    private PlayerList players;
    private UnitList units;
    private BirdList birds;
    private final float[] wind = new float[2];
    private final UnitList scratchUnits = new UnitList(32);

    public static final World create(MapDefinition mapDefinition, String string, ScenarioType scenarioType, int i3, int i4, int i5) {
        int n;
        int n2;
        Object object;
        Serializable serializable;
        UnitList unitList;
        String[] stringArray;
        int i18;
        int n3;
        Object object2;
        Object object3;
        Object object4;
        if (i3 < 32 || i4 < 32) {
            throw new RuntimeException("Map is too small.");
        }
        World world = new World();
        world.setMapDefinition(mapDefinition);
        world.setName(string);
        world.setDescription((String)null);
        world.setRandom(new FastRandom());
        world.setUnits(new UnitList(2048));
        world.setBirds(new BirdList(16));
        PlayerList playerList = new PlayerList();
        world.setPlayers(playerList);
        int n4 = 0;
        while (n4 < i5) {
            object4 = (Faction)mapDefinition.getFactions().get(n4);
            Team team = null;
            object3 = new UnitTypeList((Collection)mapDefinition.getUnitTypes());
            FogOfWar fogOfWar = new FogOfWar(i3, i4);
            Player player = Player.create(playerList.size(), (Faction)object4, team, 5000L, 100L, false, true, (UnitTypeList)object3, fogOfWar);
            playerList.add(player);
            ++n4;
        }
        switch (scenarioType) {
            case Survival: {
                object4 = SurvivalMode.create();
                ((SurvivalMode)object4).setSurvivors(new PlayerList());
                int n5 = 0;
                while (n5 < playerList.size()) {
                    object3 = (Player)playerList.get(n5);
                    if (n5 % 4 == 0) {
                        ((Player)object3).setTeam(Team.TeamA);
                        ((SurvivalMode)object4).getSurvivors().add(object3);
                    } else {
                        ((Player)object3).setTeam(Team.TeamB);
                        ((Player)object3).b((n5 - n5 / 4) * 67);
                    }
                    ++n5;
                }
                object2 = object4;
                break;
            }
            case Supremacy: {
                object2 = object4 = SupremacyMode.create();
                int n6 = 0;
                while (n6 < playerList.size()) {
                    object3 = (Player)playerList.get(n6);
                    ((Player)object3).setExplorationEnabled(true);
                    ++n6;
                }
                break;
            }
            case CaptureTheFlag: {
                object2 = object4 = CaptureTheFlagMode.create();
                break;
            }
            default: {
                OsfLog.error("Generator not implemented: " + (Object)((Object)scenarioType));
                object2 = SupremacyMode.create();
            }
        }
        world.setGameMode((GameMode)object2);
        world.setSpeedFactor(UserConfig.getWorldSpeedFactor());
        object4 = new TerrainGrid(i3, i4);
        world.setTerrainGrid((TerrainGrid)object4);
        int n7 = 8;
        int n8 = i3 / n7;
        int n9 = i4 / n7;
        int n10 = n8 / 2;
        int i14 = n9 / 2;
        boolean[][] blArray = new boolean[n9][n8];
        int n11 = 0;
        while (n11 < n9) {
            n3 = 0;
            while (n3 < n8) {
                blArray[n11][n3] = false;
                ++n3;
            }
            ++n11;
        }
        MapTemplatePainter mapTemplatePainter = new MapTemplatePainter(world);
        if (object2 instanceof CaptureTheFlagMode) {
            n3 = n8 / 2 - 1 + (world.getRandom().nextBoolean() ? 1 : 0);
            i18 = n9 / 2 - 1 + (world.getRandom().nextBoolean() ? 1 : 0);
            blArray[i18][n3] = true;
            stringArray = mapDefinition.getCaptureTheFlagTemplates()[world.getRandom().nextInt(mapDefinition.getCaptureTheFlagTemplates().length)];
            unitList = MapTemplatePainter.paintTemplate(mapTemplatePainter, n3 * n7, i18 * n7, stringArray);
            serializable = (CaptureTheFlagMode)object2;
            int n12 = 0;
            while (n12 < unitList.size()) {
                object = (Unit)unitList.get(n12);
                if (((Unit)object).getUnitType().isImmobile()) {
                    ((CaptureTheFlagMode)serializable).setFlag((Unit)object);
                    break;
                }
                ++n12;
            }
        }
        if (object2 instanceof SurvivalMode) {
            Object object5;
            Object object6;
            n3 = n8 / 2 - 1;
            i18 = n9 / 2 - 1;
            blArray[i18][n3] = true;
            blArray[i18][n3 + 1] = true;
            blArray[i18 + 1][n3] = true;
            blArray[i18 + 1][n3 + 1] = true;
            stringArray = mapDefinition.getSurvivalTemplates()[world.getRandom().nextInt(mapDefinition.getSurvivalTemplates().length)];
            unitList = MapTemplatePainter.paintTemplate(mapTemplatePainter, n3 * n7, i18 * n7, stringArray);
            serializable = ((SurvivalMode)object2).getSurvivors();
            UnitList unitList2 = unitList.getUnitsOfType(mapDefinition.getUnitTypeSlots().getHumvee().getProducer());
            object = new UnitList();
            n2 = 0;
            while (n2 < ((ArrayList)serializable).size()) {
                object6 = (Player)((ArrayList)serializable).get(n2);
                object5 = (Unit)unitList2.remove(world.getRandom().nextInt(unitList2.size()));
                ((Unit)object5).setOwner((Player)object6);
                ((Unit)object5).setHealth(((Unit)object5).getUnitType().getHealthScale());
                UnitList unitList3 = ((Unit)object5).getSubUnits();
                int n13 = 0;
                while (n13 < unitList3.size()) {
                    ((Unit)unitList3.get(n13)).setOwner((Player)object6);
                    ++n13;
                }
                ((ArrayList)object).add(object5);
                ++n2;
            }
            n2 = 0;
            while (n2 < unitList.size()) {
                object6 = (Unit)unitList.get(n2);
                if (((Unit)object6).getOwner() == null) {
                    object5 = (Player)((ArrayList)serializable).get(0);
                    float f = ((Unit)object6).getPosition().distanceSquaredTo(((Unit)((ArrayList)object).get(0)).getPosition());
                    int n14 = 1;
                    while (n14 < ((ArrayList)serializable).size()) {
                        float f2 = ((Unit)object6).getPosition().distanceSquaredTo(((Unit)((ArrayList)object).get(n14)).getPosition());
                        if (f2 < f) {
                            object5 = (Player)((ArrayList)serializable).get(n14);
                            f = f2;
                        }
                        ++n14;
                    }
                    ((Unit)object6).setOwner((Player)object5);
                    ((Unit)object6).setHealth(((Unit)object6).getUnitType().getHealthScale());
                    UnitList unitList4 = ((Unit)object6).getSubUnits();
                    n = 0;
                    while (n < unitList4.size()) {
                        ((Unit)unitList4.get(n)).setOwner((Player)object5);
                        ++n;
                    }
                }
                ++n2;
            }
        }
        PlayerList playerList2 = new PlayerList((Collection)playerList);
        i18 = world.getRandom().nextBoolean() ? 1 : 0;
        boolean bl = world.getRandom().nextBoolean();
        int n15 = 0;
        int n16 = 0;
        while (n16 < 2) {
            int n17 = 0;
            while (n17 < 2) {
                Object object7;
                int i35;
                UnitList unitList5;
                String[] stringArray2;
                int i31;
                int n18;
                int n19 = (i18 != 0 ? 1 - n17 : n17) == 0 ? 0 : n10;
                n2 = n19 == 0 ? n10 - 1 : n8 - 1;
                int n20 = (bl ? 1 - n16 : n16) == 0 ? 0 : i14;
                int n21 = n20 == 0 ? i14 - 1 : n9 - 1;
                int n22 = playerList.size() / 4 + (n15 < playerList.size() % 4 ? 1 : 0);
                PlayerList playerList3 = new PlayerList();
                n = 0;
                while (n < n22) {
                    Player player = (Player)playerList2.remove(world.getRandom().nextInt(playerList2.size()));
                    if (!(object2 instanceof SurvivalMode) || !((SurvivalMode)object2).getSurvivors().contains(player)) {
                        playerList3.add(player);
                    }
                    ++n;
                }
                if (!(object2 instanceof SurvivalMode)) {
                    n = 0;
                    n18 = n20;
                    while (n18 < n21) {
                        i31 = n19;
                        while (i31 < n2) {
                            if ((i31 == 0 && n18 == 0 || i31 == 0 && n18 == n21 - 1 || i31 == n2 - 1 && n18 == 0 || i31 == n2 - 1 && n18 == n21 - 1) && n < playerList3.size() && !blArray[n18][i31] && !blArray[n18][i31 + 1] && !blArray[n18 + 1][i31] && !blArray[n18 + 1][i31 + 1]) {
                                blArray[n18][i31] = true;
                                blArray[n18][i31 + 1] = true;
                                blArray[n18 + 1][i31] = true;
                                blArray[n18 + 1][i31 + 1] = true;
                                stringArray2 = mapDefinition.getSurvivalTemplates()[world.getRandom().nextInt(mapDefinition.getSurvivalTemplates().length)];
                                unitList5 = MapTemplatePainter.paintTemplate(mapTemplatePainter, i31 * n7, n18 * n7, stringArray2);
                                int n23 = playerList3.size() - n;
                                if (n23 > 3) {
                                    n23 = 3;
                                }
                                i35 = 0;
                                while (i35 < n23) {
                                    object7 = (Player)playerList3.get(n % playerList3.size());
                                    int n24 = 0;
                                    while (n24 < unitList5.size()) {
                                        Unit unit = (Unit)unitList5.get(n24);
                                        if (unit.getOwner() == null && unit.getUnitType() == mapDefinition.getUnitTypeSlots().getHumvee().getProducer()) {
                                            unit.setOwner((Player)object7);
                                            unit.setHealth(unit.getUnitType().getHealthScale());
                                            UnitList unitList6 = unit.getSubUnits();
                                            int i40 = 0;
                                            while (i40 < unitList6.size()) {
                                                ((Unit)unitList6.get(i40)).setOwner((Player)object7);
                                                ++i40;
                                            }
                                            break;
                                        }
                                        ++n24;
                                    }
                                    ++n;
                                    ++i35;
                                }
                            }
                            ++i31;
                        }
                        ++n18;
                    }
                    n18 = n20;
                    while (n18 <= n21) {
                        i31 = n19;
                        while (i31 <= n2) {
                            if (!(i31 != 0 && n18 != 0 && i31 != n2 && n18 != n21 || n >= playerList3.size() || blArray[n18][i31])) {
                                blArray[n18][i31] = true;
                                stringArray2 = mapDefinition.getPlayerBaseTemplates()[world.getRandom().nextInt(mapDefinition.getPlayerBaseTemplates().length)];
                                unitList5 = MapTemplatePainter.paintTemplate(mapTemplatePainter, i31 * n7, n18 * n7, stringArray2);
                                Player player = (Player)playerList3.get(n % playerList3.size());
                                i35 = 0;
                                while (i35 < unitList5.size()) {
                                    object7 = (Unit)unitList5.get(i35);
                                    if (((Unit)object7).getUnitType() == mapDefinition.getUnitTypeSlots().getHumvee().getProducer()) {
                                        ((Unit)object7).setOwner(player);
                                        ((Unit)object7).setHealth(((Unit)object7).getUnitType().getHealthScale());
                                        UnitList unitList7 = ((Unit)object7).getSubUnits();
                                        int n25 = 0;
                                        while (n25 < unitList7.size()) {
                                            ((Unit)unitList7.get(n25)).setOwner(player);
                                            ++n25;
                                        }
                                        break;
                                    }
                                    ++i35;
                                }
                                ++n;
                            }
                            ++i31;
                        }
                        ++n18;
                    }
                } else {
                    n = 0;
                    n18 = n20;
                    while (n18 <= n21) {
                        i31 = n19;
                        while (i31 <= n2) {
                            if (!(i31 != 0 && n18 != 0 && i31 != n2 && n18 != n21 || n >= playerList3.size() || blArray[n18][i31])) {
                                blArray[n18][i31] = true;
                                stringArray2 = mapDefinition.getPlayerStartTemplates()[world.getRandom().nextInt(mapDefinition.getPlayerStartTemplates().length)];
                                unitList5 = MapTemplatePainter.paintTemplate(mapTemplatePainter, i31 * n7, n18 * n7, stringArray2);
                                Player player = (Player)playerList3.get(n % playerList3.size());
                                i35 = 0;
                                while (i35 < unitList5.size()) {
                                    object7 = (Unit)unitList5.get(i35);
                                    ((Unit)object7).setOwner(player);
                                    ((Unit)object7).setHealth(((Unit)object7).getUnitType().getHealthScale());
                                    UnitList unitList8 = ((Unit)object7).getSubUnits();
                                    int n26 = 0;
                                    while (n26 < unitList8.size()) {
                                        ((Unit)unitList8.get(n26)).setOwner(player);
                                        ++n26;
                                    }
                                    ++i35;
                                }
                                ++n;
                            }
                            ++i31;
                        }
                        ++n18;
                    }
                }
                ++n15;
                ++n17;
            }
            ++n16;
        }
        n16 = 0;
        while (n16 < n9) {
            int n27 = 0;
            while (n27 < n8) {
                if (!blArray[n16][n27]) {
                    blArray[n16][n27] = true;
                    boolean bl2 = object2 instanceof SurvivalMode ? world.getRandom().nextInt(28) == 0 : (object2 instanceof CaptureTheFlagMode ? world.getRandom().nextInt(12) == 0 : world.getRandom().nextInt(5) == 0);
                    String[] stringArray3 = bl2 ? mapDefinition.getRarePropTemplates()[world.getRandom().nextInt(mapDefinition.getRarePropTemplates().length)] : mapDefinition.getCommonPropTemplates()[world.getRandom().nextInt(mapDefinition.getCommonPropTemplates().length)];
                    MapTemplatePainter.paintTemplate(mapTemplatePainter, n27 * n7, n16 * n7, stringArray3);
                }
                ++n27;
            }
            ++n16;
        }
        if (mapDefinition.getId().equals("tsf")) {
            n16 = 0;
            while (n16 < i4) {
                int n28 = 0;
                while (n28 < i3) {
                    Site site = ((TerrainGrid)object4).getSite(n28, n16);
                    if (site != null && site.getTerrainType().getCode() == 'X') {
                        ((TerrainGrid)object4).setSite(n28, n16, site.getTerrainType(), (HashUtils.hash(n28, n16) & Integer.MAX_VALUE) % 8 + (((TerrainGrid)object4).isCoastTile(n28, n16) ? 8 : 0));
                    }
                    ++n28;
                }
                ++n16;
            }
        }
        return world;
    }

    public final void activateHumanPlayer() {
        this.activateHumanPlayer((Faction)null);
    }

    public final void activateHumanPlayer(Faction faction) {
        block6: {
            Player player;
            block7: {
                if (faction != null) break block7;
                int i2 = 0;
                while (i2 < this.players.size()) {
                    Player player2 = (Player)this.players.get(i2);
                    if (player2.getController() != Controller.Human) {
                        player2.setController(Controller.Human);
                        break block6;
                    }
                    ++i2;
                }
                break block6;
            }
            boolean i2 = false;
            int n = 0;
            while (n < this.players.size()) {
                player = (Player)this.players.get(n);
                if (player.getFaction() == faction) {
                    player.setController(Controller.Human);
                    i2 = true;
                    break;
                }
                ++n;
            }
            if (i2) break block6;
            n = 0;
            while (n < this.players.size()) {
                player = (Player)this.players.get(n);
                if (player.getController() != Controller.Human) {
                    player.setController(Controller.Human);
                    player.setFaction(faction);
                    break;
                }
                ++n;
            }
        }
    }

    public final void setupDefaultPlayers(Difficulty difficulty) {
        this.setupPlayers(true, 0, true, 0, true, false, true, 0L, true, 0L, true, false, true, false, true, null, true, 0.0f, difficulty);
    }

    public final void setupPlayers(boolean bl, int i2, boolean bl2, int i4, boolean bl3, boolean bl4, boolean bl5, long l8, boolean bl6, long l11, boolean bl7, boolean bl8, boolean bl9, boolean bl10, boolean bl11, UnitTypeList unitTypeList, boolean bl12, float f20, Difficulty difficulty) {
        Difficulty difficulty2;
        int n;
        if (!bl && !(this.gameMode instanceof SurvivalMode)) {
            Object object;
            int n2;
            int n3;
            n = 0;
            if (((Player)this.players.get(0)).getTeam() != null) {
                n3 = this.players.size() - i2;
                while (n3 > 0) {
                    n2 = 0;
                    while (n2 < Team.values().length) {
                        if (n3 > 0) {
                            object = Team.values()[n2];
                            int n4 = 0;
                            while (n4 < this.players.size()) {
                                Player player = (Player)this.players.get(n4);
                                if (player.getTeam() == object) {
                                    this.removePlayer(player);
                                    if (player.getController() == Controller.Human) {
                                        ++n;
                                    }
                                    --n3;
                                    break;
                                }
                                ++n4;
                            }
                        }
                        ++n2;
                    }
                }
            } else {
                n3 = this.players.size() - i2;
                n2 = 0;
                while (n2 < n3) {
                    object = (Player)this.players.get(this.players.size() - 1);
                    this.removePlayer((Player)object);
                    if (((Player)object).getController() == Controller.Human) {
                        ++n;
                    }
                    ++n2;
                }
            }
            n3 = 0;
            while (n3 < this.players.size()) {
                Player player = (Player)this.players.get(n3);
                if (n > 0 && player.getController() != Controller.Human) {
                    player.setController(Controller.Human);
                    --n;
                }
                ++n3;
            }
        }
        if (!bl2 && !(this.gameMode instanceof SurvivalMode)) {
            n = 0;
            while (n < this.players.size()) {
                Player player = (Player)this.players.get(n);
                if (i4 == 0) {
                    player.setTeam((Team)null);
                } else {
                    player.setTeam(Team.values()[Team.TeamA.ordinal() + n % i4]);
                }
                ++n;
            }
        }
        if (!bl3) {
            if (bl4) {
                UnitType unitType = this.mapDefinition.getUnitTypeSlots().getGeneral();
                if (unitType != null) {
                    int n5 = 0;
                    while (n5 < this.players.size()) {
                        Player player = (Player)this.players.get(n5);
                        boolean bl13 = false;
                        Unit unit = null;
                        int n6 = 0;
                        while (n6 < this.units.size()) {
                            Unit unit2 = (Unit)this.units.get(n6);
                            if (unit2.getOwner() == player) {
                                if (unit2.getUnitType() == this.mapDefinition.getUnitTypeSlots().getHumvee().getProducer()) {
                                    unit = unit2;
                                }
                                if (unit2.getUnitType().isGeneral()) {
                                    bl13 = true;
                                    break;
                                }
                            }
                            ++n6;
                        }
                        if (!bl13 && unit != null) {
                            this.createHostedUnit(unitType, unit, player);
                        }
                        ++n5;
                    }
                }
            } else {
                n = 0;
                while (n < this.units.size()) {
                    Unit unit = (Unit)this.units.get(n);
                    if (unit.getUnitType().isGeneral()) {
                        this.units.remove(n);
                        this.terrainGrid.removeUnitFromTile(unit);
                        continue;
                    }
                    ++n;
                }
            }
        }
        int n7 = 0;
        while (n7 < this.players.size()) {
            Player player = (Player)this.players.get(n7);
            if (!bl5) {
                player.setResources(l8);
            }
            if (!bl6) {
                player.setIncomePerBuilding(l11);
            }
            if (!bl7) {
                player.setExplorationEnabled(bl8);
            }
            if (!bl9) {
                player.setFogEnabled(bl10);
            }
            if (!bl11) {
                player.setAvailableUnitTypes(unitTypeList);
            }
            ++n7;
        }
        if (!bl12) {
            if (this.gameMode instanceof CaptureTheFlagMode) {
                ((CaptureTheFlagMode)this.gameMode).setHoldSeconds(f20);
            } else if (this.gameMode instanceof SurvivalMode) {
                if (f20 > 0.0f) {
                    this.gameMode.setTimeLimit(f20);
                }
            } else {
                this.gameMode.setTimeLimit(f20);
            }
        }
        switch (difficulty) {
            case Casual: {
                difficulty2 = Difficulty.Hard;
                break;
            }
            case Normal: {
                difficulty2 = Difficulty.Normal;
                break;
            }
            case Hard: {
                difficulty2 = Difficulty.Casual;
                break;
            }
            case Extreme: {
                difficulty2 = Difficulty.Casual;
                break;
            }
            default: {
                difficulty2 = Difficulty.Normal;
                OsfLog.error("Opposite difficulty not defined for: " + (Object)((Object)difficulty));
            }
        }
        int n8 = 0;
        while (n8 < this.players.size()) {
            Player player = (Player)this.players.get(n8);
            if (player.getController() != Controller.Human) {
                boolean bl14 = false;
                if (player.getTeam() != null) {
                    int n9 = 0;
                    while (n9 < this.players.size()) {
                        if (((Player)this.players.get(n9)).getController() == Controller.Human && ((Player)this.players.get(n9)).getTeam() == player.getTeam()) {
                            bl14 = true;
                            break;
                        }
                        ++n9;
                    }
                }
                if (!bl14) {
                    player.setController(Controller.fromDifficulty(difficulty));
                } else {
                    player.setController(Controller.fromDifficulty(difficulty2));
                }
            }
            ++n8;
        }
        n8 = 0;
        while (n8 < this.players.size()) {
            Player player = (Player)this.players.get(n8);
            if (player.getController() == Controller.Human) {
                if (difficulty == Difficulty.Casual) {
                    player.setResources((long)((float)player.getResources() * 2.0f));
                    player.setIncomePerBuilding((long)((float)player.getIncomePerBuilding() * 2.0f));
                }
            } else {
                switch (player.getController().getDifficulty()) {
                    case Casual: {
                        player.setResources((long)((float)player.getResources() * 0.5f));
                        player.setIncomePerBuilding((long)((float)player.getIncomePerBuilding() * 0.5f));
                        break;
                    }
                    case Normal: {
                        break;
                    }
                    case Hard: {
                        player.setResources((long)((float)player.getResources() * 1.5f));
                        player.setIncomePerBuilding((long)((float)player.getIncomePerBuilding() * 1.5f));
                        break;
                    }
                    case Extreme: {
                        player.setResources((long)((float)player.getResources() * 2.0f));
                        player.setIncomePerBuilding((long)((float)player.getIncomePerBuilding() * 2.0f));
                        break;
                    }
                    default: {
                        difficulty2 = Difficulty.Normal;
                        OsfLog.error("Difficulty not defined for: " + (Object)((Object)difficulty));
                    }
                }
            }
            ++n8;
        }
        n8 = 0;
        while (n8 < this.players.size()) {
            Player player = (Player)this.players.get(n8);
            FogOfWar fogOfWar = player.getFogOfWar();
            fogOfWar.setAllExplored(!player.isExplorationEnabled());
            fogOfWar.setAllVisible(!player.isFogEnabled());
            this.updateFogOfWar(player);
            ++n8;
        }
    }

    public final void buildTerrain(ProgressCallback progressCallback) {
        this.terrainGrid.buildTerrain(progressCallback);
    }

    public final void shareTerrainFrom(World world) {
        this.terrainGrid.shareFrom(world.getTerrainGrid());
    }

    public final void updateFogOfWar(Player player) {
        FogOfWar fogOfWar = player.getFogOfWar();
        if (player.isFogEnabled()) {
            fogOfWar.setAllVisible(false);
        }
        if (player.isExplorationEnabled() || player.isFogEnabled()) {
            Team team = player.getTeam();
            int i4 = 0;
            while (i4 < this.units.size()) {
                Unit unit = (Unit)this.units.get(i4);
                Player player2 = unit.getOwner();
                if (player2 == player || team != null && player2 != null && team == player2.getTeam()) {
                    int i7 = fogOfWar.getWidth();
                    int i8 = fogOfWar.getHeight();
                    UnitPosition unitPosition = unit.getPosition();
                    int i10 = (int)unitPosition.getX();
                    int i11 = (int)unitPosition.getY();
                    int i12 = unit.getUnitType().getVisionRadiusInTiles();
                    int i13 = i11 - i12;
                    while (i13 <= i11 + i12) {
                        int i14 = i10 - i12;
                        while (i14 <= i10 + i12) {
                            int i16;
                            int i15;
                            if (i14 >= 0 && i13 >= 0 && i14 < i7 && i13 < i8 && ((i15 = Math.abs(i14 - i10)) != (i16 = Math.abs(i13 - i11)) || i15 != i12)) {
                                fogOfWar.setExplored(i14, i13, true);
                                fogOfWar.setVisible(i14, i13, true);
                            }
                            ++i14;
                        }
                        ++i13;
                    }
                }
                ++i4;
            }
        }
    }

    public MapDefinition getMapDefinition() {
        return this.mapDefinition;
    }

    public void setMapDefinition(MapDefinition mapDefinition) {
        this.mapDefinition = mapDefinition;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String string) {
        this.name = string;
    }

    public final String getDescription() {
        return this.description;
    }

    public final void setDescription(String string) {
        this.description = string;
    }

    public FastRandom getRandom() {
        return this.random;
    }

    public void setRandom(FastRandom fastRandom) {
        this.random = fastRandom;
    }

    public final GameMode getGameMode() {
        return this.gameMode;
    }

    public final void setGameMode(GameMode gameMode) {
        if (gameMode != null) {
            gameMode.setWorld(this);
        }
        this.gameMode = gameMode;
    }

    public float getSpeedFactor() {
        return this.speedFactor;
    }

    public void setSpeedFactor(float f1) {
        this.speedFactor = f1;
    }

    public final float getGameTime() {
        return this.gameTime;
    }

    public final void setGameTime(float f1) {
        this.gameTime = f1;
    }

    public final int getTurn() {
        return this.turn;
    }

    public final void setTurn(int i1) {
        this.turn = i1;
    }

    public final TerrainGrid getTerrainGrid() {
        return this.terrainGrid;
    }

    public final void setTerrainGrid(TerrainGrid terrainGrid) {
        this.terrainGrid = terrainGrid;
    }

    public final PlayerList getPlayers() {
        return this.players;
    }

    public final void setPlayers(PlayerList playerList) {
        this.players = playerList;
    }

    public final void removePlayer(Player player) {
        int i2 = 0;
        while (i2 < this.units.size()) {
            Unit unit = (Unit)this.units.get(i2);
            if (unit.getOwner() == player) {
                if (unit.isImmobile()) {
                    unit.setOwner((Player)null);
                    unit.setHealth(0);
                    UnitList unitList = unit.getSubUnits();
                    int i5 = 0;
                    while (i5 < unitList.size()) {
                        ((Unit)unitList.get(i5)).setHealth(0);
                        ++i5;
                    }
                    unitList.clear();
                    ++i2;
                    continue;
                }
                this.units.remove(i2);
                this.terrainGrid.removeUnitFromTile(unit);
                continue;
            }
            ++i2;
        }
        this.players.remove(player);
    }

    public final UnitList getUnits() {
        return this.units;
    }

    public final void setUnits(UnitList unitList) {
        this.units = unitList;
    }

    public final void addHostedUnit(UnitType unitType, Unit unit, Player player) {
        Unit unit2 = this.createHostedUnit(unitType, unit, player);
        unit2.setCount(unitType.getHealthInt());
    }

    public final void removeUnit(Unit unit) {
        unit.setPosition(0.0f, 0.0f);
        this.units.remove(unit);
    }

    public final Unit createHostedUnit(UnitType unitType, Unit unit, Player player) {
        Unit unit2 = this.units.addUnit(unitType, player);
        unit2.dockInto(unit);
        if (unitType.isFlying()) {
            unit2.setProgress(unitType.getAltitude());
        }
        return unit2;
    }

    public final Unit spawnUnit(UnitType unitType, float f2, float f3, Player player) {
        Unit unit = this.units.addUnit(unitType, player);
        unit.setPosition(f2, f3);
        if (unitType.isFlying()) {
            unit.setProgress(unitType.getAltitude());
        }
        if (unitType.isImmobile()) {
            this.terrainGrid.setUnitAtPosition((Vec2)unit.getPosition(), unit);
            int i6 = (int)f2;
            int i7 = (int)f3;
            if (unitType == this.mapDefinition.getUnitTypeSlots().getShipyard()) {
                if (this.terrainGrid.isInsideGrid((float)(i6 + 1), (float)i7) && !this.terrainGrid.isCellBlocked((float)(i6 + 1), (float)i7) && this.terrainGrid.isAllWaterTile(i6 + 1, i7) && this.terrainGrid.isAllLandTile(i6 - 1, i7)) {
                    unit.setAngle(0.0f);
                } else if (this.terrainGrid.isInsideGrid((float)i6, (float)(i7 + 1)) && !this.terrainGrid.isCellBlocked((float)i6, (float)(i7 + 1)) && this.terrainGrid.isAllWaterTile(i6, i7 + 1) && this.terrainGrid.isAllLandTile(i6, i7 - 1)) {
                    unit.setAngle(1.5707964f);
                } else if (this.terrainGrid.isInsideGrid((float)(i6 - 1), (float)i7) && !this.terrainGrid.isCellBlocked((float)(i6 - 1), (float)i7) && this.terrainGrid.isAllWaterTile(i6 - 1, i7) && this.terrainGrid.isAllLandTile(i6 + 1, i7)) {
                    unit.setAngle((float)Math.PI);
                } else if (this.terrainGrid.isInsideGrid((float)i6, (float)(i7 - 1)) && !this.terrainGrid.isCellBlocked((float)i6, (float)(i7 - 1)) && this.terrainGrid.isAllWaterTile(i6, i7 - 1) && this.terrainGrid.isAllLandTile(i6, i7 + 1)) {
                    unit.setAngle(4.712389f);
                }
            } else if (this.terrainGrid.isInsideGrid((float)(i6 + 1), (float)i7) && !this.terrainGrid.isCellBlocked((float)(i6 + 1), (float)i7) && !this.terrainGrid.isAllWaterTile(i6 + 1, i7)) {
                unit.setAngle(0.0f);
            } else if (this.terrainGrid.isInsideGrid((float)i6, (float)(i7 + 1)) && !this.terrainGrid.isCellBlocked((float)i6, (float)(i7 + 1)) && !this.terrainGrid.isAllWaterTile(i6, i7 + 1)) {
                unit.setAngle(1.5707964f);
            } else if (this.terrainGrid.isInsideGrid((float)(i6 - 1), (float)i7) && !this.terrainGrid.isCellBlocked((float)(i6 - 1), (float)i7) && !this.terrainGrid.isAllWaterTile(i6 - 1, i7)) {
                unit.setAngle((float)Math.PI);
            } else if (this.terrainGrid.isInsideGrid((float)i6, (float)(i7 - 1)) && !this.terrainGrid.isCellBlocked((float)i6, (float)(i7 - 1)) && !this.terrainGrid.isAllWaterTile(i6, i7 - 1)) {
                unit.setAngle(4.712389f);
            }
        } else {
            this.terrainGrid.addUnitToTile(unit);
        }
        return unit;
    }

    public final BirdList getBirds() {
        return this.birds;
    }

    public final void setBirds(BirdList birdList) {
        this.birds = birdList;
    }

    public float[] getWind() {
        return this.wind;
    }

    public UnitList getScratchUnits() {
        return this.scratchUnits;
    }
}


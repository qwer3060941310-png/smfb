/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.map;

import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.model.AmmoType;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Layer;
import com.desertstormfront.game.model.TerrainType;
import com.desertstormfront.game.model.TerrainTypeList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.game.model.UnitTypeSlots;
import com.desertstormfront.game.player.Faction;
import com.desertstormfront.game.player.FactionList;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.patch.PatchHelper;
import java.util.Arrays;

public strictfp class DsfRuleset
extends MapDefinition {
    private final FactionList b;
    private final FactionList c;
    private final TerrainTypeList terrainTypes;
    private UnitType general;
    private UnitType humvee;
    private UnitType battleTank;
    private UnitType artillery;
    private UnitType missileTank;
    private UnitType fourByFour;
    private UnitType mechanic;
    private UnitType truck;
    private UnitType fighterPlane;
    private UnitType helicopter;
    private UnitType gunboat;
    private UnitType submarine;
    private UnitType cruiser;
    private UnitType chinnook;
    private UnitType carrier;
    private UnitType transport;
    private UnitType hovercraft;
    private UnitType baseStation;
    private UnitType shipyard;
    private UnitType airfield;
    private UnitType oilField;
    private final UnitTypeList unitTypes;
    private final UnitTypeSlots unitTypeSlots;
    public int[][] costMatrix;
    private final UnitTypeList[] cargoUnits;
    private final String[][] playerStartTemplates;
    private final String[][] captureTheFlagTemplates;
    private final String[][] survivalTemplates;
    private final String[][] playerBaseTemplates;
    private final String[][] rarePropTemplates;
    private final String[][] commonPropTemplates;
    private UnitType infantry = PatchHelper.makeInfantry();

    DsfRuleset() {
        this.b = new FactionList(Arrays.asList(new Faction(0, "USA[i18n]: USA", "USA"), new Faction(1, "Afghanistan[i18n]: Afghanistan", "AFGHANISTAN"), new Faction(2, "France[i18n]: France", "FRANCE"), new Faction(3, "England[i18n]: England", "ENGLAND"), new Faction(4, "Italy[i18n]: Italy", "ITALY"), new Faction(5, "SaudiArabia[i18n]: Saudi Arabia", "SAUDI_ARABIA"), new Faction(6, "Iraq[i18n]: Iraq", "IRAQ"), new Faction(7, "Iran[i18n]: Iran", "IRAN"), new Faction(8, "Egypt[i18n]: Egypt", "EGYPT"), new Faction(9, "Rebels[i18n]: Rebels", "REBELS")));
        this.c = new FactionList(Arrays.asList(new Faction(0, "BlueLeader[i18n]: Blue Leader", "USA"), new Faction(1, "OrangeBlack[i18n]: Orange/Black", "AFGHANISTAN"), new Faction(2, "BlueGold[i18n]: Blue/Gold", "FRANCE"), new Faction(3, "BlueWhite[i18n]: Blue/White", "ENGLAND"), new Faction(4, "OrangeWhite[i18n]: Orange/White", "ITALY"), new Faction(5, "RedGold[i18n]: Red/Gold", "SAUDI_ARABIA"), new Faction(6, "RedBlack[i18n]: Red/Black", "IRAQ"), new Faction(7, "RedLeader[i18n]: Red Leader", "IRAN"), new Faction(8, "OrangeWhiteStriped[i18n]: Orange/White Striped", "EGYPT"), new Faction(9, "OrangeBlackStriped[i18n]: Orange/Black Striped", "REBELS")));
        this.terrainTypes = new TerrainTypeList(Arrays.asList(new TerrainType(0, "Flora[i18n]: Flora", "FLORA", 'X', 7), new TerrainType(1, "BeachFlora[i18n]: Beach Flora", "FLORA_BEACH", 'x', 7), new TerrainType(2, "Farmland[i18n]: Farmland", "FARMLAND", 'F', 2), new TerrainType(3, "Buildings[i18n]: Buildings", "BUILDINGS", 'B', 7), new TerrainType(4, "DestroyedBuildings[i18n]: Destroyed Buildings", "BUILDINGS_DESTROYED", 'Y', 4), new TerrainType(5, "Mosque[i18n]: Mosque", "MOSQUE", 'R', 2), new TerrainType(6, "Bridge[i18n]: Bridge", "BRIDGE", 'G', 2), new TerrainType(7, "Cliff[i18n]: Cliff", "CLIFF", 'C', 1), new TerrainType(8, "Oasis[i18n]: Oasis", "OASIS", 'S', 1), new TerrainType(9, "Warehouse[i18n]: Warehouse", "WAREHOUSE", 'W', 6), new TerrainType(10, "Mountain[i18n]: Mountain", "MOUNTAIN", 'M', 7), new TerrainType(11, "Ravine[i18n]: Ravine", "RAVINE", 'N', 8)));
        this.general = new UnitType(0, "General[i18n]: General", "GENERAL", 'H', 500L, false, Layer.Base, Domain.Ground, 0.34f, 0.17f, 0.34f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 0.2f, 0, 1.0f, true, false, false, false, false, 2, 2, 4.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.humvee = new UnitType(1, "Humvee[i18n]: Humvee", "HUMVEE", 'G', 100L, false, Layer.Base, Domain.Ground, 0.36f, 0.17f, 0.34f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 0.2f, 0, 1.0f, false, true, false, false, false, 1, 2, 4.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.battleTank = new UnitType(2, "BattleTank[i18n]: Battle Tank", "BATTLE_TANK", 'I', 150L, false, Layer.Base, Domain.Ground, 0.37f, 0.17f, 0.21f, 0.0f, false, true, AmmoType.Shell, 0.15f, 0.35f, 0, 1.2f, false, false, false, false, false, 1, 3, 4.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.artillery = new UnitType(3, "Artillery[i18n]: Artillery", "ARTILLERY", 'K', 200L, false, Layer.Base, Domain.Ground, 0.37f, 0.17f, 0.18f, 0.0f, false, true, AmmoType.Cannonball, 0.25f, 0.7f, 0, 2.6f, false, false, false, false, false, 2, 4, 8.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.missileTank = new UnitType(4, "MissileTank[i18n]: Missile Tank", "MISSILE_TANK", 'L', 150L, false, Layer.Base, Domain.Ground, 0.37f, 0.17f, 0.19f, 0.0f, false, true, AmmoType.Missile, 0.2f, 0.25f, 0, 1.8f, false, false, false, false, false, 3, 4, 12.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.fourByFour = new UnitType(5, "4x4[i18n]: 4x4", "4X4", '4', 80L, false, Layer.Base, Domain.Ground, 0.33f, 0.17f, 0.3f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 0.2f, 0, 1.0f, false, false, false, false, false, 1, 2, 3.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.mechanic = new UnitType(6, "Mechanic[i18n]: Mechanic", "MECHANIC", 'E', 300L, false, Layer.Base, Domain.Ground, 0.36f, 0.17f, 0.22f, 0.0f, false, false, null, 0.0f, 0.0f, 0, 0.0f, false, false, false, true, false, 1, 0, 0.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.truck = new UnitType(7, "Truck[i18n]: Truck", "TRUCK", 'F', 100L, false, Layer.Base, Domain.Ground, 0.37f, 0.17f, 0.21f, 0.0f, false, false, null, 0.0f, 0.0f, 0, 0.0f, false, false, false, false, true, 1, 0, 0.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.fighterPlane = new UnitType(8, "FighterPlane[i18n]: Fighter Plane", "FIGHTER_PLANE", 'M', 250L, false, Layer.Upper, Domain.Air, 0.2f, 1.0f, 0.88f, 26.0f, true, false, AmmoType.Bullets, 0.2f, 1.0f, 0, 1.2f, false, false, false, false, false, 3, 2, 4.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.helicopter = new UnitType(9, "Helicopter[i18n]: Helicopter", "HELICOPTER", 'N', 300L, false, Layer.Upper, Domain.Air, 0.17f, 1.0f, 0.5f, 22.0f, false, false, AmmoType.Missile, 0.2f, 1.0f, 3, 1.2f, false, false, false, false, false, 2, 2, 6.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.gunboat = new UnitType(10, "Gunboat[i18n]: Gunboat", "GUNBOAT", 'P', 200L, false, Layer.Base, Domain.Water, 0.28f, 0.11f, 0.24f, 0.0f, false, false, AmmoType.Shell, 0.2f, 0.1f, 0, 1.2f, false, false, false, false, false, 1, 2, 4.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.submarine = new UnitType(11, "Submarine[i18n]: Submarine", "SUBMARINE", 'Q', 400L, false, Layer.Under, Domain.Water, 0.21f, 0.0f, 0.2f, 0.0f, false, false, AmmoType.Torpedo, 0.2f, 0.0f, 0, 2.0f, false, false, false, false, false, 1, 2, 7.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.cruiser = new UnitType(12, "Cruiser[i18n]: Cruiser", "CRUISER", 'R', 550L, false, Layer.Base, Domain.Water, 0.45f, 0.11f, 0.19f, 0.0f, false, false, AmmoType.Missile, 0.3f, 0.2f, 0, 1.8f, false, false, false, false, false, 3, 4, 9.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
        this.chinnook = new UnitType(13, "Chinnook[i18n]: Chinnook", "CHINNOOK", 'S', 300L, false, Layer.Upper, Domain.Air, 0.28f, 1.0f, 0.22f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 1.0f, 0, 1.4f, false, false, false, false, false, 2, 2, 4.0f, new UnitTypeList(), new UnitTypeList(Arrays.asList(this.general, this.humvee, this.battleTank, this.artillery, this.missileTank, this.mechanic, this.truck, this.fourByFour)), 1, false);
        this.carrier = new UnitType(14, "Carrier[i18n]: Carrier", "CARRIER", 'T', 600L, false, Layer.Base, Domain.Water, 0.49f, 0.11f, 0.17f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 0.2f, 0, 1.4f, false, false, false, false, false, 2, 2, 4.0f, new UnitTypeList(), new UnitTypeList(Arrays.asList(this.fighterPlane, this.helicopter, this.chinnook)), 6, true);
        this.transport = new UnitType(15, "Transport[i18n]: Transport", "TRANSPORT_SHIP", 'U', 200L, false, Layer.Base, Domain.Water, 0.4f, 0.11f, 0.18f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 0.2f, 0, 1.6f, false, false, false, false, false, 1, 2, 4.0f, new UnitTypeList(), new UnitTypeList(Arrays.asList(this.general, this.humvee, this.battleTank, this.artillery, this.missileTank, this.mechanic, this.truck, this.fourByFour)), 4, false);
        this.hovercraft = new UnitType(16, "Hovercraft[i18n]: Hovercraft", "HOVERCRAFT", 'V', 2000L, false, Layer.Base, Domain.Amphibian, 0.49f, 0.15f, 0.15f, 0.0f, false, false, AmmoType.Missile, 0.2f, 0.2f, 0, 0.5f, false, false, false, false, false, 3, 4, 6.0f, new UnitTypeList(), new UnitTypeList(Arrays.asList(this.general, this.humvee, this.battleTank, this.artillery, this.missileTank, this.mechanic, this.truck)), 2, false);
        this.baseStation = new UnitType(17, "BaseStation[i18n]: Base Station", "BASE_STATION", 'A', 2000L, false, Layer.Base, Domain.Ground, 0.5f, 0.17f, 0.0f, 0.0f, false, false, null, 0.0f, 0.0f, 0, 0.0f, false, false, true, false, false, 2, 0, 0.0f, new UnitTypeList(Arrays.asList(this.humvee, this.battleTank, this.artillery, this.missileTank, this.mechanic)), new UnitTypeList(Arrays.asList(this.general, this.humvee, this.battleTank, this.artillery, this.missileTank, this.mechanic, this.truck, this.fourByFour)), 8, false);
        this.shipyard = new UnitType(18, "Shipyard[i18n]: Shipyard", "SHIPYARD", 'B', 2000L, false, Layer.Base, Domain.Water, 0.5f, 0.17f, 0.0f, 0.0f, false, false, null, 0.0f, 0.0f, 0, 0.0f, false, false, true, false, false, 2, 0, 0.0f, new UnitTypeList(Arrays.asList(this.gunboat, this.submarine, this.transport, this.cruiser, this.carrier)), new UnitTypeList(Arrays.asList(this.gunboat, this.submarine, this.transport, this.cruiser, this.carrier)), 8, false);
        this.airfield = new UnitType(19, "Airfield[i18n]: Airfield", "AIRFIELD", 'C', 2000L, false, Layer.Base, Domain.Ground, 0.5f, 0.17f, 0.0f, 0.0f, false, false, null, 0.0f, 0.0f, 0, 0.0f, false, false, true, false, false, 3, 0, 0.0f, new UnitTypeList(Arrays.asList(this.fighterPlane, this.helicopter, this.chinnook)), new UnitTypeList(Arrays.asList(this.fighterPlane, this.helicopter, this.chinnook)), 8, true);
        this.oilField = new UnitType(20, "OilField[i18n]: Oil Field", "OILFIELD", 'O', 2000L, true, Layer.Base, Domain.Ground, 0.5f, 0.17f, 0.0f, 0.0f, false, false, null, 0.0f, 0.0f, 0, 0.0f, false, false, true, false, false, 2, 0, 0.0f, new UnitTypeList(Arrays.asList(this.fourByFour)), new UnitTypeList(Arrays.asList(this.fourByFour)), 8, false);
        this.unitTypes = new UnitTypeList(Arrays.asList(this.general, this.humvee, this.battleTank, this.artillery, this.missileTank, this.fourByFour, this.mechanic, this.truck, this.fighterPlane, this.helicopter, this.gunboat, this.submarine, this.cruiser, this.chinnook, this.carrier, this.transport, this.hovercraft, this.baseStation, this.shipyard, this.airfield, this.oilField));
        this.unitTypeSlots = new UnitTypeSlots(this.general, this.humvee, this.battleTank, this.artillery, this.missileTank, this.fourByFour, this.baseStation, this.shipyard, this.airfield, this.oilField, this.transport, this.carrier, this.chinnook, this.fighterPlane, this.helicopter, this.cruiser, this.submarine, this.gunboat, this.mechanic, this.hovercraft, this.truck);
        int[][] nArrayArray = new int[21][];
        int[] nArray = new int[21];
        nArray[0] = 20;
        nArray[1] = 100;
        nArray[2] = 120;
        nArray[3] = 100;
        nArray[4] = 10;
        nArray[5] = 80;
        nArray[6] = 10;
        nArray[8] = 20;
        nArray[9] = 100;
        nArray[10] = 10;
        nArray[12] = 100;
        nArray[13] = 10;
        nArray[14] = 40;
        nArray[15] = 10;
        nArray[16] = 200;
        nArrayArray[0] = nArray;
        int[] nArray2 = new int[21];
        nArray2[0] = 20;
        nArray2[1] = 200;
        nArray2[2] = 220;
        nArray2[3] = 100;
        nArray2[4] = 10;
        nArray2[5] = 80;
        nArray2[6] = 10;
        nArray2[8] = 20;
        nArray2[9] = 250;
        nArray2[10] = 20;
        nArray2[12] = 150;
        nArray2[13] = 10;
        nArray2[14] = 40;
        nArray2[15] = 10;
        nArray2[16] = 200;
        nArrayArray[1] = nArray2;
        int[] nArray3 = new int[21];
        nArray3[0] = 20;
        nArray3[1] = 80;
        nArray3[2] = 200;
        nArray3[3] = 80;
        nArray3[4] = 10;
        nArray3[5] = 60;
        nArray3[6] = 10;
        nArray3[8] = 20;
        nArray3[9] = 260;
        nArray3[10] = 20;
        nArray3[12] = 200;
        nArray3[13] = 10;
        nArray3[14] = 40;
        nArray3[15] = 10;
        nArray3[16] = 200;
        nArrayArray[2] = nArray3;
        int[] nArray4 = new int[21];
        nArray4[0] = 20;
        nArray4[1] = 80;
        nArray4[2] = 220;
        nArray4[3] = 100;
        nArray4[4] = 10;
        nArray4[5] = 60;
        nArray4[6] = 10;
        nArray4[8] = 20;
        nArray4[9] = 260;
        nArray4[10] = 20;
        nArray4[12] = 100;
        nArray4[13] = 10;
        nArray4[14] = 40;
        nArray4[15] = 10;
        nArray4[16] = 200;
        nArrayArray[3] = nArray4;
        int[] nArray5 = new int[21];
        nArray5[0] = 20;
        nArray5[1] = 100;
        nArray5[2] = 430;
        nArray5[3] = 200;
        nArray5[4] = 10;
        nArray5[5] = 80;
        nArray5[6] = 10;
        nArray5[8] = 90;
        nArray5[9] = 30;
        nArray5[10] = 20;
        nArray5[12] = 200;
        nArray5[13] = 10;
        nArray5[14] = 40;
        nArray5[15] = 10;
        nArray5[16] = 200;
        nArrayArray[4] = nArray5;
        int[] nArray6 = new int[21];
        nArray6[0] = 20;
        nArray6[1] = 200;
        nArray6[2] = 250;
        nArray6[3] = 200;
        nArray6[4] = 10;
        nArray6[5] = 60;
        nArray6[6] = 10;
        nArray6[8] = 40;
        nArray6[9] = 260;
        nArray6[10] = 30;
        nArray6[12] = 200;
        nArray6[13] = 10;
        nArray6[14] = 60;
        nArray6[15] = 10;
        nArray6[16] = 200;
        nArrayArray[5] = nArray6;
        int[] nArray7 = new int[21];
        nArray7[0] = 20;
        nArray7[1] = 100;
        nArray7[2] = 100;
        nArray7[3] = 100;
        nArray7[4] = 10;
        nArray7[5] = 60;
        nArray7[6] = 10;
        nArray7[8] = 20;
        nArray7[9] = 100;
        nArray7[10] = 20;
        nArray7[12] = 100;
        nArray7[13] = 10;
        nArray7[14] = 40;
        nArray7[15] = 10;
        nArray7[16] = 200;
        nArrayArray[6] = nArray7;
        int[] nArray8 = new int[21];
        nArray8[0] = 10;
        nArray8[1] = 50;
        nArray8[2] = 50;
        nArray8[3] = 50;
        nArray8[4] = 5;
        nArray8[5] = 20;
        nArray8[6] = 5;
        nArray8[8] = 5;
        nArray8[9] = 40;
        nArray8[10] = 10;
        nArray8[12] = 50;
        nArray8[13] = 5;
        nArray8[14] = 10;
        nArray8[15] = 5;
        nArray8[16] = 50;
        nArrayArray[7] = nArray8;
        int[] nArray9 = new int[21];
        nArray9[0] = 20;
        nArray9[1] = 50;
        nArray9[4] = 350;
        nArray9[5] = 30;
        nArray9[6] = 10;
        nArray9[8] = 100;
        nArray9[9] = 10;
        nArray9[12] = 150;
        nArray9[13] = 10;
        nArray9[14] = 40;
        nArray9[15] = 10;
        nArray9[16] = 150;
        nArrayArray[8] = nArray9;
        int[] nArray10 = new int[21];
        nArray10[0] = 20;
        nArray10[1] = 50;
        nArray10[4] = 450;
        nArray10[5] = 30;
        nArray10[6] = 10;
        nArray10[8] = 200;
        nArray10[9] = 250;
        nArray10[12] = 150;
        nArray10[13] = 10;
        nArray10[14] = 40;
        nArray10[15] = 10;
        nArray10[16] = 150;
        nArrayArray[9] = nArray10;
        int[] nArray11 = new int[21];
        nArray11[0] = 20;
        nArray11[1] = 40;
        nArray11[2] = 45;
        nArray11[3] = 90;
        nArray11[4] = 10;
        nArray11[5] = 20;
        nArray11[6] = 10;
        nArray11[8] = 20;
        nArray11[9] = 100;
        nArray11[10] = 150;
        nArray11[11] = 50;
        nArray11[12] = 400;
        nArray11[13] = 10;
        nArray11[14] = 40;
        nArray11[15] = 10;
        nArray11[16] = 200;
        nArrayArray[10] = nArray11;
        int[] nArray12 = new int[21];
        nArray12[10] = 370;
        nArray12[11] = 200;
        nArray12[12] = 20;
        nArray12[14] = 40;
        nArrayArray[11] = nArray12;
        int[] nArray13 = new int[21];
        nArray13[0] = 20;
        nArray13[1] = 20;
        nArray13[2] = 25;
        nArray13[3] = 45;
        nArray13[4] = 10;
        nArray13[5] = 10;
        nArray13[6] = 10;
        nArray13[8] = 20;
        nArray13[9] = 20;
        nArray13[10] = 20;
        nArray13[11] = 200;
        nArray13[12] = 100;
        nArray13[13] = 10;
        nArray13[14] = 40;
        nArray13[15] = 10;
        nArray13[16] = 200;
        nArrayArray[12] = nArray13;
        int[] nArray14 = new int[21];
        nArray14[0] = 20;
        nArray14[1] = 40;
        nArray14[4] = 190;
        nArray14[5] = 30;
        nArray14[6] = 10;
        nArray14[8] = 50;
        nArray14[9] = 50;
        nArray14[12] = 50;
        nArray14[13] = 50;
        nArray14[14] = 40;
        nArray14[15] = 10;
        nArray14[16] = 200;
        nArrayArray[13] = nArray14;
        int[] nArray15 = new int[21];
        nArray15[0] = 20;
        nArray15[1] = 20;
        nArray15[2] = 25;
        nArray15[3] = 50;
        nArray15[4] = 10;
        nArray15[5] = 10;
        nArray15[6] = 10;
        nArray15[8] = 20;
        nArray15[9] = 50;
        nArray15[10] = 50;
        nArray15[11] = 200;
        nArray15[12] = 140;
        nArray15[13] = 10;
        nArray15[14] = 40;
        nArray15[15] = 10;
        nArray15[16] = 200;
        nArrayArray[14] = nArray15;
        int[] nArray16 = new int[21];
        nArray16[0] = 20;
        nArray16[1] = 30;
        nArray16[2] = 35;
        nArray16[3] = 90;
        nArray16[4] = 10;
        nArray16[5] = 10;
        nArray16[6] = 10;
        nArray16[8] = 20;
        nArray16[9] = 250;
        nArray16[10] = 150;
        nArray16[11] = 200;
        nArray16[12] = 250;
        nArray16[13] = 10;
        nArray16[14] = 40;
        nArray16[15] = 50;
        nArray16[16] = 200;
        nArrayArray[15] = nArray16;
        int[] nArray17 = new int[21];
        nArray17[0] = 8;
        nArray17[1] = 20;
        nArray17[2] = 35;
        nArray17[3] = 30;
        nArray17[4] = 5;
        nArray17[5] = 3;
        nArray17[6] = 2;
        nArray17[8] = 8;
        nArray17[9] = 20;
        nArray17[10] = 5;
        nArray17[11] = 40;
        nArray17[12] = 30;
        nArray17[13] = 2;
        nArray17[14] = 6;
        nArray17[15] = 2;
        nArray17[16] = 50;
        nArrayArray[16] = nArray17;
        int[] nArray18 = new int[21];
        nArray18[1] = 50;
        nArray18[2] = 150;
        nArray18[3] = 50;
        nArray18[4] = 10;
        nArray18[5] = 10;
        nArray18[6] = 10;
        nArray18[9] = 70;
        nArray18[10] = 15;
        nArray18[12] = 50;
        nArray18[16] = 100;
        nArrayArray[17] = nArray18;
        int[] nArray19 = new int[21];
        nArray19[1] = 50;
        nArray19[2] = 150;
        nArray19[3] = 50;
        nArray19[4] = 10;
        nArray19[5] = 10;
        nArray19[6] = 10;
        nArray19[9] = 70;
        nArray19[10] = 15;
        nArray19[12] = 50;
        nArray19[16] = 100;
        nArrayArray[18] = nArray19;
        int[] nArray20 = new int[21];
        nArray20[1] = 50;
        nArray20[2] = 150;
        nArray20[3] = 50;
        nArray20[4] = 10;
        nArray20[5] = 10;
        nArray20[6] = 10;
        nArray20[9] = 70;
        nArray20[10] = 15;
        nArray20[12] = 50;
        nArray20[16] = 100;
        nArrayArray[19] = nArray20;
        int[] nArray21 = new int[21];
        nArray21[1] = 50;
        nArray21[2] = 150;
        nArray21[3] = 50;
        nArray21[4] = 10;
        nArray21[5] = 10;
        nArray21[6] = 10;
        nArray21[9] = 70;
        nArray21[12] = 50;
        nArray21[16] = 100;
        nArrayArray[20] = nArray21;
        this.costMatrix = nArrayArray;
        this.cargoUnits = new UnitTypeList[]{new UnitTypeList(Arrays.asList(this.humvee)), new UnitTypeList(Arrays.asList(this.humvee, this.humvee, this.battleTank, this.artillery, this.missileTank)), new UnitTypeList(Arrays.asList(this.fighterPlane, this.fighterPlane, this.helicopter, this.helicopter)), new UnitTypeList(Arrays.asList(this.fighterPlane, this.fighterPlane, this.fighterPlane, this.helicopter, this.helicopter, this.helicopter)), new UnitTypeList(Arrays.asList(this.chinnook, this.fighterPlane, this.fighterPlane)), new UnitTypeList(Arrays.asList(this.gunboat, this.submarine, this.transport)), new UnitTypeList(Arrays.asList(this.gunboat, this.submarine, this.cruiser)), new UnitTypeList(Arrays.asList(this.gunboat, this.carrier))};
        this.playerStartTemplates = new String[][]{{" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |TG |   |TG |   |TG |   |TG |", " --- --- --- --- --- --- --- --- ", "|P0 |   |P0 |   |P0 |   |P0 |   |", " --- --- --- --- --- --- --- --- ", "|   |TG |   |TG |   |TG |   |TG |", " --- --- --- --- --- --- --- --- ", "|P0 |   |P0 |   |P0 |   |P0 |   |", " --- --- --- --- --- --- --- --- ", "|   |TG |   |TG |   |TG |   |TG |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |P0 |   |   |P0 |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|U8 |   |U8 |   |U8 |   |U8 |   |", " --- --- --- --- --- --- --- --- ", "|   |U8 |   |U8 |   |U8 |   |   |", " --- --- --- --- --- --- --- --- ", "|U8 |   |U8 |   |U8 |   |U8 |   |", " --- --- --- --- --- --- --- --- ", "|   |U8 |   |U8 |   |U8 |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|P0 |   |P0 |   |P0 |   |P0 |   |", " --- --- --- --- --- --- --- --- ", "|   |R0 |   |TG |   |R0 |   |   |", " --- --- --- --- --- --- --- --- ", "|TG |   |R0 |   |TG |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |P0 |   |P0 |   |P0 |   |   |", " --- --- --- --- --- --- --- --- ", "|U8 |   |U8 |   |U8 |   |U8 |   |", " --- --- --- --- --- --- --- --- ", "|   |P0 |   |P0 |   |P0 |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |S4 |   |S4 |   |S4 |   |S4 |", " --- --- --- --- --- --- --- --- ", "|S4 |   |S4 |   |S4 |   |S4 |   |", " --- --- --- --- --- --- --- --- ", "|   |S4 |   |S4 |   |S4 |   |S4 |", " --- --- --- --- --- --- --- --- ", "|S4 |   |S4 |   |S4 |   |S4 |   |", " --- --- --- --- --- --- --- --- ", "|   |S4 |   |S4 |   |S4 |   |S4 |", " --- --- --- --- --- --- --- --- ", "|S4 |   |S4 |   |S4 |   |S4 |   |", " --- --- --- --- --- --- --- --- ", "|   |S4 |   |S4 |   |S4 |   |S4 |", " --- --- --- --- --- --- --- --- "}};
        this.captureTheFlagTemplates = new String[][]{{" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   | x |   |   |   | x |   |   |", " --- ---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   |   |   |O1 |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   |   | x |   |   |   |   |   |", " --- --- ---x---x---x---x---x--- ", "|   |   |   |   |   | x | x |   |", " --- --- --- ---x---x--- --- --- ", "|   | C |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- ---x---x---x---x--- --- --- ", "|   |   | M |   |   | x | x |   |", " ---x---x---x---x---x---x---x--- ", "|   | M |   |   |   |   | M |   |", " ---x---x---x---x---x---x---x--- ", "|   |   |   |O1 |   |   |   |   |", " ---x---x---x---x---x---x---x--- ", "|   |   |   |   |   |   |   |   |", " ---x---x---x---x---x---x---x--- ", "|   | M |   |   |   | M | X |   |", " ---x---x---x---x---x---x---x--- ", "|   |   | M |   |   |   | x |   |", " --- ---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}};
        this.survivalTemplates = new String[][]{{" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- ---x---x--- --- --- --- --- --- --- ", "|   | x |   |   |   |   |   |   | M |   |   |   |   |   |   |   |", " --- ---x---x--- --- --- ---x---x---x--- --- ---x---x--- --- --- ", "|   |   | x | x |B0 |   |   | M | M |   |   |   | M |   |   |   |", " --- --- ---x---x---x--- ---x---x---x--- ---x---x---x---x--- --- ", "|   |   |   | X |   |   |   |   | M |   |   | M |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   |   |   |   | M |   |   | M | M |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   |   |   |   |   |   |   |O3 | M |   |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   | M |A1 |   |   |   |   |   |A3 |   |   | M |   |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   | M |   |   |   |O0 |O0 |   |   |   |   |   |B1 |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |B3 |   |   |   | M | M | M | M |   |   |   |   | M | x |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   | x |   |", " --- --- ---x---x--- --- --- --- --- --- ---x---x---x---x--- --- ", "|   |   |   |   |   |   |   |   |   |   |   | x |A0 | x | x |   |", " --- --- --- --- --- ---x---x---x---x--- --- ---x---x--- --- --- ", "|   |   |   |   |   |   |   | M | M |   |   | x | x | x |   |   |", " --- --- --- --- ---x---x---x---x---x--- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- ---x---x---x---x---x---x--- --- --- --- --- --- ", "|   |   |   |   |   |   |C0 |C0 |C0 |   |   |   |   |   |   |   |", " --- --- --- --- ---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   |   | C |   | x | M | M | M | M |   |   |   |   |   |   |", " --- --- --- --- --- ---x---x---x---x---x---x--- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- ---x---x--- --- --- ---x---x--- --- --- --- --- ", "|   |   |   |   | x |   |B0 |   |   |   | M |   |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   |   | M | M |   |   |   |   |   | M | M | M |   |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   | M | M |C2 |   |   | M | M |   |   |O2 | M | M |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   | M |O1 |   |   |   |A2 |A2 |   |   |   |C3 | M | x |   |", " ---x---x---x---x---x---x---x---x---x---x---x---x---x---x---x--- ", "|   | M | M |   |   |   |   |   |   |   |   |   |   | M |   |   |", " ---x---x---x---x---x---x---x---x---x---x---x---x---x---x---x--- ", "|   |   |   |   |   |   | X | X |   |   |   |   |   |   |B1 |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   | X | B4| B5| X | X |   |   |   | M |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   | X | B1| S | B3| X |   |   |   | M | x |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |B3 |   |   |   |   | B6|   | B2|   |   |   |   | M | x |   |", " ---x---x---x---x---x---x---x---x---x---x---x---x---x---x---x--- ", "|   |   |   |   |   |   |   |   | Y1|   |   |   |   | M | M |   |", " ---x---x---x---x---x---x---x---x---x---x---x---x---x---x---x--- ", "|   | x | M |A1 |   |   |   |   |   |   |   |   |A3 | M |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   | M | M |O0 |   |   |   |   |   |   |C0 | M | M | x |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   | M | M | M |   |   |   |   |   | M | M | x |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   |   |   |   | M |   |   |   |   |   | x |   |   |   |   |", " --- --- --- --- ---x---x--- --- --- ---x---x--- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- ---x---x---x---x---x--- --- --- --- ", "|   |   |   |   |   |   |   |   | M | M | M |   |   |   |   |   |", " --- ---x---x---x--- ---x---x---x---x---x---x---x--- --- --- --- ", "|   |   | M |   |   |   |   | M | M |A2 |   |   |   |   |   |   |", " --- ---x---x---x--- ---x---x---x---x---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   | M |   |   |   |   |B1 |   |   |", " --- --- ---x--- --- ---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   | M |   |   |", " --- --- --- --- --- ---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   | C |   |   | M |   |   |   |   |   |C3 | M |   |   |", " --- --- --- --- --- ---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   |   | M |   |   |   |   |   | M | M |   |   |", " --- --- --- --- ---x---x---x---x---x--- --- ---x---x---x--- --- ", "|   |   |   |   |   |   |O2 |   |   | x |   |   |   |   |   |   |", " --- --- --- ---x---x---x---x---x---x--- --- --- ---x--- --- --- ", "|   |   |   |B3 |   |   |   |   | M | x |   |   |   |   |   |   |", " --- --- --- ---x---x---x---x---x---x--- --- --- --- --- --- --- ", "|   | x | x | x |   |   |   |   | M | x | x |   |   |   |   |   |", " --- ---x---x---x---x---x---x---x---x---x--- --- --- --- --- --- ", "|   | x |A1 |   |   |   |   |   |   |   |B1 |   |   |   |   |   |", " ---x---x---x---x---x---x---x---x---x---x--- --- --- --- --- --- ", "|   | x |A1 |   |   |C0 |   |   |   |   |   |   |   |   |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x--- --- --- --- ", "|   | x | x |   |   |   |   |   |   |   | M | M |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   | x |   |   | M |O0 |O0 |   |   |   |   |   |   |   |   |", " --- --- --- ---x---x---x---x---x---x---x---x--- --- --- --- --- ", "|   |   |   |   |   | M | M | M |   |B2 |   |   |   |   |   |   |", " --- --- --- --- ---x---x---x---x--- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- ---x---x---x---x---x--- --- --- --- ---x---x--- --- ", "|   |   |   | x | M | M | M | M |   |   |   |   |   | M |   |   |", " --- --- --- ---x---x---x---x---x--- --- --- ---x---x---x--- --- ", "|   |   | x | x |   |A2 | M |   |   |   |   |   | M | M |   |   |", " --- --- ---x---x---x---x---x---x---x--- ---x---x---x---x--- --- ", "|   |   | x |   |   |   |   |   |   |   |   |   | M | M |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   | x | x | X |   |   |   |   | M | M |   |   |   |   |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   | x | M | M |   |   | M | M | M | M | M |   |   |B1 |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |O2 | M |O2 | M |O2 |   |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |B3 |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   | S |   |   |   |   |   |   |   | X | x |   |", " ---x---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   | M |   |   |   |   |   |   |   |   |C3 |   | X | X | x |   |", " ---x---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   | M |A1 |   |   |   |   | M |A0 |   |   |   | R0| X | x |   |", " ---x---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   | M | M |   |   |B2 |   | M | M | M |   |   | X | X | x |   |", " ---x---x---x--- --- --- ---x---x---x---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   | x | x | x |   |   |", " --- --- --- --- --- --- --- --- --- ---x---x--- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   | x |   |   |   |   |", " --- --- --- --- --- --- --- --- ---x---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   |   | M | M | M |   |   |   |   |", " --- --- --- --- --- --- --- --- ---x---x---x---x--- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- ---x---x---x---x--- --- --- --- --- --- --- --- --- --- ", "|   |   |   | M | M | M | x | x | x | x | x | x | x |   | x |   |", " --- ---x---x---x---x---x---x--- ---x---x---x---x--- ---x--- --- ", "|   |   | M | M |   |A2 | M | x | x |   |   |   | x | x | x |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   | F0|   | x |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   |   |O3 | M |C0 |   | F0|   |   |   | x |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   | x | x |   |   |   | M | M | M |   |   |   | M | M | x |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |B2 |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- ---x---x---x---x---x--- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   | C |   |", " --- --- --- --- --- --- ---x---x---x---x--- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- ---x---x--- --- ---x---x---x---x---x---x--- --- --- --- --- ", "|   |   | M |B0 |   |   |   |   |   |   |   |   |B0 |   |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   | M |   |   |   |   | M | M | M |   |   |   |   |   |   |", " ---x---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   | M | M |   |   |   |   |O3 | M |C1 |   |   |   | M |   |   |", " ---x---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   | M | M |C1 |   |   |   |O3 | M |   |   |   |A3 | M |   |   |", " ---x---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   | M | M |   |   |   |   |   |   |   |A0 | M | M |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   | M | M |   |   |   |   |   | M | M | M |   |   |   |", " --- --- ---x---x---x---x--- --- --- ---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}};
        this.playerBaseTemplates = new String[][]{{" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " ---x---x---x--- --- --- --- --- ", "|   | M | M |   |   | x | x |   |", " ---x---x---x---x---x---x---x--- ", "|   | M |A2 |   |   |O2 | M |   |", " ---x---x---x---x---x---x---x--- ", "|   |   |   |   |   |   |   |   |", " ---x---x-+-x---x---x-+-x---x--- ", "|   |   |   +   +   +   |   |   |", " ---x---x---x---x-+-x---x--- --- ", "|   | M | M |   |   |   | x |   |", " ---x---x---x---x---x---x--- --- ", "|   | M |   |   |B2 |   | x |   |", " ---x---x--- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   | x |B0 |   |   |   |   |", " --- --- ---x---x---x--- --- --- ", "|   |   |   |   |   | x | x |   |", " ---x---x---x-+-x---x---x---x--- ", "|   | M |   |   +   |O3 | M |   |", " ---x---x---x---x---x---x---x--- ", "|   | M |A1 |   |   |   | M |   |", " ---x---x---x---x---x---x---x--- ", "|   | M |   |   |   |C3 | M |   |", " ---x---x---x---x---x---x---x--- ", "|   |   |   |   |   | M | M |   |", " --- --- --- ---x---x---x---x--- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- ---x---x---x--- --- --- --- ", "|   | x | M | M | x |   | C |   |", " ---x---x---x---x---x--- --- --- ", "|   | M |C2 |   |A2 |   |   |   |", " ---x---x---x---x---x---x--- --- ", "|   | M |   |   |   |   |   |   |", " ---x---x---x---x---x---x--- --- ", "|   | M |O1 |   +   +   |   |   |", " ---x---x---x---x---x-+-x--- --- ", "|   | M |   |   |   |   | x |   |", " ---x---x---x---x---x---x--- --- ", "|   | M |   |   |   |B2 | x |   |", " ---x---x--- --- --- ---x--- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}};
        this.rarePropTemplates = new String[][]{{" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   | x |   |   |   |   |   |", " --- --- ---x---x---x---x---x--- ", "|   | x | x |   |   | M | M |   |", " --- ---x---x---x---x---x---x--- ", "|   | x |   |   |C3 | M | x |   |", " --- --- ---x---x---x--- --- --- ", "|   |   |   |   |   | X |   |   |", " --- --- --- ---x---x---x--- --- ", "|   | C |   | x |   | x | x |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   | C |   |   |", " --- --- ---x---x--- --- --- --- ", "|   | x |   | M |   |   |   |   |", " --- ---x---x---x---x--- --- --- ", "|   |   |   |   |   | x |   |   |", " --- --- ---x---x---x--- --- --- ", "|   |   |   |B2 |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   | C |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   | C |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- ---x--- --- --- --- ", "|   |   |   |   |   | x | C |   |", " --- --- ---x---x---x--- --- --- ", "|   |   |   | M |   |   | x |   |", " --- --- ---x---x---x---x--- --- ", "|   |   |   | M |A2 |   |   |   |", " --- --- --- ---x---x---x--- --- ", "|   |   |   | M |   |   |   |   |", " --- --- ---x---x---x---x--- --- ", "|   |   | x |   |   |   |   |   |", " --- --- --- --- --- ---x--- --- ", "|   |   |   |   |   | x | x |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   | C |   |   |   |   |", " ---x---x--- --- --- --- --- --- ", "|   | M |   |   |   | x | x |   |", " ---x---x---x---x---x---x--- --- ", "|   | M | M |   |   |   |B1 |   |", " ---x---x---x---x---x---x--- --- ", "|   |   | M |A1 |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   |   |   |   |   | x | x |   |", " --- ---x---x---x---x--- --- --- ", "|   | x |   | x |   | x |   |   |", " --- --- --- ---x--- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- ---x--- --- ---x--- --- ", "|   |   |   | x |   |   |   |   |", " --- ---x---x--- --- --- --- --- ", "|   | x |   |   |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   |   |   |   | M |   | x |   |", " ---x---x---x---x---x---x--- --- ", "|   |   |A0 |   |   |   | x |   |", " ---x---x---x---x---x---x--- --- ", "|   | M | M |C1 |   |   |   |   |", " ---x---x---x---x---x---x--- --- ", "|   |   | M | x |   |   | x |   |", " --- ---x---x--- --- ---x--- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- ---x--- --- --- --- --- ", "|   | x |   | x |   | x |   |   |", " --- ---x---x---x---x---x--- --- ", "|   |   | M | M |   | X | x |   |", " --- ---x---x---x---x---x--- --- ", "|   |   |   |C2 |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   | x |   |   |   |   |B1 |   |", " ---x---x---x---x---x---x--- --- ", "|   | M |   | M |   |   |   |   |", " ---x---x---x---x---x--- --- --- ", "|   |   |   | M | M |   | C |   |", " --- --- ---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- ---x---x--- ", "|   |   | x | x |   |   | M |   |", " --- --- ---x---x---x---x---x--- ", "|   |   |   |   +   +   |   |   |", " --- ---x---x-+-x---x---x--- --- ", "|   |   |   |   |   |   |   |   |", " ---x---x---x---x---x---x--- --- ", "|   |   | M |O0 | W0|   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   |   | M | M | M | x |   |   |", " --- ---x---x---x---x--- --- --- ", "|   |   |   |   |   | x | C |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- ---x---x--- ", "|   |   |   |   |   |   | M |   |", " --- --- ---x---x---x---x---x--- ", "|   | x | x |   |   | M | M |   |", " --- ---x---x---x---x---x---x--- ", "|   | x |O1 |   +   | W1| M |   |", " ---x---x---x---x---x---x---x--- ", "|   | x |   |   |   |   | M |   |", " --- ---x---x---x---x---x---x--- ", "|   | x |   |   |   | F0| M |   |", " --- --- ---x---x---x---x---x--- ", "|   |   |   |   |   | X | x |   |", " --- --- --- ---x---x---x--- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}};
        this.commonPropTemplates = new String[][]{{" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   | C |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   | C |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}};
        Object var2_1 = null;
        this.unitTypes.add(this.infantry);
        this.baseStation.getProducedBy().add(this.infantry);
        int[][] nArray22 = this.costMatrix;
        int i4 = nArray22.length;
        int[][] nArray23 = new int[i4 + 1][];
        for (int i6 = 0; i6 < i4; ++i6) {
            int[] nArray24 = nArray22[i6];
            int[] nArray25 = new int[nArray24.length + 1];
            for (int i9 = 0; i9 < nArray24.length; ++i9) {
                nArray25[i9] = nArray24[i9];
            }
            nArray23[i6] = nArray25;
        }
        nArray23[17][21] = 60;
        nArray23[i4] = new int[nArray23[0].length];
        this.costMatrix = nArray23;
    }

    @Override
    public String getId() {
        return "dsf";
    }

    @Override
    public FactionList getFactions() {
        return GameConfig.cleanContent ? this.c : this.b;
    }

    @Override
    public TerrainTypeList getTerrainTypes() {
        return this.terrainTypes;
    }

    @Override
    public UnitTypeList getUnitTypes() {
        return this.unitTypes;
    }

    @Override
    public UnitTypeSlots getUnitTypeSlots() {
        return this.unitTypeSlots;
    }

    @Override
    public int[][] getCostMatrix() {
        return this.costMatrix;
    }

    @Override
    public UnitTypeList getCargoUnits(int i1) {
        return this.cargoUnits[i1];
    }

    @Override
    public int getUnitTypeCount() {
        return this.cargoUnits.length;
    }

    @Override
    public String[][] getPlayerStartTemplates() {
        return this.playerStartTemplates;
    }

    @Override
    public String[][] getCaptureTheFlagTemplates() {
        return this.captureTheFlagTemplates;
    }

    @Override
    public String[][] getSurvivalTemplates() {
        return this.survivalTemplates;
    }

    @Override
    public String[][] getPlayerBaseTemplates() {
        return this.playerBaseTemplates;
    }

    @Override
    public String[][] getRarePropTemplates() {
        return this.rarePropTemplates;
    }

    @Override
    public String[][] getCommonPropTemplates() {
        return this.commonPropTemplates;
    }
}


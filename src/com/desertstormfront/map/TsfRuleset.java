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
import java.util.Arrays;

public strictfp class TsfRuleset
extends MapDefinition {
    private final FactionList a = new FactionList(Arrays.asList(new Faction(0, "USA[i18n]: USA", "USA"), new Faction(1, "China[i18n]: China", "CHINA"), new Faction(2, "Russia[i18n]: Russia", "RUSSIA"), new Faction(3, "England[i18n]: England", "ENGLAND"), new Faction(4, "Germany[i18n]: Germany", "GERMANY"), new Faction(5, "India[i18n]: India", "INDIA"), new Faction(6, "Japan[i18n]: Japan", "JAPAN"), new Faction(7, "Brazil[i18n]: Brazil", "BRAZIL")));
    private final FactionList b = new FactionList(Arrays.asList(new Faction(0, "BlueLeader[i18n]: Blue Leader", "USA"), new Faction(1, "RedLeader[i18n]: Red Leader", "CHINA"), new Faction(2, "BlueGold[i18n]: Blue/Gold", "RUSSIA"), new Faction(3, "BlueWhite[i18n]: Blue/White", "ENGLAND"), new Faction(4, "RedGold[i18n]: Red/Gold", "GERMANY"), new Faction(5, "OrangeBlack[i18n]: Orange/Black", "INDIA"), new Faction(6, "RedBlack[i18n]: Red/Black", "JAPAN"), new Faction(7, "OrangeWhite[i18n]: Orange/White", "BRAZIL")));
    private final TerrainTypeList terrainTypes = new TerrainTypeList(Arrays.asList(new TerrainType(0, "Flora[i18n]: Flora", "FLORA", 'X', 16), new TerrainType(1, "Mountain[i18n]: Mountain", "MOUNTAIN", 'M', 3), new TerrainType(2, "Volcano[i18n]: Volcano", "VOLCANO", 'K', 1), new TerrainType(3, "Island[i18n]: Island", "ISLAND", 'I', 3), new TerrainType(4, "Cliff[i18n]: Cliff", "CLIFF", 'C', 1)));
    private UnitType general = new UnitType(0, "General[i18n]: General", "GENERAL", 'H', 500L, false, Layer.Base, Domain.Ground, 0.32f, 0.17f, 0.34f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 0.2f, 0, 1.0f, true, false, false, false, false, 2, 2, 4.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
    private UnitType battleTank = new UnitType(1, "BattleTank[i18n]: Battle Tank", "BATTLE_TANK", 'I', 100L, false, Layer.Base, Domain.Ground, 0.32f, 0.17f, 0.2f, 0.0f, false, true, AmmoType.Shell, 0.15f, 0.35f, 0, 1.2f, false, true, false, false, false, 1, 2, 4.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
    private UnitType artillery = new UnitType(2, "Artillery[i18n]: Artillery", "ARTILLERY", 'K', 200L, false, Layer.Base, Domain.Ground, 0.37f, 0.17f, 0.18f, 0.0f, false, true, AmmoType.Cannonball, 0.25f, 0.7f, 0, 2.6f, false, false, false, false, false, 2, 4, 8.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
    private UnitType missileTank = new UnitType(3, "MissileTank[i18n]: Missile Tank", "MISSILE_TANK", 'L', 150L, false, Layer.Base, Domain.Ground, 0.34f, 0.17f, 0.19f, 0.0f, false, true, AmmoType.Missile, 0.2f, 0.25f, 0, 1.8f, false, false, false, false, false, 3, 4, 12.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
    private UnitType fighterPlane = new UnitType(4, "FighterPlane[i18n]: Fighter Plane", "FIGHTER_PLANE", 'M', 250L, false, Layer.Upper, Domain.Air, 0.2f, 1.0f, 0.88f, 26.0f, true, false, AmmoType.Bullets, 0.2f, 1.0f, 0, 1.2f, false, false, false, false, false, 3, 2, 4.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
    private UnitType helicopter = new UnitType(5, "Helicopter[i18n]: Helicopter", "HELICOPTER", 'N', 300L, false, Layer.Upper, Domain.Air, 0.17f, 1.0f, 0.5f, 22.0f, false, false, AmmoType.Missile, 0.2f, 1.0f, 3, 1.2f, false, false, false, false, false, 2, 2, 6.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
    private UnitType destroyer = new UnitType(6, "Destroyer[i18n]: Destroyer", "DESTROYER", 'P', 200L, false, Layer.Base, Domain.Water, 0.27f, 0.11f, 0.24f, 0.0f, false, false, AmmoType.Shell, 0.2f, 0.1f, 0, 1.2f, false, false, false, false, false, 1, 2, 4.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
    private UnitType submarine = new UnitType(7, "Submarine[i18n]: Submarine", "SUBMARINE", 'Q', 400L, false, Layer.Under, Domain.Water, 0.2f, 0.0f, 0.2f, 0.0f, false, false, AmmoType.Torpedo, 0.2f, 0.0f, 0, 2.0f, false, false, false, false, false, 1, 2, 7.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
    private UnitType cruiser = new UnitType(8, "Cruiser[i18n]: Cruiser", "CRUISER", 'R', 550L, false, Layer.Base, Domain.Water, 0.45f, 0.11f, 0.19f, 0.0f, false, false, AmmoType.Missile, 0.3f, 0.2f, 0, 1.8f, false, false, false, false, false, 3, 4, 9.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
    private UnitType airship = new UnitType(9, "Airship[i18n]: Airship", "AIRSHIP", 'S', 300L, false, Layer.Upper, Domain.Air, 0.28f, 1.0f, 0.22f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 1.0f, 0, 1.4f, false, false, false, false, false, 2, 2, 4.0f, new UnitTypeList(), new UnitTypeList(Arrays.asList(this.general, this.battleTank, this.artillery, this.missileTank)), 1, false);
    private UnitType carrier = new UnitType(10, "Carrier[i18n]: Carrier", "CARRIER", 'T', 600L, false, Layer.Base, Domain.Water, 0.49f, 0.11f, 0.17f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 0.2f, 0, 1.4f, false, false, false, false, false, 2, 2, 4.0f, new UnitTypeList(), new UnitTypeList(Arrays.asList(this.fighterPlane, this.helicopter, this.airship)), 6, true);
    private UnitType transport = new UnitType(11, "Transport[i18n]: Transport", "TRANSPORT_SHIP", 'U', 200L, false, Layer.Base, Domain.Water, 0.4f, 0.11f, 0.18f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 0.2f, 0, 1.6f, false, false, false, false, false, 1, 2, 4.0f, new UnitTypeList(), new UnitTypeList(Arrays.asList(this.general, this.battleTank, this.artillery, this.missileTank)), 4, false);
    private UnitType baseStation = new UnitType(12, "BaseStation[i18n]: Base Station", "BASE_STATION", 'A', 2000L, true, Layer.Base, Domain.Ground, 0.5f, 0.17f, 0.0f, 0.0f, false, false, null, 0.0f, 0.0f, 0, 0.0f, false, false, true, false, false, 2, 0, 0.0f, new UnitTypeList(Arrays.asList(this.battleTank, this.artillery, this.missileTank)), new UnitTypeList(Arrays.asList(this.general, this.battleTank, this.artillery, this.missileTank)), 8, false);
    private UnitType shipyard = new UnitType(13, "Shipyard[i18n]: Shipyard", "SHIPYARD", 'B', 2000L, true, Layer.Base, Domain.Water, 0.5f, 0.17f, 0.0f, 0.0f, false, false, null, 0.0f, 0.0f, 0, 0.0f, false, false, true, false, false, 2, 0, 0.0f, new UnitTypeList(Arrays.asList(this.destroyer, this.submarine, this.transport, this.cruiser, this.carrier)), new UnitTypeList(Arrays.asList(this.destroyer, this.submarine, this.transport, this.cruiser, this.carrier)), 8, false);
    private UnitType airfield = new UnitType(14, "Airfield[i18n]: Airfield", "AIRFIELD", 'C', 2000L, true, Layer.Base, Domain.Ground, 0.5f, 0.17f, 0.0f, 0.0f, false, false, null, 0.0f, 0.0f, 0, 0.0f, false, false, true, false, false, 3, 0, 0.0f, new UnitTypeList(Arrays.asList(this.fighterPlane, this.helicopter, this.airship)), new UnitTypeList(Arrays.asList(this.fighterPlane, this.helicopter, this.airship)), 8, true);
    private final UnitTypeList unitTypes = new UnitTypeList(Arrays.asList(this.general, this.battleTank, this.artillery, this.missileTank, this.fighterPlane, this.helicopter, this.destroyer, this.submarine, this.cruiser, this.airship, this.carrier, this.transport, this.baseStation, this.shipyard, this.airfield));
    private final UnitTypeSlots unitTypeSlots = new UnitTypeSlots(this.general, this.battleTank, this.battleTank, this.artillery, this.missileTank, null, this.baseStation, this.shipyard, this.airfield, null, this.transport, this.carrier, this.airship, this.fighterPlane, this.helicopter, this.cruiser, this.submarine, this.destroyer, null, null, null);
    private final int[][] costMatrix;
    private final UnitTypeList[] cargoUnits;
    private final String[][] playerStartTemplates;
    private final String[][] captureTheFlagTemplates;
    private final String[][] survivalTemplates;
    private final String[][] playerBaseTemplates;
    private final String[][] rarePropTemplates;
    private final String[][] commonPropTemplates;

    TsfRuleset() {
        int[][] nArrayArray = new int[15][];
        int[] nArray = new int[15];
        nArray[0] = 20;
        nArray[1] = 100;
        nArray[2] = 100;
        nArray[3] = 10;
        nArray[4] = 20;
        nArray[5] = 100;
        nArray[6] = 10;
        nArray[8] = 100;
        nArray[9] = 10;
        nArray[10] = 40;
        nArray[11] = 10;
        nArrayArray[0] = nArray;
        int[] nArray2 = new int[15];
        nArray2[0] = 20;
        nArray2[1] = 200;
        nArray2[2] = 100;
        nArray2[3] = 10;
        nArray2[4] = 20;
        nArray2[5] = 260;
        nArray2[6] = 20;
        nArray2[8] = 200;
        nArray2[9] = 10;
        nArray2[10] = 40;
        nArray2[11] = 10;
        nArrayArray[1] = nArray2;
        int[] nArray3 = new int[15];
        nArray3[0] = 20;
        nArray3[1] = 200;
        nArray3[2] = 100;
        nArray3[3] = 10;
        nArray3[4] = 20;
        nArray3[5] = 260;
        nArray3[6] = 20;
        nArray3[8] = 100;
        nArray3[9] = 10;
        nArray3[10] = 40;
        nArray3[11] = 10;
        nArrayArray[2] = nArray3;
        int[] nArray4 = new int[15];
        nArray4[0] = 20;
        nArray4[1] = 500;
        nArray4[2] = 200;
        nArray4[3] = 10;
        nArray4[4] = 90;
        nArray4[5] = 30;
        nArray4[6] = 20;
        nArray4[8] = 200;
        nArray4[9] = 10;
        nArray4[10] = 40;
        nArray4[11] = 10;
        nArrayArray[3] = nArray4;
        int[] nArray5 = new int[15];
        nArray5[0] = 20;
        nArray5[3] = 350;
        nArray5[4] = 100;
        nArray5[5] = 10;
        nArray5[8] = 150;
        nArray5[9] = 10;
        nArray5[10] = 40;
        nArray5[11] = 10;
        nArrayArray[4] = nArray5;
        int[] nArray6 = new int[15];
        nArray6[0] = 20;
        nArray6[3] = 450;
        nArray6[4] = 200;
        nArray6[5] = 250;
        nArray6[8] = 150;
        nArray6[9] = 10;
        nArray6[10] = 40;
        nArray6[11] = 10;
        nArrayArray[5] = nArray6;
        int[] nArray7 = new int[15];
        nArray7[0] = 20;
        nArray7[1] = 45;
        nArray7[2] = 100;
        nArray7[3] = 10;
        nArray7[4] = 20;
        nArray7[5] = 100;
        nArray7[6] = 150;
        nArray7[7] = 50;
        nArray7[8] = 400;
        nArray7[9] = 10;
        nArray7[10] = 40;
        nArray7[11] = 10;
        nArrayArray[6] = nArray7;
        int[] nArray8 = new int[15];
        nArray8[6] = 370;
        nArray8[7] = 200;
        nArray8[8] = 20;
        nArray8[10] = 40;
        nArrayArray[7] = nArray8;
        int[] nArray9 = new int[15];
        nArray9[0] = 20;
        nArray9[1] = 20;
        nArray9[2] = 45;
        nArray9[3] = 10;
        nArray9[4] = 20;
        nArray9[5] = 20;
        nArray9[6] = 20;
        nArray9[7] = 200;
        nArray9[8] = 100;
        nArray9[9] = 10;
        nArray9[10] = 40;
        nArray9[11] = 10;
        nArrayArray[8] = nArray9;
        int[] nArray10 = new int[15];
        nArray10[0] = 20;
        nArray10[3] = 150;
        nArray10[4] = 50;
        nArray10[5] = 50;
        nArray10[8] = 50;
        nArray10[9] = 50;
        nArray10[10] = 40;
        nArray10[11] = 10;
        nArrayArray[9] = nArray10;
        int[] nArray11 = new int[15];
        nArray11[0] = 20;
        nArray11[1] = 25;
        nArray11[2] = 50;
        nArray11[3] = 10;
        nArray11[4] = 20;
        nArray11[5] = 50;
        nArray11[6] = 50;
        nArray11[7] = 200;
        nArray11[8] = 140;
        nArray11[9] = 10;
        nArray11[10] = 40;
        nArray11[11] = 10;
        nArrayArray[10] = nArray11;
        int[] nArray12 = new int[15];
        nArray12[0] = 20;
        nArray12[1] = 45;
        nArray12[2] = 100;
        nArray12[3] = 10;
        nArray12[4] = 20;
        nArray12[5] = 250;
        nArray12[6] = 150;
        nArray12[7] = 200;
        nArray12[8] = 250;
        nArray12[9] = 10;
        nArray12[10] = 40;
        nArray12[11] = 50;
        nArrayArray[11] = nArray12;
        int[] nArray13 = new int[15];
        nArray13[1] = 100;
        nArray13[2] = 50;
        nArray13[3] = 10;
        nArray13[5] = 70;
        nArray13[6] = 15;
        nArray13[8] = 50;
        nArrayArray[12] = nArray13;
        int[] nArray14 = new int[15];
        nArray14[1] = 100;
        nArray14[2] = 50;
        nArray14[3] = 10;
        nArray14[5] = 70;
        nArray14[6] = 15;
        nArray14[8] = 50;
        nArrayArray[13] = nArray14;
        int[] nArray15 = new int[15];
        nArray15[1] = 100;
        nArray15[2] = 50;
        nArray15[3] = 10;
        nArray15[5] = 70;
        nArray15[6] = 15;
        nArray15[8] = 50;
        nArrayArray[14] = nArray15;
        this.costMatrix = nArrayArray;
        this.cargoUnits = new UnitTypeList[]{new UnitTypeList(Arrays.asList(this.battleTank)), new UnitTypeList(Arrays.asList(this.battleTank, this.battleTank, this.artillery, this.missileTank)), new UnitTypeList(Arrays.asList(this.fighterPlane, this.fighterPlane, this.helicopter, this.helicopter)), new UnitTypeList(Arrays.asList(this.fighterPlane, this.fighterPlane, this.fighterPlane, this.helicopter, this.helicopter, this.helicopter)), new UnitTypeList(Arrays.asList(this.airship, this.fighterPlane, this.fighterPlane)), new UnitTypeList(Arrays.asList(this.destroyer, this.submarine, this.transport)), new UnitTypeList(Arrays.asList(this.destroyer, this.submarine, this.cruiser)), new UnitTypeList(Arrays.asList(this.destroyer, this.carrier))};
        this.playerStartTemplates = new String[][]{{" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |TG |   |TG |   |TG |   |TG |", " --- --- --- --- --- --- --- --- ", "|P0 |   |P0 |   |P0 |   |P0 |   |", " --- --- --- --- --- --- --- --- ", "|   |TG |   |TG |   |TG |   |TG |", " --- --- --- --- --- --- --- --- ", "|P0 |   |P0 |   |P0 |   |P0 |   |", " --- --- --- --- --- --- --- --- ", "|   |TG |   |TG |   |TG |   |TG |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |P0 |   |   |P0 |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|U8 |   |U8 |   |U8 |   |U8 |   |", " --- --- --- --- --- --- --- --- ", "|   |U8 |   |U8 |   |U8 |   |   |", " --- --- --- --- --- --- --- --- ", "|U8 |   |U8 |   |U8 |   |U8 |   |", " --- --- --- --- --- --- --- --- ", "|   |U8 |   |U8 |   |U8 |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|P0 |   |P0 |   |P0 |   |P0 |   |", " --- --- --- --- --- --- --- --- ", "|   |R0 |   |TG |   |R0 |   |   |", " --- --- --- --- --- --- --- --- ", "|TG |   |R0 |   |TG |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |P0 |   |P0 |   |P0 |   |   |", " --- --- --- --- --- --- --- --- ", "|U8 |   |U8 |   |U8 |   |U8 |   |", " --- --- --- --- --- --- --- --- ", "|   |P0 |   |P0 |   |P0 |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |S4 |   |S4 |   |S4 |   |S4 |", " --- --- --- --- --- --- --- --- ", "|S4 |   |S4 |   |S4 |   |S4 |   |", " --- --- --- --- --- --- --- --- ", "|   |S4 |   |S4 |   |S4 |   |S4 |", " --- --- --- --- --- --- --- --- ", "|S4 |   |S4 |   |S4 |   |S4 |   |", " --- --- --- --- --- --- --- --- ", "|   |S4 |   |S4 |   |S4 |   |S4 |", " --- --- --- --- --- --- --- --- ", "|S4 |   |S4 |   |S4 |   |S4 |   |", " --- --- --- --- --- --- --- --- ", "|   |S4 |   |S4 |   |S4 |   |S4 |", " --- --- --- --- --- --- --- --- "}};
        this.captureTheFlagTemplates = new String[][]{{" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   | X | X | X |   |", " --- ---x---x---x---x---x--- --- ", "|   | X |   |   |   | X | X |   |", " --- ---x---x---x---x---x--- --- ", "|   | X | X |A1 |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   | X |   |   |   |   |   |   |", " --- --- ---x---x---x---x--- --- ", "|   |   |   |   |   | X |   |   |", " --- --- --- ---x---x--- --- --- ", "|   |   |   | X | X | X |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- ---x---x---x---x--- --- --- ", "|   | X |   |   |   | X | X |   |", " ---x---x---x---x---x---x---x--- ", "|   |   |   |   |   | X | X |   |", " ---x---x---x---x---x---x---x--- ", "|   |   | X |A1 |   |   |   |   |", " ---x---x---x---x---x---x---x--- ", "|   |   | X | X |   |   |   |   |", " ---x---x---x---x---x---x---x--- ", "|   |   |   |   |   |   | X |   |", " ---x---x---x---x---x---x---x--- ", "|   | X | X |   |   | X | X |   |", " --- --- ---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}};
        this.survivalTemplates = new String[][]{{" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   | X |   |   |   |   |   |   |   |   |   |   | X | X |   |   |", " --- ---x---x--- --- --- --- --- --- --- --- ---x---x--- --- --- ", "|   |   | X | X |B0 |   | X | X | X |   |   | X | X | X |   |   |", " --- --- ---x---x---x--- ---x---x--- --- ---x---x--- --- --- --- ", "|   |   |   | X |   |   |   | K | X |   |   | X |   |   |   |   |", " --- --- --- ---x---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   |   |   |   |   |   |   | X |   |   | X | X |   | I |   |", " --- --- --- ---x---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   |   | X |   |A2 |   |   |   |   |   | M | X |   |   |   |", " --- --- --- ---x---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   | X | X |   |   |   |   |   |A3 |   |   | X |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   | X |   |   |   | X |   |   |   |   |   |   |B1 |   |   |", " --- --- --- --- ---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   |   |   |   |B2 | X | X | X | X |   |   |   | X |   |   |", " --- --- --- --- --- --- ---x---x---x--- --- ---x---x---x--- --- ", "|   |   |   | C |   |   |   |   | X |   |   | X |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- ---x---x---x--- --- ", "|   |   |   |   |   |   | X |   |   |   |   | X |A0 |   |   |   |", " --- --- --- --- --- --- ---x---x---x--- --- ---x---x--- --- --- ", "|   | X | X |   |   |   | X | X | X |   |   | X | X | X |   |   |", " --- ---x---x--- --- ---x---x---x---x---x--- --- --- --- --- --- ", "|   | X | X |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x--- --- --- --- --- --- ", "|   |   |   | X |   | X |   |C0 |   |   |   |   | C |   |   |   |", " --- --- --- --- --- --- ---x---x---x--- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   | X | X | X |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   | X |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- ---x--- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   | X | X |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- ---x--- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   | X |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   | C |   | C |   | C |   |   | X | X | X |   |   |   |", " --- --- --- --- --- --- --- --- --- --- ---x---x--- --- --- --- ", "|   |   |   |   |   |   |   |   |   | X | X |A2 |   |   |   |   |", " --- --- --- --- --- --- --- --- --- ---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |   | X | X | X |   |   |   | X | X |   |", " --- --- --- --- --- --- --- ---x---x---x---x---x---x---x--- --- ", "|   |   | X |B0 | X |   | X | X |   |   |   | X |   |   | X |   |", " --- --- ---x---x--- --- ---x---x---x---x---x---x---x---x--- --- ", "|   | X | X |   |   |   | X | X | X |   |   | X |   |   | X |   |", " --- ---x---x---x---x--- ---x---x---x---x---x---x---x---x--- --- ", "|   | X | X |   |   |   | X |C2 |   |   | X |A1 |   |   |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   | X | X |A1 |   |   |   |   |   |   | X |   |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |   | X |   |   |   |   | X | X |   |   |", " --- --- --- ---x---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   | X |   |   |   | X | X |   |   |   | X | X |   |   |   |", " --- --- ---x---x--- --- --- ---x---x---x---x--- --- --- --- --- ", "|   |   | X | X | X |   |   | X |   |B2 | X | X |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " ---x---x---x---x--- --- --- --- --- --- --- ---x---x--- --- --- ", "|   |   | M |   | X |   |   |   |   | X |   |   | X | X |   |   |", " ---x---x---x--- --- --- --- --- ---x--- --- ---x---x---x---x--- ", "|   | X |   |   |   |   |   |B0 | X | X | X |   |C1 |   |   |   |", " --- --- --- --- ---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   | X | X | X |   |   |   |   | M |   |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x--- ---x--- --- --- ", "|   |   | X | X |   |   |   |   |C2 | X | X |   |   |   |   |   |", " --- --- --- ---x---x---x---x---x---x--- --- --- --- --- --- --- ", "|   |   |   | X |   | X |A1 |   |   | X |   | C |   |   |   |   |", " --- --- --- --- ---x---x---x---x---x---x--- --- --- --- --- --- ", "|   |   |   |   |   | X |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- ---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   |   |   | X |   |   |   | X |C1 |   |   | X |   | X |   |", " --- --- --- ---x---x---x--- --- ---x---x---x---x---x---x--- --- ", "|   |   |   |   |   |   | X |   |   |   |   |   |   | X |   |   |", " --- --- --- ---x---x---x--- --- ---x---x---x--- --- --- --- --- ", "|   |   |   | X |   |   |   |   | X |   |   |   |   |   |   |   |", " --- --- --- ---x---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   |   | X | X |   |   |   |   |   | M |   |B1 |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   |   |   |A2 |   |   |   |   |   |   |   |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   | X |   |   |   |   | X |A0 |   |   |A0 |   | X |   |   |", " --- --- --- --- ---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   |   |   | X |   | X | X |   |   |   |   |   |   |   |   |", " --- --- --- --- --- ---x---x---x---x--- ---x---x--- --- --- --- ", "|   |   | I |   |   |   | X |   | X |   |   | X | X |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   | X |   | X | X |B0 |   |   |   |", " --- --- --- --- --- --- --- --- ---x---x---x---x---x--- --- --- ", "|   |   |   |   |   | X |   |   | X | X | X |   |   | X | X |   |", " --- --- --- --- --- ---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   | X | X |   |   |A2 |   |   |   |   |   |   |   |", " --- --- --- --- ---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   |   |   |   |   |   | X |   |   |   | X |   |", " --- --- --- ---x---x---x---x---x---x---x--- --- --- ---x--- --- ", "|   |   | X |   | X | K |   |   |   |   | X |   |   | X |   |   |", " --- --- ---x---x---x---x---x---x---x--- --- --- --- --- --- --- ", "|   |   |   |   |   |A2 |   |   |   |   |   |   |   |   |   |   |", " --- --- ---x---x---x---x---x---x--- --- --- --- --- --- --- --- ", "|   |   |B3 |   |   |   |   |   | X |   |   |   |   | C |   |   |", " --- --- ---x---x---x---x---x---x--- --- --- --- --- --- --- --- ", "|   | X | X |   |   | M |   |B2 | X |   |   |   |   |   |   |   |", " --- ---x---x---x---x---x--- --- --- --- --- --- --- --- --- --- ", "|   | X |C1 |   |   |   |   |   |   |   | X |   |   |   |   |   |", " --- ---x---x---x---x--- --- --- --- --- ---x---x--- --- --- --- ", "|   |   |   |   |   |   |   | C |   |   |   |   | X | X |   |   |", " --- ---x---x---x--- --- --- --- --- ---x---x---x---x--- --- --- ", "|   | X |A1 |   | X |   |   |   |   |   |   |C3 | X |   |   |   |", " --- ---x---x---x--- --- --- --- --- ---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   | M |   |   |   |", " --- ---x---x---x--- --- --- --- --- ---x---x---x--- --- --- --- ", "|   | X |   | X | X |   |   |   |   | X |   | X | X |   |   |   |", " --- --- ---x---x---x--- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   | X |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   | X | X |   |   | X | X |   |   | X | X |   |   | X | X |   |", " --- ---x---x---x---x---x---x--- ---x---x---x---x---x---x--- --- ", "|   | X |C1 |   | X |   |   |   |   |   |   | X |   |C3 | X |   |", " --- ---x---x---x--- --- --- --- --- --- --- ---x---x---x--- --- ", "|   |   |   |   |   | C |   |   |   | C |   |   |   |   |   |   |", " --- ---x---x--- --- --- --- --- --- --- --- --- ---x---x--- --- ", "|   |   | X |   |   |   |   |   |   |   |   |   |   | X |   |   |", " --- ---x--- --- --- --- --- --- --- --- --- --- --- ---x--- --- ", "|   | X |   |   |   | X |   |   |   | X | X |   |   |   | X |   |", " --- ---x--- --- --- ---x---x---x---x---x--- --- --- ---x--- --- ", "|   | X |   |   |   |   |   | X |   |   |   |   |   |   | X |   |", " --- ---x--- --- --- ---x---x---x---x---x--- --- --- ---x--- --- ", "|   |   |   |   |   |   |   | X |A1 |   |B1 |   |   |   |   |   |", " --- --- --- --- --- ---x---x---x---x---x--- --- --- --- --- --- ", "|   |   |   |   |   |B3 |   |A3 | X |   |   |   |   |   |   |   |", " --- ---x--- --- --- ---x---x---x---x---x--- --- --- ---x--- --- ", "|   | X |   |   |   |   |   |   | X |   |   |   |   |   | X |   |", " --- ---x--- --- --- ---x---x---x---x---x--- --- --- ---x--- --- ", "|   | X |   |   |   | X | X |   |   |   | X |   |   |   | X |   |", " --- ---x--- --- --- --- --- --- --- --- --- --- --- ---x--- --- ", "|   |   | X |   |   |   |   |   |   |   |   |   |   | X |   |   |", " --- ---x---x--- --- --- --- --- --- --- --- ---x---x---x--- --- ", "|   |   |   |   |   | C |   |   |   | C |   |   |   |   |   |   |", " --- ---x---x---x--- --- --- --- --- --- --- ---x---x---x--- --- ", "|   | X |C1 |   | X |   |   |   |   |   |   | X |   |C3 | X |   |", " --- ---x---x---x---x---x---x--- ---x---x---x---x---x---x--- --- ", "|   | X | X |   |   | X | X |   |   | X | X |   |   | X | X |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   | X |   |   | X | X |   |   |   |   |   |", " --- --- --- --- --- ---x---x--- ---x---x--- --- --- --- --- --- ", "|   |   |   |   | X |   |   |B0 |   | X |   |   | X |   |   |   |", " --- --- --- ---x---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   | X |   | X | X |   |   |   |   |   |   | X |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   | X |   |   |   |   |   |   |   |A2 |   |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   |   |   |   |C1 |   | X |   |   |   |   | X |   | X |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   | X | X | M |   |   |   | X | X |   |   |   |   |   | X |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   | M | X | X | X | X |   |   |   |B1 |   |   |", " --- --- ---x---x---x---x---x--- ---x---x---x---x---x--- --- --- ", "|   |   |B3 |   |   |   |   | X | X | X |   | M |   |   |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   | X | X |   |   | X | X | X | X |   |   |   |   | X |   |   |", " --- ---x---x---x---x---x---x---x---x---x---x---x---x---x--- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |A3 | X |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   | X |A1 |   |   |   |   | X | K |   |   |   |   |   |   |", " --- --- ---x---x---x---x---x---x---x---x---x---x---x--- --- --- ", "|   |   | X | X |   |   | M |   |   |   |   | X |   | X |   |   |", " --- --- --- ---x---x---x---x---x---x---x---x---x--- --- --- --- ", "|   |   |   | X |   | X | X |   |B2 |   | X | X |   |   |   |   |", " --- --- --- --- --- ---x---x--- ---x---x--- --- --- --- --- --- ", "|   |   |   |   |   |   | X |   |   | X | X |   |   | I |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- "}};
        this.playerBaseTemplates = new String[][]{{" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   | X | X |   |   | X | X |   |", " --- ---x---x---x---x---x--- --- ", "|   | X |   |   |   | M |   |   |", " --- ---x---x---x---x---x--- --- ", "|   |   |   |A1 |   | X | X |   |", " --- ---x---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- ---x---x---x---x--- --- ", "|   | X | X |   |B2 |   | X |   |", " --- ---x---x--- --- ---x--- --- ", "|   | X |   |   |   |   | X |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   | X |B0 | X |   | C |   |", " --- --- ---x---x---x--- --- --- ", "|   |   |   |   |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   | X |   |   |   | X | X |   |", " --- ---x---x---x---x---x--- --- ", "|   | X |A1 |   |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   | X |   |   |   |C3 | X |   |", " --- --- ---x---x---x---x--- --- ", "|   |   |   | X | X | X | X |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   | X | X |   | C |   |", " ---x---x---x---x---x--- --- --- ", "|   | X |   |   |A2 |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   | X |   | X |   | M |   |   |", " --- --- --- ---x---x---x--- --- ", "|   |   |   |   |   |   | X |   |", " --- --- --- ---x---x---x--- --- ", "|   | X |   |   |   |C3 | X |   |", " --- ---x---x---x---x---x--- --- ", "|   |   | X | X |B2 |   | X |   |", " --- --- --- --- --- ---x--- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}};
        this.rarePropTemplates = new String[][]{{" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   | X |   |   | X |   |   |", " --- --- ---x---x---x--- --- --- ", "|   | X | X |   |   | X | X |   |", " --- ---x---x---x---x---x--- --- ", "|   | X |   |   |C3 | X | X |   |", " --- --- ---x---x---x--- --- --- ", "|   |   |   |   |   | X |   |   |", " --- --- --- ---x---x---x--- --- ", "|   | I |   | X |   | X | X |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   | C |   |   |", " --- --- ---x---x--- --- --- --- ", "|   | X |   | M |   |   |   |   |", " --- ---x---x---x---x--- --- --- ", "|   |   |   |   |   | X |   |   |", " --- --- ---x---x---x--- --- --- ", "|   |   |   |B2 |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   | C |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   | C |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- ---x--- --- --- --- ", "|   |   |   |   |   | X | C |   |", " --- --- --- ---x---x--- --- --- ", "|   |   |   | X |   |   | X |   |", " --- --- ---x---x---x---x--- --- ", "|   |   |   | X |A2 |   |   |   |", " --- --- --- ---x---x---x--- --- ", "|   |   |   | X |   |   |   |   |", " --- --- ---x---x---x---x--- --- ", "|   |   | X |   |   |   |   |   |", " --- --- --- --- --- ---x--- --- ", "|   |   |   |   |   | X | X |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   | X | X | C |   |   |   |   |", " --- ---x--- --- --- --- --- --- ", "|   |   |   |   |   | X | X |   |", " ---x---x---x---x---x---x--- --- ", "|   | X |   |   |   |   |B1 |   |", " --- ---x---x---x---x---x--- --- ", "|   |   | K |A1 |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   |   |   |   |   | X | X |   |", " --- ---x---x---x---x--- --- --- ", "|   | X |   | X |   | X |   |   |", " --- --- --- ---x--- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- ---x--- --- --- --- --- ", "|   |   |   | X |   |   | I |   |", " --- ---x---x--- --- --- --- --- ", "|   | X |   |   |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   |   |   |   | M |   | X |   |", " ---x---x---x---x---x---x--- --- ", "|   |   |A0 |   |   |   | X |   |", " ---x---x---x---x---x---x--- --- ", "|   | X | X |C1 |   |   |   |   |", " --- --- ---x---x---x---x--- --- ", "|   |   | X | X |   |   | X |   |", " --- --- ---x--- --- ---x--- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- ---x--- --- --- --- --- ", "|   | X |   | X |   | X |   |   |", " --- ---x---x---x---x---x--- --- ", "|   |   | X | X |   | X | X |   |", " --- ---x---x---x--- ---x--- --- ", "|   |   |   |C2 |   |   |   |   |", " --- ---x---x---x---x---x--- --- ", "|   | X |   |   |   |   |B1 |   |", " ---x---x---x---x---x---x--- --- ", "|   | X |   | M |   |   |   |   |", " ---x--- ---x---x---x--- --- --- ", "|   |   |   | X | X |   | C |   |", " --- --- --- ---x--- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}};
        this.commonPropTemplates = new String[][]{{" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   | C |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}, {" --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   | I |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- ", "|   |   |   |   |   |   |   |   |", " --- --- --- --- --- --- --- --- "}};
    }

    @Override
    public String getId() {
        return "tsf";
    }

    @Override
    public FactionList getFactions() {
        return GameConfig.cleanContent ? this.b : this.a;
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


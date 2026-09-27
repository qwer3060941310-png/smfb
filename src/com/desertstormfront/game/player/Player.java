/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.game.player.Controller;
import com.desertstormfront.game.player.Faction;
import com.desertstormfront.game.player.FogOfWar;
import com.desertstormfront.game.player.PlayerStatistics;
import com.desertstormfront.game.player.Team;
import com.desertstormfront.game.player.UnitGroupSet;

/**
 * One side in a battle: its units, its resources, and its view of the map.
 *
 * <p>Fog of war is per player and has two independent halves, each of which a game may switch off:
 * exploration (whether a tile has ever been seen - remembered for the rest of the game) and fog
 * (whether it is visible right now). Both are held in this player's {@link FogOfWar} as corner
 * grids over the terrain; {@code World.setupPlayers} wires the player's exploration and fog
 * settings into that object.
 */
public strictfp final class Player {
    private int id;
    private Controller controller;
    private long c;
    private boolean alive;
    private Faction faction;
    private Team team;
    private long resources;
    private long h;
    private UnitTypeList availableUnitTypes;
    private FogOfWar fogOfWar;
    private UnitGroupSet unitGroups;
    private boolean explorationEnabled;
    private boolean fogEnabled;
    private int n;
    private PlayerStatistics statistics;

    public static Player create(int i0, Faction faction, Team team, long l3, long l5, boolean bl, boolean bl2, UnitTypeList unitTypeList, FogOfWar fogOfWar) {
        Player player = new Player();
        player.setId(i0);
        player.setController((Controller)null);
        player.a(0L);
        player.setAlive(true);
        player.setFaction(faction);
        player.setTeam(team);
        player.setResources(l3);
        player.setIncomePerBuilding(l5);
        player.setAvailableUnitTypes(unitTypeList);
        player.setFogOfWar(fogOfWar);
        player.setUnitGroups(new UnitGroupSet());
        player.setExplorationEnabled(bl);
        player.setFogEnabled(bl2);
        player.b(0);
        player.setStatistics(new PlayerStatistics());
        return player;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i1) {
        this.id = i1;
    }

    public final Controller getController() {
        return this.controller;
    }

    public final void setController(Controller controller) {
        this.controller = controller;
    }

    public final long c() {
        return this.c;
    }

    public final void a(long l1) {
        this.c = l1;
    }

    public final boolean isAlive() {
        return this.alive;
    }

    public final void setAlive(boolean bl) {
        this.alive = bl;
    }

    public final Faction getFaction() {
        return this.faction;
    }

    public final void setFaction(Faction faction) {
        this.faction = faction;
    }

    public final Team getTeam() {
        return this.team;
    }

    public final void setTeam(Team team) {
        this.team = team;
    }

    public final boolean isEnemyOf(Player player) {
        return this != player && player != null && (this.team == null || this.team != player.getTeam());
    }

    public final long getResources() {
        return this.resources;
    }

    public final void setResources(long l1) {
        this.resources = l1;
    }

    public final UnitTypeList getAvailableUnitTypes() {
        return this.availableUnitTypes;
    }

    public final void setAvailableUnitTypes(UnitTypeList unitTypeList) {
        this.availableUnitTypes = unitTypeList;
    }

    public final FogOfWar getFogOfWar() {
        return this.fogOfWar;
    }

    public final void setFogOfWar(FogOfWar fogOfWar) {
        this.fogOfWar = fogOfWar;
    }

    public UnitGroupSet getUnitGroups() {
        return this.unitGroups;
    }

    public void setUnitGroups(UnitGroupSet unitGroupSet) {
        this.unitGroups = unitGroupSet;
    }

    public final String getName() {
        return this.faction.getName();
    }

    public final String toString() {
        return this.faction.getName();
    }

    public long getIncomePerBuilding() {
        return this.h;
    }

    public void setIncomePerBuilding(long l1) {
        this.h = l1;
    }

    public final boolean isExplorationEnabled() {
        return this.explorationEnabled;
    }

    public final void setExplorationEnabled(boolean bl) {
        this.explorationEnabled = bl;
    }

    public final boolean isFogEnabled() {
        return this.fogEnabled;
    }

    public final void setFogEnabled(boolean bl) {
        this.fogEnabled = bl;
    }

    public final int o() {
        return this.n;
    }

    public final void b(int i1) {
        this.n = i1;
    }

    public PlayerStatistics getStatistics() {
        return this.statistics;
    }

    public void setStatistics(PlayerStatistics playerStatistics) {
        this.statistics = playerStatistics;
    }
}


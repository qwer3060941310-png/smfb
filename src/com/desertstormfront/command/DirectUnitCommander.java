/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.command;

import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.game.World;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.UnitGroup;

public final class DirectUnitCommander
extends UnitCommander {
    private World world;
    private Player player;

    public DirectUnitCommander(World world, Player player) {
        this.world = world;
        this.player = player;
    }

    @Override
    public final World getWorld() {
        return this.world;
    }

    @Override
    public final Player getPlayer() {
        return this.player;
    }

    @Override
    public final boolean isCommandable(Unit unit) {
        return DirectUnitCommander.isCommandable(this.world, this.player, unit);
    }

    @Override
    public final boolean hasCommandableUnit(UnitList unitList) {
        return DirectUnitCommander.hasCommandableUnit(this.world, this.player, unitList);
    }

    @Override
    public final boolean moveUnitTo(Unit unit, float f2, float f3) {
        return DirectUnitCommander.moveUnitTo(this.world, this.player, unit, f2, f3);
    }

    @Override
    public final boolean moveUnitsTo(UnitList unitList, float f2, float f3) {
        return DirectUnitCommander.moveUnitsTo(this.world, this.player, unitList, f2, f3);
    }

    @Override
    public final boolean targetUnit(Unit unit, Unit unit2) {
        return DirectUnitCommander.targetUnit(this.world, this.player, unit, unit2);
    }

    @Override
    public final boolean targetUnits(UnitList unitList, Unit unit) {
        return DirectUnitCommander.targetUnits(this.world, this.player, unitList, unit);
    }

    @Override
    public final boolean b(Unit unit) {
        return DirectUnitCommander.b(this.world, this.player, unit);
    }

    @Override
    public final boolean attackMoveUnitTo(Unit unit, float f2, float f3) {
        return DirectUnitCommander.attackMoveUnitTo(this.world, this.player, unit, f2, f3);
    }

    @Override
    public final boolean attackUnit(Unit unit, Unit unit2) {
        return DirectUnitCommander.attackUnit(this.world, this.player, unit, unit2);
    }

    @Override
    public final boolean canRepair(Unit unit) {
        return DirectUnitCommander.canRepair(this.world, this.player, unit);
    }

    @Override
    public final boolean hasRepairableUnit(UnitList unitList) {
        return DirectUnitCommander.hasRepairableUnit(this.world, this.player, unitList);
    }

    @Override
    public final boolean repairUnit(Unit unit) {
        return DirectUnitCommander.repairUnit(this.world, this.player, unit);
    }

    @Override
    public final boolean repairUnits(UnitList unitList) {
        return DirectUnitCommander.repairUnits(this.world, this.player, unitList);
    }

    @Override
    public final boolean canSetAuto(Unit unit) {
        return DirectUnitCommander.canSetAuto(this.world, this.player, unit);
    }

    @Override
    public final boolean setAuto(Unit unit) {
        return DirectUnitCommander.setAuto(this.world, this.player, unit);
    }

    @Override
    public final boolean toggleHostedAuto(Unit unit) {
        return DirectUnitCommander.toggleHostedAuto(this.world, this.player, unit);
    }

    @Override
    public final boolean toggleHostedAutoUnits(UnitList unitList) {
        return DirectUnitCommander.toggleHostedAutoUnits(this.world, this.player, unitList);
    }

    @Override
    public final boolean canStop(Unit unit) {
        return DirectUnitCommander.canStop(this.world, this.player, unit);
    }

    @Override
    public final boolean canStopAny(UnitList unitList) {
        return DirectUnitCommander.canStopAny(this.world, this.player, unitList);
    }

    @Override
    public final boolean stopUnit(Unit unit) {
        return DirectUnitCommander.stopUnit(this.world, this.player, unit);
    }

    @Override
    public final boolean stopUnits(UnitList unitList) {
        return DirectUnitCommander.stopUnits(this.world, this.player, unitList);
    }

    @Override
    public final boolean formSquad(UnitGroup unitGroup, UnitList unitList) {
        return DirectUnitCommander.formSquad(this.world, this.player, unitGroup, unitList);
    }

    @Override
    public final boolean canAssignSquad(UnitList unitList) {
        return DirectUnitCommander.canAssignSquad(this.world, this.player, unitList);
    }

    @Override
    public final boolean assignSquad(UnitList unitList) {
        return DirectUnitCommander.assignSquad(this.world, this.player, unitList);
    }

    @Override
    public final boolean leaveSquad(Unit unit) {
        return DirectUnitCommander.leaveSquad(this.world, this.player, unit);
    }

    @Override
    public final boolean leaveSquadUnits(UnitList unitList) {
        return DirectUnitCommander.leaveSquadUnits(this.world, this.player, unitList);
    }

    @Override
    public final boolean setRallyPoint(Unit unit, float f2, float f3) {
        return DirectUnitCommander.setRallyPoint(this.world, this.player, unit, f2, f3);
    }

    @Override
    public final boolean clearRallyPoint(Unit unit) {
        return DirectUnitCommander.clearRallyPoint(this.world, this.player, unit);
    }

    @Override
    public final boolean canBuildUnit(Unit unit, UnitType unitType) {
        return DirectUnitCommander.canBuildUnit(this.world, this.player, unit, unitType);
    }

    @Override
    public final boolean buildUnit(Unit unit, UnitType unitType) {
        return DirectUnitCommander.buildUnit(this.world, this.player, unit, unitType);
    }

    @Override
    public final boolean cancelBuild(Unit unit) {
        return DirectUnitCommander.cancelBuild(this.world, this.player, unit);
    }
}


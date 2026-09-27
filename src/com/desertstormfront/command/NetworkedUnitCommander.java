/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.command;

import com.desertstormfront.command.AssignSquadCommand;
import com.desertstormfront.command.AttackGroundCommand;
import com.desertstormfront.command.AttackUnitCommand;
import com.desertstormfront.command.BuildUnitCommand;
import com.desertstormfront.command.CancelBuildCommand;
import com.desertstormfront.command.ClearRallyPointCommand;
import com.desertstormfront.command.FormSquadCommand;
import com.desertstormfront.command.LeaveSquadCommand;
import com.desertstormfront.command.LeaveSquadUnitsCommand;
import com.desertstormfront.command.MoveFormationCommand;
import com.desertstormfront.command.MoveToCommand;
import com.desertstormfront.command.RepairCommand;
import com.desertstormfront.command.RepairUnitsCommand;
import com.desertstormfront.command.SetAutoCommand;
import com.desertstormfront.command.SetRallyPointCommand;
import com.desertstormfront.command.StopCommand;
import com.desertstormfront.command.StopUnitsCommand;
import com.desertstormfront.command.TargetUnitCommand;
import com.desertstormfront.command.TargetUnitUnitsCommand;
import com.desertstormfront.command.ToggleHostedAutoCommand;
import com.desertstormfront.command.ToggleHostedAutoGroupCommand;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.game.World;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.UnitGroup;
import com.desertstormfront.session.impl.GameClient;

public final class NetworkedUnitCommander
extends UnitCommander {
    private GameClient gameClient;

    public NetworkedUnitCommander(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    @Override
    public final World getWorld() {
        return this.gameClient.getWorld();
    }

    @Override
    public final Player getPlayer() {
        return this.gameClient.getPlayer();
    }

    @Override
    public final boolean isCommandable(Unit unit) {
        return NetworkedUnitCommander.isCommandable(this.getWorld(), this.getPlayer(), unit);
    }

    @Override
    public final boolean hasCommandableUnit(UnitList unitList) {
        return NetworkedUnitCommander.hasCommandableUnit(this.getWorld(), this.getPlayer(), unitList);
    }

    public final boolean canMoveUnitTo(Unit unit, float f2, float f3) {
        return NetworkedUnitCommander.canMoveUnitTo(this.getWorld(), this.getPlayer(), unit, f2, f3);
    }

    public final boolean canAnyUnitMoveTo(UnitList unitList, float f2, float f3) {
        return NetworkedUnitCommander.canAnyUnitMoveTo(this.getWorld(), this.getPlayer(), unitList, f2, f3);
    }

    @Override
    public final boolean moveUnitTo(Unit unit, float f2, float f3) {
        if (this.canMoveUnitTo(unit, f2, f3)) {
            this.gameClient.sendCommand(MoveToCommand.create(this.getWorld(), this.getPlayer(), unit, f2, f3));
            return true;
        }
        return false;
    }

    @Override
    public final boolean moveUnitsTo(UnitList unitList, float f2, float f3) {
        if (this.canAnyUnitMoveTo(unitList, f2, f3)) {
            this.gameClient.sendCommand(MoveFormationCommand.create(this.getWorld(), this.getPlayer(), unitList, f2, f3));
            return true;
        }
        return false;
    }

    public final boolean canTargetUnit(Unit unit, Unit unit2) {
        return NetworkedUnitCommander.canTargetUnit(this.getWorld(), this.getPlayer(), unit, unit2);
    }

    public final boolean hasUnitThatCanTarget(UnitList unitList, Unit unit) {
        return NetworkedUnitCommander.hasUnitThatCanTarget(this.getWorld(), this.getPlayer(), unitList, unit);
    }

    @Override
    public final boolean targetUnit(Unit unit, Unit unit2) {
        if (this.canTargetUnit(unit, unit2)) {
            this.gameClient.sendCommand(TargetUnitCommand.create(this.getWorld(), this.getPlayer(), unit, unit2));
            return true;
        }
        return false;
    }

    @Override
    public final boolean targetUnits(UnitList unitList, Unit unit) {
        if (this.hasUnitThatCanTarget(unitList, unit)) {
            this.gameClient.sendCommand(TargetUnitUnitsCommand.create(this.getWorld(), this.getPlayer(), unitList, unit));
            return true;
        }
        return false;
    }

    @Override
    public final boolean b(Unit unit) {
        return NetworkedUnitCommander.b(this.getWorld(), this.getPlayer(), unit);
    }

    public final boolean canAttackMoveUnitTo(Unit unit, float f2, float f3) {
        return NetworkedUnitCommander.canAttackMoveUnitTo(this.getWorld(), this.getPlayer(), unit, f2, f3);
    }

    @Override
    public final boolean attackMoveUnitTo(Unit unit, float f2, float f3) {
        if (this.canAttackMoveUnitTo(unit, f2, f3)) {
            this.gameClient.sendCommand(AttackGroundCommand.create(this.getWorld(), this.getPlayer(), unit, f2, f3));
            return true;
        }
        return false;
    }

    public final boolean canAttackUnit(Unit unit, Unit unit2) {
        return NetworkedUnitCommander.canAttackUnit(this.getWorld(), this.getPlayer(), unit, unit2);
    }

    @Override
    public final boolean attackUnit(Unit unit, Unit unit2) {
        if (this.canAttackUnit(unit, unit2)) {
            this.gameClient.sendCommand(AttackUnitCommand.create(this.getWorld(), this.getPlayer(), unit, unit2));
            return true;
        }
        return false;
    }

    @Override
    public final boolean canRepair(Unit unit) {
        return NetworkedUnitCommander.canRepair(this.getWorld(), this.getPlayer(), unit);
    }

    @Override
    public final boolean hasRepairableUnit(UnitList unitList) {
        return NetworkedUnitCommander.hasRepairableUnit(this.getWorld(), this.getPlayer(), unitList);
    }

    @Override
    public final boolean repairUnit(Unit unit) {
        if (this.canRepair(unit)) {
            this.gameClient.sendCommand(RepairCommand.create(this.getWorld(), this.getPlayer(), unit));
            return true;
        }
        return false;
    }

    @Override
    public final boolean repairUnits(UnitList unitList) {
        if (this.hasRepairableUnit(unitList)) {
            this.gameClient.sendCommand(RepairUnitsCommand.create(this.getWorld(), this.getPlayer(), unitList));
            return true;
        }
        return false;
    }

    @Override
    public final boolean canSetAuto(Unit unit) {
        return NetworkedUnitCommander.canSetAuto(this.getWorld(), this.getPlayer(), unit);
    }

    @Override
    public final boolean setAuto(Unit unit) {
        if (this.canSetAuto(unit)) {
            this.gameClient.sendCommand(SetAutoCommand.create(this.getWorld(), this.getPlayer(), unit));
            return true;
        }
        return false;
    }

    public final boolean canToggleHostedAuto(Unit unit) {
        return NetworkedUnitCommander.canToggleHostedAuto(this.getWorld(), this.getPlayer(), unit);
    }

    public final boolean canToggleHostedAutoAny(UnitList unitList) {
        return NetworkedUnitCommander.canToggleHostedAutoAny(this.getWorld(), this.getPlayer(), unitList);
    }

    @Override
    public final boolean toggleHostedAuto(Unit unit) {
        if (this.canToggleHostedAuto(unit)) {
            this.gameClient.sendCommand(ToggleHostedAutoCommand.create(this.getWorld(), this.getPlayer(), unit));
            return true;
        }
        return false;
    }

    @Override
    public final boolean toggleHostedAutoUnits(UnitList unitList) {
        if (this.canToggleHostedAutoAny(unitList)) {
            this.gameClient.sendCommand(ToggleHostedAutoGroupCommand.create(this.getWorld(), this.getPlayer(), unitList));
            return true;
        }
        return false;
    }

    @Override
    public final boolean canStop(Unit unit) {
        return NetworkedUnitCommander.canStop(this.getWorld(), this.getPlayer(), unit);
    }

    @Override
    public final boolean canStopAny(UnitList unitList) {
        return NetworkedUnitCommander.canStopAny(this.getWorld(), this.getPlayer(), unitList);
    }

    @Override
    public final boolean stopUnit(Unit unit) {
        if (this.canStop(unit)) {
            this.gameClient.sendCommand(StopCommand.create(this.getWorld(), this.getPlayer(), unit));
            return true;
        }
        return false;
    }

    @Override
    public final boolean stopUnits(UnitList unitList) {
        if (this.canStopAny(unitList)) {
            this.gameClient.sendCommand(StopUnitsCommand.create(this.getWorld(), this.getPlayer(), unitList));
            return true;
        }
        return false;
    }

    public final boolean canFormSquad(UnitGroup unitGroup, UnitList unitList) {
        return NetworkedUnitCommander.canFormSquad(this.getWorld(), this.getPlayer(), unitGroup, unitList);
    }

    @Override
    public final boolean formSquad(UnitGroup unitGroup, UnitList unitList) {
        if (this.canFormSquad(unitGroup, unitList)) {
            this.gameClient.sendCommand(AssignSquadCommand.create(this.getWorld(), this.getPlayer(), unitGroup, unitList));
            return true;
        }
        return false;
    }

    @Override
    public final boolean canAssignSquad(UnitList unitList) {
        return NetworkedUnitCommander.canAssignSquad(this.getWorld(), this.getPlayer(), unitList);
    }

    @Override
    public final boolean assignSquad(UnitList unitList) {
        if (this.canAssignSquad(unitList)) {
            this.gameClient.sendCommand(FormSquadCommand.create(this.getWorld(), this.getPlayer(), unitList));
            return true;
        }
        return false;
    }

    public final boolean hasSquad(Unit unit) {
        return NetworkedUnitCommander.hasSquad(this.getWorld(), this.getPlayer(), unit);
    }

    public final boolean hasSquadAny(UnitList unitList) {
        return NetworkedUnitCommander.hasSquadAny(this.getWorld(), this.getPlayer(), unitList);
    }

    @Override
    public final boolean leaveSquad(Unit unit) {
        if (this.hasSquad(unit)) {
            this.gameClient.sendCommand(LeaveSquadCommand.create(this.getWorld(), this.getPlayer(), unit));
            return true;
        }
        return false;
    }

    @Override
    public final boolean leaveSquadUnits(UnitList unitList) {
        if (this.hasSquadAny(unitList)) {
            this.gameClient.sendCommand(LeaveSquadUnitsCommand.create(this.getWorld(), this.getPlayer(), unitList));
            return true;
        }
        return false;
    }

    public final boolean canSetRallyPoint(Unit unit, float f2, float f3) {
        return NetworkedUnitCommander.canSetRallyPoint(this.getWorld(), this.getPlayer(), unit, f2, f3);
    }

    @Override
    public final boolean setRallyPoint(Unit unit, float f2, float f3) {
        if (this.canSetRallyPoint(unit, f2, f3)) {
            this.gameClient.sendCommand(SetRallyPointCommand.create(this.getWorld(), this.getPlayer(), unit, f2, f3));
            return true;
        }
        return false;
    }

    public final boolean isImmobile(Unit unit) {
        return NetworkedUnitCommander.isImmobile(this.getWorld(), this.getPlayer(), unit);
    }

    @Override
    public final boolean clearRallyPoint(Unit unit) {
        if (this.isImmobile(unit)) {
            this.gameClient.sendCommand(ClearRallyPointCommand.create(this.getWorld(), this.getPlayer(), unit));
            return true;
        }
        return false;
    }

    @Override
    public final boolean canBuildUnit(Unit unit, UnitType unitType) {
        return NetworkedUnitCommander.canBuildUnit(this.getWorld(), this.getPlayer(), unit, unitType);
    }

    @Override
    public final boolean buildUnit(Unit unit, UnitType unitType) {
        if (this.canBuildUnit(unit, unitType)) {
            this.gameClient.sendCommand(BuildUnitCommand.create(this.getWorld(), this.getPlayer(), unit, unitType));
            return true;
        }
        return false;
    }

    public final boolean canCancelBuild(Unit unit) {
        return NetworkedUnitCommander.canCancelBuild(this.getWorld(), this.getPlayer(), unit);
    }

    @Override
    public final boolean cancelBuild(Unit unit) {
        if (this.canCancelBuild(unit)) {
            this.gameClient.sendCommand(CancelBuildCommand.create(this.getWorld(), this.getPlayer(), unit));
            return true;
        }
        return false;
    }
}


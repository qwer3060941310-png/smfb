/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.command;

import com.desertstormfront.command.GameCommand;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.game.World;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.player.Player;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;

public final class AttackUnitCommand
extends GameCommand {
    private int playerId;
    private int unitId;
    private int targetUnitId;

    public static AttackUnitCommand create(World world, Player player, Unit unit, Unit unit2) {
        AttackUnitCommand attackUnitCommand = new AttackUnitCommand();
        attackUnitCommand.playerId = player.getId();
        attackUnitCommand.unitId = unit.getId();
        attackUnitCommand.targetUnitId = unit2.getId();
        return attackUnitCommand;
    }

    @Override
    public void execute(World world) {
        Player player = world.getPlayers().getById(this.playerId);
        if (player != null) {
            Unit unit = world.getUnits().getUnitById(this.unitId);
            Unit unit2 = world.getUnits().getUnitById(this.targetUnitId);
            UnitCommander.attackUnit(world, player, unit, unit2);
        }
    }

    @Override
    protected void readFields(DataReader dataReader) {
        this.playerId = dataReader.readInt();
        this.unitId = dataReader.readInt();
        this.targetUnitId = dataReader.readInt();
    }

    @Override
    protected void writeFields(DataWriter dataWriter) {
        dataWriter.writeInt(this.playerId);
        dataWriter.writeInt(this.unitId);
        dataWriter.writeInt(this.targetUnitId);
    }
}


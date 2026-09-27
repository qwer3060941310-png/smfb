/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.command;

import com.desertstormfront.command.GameCommand;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.game.World;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.Player;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;

public final class BuildUnitCommand
extends GameCommand {
    private int playerId;
    private int unitId;
    private int unitTypeId;

    public static BuildUnitCommand create(World world, Player player, Unit unit, UnitType unitType) {
        BuildUnitCommand buildUnitCommand = new BuildUnitCommand();
        buildUnitCommand.playerId = player.getId();
        buildUnitCommand.unitId = unit.getId();
        buildUnitCommand.unitTypeId = unitType.getId();
        return buildUnitCommand;
    }

    @Override
    public void execute(World world) {
        Player player = world.getPlayers().getById(this.playerId);
        if (player != null) {
            Unit unit = world.getUnits().getUnitById(this.unitId);
            UnitType unitType = (UnitType)world.getMapDefinition().getUnitTypes().get(this.unitTypeId);
            UnitCommander.buildUnit(world, player, unit, unitType);
        }
    }

    @Override
    protected void readFields(DataReader dataReader) {
        this.playerId = dataReader.readInt();
        this.unitId = dataReader.readInt();
        this.unitTypeId = dataReader.readInt();
    }

    @Override
    protected void writeFields(DataWriter dataWriter) {
        dataWriter.writeInt(this.playerId);
        dataWriter.writeInt(this.unitId);
        dataWriter.writeInt(this.unitTypeId);
    }
}


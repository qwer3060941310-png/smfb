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

public final class SetRallyPointCommand
extends GameCommand {
    private int playerId;
    private int unitId;
    private float rallyX;
    private float rallyY;

    public static SetRallyPointCommand create(World world, Player player, Unit unit, float f3, float f4) {
        SetRallyPointCommand setRallyPointCommand = new SetRallyPointCommand();
        setRallyPointCommand.playerId = player.getId();
        setRallyPointCommand.unitId = unit.getId();
        setRallyPointCommand.rallyX = f3;
        setRallyPointCommand.rallyY = f4;
        return setRallyPointCommand;
    }

    @Override
    public void execute(World world) {
        Player player = world.getPlayers().getById(this.playerId);
        if (player != null) {
            Unit unit = world.getUnits().getUnitById(this.unitId);
            UnitCommander.setRallyPoint(world, player, unit, this.rallyX, this.rallyY);
        }
    }

    @Override
    protected void readFields(DataReader dataReader) {
        this.playerId = dataReader.readInt();
        this.unitId = dataReader.readInt();
        this.rallyX = dataReader.readFloat();
        this.rallyY = dataReader.readFloat();
    }

    @Override
    protected void writeFields(DataWriter dataWriter) {
        dataWriter.writeInt(this.playerId);
        dataWriter.writeInt(this.unitId);
        dataWriter.writeFloat(this.rallyX);
        dataWriter.writeFloat(this.rallyY);
    }
}


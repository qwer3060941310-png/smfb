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

public final class SetAutoCommand
extends GameCommand {
    private int playerId;
    private int unitId;

    public static SetAutoCommand create(World world, Player player, Unit unit) {
        SetAutoCommand setAutoCommand = new SetAutoCommand();
        setAutoCommand.playerId = player.getId();
        setAutoCommand.unitId = unit.getId();
        return setAutoCommand;
    }

    @Override
    public void execute(World world) {
        Player player = world.getPlayers().getById(this.playerId);
        if (player != null) {
            Unit unit = world.getUnits().getUnitById(this.unitId);
            UnitCommander.setAuto(world, player, unit);
        }
    }

    @Override
    protected void readFields(DataReader dataReader) {
        this.playerId = dataReader.readInt();
        this.unitId = dataReader.readInt();
    }

    @Override
    protected void writeFields(DataWriter dataWriter) {
        dataWriter.writeInt(this.playerId);
        dataWriter.writeInt(this.unitId);
    }
}


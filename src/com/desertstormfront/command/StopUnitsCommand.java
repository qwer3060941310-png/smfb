/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.command;

import com.desertstormfront.command.GameCommand;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.game.World;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.player.Player;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;

public final class StopUnitsCommand
extends GameCommand {
    private int playerId;
    private int[] unitIds;

    public static StopUnitsCommand create(World world, Player player, UnitList unitList) {
        StopUnitsCommand stopUnitsCommand = new StopUnitsCommand();
        stopUnitsCommand.playerId = player.getId();
        stopUnitsCommand.unitIds = new int[unitList.size()];
        int i4 = 0;
        while (i4 < unitList.size()) {
            stopUnitsCommand.unitIds[i4] = ((Unit)unitList.get(i4)).getId();
            ++i4;
        }
        return stopUnitsCommand;
    }

    @Override
    public void execute(World world) {
        Player player = world.getPlayers().getById(this.playerId);
        if (player != null) {
            UnitList unitList = new UnitList(this.unitIds.length);
            int i4 = 0;
            while (i4 < this.unitIds.length) {
                unitList.add(world.getUnits().getUnitById(this.unitIds[i4]));
                ++i4;
            }
            UnitCommander.stopUnits(world, player, unitList);
        }
    }

    @Override
    protected void readFields(DataReader dataReader) {
        this.playerId = dataReader.readInt();
        this.unitIds = new int[dataReader.readInt()];
        int i2 = 0;
        while (i2 < this.unitIds.length) {
            this.unitIds[i2] = dataReader.readInt();
            ++i2;
        }
    }

    @Override
    protected void writeFields(DataWriter dataWriter) {
        dataWriter.writeInt(this.playerId);
        dataWriter.writeInt(this.unitIds.length);
        int i2 = 0;
        while (i2 < this.unitIds.length) {
            dataWriter.writeInt(this.unitIds[i2]);
            ++i2;
        }
    }
}


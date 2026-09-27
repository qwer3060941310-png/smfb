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

public final class MoveFormationCommand
extends GameCommand {
    private int playerId;
    private int[] unitIds;
    private float targetX;
    private float targetY;

    public static MoveFormationCommand create(World world, Player player, UnitList unitList, float f3, float f4) {
        MoveFormationCommand moveFormationCommand = new MoveFormationCommand();
        moveFormationCommand.playerId = player.getId();
        moveFormationCommand.unitIds = new int[unitList.size()];
        int i6 = 0;
        while (i6 < unitList.size()) {
            moveFormationCommand.unitIds[i6] = ((Unit)unitList.get(i6)).getId();
            ++i6;
        }
        moveFormationCommand.targetX = f3;
        moveFormationCommand.targetY = f4;
        return moveFormationCommand;
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
            UnitCommander.moveUnitsTo(world, player, unitList, this.targetX, this.targetY);
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
        this.targetX = dataReader.readFloat();
        this.targetY = dataReader.readFloat();
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
        dataWriter.writeFloat(this.targetX);
        dataWriter.writeFloat(this.targetY);
    }
}


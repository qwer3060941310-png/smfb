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

public final class AttackGroundCommand
extends GameCommand {
    private int playerId;
    private int unitId;
    private float targetX;
    private float targetY;

    public static AttackGroundCommand create(World world, Player player, Unit unit, float f3, float f4) {
        AttackGroundCommand attackGroundCommand = new AttackGroundCommand();
        attackGroundCommand.playerId = player.getId();
        attackGroundCommand.unitId = unit.getId();
        attackGroundCommand.targetX = f3;
        attackGroundCommand.targetY = f4;
        return attackGroundCommand;
    }

    @Override
    public void execute(World world) {
        Player player = world.getPlayers().getById(this.playerId);
        if (player != null) {
            Unit unit = world.getUnits().getUnitById(this.unitId);
            UnitCommander.attackMoveUnitTo(world, player, unit, this.targetX, this.targetY);
        }
    }

    @Override
    protected void readFields(DataReader dataReader) {
        this.playerId = dataReader.readInt();
        this.unitId = dataReader.readInt();
        this.targetX = dataReader.readFloat();
        this.targetY = dataReader.readFloat();
    }

    @Override
    protected void writeFields(DataWriter dataWriter) {
        dataWriter.writeInt(this.playerId);
        dataWriter.writeInt(this.unitId);
        dataWriter.writeFloat(this.targetX);
        dataWriter.writeFloat(this.targetY);
    }
}


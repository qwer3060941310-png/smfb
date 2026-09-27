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
import com.desertstormfront.game.player.UnitGroup;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;

public final class AssignSquadCommand
extends GameCommand {
    private int playerId;
    private int groupId;
    private int[] unitIds;

    public static AssignSquadCommand create(World world, Player player, UnitGroup unitGroup, UnitList unitList) {
        AssignSquadCommand assignSquadCommand = new AssignSquadCommand();
        assignSquadCommand.playerId = player.getId();
        assignSquadCommand.groupId = unitGroup.getId();
        assignSquadCommand.unitIds = new int[unitList.size()];
        int i5 = 0;
        while (i5 < unitList.size()) {
            assignSquadCommand.unitIds[i5] = ((Unit)unitList.get(i5)).getId();
            ++i5;
        }
        return assignSquadCommand;
    }

    @Override
    public void execute(World world) {
        Player player = world.getPlayers().getById(this.playerId);
        if (player != null) {
            UnitList unitList = new UnitList(this.unitIds.length);
            int n = 0;
            while (n < this.unitIds.length) {
                unitList.add(world.getUnits().getUnitById(this.unitIds[n]));
                ++n;
            }
            UnitGroup unitGroup = player.getUnitGroups().getGroup(this.groupId);
            UnitCommander.formSquad(world, player, unitGroup, unitList);
        }
    }

    @Override
    protected void readFields(DataReader dataReader) {
        this.playerId = dataReader.readInt();
        this.groupId = dataReader.readInt();
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
        dataWriter.writeInt(this.groupId);
        dataWriter.writeInt(this.unitIds.length);
        int i2 = 0;
        while (i2 < this.unitIds.length) {
            dataWriter.writeInt(this.unitIds[i2]);
            ++i2;
        }
    }
}


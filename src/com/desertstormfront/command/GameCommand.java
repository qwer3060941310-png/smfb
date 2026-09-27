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
import com.desertstormfront.game.World;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public abstract class GameCommand {
    private static final List commandClasses = Arrays.asList(ToggleHostedAutoCommand.class, ToggleHostedAutoGroupCommand.class, SetAutoCommand.class, BuildUnitCommand.class, CancelBuildCommand.class, AttackUnitCommand.class, AttackGroundCommand.class, SetRallyPointCommand.class, RepairCommand.class, RepairUnitsCommand.class, FormSquadCommand.class, AssignSquadCommand.class, StopCommand.class, StopUnitsCommand.class, TargetUnitUnitsCommand.class, MoveFormationCommand.class, TargetUnitCommand.class, MoveToCommand.class, ClearRallyPointCommand.class, LeaveSquadCommand.class, LeaveSquadUnitsCommand.class);

    public abstract void execute(World var1);

    protected abstract void readFields(DataReader var1);

    protected abstract void writeFields(DataWriter var1);

    public static final GameCommand read(DataReader dataReader)  throws IOException {
        if (dataReader.readBoolean()) {
            GameCommand gameCommand;
            try {
                gameCommand = (GameCommand)((Class)commandClasses.get(dataReader.readByte())).newInstance();
            }
            catch (Exception exception) {
                throw new IOException(exception);
            }
            gameCommand.readFields(dataReader);
            return gameCommand;
        }
        return null;
    }

    public void write(DataWriter dataWriter) {
        GameCommand.writeOrNull(dataWriter, this);
    }

    private static final void writeOrNull(DataWriter dataWriter, GameCommand gameCommand) {
        if (gameCommand != null) {
            dataWriter.writeBoolean(true);
            dataWriter.writeByte((byte)commandClasses.indexOf(gameCommand.getClass()));
            gameCommand.writeFields(dataWriter);
        } else {
            dataWriter.writeBoolean(false);
        }
    }
}


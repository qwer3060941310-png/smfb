/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior.nodes;

import com.desertstormfront.ai.behavior.ActionNode;
import com.desertstormfront.ai.behavior.NodeStatus;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.UnitPosition;
import com.noblemaster.lib.log.OsfLog;

/**
 * 动作：部署并卸载搭载单位
 */
public strictfp final class DeployUnloadNode
extends ActionNode {
    private static final int[][] a;

    static {
        int[][] nArrayArray = new int[25][];
        nArrayArray[0] = new int[2];
        int[] nArray = new int[2];
        nArray[0] = 1;
        nArrayArray[1] = nArray;
        int[] nArray2 = new int[2];
        nArray2[0] = -1;
        nArrayArray[2] = nArray2;
        int[] nArray3 = new int[2];
        nArray3[1] = 1;
        nArrayArray[3] = nArray3;
        int[] nArray4 = new int[2];
        nArray4[1] = -1;
        nArrayArray[4] = nArray4;
        nArrayArray[5] = new int[]{-1, 1};
        nArrayArray[6] = new int[]{-1, -1};
        nArrayArray[7] = new int[]{1, 1};
        nArrayArray[8] = new int[]{1, -1};
        nArrayArray[9] = new int[]{-2, 2};
        nArrayArray[10] = new int[]{-1, 2};
        int[] nArray5 = new int[2];
        nArray5[1] = 2;
        nArrayArray[11] = nArray5;
        nArrayArray[12] = new int[]{1, 2};
        nArrayArray[13] = new int[]{2, 2};
        nArrayArray[14] = new int[]{2, 1};
        int[] nArray6 = new int[2];
        nArray6[0] = 2;
        nArrayArray[15] = nArray6;
        nArrayArray[16] = new int[]{2, -1};
        nArrayArray[17] = new int[]{2, -2};
        nArrayArray[18] = new int[]{1, -2};
        int[] nArray7 = new int[2];
        nArray7[1] = -2;
        nArrayArray[19] = nArray7;
        nArrayArray[20] = new int[]{-1, -2};
        nArrayArray[21] = new int[]{-2, -2};
        nArrayArray[22] = new int[]{-2, -1};
        int[] nArray8 = new int[2];
        nArray8[0] = -2;
        nArrayArray[23] = nArray8;
        nArrayArray[24] = new int[]{-2, 1};
        a = nArrayArray;
    }

    @Override
    public void reset() {
    }

    @Override
    public NodeStatus run(UnitBlackboard unitBlackboard) {
        MapDefinition mapDefinition = unitBlackboard.getContext().getMapDefinition();
        IntentGroup intentGroup = unitBlackboard.getIntentGroup();
        Unit unit = unitBlackboard.getUnit();
        Unit unit2 = unit.getPosition().getUnit();
        if (unit2 != null && intentGroup.getUnits().contains(unit2) && unit.getGuardPositionOrNull() == null) {
            UnitType unitType = unit2.getUnitType();
            if (unitType == mapDefinition.getUnitTypeSlots().getTransport()) {
                AIContext aIContext = unitBlackboard.getContext();
                UnitPosition unitPosition = unit2.getPosition();
                int i9 = unit2.getSubUnits().size() - 1;
                int i10 = (int)unitPosition.getX();
                int i11 = (int)unitPosition.getY();
                int i12 = 0;
                int i13 = 0;
                int n = 1;
                while (n < a.length) {
                    int n2 = i10 + a[n][0];
                    int i16 = i11 + a[n][1];
                    if (aIContext.isInsideGrid((float)n2, (float)i16) && aIContext.hasPathForDomain(Domain.Ground, i10, i11, n2, i16)) {
                        i12 = n;
                        if (i13 >= i9) break;
                        ++i13;
                    }
                    ++n;
                }
                float f = (float)(i10 + a[i12][0]) + 0.5f;
                float f2 = (float)(i11 + a[i12][1]) + 0.5f;
                unitBlackboard.moveUnitTo(f, f2);
            } else if (unitType == mapDefinition.getUnitTypeSlots().getAirTransport()) {
                unitBlackboard.moveUnitTo(intentGroup.getTargetPosition().getX(), intentGroup.getTargetPosition().getY());
            } else if (unitType == mapDefinition.getUnitTypeSlots().getCarrier()) {
                unitBlackboard.moveUnitTo(intentGroup.getTargetPosition().getX(), intentGroup.getTargetPosition().getY());
            } else {
                OsfLog.info("Unknown host encountered: " + unit2);
            }
        }
        return null;
    }
}


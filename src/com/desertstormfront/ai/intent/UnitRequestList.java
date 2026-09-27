/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.intent;

import com.desertstormfront.ai.intent.UnitRequest;
import com.desertstormfront.game.model.UnitType;
import java.util.ArrayList;

/**
 * 单位请求列表
 */
public strictfp final class UnitRequestList
extends ArrayList {
    public boolean hasRequestFor(UnitType unitType) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((UnitRequest)this.get(i2)).getUnitType() == unitType) {
                return true;
            }
            ++i2;
        }
        return false;
    }
}


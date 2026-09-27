/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.intent;

import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;
import java.util.ArrayList;

/**
 * 意图分组列表
 */
public strictfp final class IntentGroupList
extends ArrayList {
    public boolean hasRequests() {
        int i1 = 0;
        while (i1 < this.size()) {
            if (((IntentGroup)this.get(i1)).hasRequests()) {
                return true;
            }
            ++i1;
        }
        return false;
    }

    public boolean hasUnits() {
        int i1 = 0;
        while (i1 < this.size()) {
            if (((IntentGroup)this.get(i1)).hasUnits()) {
                return true;
            }
            ++i1;
        }
        return false;
    }

    public boolean hasUnitOfType(UnitType unitType) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((IntentGroup)this.get(i2)).hasUnitOfType(unitType)) {
                return true;
            }
            ++i2;
        }
        return false;
    }

    public boolean hasCapturingUnit() {
        int i1 = 0;
        while (i1 < this.size()) {
            if (((IntentGroup)this.get(i1)).hasCapturingUnit()) {
                return true;
            }
            ++i1;
        }
        return false;
    }

    public IntentGroup findGroup(Unit unit) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((IntentGroup)this.get(i2)).getUnits().contains(unit)) {
                return (IntentGroup)this.get(i2);
            }
            ++i2;
        }
        return null;
    }
}


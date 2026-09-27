/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.intent;

import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.intent.IntentType;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.world.Vec2;
import java.util.ArrayList;

/**
 * 意图列表
 */
public strictfp final class IntentList
extends ArrayList {
    public Intent findIntent(Unit unit) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((Intent)this.get(i2)).getGroupList().findGroup(unit) != null) {
                return (Intent)this.get(i2);
            }
            ++i2;
        }
        return null;
    }

    public IntentGroup findGroup(Unit unit) {
        int i2 = 0;
        while (i2 < this.size()) {
            IntentGroup intentGroup = ((Intent)this.get(i2)).getGroupList().findGroup(unit);
            if (intentGroup != null) {
                return intentGroup;
            }
            ++i2;
        }
        return null;
    }

    public Intent takeWeakest(IntentType intentType) {
        int i2 = -1;
        long l3 = Long.MAX_VALUE;
        int i5 = 0;
        while (i5 < this.size()) {
            Intent intent = (Intent)this.get(i5);
            if (intent.getType() == intentType) {
                long l7 = 0L;
                IntentGroupList intentGroupList = intent.getGroupList();
                int i10 = 0;
                while (i10 < intentGroupList.size()) {
                    UnitList unitList = ((IntentGroup)intentGroupList.get(i10)).getUnits();
                    int i12 = 0;
                    while (i12 < unitList.size()) {
                        l7 += ((Unit)unitList.get(i12)).getUnitType().getHealth();
                        ++i12;
                    }
                    ++i10;
                }
                if (l7 < l3) {
                    l3 = l7;
                    i2 = i5;
                }
            }
            ++i5;
        }
        if (i2 >= 0) {
            return (Intent)this.remove(i2);
        }
        return null;
    }

    public int countAt(IntentType intentType, float f2, float f3) {
        int i4 = 0;
        int i5 = 0;
        while (i5 < this.size()) {
            Vec2 vec2;
            if (((Intent)this.get(i5)).getType() == intentType && (vec2 = ((Intent)this.get(i5)).getTargetPosition()) != null && vec2.equalsInt(f2, f3)) {
                ++i4;
            }
            ++i5;
        }
        return i4;
    }

    public int countTargeting(IntentType intentType, Unit unit) {
        int i3 = 0;
        int i4 = 0;
        while (i4 < this.size()) {
            Unit unit2;
            if (((Intent)this.get(i4)).getType() == intentType && (unit2 = ((Intent)this.get(i4)).getTargetUnit()) == unit) {
                ++i3;
            }
            ++i4;
        }
        return i3;
    }

    public int count(IntentType intentType) {
        int i2 = 0;
        int i3 = 0;
        while (i3 < this.size()) {
            if (((Intent)this.get(i3)).getType() == intentType) {
                ++i2;
            }
            ++i3;
        }
        return i2;
    }

    public boolean hasTargetAt(Vec2 vec2) {
        return this.hasTargetAt(vec2.getX(), vec2.getY());
    }

    public boolean hasTargetAt(float f1, float f2) {
        int i3 = 0;
        while (i3 < this.size()) {
            IntentGroupList intentGroupList = ((Intent)this.get(i3)).getGroupList();
            int i5 = 0;
            while (i5 < intentGroupList.size()) {
                Vec2 vec2 = ((IntentGroup)intentGroupList.get(i5)).getTargetPosition();
                if (vec2 != null && vec2.equalsInt(f1, f2)) {
                    return true;
                }
                ++i5;
            }
            ++i3;
        }
        return false;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.UnitType;
import java.util.ArrayList;
import java.util.Collection;

public strictfp final class UnitTypeList
extends ArrayList {
    public UnitTypeList() {
    }

    public UnitTypeList(int i1) {
        super(i1);
    }

    public UnitTypeList(Collection collection) {
        super(collection);
    }

    public final boolean hasDomain(Domain domain) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((UnitType)this.get(i2)).getDomain() == domain) {
                return true;
            }
            ++i2;
        }
        return false;
    }

    public final boolean hasCapturingUnit() {
        int i1 = 0;
        while (i1 < this.size()) {
            if (((UnitType)this.get(i1)).canCapture()) {
                return true;
            }
            ++i1;
        }
        return false;
    }

    public final UnitType getByCode(char c) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((UnitType)this.get(i2)).getCode() == c) {
                return (UnitType)this.get(i2);
            }
            ++i2;
        }
        return null;
    }

    public final UnitType getByKey(String string) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((UnitType)this.get(i2)).getKey().equals(string)) {
                return (UnitType)this.get(i2);
            }
            ++i2;
        }
        return null;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.TerrainType;
import java.util.ArrayList;
import java.util.Collection;

public strictfp final class TerrainTypeList
extends ArrayList {
    public TerrainTypeList() {
    }

    public TerrainTypeList(Collection collection) {
        super(collection);
    }

    public final TerrainType getByCode(char c) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((TerrainType)this.get(i2)).getCode() == c) {
                return (TerrainType)this.get(i2);
            }
            ++i2;
        }
        return null;
    }

    public final TerrainType getByKey(String string) {
        int i2 = 0;
        while (i2 < this.size()) {
            if (((TerrainType)this.get(i2)).getKey().equals(string)) {
                return (TerrainType)this.get(i2);
            }
            ++i2;
        }
        return null;
    }
}


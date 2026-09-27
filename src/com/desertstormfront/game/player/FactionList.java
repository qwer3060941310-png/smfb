/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

import com.desertstormfront.game.player.Faction;
import java.util.ArrayList;
import java.util.Collection;

public strictfp final class FactionList
extends ArrayList {
    public FactionList() {
    }

    public FactionList(Collection collection) {
        super(collection);
    }

    public final Faction getByKey(String string) {
        int i2 = 0;
        while (i2 < this.size()) {
            Faction faction = (Faction)this.get(i2);
            if (faction.getKey().equals(string)) {
                return faction;
            }
            ++i2;
        }
        return null;
    }
}


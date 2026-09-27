/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.patch;

import com.desertstormfront.game.model.AmmoType;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Layer;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;

public class PatchHelper {
    public static UnitType makeInfantry() {
        return new UnitType(21, "Infantry[i18n]: Infantry", "INFANTRY", 'D', 60L, false, Layer.Base, Domain.Ground, 0.18f, 0.17f, 0.17f, 0.0f, false, false, AmmoType.Bullets, 0.2f, 0.2f, 0, 1.0f, false, false, false, false, false, 1, 2, 4.0f, new UnitTypeList(), new UnitTypeList(), 0, false);
    }
}


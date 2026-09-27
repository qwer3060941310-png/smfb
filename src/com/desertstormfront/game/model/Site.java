/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.Sortable;
import com.desertstormfront.game.model.TerrainType;
import com.desertstormfront.world.Vec2;
import com.noblemaster.lib.log.OsfLog;

public final class Site
implements Sortable {
    private final TerrainType a;
    private final int b;
    private final Vec2 c;
    private final float d;

    private Site(TerrainType terrainType, int i2, Vec2 vec2) {
        this.a = terrainType;
        this.b = i2 % terrainType.getSpriteCount();
        this.c = vec2;
        this.d = vec2.getX() + vec2.getY();
        if (i2 >= terrainType.getSpriteCount()) {
            OsfLog.error("Site index is too big for " + terrainType.getName() + ": " + i2);
        }
    }

    public static final Site of(TerrainType terrainType, int i1, Vec2 vec2) {
        return new Site(terrainType, i1, vec2);
    }

    public final TerrainType getTerrainType() {
        return this.a;
    }

    public final int getVariantIndex() {
        return this.b;
    }

    public final Vec2 getPosition() {
        return this.c;
    }

    @Override
    public final boolean isSite() {
        return true;
    }

    @Override
    public final boolean isMoveTargetMarker() {
        return false;
    }

    @Override
    public final float getSortValue() {
        return this.d;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.desertstormfront.world.RoadMask;

public strictfp final class RoadTile {
    private RoadMask a;
    private boolean b;

    public static RoadTile create(RoadMask roadMask, boolean bl) {
        RoadTile roadTile = new RoadTile();
        roadTile.setRoadMask(roadMask);
        roadTile.a(bl);
        return roadTile;
    }

    public RoadMask getRoadMask() {
        return this.a;
    }

    public void setRoadMask(RoadMask roadMask) {
        this.a = roadMask;
    }

    public boolean b() {
        return this.b;
    }

    public void a(boolean bl) {
        this.b = bl;
    }
}


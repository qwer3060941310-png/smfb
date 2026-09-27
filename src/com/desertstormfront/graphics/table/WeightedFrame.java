/*
 * One step of a weighted animation: a selection weight plus the region drawn for it. The weight is
 * what SpriteAnimationSet sums into its per-facing totals.
 *
 * Deobfuscation: the field pair (a, b) and their synthetic static accessors were named
 * weight/region from that consumption pattern. Evidence: run\map-graphics-table.tsv.
 */
package com.desertstormfront.graphics.table;

import com.desertstormfront.graphics.table.SpriteRegion;

public class WeightedFrame {
    private int weight;
    private SpriteRegion region;

    public WeightedFrame(int i1, SpriteRegion spriteRegion) {
        this.weight = i1;
        this.region = spriteRegion;
    }

    static /* synthetic */ int getWeight(WeightedFrame weightedFrame) {
        return weightedFrame.weight;
    }

    static /* synthetic */ SpriteRegion getRegion(WeightedFrame weightedFrame) {
        return weightedFrame.region;
    }
}


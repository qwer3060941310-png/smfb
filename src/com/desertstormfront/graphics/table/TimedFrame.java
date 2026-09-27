/*
 * One step of a time-driven animation: a duration in milliseconds plus the region drawn for it.
 * SpriteAnimationSet and UnitSpriteAnimation advance it with (elapsed % total duration).
 *
 * Deobfuscation: the field pair (a, b) and the two synthetic static accessors the tables call were
 * named duration/region after that consumption pattern. Evidence: run\map-graphics-table.tsv.
 */
package com.desertstormfront.graphics.table;

import com.desertstormfront.graphics.table.SpriteRegion;

public class TimedFrame {
    private int duration;
    private SpriteRegion region;

    public TimedFrame(int i1, SpriteRegion spriteRegion) {
        this.duration = i1;
        this.region = spriteRegion;
    }

    static /* synthetic */ int getDuration(TimedFrame timedFrame) {
        return timedFrame.duration;
    }

    static /* synthetic */ SpriteRegion getRegion(TimedFrame timedFrame) {
        return timedFrame.region;
    }
}


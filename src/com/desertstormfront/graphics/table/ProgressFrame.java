/*
 * One step of a progress-driven animation: the duration is stored as (int)(seconds * 10000) so the
 * 0..1 progress value the caller holds can be compared with integer arithmetic.
 *
 * Deobfuscation: the field pair (a, b) and their synthetic static accessors were named
 * duration/region from the constructor and from UnitSpriteAnimation.getProgressRegion, their only
 * reader. Evidence: run\map-graphics-table.tsv.
 */
package com.desertstormfront.graphics.table;

import com.desertstormfront.graphics.table.SpriteRegion;

public class ProgressFrame {
    private int duration;
    private SpriteRegion region;

    public ProgressFrame(float f1, SpriteRegion spriteRegion) {
        this.duration = (int)(f1 * 10000.0f);
        this.region = spriteRegion;
    }

    static /* synthetic */ int getDuration(ProgressFrame progressFrame) {
        return progressFrame.duration;
    }

    static /* synthetic */ SpriteRegion getRegion(ProgressFrame progressFrame) {
        return progressFrame.region;
    }
}


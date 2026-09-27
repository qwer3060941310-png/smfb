/*
 * One unit's animation row: the base region plus optional progress-driven frames (production /
 * construction) and time-driven frames (movement loops), each with a pre-computed total.
 *
 * Deobfuscation: fields a..g became unitTypeId/facing/baseRegion/progressFrames/progressTotal/
 * timedFrames/timedTotal, and the two pickers a(boolean, float) / a(long) became
 * getProgressRegion / getTimedRegion. The mapping is fixed by UnitSpriteTable (rows indexed by unit
 * type id, columns by facing) and by the config parser that feeds the constructor. Evidence:
 * run\map-graphics-table.tsv.
 */
package com.desertstormfront.graphics.table;

import com.desertstormfront.graphics.table.ProgressFrame;
import com.desertstormfront.graphics.table.SpriteRegion;
import com.desertstormfront.graphics.table.TimedFrame;
import java.util.List;

public final class UnitSpriteAnimation {
    private int unitTypeId;
    private int facing;
    private SpriteRegion baseRegion;
    private List progressFrames;
    private int progressTotal;
    private List timedFrames;
    private int timedTotal;

    public UnitSpriteAnimation(int i1, int i2, SpriteRegion spriteRegion, List list, List list2) {
        int i6;
        this.unitTypeId = i1;
        this.facing = i2;
        this.baseRegion = spriteRegion;
        this.progressFrames = list;
        this.timedFrames = list2;
        this.progressTotal = 0;
        if (list != null) {
            i6 = 0;
            while (i6 < list.size()) {
                this.progressTotal += ProgressFrame.getDuration((ProgressFrame)list.get(i6));
                ++i6;
            }
            this.timedTotal = 0;
        }
        if (list2 != null) {
            i6 = 0;
            while (i6 < list2.size()) {
                this.timedTotal += TimedFrame.getDuration((TimedFrame)list2.get(i6));
                ++i6;
            }
        }
    }

    public int getUnitTypeId() {
        return this.unitTypeId;
    }

    public int getFacing() {
        return this.facing;
    }

    public SpriteRegion getProgressRegion(boolean bl, float f2) {
        if (!bl && this.baseRegion != null || this.progressFrames == null) {
            return this.baseRegion;
        }
        if (this.progressTotal == 0) {
            return ProgressFrame.getRegion((ProgressFrame)this.progressFrames.get(0));
        }
        int i3 = (int)(f2 * 10000.0f) % this.progressTotal;
        int i4 = 0;
        int i5 = 0;
        while (i5 < this.progressFrames.size()) {
            if (i3 < (i4 += ProgressFrame.getDuration((ProgressFrame)this.progressFrames.get(i5)))) {
                return ProgressFrame.getRegion((ProgressFrame)this.progressFrames.get(i5));
            }
            ++i5;
        }
        return null;
    }

    public SpriteRegion getTimedRegion(long l1) {
        if (this.timedFrames == null) {
            return null;
        }
        if (this.timedTotal == 0) {
            return TimedFrame.getRegion((TimedFrame)this.timedFrames.get(0));
        }
        int i3 = (int)(l1 % (long)this.timedTotal);
        int i4 = 0;
        int i5 = 0;
        while (i5 < this.timedFrames.size()) {
            if (i3 < (i4 += TimedFrame.getDuration((TimedFrame)this.timedFrames.get(i5)))) {
                return TimedFrame.getRegion((TimedFrame)this.timedFrames.get(i5));
            }
            ++i5;
        }
        return null;
    }
}


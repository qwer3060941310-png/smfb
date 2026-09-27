/*
 * A set of weighted animations, one per facing: frameLists[facing] holds the WeightedFrame list and
 * totalWeights[facing] its pre-computed sum, so picking a frame is a modulo over elapsed time.
 *
 * Deobfuscation: the pair (a, b) became frameLists/totalWeights, and a(int, long) / a() became
 * getRegion(direction, elapsedMs) / getListCount - the facing index is how both sprite tables index
 * the rows. Evidence: run\map-graphics-table.tsv.
 */
package com.desertstormfront.graphics.table;

import com.desertstormfront.graphics.table.SpriteRegion;
import com.desertstormfront.graphics.table.WeightedFrame;
import java.util.List;

public final class SpriteAnimationSet {
    private List[] frameLists;
    private int[] totalWeights;

    public SpriteAnimationSet(List[] listArray) {
        this.frameLists = listArray;
        this.totalWeights = new int[listArray.length];
        int i2 = 0;
        while (i2 < listArray.length) {
            List list = listArray[i2];
            this.totalWeights[i2] = 0;
            int i4 = 0;
            while (i4 < list.size()) {
                int n = i2;
                this.totalWeights[n] = this.totalWeights[n] + WeightedFrame.getWeight((WeightedFrame)list.get(i4));
                ++i4;
            }
            ++i2;
        }
    }

    public SpriteRegion getRegion(int i1, long l2) {
        List list = this.frameLists[i1];
        int i5 = this.totalWeights[i1];
        if (i5 == 0) {
            return WeightedFrame.getRegion((WeightedFrame)list.get(0));
        }
        int i6 = (int)(l2 % (long)i5);
        int i7 = 0;
        int i8 = 0;
        while (i8 < list.size()) {
            if (i6 < (i7 += WeightedFrame.getWeight((WeightedFrame)list.get(i8)))) {
                return WeightedFrame.getRegion((WeightedFrame)list.get(i8));
            }
            ++i8;
        }
        return null;
    }

    public int getListCount() {
        return this.frameLists.length;
    }
}


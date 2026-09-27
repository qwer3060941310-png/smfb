/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.CheckboxState;

public class CheckboxStyle {
    public int[] frameX;
    public int[] frameY;
    public int width;
    public int height;

    public CheckboxStyle() {
        this(new int[2 * CheckboxState.values().length], new int[2 * CheckboxState.values().length], 0, 0);
    }

    public CheckboxStyle(int[] nArray, int[] nArray2, int i3, int i4) {
        this.frameX = nArray;
        this.frameY = nArray2;
        this.width = i3;
        this.height = i4;
    }

    public CheckboxStyle copy() {
        CheckboxStyle checkboxStyle = new CheckboxStyle();
        checkboxStyle.frameX = new int[this.frameX.length];
        int i2 = 0;
        while (i2 < checkboxStyle.frameX.length) {
            checkboxStyle.frameX[i2] = this.frameX[i2];
            ++i2;
        }
        checkboxStyle.frameY = new int[this.frameY.length];
        i2 = 0;
        while (i2 < checkboxStyle.frameY.length) {
            checkboxStyle.frameY[i2] = this.frameY[i2];
            ++i2;
        }
        checkboxStyle.width = this.width;
        checkboxStyle.height = this.height;
        return checkboxStyle;
    }

    public /* synthetic */ Object clone() {
        return this.copy();
    }
}


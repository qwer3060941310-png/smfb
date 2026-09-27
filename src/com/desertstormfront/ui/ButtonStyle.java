/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ButtonState;

public class ButtonStyle {
    public int[] frameX;
    public int[] frameY;
    public int width;
    public int height;

    public ButtonStyle() {
        this(new int[ButtonState.values().length], new int[ButtonState.values().length], 0, 0);
    }

    public ButtonStyle(int[] nArray, int[] nArray2, int i3, int i4) {
        this.frameX = nArray;
        this.frameY = nArray2;
        this.width = i3;
        this.height = i4;
    }

    public ButtonStyle copy() {
        ButtonStyle buttonStyle = new ButtonStyle();
        buttonStyle.frameX = new int[this.frameX.length];
        int i2 = 0;
        while (i2 < buttonStyle.frameX.length) {
            buttonStyle.frameX[i2] = this.frameX[i2];
            ++i2;
        }
        buttonStyle.frameY = new int[this.frameY.length];
        i2 = 0;
        while (i2 < buttonStyle.frameY.length) {
            buttonStyle.frameY[i2] = this.frameY[i2];
            ++i2;
        }
        buttonStyle.width = this.width;
        buttonStyle.height = this.height;
        return buttonStyle;
    }

    public /* synthetic */ Object clone() {
        return this.copy();
    }
}


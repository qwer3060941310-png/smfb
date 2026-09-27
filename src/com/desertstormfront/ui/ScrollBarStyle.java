/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ScrollState;

public class ScrollBarStyle {
    public int[] upArrowFrameX;
    public int[] upArrowFrameY;
    public int upArrowWidth;
    public int upArrowHeight;
    public int[] trackFrameX;
    public int[] trackFrameY;
    public int trackWidth;
    public int trackHeight;
    public int[] downArrowFrameX;
    public int[] downArrowFrameY;
    public int downArrowWidth;
    public int downArrowHeight;

    public ScrollBarStyle() {
        this(new int[ScrollState.values().length], new int[ScrollState.values().length], 0, 0, new int[ScrollState.values().length], new int[ScrollState.values().length], 0, 0, new int[ScrollState.values().length], new int[ScrollState.values().length], 0, 0);
    }

    public ScrollBarStyle(int[] nArray, int[] nArray2, int i3, int i4, int[] nArray3, int[] nArray4, int i7, int i8, int[] nArray5, int[] nArray6, int i11, int i12) {
        this.upArrowFrameX = nArray;
        this.upArrowFrameY = nArray2;
        this.upArrowWidth = i3;
        this.upArrowHeight = i4;
        this.trackFrameX = nArray3;
        this.trackFrameY = nArray4;
        this.trackWidth = i7;
        this.trackHeight = i8;
        this.downArrowFrameX = nArray5;
        this.downArrowFrameY = nArray6;
        this.downArrowWidth = i11;
        this.downArrowHeight = i12;
    }

    public ScrollBarStyle copy() {
        ScrollBarStyle scrollBarStyle = new ScrollBarStyle();
        scrollBarStyle.upArrowFrameX = new int[this.upArrowFrameX.length];
        int i2 = 0;
        while (i2 < scrollBarStyle.upArrowFrameX.length) {
            scrollBarStyle.upArrowFrameX[i2] = this.upArrowFrameX[i2];
            ++i2;
        }
        scrollBarStyle.upArrowFrameY = new int[this.upArrowFrameY.length];
        i2 = 0;
        while (i2 < scrollBarStyle.upArrowFrameY.length) {
            scrollBarStyle.upArrowFrameY[i2] = this.upArrowFrameY[i2];
            ++i2;
        }
        scrollBarStyle.upArrowWidth = this.upArrowWidth;
        scrollBarStyle.upArrowHeight = this.upArrowHeight;
        scrollBarStyle.trackFrameX = new int[this.trackFrameX.length];
        i2 = 0;
        while (i2 < scrollBarStyle.trackFrameX.length) {
            scrollBarStyle.trackFrameX[i2] = this.trackFrameX[i2];
            ++i2;
        }
        scrollBarStyle.trackFrameY = new int[this.trackFrameY.length];
        i2 = 0;
        while (i2 < scrollBarStyle.trackFrameY.length) {
            scrollBarStyle.trackFrameY[i2] = this.trackFrameY[i2];
            ++i2;
        }
        scrollBarStyle.trackWidth = this.trackWidth;
        scrollBarStyle.trackHeight = this.trackHeight;
        scrollBarStyle.downArrowFrameX = new int[this.downArrowFrameX.length];
        i2 = 0;
        while (i2 < scrollBarStyle.downArrowFrameX.length) {
            scrollBarStyle.downArrowFrameX[i2] = this.downArrowFrameX[i2];
            ++i2;
        }
        scrollBarStyle.downArrowFrameY = new int[this.downArrowFrameY.length];
        i2 = 0;
        while (i2 < scrollBarStyle.downArrowFrameY.length) {
            scrollBarStyle.downArrowFrameY[i2] = this.downArrowFrameY[i2];
            ++i2;
        }
        scrollBarStyle.downArrowWidth = this.downArrowWidth;
        scrollBarStyle.downArrowHeight = this.downArrowHeight;
        return scrollBarStyle;
    }

    public /* synthetic */ Object clone() {
        return this.copy();
    }
}


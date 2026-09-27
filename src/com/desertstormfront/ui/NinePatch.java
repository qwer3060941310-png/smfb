/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

public class NinePatch {
    public int left;
    public int leftSplit;
    public int rightSplit;
    public int right;
    public int top;
    public int topSplit;
    public int bottomSplit;
    public int bottom;

    public NinePatch() {
        this(0, 0, 0, 0, 0, 0, 0, 0);
    }

    public NinePatch(int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.left = i1;
        this.leftSplit = i2;
        this.rightSplit = i3;
        this.right = i4;
        this.top = i5;
        this.topSplit = i6;
        this.bottomSplit = i7;
        this.bottom = i8;
    }

    public NinePatch copy() {
        NinePatch ninePatch = new NinePatch();
        ninePatch.left = this.left;
        ninePatch.leftSplit = this.leftSplit;
        ninePatch.rightSplit = this.rightSplit;
        ninePatch.right = this.right;
        ninePatch.top = this.top;
        ninePatch.topSplit = this.topSplit;
        ninePatch.bottomSplit = this.bottomSplit;
        ninePatch.bottom = this.bottom;
        return ninePatch;
    }

    public /* synthetic */ Object clone() {
        return this.copy();
    }
}


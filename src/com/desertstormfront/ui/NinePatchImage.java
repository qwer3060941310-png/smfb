/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.NinePatch;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.Widget;

public class NinePatchImage
extends Widget {
    private NinePatch ninePatch;

    public NinePatchImage(NinePatch ninePatch) {
        this.ninePatch = ninePatch;
    }

    @Override
    public void pack() {
    }

    @Override
    public boolean handleInput(ActionListener actionListener, float f2, float f3, boolean bl) {
        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float f2, float f3) {
        if (this.ninePatch != null) {
            int i4 = this.ninePatch.leftSplit - this.ninePatch.left;
            int i5 = this.ninePatch.rightSplit - this.ninePatch.leftSplit;
            int i6 = this.ninePatch.right - this.ninePatch.rightSplit;
            int i7 = this.ninePatch.topSplit - this.ninePatch.top;
            int i8 = this.ninePatch.bottomSplit - this.ninePatch.topSplit;
            int i9 = this.ninePatch.bottom - this.ninePatch.bottomSplit;
            float f10 = this.getX() + f2;
            float f11 = f10 + (float)i4;
            float f12 = f10 + this.getWidth() - (float)i6;
            float f13 = this.getY() + f3;
            float f14 = f13 + (float)i7;
            float f15 = f13 + this.getHeight() - (float)i9;
            spriteBatch.draw(f10, f13, (float)i4, (float)i7, this.ninePatch.left, this.ninePatch.top, i4, i7, this.getColor());
            spriteBatch.draw(f11, f13, f12 - f11, (float)i7, this.ninePatch.leftSplit, this.ninePatch.top, i5, i7, this.getColor());
            spriteBatch.draw(f12, f13, (float)i6, (float)i7, this.ninePatch.rightSplit, this.ninePatch.top, i6, i7, this.getColor());
            spriteBatch.draw(f10, f14, (float)i4, f15 - f14, this.ninePatch.left, this.ninePatch.topSplit, i4, i8, this.getColor());
            spriteBatch.draw(f11, f14, f12 - f11, f15 - f14, this.ninePatch.leftSplit, this.ninePatch.topSplit, i5, i8, this.getColor());
            spriteBatch.draw(f12, f14, (float)i6, f15 - f14, this.ninePatch.rightSplit, this.ninePatch.topSplit, i6, i8, this.getColor());
            spriteBatch.draw(f10, f15, (float)i4, (float)i9, this.ninePatch.left, this.ninePatch.bottomSplit, i4, i9, this.getColor());
            spriteBatch.draw(f11, f15, f12 - f11, (float)i9, this.ninePatch.leftSplit, this.ninePatch.bottomSplit, i5, i9, this.getColor());
            spriteBatch.draw(f12, f15, (float)i6, (float)i9, this.ninePatch.rightSplit, this.ninePatch.bottomSplit, i6, i9, this.getColor());
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

public class TextureRegion {
    public int x;
    public int y;
    public int width;
    public int height;

    public TextureRegion() {
        this(0, 0, 0, 0);
    }

    public TextureRegion(int i1, int i2, int i3, int i4) {
        this.x = i1;
        this.y = i2;
        this.width = i3;
        this.height = i4;
    }

    public TextureRegion copy() {
        TextureRegion textureRegion = new TextureRegion();
        textureRegion.x = this.x;
        textureRegion.y = this.y;
        textureRegion.width = this.width;
        textureRegion.height = this.height;
        return textureRegion;
    }

    public /* synthetic */ Object clone() {
        return this.copy();
    }
}


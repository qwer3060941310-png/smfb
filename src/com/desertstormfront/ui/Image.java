/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.Widget;

public class Image
extends Widget {
    private TextureRegion region;

    public Image(TextureRegion textureRegion) {
        this.region = textureRegion;
    }

    public TextureRegion getRegion() {
        return this.region;
    }

    public void setRegion(TextureRegion textureRegion) {
        this.region = textureRegion;
    }

    @Override
    public void pack() {
        if (this.region != null) {
            if (this.getWidth() == 0.0f && this.getHeight() == 0.0f) {
                this.setWidth(this.region.width);
                this.setHeight(this.region.height);
            }
        } else {
            this.setWidth(0.0f);
            this.setHeight(0.0f);
        }
    }

    @Override
    public boolean handleInput(ActionListener actionListener, float f2, float f3, boolean bl) {
        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float f2, float f3) {
        if (this.region != null) {
            spriteBatch.draw(this.getX() + f2, this.getY() + f3, this.getWidth(), this.getHeight(), this.region.x, this.region.y, this.region.width, this.region.height, this.getColor());
        }
    }
}


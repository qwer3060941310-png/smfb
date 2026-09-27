/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.TextureRegion;

public final class ResultBanner
extends Container {
    private boolean victory;
    private Image separatorImage = new Image(new TextureRegion(565, 967, 1, 68));
    private Image bannerImage;

    public ResultBanner() {
        this.addChild(this.separatorImage);
        this.bannerImage = new Image(new TextureRegion());
        this.addChild(this.bannerImage);
        this.setVictory(true);
    }

    public void resize(int i1, int i2) {
        this.pack();
        this.separatorImage.setWidth(i1);
        this.setWidth(i1);
        this.setVictory(this.isVictory());
    }

    public boolean isVictory() {
        return this.victory;
    }

    public void setVictory(boolean bl) {
        this.victory = bl;
        if (bl) {
            this.bannerImage.getRegion().x = 0;
            this.bannerImage.getRegion().y = 967;
            this.bannerImage.getRegion().width = 287;
            this.bannerImage.getRegion().height = 68;
        } else {
            this.bannerImage.getRegion().x = 288;
            this.bannerImage.getRegion().y = 967;
            this.bannerImage.getRegion().width = 275;
            this.bannerImage.getRegion().height = 68;
        }
        this.bannerImage.setX((this.getWidth() - (float)this.bannerImage.getRegion().width) / 2.0f);
        this.bannerImage.setY((this.getHeight() - (float)this.bannerImage.getRegion().height) / 2.0f + 1.0f);
        this.bannerImage.pack();
    }
}


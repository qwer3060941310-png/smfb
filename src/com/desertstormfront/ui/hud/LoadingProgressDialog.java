/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.hud.GuiAssets;

public final class LoadingProgressDialog
extends Container {
    private Label messageLabel;
    private Image[] progressSegments;
    private boolean networkMode;
    private long lastUpdateTime;

    public LoadingProgressDialog(String string) {
        this(string, false);
    }

    public LoadingProgressDialog(String string, boolean bl) {
        this.networkMode = bl;
        Image image = new Image(new TextureRegion(1019, 333, 481, 172));
        this.addChild(image);
        Label label = new Label(GuiAssets.getFontXirod17());
        label.setText(string);
        label.setX(37.0f);
        label.setY(12.0f);
        label.a(-16777216);
        this.addChild(label);
        Label label2 = new Label(GuiAssets.getFontXirod17());
        label2.setText(string);
        label2.setX(35.0f);
        label2.setY(10.0f);
        this.addChild(label2);
        this.messageLabel = new Label(GuiAssets.getDefaultFont());
        this.messageLabel.setX(51.0f);
        this.messageLabel.setY(60.0f);
        this.messageLabel.a(-6250336);
        this.addChild(this.messageLabel);
        int i6 = 82;
        this.progressSegments = new Image[10];
        this.progressSegments[0] = new Image(new TextureRegion(193, 727, 70, 45));
        this.progressSegments[0].setX(43.0f);
        this.progressSegments[0].setY((float)i6);
        this.addChild(this.progressSegments[0]);
        int i7 = 1;
        while (i7 < this.progressSegments.length - 1) {
            this.progressSegments[i7] = new Image(new TextureRegion(264, 727, 57, 45));
            this.progressSegments[i7].setX(91 + (i7 - 1) * 35);
            this.progressSegments[i7].setY((float)i6);
            this.addChild(this.progressSegments[i7]);
            ++i7;
        }
        this.progressSegments[9] = new Image(new TextureRegion(322, 727, 68, 45));
        this.progressSegments[9].setX(371.0f);
        this.progressSegments[9].setY((float)i6);
        this.addChild(this.progressSegments[9]);
        this.setProgress(0.0f);
        this.pack();
    }

    public void setProgress(float f1) {
        if (this.networkMode) {
            this.lastUpdateTime = System.currentTimeMillis();
        } else {
            int i2 = (int)(f1 * ((float)this.progressSegments.length + 0.5f));
            int i3 = 0;
            while (i3 < this.progressSegments.length) {
                this.progressSegments[i3].setVisible(i3 <= i2);
                ++i3;
            }
        }
    }

    public void setMessage(String string) {
        this.messageLabel.setText(string);
        this.pack();
    }

    @Override
    protected void draw(SpriteBatch spriteBatch, float f2, float f3) {
        if (this.networkMode) {
            int i4;
            int i5 = i4 = (int)((System.currentTimeMillis() - this.lastUpdateTime) / 200L % (long)(this.progressSegments.length / 2));
            int i6 = this.progressSegments.length - i4 - 1;
            int i7 = 0;
            while (i7 < this.progressSegments.length) {
                this.progressSegments[i7].setVisible(i7 == i5 || i7 == i6);
                ++i7;
            }
        }
        super.draw(spriteBatch, f2, f3);
    }
}


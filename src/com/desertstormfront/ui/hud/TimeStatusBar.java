/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.app.support.TextFormatter;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.hud.GuiAssets;

public final class TimeStatusBar
extends Container {
    private Label a;
    private Label b;

    public TimeStatusBar(boolean bl, boolean bl2) {
        Image image = bl || bl2 ? new Image(new TextureRegion(0, 0, 207, 109)) : new Image(new TextureRegion(0, 0, 207, 51));
        this.addChild(image);
        this.a = new Label(GuiAssets.getFontDungeon34());
        this.a.a(-6250336);
        this.a.setX(0.0f);
        this.a.setY(-2.0f);
        this.a.setMaxWidth(185);
        this.a.setAlign(Align.RIGHT);
        this.addChild(this.a);
        this.b = new Label(GuiAssets.getFontDungeon25());
        this.b.a(-6250336);
        this.b.setX(46.0f);
        this.b.setY(56.0f);
        this.b.setMaxWidth(88);
        this.b.setAlign(Align.LEFT);
        this.addChild(this.b);
        int[] nArray = new int[4];
        nArray[1] = 50;
        nArray[2] = 100;
        nArray[3] = 150;
        Button button = new Button(new ButtonStyle(nArray, new int[]{110, 110, 110, 110}, 49, 49));
        button.setX(-2.0f);
        button.setY(-3.0f);
        this.addChild(button);
        this.pack();
    }

    public void setGameTime(long l1) {
        this.a.setText(TextFormatter.formatAmount(l1));
    }

    public void setResourceAmount(float f1) {
        this.b.setText(TextFormatter.formatTime(f1));
    }
}


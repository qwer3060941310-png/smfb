/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.GlTexture;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.TextureRegion;

public final class MinimapPanel
extends Container {
    private Container a = new Container();
    private Container b;
    private Button c;
    private Button d;

    public MinimapPanel(GlTexture glTexture) {
        this.addChild(this.a);
        Image image = new Image(new TextureRegion(0, 825, 255, 138));
        this.a.addChild(image);
        Button button = new Button(new ButtonStyle(new int[]{388, 437, 486, 486}, new int[]{933, 933, 933, 933}, 48, 24));
        button.setData(100);
        button.setX(187.0f);
        button.setY(10.0f);
        this.a.addChild(button);
        this.c = new Button(new ButtonStyle(new int[]{256, 289, 322, 355}, new int[]{950, 950, 950, 950}, 32, 16));
        this.c.setData(101);
        this.c.setX(46.0f);
        this.c.setY(26.0f);
        this.a.addChild(this.c);
        this.d = new Button(new ButtonStyle(new int[]{256, 289, 322, 355}, new int[]{933, 933, 933, 933}, 32, 16));
        this.d.setData(102);
        this.d.setX(183.0f);
        this.d.setY(86.0f);
        this.a.addChild(this.d);
        this.b = new Container();
        this.addChild(this.b);
        Image image2 = new Image(new TextureRegion(256, 825, 109, 61));
        image2.setX(146.0f);
        this.b.addChild(image2);
        Button button2 = new Button(new ButtonStyle(new int[]{486, 535, 388, 388}, new int[]{933, 933, 933, 933}, 48, 24));
        button2.setData(200);
        button2.setX(187.0f);
        button2.setY(10.0f);
        this.b.addChild(button2);
        this.pack();
    }

    public boolean isExpanded() {
        return this.a.isVisible();
    }

    public void setExpanded(boolean bl) {
        this.a.setVisible(bl);
        this.b.setVisible(!bl);
    }

    public void setZoomInEnabled(boolean bl) {
        this.c.setEnabled(bl);
    }

    public void setZoomOutEnabled(boolean bl) {
        this.d.setEnabled(bl);
    }

    @Override
    public boolean contains(float f1, float f2) {
        if (this.isExpanded()) {
            return f1 >= this.getX() + 20.0f && f2 >= this.getY() && f1 < this.getX() + this.getWidth() && f2 < this.getY() + this.getHeight() - 20.0f;
        }
        return f1 >= this.getX() + 184.0f && f2 >= this.getY() && f1 < this.getX() + this.getWidth() && f2 < this.getY() + this.getHeight() - 102.0f;
    }
}


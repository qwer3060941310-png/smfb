/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.Widget;
import java.util.ArrayList;
import java.util.List;

public class Container
extends Widget {
    private List children = new ArrayList();

    public void addChild(Widget widget) {
        this.children.add(widget);
    }

    public void removeChild(Widget widget) {
        this.children.remove(widget);
    }

    public void clearChildren() {
        this.children.clear();
    }

    @Override
    public void pack() {
        float f1 = 0.0f;
        float f2 = 0.0f;
        int i3 = 0;
        while (i3 < this.children.size()) {
            Widget widget = (Widget)this.children.get(i3);
            widget.pack();
            if (widget.isVisible()) {
                if (widget.getX() + widget.getWidth() > f1) {
                    f1 = widget.getX() + widget.getWidth();
                }
                if (widget.getY() + widget.getHeight() > f2) {
                    f2 = widget.getY() + widget.getHeight();
                }
            }
            ++i3;
        }
        this.setWidth(f1);
        this.setHeight(f2);
    }

    @Override
    public boolean handleInput(ActionListener actionListener, float f2, float f3, boolean bl) {
        if (this.isVisible()) {
            boolean i5 = false;
            int i6 = 0;
            while (i6 < this.children.size()) {
                Widget widget = (Widget)this.children.get(i6);
                if (widget.handleInput(actionListener, f2 - this.getX(), f3 - this.getY(), bl)) {
                    i5 = true;
                }
                ++i6;
            }
            return i5;
        }
        return false;
    }

    @Override
    protected void draw(SpriteBatch spriteBatch, float f2, float f3) {
        int i4 = 0;
        while (i4 < this.children.size()) {
            Widget widget = (Widget)this.children.get(i4);
            if (widget.isVisible()) {
                widget.draw(spriteBatch, this.getX() + f2, this.getY() + f3);
            }
            ++i4;
        }
    }
}


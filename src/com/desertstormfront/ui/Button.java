/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.ButtonState;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.Widget;

public class Button
extends Widget {
    private ButtonState state;
    private ButtonStyle style;
    private Widget label;

    public Button(ButtonStyle buttonStyle) {
        this(buttonStyle, null);
    }

    public Button(ButtonStyle buttonStyle, Widget widget) {
        this.style = buttonStyle;
        this.label = widget;
        this.state = ButtonState.NORMAL;
    }

    public ButtonStyle getStyle() {
        return this.style;
    }

    public void setStyle(ButtonStyle buttonStyle) {
        this.style = buttonStyle;
    }

    public void setLabel(Widget widget) {
        this.label = widget;
    }

    public boolean isEnabled() {
        return this.state != ButtonState.DISABLED;
    }

    public void setEnabled(boolean bl) {
        if (bl != this.isEnabled()) {
            this.state = bl ? ButtonState.NORMAL : ButtonState.DISABLED;
        }
    }

    @Override
    public void pack() {
        if (this.label != null) {
            this.label.pack();
        }
        if (this.style != null) {
            this.setWidth(this.style.width);
            this.setHeight(this.style.height);
        } else {
            this.setWidth(0.0f);
            this.setHeight(0.0f);
        }
    }

    @Override
    public boolean handleInput(ActionListener actionListener, float f2, float f3, boolean bl) {
        if (this.isVisible() && this.contains(f2, f3)) {
            if (this.state == ButtonState.DISABLED) {
                return false;
            }
            if (this.state == ButtonState.NORMAL || this.state == ButtonState.HOVER) {
                this.state = bl ? ButtonState.PRESSED : ButtonState.HOVER;
            } else if (this.state == ButtonState.PRESSED && !bl) {
                this.state = ButtonState.NORMAL;
                actionListener.onAction(this);
            }
            return true;
        }
        if (this.state != ButtonState.DISABLED) {
            this.state = ButtonState.NORMAL;
        }
        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float f2, float f3) {
        if (this.style != null) {
            int i4 = this.state.ordinal();
            spriteBatch.draw(this.getX() + f2, this.getY() + f3, this.style.frameX[i4], this.style.frameY[i4], this.style.width, this.style.height, this.getColor());
            if (this.label != null) {
                this.label.draw(spriteBatch, this.getX() + f2, this.getY() + f3);
            }
        }
    }
}


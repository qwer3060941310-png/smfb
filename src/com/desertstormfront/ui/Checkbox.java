/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.CheckboxState;
import com.desertstormfront.ui.CheckboxStyle;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.Widget;

public class Checkbox
extends Widget {
    private CheckboxState state;
    private boolean checked;
    private CheckboxStyle style;
    private Widget label;

    public Checkbox(CheckboxStyle checkboxStyle) {
        this(checkboxStyle, null);
    }

    public Checkbox(CheckboxStyle checkboxStyle, Widget widget) {
        this.style = checkboxStyle;
        this.label = widget;
        this.state = CheckboxState.NORMAL;
        this.checked = false;
    }

    public void setChecked(boolean bl) {
        this.checked = bl;
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
            if (this.state == CheckboxState.DISABLED) {
                return false;
            }
            if (this.state == CheckboxState.NORMAL || this.state == CheckboxState.HOVER) {
                this.state = bl ? CheckboxState.PRESSED : CheckboxState.HOVER;
            } else if (this.state == CheckboxState.PRESSED && !bl) {
                this.state = CheckboxState.NORMAL;
                this.checked = !this.checked;
                actionListener.onAction(this);
            }
            return true;
        }
        if (this.state != CheckboxState.DISABLED) {
            this.state = CheckboxState.NORMAL;
        }
        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float f2, float f3) {
        if (this.style != null) {
            int i4 = this.state.ordinal() + (this.state == CheckboxState.PRESSED ? (this.checked ? 0 : 4) : (this.checked ? 4 : 0));
            spriteBatch.draw(this.getX() + f2, this.getY() + f3, this.style.frameX[i4], this.style.frameY[i4], this.style.width, this.style.height, this.getColor());
            if (this.label != null) {
                this.label.draw(spriteBatch, this.getX() + f2, this.getY() + f3);
            }
        }
    }
}


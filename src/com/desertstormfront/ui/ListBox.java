/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.BitmapFont;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.ListBoxListener;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.Widget;

public class ListBox
extends Widget {
    private int selectedIndex;
    private String[] items;
    private ActionListener listener;
    private boolean changed = false;
    private Button previousButton;
    private Button nextButton;
    private Label label;

    public ListBox(ButtonStyle buttonStyle, ButtonStyle buttonStyle2, BitmapFont bitmapFont) {
        this(buttonStyle, buttonStyle2, bitmapFont, 0);
    }

    public ListBox(ButtonStyle buttonStyle, ButtonStyle buttonStyle2, BitmapFont bitmapFont, int i4) {
        this.previousButton = new Button(buttonStyle);
        this.previousButton.setData(0);
        this.previousButton.pack();
        this.nextButton = new Button(buttonStyle2);
        this.nextButton.setData(1);
        this.nextButton.pack();
        this.label = new Label(bitmapFont);
        this.label.setMaxWidth(i4);
        this.label.setY((float)((buttonStyle.width - bitmapFont.getLineHeight()) / 2));
        this.label.setAlign(Align.CENTER);
        this.listener = new ListBoxListener(this);
        this.selectedIndex = -1;
        this.items = null;
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public void setSelectedIndex(int i1) {
        if (this.items == null && i1 != -1 || this.items != null && (i1 < 0 || i1 >= this.items.length)) {
            throw new IllegalArgumentException("Illegal Selection: " + i1);
        }
        this.selectedIndex = i1;
        if (this.items != null) {
            this.label.setText(this.items[i1]);
            this.previousButton.setEnabled(i1 > 0);
            this.nextButton.setEnabled(i1 < this.items.length - 1);
        } else {
            this.label.setText("");
            this.previousButton.setEnabled(false);
            this.nextButton.setEnabled(false);
        }
    }

    public void setLabelMaxWidth(int i1) {
        this.label.setMaxWidth(i1);
    }

    public void setLabelAlign(int i1) {
        this.label.a(i1);
    }

    public int getArrowWidth() {
        return Math.round(this.previousButton.getWidth() + this.nextButton.getWidth());
    }

    public String[] getItems() {
        return this.items;
    }

    public void setItems(String[] stringArray) {
        this.items = stringArray;
        this.setSelectedIndex(0);
    }

    @Override
    public void pack() {
        this.setWidth(this.previousButton.getWidth() + this.nextButton.getWidth() + (float)this.label.getMaxWidth());
        this.setHeight(this.previousButton.getHeight());
    }

    @Override
    public boolean handleInput(ActionListener actionListener, float f2, float f3, boolean bl) {
        if (this.isVisible()) {
            boolean i5 = false;
            if (this.previousButton.handleInput(this.listener, f2 - this.getX(), f3 - this.getY(), bl)) {
                i5 = true;
            }
            if (this.nextButton.handleInput(this.listener, f2 - this.getX() - (this.getWidth() - this.nextButton.getWidth()), f3 - this.getY(), bl)) {
                i5 = true;
            }
            if (this.changed) {
                this.changed = false;
                actionListener.onAction(this);
            }
            return i5;
        }
        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float f2, float f3) {
        this.previousButton.draw(spriteBatch, this.getX() + f2, this.getY() + f3);
        this.nextButton.draw(spriteBatch, this.getX() + f2 + this.getWidth() - this.nextButton.getWidth(), this.getY() + f3);
        this.label.draw(spriteBatch, this.getX() + f2 + this.previousButton.getWidth(), this.getY() + f3);
    }

    static /* synthetic */ int getSelectedIndex(ListBox listBox) {
        return listBox.selectedIndex;
    }

    static /* synthetic */ void setChanged(ListBox listBox, boolean bl) {
        listBox.changed = bl;
    }
}


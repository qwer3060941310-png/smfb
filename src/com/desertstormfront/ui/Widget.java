/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.SpriteBatch;

public abstract class Widget {
    private Object data;
    private boolean visible;
    private float x;
    private float y;
    private float width;
    private float height;
    private int color;

    protected Widget() {
        this.setVisible(true);
        this.setColor(-1);
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object object) {
        this.data = object;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float f1) {
        this.x = f1;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float f1) {
        this.y = f1;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float f1) {
        this.width = f1;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float f1) {
        this.height = f1;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i1) {
        this.color = i1;
    }

    public boolean contains(float f1, float f2) {
        return f1 >= this.x && f2 >= this.y && f1 < this.x + this.width && f2 < this.y + this.height;
    }

    public abstract void pack();

    public abstract boolean handleInput(ActionListener var1, float var2, float var3, boolean var4);

    public void draw(SpriteBatch spriteBatch) {
        if (this.visible) {
            this.draw(spriteBatch, 0.0f, 0.0f);
        }
    }

    protected abstract void draw(SpriteBatch var1, float var2, float var3);
}


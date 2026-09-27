/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.ClipRect;
import com.desertstormfront.ui.ScrollBarStyle;
import com.desertstormfront.ui.ScrollState;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.Widget;

public class ScrollPane
extends Widget {
    private ScrollState state;
    private ScrollBarStyle style;
    private Widget content;
    private ClipRect clipRect;
    private long lastInputTime;
    private float dragStartY;
    private int historyIndex;
    private long[] historyTimes;
    private float[] historyPositions;
    private float dragStartContentY;
    private float flingVelocity;
    private int dragRegion;
    private boolean flinging;

    public ScrollPane(ScrollBarStyle scrollBarStyle) {
        this(scrollBarStyle, null);
    }

    public ScrollPane(ScrollBarStyle scrollBarStyle, Widget widget) {
        this.style = scrollBarStyle;
        this.content = widget;
        this.clipRect = new ClipRect();
        this.state = ScrollState.IDLE;
        this.flinging = false;
        this.historyIndex = 0;
        this.historyTimes = new long[16];
        this.historyPositions = new float[this.historyTimes.length];
    }

    public Widget getContent() {
        return this.content;
    }

    public void setContent(Widget widget) {
        this.content = widget;
    }

    public int getScrollBarWidth() {
        return this.style.trackWidth;
    }

    public int getScrollBarHeight() {
        if (this.content != null && this.content.getHeight() > 0.0f) {
            int i1 = (int)(this.getHeight() * this.getHeight() / this.content.getHeight());
            if (i1 < this.style.upArrowHeight + this.style.downArrowHeight) {
                i1 = this.style.upArrowHeight + this.style.downArrowHeight;
            }
            return i1;
        }
        return 0;
    }

    @Override
    public void pack() {
        if (this.content != null) {
            this.content.pack();
        }
    }

    @Override
    public boolean handleInput(ActionListener actionListener, float f2, float f3, boolean bl) {
        boolean i8;
        long l5 = System.nanoTime();
        float f7 = (float)(l5 - this.lastInputTime) / 1.0E9f;
        if (this.isVisible() && this.content != null) {
            int n;
            if (this.content.getHeight() > this.getHeight()) {
                n = f2 - this.getX() >= this.getWidth() - (float)this.getScrollBarWidth() ? -1 : (this.content.handleInput(actionListener, f2 - this.getX(), f3 - this.getY(), bl) ? 0 : 1);
            } else {
                this.content.handleInput(actionListener, f2 - this.getX(), f3 - this.getY(), bl);
                n = 0;
            }
            if (n != 0) {
                if (this.state == ScrollState.IDLE || this.state == ScrollState.HOVER) {
                    if (bl && this.contains(f2, f3)) {
                        this.dragRegion = n;
                        this.flinging = false;
                        this.dragStartY = f3;
                        this.dragStartContentY = this.content.getY();
                        this.state = ScrollState.DRAGGING;
                    } else {
                        this.state = ScrollState.HOVER;
                    }
                } else if (this.state == ScrollState.DRAGGING) {
                    if (!bl) {
                        this.flinging = true;
                        long l = l5 - 160000000L;
                        long l12 = Long.MAX_VALUE;
                        int i14 = this.historyIndex;
                        int n2 = 0;
                        while (n2 < this.historyTimes.length) {
                            long l2 = Math.abs(l - this.historyTimes[(this.historyTimes.length + this.historyIndex - n2) % this.historyTimes.length]);
                            if (l2 < l12) {
                                i14 = (this.historyTimes.length + this.historyIndex - n2) % this.historyTimes.length;
                                l12 = l2;
                            }
                            ++n2;
                        }
                        float f = (float)(l5 - this.historyTimes[i14]) / 1.0E9f;
                        float f4 = f3 - this.historyPositions[i14];
                        if (this.dragRegion == -1) {
                            f4 = -1.0f * f4 * (this.content.getHeight() - this.getHeight()) / (this.getHeight() - (float)this.getScrollBarHeight());
                        }
                        this.flingVelocity = f4 / f;
                        this.state = ScrollState.IDLE;
                    } else {
                        float f = f3 - this.dragStartY;
                        if (this.dragRegion == -1) {
                            f = -1.0f * f * (this.content.getHeight() - this.getHeight()) / (this.getHeight() - (float)this.getScrollBarHeight());
                        }
                        this.setContentY(this.dragStartContentY + f);
                    }
                }
            }
            i8 = true;
        } else {
            this.state = ScrollState.IDLE;
            i8 = false;
        }
        if (this.flinging) {
            float f = 2.64f;
            float f5 = Math.abs(this.flingVelocity);
            if (f5 > f) {
                this.flingVelocity -= f * this.flingVelocity / f5;
                float f11 = this.flingVelocity * f7;
                this.setContentY(this.content.getY() + f11);
            } else {
                this.flinging = false;
            }
        }
        this.lastInputTime = l5;
        this.historyIndex = (this.historyIndex + 1) % this.historyTimes.length;
        this.historyTimes[this.historyIndex] = l5;
        this.historyPositions[this.historyIndex] = f3;
        return i8;
    }

    private void setContentY(float f1) {
        if (f1 > 0.0f) {
            f1 = 0.0f;
            this.flinging = false;
        } else {
            float f2 = this.getHeight() - this.content.getHeight();
            if (f2 >= 0.0f) {
                f1 = 0.0f;
                this.flinging = false;
            } else if (f1 < f2) {
                f1 = f2;
                this.flinging = false;
            }
        }
        this.content.setY(f1);
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float f2, float f3) {
        if (this.content != null) {
            this.clipRect.setLeft(Math.round(this.getX() + f2));
            this.clipRect.setRight(Math.round(this.getX() + f2 + this.getWidth() - 1.0f));
            this.clipRect.setTop(Math.round(this.getY() + f3));
            this.clipRect.setBottom(Math.round(this.getY() + f3 + this.getHeight() - 1.0f));
            spriteBatch.setClipRect(this.clipRect);
            this.content.draw(spriteBatch, this.getX() + f2, this.getY() + f3);
            spriteBatch.setClipRect(null);
            if (this.style != null && this.content.getHeight() > this.getHeight()) {
                int i4 = this.getScrollBarHeight();
                int i5 = i4 / 2;
                int i6 = (int)((this.getHeight() - (float)i4) * this.content.getY() / (this.getHeight() - this.content.getHeight()));
                int i7 = this.state.ordinal();
                int i8 = (int)(this.getX() + f2 + this.getWidth() - (float)this.getScrollBarWidth());
                int i9 = (int)(this.getY() + f3 + (float)i6 + (float)i5);
                if (i4 > this.style.upArrowHeight + this.style.downArrowHeight) {
                    spriteBatch.draw(i8, i9 - i5 + this.style.upArrowHeight, this.style.trackWidth, i4 - this.style.upArrowHeight - this.style.downArrowHeight, this.style.trackFrameX[i7], this.style.trackFrameY[i7], this.style.trackWidth, this.style.trackHeight, this.getColor());
                }
                spriteBatch.draw(i8, i9 - i5, this.style.upArrowFrameX[i7], this.style.upArrowFrameY[i7], this.style.upArrowWidth, this.style.upArrowHeight, this.getColor());
                spriteBatch.draw(i8, i9 + i5 - this.style.downArrowHeight, this.style.downArrowFrameX[i7], this.style.downArrowFrameY[i7], this.style.downArrowWidth, this.style.downArrowHeight, this.getColor());
            }
        }
    }
}


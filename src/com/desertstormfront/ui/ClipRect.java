/*
 * Clip window for the immediate-mode renderer: left/top/right/bottom in screen pixels. SpriteBatch
 * rejects quads that fall outside it and shrinks the rest, texture coordinates included.
 *
 * Deobfuscation: fields and accessors a..d became left/top/right/bottom - the order is fixed by
 * that clipping code (x is compared against a()/c(), y against b()/d()).
 * Evidence: run\map-ui-render.tsv.
 */
package com.desertstormfront.ui;

public final class ClipRect {
    private int left;
    private int top;
    private int right;
    private int bottom;

    public final int getLeft() {
        return this.left;
    }

    public final void setLeft(int i1) {
        this.left = i1;
    }

    public final int getTop() {
        return this.top;
    }

    public final void setTop(int i1) {
        this.top = i1;
    }

    public final int getRight() {
        return this.right;
    }

    public final void setRight(int i1) {
        this.right = i1;
    }

    public final int getBottom() {
        return this.bottom;
    }

    public final void setBottom(int i1) {
        this.bottom = i1;
    }
}


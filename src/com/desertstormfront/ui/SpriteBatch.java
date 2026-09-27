/*
 * Immediate-mode quad batcher in front of BatchRenderer: every draw(...) variant funnels into the
 * short-coordinate overload, which clips against clipRect and appends two triangles (position, u/v
 * and the packed colour) to the shared vertex arrays, flushing when they are full.
 *
 * Deobfuscation: fields a..e became renderer/clipRect/color/colorRG/colorBA - the last two are the
 * ARGB colour split into byte pairs for the shader; the five a(...) overloads became draw(...) and
 * the private a(int) setColor. Evidence: run\map-ui-render.tsv.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.BatchRenderer;
import com.desertstormfront.ui.ClipRect;
import com.noblemaster.lib.math.MathHelper;

public final class SpriteBatch {
    private BatchRenderer renderer;
    private ClipRect clipRect;
    private int color;
    private short colorRG;
    private short colorBA;

    SpriteBatch(BatchRenderer batchRenderer) {
        this.renderer = batchRenderer;
        this.clipRect = null;
        this.setColor(-1);
    }

    private void setColor(int i1) {
        if (this.color != i1) {
            this.color = i1;
            this.colorRG = (short)(i1 & 0xFF00 | i1 >> 16 & 0xFF);
            this.colorBA = (short)(i1 >> 16 & 0xFF00 | i1 & 0xFF);
        }
    }

    public final void setClipRect(ClipRect clipRect) {
        this.clipRect = clipRect;
    }

    public final void draw(float f1, float f2, int i3, int i4, int i5, int i6, int i7) {
        this.draw(MathHelper.round(f1), MathHelper.round(f2), i5, i6, i3, i4, i5, i6, i7);
    }

    public final void draw(int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.draw(i1, i2, i5, i6, i3, i4, i5, i6, i7);
    }

    public final void draw(float f1, float f2, float f3, float f4, int i5, int i6, int i7, int i8, int i9) {
        this.draw(MathHelper.round(f1), MathHelper.round(f2), MathHelper.round(f3), MathHelper.round(f4), i5, i6, i7, i8, i9);
    }

    public final void draw(int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        this.draw((short)i1, (short)i2, (short)i3, (short)i4, (short)i5, (short)i6, (short)i7, (short)i8, i9);
    }

    public final void draw(short s, short s2, short s3, short s4, short s5, short s6, short s7, short s8, int i9) {
        if (this.renderer.shortDataCount >= this.renderer.shortDataCapacity) {
            this.renderer.flush();
        }
        if (this.clipRect != null) {
            int i10;
            if (s > this.clipRect.getRight() || s + s3 < this.clipRect.getLeft() || s2 > this.clipRect.getBottom() || s2 + s4 < this.clipRect.getTop()) {
                return;
            }
            if (s < this.clipRect.getLeft()) {
                i10 = this.clipRect.getLeft() - s;
                s = (short)(s + i10);
                s3 = (short)(s3 - i10);
                s5 = (short)(s5 + i10);
                s7 = (short)(s7 - i10);
            }
            if (s + s3 > this.clipRect.getRight()) {
                i10 = s + s3 - this.clipRect.getRight();
                s3 = (short)(s3 - i10);
                s7 = (short)(s7 - i10);
            }
            if (s2 < this.clipRect.getTop()) {
                i10 = this.clipRect.getTop() - s2;
                s2 = (short)(s2 + i10);
                s4 = (short)(s4 - i10);
                s6 = (short)(s6 + i10);
                s8 = (short)(s8 - i10);
            }
            if (s2 + s4 > this.clipRect.getBottom()) {
                i10 = s2 + s4 - this.clipRect.getBottom();
                s4 = (short)(s4 - i10);
                s8 = (short)(s8 - i10);
            }
        }
        this.setColor(i9);
        if (this.renderer.useVbo) {
            this.renderer.vertexData[this.renderer.vertexDataCount++] = s;
            this.renderer.vertexData[this.renderer.vertexDataCount++] = s2;
            this.renderer.vertexData[this.renderer.vertexDataCount++] = s5;
            this.renderer.vertexData[this.renderer.vertexDataCount++] = s6;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorRG;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorBA;
            this.renderer.vertexData[this.renderer.vertexDataCount++] = (short)(s + s3);
            this.renderer.vertexData[this.renderer.vertexDataCount++] = s2;
            this.renderer.vertexData[this.renderer.vertexDataCount++] = (short)(s5 + s7);
            this.renderer.vertexData[this.renderer.vertexDataCount++] = s6;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorRG;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorBA;
            this.renderer.vertexData[this.renderer.vertexDataCount++] = (short)(s + s3);
            this.renderer.vertexData[this.renderer.vertexDataCount++] = (short)(s2 + s4);
            this.renderer.vertexData[this.renderer.vertexDataCount++] = (short)(s5 + s7);
            this.renderer.vertexData[this.renderer.vertexDataCount++] = (short)(s6 + s8);
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorRG;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorBA;
            this.renderer.vertexData[this.renderer.vertexDataCount++] = s;
            this.renderer.vertexData[this.renderer.vertexDataCount++] = (short)(s2 + s4);
            this.renderer.vertexData[this.renderer.vertexDataCount++] = s5;
            this.renderer.vertexData[this.renderer.vertexDataCount++] = (short)(s6 + s8);
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorRG;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorBA;
        } else {
            this.renderer.shortData[this.renderer.shortDataCount++] = s;
            this.renderer.shortData[this.renderer.shortDataCount++] = s2;
            this.renderer.shortData[this.renderer.shortDataCount++] = s5;
            this.renderer.shortData[this.renderer.shortDataCount++] = s6;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorRG;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorBA;
            this.renderer.shortData[this.renderer.shortDataCount++] = (short)(s + s3);
            this.renderer.shortData[this.renderer.shortDataCount++] = s2;
            this.renderer.shortData[this.renderer.shortDataCount++] = (short)(s5 + s7);
            this.renderer.shortData[this.renderer.shortDataCount++] = s6;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorRG;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorBA;
            this.renderer.shortData[this.renderer.shortDataCount++] = (short)(s + s3);
            this.renderer.shortData[this.renderer.shortDataCount++] = (short)(s2 + s4);
            this.renderer.shortData[this.renderer.shortDataCount++] = (short)(s5 + s7);
            this.renderer.shortData[this.renderer.shortDataCount++] = (short)(s6 + s8);
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorRG;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorBA;
            this.renderer.shortData[this.renderer.shortDataCount++] = s;
            this.renderer.shortData[this.renderer.shortDataCount++] = (short)(s2 + s4);
            this.renderer.shortData[this.renderer.shortDataCount++] = s5;
            this.renderer.shortData[this.renderer.shortDataCount++] = (short)(s6 + s8);
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorRG;
            this.renderer.shortData[this.renderer.shortDataCount++] = this.colorBA;
        }
    }
}


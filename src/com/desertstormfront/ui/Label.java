/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.BitmapFont;
import com.desertstormfront.ui.Glyph;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.TextMetrics;
import com.desertstormfront.ui.Widget;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.math.MathHelper;

public class Label
extends Widget {
    private BitmapFont font;
    private Align align = Align.LEFT;
    private String text;
    private int maxWidth;
    private boolean halfScale;

    public Label(BitmapFont bitmapFont) {
        this(bitmapFont, false);
    }

    public Label(BitmapFont bitmapFont, boolean bl) {
        this.font = bitmapFont;
        this.halfScale = bl;
        this.setAlign(Align.LEFT);
        this.setMaxWidth(0);
    }

    public void a(int i1) {
        this.setColor(i1);
    }

    public void setFont(BitmapFont bitmapFont) {
        this.font = bitmapFont;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        this.text = string;
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public void setMaxWidth(int i1) {
        this.maxWidth = i1;
    }

    public int getTextWidth() {
        return this.measureTextWidth(this.text, this.maxWidth);
    }

    public int measureTextWidth(String string, int i2) {
        if (i2 > 0) {
            return this.font.measureWrapped((String)string, (int)i2).height;
        }
        return this.font.measure((CharSequence)string).height;
    }

    @Override
    public void pack() {
        if (this.text != null) {
            if (this.maxWidth > 0) {
                TextMetrics textMetrics = this.font.measureWrapped(this.text, this.maxWidth);
                this.setWidth(this.maxWidth);
                this.setHeight(textMetrics.lineCount * this.font.getLineHeight());
            } else {
                TextMetrics textMetrics = this.font.measure(this.text);
                this.setWidth(textMetrics.width);
                this.setHeight(this.font.getLineHeight());
            }
        } else {
            this.setWidth(0.0f);
            this.setHeight(0.0f);
        }
    }

    @Override
    public boolean handleInput(ActionListener actionListener, float f2, float f3, boolean bl) {
        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float f2, float f3) {
        if (this.text != null) {
            if (this.maxWidth > 0) {
                this.drawWrappedText(spriteBatch, (int)(this.getX() + f2), (int)(this.getY() + f3), this.text);
            } else {
                this.drawTextLine(spriteBatch, (int)(this.getX() + f2), (int)(this.getY() + f3), this.text, 0, this.text.length());
            }
        }
    }

    private void drawWrappedText(SpriteBatch spriteBatch, int i2, int i3, String string) {
        int i5 = this.font.getLineHeight();
        int i6 = 0;
        int i7 = string.length();
        int i8 = 0;
        while (i6 < i7) {
            int i10;
            int i12;
            int i11;
            int i9 = i6 + this.font.fitWidth(string, i6, this.font.indexOfChar((CharSequence)string, '\n', i6), this.maxWidth * (this.halfScale ? 2 : 1));
            if (i9 < i7) {
                i11 = i9;
                while (i9 > i6) {
                    i12 = string.charAt(i9);
                    if (Messages.canWrapLineAt((char)i12)) break;
                    --i9;
                }
                if (i9 == i6) {
                    i9 = i11;
                    if (i9 == i6) {
                        // empty if block
                    }
                    i10 = ++i9;
                } else {
                    i12 = string.charAt(i9);
                    i10 = i9 + (Messages.isWrapCharDropped((char)i12) ? 1 : 0);
                }
            } else {
                if (i9 == i6) {
                    ++i9;
                }
                i10 = i7;
            }
            i11 = 0;
            if (this.align != Align.LEFT) {
                i12 = MathHelper.round(this.font.measure((CharSequence)string, (int)i6, (int)i9).width);
                i11 = this.maxWidth - i12;
                if (this.align == Align.CENTER) {
                    i11 /= 2;
                }
            }
            i12 = this.drawTextLine(spriteBatch, i2 + i11, i3, string, i6, i9);
            i8 = Math.max(i8, i12);
            i6 = i10;
            i3 += this.halfScale ? i5 / 2 : i5;
        }
    }

    private int drawTextLine(SpriteBatch spriteBatch, int i2, int i3, CharSequence charSequence, int i5, int i6) {
        i3 += this.halfScale ? this.font.getAscent() / 2 : this.font.getAscent();
        float f7 = i2;
        Glyph glyph = null;
        while (i5 < i6) {
            if ((glyph = this.font.getGlyph(charSequence.charAt(i5++))) == null) continue;
            this.drawGlyph(spriteBatch, glyph, i2, i3);
            i2 += this.halfScale ? glyph.xAdvance / 2 : glyph.xAdvance;
            break;
        }
        while (i5 < i6) {
            char i9;
            Glyph glyph2;
            if ((glyph2 = this.font.getGlyph(i9 = charSequence.charAt(i5++))) == null) continue;
            glyph = glyph2;
            this.drawGlyph(spriteBatch, glyph, i2 += glyph.getData(i9), i3);
            i2 += this.halfScale ? glyph2.xAdvance / 2 : glyph2.xAdvance;
        }
        return (int)((float)i2 - f7);
    }

    private void drawGlyph(SpriteBatch spriteBatch, Glyph glyph, int i3, int i4) {
        if (this.halfScale) {
            spriteBatch.draw((int)((short)(i3 + glyph.xOffset / 2)), (int)((short)(i4 + glyph.yOffset / 2)), (short)glyph.width / 2, (short)glyph.height / 2, (int)((short)glyph.x), (int)((short)glyph.y), (int)((short)glyph.width), (int)((short)glyph.height), this.getColor());
        } else {
            spriteBatch.draw((short)(i3 + glyph.xOffset), (short)(i4 + glyph.yOffset), (short)glyph.width, (short)glyph.height, (short)glyph.x, (short)glyph.y, (short)glyph.width, (short)glyph.height, this.getColor());
        }
    }
}


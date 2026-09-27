/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.Glyph;
import com.desertstormfront.ui.TextMetrics;
import com.noblemaster.lib.io.GameFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class BitmapFont {
    private String imageFileName;
    private int lineHeight;
    private int base;
    private int capHeight;
    private int ascent;
    private int spaceWidth;
    private int xHeight;
    private List glyphs;
    private Glyph[][] glyphTable = new Glyph[128][];
    private final TextMetrics textMetrics = new TextMetrics();

    public BitmapFont(GameFile gameFile, int i2, int i3) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gameFile.getFileHandle().read()), 512);
        try {
            try {
                Object object;
                int i10;
                Object object2;
                Object object3;
                bufferedReader.readLine();
                String[] stringArray = bufferedReader.readLine().split(" ", 4);
                if (stringArray.length < 4) {
                    throw new RuntimeException("Invalid font file: " + gameFile);
                }
                if (!stringArray[1].startsWith("lineHeight=")) {
                    throw new RuntimeException("Invalid font file: " + gameFile);
                }
                this.lineHeight = Integer.parseInt(stringArray[1].substring(11));
                if (!stringArray[2].startsWith("base=")) {
                    throw new RuntimeException("Invalid font file: " + gameFile);
                }
                this.base = Integer.parseInt(stringArray[2].substring(5));
                String[] stringArray2 = bufferedReader.readLine().split(" ", 4);
                if (!stringArray2[2].startsWith("file=")) {
                    throw new RuntimeException("Invalid font file: " + gameFile);
                }
                this.imageFileName = gameFile.getFileHandle().parent().child(stringArray2[2].substring(5)).name();
                this.glyphs = new ArrayList();
                while ((object3 = bufferedReader.readLine()) != null && !((String)object3).startsWith("kernings ")) {
                    if (!((String)object3).startsWith("char ")) continue;
                    Glyph glyph = new Glyph();
                    StringTokenizer stringTokenizer = new StringTokenizer((String)object3, " =");
                    stringTokenizer.nextToken();
                    stringTokenizer.nextToken();
                    i10 = Integer.parseInt(stringTokenizer.nextToken());
                    if (i10 > 65535) continue;
                    Glyph[] glyphRow = this.glyphTable[i10 / 512];
                    if (glyphRow == null) {
                        glyphRow = new Glyph[512];
                        this.glyphTable[i10 / 512] = glyphRow;
                    }
                    glyphRow[i10 & 0x1FF] = glyph;
                    stringTokenizer.nextToken();
                    glyph.x = Integer.parseInt(stringTokenizer.nextToken()) + i2;
                    stringTokenizer.nextToken();
                    glyph.y = Integer.parseInt(stringTokenizer.nextToken()) + i3;
                    stringTokenizer.nextToken();
                    glyph.width = Integer.parseInt(stringTokenizer.nextToken());
                    stringTokenizer.nextToken();
                    glyph.height = Integer.parseInt(stringTokenizer.nextToken());
                    stringTokenizer.nextToken();
                    glyph.xOffset = Integer.parseInt(stringTokenizer.nextToken());
                    stringTokenizer.nextToken();
                    glyph.yOffset = Integer.parseInt(stringTokenizer.nextToken());
                    stringTokenizer.nextToken();
                    glyph.xAdvance = Integer.parseInt(stringTokenizer.nextToken());
                    glyph.right = glyph.x + glyph.width;
                    glyph.bottom = glyph.y + glyph.height;
                    glyph.index = this.glyphs.size();
                    this.glyphs.add(glyph);
                }
                while ((object3 = bufferedReader.readLine()) != null && ((String)object3).startsWith("kerning ")) {
                    object2 = new StringTokenizer((String)object3, " =");
                    ((StringTokenizer)object2).nextToken();
                    ((StringTokenizer)object2).nextToken();
                    int n = Integer.parseInt(((StringTokenizer)object2).nextToken());
                    ((StringTokenizer)object2).nextToken();
                    i10 = Integer.parseInt(((StringTokenizer)object2).nextToken());
                    if (n < 0 || n > 65535 || i10 < 0 || i10 > 65535) continue;
                    object = this.getGlyph((char)n);
                    ((StringTokenizer)object2).nextToken();
                    int i12 = Integer.parseInt(((StringTokenizer)object2).nextToken());
                    ((Glyph)object).setData(i10, i12);
                }
                object3 = this.getGlyph(' ');
                this.spaceWidth = object3 != null ? ((Glyph)object3).xAdvance + ((Glyph)object3).width : 1;
                object3 = this.getGlyph('x');
                this.xHeight = object3 != null ? ((Glyph)object3).height : 1;
                object3 = this.getGlyph('M');
                this.capHeight = object3 != null ? ((Glyph)object3).height : 1;
                this.ascent = this.capHeight - this.base;
            }
            catch (Exception exception) {
                throw new RuntimeException("Error loading font file: " + gameFile, exception);
            }
        }
        catch (Throwable throwable) {
            try {
                bufferedReader.close();
            }
            catch (IOException iOException) {}
            throw throwable;
        }
        try {
            bufferedReader.close();
        }
        catch (IOException iOException) {}
    }

    public final Glyph getGlyph(char c) {
        Glyph[] glyphArray = this.glyphTable[c / 512];
        if (glyphArray != null) {
            return glyphArray[c & 0x1FF];
        }
        return null;
    }

    public final TextMetrics measure(CharSequence charSequence) {
        return this.measure(charSequence, 0, charSequence.length());
    }

    public final TextMetrics measure(CharSequence charSequence, int i2, int i3) {
        int i4 = 0;
        Glyph glyph = null;
        while (i2 < i3) {
            if ((glyph = this.getGlyph(charSequence.charAt(i2++))) == null) continue;
            i4 = glyph.xAdvance;
            break;
        }
        while (i2 < i3) {
            char i6;
            Glyph glyph2;
            if ((glyph2 = this.getGlyph(i6 = charSequence.charAt(i2++))) == null) continue;
            i4 += glyph.getData(i6);
            glyph = glyph2;
            i4 += glyph2.xAdvance;
        }
        this.textMetrics.width = i4;
        this.textMetrics.height = this.capHeight;
        this.textMetrics.lineCount = 1;
        return this.textMetrics;
    }

    public final TextMetrics measureWrapped(String string, int i2) {
        int i3 = 0;
        int i4 = 0;
        int i5 = string.length();
        int i6 = 0;
        while (i3 < i5) {
            int i8;
            int i9;
            int i7 = i3 + this.fitWidth(string, i3, this.indexOfChar((CharSequence)string, '\n', i3), i2);
            if (i7 < i5) {
                i9 = i7;
                while (i7 > i3) {
                    char i10 = string.charAt(i7);
                    if (i10 == ' ' || i10 == '\n') break;
                    --i7;
                }
                if (i7 == i3) {
                    i7 = i9;
                    if (i7 == i3) {
                        // empty if block
                    }
                    i8 = ++i7;
                } else {
                    i8 = i7 + 1;
                }
            } else {
                if (i7 == i3) {
                    ++i7;
                }
                i8 = i5;
            }
            i9 = this.measure((CharSequence)string, (int)i3, (int)i7).width;
            i6 = Math.max(i6, i9);
            i3 = i8;
            ++i4;
        }
        this.textMetrics.width = i6;
        this.textMetrics.height = this.capHeight + (i4 - 1) * this.lineHeight;
        this.textMetrics.lineCount = i4;
        return this.textMetrics;
    }

    public final int fitWidth(String string, int i2, int i3, int i4) {
        int i5 = i2;
        int i6 = 0;
        Glyph glyph = null;
        while (i5 < i3) {
            char i8 = string.charAt(i5);
            Glyph glyph2 = this.getGlyph(i8);
            if (glyph2 != null) {
                if (glyph != null) {
                    i6 += glyph.getData(i8);
                }
                glyph = glyph2;
                if (i6 + glyph2.width + glyph2.xOffset > i4) break;
                i6 += glyph2.xAdvance;
            }
            ++i5;
        }
        return i5 - i2;
    }

    public final int getLineHeight() {
        return this.lineHeight;
    }

    public final void setLineHeight(int i1) {
        this.lineHeight = i1;
    }

    public final int getAscent() {
        return this.ascent;
    }

    public final void setAscent(int i1) {
        this.ascent = i1;
    }

    public final int indexOfChar(CharSequence charSequence, char c, int i3) {
        int i4 = charSequence.length();
        while (i3 < i4) {
            if (charSequence.charAt(i3) == c) {
                return i3;
            }
            ++i3;
        }
        return i4;
    }
}


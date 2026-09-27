/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

public class Glyph {
    int index;
    int width;
    int height;
    int x;
    int y;
    int right;
    int bottom;
    int xOffset;
    int yOffset;
    int xAdvance;
    byte[][] data;

    int getData(char c) {
        byte[] byArray;
        if (this.data != null && (byArray = this.data[c >>> 9]) != null) {
            return byArray[c & 0x1FF];
        }
        return 0;
    }

    void setData(int i1, int i2) {
        byte[] byArray;
        if (this.data == null) {
            this.data = new byte[128][];
        }
        if ((byArray = this.data[i1 >>> 9]) == null) {
            byArray = new byte[512];
            this.data[i1 >>> 9] = byArray;
        }
        byArray[i1 & 0x1FF] = (byte)i2;
    }
}


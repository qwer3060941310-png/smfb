/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.intent;

import com.desertstormfront.world.Vec2;

/**
 * 影响力图，用于威胁与控制区域评估
 */
public strictfp final class InfluenceMap {
    private float[] values;
    private int width;
    private int height;
    private int cellSize;
    private float[] cellValues;
    private int cellWidth;
    private int cellHeight;

    public static InfluenceMap create(int i0, int i1, int i2) {
        InfluenceMap influenceMap = new InfluenceMap();
        influenceMap.width = i0;
        influenceMap.height = i1;
        influenceMap.values = new float[influenceMap.height * influenceMap.width];
        influenceMap.cellSize = i2;
        influenceMap.cellWidth = (i0 - 1) / i2 + 1;
        influenceMap.cellHeight = (i1 - 1) / i2 + 1;
        influenceMap.cellValues = new float[influenceMap.cellHeight * influenceMap.cellWidth];
        return influenceMap;
    }

    public void rebuild() {
        int i1 = this.cellSize;
        int i2 = i1 + i1;
        int i3 = 0;
        while (i3 < this.cellHeight) {
            int i4 = i3 * i1;
            int i5 = 0;
            while (i5 < this.cellWidth) {
                int i6 = i5 * i1;
                float f7 = 0.0f;
                int i8 = i4 - i1;
                while (i8 <= i4 + i2) {
                    int i9 = i6 - i1;
                    while (i9 <= i6 + i2) {
                        if (i8 > 0 && i8 < this.height && i9 > 0 && i9 < this.width) {
                            f7 = i8 >= i4 && i8 < i4 + i1 && i9 >= i6 && i9 < i6 + i1 ? (f7 += this.values[i8 * this.width + i9]) : (f7 += this.values[i8 * this.width + i9] * 0.7f);
                        }
                        ++i9;
                    }
                    ++i8;
                }
                this.cellValues[i3 * this.cellWidth + i5] = f7;
                ++i5;
            }
            ++i3;
        }
    }

    public int getCellSize() {
        return this.cellSize;
    }

    public float getInfluence(Vec2 vec2) {
        return this.getInfluence(vec2.getX(), vec2.getY());
    }

    public float getInfluence(float f1, float f2) {
        return this.getInfluence((int)f1, (int)f2);
    }

    public float getInfluence(int i1, int i2) {
        return this.cellValues[i2 / this.cellSize * this.cellWidth + i1 / this.cellSize];
    }

    public void setValue(int i1, int i2, float f3) {
        this.values[i2 * this.width + i1] = f3;
    }

    public float getValue(int i1, int i2) {
        return this.values[i2 * this.width + i1];
    }
}


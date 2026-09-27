/*
 * One sprite cut-out of a sprite sheet: the six columns SpriteSheetLoader parses per frame
 * ("x y w h offX offY"), with the anchor already applied as x = col0 - offX/2, y = col1 - offY/2.
 *
 * Deobfuscation: fields and getters were a..f; they are named after the config columns they carry,
 * an order fixed by both construction sites in SpriteSheetLoader. Evidence: run\map-graphics-table.tsv.
 */
package com.desertstormfront.graphics.table;

public final class SpriteRegion {
    private int x;
    private int y;
    private int width;
    private int height;
    private int offsetX;
    private int offsetY;

    public SpriteRegion(int i1, int i2, int i3, int i4, int i5, int i6) {
        this.x = i1;
        this.y = i2;
        this.width = i3;
        this.height = i4;
        this.offsetX = i5;
        this.offsetY = i6;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
    }
}


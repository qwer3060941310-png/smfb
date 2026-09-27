/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.noblemaster.lib.io.GameFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public final class GlTexture {
    private Texture texture;
    private int textureId;
    private int width;
    private int height;

    public GlTexture(GameFile gameFile, int i2, int i3, int i4, int i5) {
        this(new GameFile[]{gameFile}, new int[0], new int[0], i2, i3, i4, i5);
    }

    public GlTexture(GameFile[] gameFileArray, int[] nArray, int[] nArray2, int i4, int i5, int i6, int i7) {
        GL20 gL20 = Gdx.gl20;
        gL20.glPixelStorei(3317, 4);
        boolean i9 = false;
        if (!i9) {
            IntBuffer intBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
            gL20.glGenTextures(1, intBuffer);
            this.textureId = intBuffer.get(0);
            gL20.glBindTexture(3553, this.textureId);
            Pixmap pixmap = this.loadPixmap(gameFileArray[0].getFileHandle());
            this.width = pixmap.getWidth();
            this.height = pixmap.getHeight();
            int i12 = 1;
            while (i12 < gameFileArray.length) {
                Pixmap pixmap2 = this.loadPixmap(gameFileArray[i12].getFileHandle());
                pixmap.drawPixmap(pixmap2, nArray[i12 - 1], nArray2[i12 - 1], 0, 0, this.width, this.height);
                pixmap2.dispose();
                pixmap2 = null;
                ++i12;
            }
            gL20.glTexImage2D(3553, 0, 6408, this.width, this.height, 0, 6408, 5121, pixmap.getPixels());
            pixmap.dispose();
            pixmap = null;
        } else {
            this.texture = new Texture(gameFileArray[0].getFileHandle(), false);
            this.texture.bind();
            this.width = this.texture.getWidth();
            this.height = this.texture.getHeight();
        }
        gL20.glTexParameteri(3553, 10242, i4);
        gL20.glTexParameteri(3553, 10243, i5);
        gL20.glTexParameteri(3553, 10241, i6);
        gL20.glTexParameteri(3553, 10240, i7);
    }

    public GlTexture(int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        GL20 gL20 = Gdx.gl20;
        gL20.glPixelStorei(3317, 4);
        IntBuffer intBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        gL20.glGenTextures(1, intBuffer);
        this.textureId = intBuffer.get(0);
        gL20.glBindTexture(3553, this.textureId);
        Pixmap.Format format = i1 == 6408 ? Pixmap.Format.RGBA8888 : (i1 == 32854 ? Pixmap.Format.RGBA4444 : (i1 == 6407 ? Pixmap.Format.RGB888 : (i1 == 36194 ? Pixmap.Format.RGB565 : null)));
        Pixmap pixmap = new Pixmap(i2, i3, format);
        this.width = pixmap.getWidth();
        this.height = pixmap.getHeight();
        gL20.glTexImage2D(3553, 0, i1, i2, i3, 0, i1, 5121, pixmap.getPixels());
        pixmap.dispose();
        gL20.glTexParameteri(3553, 10242, i4);
        gL20.glTexParameteri(3553, 10243, i5);
        gL20.glTexParameteri(3553, 10241, i6);
        gL20.glTexParameteri(3553, 10240, i7);
    }

    private Pixmap loadPixmap(FileHandle fileHandle) {
        boolean i3;
        int i2 = (int)fileHandle.length();
        if (i2 == 0) {
            i2 = 512;
            i3 = false;
        } else {
            i3 = true;
        }
        byte[] byArray = new byte[i2];
        int i5 = 0;
        InputStream inputStream = fileHandle.read();
        try {
            try {
                int n;
                while (!((n = inputStream.read(byArray, i5, byArray.length - i5)) == -1 || i3 && (i5 += n) == byArray.length)) {
                    if (i3 || n != 0 || i5 != byArray.length) continue;
                    byte[] byArray2 = new byte[byArray.length * 2];
                    System.arraycopy(byArray, 0, byArray2, 0, i5);
                    byArray = byArray2;
                }
            }
            catch (IOException iOException) {
                throw new GdxRuntimeException("Error reading file: " + this, iOException);
            }
        }
        catch (Throwable throwable) {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException iOException) {}
            throw throwable;
        }
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        catch (IOException iOException) {}
        if (i5 < byArray.length) {
            byte[] byArray3 = new byte[i5];
            System.arraycopy(byArray, 0, byArray3, 0, i5);
            byArray = byArray3;
        }
        return new Pixmap(byArray, 0, byArray.length);
    }

    public final int getGlId() {
        if (this.texture != null) {
            throw new RuntimeException("libgdx Textures don't have an ID!");
        }
        return this.textureId;
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final void bind() {
        if (this.texture != null) {
            this.texture.bind();
        } else {
            GL20 gL20 = Gdx.gl20;
            gL20.glBindTexture(3553, this.textureId);
        }
    }

    public final void dispose() {
        if (this.texture != null) {
            this.texture.dispose();
        } else {
            GL20 gL20 = Gdx.gl20;
            IntBuffer intBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
            intBuffer.clear();
            intBuffer.put(this.textureId);
            intBuffer.flip();
            gL20.glBindTexture(3553, 0);
            gL20.glDeleteTextures(1, intBuffer);
        }
    }
}


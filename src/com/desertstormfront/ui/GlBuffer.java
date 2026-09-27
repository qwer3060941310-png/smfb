/*
 * Thin VBO wrapper: uploads a direct NIO buffer once (creating the GL name), then offers bind,
 * unbind, full or partial re-upload and dispose.
 *
 * Deobfuscation: fields a..d became target/sizeBytes/data/bufferId; the single-letter methods became
 * upload/uploadAll/uploadRange/bind/unbind/dispose/getSize/getData.
 * Evidence: run\map-ui-render.tsv.
 */
package com.desertstormfront.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public final class GlBuffer {
    private int target;
    private int sizeBytes;
    private Buffer data;
    private int bufferId;

    public GlBuffer(int i1, float[] fArray, int i3) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(fArray.length * 32 / 8);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.clear();
        floatBuffer.put(fArray);
        floatBuffer.flip();
        this.upload(i1, fArray.length * 32 / 8, floatBuffer, i3);
    }

    public GlBuffer(int i1, short[] sArray, int i3) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(sArray.length * 16 / 8);
        byteBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.clear();
        shortBuffer.put(sArray);
        shortBuffer.flip();
        this.upload(i1, sArray.length * 16 / 8, shortBuffer, i3);
    }

    private final void upload(int i1, int i2, Buffer buffer, int i4) {
        this.target = i1;
        this.sizeBytes = i2;
        this.data = buffer;
        GL20 gL20 = Gdx.gl20;
        IntBuffer intBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        gL20.glGenBuffers(1, intBuffer);
        this.bufferId = intBuffer.get(0);
        gL20.glBindBuffer(i1, this.bufferId);
        gL20.glBufferData(i1, i2, buffer, i4);
        gL20.glBindBuffer(i1, 0);
    }

    public final int getSize() {
        return this.sizeBytes;
    }

    public final Buffer getData() {
        return this.data;
    }

    public final void bind() {
        GL20 gL20 = Gdx.gl20;
        gL20.glBindBuffer(this.target, this.bufferId);
    }

    public final void uploadAll() {
        this.uploadRange(0, this.sizeBytes);
    }

    public final void uploadRange(int i1, int i2) {
        GL20 gL20 = Gdx.gl20;
        gL20.glBufferSubData(this.target, i1, i2, this.data);
    }

    public final void unbind() {
        GL20 gL20 = Gdx.gl20;
        gL20.glBindBuffer(this.target, 0);
    }

    public final void dispose() {
        GL20 gL20 = Gdx.gl20;
        IntBuffer intBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        intBuffer.clear();
        intBuffer.put(this.bufferId);
        intBuffer.flip();
        gL20.glBindBuffer(this.target, 0);
        gL20.glDeleteBuffers(1, intBuffer);
        this.bufferId = 0;
    }
}


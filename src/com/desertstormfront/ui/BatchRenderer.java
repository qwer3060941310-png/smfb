/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.GlBuffer;
import com.desertstormfront.ui.GlTexture;
import com.desertstormfront.ui.ShaderProgram;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.Widget;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * The batched geometry renderer behind the whole 2D UI: every widget draws into one
 * {@link SpriteBatch}, and this class owns the shader, the atlas texture and the GL buffers that
 * carry the queued vertices to the GPU.
 *
 * <p>Two vertex layouts exist, chosen once per run by {@link #useVbo}:
 * <ul>
 *   <li>Desktop/WebGL - {@link #vertexData} holds the position and texture coordinates as floats
 *       and {@link #shortData} holds the packed colour, uploaded to two array buffers.
 *   <li>GL ES - {@link #shortData} holds position, texture coordinates and colour interleaved as
 *       shorts, uploaded to a single array buffer.
 * </ul>
 * Both paths draw through the fixed quad index pattern in {@link #elementBuffers}.
 *
 * <p>The buffer arrays are a ring: {@link #bufferIndex} advances every {@link #flush()} so the GPU
 * can still read the buffer that was just submitted while the CPU fills the next one.
 *
 * <p>{@code shortData} and its two counters are package-private because {@link SpriteBatch} writes
 * them directly on the hot path.
 */
public final class BatchRenderer {
    private Widget rootWidget;
    private ShaderProgram shaderProgram;
    private int attrPosition;
    private int attrTexCoord;
    private int attrColor;
    private int uniformTexture;
    private int uniformMvpMatrix;
    private int uniformTexScale;
    private int bufferIndex;
    private GlBuffer[] elementBuffers;
    short[] shortData;
    int shortDataCount;
    int shortDataCapacity;
    private GlBuffer[] shortDataBuffers;
    boolean useVbo;
    float[] vertexData;
    int vertexDataCount;
    int vertexDataCapacity;
    private GlBuffer[] vertexDataBuffers;
    private GlTexture texture;
    private SpriteBatch spriteBatch;

    public BatchRenderer(GlTexture glTexture, int i2) {
        int n;
        this.texture = glTexture;
        this.shaderProgram = new ShaderProgram("attribute vec4 a_position;                            \nattribute vec2 a_texCoord;                            \nattribute vec4 a_color;                               \nvarying vec2 v_texCoord;                              \nvarying vec4 v_color;                                 \nuniform mat4 u_mvpMatrix;                             \nuniform float u_texScale;                             \nvoid main()                                           \n{                                                     \n   gl_Position = a_position * u_mvpMatrix;            \n   v_texCoord = a_texCoord * u_texScale;              \n   v_color = a_color;                                 \n}                                                     \n", "#ifdef GL_ES                                          \nprecision mediump float;                              \n#endif                                                \nvarying vec2 v_texCoord;                              \nvarying vec4 v_color;                                 \nuniform sampler2D s_texture;                          \nvoid main()                                           \n{                                                     \n  gl_FragColor = texture2D(s_texture, v_texCoord);    \n  gl_FragColor *= v_color;                            \n}                                                     \n");
        this.attrPosition = this.shaderProgram.getAttributeLocation("a_position");
        this.attrTexCoord = this.shaderProgram.getAttributeLocation("a_texCoord");
        this.attrColor = this.shaderProgram.getAttributeLocation("a_color");
        this.uniformTexture = this.shaderProgram.getUniformLocation("s_texture");
        this.uniformMvpMatrix = this.shaderProgram.getUniformLocation("u_mvpMatrix");
        this.uniformTexScale = this.shaderProgram.getUniformLocation("u_texScale");
        this.shaderProgram.bind();
        GL20 gL20 = Gdx.gl20;
        gL20.glUniform1f(this.uniformTexScale, 1.0f / (float)glTexture.getWidth());
        this.useVbo = Gdx.app.getType() == Application.ApplicationType.Desktop || Gdx.app.getType() == Application.ApplicationType.WebGL;
        int i4 = i2 / 128;
        if (i4 == 0) {
            i4 = 1;
        }
        if (this.useVbo) {
            this.vertexData = new float[2048];
            this.vertexDataCount = 0;
            this.vertexDataCapacity = this.vertexData.length;
            this.vertexDataBuffers = new GlBuffer[i4];
            n = 0;
            while (n < i4) {
                this.vertexDataBuffers[n] = new GlBuffer(34962, this.vertexData, 35040);
                ++n;
            }
        }
        this.shortData = new short[(this.useVbo ? 8 : 24) * 128];
        this.shortDataCount = 0;
        this.shortDataCapacity = this.shortData.length;
        this.shortDataBuffers = new GlBuffer[i4];
        n = 0;
        while (n < i4) {
            this.shortDataBuffers[n] = new GlBuffer(34962, this.shortData, 35040);
            ++n;
        }
        short[] sArray = new short[768];
        int i6 = 0;
        while (i6 < 128) {
            sArray[6 * i6 + 0] = (short)(4 * i6 + 0);
            sArray[6 * i6 + 1] = (short)(4 * i6 + 1);
            sArray[6 * i6 + 2] = (short)(4 * i6 + 2);
            sArray[6 * i6 + 3] = (short)(4 * i6 + 0);
            sArray[6 * i6 + 4] = (short)(4 * i6 + 2);
            sArray[6 * i6 + 5] = (short)(4 * i6 + 3);
            ++i6;
        }
        this.elementBuffers = new GlBuffer[i4];
        i6 = 0;
        while (i6 < i4) {
            this.elementBuffers[i6] = new GlBuffer(34963, sArray, 35044);
            ++i6;
        }
        this.bufferIndex = 0;
        this.spriteBatch = new SpriteBatch(this);
    }

    /** The atlas every widget is drawn from. */
    public GlTexture getTexture() {
        return this.texture;
    }

    /** The widget the UI installed with {@link #setRootWidget}; null until then. */
    public final Widget getRootWidget() {
        return this.rootWidget;
    }

    /** Installs the widget tree that {@link #render()} walks. */
    public final void setRootWidget(Widget widget) {
        this.rootWidget = widget;
    }

    /** Forwards one input event to the widget tree; false when there is none or nobody consumed it. */
    public final boolean handleInput(ActionListener actionListener, float f2, float f3, boolean bl) {
        if (this.rootWidget != null) {
            return this.rootWidget.handleInput(actionListener, f2, f3, bl);
        }
        return false;
    }

    /** Rebuilds the model-view-projection matrix for a viewport of the given size in pixels. */
    public final void setViewportSize(int i1, int i2) {
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(new float[]{2.0f / (float)i1, 0.0f, 0.0f, -1.0f, 0.0f, -2.0f / (float)i2, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
        floatBuffer.flip();
        this.shaderProgram.bind();
        GL20 gL20 = Gdx.gl20;
        gL20.glUniformMatrix4fv(this.uniformMvpMatrix, 1, false, floatBuffer);
        gL20.glUniform1i(this.uniformTexture, 0);
    }

    /** Draws the widget tree and flushes whatever the batch still holds. */
    public final void render() {
        if (this.rootWidget != null) {
            this.shaderProgram.bind();
            this.texture.bind();
            this.rootWidget.draw(this.spriteBatch);
            this.flush();
        }
    }

    /**
     * Uploads and draws the queued geometry, then advances the buffer ring. Does nothing when the
     * batch is empty.
     */
    void flush() {
        if (this.shortDataCount > 0) {
            int i2;
            GL20 gL20 = Gdx.gl20;
            if (this.useVbo) {
                i2 = this.vertexDataCount / 16;
                GlBuffer glBuffer = this.vertexDataBuffers[this.bufferIndex];
                FloatBuffer floatBuffer = (FloatBuffer)glBuffer.getData();
                floatBuffer.position(0);
                floatBuffer.limit(this.vertexDataCount);
                floatBuffer.put(this.vertexData, 0, this.vertexDataCount);
                floatBuffer.flip();
                glBuffer.bind();
                glBuffer.uploadRange(0, i2 * 64);
                this.vertexDataCount = 0;
                gL20.glVertexAttribPointer(this.attrPosition, 2, 5126, false, 16, 0);
                gL20.glEnableVertexAttribArray(this.attrPosition);
                gL20.glVertexAttribPointer(this.attrTexCoord, 2, 5126, false, 16, 8);
                gL20.glEnableVertexAttribArray(this.attrTexCoord);
                GlBuffer glBuffer2 = this.shortDataBuffers[this.bufferIndex];
                ShortBuffer shortBuffer = (ShortBuffer)glBuffer2.getData();
                shortBuffer.position(0);
                shortBuffer.limit(this.shortDataCount);
                shortBuffer.put(this.shortData, 0, this.shortDataCount);
                shortBuffer.flip();
                glBuffer2.bind();
                glBuffer2.uploadRange(0, i2 * 16);
                this.shortDataCount = 0;
                gL20.glVertexAttribPointer(this.attrColor, 4, 5121, true, 4, 0);
                gL20.glEnableVertexAttribArray(this.attrColor);
            } else {
                i2 = this.shortDataCount / 24;
                GlBuffer glBuffer = this.shortDataBuffers[this.bufferIndex];
                ShortBuffer shortBuffer = (ShortBuffer)glBuffer.getData();
                shortBuffer.position(0);
                shortBuffer.limit(this.shortDataCount);
                shortBuffer.put(this.shortData, 0, this.shortDataCount);
                shortBuffer.flip();
                glBuffer.bind();
                glBuffer.uploadRange(0, i2 * 48);
                this.shortDataCount = 0;
                gL20.glVertexAttribPointer(this.attrPosition, 2, 5122, false, 12, 0);
                gL20.glEnableVertexAttribArray(this.attrPosition);
                gL20.glVertexAttribPointer(this.attrTexCoord, 2, 5122, false, 12, 4);
                gL20.glEnableVertexAttribArray(this.attrTexCoord);
                gL20.glVertexAttribPointer(this.attrColor, 4, 5121, true, 12, 8);
                gL20.glEnableVertexAttribArray(this.attrColor);
            }
            this.elementBuffers[this.bufferIndex].bind();
            gL20.glDrawElements(4, i2 * 6, 5123, 0);
            gL20.glDisableVertexAttribArray(this.attrPosition);
            gL20.glDisableVertexAttribArray(this.attrTexCoord);
            gL20.glDisableVertexAttribArray(this.attrColor);
            this.bufferIndex = (this.bufferIndex + 1) % this.shortDataBuffers.length;
        }
    }

    /** Releases every GL object this renderer created - buffers, atlas texture and shader. */
    public final void dispose() {
        int i1 = 0;
        while (i1 < this.elementBuffers.length) {
            this.elementBuffers[i1].dispose();
            ++i1;
        }
        i1 = 0;
        while (i1 < this.shortDataBuffers.length) {
            this.shortDataBuffers[i1].dispose();
            ++i1;
        }
        if (this.vertexDataBuffers != null) {
            i1 = 0;
            while (i1 < this.vertexDataBuffers.length) {
                this.vertexDataBuffers[i1].dispose();
                ++i1;
            }
        }
        this.texture.dispose();
        this.shaderProgram.dispose();
    }
}


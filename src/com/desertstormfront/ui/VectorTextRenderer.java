/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.ui.GlBuffer;
import com.desertstormfront.ui.ShaderProgram;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

public final class VectorTextRenderer {
    private static final Map glyphStrokes = new HashMap();
    private ShaderProgram shaderProgram = new ShaderProgram("attribute vec4 a_position;                            \nattribute vec4 a_color;                               \nvarying vec4 v_color;                                 \nuniform mat4 u_mvpMatrix;                             \nvoid main()                                           \n{                                                     \n   gl_Position = a_position;                          \n   v_color = a_color;                                 \n   gl_Position *= u_mvpMatrix;                        \n}                                                     \n", "#ifdef GL_ES                                          \nprecision mediump float;                              \n#endif                                                \nvarying vec4 v_color;                                 \nvoid main()                                           \n{                                                     \n  gl_FragColor = v_color;                             \n}                                                     \n");
    private int positionAttrib = this.shaderProgram.getAttributeLocation("a_position");
    private int colorAttrib = this.shaderProgram.getAttributeLocation("a_color");
    private int mvpMatrixUniform = this.shaderProgram.getUniformLocation("u_mvpMatrix");
    private int currentBuffer;
    private GlBuffer[] indexBuffers;
    short[] shortData;
    int shortCount;
    int shortCapacity;
    private GlBuffer[] shortBuffers;
    boolean useFloatVertices = Gdx.app.getType() == Application.ApplicationType.Desktop || Gdx.app.getType() == Application.ApplicationType.WebGL;
    float[] floatData;
    int floatCount;
    int floatCapacity;
    private GlBuffer[] floatBuffers;
    private int currentColor;
    private short packedColorA;
    private short packedColorB;

    static {
        glyphStrokes.put(Character.valueOf('A'), new float[]{0.0f, 1.0f, 0.5f, 0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 0.25f, 0.5f, 0.75f, 0.5f});
        glyphStrokes.put(Character.valueOf('B'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f, 0.25f, 0.0f, 0.5f, 0.0f, 0.5f, 1.0f, 0.75f, 1.0f, 0.75f, 0.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('C'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('D'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f, 0.5f, 0.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('E'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 0.75f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('F'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 0.75f, 0.5f});
        glyphStrokes.put(Character.valueOf('G'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.5f});
        glyphStrokes.put(Character.valueOf('H'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.5f, 1.0f, 0.5f});
        glyphStrokes.put(Character.valueOf('I'), new float[]{0.5f, 0.0f, 0.5f, 1.0f});
        glyphStrokes.put(Character.valueOf('J'), new float[]{0.5f, 0.0f, 0.5f, 1.0f, 0.0f, 1.0f, 0.5f, 1.0f});
        glyphStrokes.put(Character.valueOf('K'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('L'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('M'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 1.0f, 0.0f});
        glyphStrokes.put(Character.valueOf('N'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('O'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('P'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f, 0.25f, 0.0f, 0.5f});
        glyphStrokes.put(Character.valueOf('Q'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.5f, 0.5f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('R'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f, 0.25f, 0.0f, 0.5f, 0.0f, 0.5f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('S'), new float[]{0.0f, 0.25f, 1.0f, 0.0f, 0.0f, 0.25f, 1.0f, 0.75f, 0.0f, 1.0f, 1.0f, 0.75f});
        glyphStrokes.put(Character.valueOf('T'), new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 0.0f, 0.5f, 1.0f});
        glyphStrokes.put(Character.valueOf('U'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('V'), new float[]{0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 1.0f, 1.0f, 0.0f});
        glyphStrokes.put(Character.valueOf('W'), new float[]{0.0f, 0.0f, 0.25f, 1.0f, 0.25f, 1.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.75f, 1.0f, 0.75f, 1.0f, 1.0f, 0.0f});
        glyphStrokes.put(Character.valueOf('X'), new float[]{0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('Y'), new float[]{0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.5f, 0.5f, 0.5f, 1.0f});
        glyphStrokes.put(Character.valueOf('Z'), new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('0'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('1'), new float[]{0.5f, 0.0f, 0.5f, 1.0f, 0.5f, 0.0f, 0.25f, 0.25f});
        glyphStrokes.put(Character.valueOf('2'), new float[]{0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 1.0f, 0.5f, 0.0f, 0.5f, 0.0f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('3'), new float[]{1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('4'), new float[]{0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 1.0f, 0.5f, 1.0f, 0.0f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('5'), new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 1.0f, 0.5f, 1.0f, 0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('6'), new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f, 0.5f, 1.0f, 0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('7'), new float[]{0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('8'), new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f});
        glyphStrokes.put(Character.valueOf('9'), new float[]{0.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f});
    }

    public VectorTextRenderer(int i1) {
        int n;
        int i2 = 256;
        int i3 = i1 / i2;
        if (i3 == 0) {
            i3 = 1;
        }
        if (this.useFloatVertices) {
            this.floatData = new float[512];
            this.floatCount = 0;
            this.floatCapacity = this.floatData.length;
            this.floatBuffers = new GlBuffer[i3];
            n = 0;
            while (n < i3) {
                this.floatBuffers[n] = new GlBuffer(34962, this.floatData, 35040);
                ++n;
            }
        }
        this.shortData = new short[(this.useFloatVertices ? 4 : 8) * 128];
        this.shortCount = 0;
        this.shortCapacity = this.shortData.length;
        this.shortBuffers = new GlBuffer[i3];
        n = 0;
        while (n < i3) {
            this.shortBuffers[n] = new GlBuffer(34962, this.shortData, 35040);
            ++n;
        }
        short[] sArray = new short[2 * i2];
        int i5 = 0;
        while (i5 < i2) {
            sArray[2 * i5 + 0] = (short)(2 * i5 + 0);
            sArray[2 * i5 + 1] = (short)(2 * i5 + 1);
            ++i5;
        }
        this.indexBuffers = new GlBuffer[i3];
        i5 = 0;
        while (i5 < i3) {
            this.indexBuffers[i5] = new GlBuffer(34963, sArray, 35044);
            ++i5;
        }
        this.currentBuffer = 0;
        this.setColor(-1);
    }

    private void setColor(int i1) {
        if (this.currentColor != i1) {
            this.currentColor = i1;
            this.packedColorA = (short)(i1 & 0xFF00 | i1 >> 16 & 0xFF);
            this.packedColorB = (short)(i1 >> 16 & 0xFF00 | i1 & 0xFF);
        }
    }

    public final void setViewport(int i1, int i2) {
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(new float[]{2.0f / (float)i1, 0.0f, 0.0f, -1.0f, 0.0f, -2.0f / (float)i2, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
        floatBuffer.flip();
        this.shaderProgram.bind();
        GL20 gL20 = Gdx.gl20;
        gL20.glUniformMatrix4fv(this.mvpMatrixUniform, 1, false, floatBuffer);
    }

    public final void bindShader() {
        this.shaderProgram.bind();
    }

    public final void drawText(float f1, float f2, String string, int i4, int i5) {
        this.drawText((int)f1, (int)f2, string, i4, i5);
    }

    public final void drawText(int i1, int i2, String string, int i4, int i5) {
        int i6 = 0;
        while (i6 < string.length()) {
            this.drawGlyph(i1 + i6 * i4, i2, string.charAt(i6), i4, i5);
            ++i6;
        }
    }

    public final void drawGlyph(int i1, int i2, char c, int i4, int i5) {
        float[] fArray = (float[])glyphStrokes.get(Character.valueOf(Character.toUpperCase(c)));
        if (fArray != null) {
            int i7 = 0;
            while (i7 < fArray.length) {
                this.drawLine((float)i1 + fArray[i7] * (float)i4 * 0.75f, (float)i2 + fArray[i7 + 1] * (float)i4, (float)i1 + fArray[i7 + 2] * (float)i4 * 0.75f, (float)i2 + fArray[i7 + 3] * (float)i4, i5);
                i7 += 4;
            }
        }
    }

    public final void fillRectangle(float f1, float f2, float f3, float f4, int i5) {
        int i6 = 0;
        while ((float)i6 < f4) {
            this.drawLine((int)f1, (int)(f2 + (float)i6), (int)(f1 + f3), (int)(f2 + (float)i6), i5);
            ++i6;
        }
    }

    public final void drawLine(float f1, float f2, float f3, float f4, int i5) {
        this.drawLine((int)f1, (int)f2, (int)f3, (int)f4, i5);
    }

    public final void drawLine(int i1, int i2, int i3, int i4, int i5) {
        if (this.shortCount >= this.shortCapacity) {
            this.flushBatch();
        }
        this.setColor(i5);
        if (this.useFloatVertices) {
            this.floatData[this.floatCount++] = (short)i1;
            this.floatData[this.floatCount++] = (short)i2;
            this.shortData[this.shortCount++] = this.packedColorA;
            this.shortData[this.shortCount++] = this.packedColorB;
            this.floatData[this.floatCount++] = (short)i3;
            this.floatData[this.floatCount++] = (short)i4;
            this.shortData[this.shortCount++] = this.packedColorA;
            this.shortData[this.shortCount++] = this.packedColorB;
        } else {
            this.shortData[this.shortCount++] = (short)i1;
            this.shortData[this.shortCount++] = (short)i2;
            this.shortData[this.shortCount++] = this.packedColorA;
            this.shortData[this.shortCount++] = this.packedColorB;
            this.shortData[this.shortCount++] = (short)i3;
            this.shortData[this.shortCount++] = (short)i4;
            this.shortData[this.shortCount++] = this.packedColorA;
            this.shortData[this.shortCount++] = this.packedColorB;
        }
    }

    public final void flush() {
        this.flushBatch();
    }

    private void flushBatch() {
        if (this.shortCount > 0) {
            int i2;
            GL20 gL20 = Gdx.gl20;
            if (this.useFloatVertices) {
                i2 = this.floatCount / 4;
                GlBuffer glBuffer = this.floatBuffers[this.currentBuffer];
                FloatBuffer floatBuffer = (FloatBuffer)glBuffer.getData();
                floatBuffer.position(0);
                floatBuffer.limit(this.floatCount);
                floatBuffer.put(this.floatData, 0, this.floatCount);
                floatBuffer.flip();
                glBuffer.bind();
                glBuffer.uploadRange(0, i2 * 16);
                this.floatCount = 0;
                gL20.glVertexAttribPointer(this.positionAttrib, 2, 5126, false, 8, 0);
                gL20.glEnableVertexAttribArray(this.positionAttrib);
                GlBuffer glBuffer2 = this.shortBuffers[this.currentBuffer];
                ShortBuffer shortBuffer = (ShortBuffer)glBuffer2.getData();
                shortBuffer.position(0);
                shortBuffer.limit(this.shortCount);
                shortBuffer.put(this.shortData, 0, this.shortCount);
                shortBuffer.flip();
                glBuffer2.bind();
                glBuffer2.uploadRange(0, i2 * 8);
                this.shortCount = 0;
                gL20.glVertexAttribPointer(this.colorAttrib, 4, 5121, true, 4, 0);
                gL20.glEnableVertexAttribArray(this.colorAttrib);
            } else {
                i2 = this.shortCount / 8;
                GlBuffer glBuffer = this.shortBuffers[this.currentBuffer];
                ShortBuffer shortBuffer = (ShortBuffer)glBuffer.getData();
                shortBuffer.position(0);
                shortBuffer.limit(this.shortCount);
                shortBuffer.put(this.shortData, 0, this.shortCount);
                shortBuffer.flip();
                glBuffer.bind();
                glBuffer.uploadRange(0, i2 * 16);
                gL20.glVertexAttribPointer(this.positionAttrib, 2, 5122, false, 8, 0);
                gL20.glEnableVertexAttribArray(this.positionAttrib);
                gL20.glVertexAttribPointer(this.colorAttrib, 4, 5121, true, 8, 4);
                gL20.glEnableVertexAttribArray(this.colorAttrib);
            }
            this.indexBuffers[this.currentBuffer].bind();
            gL20.glDrawElements(1, i2 * 2, 5123, 0);
            gL20.glDisableVertexAttribArray(this.positionAttrib);
            gL20.glDisableVertexAttribArray(this.colorAttrib);
            this.currentBuffer = (this.currentBuffer + 1) % this.shortBuffers.length;
            this.shortCount = 0;
        }
    }

    public final void dispose() {
        int i1 = 0;
        while (i1 < this.indexBuffers.length) {
            this.indexBuffers[i1].dispose();
            ++i1;
        }
        i1 = 0;
        while (i1 < this.shortBuffers.length) {
            this.shortBuffers[i1].dispose();
            ++i1;
        }
        this.shaderProgram.dispose();
    }
}


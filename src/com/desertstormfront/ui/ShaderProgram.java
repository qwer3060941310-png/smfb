/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.noblemaster.lib.log.OsfLog;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public final class ShaderProgram {
    private int a;
    private int b;
    private int c;

    public ShaderProgram(String string, String string2) {
        this.a = this.compileShader(35633, string);
        this.b = this.compileShader(35632, string2);
        this.c = this.linkProgram(this.a, this.b);
    }

    public final int getAttributeLocation(String string) {
        GL20 gL20 = Gdx.gl20;
        int i3 = gL20.glGetAttribLocation(this.c, string);
        if (i3 == -1) {
            OsfLog.error("Attribute location not found or starts with \"gl_\": " + i3);
        }
        return i3;
    }

    public final int getUniformLocation(String string) {
        GL20 gL20 = Gdx.gl20;
        int i3 = gL20.glGetUniformLocation(this.c, string);
        if (i3 == -1) {
            OsfLog.error("Uniform location not found or starts with \"gl_\": " + i3);
        }
        return i3;
    }

    public final void bind() {
        GL20 gL20 = Gdx.gl20;
        gL20.glUseProgram(this.c);
    }

    public final void dispose() {
        GL20 gL20 = Gdx.gl20;
        gL20.glUseProgram(0);
        gL20.glDeleteProgram(this.c);
        gL20.glDeleteShader(this.b);
        gL20.glDeleteShader(this.a);
    }

    private final int compileShader(int i1, String string) {
        GL20 gL20 = Gdx.gl20;
        IntBuffer intBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        int i5 = gL20.glCreateShader(i1);
        if (i5 == 0) {
            throw new RuntimeException("Error creating shader.");
        }
        gL20.glShaderSource(i5, string);
        gL20.glCompileShader(i5);
        gL20.glGetShaderiv(i5, 35713, intBuffer);
        int i6 = intBuffer.get(0);
        if (i6 == 0) {
            gL20.glGetShaderiv(i5, 35716, intBuffer);
            int i7 = intBuffer.get(0);
            if (i7 > 1) {
                String string2 = gL20.glGetShaderInfoLog(i5);
                OsfLog.error("Error compiling shader: " + string2);
            }
            throw new RuntimeException("Error compiling the shader.");
        }
        return i5;
    }

    private final int linkProgram(int i1, int i2) {
        GL20 gL20 = Gdx.gl20;
        int i4 = gL20.glCreateProgram();
        if (i4 == 0) {
            throw new RuntimeException("Error creating program.");
        }
        gL20.glAttachShader(i4, i1);
        gL20.glAttachShader(i4, i2);
        gL20.glLinkProgram(i4);
        IntBuffer intBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        gL20.glGetProgramiv(i4, 35714, intBuffer);
        int i6 = intBuffer.get(0);
        if (i6 == 0) {
            gL20.glGetProgramiv(i4, 35716, intBuffer);
            int i7 = intBuffer.get(0);
            if (i7 > 1) {
                String string = gL20.glGetProgramInfoLog(i4);
                OsfLog.error("Error linking program: " + string);
            }
            throw new RuntimeException("Error linking program.");
        }
        return i4;
    }
}


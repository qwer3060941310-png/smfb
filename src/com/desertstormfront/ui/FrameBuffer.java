/*
 * Off-screen render target: attaches a texture as its colour buffer, remembers the framebuffer that
 * was bound before (captured once) and restores it; nested FrameBuffers chain through current.
 *
 * Deobfuscation: fields a..i became current/previousFramebuffer/previousFramebufferCaptured/target/
 * framebufferId/width/height/texture/renderbufferId, and the methods became checkStatus, getWidth,
 * getHeight, bind, unbind (plain and boolean) and dispose. Evidence: run\map-ui-render.tsv.
 */
package com.desertstormfront.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.ui.GlTexture;
import com.noblemaster.lib.log.OsfLog;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public final class FrameBuffer {
    private static int previousFramebuffer;
    private static boolean previousFramebufferCaptured;
    public static FrameBuffer current;
    private int target;
    private int framebufferId;
    private int width;
    private int height;
    private GlTexture texture;
    private int renderbufferId;

    static {
        previousFramebufferCaptured = false;
        current = null;
    }

    public FrameBuffer(int i1, GlTexture glTexture) {
        this(i1, glTexture.getWidth(), glTexture.getHeight());
        GL20 gL20 = Gdx.gl20;
        this.texture = glTexture;
        gL20.glFramebufferTexture2D(i1, 36064, 3553, glTexture.getGlId(), 0);
        this.checkStatus();
        this.unbind();
    }

    private FrameBuffer(int i1, int i2, int i3) {
        IntBuffer intBuffer;
        this.target = i1;
        this.width = i2;
        this.height = i3;
        GL20 gL20 = Gdx.gl20;
        if (!previousFramebufferCaptured) {
            previousFramebufferCaptured = true;
            intBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
            gL20.glGetIntegerv(36006, intBuffer);
            previousFramebuffer = intBuffer.get(0);
        }
        intBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        gL20.glGenFramebuffers(1, intBuffer);
        this.framebufferId = intBuffer.get(0);
        gL20.glBindFramebuffer(i1, this.framebufferId);
    }

    private final void checkStatus() {
        int i1 = Gdx.gl20.glCheckFramebufferStatus(36160);
        if (i1 != 36053) {
            String string;
            switch (i1) {
                case 36054: {
                    string = "Framebuffer Error (" + i1 + "): GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT";
                    break;
                }
                case 36055: {
                    string = "Framebuffer Error (" + i1 + "): GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT";
                    break;
                }
                case 36057: {
                    string = "Framebuffer Error (" + i1 + "): GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS";
                    break;
                }
                case 36061: {
                    string = "Framebuffer Error (" + i1 + "): GL_FRAMEBUFFER_UNSUPPORTED";
                    break;
                }
                default: {
                    string = "Framebuffer Error (" + i1 + ")";
                }
            }
            OsfLog.error(string);
        }
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final void bind() {
        GL20 gL20 = Gdx.gl20;
        gL20.glBindFramebuffer(this.target, this.framebufferId);
        gL20.glViewport(0, 0, this.width, this.height);
    }

    public final void unbind() {
        this.unbind(false);
    }

    public final void unbind(boolean bl) {
        GL20 gL20 = Gdx.gl20;
        if (bl || current == null) {
            gL20.glBindFramebuffer(this.target, previousFramebuffer);
            gL20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else {
            current.bind();
        }
    }

    public final void dispose() {
        IntBuffer intBuffer;
        GL20 gL20 = Gdx.gl20;
        if (this.texture == null) {
            intBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
            intBuffer.clear();
            intBuffer.put(this.renderbufferId);
            intBuffer.flip();
            gL20.glDeleteRenderbuffers(1, intBuffer);
        }
        intBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        intBuffer.clear();
        intBuffer.put(this.framebufferId);
        intBuffer.flip();
        gL20.glDeleteFramebuffers(1, intBuffer);
    }
}


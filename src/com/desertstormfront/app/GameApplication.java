/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.audio.AudioClip;
import com.desertstormfront.audio.MusicPlaylist;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.BootScreen;
import com.desertstormfront.ui.FrameBuffer;
import com.desertstormfront.ui.GlBuffer;
import com.desertstormfront.ui.GlErrorChecker;
import com.desertstormfront.ui.GlTexture;
import com.desertstormfront.ui.ShaderProgram;
import com.noblemaster.lib.data.Language;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.license.License;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.market.Market;
import com.noblemaster.lib.math.MathHelper;
import com.noblemaster.lib.net.match.AddressProvider;
import com.noblemaster.lib.net.match.PortMappingCallback;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Abstract libGDX {@link ApplicationListener} that owns the screen lifecycle and the optional
 * downscaled framebuffer used on high-DPI Android screens.
 *
 * <p>A concrete platform (see {@code DesktopApplication}) supplies the market, license and a few
 * platform hooks (locale, address provider, port mapper, URL opener). The base class drives screen
 * transitions in {@link #render} and rebuilds the GL resources on resize/resume.
 *
 * <p>The abstract members are the platform contract; the {@code scale*} fields describe the
 * off-screen framebuffer that {@link #render} blits when Android scaling is active.
 */
public abstract class GameApplication
implements ApplicationListener {
    private Market market;
    private License license;
    private BaseScreen screen;
    private boolean active;
    public static float scale = 1.0f;
    private ShaderProgram scaleShader;
    private int positionAttr;
    private int texCoordAttr;
    private int textureUniform;
    private int mvpUniform;
    private int scaledWidth;
    private int scaledHeight;
    private GlTexture scaleTexture;
    private GlBuffer vertexBuffer;
    private GlBuffer indexBuffer;
    private FrameBuffer scaleFrameBuffer;

    protected GameApplication(Market market, License license) {
        this.market = market;
        this.license = license;
        GameConfig.market = market;
        this.active = false;
    }

    @Override
    public void create() {
        String string = "";
        if (GameConfig.isDebugEnabled()) {
            string = "DEBUG";
        }
        if (GameConfig.isLiteMode()) {
            string = String.valueOf(string) + (string.length() > 0 ? ", " : "") + "LITE";
        }
        OsfLog.info(String.valueOf(GameConfig.getTitle()) + " " + GameConfig.getVersion() + " (" + string + ")");
        this.setCustomCursor();
        UserConfig.load();
        AudioClip.setMasterVolume(UserConfig.getAudioVolume());
        MusicPlaylist.setMasterVolume(UserConfig.getMusicVolume());
        Language language = UserConfig.getLanguage();
        Messages.load(language != null ? language : Language.fromCode(this.getLanguage()));
        this.screen = new BootScreen();
        this.screen.setApplication(this);
        this.screen.show();
        this.screen.createGlResources();
        this.active = true;
        this.onResize(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
    }

    @Override
    public void resume() {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            this.screen.createGlResources();
            this.active = true;
            this.onResize(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        }
    }

    @Override
    public void resize(int i1, int i2) {
        if (this.active) {
            this.onResize(i1, i2);
        }
    }

    private void onResize(int i1, int i2) {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            int i5;
            int i4;
            int i3 = MathHelper.round(Gdx.graphics.getDensity() * 160.0f);
            OsfLog.info("DPI = " + i3 + " (Screen Density = " + Gdx.graphics.getDensity() + ")");
            if (i3 > 260) {
                scale = 260.0f / (float)i3;
                i4 = MathHelper.round((float)i1 * scale);
                i5 = MathHelper.round((float)i2 * scale);
                if (i4 < 800) {
                    scale = 800.0f / (float)i1;
                    i4 = MathHelper.round((float)i1 * scale);
                    i5 = MathHelper.round((float)i2 * scale);
                }
            } else {
                scale = 1.0f;
                i4 = i1;
                i5 = i2;
            }
            OsfLog.info("Screen Scaling: " + scale + " (" + i1 + "x" + i2 + " => " + i4 + "x" + i5 + ")");
            if (i4 != this.scaledWidth || i5 != this.scaledHeight) {
                this.disposeGlResources();
                this.scaledWidth = i4;
                this.scaledHeight = i5;
            }
            if (this.scaleFrameBuffer == null && scale != 1.0f && i4 > 0 && i5 > 0) {
                this.scaleShader = new ShaderProgram("attribute vec4 a_position;                            \nattribute vec2 a_texCoord;                            \nvarying vec2 v_texCoord;                              \nuniform mat4 u_mvpMatrix;                             \nvoid main()                                           \n{                                                     \n   gl_Position = a_position;                          \n   v_texCoord = a_texCoord;                           \n   gl_Position *= u_mvpMatrix;                        \n}                                                     \n", "#ifdef GL_ES                                          \nprecision mediump float;                              \n#endif                                                \nvarying vec2 v_texCoord;                              \nuniform sampler2D s_texture;                          \nvoid main()                                           \n{                                                     \n  gl_FragColor = texture2D(s_texture, v_texCoord);    \n}                                                     \n");
                this.positionAttr = this.scaleShader.getAttributeLocation("a_position");
                this.texCoordAttr = this.scaleShader.getAttributeLocation("a_texCoord");
                this.textureUniform = this.scaleShader.getUniformLocation("s_texture");
                this.mvpUniform = this.scaleShader.getUniformLocation("u_mvpMatrix");
                this.scaleTexture = new GlTexture(6407, i4, i5, 33071, 33071, 9729, 9729);
                float[] fArray = new float[16];
                short[] sArray = new short[]{0, 1, 2, 0, 2, 3};
                this.vertexBuffer = new GlBuffer(34962, fArray, 35040);
                this.indexBuffer = new GlBuffer(34963, sArray, 35044);
                FrameBuffer.current = this.scaleFrameBuffer = new FrameBuffer(36160, this.scaleTexture);
                int i8 = i1;
                int i9 = i2;
                FloatBuffer floatBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();
                floatBuffer.put(new float[]{2.0f / (float)i8, 0.0f, 0.0f, -1.0f, 0.0f, -2.0f / (float)i9, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
                floatBuffer.flip();
                this.scaleShader.bind();
                GL20 gL20 = Gdx.gl20;
                gL20.glUniformMatrix4fv(this.mvpUniform, 1, false, floatBuffer);
                gL20.glUniform1i(this.textureUniform, 0);
                this.vertexBuffer.bind();
                FloatBuffer floatBuffer2 = (FloatBuffer)this.vertexBuffer.getData();
                floatBuffer2.clear();
                floatBuffer2.put(0.0f);
                floatBuffer2.put(0.0f);
                floatBuffer2.put(0.0f);
                floatBuffer2.put(1.0f);
                floatBuffer2.put(i1 + 1);
                floatBuffer2.put(0.0f);
                floatBuffer2.put(1.0f);
                floatBuffer2.put(1.0f);
                floatBuffer2.put(i1 + 1);
                floatBuffer2.put(i2 + 1);
                floatBuffer2.put(1.0f);
                floatBuffer2.put(0.0f);
                floatBuffer2.put(0.0f);
                floatBuffer2.put(i2 + 1);
                floatBuffer2.put(0.0f);
                floatBuffer2.put(0.0f);
                floatBuffer2.flip();
                this.vertexBuffer.uploadRange(0, 64);
            }
        }
        this.screen.resize(MathHelper.round((float)i1 * scale), MathHelper.round((float)i2 * scale));
    }

    @Override
    public void render() {
        BaseScreen baseScreen = this.screen.update();
        if (baseScreen != null) {
            this.active = false;
            this.screen.pause();
            this.screen.dispose();
            this.screen = baseScreen;
            this.screen.setApplication(this);
            this.screen.show();
            this.screen.createGlResources();
            this.active = true;
            this.onResize(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        }
        if (this.scaleFrameBuffer != null) {
            this.scaleFrameBuffer.bind();
        }
        this.screen.render();
        if (this.scaleFrameBuffer != null) {
            this.scaleFrameBuffer.unbind(true);
            GL20 gL20 = Gdx.gl20;
            this.scaleShader.bind();
            this.scaleTexture.bind();
            this.vertexBuffer.bind();
            gL20.glVertexAttribPointer(this.positionAttr, 2, 5126, false, 16, 0);
            gL20.glEnableVertexAttribArray(this.positionAttr);
            gL20.glVertexAttribPointer(this.texCoordAttr, 2, 5126, false, 16, 8);
            gL20.glEnableVertexAttribArray(this.texCoordAttr);
            this.indexBuffer.bind();
            gL20.glDrawElements(4, 6, 5123, 0);
            gL20.glDisableVertexAttribArray(this.positionAttr);
            gL20.glDisableVertexAttribArray(this.texCoordAttr);
        }
        if (GameConfig.isDebugEnabled()) {
            GlErrorChecker.check();
        }
    }

    @Override
    public void pause() {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            this.active = false;
            this.screen.pause();
            this.screen.disposeHudRenderer();
        }
    }

    private void disposeGlResources() {
        if (this.scaleFrameBuffer != null) {
            this.scaleFrameBuffer.dispose();
            this.scaleFrameBuffer = null;
            FrameBuffer.current = null;
            this.scaleTexture.dispose();
            this.scaleTexture = null;
            this.vertexBuffer.dispose();
            this.vertexBuffer = null;
            this.indexBuffer.dispose();
            this.indexBuffer = null;
            this.scaleShader.dispose();
            this.scaleShader = null;
        }
    }

    @Override
    public void dispose() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            this.active = false;
            this.screen.pause();
            this.screen.disposeHudRenderer();
        }
        this.screen.dispose();
        this.license.dispose();
        this.disposeGlResources();
        this.exit();
    }

    public Market getMarket() {
        return this.market;
    }

    public License getLicense() {
        return this.license;
    }

    public final void exit() {
        Gdx.app.exit();
    }

    public abstract String getLanguage();

    public abstract boolean isDesktopMode();

    public abstract AddressProvider getAddressProvider();

    public abstract PortMappingCallback getPortMappingCallback();

    public abstract void openUrl(String var1);

    public abstract void resetCursor();

    public abstract void setCustomCursor();

    public int clampMouseX(int i1, int i2) {
        return 0;
    }

    public int clampMouseY(int i1, int i2) {
        return 0;
    }
}


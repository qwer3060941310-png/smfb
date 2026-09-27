/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.GameScreen;
import com.desertstormfront.screen.MultiplayerLobbyScreen;
import com.desertstormfront.session.SessionMode;
import com.desertstormfront.session.impl.MultiplayerGameMode;
import com.desertstormfront.ui.BatchRenderer;
import com.desertstormfront.ui.GlTexture;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.TextureRegion;

public final class LoadGameScreen
extends BaseScreen {
    private long startTime;
    private int frameCount;
    private BatchRenderer renderer;
    private SessionMode sessionMode;
    private boolean restoreStartWorld;

    public LoadGameScreen(SessionMode sessionMode) {
        this(sessionMode, false);
    }

    public LoadGameScreen(SessionMode sessionMode, boolean bl) {
        this.sessionMode = sessionMode;
        this.restoreStartWorld = bl;
    }

    @Override
    public BaseScreen update() {
        if (System.currentTimeMillis() >= this.startTime + 100L && this.frameCount >= 10) {
            if (this.restoreStartWorld) {
                this.sessionMode.restoreStartWorld();
            }
            if (this.sessionMode instanceof MultiplayerGameMode) {
                try {
                    ((MultiplayerGameMode)this.sessionMode).hostGame(this.getAddressProvider(), this.getPortMappingCallback());
                }
                catch (Exception exception) {
                    return new MultiplayerLobbyScreen(exception);
                }
            }
            return new GameScreen(this.sessionMode);
        }
        ++this.frameCount;
        return null;
    }

    @Override
    public void show() {
        this.startTime = System.currentTimeMillis();
        this.frameCount = 0;
    }

    @Override
    public void createGlResources() {
        GL20 gL20 = Gdx.gl20;
        gL20.glBlendFunc(770, 771);
        gL20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gL20.glEnable(3042);
        gL20.glDepthFunc(515);
        gL20.glClearDepthf(1.0f);
        gL20.glDisable(2929);
        gL20.glDisable(3024);
        gL20.glDisable(2960);
        gL20.glDisable(2884);
        gL20.glActiveTexture(33984);
        this.renderer = new BatchRenderer(new GlTexture(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "texture_load.png"), 33071, 33071, 9728, 9728), 1);
        this.renderer.setRootWidget(new Image(new TextureRegion(0, 0, 256, 256)));
        this.renderer.getRootWidget().pack();
    }

    @Override
    protected void layout() {
        GL20 gL20 = Gdx.gl20;
        int i2 = this.getWidth();
        int i3 = this.getHeight();
        gL20.glViewport(0, 0, i2, i3);
        this.renderer.setViewportSize(i2, i3);
        this.renderer.getRootWidget().setX(((float)i2 - this.renderer.getRootWidget().getWidth()) / 2.0f);
        this.renderer.getRootWidget().setY(((float)i3 - this.renderer.getRootWidget().getHeight()) / 2.0f);
    }

    @Override
    public void render() {
        GL20 gL20 = Gdx.gl20;
        gL20.glClear(16384);
        this.renderer.render();
        super.render();
    }

    @Override
    public void pause() {
        this.renderer.dispose();
        this.renderer = null;
    }

    @Override
    public void dispose() {
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.audio.AudioClip;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.MainMenuScreen;
import com.desertstormfront.ui.BatchRenderer;
import com.desertstormfront.ui.GlTexture;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.TextureRegion;

public final class LogoScreen
extends BaseScreen {
    private long startTime;
    private BatchRenderer logoRenderer;
    private BatchRenderer ratingRenderer;
    private AudioClip introMusic;

    @Override
    public BaseScreen update() {
        if (this.isTouchDown() && !this.getMarket().getName().equals("JoyMoa") || System.currentTimeMillis() >= this.startTime + 3000L) {
            return new MainMenuScreen();
        }
        return null;
    }

    @Override
    public void show() {
        this.startTime = System.currentTimeMillis();
        this.introMusic = new AudioClip(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "music_intro.mp3"), false);
        this.introMusic.play();
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
        this.logoRenderer = new BatchRenderer(new GlTexture(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "logo_application.png"), 33071, 33071, 9728, 9728), 1);
        this.logoRenderer.setRootWidget(new Image(new TextureRegion(0, 0, 512, 512)));
        this.logoRenderer.getRootWidget().pack();
        if (this.getMarket().getName().equals("JoyMoa")) {
            this.ratingRenderer = new BatchRenderer(new GlTexture(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "publisher_joymoa_rating.png"), 33071, 33071, 9728, 9728), 1);
            this.ratingRenderer.setRootWidget(new Image(new TextureRegion(0, 0, 128, 128)));
            this.ratingRenderer.getRootWidget().pack();
        }
    }

    @Override
    protected void layout() {
        GL20 gL20 = Gdx.gl20;
        int i2 = this.getWidth();
        int i3 = this.getHeight();
        gL20.glViewport(0, 0, i2, i3);
        this.logoRenderer.setViewportSize(i2, i3);
        this.logoRenderer.getRootWidget().setX(((float)i2 - this.logoRenderer.getRootWidget().getWidth()) / 2.0f);
        this.logoRenderer.getRootWidget().setY(((float)i3 - this.logoRenderer.getRootWidget().getHeight()) / 2.0f);
        if (this.ratingRenderer != null) {
            this.ratingRenderer.setViewportSize(i2, i3);
            this.ratingRenderer.getRootWidget().setX((float)i2 - this.ratingRenderer.getRootWidget().getWidth() - 5.0f);
            this.ratingRenderer.getRootWidget().setY(5.0f);
        }
    }

    @Override
    public void render() {
        GL20 gL20 = Gdx.gl20;
        gL20.glClear(16384);
        this.logoRenderer.render();
        if (this.ratingRenderer != null) {
            this.ratingRenderer.render();
        }
        super.render();
    }

    @Override
    public void pause() {
        this.logoRenderer.dispose();
        this.logoRenderer = null;
        if (this.ratingRenderer != null) {
            this.ratingRenderer.dispose();
            this.ratingRenderer = null;
        }
    }

    @Override
    public void dispose() {
        if (this.introMusic != null) {
            this.introMusic.dispose();
            this.introMusic = null;
        }
    }
}


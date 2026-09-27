/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.audio.AudioClip;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.LogoScreen;
import com.desertstormfront.screen.MainMenuScreen;
import com.desertstormfront.ui.BatchRenderer;
import com.desertstormfront.ui.GlTexture;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.TextureRegion;

public final class PublisherSplashScreen
extends BaseScreen {
    private long startTime;
    private long logoDuration = 3000L;
    private boolean hasPublisherSplash;
    private boolean publisherPhaseActive;
    private long publisherSplashDuration;
    private BatchRenderer nobleMasterRenderer;
    private BatchRenderer publisherRenderer;
    private AudioClip nobleMasterMusic;
    private AudioClip publisherMusic;

    @Override
    public BaseScreen update() {
        if (this.isTouchDown() && !this.getMarket().getName().equals("JoyMoa")) {
            return new MainMenuScreen();
        }
        if (this.hasPublisherSplash && this.publisherPhaseActive) {
            if (System.currentTimeMillis() >= this.startTime + this.publisherSplashDuration) {
                this.publisherPhaseActive = false;
                this.nobleMasterMusic.play();
            }
            return null;
        }
        if (this.hasPublisherSplash) {
            if (System.currentTimeMillis() >= this.startTime + this.publisherSplashDuration + this.logoDuration) {
                return new LogoScreen();
            }
            return null;
        }
        if (System.currentTimeMillis() >= this.startTime + this.logoDuration) {
            return new LogoScreen();
        }
        return null;
    }

    @Override
    public void show() {
        this.startTime = System.currentTimeMillis();
        if (this.getMarket().getName().equals("JoyMoa")) {
            this.hasPublisherSplash = true;
            this.publisherSplashDuration = 5500L;
            this.publisherMusic = new AudioClip(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "publisher_joymoa.mp3"), false);
        } else if (this.getMarket().getName().equals("DarkGame")) {
            this.hasPublisherSplash = true;
            this.publisherSplashDuration = 3000L;
            this.publisherMusic = null;
        } else if (this.getMarket().getName().equals("iWin")) {
            this.hasPublisherSplash = true;
            this.publisherSplashDuration = 3000L;
            this.publisherMusic = null;
        } else {
            this.hasPublisherSplash = false;
        }
        this.nobleMasterMusic = new AudioClip(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "music_noblemaster.mp3"), false);
        if (this.hasPublisherSplash) {
            this.publisherPhaseActive = true;
            if (this.publisherMusic != null) {
                this.publisherMusic.play();
            }
        } else {
            this.nobleMasterMusic.play();
        }
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
        if (this.hasPublisherSplash) {
            if (this.getMarket().getName().equals("JoyMoa")) {
                this.publisherRenderer = new BatchRenderer(new GlTexture(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "publisher_joymoa.png"), 33071, 33071, 9728, 9728), 1);
                this.publisherRenderer.setRootWidget(new Image(new TextureRegion(0, 0, 512, 512)));
                this.publisherRenderer.getRootWidget().pack();
            } else if (this.getMarket().getName().equals("DarkGame")) {
                this.publisherRenderer = new BatchRenderer(new GlTexture(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "publisher_darkgame.png"), 33071, 33071, 9728, 9728), 1);
                this.publisherRenderer.setRootWidget(new Image(new TextureRegion(0, 0, 512, 512)));
                this.publisherRenderer.getRootWidget().pack();
            } else if (this.getMarket().getName().equals("iWin")) {
                this.publisherRenderer = new BatchRenderer(new GlTexture(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "publisher_iwin.png"), 33071, 33071, 9728, 9728), 1);
                this.publisherRenderer.setRootWidget(new Image(new TextureRegion(0, 0, 512, 512)));
                this.publisherRenderer.getRootWidget().pack();
            }
        }
        this.nobleMasterRenderer = new BatchRenderer(new GlTexture(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "logo_noblemaster.png"), 33071, 33071, 9728, 9728), 1);
        this.nobleMasterRenderer.setRootWidget(new Image(new TextureRegion(0, 0, 512, 512)));
        this.nobleMasterRenderer.getRootWidget().pack();
    }

    @Override
    protected void layout() {
        GL20 gL20 = Gdx.gl20;
        int i2 = this.getWidth();
        int i3 = this.getHeight();
        gL20.glViewport(0, 0, i2, i3);
        if (this.publisherRenderer != null) {
            this.publisherRenderer.setViewportSize(i2, i3);
            this.publisherRenderer.getRootWidget().setX(((float)i2 - this.publisherRenderer.getRootWidget().getWidth()) / 2.0f);
            this.publisherRenderer.getRootWidget().setY(((float)i3 - this.publisherRenderer.getRootWidget().getHeight()) / 2.0f);
        }
        this.nobleMasterRenderer.setViewportSize(i2, i3);
        this.nobleMasterRenderer.getRootWidget().setX(((float)i2 - this.nobleMasterRenderer.getRootWidget().getWidth()) / 2.0f);
        this.nobleMasterRenderer.getRootWidget().setY(((float)i3 - this.nobleMasterRenderer.getRootWidget().getHeight()) / 2.0f);
    }

    @Override
    public void render() {
        GL20 gL20 = Gdx.gl20;
        gL20.glClear(16384);
        if (this.hasPublisherSplash && this.publisherPhaseActive) {
            this.publisherRenderer.render();
        } else {
            this.nobleMasterRenderer.render();
        }
        super.render();
    }

    @Override
    public void pause() {
        if (this.publisherRenderer != null) {
            this.publisherRenderer.dispose();
            this.publisherRenderer = null;
        }
        this.nobleMasterRenderer.dispose();
        this.nobleMasterRenderer = null;
    }

    @Override
    public void dispose() {
        if (this.publisherMusic != null) {
            this.publisherMusic.dispose();
            this.nobleMasterMusic = null;
        }
        if (this.nobleMasterMusic != null) {
            this.nobleMasterMusic.dispose();
            this.nobleMasterMusic = null;
        }
    }
}


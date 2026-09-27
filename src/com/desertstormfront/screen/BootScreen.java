/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.PublisherSplashScreen;
import com.noblemaster.lib.log.OsfLog;

public final class BootScreen
extends BaseScreen {
    /** Guards the fail-safe below so the reason is logged once, not every frame. */
    private boolean reportedInvalidLicense;

    @Override
    public BaseScreen update() {
        if (this.getLicense().isValid()) {
            GameConfig.setMacBuild(this.getLicense().isMacBuild());
            return new PublisherSplashScreen();
        }
        // Fail-safe: an invalid/expired license (e.g. the Review market's 90-day trial) used to
        // make update() return null forever, leaving the player on a black BootScreen with no
        // message at all (see 24_black screen root cause.md). Report it readably and exit so the
        // process terminates instead of hanging. A licensed market key (-m<Market>) starts normally.
        if (!this.reportedInvalidLicense) {
            this.reportedInvalidLicense = true;
            OsfLog.info("License is invalid or expired (market=" + this.getMarket().getName()
                    + ", license=" + this.getLicense().getName() + "). The game cannot start.");
            OsfLog.info("Start with a licensed market key, e.g. -mNobleMaster, or install a licensed build.");
            Gdx.app.exit();
        }
        return null;
    }

    @Override
    public void show() {
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
    }

    @Override
    protected void layout() {
    }

    @Override
    public void render() {
        GL20 gL20 = Gdx.gl20;
        gL20.glClear(16384);
        super.render();
    }

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
    }
}


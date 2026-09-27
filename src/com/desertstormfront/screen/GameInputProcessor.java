/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.badlogic.gdx.InputProcessor;
import com.desertstormfront.screen.GameScene;
import com.desertstormfront.screen.GameScreen;
import com.desertstormfront.ui.hud.GuiAssets;

class GameInputProcessor
implements InputProcessor {
    final /* synthetic */ GameScreen screen;

    GameInputProcessor(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    @Override
    public boolean touchUp(int i1, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i1, int i2) {
        return false;
    }

    @Override
    public boolean touchDragged(int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDown(int i1, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean scrolled(int i1) {
        float f2 = GameScreen.getZoom(this.screen) * (1.0f - (float)i1 * 0.04f);
        if (f2 < 0.5f) {
            f2 = 0.5f;
        } else if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        GameScreen.setTargetZoom(this.screen, f2);
        GameScreen.getMinimapPanel(this.screen).setZoomOutEnabled(f2 > 0.5f);
        GameScreen.getMinimapPanel(this.screen).setZoomInEnabled(f2 < 1.0f);
        return true;
    }

    @Override
    public boolean keyUp(int i1) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean keyDown(int i1) {
        if (i1 == 4 || i1 == 131) {
            GuiAssets.playClickSound();
            GameScreen.getSelectedUnits(this.screen).clear();
            GameScreen.getGroupBar(this.screen).collapse();
            GameScreen.invokeSetScene(this.screen, GameScene.PauseMenu);
            return true;
        }
        return false;
    }
}


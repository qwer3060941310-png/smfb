/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.badlogic.gdx.InputProcessor;
import com.desertstormfront.screen.BaseScreen;

class ScreenInputProcessor
implements InputProcessor {
    final /* synthetic */ BaseScreen screen;

    ScreenInputProcessor(BaseScreen baseScreen) {
        this.screen = baseScreen;
    }

    @Override
    public boolean touchUp(int i1, int i2, int i3, int i4) {
        if (i4 == 0) {
            BaseScreen.setTouchDownUntilFrame(this.screen, BaseScreen.getFrameCounter(this.screen));
            if (BaseScreen.getTouchDownUntilFrame(this.screen) - BaseScreen.getTouchDownFrame(this.screen) < 2) {
                BaseScreen.setTouchDownUntilFrame(this.screen, 2 + BaseScreen.getTouchDownFrame(this.screen));
            }
        }
        if (i4 != 0) {
            BaseScreen.setTouchUpUntilFrame(this.screen, BaseScreen.getFrameCounter(this.screen));
            if (BaseScreen.getTouchUpUntilFrame(this.screen) - BaseScreen.getTouchUpFrame(this.screen) < 2) {
                BaseScreen.setTouchUpUntilFrame(this.screen, 2 + BaseScreen.getTouchUpFrame(this.screen));
            }
        }
        BaseScreen.setTouchX(this.screen, i1);
        BaseScreen.setTouchY(this.screen, i2);
        return true;
    }

    @Override
    public boolean mouseMoved(int i1, int i2) {
        BaseScreen.setTouchX(this.screen, i1);
        BaseScreen.setTouchY(this.screen, i2);
        return true;
    }

    @Override
    public boolean touchDragged(int i1, int i2, int i3) {
        BaseScreen.setTouchX(this.screen, i1);
        BaseScreen.setTouchY(this.screen, i2);
        return true;
    }

    @Override
    public boolean touchDown(int i1, int i2, int i3, int i4) {
        if (i4 == 0) {
            BaseScreen.setTouchDown(this.screen, true);
            BaseScreen.setTouchDownFrame(this.screen, BaseScreen.getFrameCounter(this.screen));
            BaseScreen.setTouchDownUntilFrame(this.screen, Integer.MAX_VALUE);
        }
        if (i4 != 0) {
            BaseScreen.setTouchUp(this.screen, true);
            BaseScreen.setTouchUpFrame(this.screen, BaseScreen.getFrameCounter(this.screen));
            BaseScreen.setTouchUpUntilFrame(this.screen, Integer.MAX_VALUE);
        }
        BaseScreen.setTouchX(this.screen, i1);
        BaseScreen.setTouchY(this.screen, i2);
        return true;
    }

    @Override
    public boolean scrolled(int i1) {
        return false;
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
        return false;
    }
}


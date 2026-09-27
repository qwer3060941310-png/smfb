/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.screen.GameScene;
import com.desertstormfront.screen.GameScreen;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;

class RestartConfirmListener
implements ActionListener {
    final /* synthetic */ GameScreen screen;

    RestartConfirmListener(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    @Override
    public void onAction(Widget widget) {
        GuiAssets.playClickSound();
        int i2 = (Integer)widget.getData();
        if (i2 == 0) {
            GameScreen.invokeSetScene(this.screen, GameScene.RestartGame);
        } else {
            GameScreen.invokeSetScene(this.screen, GameScene.PauseMenu);
        }
    }
}


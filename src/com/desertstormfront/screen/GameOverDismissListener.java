/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.screen.GameScene;
import com.desertstormfront.screen.GameScreen;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;

class GameOverDismissListener
implements ActionListener {
    final /* synthetic */ GameScreen screen;

    GameOverDismissListener(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    @Override
    public void onAction(Widget widget) {
        GuiAssets.playClickSound();
        GameScreen.invokeSetScene(this.screen, GameScene.ExitToMenu);
    }
}


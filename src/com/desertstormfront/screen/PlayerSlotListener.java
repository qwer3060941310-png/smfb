/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.screen.GameScene;
import com.desertstormfront.screen.GameScreen;
import com.desertstormfront.session.impl.MultiplayerGameMode;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;

class PlayerSlotListener
implements ActionListener {
    final /* synthetic */ GameScreen screen;

    PlayerSlotListener(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    @Override
    public void onAction(Widget widget) {
        GuiAssets.playClickSound();
        int i2 = (Integer)widget.getData();
        MultiplayerGameMode multiplayerGameMode = (MultiplayerGameMode)GameScreen.getSessionMode(this.screen);
        multiplayerGameMode.getClient().claimSlot(i2);
        GameScreen.invokeSetScene(this.screen, GameScene.WaitingForPlayers);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.game.model.Unit;
import com.desertstormfront.screen.GameScene;
import com.desertstormfront.screen.GameScreen;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import java.util.Collection;

class MultiSelectPanelListener
implements ActionListener {
    final /* synthetic */ GameScreen screen;

    MultiSelectPanelListener(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    @Override
    public void onAction(Widget widget) {
        int i2 = (Integer)widget.getData();
        if (i2 == -10000) {
            GameScreen.getTutorialHintBox(this.screen).notifyStepEvent("ClosePanel");
            GameScreen.getSelectedUnits(this.screen).clear();
            GameScreen.getGroupBar(this.screen).collapse();
            GameScreen.invokeSetScene(this.screen, GameScene.Default);
            GuiAssets.playClickSound();
        } else if (i2 == 10000) {
            GameScreen.getMultiSelectBuffer(this.screen).addAll((Collection)GameScreen.getSelectedUnits(this.screen));
            GameScreen.getSelectedUnits(this.screen).clear();
            GameScreen.getGroupBar(this.screen).collapse();
            int i3 = 0;
            while (i3 < GameScreen.getMultiSelectBuffer(this.screen).size()) {
                GameScreen.getSelectedUnits(this.screen).addAll((Collection)((Unit)GameScreen.getMultiSelectBuffer(this.screen).get(i3)).getSubUnits());
                ++i3;
            }
            GameScreen.getMultiSelectBuffer(this.screen).clear();
            GameScreen.invokeSetScene(this.screen, GameScene.Attack);
            GuiAssets.playClickSound();
        } else if (i2 == 10001) {
            GameScreen.getUnitCommander(this.screen).toggleHostedAutoUnits(GameScreen.getSelectedUnits(this.screen));
            GameScreen.getGroupBar(this.screen).collapse();
            GameScreen.getSelectedUnits(this.screen).clear();
            GameScreen.invokeSetScene(this.screen, GameScene.Default);
            this.screen.getAudio().playOk();
        } else if (i2 == 11000) {
            GameScreen.invokeCommandUngroup(this.screen);
        } else if (i2 == 0) {
            GameScreen.invokeSetScene(this.screen, GameScene.Attack);
            GuiAssets.playClickSound();
        } else if (i2 == 1) {
            GameScreen.invokeCommandGroup(this.screen);
        } else if (i2 == 2) {
            GameScreen.invokeCommandRepair(this.screen);
        } else {
            GameScreen.invokeCommandStop(this.screen);
        }
    }
}


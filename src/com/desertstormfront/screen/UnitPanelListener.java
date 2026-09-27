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

class UnitPanelListener
implements ActionListener {
    final /* synthetic */ GameScreen a;

    UnitPanelListener(GameScreen gameScreen) {
        this.a = gameScreen;
    }

    @Override
    public void onAction(Widget widget) {
        Unit unit = (Unit)GameScreen.getSelectedUnits(this.a).get(0);
        int i3 = (Integer)widget.getData();
        if (i3 == -10000) {
            GameScreen.getTutorialHintBox(this.a).notifyStepEvent("ClosePanel");
            GameScreen.getSelectedUnits(this.a).clear();
            GameScreen.getGroupBar(this.a).collapse();
            GameScreen.invokeSetScene(this.a, GameScene.Default);
            GuiAssets.playClickSound();
        } else if (i3 == 11000) {
            GameScreen.invokeCommandUngroup(this.a);
        } else if (i3 == 0) {
            GameScreen.invokeSetScene(this.a, GameScene.Attack);
            GuiAssets.playClickSound();
        } else if (i3 == 1) {
            GameScreen.invokeCommandPatrol(this.a);
        } else if (i3 == 2) {
            GameScreen.invokeCommandRepair(this.a);
        } else if (i3 == 3) {
            GameScreen.getUnitCommander(this.a).setAuto(unit);
            GameScreen.getSelectedUnits(this.a).clear();
            GameScreen.getGroupBar(this.a).collapse();
            GameScreen.invokeSetScene(this.a, GameScene.Default);
            this.a.getAudio().playOk();
        } else {
            GameScreen.invokeCommandStop(this.a);
        }
    }
}


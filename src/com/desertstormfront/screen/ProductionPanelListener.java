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

class ProductionPanelListener
implements ActionListener {
    final /* synthetic */ GameScreen screen;

    ProductionPanelListener(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    @Override
    public void onAction(Widget widget) {
        Unit unit = (Unit)GameScreen.getSelectedUnits(this.screen).get(0);
        int i3 = (Integer)widget.getData();
        if (i3 == -10000) {
            GameScreen.getTutorialHintBox(this.screen).notifyStepEvent("ClosePanel");
            GameScreen.getSelectedUnits(this.screen).clear();
            GameScreen.getGroupBar(this.screen).collapse();
            GameScreen.invokeSetScene(this.screen, GameScene.Default);
            GuiAssets.playClickSound();
        } else if (i3 == 10000) {
            GameScreen.getSelectedUnits(this.screen).clear();
            GameScreen.getGroupBar(this.screen).collapse();
            int i4 = 0;
            while (i4 < unit.getSubUnits().size()) {
                Unit unit2 = (Unit)unit.getSubUnits().get(i4);
                if (!unit2.getUnitType().isGeneral()) {
                    GameScreen.getSelectedUnits(this.screen).add(unit2);
                }
                ++i4;
            }
            GameScreen.invokeSetScene(this.screen, GameScene.Attack);
            GuiAssets.playClickSound();
        } else if (i3 == 10001) {
            GameScreen.getUnitCommander(this.screen).toggleHostedAuto(unit);
            GameScreen.getGroupBar(this.screen).collapse();
            GameScreen.getSelectedUnits(this.screen).clear();
            GameScreen.invokeSetScene(this.screen, GameScene.Default);
            this.screen.getAudio().playOk();
        } else if (i3 == 11000) {
            GameScreen.invokeCommandUngroup(this.screen);
        } else if (i3 == 0) {
            GameScreen.invokeSetScene(this.screen, GameScene.Attack);
            GuiAssets.playClickSound();
        } else if (i3 == 1) {
            GameScreen.invokeCommandPatrol(this.screen);
        } else if (i3 == 2) {
            GameScreen.invokeCommandRepair(this.screen);
        } else if (i3 == 3) {
            GameScreen.getUnitCommander(this.screen).setAuto(unit);
            GameScreen.getGroupBar(this.screen).collapse();
            GameScreen.getSelectedUnits(this.screen).clear();
            GameScreen.invokeSetScene(this.screen, GameScene.Default);
            this.screen.getAudio().playOk();
        } else if (i3 == 4) {
            GameScreen.invokeCommandStop(this.screen);
        } else if (-i3 - 1 < unit.getSubUnits().size()) {
            GameScreen.getTutorialHintBox(this.screen).notifyStepEvent("SelectGroundInMobileHost");
            GameScreen.getSelectedUnits(this.screen).clear();
            GameScreen.getSelectedUnits(this.screen).add((Unit)unit.getSubUnits().get(-i3 - 1));
            GameScreen.getGroupBar(this.screen).collapse();
            GameScreen.invokeSetScene(this.screen, GameScene.Build);
            GuiAssets.playClickSound();
        }
    }
}


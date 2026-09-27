/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.screen.GameScene;
import com.desertstormfront.screen.GameScreen;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;

class BuildPanelListener
implements ActionListener {
    final /* synthetic */ GameScreen screen;

    BuildPanelListener(GameScreen gameScreen) {
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
            int n = 0;
            while (n < unit.getSubUnits().size()) {
                Unit unit2 = (Unit)unit.getSubUnits().get(n);
                if (!unit2.getUnitType().isGeneral()) {
                    GameScreen.getSelectedUnits(this.screen).add(unit2);
                }
                ++n;
            }
            GameScreen.invokeSetScene(this.screen, GameScene.Attack);
            GuiAssets.playClickSound();
        } else if (i3 == 10001) {
            GameScreen.getUnitCommander(this.screen).toggleHostedAuto(unit);
            GameScreen.getGroupBar(this.screen).collapse();
            GameScreen.getSelectedUnits(this.screen).clear();
            GameScreen.invokeSetScene(this.screen, GameScene.Default);
            this.screen.getAudio().playOk();
        } else if (i3 == 10002) {
            GameScreen.invokeSetScene(this.screen, GameScene.Attack);
            GuiAssets.playClickSound();
        } else if (i3 == 11000) {
            GameScreen.invokeCommandUngroup(this.screen);
        } else if (i3 > 0) {
            UnitType unitType = (UnitType)unit.getUnitType().getProducedBy().get(i3 - 1);
            GameScreen.getTutorialHintBox(this.screen).notifyStepEvent("BuildGround");
            GameScreen.getUnitCommander(this.screen).buildUnit(unit, unitType);
            GameScreen.invokeSetScene(this.screen, GameScene.Build);
            if (GameScreen.getLastBuildActionTime(this.screen) <= GameScreen.getGameTimeNanos(this.screen)) {
                this.screen.getAudio().playConstructing();
            }
            GameScreen.setLastBuildActionTime(this.screen, GameScreen.getGameTimeNanos(this.screen) + 1000000000L);
        } else if (-i3 - 1 < unit.getSubUnits().size()) {
            Unit unit3 = (Unit)unit.getSubUnits().get(-i3 - 1);
            if (!unit3.isCountZero()) {
                GameScreen.getUnitCommander(this.screen).cancelBuild(unit3);
                this.screen.getAudio().playOk();
            } else {
                GameScreen.getTutorialHintBox(this.screen).notifyStepEvent("ClosePanel");
                GameScreen.getTutorialHintBox(this.screen).notifyStepEvent("MoveMap");
                GameScreen.getTutorialHintBox(this.screen).notifyStepEvent("SelectGround");
                GameScreen.getSelectedUnits(this.screen).clear();
                GameScreen.getSelectedUnits(this.screen).add(unit3);
                GameScreen.getGroupBar(this.screen).collapse();
                GameScreen.invokeSetScene(this.screen, GameScene.Build);
                GuiAssets.playClickSound();
            }
        }
    }
}


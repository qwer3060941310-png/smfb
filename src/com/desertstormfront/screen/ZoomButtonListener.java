/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.screen.GameScreen;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Widget;

class ZoomButtonListener
implements ActionListener {
    final /* synthetic */ GameScreen a;

    ZoomButtonListener(GameScreen gameScreen) {
        this.a = gameScreen;
    }

    @Override
    public void onAction(Widget widget) {
        int i2 = (Integer)widget.getData();
        if (i2 == 100) {
            GameScreen.getMinimapRenderer(this.a).setVisible(false);
            GameScreen.getMinimapPanel(this.a).setExpanded(false);
        } else if (i2 == 101) {
            GameScreen gameScreen = this.a;
            GameScreen.setTargetZoom(gameScreen, GameScreen.getTargetZoom(gameScreen) + 0.5f);
            if (GameScreen.getTargetZoom(this.a) > 1.0f) {
                GameScreen.setTargetZoom(this.a, 1.0f);
            }
            GameScreen.getMinimapPanel(this.a).setZoomOutEnabled(true);
            GameScreen.getMinimapPanel(this.a).setZoomInEnabled(GameScreen.getTargetZoom(this.a) < 1.0f);
        } else if (i2 == 102) {
            GameScreen gameScreen = this.a;
            GameScreen.setTargetZoom(gameScreen, GameScreen.getTargetZoom(gameScreen) - 0.5f);
            if (GameScreen.getTargetZoom(this.a) < 0.5f) {
                GameScreen.setTargetZoom(this.a, 0.5f);
            }
            GameScreen.getMinimapPanel(this.a).setZoomOutEnabled(GameScreen.getTargetZoom(this.a) > 0.5f);
            GameScreen.getMinimapPanel(this.a).setZoomInEnabled(true);
        } else if (i2 == 200) {
            GameScreen.getMinimapRenderer(this.a).setVisible(true);
            GameScreen.getMinimapPanel(this.a).setExpanded(true);
        }
    }
}


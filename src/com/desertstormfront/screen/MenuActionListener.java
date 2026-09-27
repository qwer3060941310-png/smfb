/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.screen.BaseMenuScreen;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;

class MenuActionListener
implements ActionListener {
    final /* synthetic */ BaseMenuScreen menuScreen;

    MenuActionListener(BaseMenuScreen baseMenuScreen) {
        this.menuScreen = baseMenuScreen;
    }

    @Override
    public void onAction(Widget widget) {
        GuiAssets.playClickSound();
        BaseMenuScreen.setNextScreen(this.menuScreen, this.menuScreen.handleWidgetAction(widget));
    }
}


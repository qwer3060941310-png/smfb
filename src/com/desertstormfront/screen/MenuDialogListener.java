/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.screen.BaseMenuScreen;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;

class MenuDialogListener
implements ActionListener {
    final /* synthetic */ BaseMenuScreen menuScreen;

    MenuDialogListener(BaseMenuScreen baseMenuScreen) {
        this.menuScreen = baseMenuScreen;
    }

    @Override
    public void onAction(Widget widget) {
        BaseMenuScreen.getMessageDialog(this.menuScreen).setVisible(false);
        GuiAssets.playClickSound();
        BaseMenuScreen.setNextScreen(this.menuScreen, this.menuScreen.handleAction(BaseMenuScreen.getPendingAction(this.menuScreen), (Integer)widget.getData()));
    }
}


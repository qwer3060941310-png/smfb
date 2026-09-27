/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.screen.GameScreen;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Widget;

class SquadBarListener
implements ActionListener {
    final /* synthetic */ GameScreen a;

    SquadBarListener(GameScreen gameScreen) {
        this.a = gameScreen;
    }

    @Override
    public void onAction(Widget widget) {
        int i2 = (Integer)widget.getData();
        GameScreen.invokeSelectUnitGroup(this.a, i2);
    }
}


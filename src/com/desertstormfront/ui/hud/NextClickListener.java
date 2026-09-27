/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.TutorialHintBox;

class NextClickListener
implements ActionListener {
    final /* synthetic */ TutorialHintBox hintBox;

    NextClickListener(TutorialHintBox tutorialHintBox) {
        this.hintBox = tutorialHintBox;
    }

    @Override
    public void onAction(Widget widget) {
        TutorialHintBox.getStepPlayerFromListener(this.hintBox).advance();
    }
}


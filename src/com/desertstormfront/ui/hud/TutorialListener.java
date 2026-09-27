/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.ui.hud.TutorialHintBox;
import com.noblemaster.lib.script.StepListener;

class TutorialListener
implements StepListener {
    final /* synthetic */ TutorialHintBox hintBox;

    TutorialListener(TutorialHintBox tutorialHintBox) {
        this.hintBox = tutorialHintBox;
    }

    @Override
    public void onStepChanged() {
        TutorialHintBox.refreshFromListener(this.hintBox);
    }
}


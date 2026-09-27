/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.script;

import com.noblemaster.lib.script.ScriptStep;
import com.noblemaster.lib.script.ScriptStepList;
import com.noblemaster.lib.script.StepListener;
import java.util.ArrayList;
import java.util.List;

public class StepSequencePlayer {
    private List<StepListener> a = new ArrayList<StepListener>();
    private int b;
    private ScriptStepList c;

    public StepSequencePlayer() {
        this(null);
    }

    public StepSequencePlayer(ScriptStepList scriptStepList) {
        this.setSteps(scriptStepList);
    }

    public void advance() {
        ScriptStep scriptStep = this.getCurrentStep();
        if (scriptStep != null && scriptStep.getExpectedInput() == null) {
            ++this.b;
            this.notifyListeners();
        }
    }

    public void advance(String string) {
        ScriptStep scriptStep = this.getCurrentStep();
        if (scriptStep != null && string.equals(scriptStep.getExpectedInput())) {
            ++this.b;
            this.notifyListeners();
        }
    }

    public ScriptStep getCurrentStep() {
        if (this.c != null && this.b < this.c.size()) {
            return (ScriptStep)this.c.get(this.b);
        }
        return null;
    }

    public void setSteps(ScriptStepList scriptStepList) {
        this.c = scriptStepList;
        this.b = 0;
        this.notifyListeners();
    }

    public void addListener(StepListener stepListener) {
        this.a.add(stepListener);
    }

    private void notifyListeners() {
        for (StepListener stepListener : this.a) {
            stepListener.onStepChanged();
        }
    }
}


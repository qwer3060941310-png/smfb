/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.script;

import com.noblemaster.lib.script.ScriptStep;
import java.io.Serializable;
import java.util.ArrayList;

public class ScriptStepList
extends ArrayList
implements Serializable {
    public ScriptStepList() {
    }

    public ScriptStepList(String[][] stringArray) {
        int i2 = 0;
        while (i2 < stringArray.length) {
            this.add(new ScriptStep(stringArray[i2][0], stringArray[i2][1], stringArray[i2][2], stringArray[i2][3]));
            ++i2;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.script;

public class ScriptStep {
    private String a;
    private String b;
    private String c;
    private String d;

    public ScriptStep() {
        this(null, null);
    }

    public ScriptStep(String string, String string2) {
        this(string, string2, null, null);
    }

    public ScriptStep(String string, String string2, String string3, String string4) {
        this.a = string;
        this.b = string2;
        this.c = string3;
        this.d = string4;
    }

    public String getTextKey() {
        return this.a;
    }

    public String getPositionSpec() {
        return this.b;
    }

    public String getExpectedInput() {
        return this.c;
    }

    public String getActionCode() {
        return this.d;
    }
}


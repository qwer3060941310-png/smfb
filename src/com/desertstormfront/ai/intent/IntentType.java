/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.intent;

/**
 * 意图类型枚举（攻击/防御/探索/巡逻等）
 */
public enum IntentType {
    Command("COMD"),
    Truck("TRUC"),
    Explore("XPLR"),
    Conquer("CONQ"),
    Attack("ATTA"),
    Defend("DEFD"),
    Patrol("PATR"),
    Rogue("ROGU");

    private String name;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private IntentType(String var3_1) {
        this.name = var3_1;
    }

    public String getName() {
        return this.name;
    }
}


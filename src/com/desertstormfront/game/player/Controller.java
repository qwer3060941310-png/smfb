/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

import com.desertstormfront.game.player.Difficulty;

public enum Controller {
    Human("Human[i18n]: Human", null),
    AiCasual("AI[i18n]: AI", Difficulty.Casual),
    AiNormal("AI[i18n]: AI", Difficulty.Normal),
    AiHard("AI[i18n]: AI", Difficulty.Hard),
    AiExtreme("AI[i18n]: AI", Difficulty.Extreme);

    private String f;
    private Difficulty g;

    /*
     * WARNING - void declaration
     */
    private Controller(String var3_1, Difficulty var4_2) {
        this.f = var3_1;
        this.g = var4_2;
    }

    public final Difficulty getDifficulty() {
        return this.g;
    }

    public static final Controller fromDifficulty(Difficulty difficulty) {
        int i1 = 0;
        while (i1 < Controller.values().length) {
            Controller controller = Controller.values()[i1];
            if (controller.getDifficulty() == difficulty) {
                return controller;
            }
            ++i1;
        }
        return null;
    }

    public final String toString() {
        return this.f;
    }
}


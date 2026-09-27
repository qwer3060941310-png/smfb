/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

enum GameScene {
    LanNotice(false, true),
    NationSelect(false, true),
    WaitingForPlayers(false, true),
    MissionBriefing(false, true),
    Loading(false, true),
    Default(true, false),
    Build(true, false),
    Attack(true, false),
    ForceAttack(true, false),
    Move(true, false),
    PauseMenu(false, true),
    MissionTerminated(false, false),
    ScoreDialog(false, true),
    RestartConfirm(false, true),
    QuitConfirm(false, true),
    RestartGame(false, true),
    ExitToMenu(false, true);

    private boolean isMapScene;
    private boolean isPanelOpen;

    /*
     * WARNING - void declaration
     */
    private GameScene(boolean var3_1, boolean var4_2) {
        this.isMapScene = var3_1;
        this.isPanelOpen = var4_2;
    }

    public boolean isMapScene() {
        return this.isMapScene;
    }

    public boolean isPanelOpen() {
        return this.isPanelOpen;
    }
}


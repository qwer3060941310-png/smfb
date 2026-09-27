/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.audio.AudioClip;
import com.desertstormfront.audio.MusicPlaylist;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.screen.GameScene;
import com.desertstormfront.screen.GameScreen;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.ListBox;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;

class PauseMenuListener
implements ActionListener {
    final /* synthetic */ GameScreen screen;

    PauseMenuListener(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    @Override
    public void onAction(Widget widget) {
        GuiAssets.playClickSound();
        int i2 = (Integer)widget.getData();
        if (i2 == 0) {
            GameScreen.invokeSetScene(this.screen, GameScene.Default);
        }
        if (i2 == 1) {
            GameScreen.invokeSetScene(this.screen, GameScene.RestartConfirm);
        } else if (i2 == 2) {
            GameScreen.invokeSetScene(this.screen, GameScene.QuitConfirm);
        } else if (i2 == 3) {
            UserConfig.setAudioVolume((float)((ListBox)widget).getSelectedIndex() * 0.1f);
            AudioClip.setMasterVolume(UserConfig.getAudioVolume());
        } else if (i2 == 4) {
            UserConfig.setMusicVolume((float)((ListBox)widget).getSelectedIndex() * 0.1f);
            MusicPlaylist.setMasterVolume(UserConfig.getMusicVolume());
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.game.player.Controller;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerList;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.hud.GuiAssets;
import com.desertstormfront.ui.hud.OkDialog;

public final class MissionBriefingDialog
extends OkDialog {
    public MissionBriefingDialog() {
        super(new TextureRegion(1508, 709, 540, 388));
    }

    public void setPlayers(PlayerList playerList) {
        Player player = (Player)playerList.get(0);
        int i3 = 0;
        while (i3 < playerList.size()) {
            if (((Player)playerList.get(i3)).getController() == Controller.Human) {
                player = (Player)playerList.get(i3);
            }
            ++i3;
        }
        i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i5 < playerList.size()) {
            boolean i9;
            Player player2 = (Player)playerList.get(i5);
            TextureRegion textureRegion = GuiAssets.getFlagRegion(player2.getFaction());
            Image image = new Image(textureRegion);
            if (player2 == player || !player2.isEnemyOf(player)) {
                if (i3 < 4) {
                    image.setX(50 + i3 % 2 * 79);
                    image.setY((float)(48 + i3 / 2 * 78));
                    ++i3;
                    i9 = true;
                } else {
                    i9 = false;
                }
            } else if (i4 < 4) {
                image.setX(345 + i4 % 2 * 79);
                image.setY((float)(48 + i4 / 2 * 78));
                ++i4;
                i9 = true;
            } else {
                i9 = false;
            }
            if (i9) {
                this.addChild(image);
            }
            ++i5;
        }
        this.pack();
    }
}


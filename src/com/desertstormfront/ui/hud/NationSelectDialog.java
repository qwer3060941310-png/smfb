/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerList;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.hud.GuiAssets;
import com.desertstormfront.ui.hud.MessageDialog;
import com.noblemaster.lib.i18n.Messages;
import java.util.ArrayList;
import java.util.List;

public final class NationSelectDialog
extends MessageDialog {
    private List a;
    private List b;

    public NationSelectDialog(PlayerList playerList) {
        this(playerList, null);
    }

    public NationSelectDialog(PlayerList playerList, String[] stringArray) {
        super(Messages.getFallback("SelectNation[i18n]: Select Nation"), true);
        boolean i3 = GameConfig.isDarkTheme();
        this.a = new ArrayList();
        this.b = new ArrayList();
        ButtonStyle buttonStyle = new ButtonStyle(new int[]{1358, 1261, 1164, 1067}, new int[]{51, 51, 51, 51}, 96, 96);
        int i5 = 0;
        while (i5 < playerList.size()) {
            float f6 = 98 + i5 % 4 * 102 + (playerList.size() < 4 ? (4 - playerList.size()) * 51 : 0);
            float f7 = 91 + i5 / 4 * 126 + (playerList.size() <= 4 ? 51 : 0);
            Player player = (Player)playerList.get(i5);
            TextureRegion textureRegion = GuiAssets.getFlagRegion(player.getFaction());
            Image image = new Image(textureRegion);
            image.setX(f6 - (float)(textureRegion.width / 2));
            image.setY(f7 - (float)(textureRegion.height / 2));
            this.addChild(image);
            Label label = i3 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
            label.setX(f6 - (float)(textureRegion.width / 2) - 16.0f);
            label.setY(f7 + (float)(textureRegion.height / 2) + 11.0f);
            label.setMaxWidth(textureRegion.width + 32);
            label.setAlign(Align.CENTER);
            this.addChild(label);
            this.a.add(label);
            Button button = new Button(buttonStyle);
            button.setData(i5);
            button.setX(f6 - (float)(buttonStyle.width / 2) + 1.0f);
            button.setY(f7 - (float)(buttonStyle.height / 2) + 1.0f);
            this.addChild(button);
            this.b.add(button);
            ++i5;
        }
        this.setPlayers(playerList, stringArray);
        this.pack();
    }

    public void setPlayers(PlayerList playerList, String[] stringArray) {
        boolean i3 = GameConfig.isDarkTheme();
        int i4 = 0;
        while (i4 < playerList.size()) {
            Player player = (Player)playerList.get(i4);
            String string = null;
            if (stringArray != null) {
                string = stringArray[i4];
            }
            if (string != null) {
                ((Label)this.a.get(i4)).setText(String.valueOf(string) + "\n" + (player.getTeam() != null ? (i3 ? Messages.get(player.getTeam().getName()) : Messages.getFallback(player.getTeam().getName())) : ""));
                ((Button)this.b.get(i4)).setEnabled(false);
            } else {
                ((Label)this.a.get(i4)).setText("<open>\n" + (player.getTeam() != null ? (i3 ? Messages.get(player.getTeam().getName()) : Messages.getFallback(player.getTeam().getName())) : ""));
                ((Button)this.b.get(i4)).setEnabled(true);
            }
            ++i4;
        }
    }
}


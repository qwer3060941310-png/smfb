/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.config.UserConfig;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.ListBox;
import com.desertstormfront.ui.ScrollPane;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.hud.GuiAssets;
import com.noblemaster.lib.i18n.Messages;

public final class InGameMenu
extends Container {
    public InGameMenu(String string) {
        Image image = new Image(new TextureRegion(1535, 0, 513, 348));
        this.addChild(image);
        Label label = new Label(GuiAssets.getFontXirod17());
        label.setX(47.0f);
        label.setY(18.0f);
        label.setText(Messages.get("Menu[i18n]: Menu"));
        label.a(-16777216);
        this.addChild(label);
        Label label2 = new Label(GuiAssets.getFontXirod17());
        label2.setX(45.0f);
        label2.setY(16.0f);
        label2.setText(Messages.get("Menu[i18n]: Menu"));
        this.addChild(label2);
        this.createButton(0, 46, Messages.get("Resume[i18n]: Resume"));
        this.createButton(1, 100, Messages.get("Restart[i18n]: Restart"));
        this.createButton(2, 154, Messages.get("QuitGame[i18n]: Quit Game"));
        ScrollPane scrollPane = new ScrollPane(GuiAssets.getScrollBarStyle());
        scrollPane.setX(251.0f);
        scrollPane.setY(48.0f);
        scrollPane.setWidth(219.0f);
        scrollPane.setHeight(152.0f);
        this.addChild(scrollPane);
        Label label3 = new Label(GuiAssets.getDefaultFont());
        label3.setText(string);
        label3.setMaxWidth(Math.round(scrollPane.getWidth() - (float)scrollPane.getScrollBarWidth()));
        label3.a(-6250336);
        scrollPane.setContent(label3);
        ListBox listBox = new ListBox(GuiAssets.getListBoxItemStyle(), GuiAssets.getListBoxSelectionStyle(), GuiAssets.getDefaultFont(), 250);
        listBox.setLabelAlign(-16732433);
        listBox.setData((Object)3);
        listBox.setX(43.0f);
        listBox.setY(215.0f);
        listBox.setItems(new String[]{Messages.get("SoundFXOff[i18n]: Sound FX: Off"), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 10), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 20), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 30), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 40), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 50), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 60), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 70), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 80), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 90), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 100)});
        this.addChild(listBox);
        listBox.setSelectedIndex(Math.round(UserConfig.getAudioVolume() * 10.0f));
        ListBox listBox2 = new ListBox(GuiAssets.getListBoxItemStyle(), GuiAssets.getListBoxSelectionStyle(), GuiAssets.getDefaultFont(), 250);
        listBox2.setLabelAlign(-16732433);
        listBox2.setData((Object)4);
        listBox2.setX(43.0f);
        listBox2.setY(270.0f);
        listBox2.setItems(new String[]{Messages.get("MusicOff[i18n]: Music: Off"), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 10), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 20), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 30), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 40), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 50), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 60), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 70), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 80), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 90), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 100)});
        this.addChild(listBox2);
        listBox2.setSelectedIndex(Math.round(UserConfig.getMusicVolume() * 10.0f));
        this.pack();
    }

    private Button createButton(int i1, int i2, String string) {
        Container container = new Container();
        Label label = new Label(GuiAssets.getDefaultFont());
        label.setX(1.0f);
        label.setY(17.0f);
        label.a(-1);
        label.setText(string);
        label.setMaxWidth(GuiAssets.getDialogButtonStyle().width);
        label.setAlign(Align.CENTER);
        container.addChild(label);
        Label label2 = new Label(GuiAssets.getDefaultFont());
        label2.setX(0.0f);
        label2.setY(16.0f);
        label2.a(-16777216);
        label2.setText(string);
        label2.setMaxWidth(GuiAssets.getDialogButtonStyle().width);
        label2.setAlign(Align.CENTER);
        container.addChild(label2);
        Button button = new Button(GuiAssets.getDialogButtonStyle(), container);
        button.setData(i1);
        button.setX(43.0f);
        button.setY((float)i2);
        this.addChild(button);
        return button;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.ScrollPane;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.hud.GuiAssets;
import com.noblemaster.lib.i18n.Messages;

public abstract class OkDialog
extends Container {
    private Label titleLabel;
    private Label titleShadowLabel;
    private ScrollPane scrollPane;
    private Label textLabel;

    public OkDialog(TextureRegion textureRegion) {
        Image image = new Image(textureRegion);
        this.addChild(image);
        this.titleShadowLabel = new Label(GuiAssets.getFontXirod17());
        this.titleShadowLabel.setX(47.0f);
        this.titleShadowLabel.setY(18.0f);
        this.titleShadowLabel.a(-16777216);
        this.addChild(this.titleShadowLabel);
        this.titleLabel = new Label(GuiAssets.getFontXirod17());
        this.titleLabel.setX(45.0f);
        this.titleLabel.setY(16.0f);
        this.addChild(this.titleLabel);
        this.scrollPane = new ScrollPane(GuiAssets.getScrollBarStyle());
        this.scrollPane.setX(56.0f);
        this.scrollPane.setY(210.0f);
        this.scrollPane.setWidth(431.0f);
        this.scrollPane.setHeight(103.0f);
        this.addChild(this.scrollPane);
        this.textLabel = new Label(GuiAssets.getDefaultFont());
        this.textLabel.setMaxWidth(Math.round(this.scrollPane.getWidth() - (float)this.scrollPane.getScrollBarWidth()));
        this.textLabel.a(-6250336);
        this.scrollPane.setContent(this.textLabel);
        String string = Messages.get("OK[i18n]: OK");
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
        button.setData(0);
        button.setX(174.0f);
        button.setY(329.0f);
        this.addChild(button);
    }

    public void setTitle(String string) {
        this.titleLabel.setText(string);
        this.titleShadowLabel.setText(string);
        this.pack();
    }

    public void setMessage(String string) {
        this.textLabel.setText(string);
        this.pack();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.NinePatch;
import com.desertstormfront.ui.NinePatchImage;
import com.desertstormfront.ui.ScrollPane;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import java.util.ArrayList;
import java.util.List;

public class MessageDialog
extends Container {
    private Label titleLabel;
    private Label titleShadowLabel;
    private ScrollPane scrollPane;
    private Label textLabel;
    private List buttons;

    public MessageDialog() {
        this(null);
    }

    public MessageDialog(String string) {
        this(string, null, null, false);
    }

    public MessageDialog(String string, boolean bl) {
        this(string, null, null, bl);
    }

    public MessageDialog(String string, String string2, String[] stringArray) {
        this(string, string2, stringArray, false);
    }

    public MessageDialog(String string, String string2, String[] stringArray, boolean bl) {
        Image image = new Image(new TextureRegion(1019, 0, 503, 332));
        this.addChild(image);
        if (bl) {
            NinePatchImage ninePatchImage = new NinePatchImage(new NinePatch(1456, 1461, 1466, 1472, 70, 75, 80, 86));
            ninePatchImage.setX(39.0f);
            ninePatchImage.setY(43.0f);
            ninePatchImage.setWidth(428.0f);
            ninePatchImage.setHeight(216.0f);
            this.addChild(ninePatchImage);
        } else {
            NinePatchImage ninePatchImage = new NinePatchImage(new NinePatch(1456, 1461, 1466, 1472, 52, 57, 62, 68));
            ninePatchImage.setX(46.0f);
            ninePatchImage.setY(49.0f);
            ninePatchImage.setWidth(412.0f);
            ninePatchImage.setHeight(199.0f);
            this.addChild(ninePatchImage);
        }
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
        this.scrollPane.setX(46.0f);
        this.scrollPane.setY(51.0f);
        this.scrollPane.setWidth(413.0f);
        this.scrollPane.setHeight(196.0f);
        this.addChild(this.scrollPane);
        this.textLabel = new Label(GuiAssets.getDefaultFont());
        this.textLabel.setMaxWidth(Math.round(this.scrollPane.getWidth() - (float)this.scrollPane.getScrollBarWidth()));
        this.textLabel.a(-6250336);
        this.scrollPane.setContent(this.textLabel);
        this.buttons = new ArrayList();
        this.setTitle(string);
        this.setText(string2);
        this.setLines(stringArray);
    }

    public void setTitle(String string) {
        this.titleLabel.setText(string);
        this.titleShadowLabel.setText(string);
        this.pack();
    }

    public void setText(String string) {
        this.textLabel.setText(string);
        this.pack();
    }

    public void setLines(String[] stringArray) {
        int i2 = 0;
        while (i2 < this.buttons.size()) {
            this.removeChild((Widget)this.buttons.get(i2));
            ++i2;
        }
        this.buttons.clear();
        if (stringArray != null) {
            i2 = (496 - (stringArray.length * GuiAssets.getDialogButtonStyle().width + (stringArray.length - 1) * 4)) / 2;
            int i3 = 0;
            while (i3 < stringArray.length) {
                Image image = new Image(new TextureRegion(241, 314, 197, 51));
                image.setX(i2 + i3 * (GuiAssets.getDialogButtonStyle().width + 6) - 3);
                image.setY(255.0f);
                this.addChild(image);
                Container container = new Container();
                Label label = new Label(GuiAssets.getDefaultFont());
                label.setX(1.0f);
                label.setY(17.0f);
                label.a(-1);
                label.setText(stringArray[i3]);
                label.setMaxWidth(GuiAssets.getDialogButtonStyle().width);
                label.setAlign(Align.CENTER);
                container.addChild(label);
                Label label2 = new Label(GuiAssets.getDefaultFont());
                label2.setX(0.0f);
                label2.setY(16.0f);
                label2.a(-16777216);
                label2.setText(stringArray[i3]);
                label2.setMaxWidth(GuiAssets.getDialogButtonStyle().width);
                label2.setAlign(Align.CENTER);
                container.addChild(label2);
                Button button = new Button(GuiAssets.getDialogButtonStyle(), container);
                button.setData(i3);
                button.setX(i2 + i3 * (GuiAssets.getDialogButtonStyle().width + 6));
                button.setY(257.0f);
                this.addChild(button);
                this.buttons.add(button);
                ++i3;
            }
        }
        this.pack();
    }
}


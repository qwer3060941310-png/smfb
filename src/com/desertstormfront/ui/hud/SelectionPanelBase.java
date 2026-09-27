/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Layer;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.BuildingPanel;
import com.desertstormfront.ui.hud.GuiAssets;
import com.desertstormfront.ui.hud.SingleUnitPanel;
import com.noblemaster.lib.i18n.Messages;

public abstract class SelectionPanelBase
extends Container {
    protected static final Domain[] domains = new Domain[]{Domain.Ground, Domain.Air, Domain.Water, Domain.Water};
    protected static final Layer[] layers = new Layer[]{Layer.Base, Layer.Upper, Layer.Base, Layer.Under};
    protected final TextureRegion[] c;
    protected final TextureRegion[] d;
    protected UnitCommander unitCommander;
    protected Image[] f;
    protected Image[] g;
    private Label titleLabel;
    private Container infoContainer;
    private boolean showExtraPanels;
    private Container carrierPanel;
    private Image carrierHighlight;
    private Container buildingPanel;
    private Image buildingHighlight;
    private Container mobilePanel;
    private Button actionButton;

    protected SelectionPanelBase(UnitCommander unitCommander, TextureRegion[] textureRegionArray) {
        Widget widget;
        this.unitCommander = unitCommander;
        boolean i3 = GameConfig.isDarkTheme();
        UnitTypeList unitTypeList = unitCommander.getWorld().getMapDefinition().getUnitTypes();
        this.c = new TextureRegion[unitTypeList.size()];
        this.d = new TextureRegion[unitTypeList.size()];
        int i5 = 0;
        while (i5 < unitTypeList.size()) {
            this.c[i5] = new TextureRegion(i5 * 79, 1101, 78, 48);
            this.d[i5] = new TextureRegion(i5 * 79, 1150, 78, 48);
            ++i5;
        }
        i5 = 0;
        int n = 0;
        while (n < textureRegionArray.length) {
            widget = new Image(textureRegionArray[n]);
            widget.setY((float)i5);
            this.addChild(widget);
            i5 += ((Image)widget).getRegion().height;
            ++n;
        }
        Image image = new Image(new TextureRegion(461, 810, 122, 74));
        image.setX(30.0f);
        image.setY((float)i5);
        this.addChild(image);
        this.titleLabel = i3 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
        this.titleLabel.setX(43.0f);
        this.titleLabel.setY((float)(7 + (i3 ? 4 : 0)));
        this.titleLabel.a(-5197648);
        this.addChild(this.titleLabel);
        int[] nArray = new int[4];
        nArray[0] = 613;
        nArray[1] = 674;
        nArray[2] = 735;
        widget = new Button(new ButtonStyle(nArray, new int[]{394, 394, 394, 394}, 60, 79));
        widget.setData(-10000);
        widget.setX(176.0f);
        widget.setY(0.0f);
        this.addChild(widget);
        this.f = new Image[domains.length];
        this.g = new Image[domains.length];
        int n2 = 0;
        while (n2 < domains.length) {
            int n3 = 96 + n2 * 19;
            this.f[n2] = new Image(new TextureRegion(0, 366, 12, 14));
            this.f[n2].setX(n3);
            this.f[n2].setY(50.0f);
            this.addChild(this.f[n2]);
            this.f[n2].setVisible(false);
            this.g[n2] = new Image(new TextureRegion(0, 381, 12, 14));
            this.g[n2].setX(n3);
            this.g[n2].setY(65.0f);
            this.addChild(this.g[n2]);
            this.g[n2].setVisible(false);
            ++n2;
        }
        this.infoContainer = new Container();
        this.infoContainer.setX(181.0f);
        this.infoContainer.setY(107.0f);
        this.addChild(this.infoContainer);
        this.infoContainer.addChild(new Image(new TextureRegion(823, 402, 91, 71)));
        Container container = new Container();
        Image image2 = new Image(new TextureRegion(348, 911, 23, 21));
        image2.setX(17.0f);
        image2.setY(4.0f);
        container.addChild(image2);
        Label label = i3 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
        label.setX(0.0f);
        label.setY((float)(23 + (i3 ? 4 : 0)));
        label.a(-805306368);
        label.setText(i3 ? Messages.get("Split[i18n]: Split") : Messages.getFallback("Split[i18n]: Split"));
        label.setMaxWidth(57);
        label.setAlign(Align.CENTER);
        container.addChild(label);
        Button button = new Button(new ButtonStyle(new int[]{193, 251, 309, 460}, new int[]{773, 773, 773, 885}, 57, 45), container);
        button.setData(11000);
        button.setX(12.0f);
        button.setY(13.0f);
        this.infoContainer.addChild(button);
        if (!(this instanceof SingleUnitPanel)) {
            this.showExtraPanels = true;
            this.carrierPanel = new Container();
            this.carrierPanel.setX(181.0f);
            this.carrierPanel.setY((float)(this instanceof BuildingPanel ? 87 : 214));
            this.addChild(this.carrierPanel);
            this.carrierPanel.addChild(new Image(new TextureRegion(915, 402, 91, 142)));
            Container container2 = new Container();
            Image image3 = new Image(new TextureRegion(313, 887, 30, 21));
            image3.setX(14.0f);
            image3.setY(5.0f);
            container2.addChild(image3);
            Label label2 = i3 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
            label2.setX(0.0f);
            label2.setY((float)(26 + (i3 ? 4 : 0)));
            label2.a(-805306368);
            label2.setText(i3 ? Messages.get("Auto[i18n]: Auto") : Messages.getFallback("Auto[i18n]: Auto"));
            label2.setMaxWidth(57);
            label2.setAlign(Align.CENTER);
            container2.addChild(label2);
            Button button2 = new Button(new ButtonStyle(new int[]{193, 251, 309, 460}, new int[]{773, 773, 773, 885}, 57, 45), container2);
            button2.setData(10001);
            button2.setX(12.0f);
            button2.setY(29.0f);
            this.carrierPanel.addChild(button2);
            this.carrierHighlight = new Image(new TextureRegion(256, 887, 56, 20));
            this.carrierHighlight.setX(13.0f);
            this.carrierHighlight.setY(6.0f);
            this.carrierPanel.addChild(this.carrierHighlight);
            this.buildingPanel = new Container();
            this.buildingPanel.setX(181.0f);
            this.buildingPanel.setY((float)(this instanceof BuildingPanel ? 87 : 214));
            this.addChild(this.buildingPanel);
            this.buildingPanel.addChild(new Image(new TextureRegion(915, 402, 91, 142)));
            Container container3 = new Container();
            Image image4 = new Image(new TextureRegion(367, 887, 22, 21));
            image4.setX(18.0f);
            image4.setY(4.0f);
            container3.addChild(image4);
            Label label3 = i3 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
            label3.setX(0.0f);
            label3.setY((float)(26 + (i3 ? 4 : 0)));
            label3.a(-805306368);
            label3.setText(i3 ? Messages.get("Rally[i18n]: Rally") : Messages.getFallback("Rally[i18n]: Rally"));
            label3.setMaxWidth(57);
            label3.setAlign(Align.CENTER);
            container3.addChild(label3);
            Button button3 = new Button(new ButtonStyle(new int[]{193, 251, 309, 460}, new int[]{773, 773, 773, 885}, 57, 45), container3);
            button3.setData(10002);
            button3.setX(12.0f);
            button3.setY(29.0f);
            this.buildingPanel.addChild(button3);
            this.buildingHighlight = new Image(new TextureRegion(256, 887, 56, 20));
            this.buildingHighlight.setX(13.0f);
            this.buildingHighlight.setY(6.0f);
            this.buildingPanel.addChild(this.buildingHighlight);
            this.mobilePanel = new Container();
            this.mobilePanel.setX(181.0f);
            this.mobilePanel.setY(this.carrierPanel.getY() + 71.0f);
            this.addChild(this.mobilePanel);
            this.mobilePanel.addChild(new Image(new TextureRegion(823, 402, 91, 71)));
            Container container4 = new Container();
            Image image5 = new Image(new TextureRegion(345, 887, 21, 21));
            image5.setX(19.0f);
            image5.setY(5.0f);
            container4.addChild(image5);
            Label label4 = i3 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
            label4.setX(0.0f);
            label4.setY((float)(26 + (i3 ? 4 : 0)));
            label4.a(-805306368);
            label4.setText(i3 ? Messages.get("Move[i18n]: Move") : Messages.getFallback("Move[i18n]: Move"));
            label4.setMaxWidth(57);
            label4.setAlign(Align.CENTER);
            container4.addChild(label4);
            this.actionButton = new Button(new ButtonStyle(new int[]{193, 251, 309, 460}, new int[]{773, 773, 773, 885}, 57, 45), container4);
            this.actionButton.setData(10000);
            this.actionButton.setX(193.0f);
            this.actionButton.setY(this.mobilePanel.getY() + 13.0f);
            this.addChild(this.actionButton);
        } else {
            this.showExtraPanels = false;
        }
    }

    @Override
    public boolean contains(float f1, float f2) {
        if (f1 < this.getX() || f2 < this.getY() || f2 > this.getY() + this.getHeight() - 74.0f) {
            return false;
        }
        if (f1 < this.getX() + 192.0f) {
            return true;
        }
        if (f2 >= this.getY() + 0.0f && f2 <= this.getY() + 82.0f && f1 < this.getX() + 240.0f) {
            return true;
        }
        if (this.infoContainer.isVisible() && f2 >= this.getY() + this.infoContainer.getY() && f2 <= this.getY() + this.infoContainer.getY() + this.infoContainer.getHeight() && f1 < this.getX() + 268.0f) {
            return true;
        }
        if (this.mobilePanel != null && this.mobilePanel.isVisible() && f2 >= this.getY() + this.mobilePanel.getY() && f2 <= this.getY() + this.mobilePanel.getY() + this.mobilePanel.getHeight() && f1 < this.getX() + 268.0f) {
            return true;
        }
        if (this.carrierPanel != null && this.carrierPanel.isVisible() && f2 >= this.getY() + this.carrierPanel.getY() && f2 <= this.getY() + this.carrierPanel.getY() + this.carrierPanel.getHeight() && f1 < this.getX() + 268.0f) {
            return true;
        }
        return this.buildingPanel != null && this.buildingPanel.isVisible() && f2 >= this.getY() + this.buildingPanel.getY() && f2 <= this.getY() + this.buildingPanel.getY() + this.buildingPanel.getHeight() && f1 < this.getX() + 268.0f;
    }

    protected void a(String string, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6) {
        this.titleLabel.setText(string);
        if (this.showExtraPanels) {
            if (bl) {
                this.carrierHighlight.setVisible(bl2);
                this.carrierPanel.setVisible(true);
                this.buildingPanel.setVisible(false);
                this.mobilePanel.setVisible(false);
            } else if (bl3) {
                this.carrierPanel.setVisible(false);
                this.buildingHighlight.setVisible(bl4);
                this.buildingPanel.setVisible(true);
                this.mobilePanel.setVisible(false);
            } else {
                this.carrierPanel.setVisible(false);
                this.buildingPanel.setVisible(false);
                this.mobilePanel.setVisible(true);
            }
            this.actionButton.setEnabled(bl5);
        }
        this.infoContainer.setVisible(bl6);
    }
}


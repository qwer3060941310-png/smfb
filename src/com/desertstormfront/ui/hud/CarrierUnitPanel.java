/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitOrderMode;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import com.desertstormfront.ui.hud.UnitPanelBase;
import com.noblemaster.lib.i18n.Messages;
import java.util.ArrayList;
import java.util.List;

public final class CarrierUnitPanel
extends UnitPanelBase {
    private List k;
    private List l;
    private List m;
    private List n;
    private List o;
    private long p;

    public CarrierUnitPanel(UnitCommander unitCommander) {
        super(unitCommander, new TextureRegion[]{new TextureRegion(511, 0, 224, 388)});
        int i9;
        int i8;
        Button button;
        Widget widget;
        Image image;
        Widget widget2;
        boolean i2 = GameConfig.isDarkTheme();
        this.k = new ArrayList();
        this.l = new ArrayList();
        this.m = new ArrayList();
        this.n = new ArrayList();
        this.o = new ArrayList();
        int i3 = 0;
        while (i3 < h.length) {
            widget2 = new Container();
            image = new Image(i[i3]);
            image.setX(11.0f);
            image.setY(4.0f);
            ((Container)widget2).addChild(image);
            widget = i2 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
            widget.setX(0.0f);
            widget.setY((float)(29 + (i2 ? 4 : 0)));
            ((Label)widget).a(-268435456);
            ((Label)widget).setText(i2 ? Messages.get(h[i3]) : Messages.getFallback(h[i3]));
            ((Label)widget).setMaxWidth(GuiAssets.getUnitSlotButtonStyle().width);
            ((Label)widget).setAlign(Align.CENTER);
            ((Container)widget2).addChild(widget);
            button = new Button(GuiAssets.getUnitSlotButtonStyle().copy(), widget2);
            button.setData(i3);
            i8 = i3 < 3 ? i3 : i3 - 1;
            i9 = 4 + i8 % 2 * (GuiAssets.getUnitSlotButtonStyle().width + 7);
            int n = 88 + i8 / 2 * (GuiAssets.getUnitSlotButtonStyle().height + 8);
            button.setX(i9);
            button.setY((float)n);
            this.addChild(button);
            this.k.add(button);
            ++i3;
        }
        i3 = 0;
        while (i3 < 6) {
            widget2 = new Image(new TextureRegion());
            widget2.setX(1.0f);
            widget2.setY(0.0f);
            this.l.add(widget2);
            image = new Image(new TextureRegion(0, 0, 80, 48));
            image.setX(0.0f);
            image.setY(0.0f);
            this.m.add(image);
            widget = new Container();
            ((Container)widget).addChild(image);
            ((Container)widget).addChild(widget2);
            button = new Button(GuiAssets.getUnitSlotButtonStyle().copy(), widget);
            button.setData(-i3 - 1);
            i8 = 4 + i3 % 2 * (GuiAssets.getUnitSlotButtonStyle().width + 7);
            i9 = 204 + i3 / 2 * (GuiAssets.getUnitSlotButtonStyle().height + 8);
            button.setX(i8);
            button.setY((float)i9);
            this.addChild(button);
            this.k.add(button);
            Image image2 = new Image(new TextureRegion(0, 531, GuiAssets.getProductionButtonStyle().width, GuiAssets.getProductionButtonStyle().height));
            image2.setX(i8);
            image2.setY((float)i9);
            this.addChild(image2);
            this.n.add(image2);
            Image image3 = new Image(new TextureRegion(81, 531, GuiAssets.getProductionButtonStyle().width, GuiAssets.getProductionButtonStyle().height));
            image3.setX(i8);
            image3.setY((float)i9);
            this.addChild(image3);
            this.o.add(image3);
            ++i3;
        }
        this.pack();
    }

    @Override
    public void setVisible(boolean bl) {
        if (this.isVisible() != bl) {
            this.p = System.currentTimeMillis();
        }
        super.setVisible(bl);
    }

    @Override
    public void setUnit(Unit unit) {
        super.setUnit(unit);
        UnitType unitType = unit.getUnitType();
        ((Button)this.k.get(0)).setEnabled(this.unitCommander.isCommandable(unit));
        ((Button)this.k.get(1)).setEnabled(this.unitCommander.b(unit));
        ((Button)this.k.get((int)1)).getStyle().frameY[3] = unit.getOrderMode() == UnitOrderMode.b ? 531 : 433;
        ((Button)this.k.get(2)).setVisible(!unitType.isFlying());
        ((Button)this.k.get(2)).setEnabled(this.unitCommander.canRepair(unit));
        ((Button)this.k.get((int)2)).getStyle().frameY[3] = unit.getOrderMode() == UnitOrderMode.c ? 531 : 433;
        ((Button)this.k.get(3)).setVisible(unitType.isFlying());
        ((Button)this.k.get(3)).setEnabled(this.unitCommander.canSetAuto(unit));
        ((Button)this.k.get((int)3)).getStyle().frameY[3] = unit.getOrderMode() == UnitOrderMode.d ? 531 : 433;
        ((Button)this.k.get(4)).setEnabled(this.unitCommander.canStop(unit));
        ButtonStyle buttonStyle = ((Button)this.k.get(0)).getStyle();
        buttonStyle.frameX[0] = (System.currentTimeMillis() - this.p) / 400L % 2L == 0L ? 162 : 0;
        UnitList unitList = unit.getSubUnits();
        int i5 = 0;
        while (i5 < 6) {
            Button button = (Button)this.k.get(j + i5);
            if (i5 < unitList.size()) {
                Unit unit2 = (Unit)unitList.get(i5);
                UnitType unitType2 = unit2.getUnitType();
                ((Image)this.n.get(i5)).setVisible(false);
                ((Image)this.o.get(i5)).setVisible(false);
                if (!unit2.isDeployed()) {
                    int i9 = 12 * unit2.getHealth() / 1000 + (!unitType2.isFlying() ? 12 : (int)(12.0f * unit2.getProgress() / unitType2.getAltitude()));
                    if (i9 == 0) {
                        i9 = 1;
                    }
                    TextureRegion textureRegion = ((Image)this.m.get(i5)).getRegion();
                    textureRegion.x = 0 + i9 * 81;
                    textureRegion.y = 1248;
                    ((Image)this.m.get(i5)).setVisible(true);
                } else {
                    ((Image)this.m.get(i5)).setVisible(false);
                }
                ((Image)this.l.get(i5)).setRegion(button.isEnabled() ? this.c[unitType2.getId()] : this.d[unitType2.getId()]);
                ((Image)this.l.get(i5)).pack();
                button.setVisible(true);
            } else {
                button.setVisible(false);
                if (i5 < unitType.getCapacity()) {
                    ((Image)this.n.get(i5)).setVisible(true);
                    ((Image)this.o.get(i5)).setVisible(false);
                } else {
                    ((Image)this.n.get(i5)).setVisible(false);
                    ((Image)this.o.get(i5)).setVisible(true);
                }
            }
            ++i5;
        }
    }
}


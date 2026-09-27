/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.app.support.TextFormatter;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonState;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import com.desertstormfront.ui.hud.UnitPanelBase;
import java.util.ArrayList;
import java.util.List;

public final class BuildingPanel
extends UnitPanelBase {
    private List k = new ArrayList();
    private List l = new ArrayList();
    private List m = new ArrayList();
    private List n = new ArrayList();
    private List o = new ArrayList();
    private List p = new ArrayList();

    public BuildingPanel(UnitCommander unitCommander) {
        super(unitCommander, new TextureRegion[]{new TextureRegion(736, 0, 282, 387)});
        int i8;
        int i7;
        Button button;
        Widget widget;
        Image image;
        Widget widget2;
        int i2 = 0;
        while (i2 < 6) {
            widget2 = new Container();
            image = new Image(new TextureRegion());
            image.setX(0.0f);
            image.setY(0.0f);
            ((Container)widget2).addChild(image);
            this.l.add(image);
            widget = new Label(GuiAssets.getFontDungeon15Outline());
            widget.setX(0.0f);
            widget.setY(26.0f);
            ((Label)widget).setMaxWidth(GuiAssets.getProductionButtonStyle().width - 4);
            ((Label)widget).setAlign(Align.RIGHT);
            ((Container)widget2).addChild(widget);
            this.m.add(widget);
            button = new Button(GuiAssets.getProductionButtonStyle(), widget2);
            button.setData(i2 + 1);
            i7 = 4 + i2 % 3 * (GuiAssets.getProductionButtonStyle().width + 7);
            i8 = 261 + i2 / 3 * (GuiAssets.getProductionButtonStyle().height + 7);
            button.setX(i7);
            button.setY((float)i8);
            this.addChild(button);
            this.k.add(button);
            ++i2;
        }
        i2 = 0;
        while (i2 < 8) {
            widget2 = new Image(new TextureRegion());
            widget2.setX(1.0f);
            widget2.setY(0.0f);
            this.l.add(widget2);
            image = new Image(new TextureRegion(0, 0, 80, 48));
            image.setX(0.0f);
            image.setY(0.0f);
            this.n.add(image);
            widget = new Container();
            ((Container)widget).addChild(image);
            ((Container)widget).addChild(widget2);
            button = new Button(GuiAssets.getUnitSlotButtonStyle().copy(), widget);
            button.setData(-i2 - 1);
            i7 = 4 + i2 % 2 * (GuiAssets.getUnitSlotButtonStyle().width + 7);
            i8 = 32 + i2 / 2 * (GuiAssets.getUnitSlotButtonStyle().height + 8);
            button.setX(i7);
            button.setY((float)i8);
            this.addChild(button);
            this.k.add(button);
            Image image2 = new Image(new TextureRegion(0, 531, GuiAssets.getUnitSlotButtonStyle().width, GuiAssets.getUnitSlotButtonStyle().height));
            image2.setX(i7);
            image2.setY((float)i8);
            this.addChild(image2);
            this.o.add(image2);
            Image image3 = new Image(new TextureRegion(81, 531, GuiAssets.getUnitSlotButtonStyle().width, GuiAssets.getUnitSlotButtonStyle().height));
            image3.setX(i7);
            image3.setY((float)i8);
            this.addChild(image3);
            this.p.add(image3);
            ++i2;
        }
        this.pack();
    }

    @Override
    public boolean contains(float f1, float f2) {
        if (super.contains(f1, f2)) {
            return true;
        }
        return f2 >= this.getY() + 237.0f && f1 < this.getX() + 280.0f;
    }

    @Override
    public void setUnit(Unit unit) {
        Object object;
        super.setUnit(unit);
        UnitTypeList unitTypeList = unit.getOwner() != null ? unit.getOwner().getAvailableUnitTypes() : null;
        UnitTypeList unitTypeList2 = unit.getUnitType().getProducedBy();
        int n = 0;
        while (n < 6) {
            Button button = (Button)this.k.get(n);
            if (n < unitTypeList2.size()) {
                object = (UnitType)unitTypeList2.get(n);
                if (unitTypeList != null && unitTypeList.contains(object)) {
                    boolean bl = this.unitCommander.canBuildUnit(unit, (UnitType)object);
                    button.setEnabled(bl);
                    ((Image)this.l.get(n)).setRegion(bl ? this.c[((UnitType)object).getId()] : this.d[((UnitType)object).getId()]);
                    ((Image)this.l.get(n)).pack();
                    ((Label)this.m.get(n)).setText(TextFormatter.formatAmount(((UnitType)object).getHealth()));
                    button.setVisible(true);
                } else {
                    button.setVisible(false);
                }
            } else {
                button.setVisible(false);
            }
            ++n;
        }
        UnitList unitList = unit.getSubUnits();
        int n2 = 0;
        while (n2 < 8) {
            object = (Button)this.k.get(6 + n2);
            if (n2 < unitList.size()) {
                Object object2;
                boolean i9;
                Unit unit2 = (Unit)unitList.get(n2);
                UnitType unitType = unit2.getUnitType();
                ((Image)this.o.get(n2)).setVisible(false);
                ((Image)this.p.get(n2)).setVisible(false);
                if (unit2.isCountFull()) {
                    ButtonStyle buttonStyle = ((Button)object).getStyle();
                    buttonStyle.frameX[ButtonState.NORMAL.ordinal()] = GuiAssets.getFullCountButtonStyle().frameX[ButtonState.NORMAL.ordinal()];
                    buttonStyle.frameY[ButtonState.NORMAL.ordinal()] = GuiAssets.getFullCountButtonStyle().frameY[ButtonState.NORMAL.ordinal()];
                    buttonStyle.frameX[ButtonState.HOVER.ordinal()] = GuiAssets.getFullCountButtonStyle().frameX[ButtonState.HOVER.ordinal()];
                    buttonStyle.frameY[ButtonState.HOVER.ordinal()] = GuiAssets.getFullCountButtonStyle().frameY[ButtonState.HOVER.ordinal()];
                    buttonStyle.frameX[ButtonState.PRESSED.ordinal()] = GuiAssets.getFullCountButtonStyle().frameX[ButtonState.PRESSED.ordinal()];
                    buttonStyle.frameY[ButtonState.PRESSED.ordinal()] = GuiAssets.getFullCountButtonStyle().frameY[ButtonState.PRESSED.ordinal()];
                    ((Image)this.n.get(n2)).setVisible(false);
                    ((Button)object).setEnabled(true);
                    i9 = false;
                } else if (!unit2.isCountZero()) {
                    int n3 = 24 * (unitType.getHealthInt() - unit2.getCount()) / unitType.getHealthInt();
                    object2 = ((Button)object).getStyle();
                    ((ButtonStyle)object2).frameX[ButtonState.DISABLED.ordinal()] = 0 + n3 * 81;
                    ((ButtonStyle)object2).frameY[ButtonState.DISABLED.ordinal()] = 1199;
                    ((Image)this.n.get(n2)).setVisible(false);
                    ((Button)object).setEnabled(false);
                    i9 = false;
                } else {
                    int n4 = 12 * unit2.getHealth() / 1000 + (!unitType.isFlying() ? 12 : (int)(12.0f * unit2.getProgress() / unitType.getAltitude()));
                    object2 = ((Image)this.n.get(n2)).getRegion();
                    ((TextureRegion)object2).x = 0 + n4 * 81;
                    ((TextureRegion)object2).y = 1248;
                    ButtonStyle buttonStyle = ((Button)object).getStyle();
                    buttonStyle.frameX[ButtonState.NORMAL.ordinal()] = GuiAssets.getUnitSlotButtonStyle().frameX[ButtonState.NORMAL.ordinal()];
                    buttonStyle.frameY[ButtonState.NORMAL.ordinal()] = GuiAssets.getUnitSlotButtonStyle().frameY[ButtonState.NORMAL.ordinal()];
                    buttonStyle.frameX[ButtonState.HOVER.ordinal()] = GuiAssets.getUnitSlotButtonStyle().frameX[ButtonState.HOVER.ordinal()];
                    buttonStyle.frameY[ButtonState.HOVER.ordinal()] = GuiAssets.getUnitSlotButtonStyle().frameY[ButtonState.HOVER.ordinal()];
                    buttonStyle.frameX[ButtonState.PRESSED.ordinal()] = GuiAssets.getUnitSlotButtonStyle().frameX[ButtonState.PRESSED.ordinal()];
                    buttonStyle.frameY[ButtonState.PRESSED.ordinal()] = GuiAssets.getUnitSlotButtonStyle().frameY[ButtonState.PRESSED.ordinal()];
                    ((Image)this.n.get(n2)).setVisible(!unit2.isDeployed());
                    ((Button)object).setEnabled(true);
                    i9 = true;
                }
                ((Image)this.l.get(6 + n2)).setRegion(i9 ? this.c[unitType.getId()] : this.d[unitType.getId()]);
                ((Image)this.l.get(6 + n2)).pack();
                ((Widget)object).setVisible(true);
            } else {
                ((Widget)object).setVisible(false);
                if (n2 < unit.getUnitType().getCapacity()) {
                    ((Image)this.o.get(n2)).setVisible(true);
                    ((Image)this.p.get(n2)).setVisible(false);
                } else {
                    ((Image)this.o.get(n2)).setVisible(false);
                    ((Image)this.p.get(n2)).setVisible(true);
                }
            }
            ++n2;
        }
    }
}


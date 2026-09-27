/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
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
import com.desertstormfront.ui.hud.SelectionPanelBase;
import com.noblemaster.lib.i18n.Messages;
import java.util.ArrayList;
import java.util.List;

public final class SquadPanel
extends SelectionPanelBase {
    private final String[] h = new String[]{"Move[i18n]: Move", "Group[i18n]: Group", "Repair[i18n]: Repair", "Stop[i18n]: Stop"};
    private final TextureRegion[] i = new TextureRegion[]{new TextureRegion(579, 577, 56, 26), new TextureRegion(864, 577, 56, 26), new TextureRegion(693, 577, 56, 26), new TextureRegion(807, 577, 56, 26)};
    private final int j = 6;
    private String k;
    private List l;
    private UnitType[] m;
    private int[] n;
    private List o;
    private List p;
    private List q;
    private List r;
    private long s;

    public SquadPanel(UnitCommander unitCommander) {
        super(unitCommander, new TextureRegion[]{new TextureRegion(511, 0, 224, 388)});
        Widget widget;
        Widget widget2;
        boolean i2 = GameConfig.isDarkTheme();
        this.k = i2 ? Messages.get("Squad[i18n]: Squad") : Messages.getFallback("Squad[i18n]: Squad");
        Image image = new Image(new TextureRegion(786, 1052, 80, 48));
        image.setX(5.0f);
        image.setY(33.0f);
        this.addChild(image);
        this.l = new ArrayList(this.h.length);
        int i4 = 0;
        while (i4 < this.h.length) {
            Container container = new Container();
            Image image2 = new Image(this.i[i4]);
            image2.setX(11.0f);
            image2.setY(4.0f);
            container.addChild(image2);
            widget2 = i2 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
            widget2.setX(0.0f);
            widget2.setY((float)(29 + (i2 ? 4 : 0)));
            ((Label)widget2).a(-268435456);
            ((Label)widget2).setText(i2 ? Messages.get(this.h[i4]) : Messages.getFallback(this.h[i4]));
            ((Label)widget2).setMaxWidth(GuiAssets.getUnitSlotButtonStyle().width);
            ((Label)widget2).setAlign(Align.CENTER);
            container.addChild(widget2);
            widget = new Button(GuiAssets.getUnitSlotButtonStyle().copy(), container);
            widget.setData(i4);
            int n = i4++;
            int n2 = 4 + n % 2 * (GuiAssets.getUnitSlotButtonStyle().width + 7);
            int i11 = 88 + n / 2 * (GuiAssets.getUnitSlotButtonStyle().height + 8);
            widget.setX(n2);
            widget.setY((float)i11);
            this.addChild(widget);
            this.l.add(widget);
        }
        this.m = new UnitType[6];
        this.n = new int[6];
        this.o = new ArrayList(6);
        this.p = new ArrayList(6);
        this.q = new ArrayList(6);
        this.r = new ArrayList(6);
        i4 = 0;
        while (i4 < 6) {
            int n = 4 + i4 % 2 * (GuiAssets.getUnitSlotButtonStyle().width + 7);
            int n3 = 204 + i4 / 2 * (GuiAssets.getUnitSlotButtonStyle().height + 7);
            widget2 = new Image(new TextureRegion(162, 531, GuiAssets.getProductionButtonStyle().width, GuiAssets.getProductionButtonStyle().height));
            widget2.setX(n);
            widget2.setY((float)n3);
            this.addChild(widget2);
            this.o.add(widget2);
            widget = new Image(new TextureRegion());
            widget.setX(n + 1);
            widget.setY((float)n3);
            this.addChild(widget);
            this.p.add(widget);
            Label label = new Label(GuiAssets.getFontDungeon15Outline());
            label.setX((float)n);
            label.setY((float)(n3 + 26));
            label.setMaxWidth(GuiAssets.getProductionButtonStyle().width - 4);
            label.setAlign(Align.RIGHT);
            this.addChild(label);
            this.q.add(label);
            Image image3 = new Image(new TextureRegion(0, 531, GuiAssets.getProductionButtonStyle().width, GuiAssets.getProductionButtonStyle().height));
            image3.setX(n);
            image3.setY((float)n3);
            this.addChild(image3);
            this.r.add(image3);
            ++i4;
        }
        this.pack();
    }

    @Override
    public void setVisible(boolean bl) {
        if (this.isVisible() != bl) {
            this.s = System.currentTimeMillis();
        }
        super.setVisible(bl);
    }

    public void setUnits(UnitList unitList, int i2) {
        int n;
        UnitList unitList2;
        boolean i3 = false;
        boolean i4 = true;
        boolean i5 = false;
        boolean i6 = false;
        int i7 = 0;
        while (i7 < unitList.size()) {
            Unit unit = (Unit)unitList.get(i7);
            if (unit.getUnitType().canCarryAircraft()) {
                i3 = true;
                if (!unit.isHostedAuto()) {
                    i4 = false;
                }
            }
            unitList2 = unit.getSubUnits();
            n = 0;
            while (n < unitList2.size()) {
                if (this.unitCommander.isCommandable((Unit)unitList2.get(n))) {
                    i5 = true;
                    break;
                }
                ++n;
            }
            if (unit.getUnitGroup() != null) {
                i6 = true;
            }
            ++i7;
        }
        this.a(this.k, i3, i4, false, false, i5, i6);
        i7 = 0;
        while (i7 < domains.length) {
            int n2 = 0;
            int n3 = 0;
            while (n3 < unitList.size()) {
                n2 += ((Unit)unitList.get(n3)).getUnitType().getDamageAgainst(domains[i7], layers[i7]);
                ++n3;
            }
            n3 = (n2 /= unitList.size()) * 5 / 200;
            if (n3 > 5) {
                n3 = 5;
            }
            if (n3 > 0) {
                Image image = this.f[i7];
                image.getRegion().x = 241 + n3 * 13;
                image.setVisible(true);
            } else {
                this.f[i7].setVisible(false);
            }
            n = 0;
            int n4 = 0;
            while (n4 < unitList.size()) {
                n += ((Unit)unitList.get(n4)).getUnitType().getDamageFrom(domains[i7], layers[i7]);
                ++n4;
            }
            n3 = (n /= unitList.size()) * 5 / 200;
            if (n3 > 5) {
                n3 = 5;
            }
            if (n3 > 0) {
                Image image = this.g[i7];
                image.getRegion().x = 241 + n3 * 13;
                image.setVisible(true);
            } else {
                this.g[i7].setVisible(false);
            }
            ++i7;
        }
        i7 = 0;
        int n5 = 0;
        while (n5 < unitList.size()) {
            unitList2 = ((Unit)unitList.get(n5)).getSubUnits();
            n = 0;
            while (n < unitList2.size()) {
                UnitType unitType = ((Unit)unitList2.get(n)).getUnitType();
                int i12 = 0;
                int i13 = 0;
                while (i13 < i7) {
                    if (this.m[i13] == unitType) break;
                    ++i12;
                    ++i13;
                }
                if (i12 < 6) {
                    if (i12 >= i7) {
                        this.m[i12] = unitType;
                        this.n[i12] = 1;
                        i7 = i12 + 1;
                    } else {
                        int n6 = i12;
                        this.n[n6] = this.n[n6] + 1;
                    }
                }
                ++n;
            }
            ++n5;
        }
        n5 = 0;
        while (n5 < 6) {
            if (n5 < i7) {
                ((Image)this.o.get(n5)).setVisible(true);
                ((Image)this.p.get(n5)).setRegion(this.c[this.m[n5].getId()]);
                ((Image)this.p.get(n5)).setVisible(true);
                ((Image)this.p.get(n5)).pack();
                ((Label)this.q.get(n5)).setText(String.valueOf(this.n[n5]));
                ((Label)this.q.get(n5)).setVisible(true);
                ((Image)this.r.get(n5)).setVisible(false);
            } else {
                ((Image)this.o.get(n5)).setVisible(false);
                ((Image)this.p.get(n5)).setVisible(false);
                ((Label)this.q.get(n5)).setVisible(false);
                ((Image)this.r.get(n5)).setVisible(true);
            }
            ++n5;
        }
        ((Button)this.l.get(0)).setEnabled(this.unitCommander.hasCommandableUnit(unitList));
        ((Button)this.l.get(1)).setEnabled(i2 < 0 && this.unitCommander.canAssignSquad(unitList));
        ((Button)this.l.get(2)).setEnabled(this.unitCommander.hasRepairableUnit(unitList));
        ((Button)this.l.get(3)).setEnabled(this.unitCommander.canStopAny(unitList));
        ButtonStyle buttonStyle = ((Button)this.l.get(0)).getStyle();
        buttonStyle.frameX[0] = (System.currentTimeMillis() - this.s) / 400L % 2L == 0L ? 162 : 0;
    }
}


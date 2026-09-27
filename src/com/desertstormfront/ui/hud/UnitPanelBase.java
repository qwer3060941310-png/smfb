/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.hud.SelectionPanelBase;
import com.noblemaster.lib.i18n.Messages;

public abstract class UnitPanelBase
extends SelectionPanelBase {
    protected static final String[] h = new String[]{"Move[i18n]: Move", "Patrol[i18n]: Patrol", "Repair[i18n]: Repair", "Auto[i18n]: Auto", "Stop[i18n]: Stop"};
    protected static final TextureRegion[] i;
    protected static final int j;
    private UnitType k;
    private Image l = new Image(new TextureRegion(0, 0, 78, 48));

    static {
        j = h.length;
        i = new TextureRegion[h.length];
        int i0 = 0;
        while (i0 < h.length) {
            UnitPanelBase.i[i0] = new TextureRegion(579 + i0 * 57, 577, 56, 26);
            ++i0;
        }
    }

    protected UnitPanelBase(UnitCommander unitCommander, TextureRegion[] textureRegionArray) {
        super(unitCommander, textureRegionArray);
        this.l.setX(6.0f);
        this.l.setY(33.0f);
        this.addChild(this.l);
        this.k = null;
    }

    protected void setUnit(Unit unit) {
        UnitType unitType = unit.getUnitType();
        boolean i3 = false;
        UnitList unitList = unit.getSubUnits();
        int i5 = 0;
        while (i5 < unitList.size()) {
            if (this.unitCommander.isCommandable((Unit)unitList.get(i5))) {
                i3 = true;
                break;
            }
            ++i5;
        }
        i5 = GameConfig.isDarkTheme() ? 1 : 0;
        this.a(i5 != 0 ? Messages.get(unitType.getName()) : Messages.getFallback(unitType.getName()), unitType.canCarryAircraft(), unit.isHostedAuto(), unit.isImmobile(), unit.hasMoveTarget(), i3, unit.getUnitGroup() != null);
        if (this.k != unitType) {
            if (unitType.isImmobile()) {
                this.l.setVisible(false);
            } else {
                this.l.setRegion(this.d[unitType.getId()]);
                this.l.setVisible(true);
            }
            if (unitType.isImmobile()) {
                int i6 = 0;
                while (i6 < Domain.values().length) {
                    this.f[i6].setVisible(false);
                    this.g[i6].setVisible(false);
                    ++i6;
                }
            } else {
                int i6 = 0;
                while (i6 < domains.length) {
                    int i7 = unitType.getDamageAgainst(domains[i6], layers[i6]);
                    int i8 = i7 * 5 / 200;
                    if (i8 > 5) {
                        i8 = 5;
                    }
                    if (i8 > 0) {
                        Image image = this.f[i6];
                        image.getRegion().x = 241 + i8 * 13;
                        image.setVisible(true);
                    } else {
                        this.f[i6].setVisible(false);
                    }
                    int n = unitType.getDamageFrom(domains[i6], layers[i6]);
                    i8 = n * 5 / 200;
                    if (i8 > 5) {
                        i8 = 5;
                    }
                    if (i8 > 0) {
                        Image image = this.g[i6];
                        image.getRegion().x = 241 + i8 * 13;
                        image.setVisible(true);
                    } else {
                        this.g[i6].setVisible(false);
                    }
                    ++i6;
                }
            }
            this.k = unitType;
        }
    }
}


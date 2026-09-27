/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitOrderMode;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.hud.GuiAssets;
import com.desertstormfront.ui.hud.UnitPanelBase;
import com.noblemaster.lib.i18n.Messages;
import java.util.ArrayList;
import java.util.List;

public final class SingleUnitPanel
extends UnitPanelBase {
    private List k;
    private long l;

    public SingleUnitPanel(UnitCommander unitCommander) {
        super(unitCommander, new TextureRegion[]{new TextureRegion(511, 0, 224, 198), new TextureRegion(772, 669, 194, 17)});
        boolean i2 = GameConfig.isDarkTheme();
        this.k = new ArrayList();
        int i3 = 0;
        while (i3 < h.length) {
            Container container = new Container();
            Image image = new Image(i[i3]);
            image.setX(11.0f);
            image.setY(4.0f);
            container.addChild(image);
            Label label = i2 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
            label.setX(0.0f);
            label.setY((float)(29 + (i2 ? 4 : 0)));
            label.a(-268435456);
            label.setText(i2 ? Messages.get(h[i3]) : Messages.getFallback(h[i3]));
            label.setMaxWidth(GuiAssets.getUnitSlotButtonStyle().width);
            label.setAlign(Align.CENTER);
            container.addChild(label);
            Button button = new Button(GuiAssets.getUnitSlotButtonStyle().copy(), container);
            button.setData(i3);
            int i8 = i3 < 3 ? i3 : i3 - 1;
            int i9 = 4 + i8 % 2 * (GuiAssets.getUnitSlotButtonStyle().width + 7);
            int i10 = 88 + i8 / 2 * (GuiAssets.getUnitSlotButtonStyle().height + 8);
            button.setX(i9);
            button.setY((float)i10);
            this.addChild(button);
            this.k.add(button);
            ++i3;
        }
        this.pack();
    }

    @Override
    public void setVisible(boolean bl) {
        if (this.isVisible() != bl) {
            this.l = System.currentTimeMillis();
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
        buttonStyle.frameX[0] = (System.currentTimeMillis() - this.l) / 400L % 2L == 0L ? 162 : 0;
    }
}


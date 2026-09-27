/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.config.TouchDeviceFlags;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.player.UnitGroup;
import com.desertstormfront.game.player.UnitGroupSet;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.hud.GuiAssets;

public final class GroupBar
extends Container {
    private final ButtonStyle slotButtonStyle;
    private final ButtonStyle[] b;
    private Button toggleButton;
    private Image toggleIcon;
    private Button[] slotButtons;
    private TextureRegion[] slotRegions;
    private Image[] slotSelectionImages;
    private Label[] slotLabels;
    private int[] slotCounts;
    private UnitCommander unitCommander;
    private boolean expanded;
    private int selectedGroup;

    public GroupBar(UnitCommander unitCommander) {
        Image image;
        int[] nArray = new int[4];
        nArray[1] = 71;
        nArray[2] = 142;
        nArray[3] = -1;
        this.slotButtonStyle = new ButtonStyle(new int[]{452, 452, 452, -1}, nArray, 58, 70);
        ButtonStyle[] buttonStyleArray = new ButtonStyle[3];
        int[] nArray2 = new int[4];
        nArray2[1] = 71;
        nArray2[2] = 142;
        nArray2[3] = -1;
        buttonStyleArray[0] = new ButtonStyle(new int[]{275, 275, 275, -1}, nArray2, 58, 70);
        int[] nArray3 = new int[4];
        nArray3[1] = 71;
        nArray3[2] = 142;
        nArray3[3] = -1;
        buttonStyleArray[1] = new ButtonStyle(new int[]{334, 334, 334, -1}, nArray3, 58, 70);
        int[] nArray4 = new int[4];
        nArray4[1] = 71;
        nArray4[2] = 142;
        nArray4[3] = -1;
        buttonStyleArray[2] = new ButtonStyle(new int[]{393, 393, 393, -1}, nArray4, 58, 70);
        this.b = buttonStyleArray;
        this.unitCommander = unitCommander;
        this.expanded = false;
        this.selectedGroup = -1;
        this.toggleButton = new Button(new ButtonStyle(new int[]{625, 625, 625, -1}, new int[]{604, 669, 734, -1}, 64, 64));
        this.toggleButton.setData(-1);
        this.toggleButton.setX(0.0f);
        this.toggleButton.setY(8.0f);
        this.toggleIcon = image = new Image(new TextureRegion(711, 0, 58, 58));
        image.setX(3.0f);
        image.setY(3.0f);
        image.setVisible(false);
        Container container = new Container();
        container.addChild(image);
        this.toggleButton.setLabel(container);
        this.addChild(this.toggleButton);
        this.slotButtons = new Button[8];
        this.slotRegions = new TextureRegion[this.slotButtons.length];
        this.slotSelectionImages = new Image[this.slotButtons.length];
        this.slotLabels = new Label[this.slotButtons.length];
        this.slotCounts = new int[this.slotButtons.length];
        int i4 = 0;
        while (i4 < this.slotButtons.length) {
            Label label;
            Image image2;
            Button button = new Button(this.slotButtonStyle);
            button.setData(i4);
            button.setX(63 + i4 * 60);
            button.setY(11.0f);
            Container container2 = new Container();
            this.slotSelectionImages[i4] = image2 = new Image(new TextureRegion(711, 0, 58, 58));
            image2.setX(0.0f);
            image2.setY(0.0f);
            image2.setVisible(false);
            container2.addChild(image2);
            Image image3 = new Image(new TextureRegion(0, 400, 32, 32));
            this.slotRegions[i4] = image3.getRegion();
            image3.setX(13.0f);
            image3.setY(12.0f);
            container2.addChild(image3);
            this.slotLabels[i4] = label = new Label(GuiAssets.getFontDungeon15Outline());
            this.slotCounts[i4] = -1;
            label.setX(0.0f);
            label.setY(31.0f);
            label.setMaxWidth(50);
            label.setAlign(Align.RIGHT);
            label.a(-65792);
            container2.addChild(label);
            button.setLabel(container2);
            this.addChild(button);
            this.slotButtons[i4] = button;
            ++i4;
        }
        this.pack();
    }

    public void collapse() {
        this.expanded = false;
        this.toggleButton.setY(8.0f);
        this.toggleIcon.setVisible(false);
        this.selectGroup(-1);
    }

    public void expand() {
        this.expanded = true;
        this.toggleButton.setY(0.0f);
        this.toggleIcon.setVisible(true);
    }

    public void selectGroup(int i1) {
        int i2 = 0;
        while (i2 < this.slotButtons.length) {
            Button button = this.slotButtons[i2];
            if (i2 == i1) {
                button.setY(3.0f);
                this.slotSelectionImages[i2].setVisible(true);
            } else {
                button.setY(11.0f);
                this.slotSelectionImages[i2].setVisible(false);
            }
            ++i2;
        }
        this.selectedGroup = i1;
    }

    public int getSelectedGroup() {
        return this.selectedGroup;
    }

    public void refresh() {
        this.toggleButton.setVisible(!TouchDeviceFlags.isTouchDevice() || UserConfig.isTouchScroll());
        UnitGroupSet unitGroupSet = this.unitCommander.getPlayer().getUnitGroups();
        int i2 = 0;
        while (i2 < this.slotButtons.length) {
            UnitGroup unitGroup = unitGroupSet.get(i2);
            int i4 = unitGroup.getUnits().size();
            TextureRegion textureRegion = this.slotRegions[i2];
            if (i4 == 0) {
                textureRegion.x = 0;
                this.slotButtons[i2].setStyle(this.slotButtonStyle);
                this.slotLabels[i2].setVisible(false);
            } else {
                textureRegion.x = 33 * (unitGroup.getId() + 1);
                Domain domain = ((Unit)unitGroup.getUnits().get(0)).getUnitType().getDomain();
                int i7 = domain == Domain.Amphibian ? 1 : domain.ordinal();
                this.slotButtons[i2].setStyle(this.b[i7]);
                if (this.slotCounts[i2] != i4) {
                    this.slotCounts[i2] = i4;
                    this.slotLabels[i2].setText(String.valueOf(i4));
                }
                this.slotLabels[i2].setVisible(true);
            }
            ++i2;
        }
        if (this.expanded) {
            this.toggleIcon.getRegion().y = (int)(604L + 59L * (System.currentTimeMillis() / 100L % 4L));
        } else if (this.selectedGroup >= 0) {
            this.slotSelectionImages[this.selectedGroup].getRegion().y = (int)(604L + 59L * (System.currentTimeMillis() / 100L % 4L));
        }
    }
}


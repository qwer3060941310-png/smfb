/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.audio.AudioClip;
import com.desertstormfront.audio.MusicPlaylist;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.TouchDeviceFlags;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.screen.BaseMenuScreen;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.MainMenuScreen;
import com.desertstormfront.ui.Checkbox;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.KeyCode;
import com.desertstormfront.ui.ListBox;
import com.desertstormfront.ui.NinePatchImage;
import com.desertstormfront.ui.ScrollPane;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import com.noblemaster.lib.data.Language;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.log.OsfLog;

/**
 * Settings dialog. Builds one {@link ListBox} per option (language, volumes, unit speed, scroll
 * speed, multiplayer port and the touch-device hot keys), all stacked inside a single scrolling
 * {@link Container}. Each list box carries its option id through {@code setData(id)}; the same id
 * is the case label in {@link #handleWidgetAction(int, Object)} which dispatches to the matching
 * {@link UserConfig} setter. The selected values are read straight from {@link UserConfig} on
 * construction, so the dialog always reflects the persisted configuration.
 */
public final class OptionsScreen
extends BaseMenuScreen {
    private NinePatchImage background;
    private ScrollPane scrollPane;
    private Container contentContainer;
    private ListBox languageList;
    private Language[] languages;
    private ListBox soundVolumeList;
    private ListBox musicVolumeList;
    private ListBox unitSpeedList;
    private ListBox portList;
    private ListBox scrollSpeedList;
    private ListBox stopKeyList;
    private ListBox repairKeyList;
    private ListBox patrolKeyList;
    private ListBox groupKeyList;
    private ListBox ungroupKeyList;
    private ListBox scrollKeyLeftList;
    private ListBox scrollKeyRightList;
    private ListBox scrollKeyUpList;
    private ListBox scrollKeyDownList;
    private float contentHeight;
    private Language selectedLanguage;

    public OptionsScreen(BaseMenuScreen baseMenuScreen) {
        super(baseMenuScreen);
    }

    @Override
    protected boolean isBackEnabled(Container container) {
        Object object;
        Object object2;
        this.background = new NinePatchImage(GuiAssets.getBackgroundNinePatch());
        container.addChild(this.background);
        this.scrollPane = new ScrollPane(GuiAssets.getScrollBarStyle());
        container.addChild(this.scrollPane);
        this.contentContainer = new Container();
        this.scrollPane.setContent(this.contentContainer);
        int i2 = 5;
        this.selectedLanguage = UserConfig.getLanguage();
        int i3 = 0;
        Language[] languageArray = Messages.getAvailableLanguages();
        this.languages = new Language[languageArray.length + 1];
        this.languages[0] = null;
        String[] stringArray = new String[languageArray.length + 1];
        stringArray[0] = Messages.get("LanguageDefault[i18n]: Language: Default");
        int n = 0;
        while (n < languageArray.length) {
            stringArray[n + 1] = languageArray[n].getDisplayName();
            this.languages[n + 1] = languageArray[n];
            if (this.selectedLanguage != null && this.selectedLanguage.equals(languageArray[n])) {
                i3 = n + 1;
            }
            ++n;
        }
        this.languageList = this.createListBox(1, 5, i2);
        this.languageList.setItems(stringArray);
        this.contentContainer.addChild(this.languageList);
        this.languageList.setSelectedIndex(i3);
        this.soundVolumeList = this.createListBox(2, 5, i2 += 60);
        this.soundVolumeList.setItems(new String[]{Messages.get("SoundFXOff[i18n]: Sound FX: Off"), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 10), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 20), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 30), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 40), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 50), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 60), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 70), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 80), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 90), Messages.format("SoundFXPercentX[i18n]: Sound FX: {0}%", 100)});
        this.contentContainer.addChild(this.soundVolumeList);
        this.soundVolumeList.setSelectedIndex(Math.round(UserConfig.getAudioVolume() * 10.0f));
        this.musicVolumeList = this.createListBox(3, 5, i2 += 60);
        this.musicVolumeList.setItems(new String[]{Messages.get("MusicOff[i18n]: Music: Off"), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 10), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 20), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 30), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 40), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 50), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 60), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 70), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 80), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 90), Messages.format("MusicPercentX[i18n]: Music FX: {0}%", 100)});
        this.contentContainer.addChild(this.musicVolumeList);
        this.musicVolumeList.setSelectedIndex(Math.round(UserConfig.getMusicVolume() * 10.0f));
        this.unitSpeedList = this.createListBox(4, 5, i2 += 60);
        this.unitSpeedList.setItems(new String[]{Messages.get("UnitSpeedsNormal[i18n]: Unit Speeds: Normal"), Messages.format("UnitSpeedsX[i18n]: Unit Speeds: {0}x", "1.5")});
        this.contentContainer.addChild(this.unitSpeedList);
        this.unitSpeedList.setSelectedIndex(UserConfig.getWorldSpeedFactor() == 1.0f ? 0 : 1);
        Checkbox checkbox = this.createCheckbox(5, 5, i2 += 60);
        checkbox.setChecked(UserConfig.isRenderDetails());
        this.contentContainer.addChild(checkbox);
        this.contentContainer.addChild(this.createLabel(65, i2 + 15, Messages.get("RenderDetails[i18n]: Render Details")));
        i2 += 60;
        if (TouchDeviceFlags.isTouchDevice()) {
            object2 = this.createCheckbox(6, 5, i2);
            ((Checkbox)object2).setChecked(UserConfig.isTouchScroll());
            this.contentContainer.addChild((Widget)object2);
            this.contentContainer.addChild(this.createLabel(65, i2 + 15, Messages.get("TouchScrolling[i18n]: Touch Scrolling")));
            Checkbox checkbox2 = this.createCheckbox(7, 5, i2 += 60);
            checkbox2.setChecked(UserConfig.isMouseControl());
            this.contentContainer.addChild(checkbox2);
            this.contentContainer.addChild(this.createLabel(65, i2 + 15, Messages.get("MouseRTSModeETC[i18n]: Mouse: L-select / R-target (RTS Mode)")));
            i2 += 60;
        }
        String[] portNames = new String[100];
        int n2 = 0;
        while (n2 < portNames.length) {
            portNames[n2] = Messages.format("MultiplayerPort[i18n]: Multiplayer Port: {0}", String.valueOf(2300 + n2));
            ++n2;
        }
        this.portList = this.createListBox(8, 5, i2);
        this.portList.setItems(portNames);
        this.contentContainer.addChild(this.portList);
        n2 = UserConfig.getMultiplayerPort() - 2300;
        if (n2 < 0 || n2 >= portNames.length) {
            n2 = 0;
        }
        this.portList.setSelectedIndex(n2);
        i2 += 60;
        if (TouchDeviceFlags.isTouchDevice()) {
            String[] scrollLabels = new String[50];
            int n3 = 0;
            while (n3 < scrollLabels.length) {
                scrollLabels[n3] = Messages.format("ScrollSpeed[i18n]: Scroll Speed: {0}px/s", String.valueOf((n3 + 1) * 100));
                ++n3;
            }
            this.scrollSpeedList = this.createListBox(9, 5, i2);
            this.scrollSpeedList.setItems(scrollLabels);
            this.contentContainer.addChild(this.scrollSpeedList);
            this.scrollSpeedList.setSelectedIndex(UserConfig.getScrollSpeed() / 100 - 1);
            this.stopKeyList = this.createListBox(50, 5, i2 += 60);
            String[] stringArray2 = new String[KeyCode.values().length];
            int i11 = 0;
            int n4 = 0;
            while (n4 < KeyCode.values().length) {
                stringArray2[n4] = Messages.format("UnitStopKey[i18n]: Unit [Stop] Key: {0}", KeyCode.values()[n4].getLabel());
                if (UserConfig.getActionKeyStop() == KeyCode.values()[n4].getKeyCode()) {
                    i11 = n4;
                }
                ++n4;
            }
            this.stopKeyList.setItems(stringArray2);
            this.contentContainer.addChild(this.stopKeyList);
            this.stopKeyList.setSelectedIndex(i11);
            this.repairKeyList = this.createListBox(51, 5, i2 += 60);
            String[] stringArray3 = new String[KeyCode.values().length];
            int i13 = 0;
            int n5 = 0;
            while (n5 < KeyCode.values().length) {
                stringArray3[n5] = Messages.format("UnitRepairKey[i18n]: Unit [Repair] Key: {0}", KeyCode.values()[n5].getLabel());
                if (UserConfig.getActionKeyRepair() == KeyCode.values()[n5].getKeyCode()) {
                    i13 = n5;
                }
                ++n5;
            }
            this.repairKeyList.setItems(stringArray3);
            this.contentContainer.addChild(this.repairKeyList);
            this.repairKeyList.setSelectedIndex(i13);
            this.patrolKeyList = this.createListBox(52, 5, i2 += 60);
            String[] stringArray4 = new String[KeyCode.values().length];
            int i15 = 0;
            int n6 = 0;
            while (n6 < KeyCode.values().length) {
                stringArray4[n6] = Messages.format("UnitPatrolKey[i18n]: Unit [Patrol] Key: {0}", KeyCode.values()[n6].getLabel());
                if (UserConfig.getActionKeyPatrol() == KeyCode.values()[n6].getKeyCode()) {
                    i15 = n6;
                }
                ++n6;
            }
            this.patrolKeyList.setItems(stringArray4);
            this.contentContainer.addChild(this.patrolKeyList);
            this.patrolKeyList.setSelectedIndex(i15);
            this.groupKeyList = this.createListBox(53, 5, i2 += 60);
            String[] stringArray5 = new String[KeyCode.values().length];
            int i17 = 0;
            int n7 = 0;
            while (n7 < KeyCode.values().length) {
                stringArray5[n7] = Messages.format("UnitGroupKey[i18n]: Unit [Group] Key: {0}", KeyCode.values()[n7].getLabel());
                if (UserConfig.getActionKeyGroup() == KeyCode.values()[n7].getKeyCode()) {
                    i17 = n7;
                }
                ++n7;
            }
            this.groupKeyList.setItems(stringArray5);
            this.contentContainer.addChild(this.groupKeyList);
            this.groupKeyList.setSelectedIndex(i17);
            this.ungroupKeyList = this.createListBox(54, 5, i2 += 60);
            String[] stringArray6 = new String[KeyCode.values().length];
            int i19 = 0;
            int n8 = 0;
            while (n8 < KeyCode.values().length) {
                stringArray6[n8] = Messages.format("UnitUnGroupKey[i18n]: Unit [Split] Key (un-Group): {0}", KeyCode.values()[n8].getLabel());
                if (UserConfig.getActionKeyUngroup() == KeyCode.values()[n8].getKeyCode()) {
                    i19 = n8;
                }
                ++n8;
            }
            this.ungroupKeyList.setItems(stringArray6);
            this.contentContainer.addChild(this.ungroupKeyList);
            this.ungroupKeyList.setSelectedIndex(i19);
            this.scrollKeyLeftList = this.createListBox(100, 5, i2 += 60);
            String[] stringArray7 = new String[KeyCode.values().length];
            int i21 = 0;
            int n9 = 0;
            while (n9 < KeyCode.values().length) {
                stringArray7[n9] = Messages.format("ScrollKeyXisY[i18n]: Scroll Key {0}: {1}", Messages.get("Left[i18n]: Left"), KeyCode.values()[n9].getLabel());
                if (UserConfig.getScrollKeyLeft() == KeyCode.values()[n9].getKeyCode()) {
                    i21 = n9;
                }
                ++n9;
            }
            this.scrollKeyLeftList.setItems(stringArray7);
            this.contentContainer.addChild(this.scrollKeyLeftList);
            this.scrollKeyLeftList.setSelectedIndex(i21);
            this.scrollKeyRightList = this.createListBox(101, 5, i2 += 60);
            String[] stringArray8 = new String[KeyCode.values().length];
            i21 = 0;
            int n10 = 0;
            while (n10 < KeyCode.values().length) {
                stringArray8[n10] = Messages.format("ScrollKeyXisY[i18n]: Scroll Key {0}: {1}", Messages.get("Right[i18n]: Right"), KeyCode.values()[n10].getLabel());
                if (UserConfig.getScrollKeyRight() == KeyCode.values()[n10].getKeyCode()) {
                    i21 = n10;
                }
                ++n10;
            }
            this.scrollKeyRightList.setItems(stringArray8);
            this.contentContainer.addChild(this.scrollKeyRightList);
            this.scrollKeyRightList.setSelectedIndex(i21);
            this.scrollKeyUpList = this.createListBox(102, 5, i2 += 60);
            String[] stringArray9 = new String[KeyCode.values().length];
            i21 = 0;
            int n11 = 0;
            while (n11 < KeyCode.values().length) {
                stringArray9[n11] = Messages.format("ScrollKeyXisY[i18n]: Scroll Key {0}: {1}", Messages.get("Up[i18n]: Up"), KeyCode.values()[n11].getLabel());
                if (UserConfig.getScrollKeyUp() == KeyCode.values()[n11].getKeyCode()) {
                    i21 = n11;
                }
                ++n11;
            }
            this.scrollKeyUpList.setItems(stringArray9);
            this.contentContainer.addChild(this.scrollKeyUpList);
            this.scrollKeyUpList.setSelectedIndex(i21);
            this.scrollKeyDownList = this.createListBox(103, 5, i2 += 60);
            String[] stringArray10 = new String[KeyCode.values().length];
            i21 = 0;
            int i25 = 0;
            while (i25 < KeyCode.values().length) {
                stringArray10[i25] = Messages.format("ScrollKeyXisY[i18n]: Scroll Key {0}: {1}", Messages.get("Down[i18n]: Down"), KeyCode.values()[i25].getLabel());
                if (UserConfig.getScrollKeyDown() == KeyCode.values()[i25].getKeyCode()) {
                    i21 = i25;
                }
                ++i25;
            }
            this.scrollKeyDownList.setItems(stringArray10);
            this.contentContainer.addChild(this.scrollKeyDownList);
            this.scrollKeyDownList.setSelectedIndex(i21);
            i2 += 60;
        }
        if (GameConfig.isDebugEnabled()) {
            object = this.createCheckbox(10000, 5, i2);
            ((Checkbox)object).setChecked(UserConfig.isDisplayAi());
            this.contentContainer.addChild((Widget)object);
            this.contentContainer.addChild(this.createLabel(65, i2 + 15, Messages.get("DisplayAI[i18n]: Display AI")));
            i2 += 60;
        }
        this.contentHeight = i2;
        this.showWebsiteAndForumButtons();
        return true;
    }

    @Override
    protected void createContent(Container container) {
        this.setupScrollPane(container, this.background, this.scrollPane);
        float f2 = this.scrollPane.getWidth();
        if (this.contentHeight >= this.scrollPane.getHeight()) {
            f2 -= (float)this.scrollPane.getScrollBarWidth();
        }
        this.languageList.setLabelMaxWidth(Math.round(f2 - (float)this.languageList.getArrowWidth() - 10.0f));
        this.soundVolumeList.setLabelMaxWidth(Math.round(f2 - (float)this.soundVolumeList.getArrowWidth() - 10.0f));
        this.musicVolumeList.setLabelMaxWidth(Math.round(f2 - (float)this.musicVolumeList.getArrowWidth() - 10.0f));
        this.unitSpeedList.setLabelMaxWidth(Math.round(f2 - (float)this.unitSpeedList.getArrowWidth() - 10.0f));
        this.portList.setLabelMaxWidth(Math.round(f2 - (float)this.portList.getArrowWidth() - 10.0f));
        if (this.scrollSpeedList != null) {
            this.scrollSpeedList.setLabelMaxWidth(Math.round(f2 - (float)this.scrollSpeedList.getArrowWidth() - 10.0f));
            this.stopKeyList.setLabelMaxWidth(Math.round(f2 - (float)this.stopKeyList.getArrowWidth() - 10.0f));
            this.repairKeyList.setLabelMaxWidth(Math.round(f2 - (float)this.repairKeyList.getArrowWidth() - 10.0f));
            this.patrolKeyList.setLabelMaxWidth(Math.round(f2 - (float)this.patrolKeyList.getArrowWidth() - 10.0f));
            this.groupKeyList.setLabelMaxWidth(Math.round(f2 - (float)this.groupKeyList.getArrowWidth() - 10.0f));
            this.ungroupKeyList.setLabelMaxWidth(Math.round(f2 - (float)this.ungroupKeyList.getArrowWidth() - 10.0f));
            this.scrollKeyLeftList.setLabelMaxWidth(Math.round(f2 - (float)this.scrollKeyLeftList.getArrowWidth() - 10.0f));
            this.scrollKeyRightList.setLabelMaxWidth(Math.round(f2 - (float)this.scrollKeyRightList.getArrowWidth() - 10.0f));
            this.scrollKeyUpList.setLabelMaxWidth(Math.round(f2 - (float)this.scrollKeyUpList.getArrowWidth() - 10.0f));
            this.scrollKeyDownList.setLabelMaxWidth(Math.round(f2 - (float)this.scrollKeyDownList.getArrowWidth() - 10.0f));
        }
        container.pack();
    }

    /**
     * Dispatches a selection from any option list box. The list box id stored via
     * {@code setData(id)} (see {@link #createListBox(int, int, int, String[])}) is the switch key;
     * each case writes the new value back to {@link UserConfig} (or toggles a
     * {@link TouchDeviceFlags} bit for the hot-key rows). Returning a {@link MainMenuScreen}
     * rebuilds the caller when the language changed, otherwise the same screen is kept open.
     */
    @Override
    protected BaseScreen handleWidgetAction(Widget widget) {
        int i2 = (Integer)widget.getData();
        switch (i2) {
            case -2: {
                if (Messages.getLanguage() == Language.SPANISH) {
                    this.openUrl("http://www.darkgame.es/foro/viewforum.php?f=44");
                } else {
                    this.openUrl(GameConfig.getForumUrl());
                }
                return null;
            }
            case -1: {
                if (Messages.getLanguage() == Language.SPANISH) {
                    this.openUrl("http://darkgame.es/");
                } else {
                    this.openUrl(GameConfig.getWebsiteUrl());
                }
                return null;
            }
            case 0: {
                Language language = UserConfig.getLanguage();
                if (this.selectedLanguage == null && language != null || this.selectedLanguage != null && language == null || this.selectedLanguage != null && !this.selectedLanguage.equals(language)) {
                    Messages.load(language != null ? language : Language.fromCode(this.getLanguage()));
                    return new MainMenuScreen();
                }
                return new MainMenuScreen(this);
            }
            case 1: {
                UserConfig.setLanguage(this.languages[((ListBox)widget).getSelectedIndex()]);
                return null;
            }
            case 2: {
                UserConfig.setAudioVolume((float)((ListBox)widget).getSelectedIndex() * 0.1f);
                AudioClip.setMasterVolume(UserConfig.getAudioVolume());
                return null;
            }
            case 3: {
                UserConfig.setMusicVolume((float)((ListBox)widget).getSelectedIndex() * 0.1f);
                MusicPlaylist.setMasterVolume(UserConfig.getMusicVolume());
                return null;
            }
            case 4: {
                UserConfig.setWorldSpeedFactor(((ListBox)widget).getSelectedIndex() == 0 ? 1.0f : 1.5f);
                return null;
            }
            case 5: {
                UserConfig.setRenderDetails(!UserConfig.isRenderDetails());
                return null;
            }
            case 6: {
                UserConfig.setTouchScroll(!UserConfig.isTouchScroll());
                return null;
            }
            case 7: {
                UserConfig.setMouseControl(!UserConfig.isMouseControl());
                return null;
            }
            case 8: {
                UserConfig.setMultiplayerPort(((ListBox)widget).getSelectedIndex() + 2300);
                return null;
            }
            case 9: {
                UserConfig.setScrollSpeed((((ListBox)widget).getSelectedIndex() + 1) * 100);
                return null;
            }
            case 50: {
                UserConfig.setActionKeyStop(KeyCode.values()[((ListBox)widget).getSelectedIndex()].getKeyCode());
                return null;
            }
            case 51: {
                UserConfig.setActionKeyRepair(KeyCode.values()[((ListBox)widget).getSelectedIndex()].getKeyCode());
                return null;
            }
            case 52: {
                UserConfig.setActionKeyPatrol(KeyCode.values()[((ListBox)widget).getSelectedIndex()].getKeyCode());
                return null;
            }
            case 53: {
                UserConfig.setActionKeyGroup(KeyCode.values()[((ListBox)widget).getSelectedIndex()].getKeyCode());
                return null;
            }
            case 54: {
                UserConfig.setActionKeyUngroup(KeyCode.values()[((ListBox)widget).getSelectedIndex()].getKeyCode());
                return null;
            }
            case 100: {
                UserConfig.setScrollKeyLeft(KeyCode.values()[((ListBox)widget).getSelectedIndex()].getKeyCode());
                return null;
            }
            case 101: {
                UserConfig.setScrollKeyRight(KeyCode.values()[((ListBox)widget).getSelectedIndex()].getKeyCode());
                return null;
            }
            case 102: {
                UserConfig.setScrollKeyUp(KeyCode.values()[((ListBox)widget).getSelectedIndex()].getKeyCode());
                return null;
            }
            case 103: {
                UserConfig.setScrollKeyDown(KeyCode.values()[((ListBox)widget).getSelectedIndex()].getKeyCode());
                return null;
            }
            case 10000: {
                UserConfig.setDisplayAi(!UserConfig.isDisplayAi());
                return null;
            }
        }
        OsfLog.error("Action not implemented: " + i2);
        return null;
    }

    @Override
    protected BaseScreen handleAction(int i1, int i2) {
        return null;
    }
}


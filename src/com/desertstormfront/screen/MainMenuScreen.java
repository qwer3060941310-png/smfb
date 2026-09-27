/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.desertstormfront.app.support.TextFormatter;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.TouchDeviceFlags;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.game.player.PlayerStatistics;
import com.desertstormfront.screen.BaseMenuScreen;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.CampaignScreen;
import com.desertstormfront.screen.CreditsScreen;
import com.desertstormfront.screen.LoadGameScreen;
import com.desertstormfront.screen.ManualScreen;
import com.desertstormfront.screen.MultiplayerLobbyScreen;
import com.desertstormfront.screen.OptionsScreen;
import com.desertstormfront.screen.SkirmishSetupScreen;
import com.desertstormfront.session.impl.CampaignGameMode;
import com.desertstormfront.session.impl.TestScenarioMode;
import com.desertstormfront.session.impl.TutorialGameMode;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Checkbox;
import com.desertstormfront.ui.CheckboxStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.NinePatchImage;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.log.OsfLog;

public final class MainMenuScreen
extends BaseMenuScreen {
    private Image manualButtonImage;
    private Button manualButton;
    private Image creditsButtonImage;
    private Button creditsButton;
    private NinePatchImage fullScreenRowPanel;
    private Label fullScreenLabel;
    private Image fullScreenButtonImage;
    private Checkbox fullScreenCheckbox;
    private Image menuDividerLine;
    private Image menuDividerEnd;
    private Container[] menuItemContainers;
    private NinePatchImage statisticsPanel;
    private Label statisticsTitleLabel;
    private Image[] menuItemImages;
    private Label campaignHighScoreCaption;
    private Label campaignHighScoreValue;
    private Label hoursPlayedCaption;
    private Label hoursPlayedValue;
    private Label unitsCaption;
    private Label unitsDestroyedValue;
    private Label unitsLostValue;
    private Label damageCaption;
    private Label damageInflictedValue;
    private Label damageReceivedValue;
    private Label basesCaption;
    private Label basesCapturedValue;
    private Label basesLostValue;

    public MainMenuScreen() {
    }

    public MainMenuScreen(BaseMenuScreen baseMenuScreen) {
        super(baseMenuScreen);
    }

    @Override
    public BaseScreen update() {
        BaseScreen baseScreen = super.update();
        if (baseScreen == null) {
            if (Gdx.input.isKeyPressed(48)) {
                TestScenarioMode testScenarioMode = GameConfig.getTestScenarioGameMode();
                testScenarioMode.createStartWorld();
                return new LoadGameScreen(testScenarioMode);
            }
            return null;
        }
        return baseScreen;
    }

    @Override
    protected boolean isBackEnabled(Container container) {
        String[] stringArray;
        Object object;
        Object object2;
        Object object3;
        boolean i2 = GameConfig.isDarkTheme();
        this.manualButtonImage = new Image(new TextureRegion(0, 304, 83, 71));
        container.addChild(this.manualButtonImage);
        this.manualButton = new Button(new ButtonStyle(new int[]{229, 229, 229, 229}, new int[]{33, 79, 125, 171}, 45, 45));
        this.manualButton.setData(100);
        container.addChild(this.manualButton);
        this.creditsButtonImage = new Image(new TextureRegion(0, 304, 83, 71));
        container.addChild(this.creditsButtonImage);
        this.creditsButton = new Button(new ButtonStyle(new int[]{579, 579, 579, 579}, new int[]{626, 672, 718, 764}, 45, 45));
        this.creditsButton.setData(101);
        container.addChild(this.creditsButton);
        if (TouchDeviceFlags.isTouchDevice() && Gdx.graphics.supportsDisplayModeChange()) {
            this.fullScreenButtonImage = new Image(new TextureRegion(0, 304, 83, 71));
            container.addChild(this.fullScreenButtonImage);
            this.fullScreenCheckbox = new Checkbox(new CheckboxStyle(new int[]{457, 457, 457, 457, 503, 503, 503, 503}, new int[]{396, 442, 488, 534, 396, 442, 488, 534}, 45, 45));
            this.fullScreenCheckbox.setData(102);
            this.fullScreenCheckbox.setChecked(UserConfig.isFullScreen());
            container.addChild(this.fullScreenCheckbox);
            this.fullScreenLabel = i2 ? new Label(GuiAssets.getDefaultFont(), false) : new Label(GuiAssets.getFontDokchampa15());
            this.fullScreenLabel.a(-14671840);
            this.fullScreenLabel.setText(i2 ? Messages.get("FullScreen[i18n]: Full Screen") : Messages.getFallback("FullScreen[i18n]: Full Screen"));
            this.fullScreenLabel.pack();
            this.fullScreenRowPanel = new NinePatchImage(GuiAssets.getOptionRowNinePatch());
            this.fullScreenRowPanel.setWidth(this.fullScreenLabel.getWidth() + 26.0f);
            this.fullScreenRowPanel.setHeight(39.0f);
            container.addChild(this.fullScreenRowPanel);
            container.addChild(this.fullScreenLabel);
        }
        this.menuDividerLine = new Image(new TextureRegion(162, 233, 57, 14));
        container.addChild(this.menuDividerLine);
        this.menuDividerEnd = new Image(new TextureRegion(162, 249, 57, 16));
        container.addChild(this.menuDividerEnd);
        String[] stringArray2 = new String[]{Messages.get("Tutorial[i18n]: Tutorial"), Messages.get("Campaign[i18n]: Campaign"), Messages.get("Skirmish[i18n]: Skirmish"), Messages.get("Multiplayer[i18n]: Multiplayer"), Messages.get("Options[i18n]: Options")};
        ButtonStyle buttonStyle = new ButtonStyle(new int[]{392, 392, 392, 392}, new int[]{580, 672, 718, 764}, 186, 45);
        ButtonStyle buttonStyle2 = new ButtonStyle(new int[]{392, 392, 392, 392}, new int[]{626, 672, 718, 764}, 186, 45);
        this.menuItemContainers = new Container[stringArray2.length];
        int n = 0;
        while (n < stringArray2.length) {
            Container container2 = new Container();
            container2.setX(300.0f);
            container2.setY((float)(n * 75));
            container.addChild(container2);
            this.menuItemContainers[n] = container2;
            Image image = new Image(new TextureRegion(0, 160, 223, 71));
            container2.addChild(image);
            object3 = new Container();
            object2 = new Label(GuiAssets.getDefaultFont());
            ((Widget)object2).setX(1.0f);
            ((Widget)object2).setY(17.0f);
            ((Label)object2).a(-1);
            ((Label)object2).setText(stringArray2[n]);
            ((Label)object2).setMaxWidth(buttonStyle.width);
            ((Label)object2).setAlign(Align.CENTER);
            ((Container)object3).addChild((Widget)object2);
            object = new Label(GuiAssets.getDefaultFont());
            ((Widget)object).setX(0.0f);
            ((Widget)object).setY(16.0f);
            ((Label)object).a(-16777216);
            ((Label)object).setText(stringArray2[n]);
            ((Label)object).setMaxWidth(buttonStyle.width);
            ((Label)object).setAlign(Align.CENTER);
            ((Container)object3).addChild((Widget)object);
            Button menuButton = new Button(n == 0 ? buttonStyle2 : buttonStyle, (Widget)object3);
            menuButton.setData(n + 1);
            menuButton.setX(19.0f);
            menuButton.setY(12.0f);
            container2.addChild((Widget)menuButton);
            ++n;
        }
        this.statisticsPanel = new NinePatchImage(GuiAssets.getMenuPanelNinePatch());
        this.statisticsPanel.setWidth(230.0f);
        this.statisticsPanel.setHeight(100.0f);
        container.addChild(this.statisticsPanel);
        this.statisticsTitleLabel = new Label(GuiAssets.getFontXirod17());
        this.statisticsTitleLabel.a(-2039584);
        this.statisticsTitleLabel.setText(Messages.getFallback("Statistics[i18n]: Statistics"));
        this.statisticsTitleLabel.setMaxWidth(230);
        this.statisticsTitleLabel.setAlign(Align.CENTER);
        container.addChild(this.statisticsTitleLabel);
        this.menuItemImages = new Image[6];
        TextureRegion textureRegion = new TextureRegion(163, 266, 55, 3);
        int n2 = 0;
        while (n2 < this.menuItemImages.length) {
            this.menuItemImages[n2] = new Image(textureRegion);
            container.addChild(this.menuItemImages[n2]);
            ++n2;
        }
        CampaignGameMode campaignGameMode = GameConfig.getCampaignGameMode();
        this.campaignHighScoreCaption = i2 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
        this.campaignHighScoreCaption.a(-7303024);
        this.campaignHighScoreCaption.setText(i2 ? Messages.get("CampaignHighScore[i18n]: Campaign High Score") : Messages.getFallback("CampaignHighScore[i18n]: Campaign High Score"));
        container.addChild(this.campaignHighScoreCaption);
        this.campaignHighScoreValue = new Label(GuiAssets.getDefaultFont());
        this.campaignHighScoreValue.a(-1);
        this.campaignHighScoreValue.setText(String.valueOf(campaignGameMode.getScore()));
        this.campaignHighScoreValue.setMaxWidth(230);
        this.campaignHighScoreValue.setAlign(Align.RIGHT);
        container.addChild(this.campaignHighScoreValue);
        float f = UserConfig.getPlayingTime();
        this.hoursPlayedCaption = i2 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
        this.hoursPlayedCaption.a(-7303024);
        this.hoursPlayedCaption.setText(i2 ? Messages.get("HoursPlayed[i18n]: Hours Played") : Messages.getFallback("HoursPlayed[i18n]: Hours Played"));
        container.addChild(this.hoursPlayedCaption);
        this.hoursPlayedValue = new Label(GuiAssets.getDefaultFont());
        this.hoursPlayedValue.a(-1);
        this.hoursPlayedValue.setText(TextFormatter.formatTime(f / 60.0f));
        this.hoursPlayedValue.setMaxWidth(230);
        this.hoursPlayedValue.setAlign(Align.RIGHT);
        container.addChild(this.hoursPlayedValue);
        object3 = UserConfig.getStatistics();
        this.unitsCaption = i2 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
        this.unitsCaption.a(-7303024);
        this.unitsCaption.setText(i2 ? Messages.get("UnitsDestroyedLost[i18n]: Units Destroyed/Lost") : Messages.getFallback("UnitsDestroyedLost[i18n]: Units Destroyed/Lost"));
        container.addChild(this.unitsCaption);
        this.unitsDestroyedValue = new Label(GuiAssets.getDefaultFont());
        this.unitsDestroyedValue.a(-7168);
        this.unitsDestroyedValue.setText(String.valueOf(((PlayerStatistics)object3).getDestroyedUnits()));
        this.unitsDestroyedValue.setMaxWidth(230);
        container.addChild(this.unitsDestroyedValue);
        this.unitsLostValue = new Label(GuiAssets.getDefaultFont());
        this.unitsLostValue.a(-65536);
        this.unitsLostValue.setText(String.valueOf(((PlayerStatistics)object3).getLostUnits()));
        this.unitsLostValue.setMaxWidth(230);
        this.unitsLostValue.setAlign(Align.RIGHT);
        container.addChild(this.unitsLostValue);
        this.damageCaption = i2 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
        this.damageCaption.a(-7303024);
        this.damageCaption.setText(i2 ? Messages.get("DamageInflicedSustained[i18n]: Damage Inflicted/Sustained") : Messages.getFallback("DamageInflicedSustained[i18n]: Damage Inflicted/Sustained"));
        container.addChild(this.damageCaption);
        this.damageInflictedValue = new Label(GuiAssets.getDefaultFont());
        this.damageInflictedValue.a(-7168);
        this.damageInflictedValue.setText(TextFormatter.formatTenths(((PlayerStatistics)object3).getDamageInflicted()));
        this.damageInflictedValue.setMaxWidth(230);
        container.addChild(this.damageInflictedValue);
        this.damageReceivedValue = new Label(GuiAssets.getDefaultFont());
        this.damageReceivedValue.a(-65536);
        this.damageReceivedValue.setText(TextFormatter.formatTenths(((PlayerStatistics)object3).getDamageReceived()));
        this.damageReceivedValue.setMaxWidth(230);
        this.damageReceivedValue.setAlign(Align.RIGHT);
        container.addChild(this.damageReceivedValue);
        this.basesCaption = i2 ? new Label(GuiAssets.getDefaultFont(), true) : new Label(GuiAssets.getFontDokchampa15());
        this.basesCaption.a(-7303024);
        this.basesCaption.setText(i2 ? Messages.get("BasesCapturedLost[i18n]: Bases Captured/Lost") : Messages.getFallback("BasesCapturedLost[i18n]: Bases Captured/Lost"));
        container.addChild(this.basesCaption);
        this.basesCapturedValue = new Label(GuiAssets.getDefaultFont());
        this.basesCapturedValue.a(-7168);
        this.basesCapturedValue.setText(String.valueOf(((PlayerStatistics)object3).getBasesCaptured()));
        this.basesCapturedValue.setMaxWidth(230);
        container.addChild(this.basesCapturedValue);
        this.basesLostValue = new Label(GuiAssets.getDefaultFont());
        this.basesLostValue.a(-65536);
        this.basesLostValue.setText(String.valueOf(((PlayerStatistics)object3).getBasesLost()));
        this.basesLostValue.setMaxWidth(230);
        this.basesLostValue.setAlign(Align.RIGHT);
        container.addChild(this.basesLostValue);
        if (!GameConfig.getDataFileResolver().isExternalStorageAvailable()) {
            object2 = Messages.getFallback("ExternalStorage[i18n]: External Storage");
            object = Messages.get("ExternalStorageMissingETC[i18n]: External storage is not available to load/save games; i.e. on Android make sure the SD card is plugged in and available for data I/O. Make sure it is not mounted for use with a PC. The most likely solution is to disconnect the phone charging cable from the PC. ");
            stringArray = new String[]{Messages.get("Exit[i18n]: Exit")};
            this.showDialog(0, (String)object2, (String)object, stringArray);
        } else if (UserConfig.isShowTutorialDialog()) {
            UserConfig.setShowTutorialDialog(false);
            object2 = Messages.getFallback("PlayTutorial[i18n]: Play Tutorial");
            object = Messages.get("PlayTutorialETC[i18n]: This is your first time, do you want to play the tutorial to get started?");
            stringArray = new String[]{Messages.get("Play[i18n]: Play"), Messages.get("Later[i18n]: Later")};
            this.showDialog(1, (String)object2, (String)object, stringArray);
        } else if (Gdx.app.getType() == Application.ApplicationType.Desktop && GameConfig.getVersion().isOlderThan(UserConfig.getNewsSeenVersion()) && !this.getMarket().getName().equals("iWin")) {
            UserConfig.setShowTutorialDialog(false);
            object2 = Messages.getFallback("WhatsNew[i18n]: What's New");
            object = GameConfig.getConfigDir().equals("config_tsf/") ? "LATEST UPDATE:\n\nMatch-Making Server Updated." : "LATEST UPDATE:\n\nMatch-Making Server Updated.";
            stringArray = new String[]{Messages.get("OK[i18n]: OK")};
            this.showDialog(2, (String)object2, (String)object, stringArray);
        }
        UserConfig.setNewsSeenVersion(GameConfig.getVersion());
        return false;
    }

    @Override
    protected void createContent(Container container) {
        int i2 = this.getWidth();
        int i3 = i2 < 800 ? 0 : (i2 - 800) / 16;
        this.manualButtonImage.setX(0.0f);
        this.manualButtonImage.setY(container.getHeight() - this.manualButtonImage.getHeight() - 18.0f - (float)i3);
        this.manualButton.setX(this.manualButtonImage.getX() + 19.0f);
        this.manualButton.setY(this.manualButtonImage.getY() + 12.0f);
        this.creditsButtonImage.setX(this.manualButtonImage.getX() + 100.0f);
        this.creditsButtonImage.setY(container.getHeight() - this.creditsButtonImage.getHeight() - 18.0f - (float)i3);
        this.creditsButton.setX(this.creditsButtonImage.getX() + 19.0f);
        this.creditsButton.setY(this.creditsButtonImage.getY() + 12.0f);
        if (this.fullScreenButtonImage != null) {
            this.fullScreenButtonImage.setX(this.creditsButtonImage.getX() + 100.0f);
            this.fullScreenButtonImage.setY(container.getHeight() - this.fullScreenButtonImage.getHeight() - 18.0f - (float)i3);
            this.fullScreenCheckbox.setX(this.fullScreenButtonImage.getX() + 19.0f);
            this.fullScreenCheckbox.setY(this.fullScreenButtonImage.getY() + 12.0f);
            this.fullScreenRowPanel.setX(this.fullScreenButtonImage.getX() + 26.0f);
            this.fullScreenRowPanel.setY(this.fullScreenButtonImage.getY() - 34.0f);
            this.fullScreenLabel.setX(this.fullScreenRowPanel.getX() + 12.0f);
            this.fullScreenLabel.setY(this.fullScreenRowPanel.getY() + 7.0f);
        }
        float f4 = container.getWidth();
        float f5 = container.getHeight();
        float f6 = this.menuItemContainers[0].getWidth();
        float f7 = this.menuItemContainers[0].getHeight();
        float f8 = (f5 - (float)this.menuItemContainers.length * f7) / (float)(this.menuItemContainers.length - 1);
        if (f8 > 12.0f) {
            f8 = 12.0f;
        }
        float f9 = f8 + f7;
        float f10 = (f5 - (float)this.menuItemContainers.length * f7 - (float)(this.menuItemContainers.length - 1) * f8) / 3.0f;
        float f11 = f4 - f6 - 30.0f;
        int n = 0;
        while (n < this.menuItemContainers.length) {
            this.menuItemContainers[n].setX(f11);
            this.menuItemContainers[n].setY(f10 + (float)n * f9);
            ++n;
        }
        this.menuDividerLine.setX(f11 + 83.0f);
        this.menuDividerLine.setY(0.0f);
        this.menuDividerLine.setHeight(this.menuItemContainers[this.menuItemContainers.length - 1].getY() + 15.0f);
        this.menuDividerEnd.setX(this.menuDividerLine.getX());
        this.menuDividerEnd.setY(this.menuItemContainers[this.menuItemContainers.length - 1].getY() + this.menuItemContainers[this.menuItemContainers.length - 1].getHeight());
        this.statisticsPanel.setX(this.menuItemContainers[0].getX() - 230.0f - 25.0f - 25.0f - 20.0f - (float)i3);
        this.statisticsPanel.setY(this.menuItemContainers[0].getY());
        this.statisticsPanel.setWidth(280.0f);
        this.statisticsPanel.setHeight(this.menuItemContainers[this.menuItemContainers.length - 2].getY() + this.menuItemContainers[this.menuItemContainers.length - 2].getHeight() - this.statisticsPanel.getY());
        float f = this.statisticsPanel.getX() + 25.0f;
        float f13 = this.statisticsPanel.getY() + 68.0f;
        this.statisticsTitleLabel.setX(f);
        this.statisticsTitleLabel.setY(f13 - 38.0f);
        int i14 = 0;
        while (i14 < this.menuItemImages.length) {
            this.menuItemImages[i14].setX(f);
            this.menuItemImages[i14].setY(f13 + (float)(i14 * 45) - 6.0f);
            this.menuItemImages[i14].setWidth(230.0f);
            ++i14;
        }
        this.campaignHighScoreCaption.setX(f);
        this.campaignHighScoreCaption.setY(f13 + 0.0f);
        this.campaignHighScoreValue.setX(f);
        this.campaignHighScoreValue.setY(f13 + 0.0f + 20.0f);
        this.hoursPlayedCaption.setX(f);
        this.hoursPlayedCaption.setY(f13 + 45.0f);
        this.hoursPlayedValue.setX(f);
        this.hoursPlayedValue.setY(f13 + 45.0f + 20.0f);
        this.unitsCaption.setX(f);
        this.unitsCaption.setY(f13 + 90.0f);
        this.unitsDestroyedValue.setX(f);
        this.unitsDestroyedValue.setY(f13 + 90.0f + 20.0f);
        this.unitsLostValue.setX(f);
        this.unitsLostValue.setY(f13 + 90.0f + 20.0f);
        this.damageCaption.setX(f);
        this.damageCaption.setY(f13 + 135.0f);
        this.damageInflictedValue.setX(f);
        this.damageInflictedValue.setY(f13 + 135.0f + 20.0f);
        this.damageReceivedValue.setX(f);
        this.damageReceivedValue.setY(f13 + 135.0f + 20.0f);
        this.basesCaption.setX(f);
        this.basesCaption.setY(f13 + 180.0f);
        this.basesCapturedValue.setX(f);
        this.basesCapturedValue.setY(f13 + 180.0f + 20.0f);
        this.basesLostValue.setX(f);
        this.basesLostValue.setY(f13 + 180.0f + 20.0f);
    }

    @Override
    protected BaseScreen handleWidgetAction(Widget widget) {
        int i2 = (Integer)widget.getData();
        switch (i2) {
            case 0: {
                this.exitApplication();
                return null;
            }
            case 1: {
                TutorialGameMode tutorialGameMode = GameConfig.getTutorialGameMode();
                tutorialGameMode.createStartWorld();
                return new LoadGameScreen(tutorialGameMode);
            }
            case 2: {
                return new CampaignScreen(this);
            }
            case 3: {
                return new SkirmishSetupScreen(this, GameConfig.getSkirmishGameMode());
            }
            case 4: {
                return new MultiplayerLobbyScreen(this);
            }
            case 5: {
                return new OptionsScreen(this);
            }
            case 100: {
                return new ManualScreen(this);
            }
            case 101: {
                return new CreditsScreen(this);
            }
            case 102: {
                UserConfig.setFullScreen(!UserConfig.isFullScreen());
                if (UserConfig.isFullScreen()) {
                    Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
                } else {
                    Gdx.graphics.setDisplayMode(GameConfig.getScreenWidth(), GameConfig.getScreenHeight(), false);
                }
                this.resetCursor();
                return null;
            }
        }
        OsfLog.error("Action not implemented: " + i2);
        return null;
    }

    @Override
    protected BaseScreen handleAction(int i1, int i2) {
        if (i1 == 0) {
            this.exitApplication();
            return null;
        }
        if (i1 == 1) {
            switch (i2) {
                case 0: {
                    TutorialGameMode tutorialGameMode = GameConfig.getTutorialGameMode();
                    tutorialGameMode.createStartWorld();
                    return new LoadGameScreen(tutorialGameMode);
                }
                case 1: {
                    return null;
                }
            }
            OsfLog.error("Action not implemented: " + i2);
            return null;
        }
        if (i1 == 2) {
            return null;
        }
        OsfLog.error("Reference not implemented: " + i1);
        return null;
    }
}


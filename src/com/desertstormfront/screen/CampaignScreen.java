/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.player.Difficulty;
import com.desertstormfront.screen.BaseMenuScreen;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.LoadGameScreen;
import com.desertstormfront.screen.MainMenuScreen;
import com.desertstormfront.session.impl.CampaignGameMode;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.ListBox;
import com.desertstormfront.ui.NinePatchImage;
import com.desertstormfront.ui.ScrollPane;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.log.OsfLog;

public final class CampaignScreen
extends BaseMenuScreen {
    private NinePatchImage backgroundImage;
    private ScrollPane scrollPane;
    private Container contentContainer;
    private NinePatchImage scorePanel;
    private Label scoreCaption;
    private Label scoreValue;
    private NinePatchImage highScorePanel;
    private Label highScoreCaption;
    private Label highScoreValue;
    private Label overviewLabel;
    private ListBox difficultyListBox;
    private NinePatchImage actionRowPanel;
    private Image actionRowHighlight;
    private Button actionButton;
    private Label storyLabel;
    private NinePatchImage storyRowPanel;
    private Image storyRowHighlight;
    private Button startButton;
    private Image completeBarImage;
    private Label campaignCompleteLabel;
    private NinePatchImage completeRowPanel;
    private Image completeRowHighlight;
    private Button restartButton;
    private Widget[] missionWidgets;
    private Label[] missionLabels;
    private Label difficultyCaption;
    private NinePatchImage difficultyPanel;
    private ListBox difficultyListBoxStarted;
    private Label resetCaption;
    private NinePatchImage resetPanel;
    private Button resetButton;
    private int newGameConfirmStep;
    private Difficulty selectedDifficulty;
    private int pendingMission;

    public CampaignScreen() {
    }

    public CampaignScreen(BaseMenuScreen baseMenuScreen) {
        super(baseMenuScreen);
    }

    @Override
    protected boolean isBackEnabled(Container container) {
        CampaignGameMode campaignGameMode = GameConfig.getCampaignGameMode();
        this.backgroundImage = new NinePatchImage(GuiAssets.getBackgroundNinePatch());
        container.addChild(this.backgroundImage);
        this.scrollPane = new ScrollPane(GuiAssets.getScrollBarStyle());
        container.addChild(this.scrollPane);
        this.contentContainer = new Container();
        this.scrollPane.setContent(this.contentContainer);
        boolean i3 = GameConfig.isDarkTheme();
        this.scorePanel = new NinePatchImage(GuiAssets.getMenuPanelNinePatch());
        container.addChild(this.scorePanel);
        this.scoreCaption = i3 ? new Label(GuiAssets.getDefaultFont(), false) : new Label(GuiAssets.getFontDokchampa15());
        this.scoreCaption.a(-8355712);
        this.scoreCaption.setText(i3 ? Messages.get("CurrentScore[i18n]: Current Score") : Messages.getFallback("CurrentScore[i18n]: Current Score"));
        container.addChild(this.scoreCaption);
        this.scoreValue = new Label(GuiAssets.getFontDungeon34());
        this.scoreValue.a(-65536);
        this.scoreValue.setAlign(Align.RIGHT);
        container.addChild(this.scoreValue);
        this.highScorePanel = new NinePatchImage(GuiAssets.getMenuPanelNinePatch());
        container.addChild(this.highScorePanel);
        this.highScoreCaption = i3 ? new Label(GuiAssets.getDefaultFont(), false) : new Label(GuiAssets.getFontDokchampa15());
        this.highScoreCaption.a(-8355712);
        this.highScoreCaption.setText(i3 ? Messages.get("HighScore[i18n]: High Score") : Messages.getFallback("HighScore[i18n]: High Score"));
        container.addChild(this.highScoreCaption);
        this.highScoreValue = new Label(GuiAssets.getFontDungeon34());
        this.highScoreValue.a(-65536);
        this.highScoreValue.setText(String.valueOf(campaignGameMode.getHighScore()));
        this.highScoreValue.setAlign(Align.RIGHT);
        container.addChild(this.highScoreValue);
        this.newGameConfirmStep = 0;
        this.selectedDifficulty = null;
        this.rebuildMissionList();
        this.showPurchaseButton();
        return true;
    }

    private void rebuildMissionList() {
        CampaignGameMode campaignGameMode = GameConfig.getCampaignGameMode();
        this.contentContainer.clearChildren();
        this.overviewLabel = null;
        this.difficultyListBox = null;
        this.actionRowPanel = null;
        this.actionRowHighlight = null;
        this.actionButton = null;
        this.storyLabel = null;
        this.storyRowPanel = null;
        this.storyRowHighlight = null;
        this.startButton = null;
        this.completeBarImage = null;
        this.campaignCompleteLabel = null;
        this.completeRowPanel = null;
        this.completeRowHighlight = null;
        this.restartButton = null;
        this.missionWidgets = null;
        this.missionLabels = null;
        this.difficultyCaption = null;
        this.difficultyPanel = null;
        this.difficultyListBoxStarted = null;
        this.resetCaption = null;
        this.resetPanel = null;
        this.resetButton = null;
        if (campaignGameMode.isStarted()) {
            this.scoreValue.setText(String.valueOf(campaignGameMode.getScore()));
            if (campaignGameMode.isComplete()) {
                this.completeBarImage = new Image(new TextureRegion(549, 474, 364, 53));
                this.contentContainer.addChild(this.completeBarImage);
                this.campaignCompleteLabel = this.createLabel(5, 0, campaignGameMode.getCampaignCompleteText());
                this.contentContainer.addChild(this.campaignCompleteLabel);
                this.completeRowPanel = new NinePatchImage(GuiAssets.getRowNinePatch());
                this.contentContainer.addChild(this.completeRowPanel);
                this.completeRowHighlight = new Image(GuiAssets.getRowHighlightRegion());
                this.contentContainer.addChild(this.completeRowHighlight);
                this.restartButton = this.createButton(-1001, 0, 0, Messages.get("Restart[i18n]: Restart"));
                this.contentContainer.addChild(this.restartButton);
            }
            int n = campaignGameMode.getMissionNumber();
            this.missionWidgets = new Widget[campaignGameMode.getTotalMissions()];
            this.missionLabels = new Label[campaignGameMode.getTotalMissions()];
            int[] nArray = new int[4];
            nArray[1] = 65;
            nArray[2] = 130;
            nArray[3] = 195;
            ButtonStyle buttonStyle = new ButtonStyle(nArray, new int[]{1036, 1036, 1036, 1036}, 64, 64);
            int[] nArray2 = new int[4];
            nArray2[0] = 772;
            nArray2[1] = 837;
            nArray2[2] = 902;
            int[] nArray3 = new int[4];
            nArray3[0] = 604;
            nArray3[1] = 604;
            nArray3[2] = 604;
            ButtonStyle buttonStyle2 = new ButtonStyle(nArray2, nArray3, 64, 64);
            TextureRegion textureRegion = new TextureRegion(325, 1036, 64, 64);
            int n2 = 0;
            while (n2 < campaignGameMode.getTotalMissions()) {
                Widget widget;
                Widget object;
                if (n2 < campaignGameMode.getMissionCount() && (GameConfig.isDebugEnabled() || n2 == n - 1)) {
                    object = new Button(buttonStyle);
                    ((Widget)object).setData(n2 + 1);
                    ((Widget)object).setX(5.0f);
                    this.contentContainer.addChild((Widget)object);
                    widget = object;
                } else if (n2 < campaignGameMode.getMissionCount() && n2 < n) {
                    object = new Button(buttonStyle2);
                    ((Widget)object).setData(n2 + 1);
                    ((Widget)object).setX(5.0f);
                    this.contentContainer.addChild((Widget)object);
                    widget = object;
                } else {
                    object = new Image(textureRegion);
                    ((Widget)object).setX(5.0f);
                    this.contentContainer.addChild((Widget)object);
                    widget = object;
                }
                String missionText = GameConfig.isDebugEnabled() ? (n2 >= campaignGameMode.getMissionCount() ? Messages.format("MissionXNotAvailableLiteETC[i18n]: Mission {0} is not available for the LITE version of the game.", n2 + 1) : String.valueOf(Messages.format("MissionX[i18n]: Mission {0}:", n2 + 1)) + Messages.getWordSeparator() + campaignGameMode.getMissionDescription(n2 + 1)) : (n2 >= campaignGameMode.getMissionCount() ? Messages.format("MissionXNotAvailableLiteETC[i18n]: Mission {0} is not available for the LITE version of the game.", n2 + 1) : (n2 == n - 1 ? String.valueOf(Messages.format("MissionX[i18n]: Mission {0}:", n2 + 1)) + Messages.getWordSeparator() + campaignGameMode.getMissionDescription(n2 + 1) : (n2 < n ? Messages.format("MissionXCompleted[i18n]: Mission {0} completed.", n2 + 1) : Messages.format("MissionXLocked[i18n]: Mission {0} locked.", n2 + 1))));
                Label label = this.createLabel(Math.round(widget.getX() + widget.getWidth() + 5.0f), 0, missionText);
                this.contentContainer.addChild(label);
                this.missionWidgets[n2] = widget;
                this.missionLabels[n2] = label;
                ++n2;
            }
            this.difficultyCaption = this.createLabel(5, 5, String.valueOf(Messages.get("AdjustTheDifficulty[i18n]: Adjust the difficulty for the campaign")) + ":");
            this.contentContainer.addChild(this.difficultyCaption);
            this.difficultyPanel = new NinePatchImage(GuiAssets.getListPanelNinePatch());
            this.contentContainer.addChild(this.difficultyPanel);
            this.difficultyListBoxStarted = this.createListBox(-101, 5, 0);
            String[] stringArray = new String[Difficulty.values().length];
            int n3 = 0;
            while (n3 < Difficulty.values().length) {
                stringArray[n3] = Messages.format("DifficultyX[i18n]: Difficulty: {0}", Messages.get(Difficulty.values()[n3].getName()));
                ++n3;
            }
            this.difficultyListBoxStarted.setItems(stringArray);
            this.difficultyListBoxStarted.setSelectedIndex(campaignGameMode.getDifficulty().ordinal());
            this.contentContainer.addChild(this.difficultyListBoxStarted);
            this.resetCaption = this.createLabel(5, 0, String.valueOf(Messages.get("ResetTheCampaignETC[i18n]: Reset the campaign (cannot be undone)")) + ":");
            this.contentContainer.addChild(this.resetCaption);
            this.resetPanel = new NinePatchImage(GuiAssets.getListPanelNinePatch());
            this.contentContainer.addChild(this.resetPanel);
            this.resetButton = this.createButton(-1000, 0, 0, Messages.get("Reset[i18n]: Reset"));
            this.contentContainer.addChild(this.resetButton);
        } else {
            this.scoreValue.setText(Messages.get("NA[i18n]: N/A"));
            if (this.newGameConfirmStep == 0) {
                String string = Messages.get("CampaignOverviewETC[i18n]: Take command and join the ultimate war of good versus evil.");
                this.overviewLabel = this.createLabel(5, 5, string);
                this.contentContainer.addChild(this.overviewLabel);
                this.difficultyListBox = this.createListBox(-100, 5, 0);
                String[] stringArray = new String[Difficulty.values().length];
                int n = 0;
                while (n < Difficulty.values().length) {
                    stringArray[n] = Messages.format("DifficultyX[i18n]: Difficulty: {0}", Messages.get(Difficulty.values()[n].getName()));
                    ++n;
                }
                this.difficultyListBox.setItems(stringArray);
                this.selectedDifficulty = Difficulty.Casual;
                this.difficultyListBox.setSelectedIndex(this.selectedDifficulty.ordinal());
                this.contentContainer.addChild(this.difficultyListBox);
                this.actionRowPanel = new NinePatchImage(GuiAssets.getRowNinePatch());
                this.contentContainer.addChild(this.actionRowPanel);
                this.actionRowHighlight = new Image(GuiAssets.getRowHighlightRegion());
                this.contentContainer.addChild(this.actionRowHighlight);
                this.actionButton = this.createButton(-2, 0, 0, Messages.get("Create[i18n]: Create"));
                this.contentContainer.addChild(this.actionButton);
            } else {
                this.storyLabel = this.createLabel(5, 5, campaignGameMode.getCampaignStory());
                this.contentContainer.addChild(this.storyLabel);
                this.storyRowPanel = new NinePatchImage(GuiAssets.getRowNinePatch());
                this.contentContainer.addChild(this.storyRowPanel);
                this.storyRowHighlight = new Image(GuiAssets.getRowHighlightRegion());
                this.contentContainer.addChild(this.storyRowHighlight);
                this.startButton = this.createButton(-2, 0, 0, Messages.get("Start[i18n]: Start"));
                this.contentContainer.addChild(this.startButton);
            }
        }
        this.contentContainer.pack();
        this.layout();
    }

    @Override
    protected void createContent(Container container) {
        float f5;
        float f6;
        this.setupScrollPane(container, this.backgroundImage, this.scrollPane, 70);
        float f2 = 25.0f;
        float f3 = this.backgroundImage.getX() + f2;
        float f4 = this.backgroundImage.getWidth() - f2 - f2;
        this.scorePanel.setX(f3);
        this.scorePanel.setY(this.backgroundImage.getY() - 75.0f);
        this.scorePanel.setWidth((f4 - f2) / 2.0f);
        this.scorePanel.setHeight(65.0f);
        this.highScorePanel.setX(this.scorePanel.getX() + this.scorePanel.getWidth() + f2);
        this.highScorePanel.setY(this.scorePanel.getY());
        this.highScorePanel.setWidth((f4 - f2) / 2.0f);
        this.highScorePanel.setHeight(65.0f);
        this.scoreCaption.setX(this.scorePanel.getX() + 30.0f);
        this.scoreCaption.setY(this.scorePanel.getY() + 23.0f);
        this.scoreValue.setMaxWidth((int)this.scorePanel.getWidth() - 30);
        this.scoreValue.setX(this.scorePanel.getX());
        this.scoreValue.setY(this.scorePanel.getY() + 8.0f);
        this.highScoreCaption.setX(this.highScorePanel.getX() + 30.0f);
        this.highScoreCaption.setY(this.highScorePanel.getY() + 23.0f);
        this.highScoreValue.setMaxWidth((int)this.highScorePanel.getWidth() - 30);
        this.highScoreValue.setX(this.highScorePanel.getX());
        this.highScoreValue.setY(this.highScorePanel.getY() + 8.0f);
        if (this.campaignCompleteLabel != null) {
            this.campaignCompleteLabel.setMaxWidth(Math.round(this.scrollPane.getWidth() - (float)this.scrollPane.getScrollBarWidth() - 10.0f));
            f6 = this.scrollPane.getWidth();
            if (20.0f + this.completeBarImage.getY() + 10.0f + (float)this.campaignCompleteLabel.getTextWidth() + 18.0f + this.restartButton.getHeight() + 5.0f >= this.scrollPane.getHeight()) {
                f6 -= (float)this.scrollPane.getScrollBarWidth();
            }
            this.completeBarImage.setX((f6 - this.completeBarImage.getWidth()) / 2.0f);
            this.completeBarImage.setY(20.0f);
            this.campaignCompleteLabel.setY(this.completeBarImage.getY() + this.completeBarImage.getHeight() + 20.0f);
            this.completeRowPanel.setWidth(f6);
            this.completeRowPanel.setHeight(55.0f);
            this.completeRowPanel.setY(this.campaignCompleteLabel.getY() + (float)this.campaignCompleteLabel.getTextWidth() + 18.0f);
            this.completeRowHighlight.setX((f6 - this.completeRowHighlight.getWidth()) / 2.0f);
            this.completeRowHighlight.setY(this.completeRowPanel.getY() + 2.0f);
            this.restartButton.setX(this.completeRowHighlight.getX() + 2.0f);
            this.restartButton.setY(this.completeRowHighlight.getY() + 2.0f);
            f5 = this.completeRowHighlight.getY() + this.completeRowHighlight.getHeight() + 25.0f;
        } else {
            f5 = 5.0f;
        }
        if (this.missionWidgets != null) {
            f6 = this.scrollPane.getWidth() - (float)this.scrollPane.getScrollBarWidth();
            int i7 = 0;
            while (i7 < this.missionWidgets.length) {
                this.missionWidgets[i7].setY(f5);
                this.missionLabels[i7].setX(this.missionWidgets[i7].getX() + this.missionWidgets[i7].getWidth() + 5.0f);
                this.missionLabels[i7].setMaxWidth(Math.round(f6 - this.missionLabels[i7].getX() - 5.0f));
                this.missionLabels[i7].setY(f5);
                f5 = (float)this.missionLabels[i7].getTextWidth() > this.missionWidgets[i7].getHeight() ? (f5 += (float)this.missionLabels[i7].getTextWidth()) : (f5 += this.missionWidgets[i7].getHeight());
                f5 += 20.0f;
                ++i7;
            }
            this.difficultyCaption.setY(f5 += 20.0f);
            this.difficultyCaption.setMaxWidth(Math.round(f6 - 10.0f));
            this.difficultyPanel.setWidth(f6);
            this.difficultyPanel.setHeight(55.0f);
            this.difficultyPanel.setY(f5 += (float)(this.difficultyCaption.getTextWidth() + 5));
            this.difficultyListBoxStarted.setLabelMaxWidth(Math.round(f6 - (float)this.difficultyListBoxStarted.getArrowWidth() - 10.0f));
            this.difficultyListBoxStarted.setY(this.difficultyPanel.getY() + 4.0f);
            this.difficultyListBoxStarted.setLabelAlign(-16732433);
            this.resetCaption.setY(f5 += this.difficultyPanel.getHeight() + 15.0f);
            this.resetCaption.setMaxWidth(Math.round(f6 - 10.0f));
            this.resetPanel.setWidth(f6);
            this.resetPanel.setHeight(55.0f);
            this.resetPanel.setY(f5 += (float)(this.resetCaption.getTextWidth() + 5));
            this.resetButton.setX((f6 - this.resetButton.getWidth()) / 2.0f);
            this.resetButton.setY(this.resetPanel.getY() + 4.0f);
        }
        if (this.overviewLabel != null) {
            this.overviewLabel.setMaxWidth(Math.round(this.scrollPane.getWidth() - (float)this.scrollPane.getScrollBarWidth() - 10.0f));
            f6 = this.scrollPane.getWidth();
            if (this.overviewLabel.getY() + (float)this.overviewLabel.getTextWidth() + 20.0f + 55.0f + this.actionButton.getHeight() + 5.0f >= this.scrollPane.getHeight()) {
                f6 -= (float)this.scrollPane.getScrollBarWidth();
            }
            this.difficultyListBox.setLabelMaxWidth(Math.round(f6 - (float)this.difficultyListBox.getArrowWidth() - 10.0f));
            this.difficultyListBox.setY(this.overviewLabel.getY() + (float)this.overviewLabel.getTextWidth() + 20.0f);
            this.actionRowPanel.setWidth(f6);
            this.actionRowPanel.setHeight(55.0f);
            this.actionRowPanel.setY(this.difficultyListBox.getY() + 55.0f);
            this.actionRowHighlight.setX((f6 - this.actionRowHighlight.getWidth()) / 2.0f);
            this.actionRowHighlight.setY(this.actionRowPanel.getY() + 2.0f);
            this.actionButton.setX(this.actionRowHighlight.getX() + 2.0f);
            this.actionButton.setY(this.actionRowHighlight.getY() + 2.0f);
        }
        if (this.storyLabel != null) {
            this.storyLabel.setMaxWidth(Math.round(this.scrollPane.getWidth() - (float)this.scrollPane.getScrollBarWidth() - 10.0f));
            f6 = this.scrollPane.getWidth();
            if (this.storyLabel.getY() + (float)this.storyLabel.getTextWidth() + 18.0f + this.startButton.getHeight() + 5.0f >= this.scrollPane.getHeight()) {
                f6 -= (float)this.scrollPane.getScrollBarWidth();
            }
            this.storyRowPanel.setWidth(f6);
            this.storyRowPanel.setHeight(55.0f);
            this.storyRowPanel.setY(this.storyLabel.getY() + (float)this.storyLabel.getTextWidth() + 18.0f);
            this.storyRowHighlight.setX((f6 - this.storyRowHighlight.getWidth()) / 2.0f);
            this.storyRowHighlight.setY(this.storyRowPanel.getY() + 2.0f);
            this.startButton.setX(this.storyRowHighlight.getX() + 2.0f);
            this.startButton.setY(this.storyRowHighlight.getY() + 2.0f);
        }
        container.pack();
    }

    @Override
    protected BaseScreen handleWidgetAction(Widget widget) {
        int i2 = (Integer)widget.getData();
        if (i2 == 0) {
            return new MainMenuScreen(this);
        }
        if (i2 > 0) {
            CampaignGameMode campaignGameMode = GameConfig.getCampaignGameMode();
            if (!campaignGameMode.hasSavedGame()) {
                campaignGameMode.selectMission(i2);
                return new LoadGameScreen(campaignGameMode);
            }
            if (campaignGameMode.getSelectedMission() != i2) {
                this.pendingMission = i2;
                String string = Messages.getFallback("ExistingGame[i18n]: Existing Game");
                String string2 = Messages.format("ExistingGameCampaignXETC[i18n]: You have an existing game active for Mission {0}. The existing game will be deleted if you decide to continue!", campaignGameMode.getSelectedMission());
                String[] stringArray = new String[]{Messages.get("Cancel[i18n]: Cancel"), Messages.get("NewGame[i18n]: New Game")};
                this.showDialog(3, string, string2, stringArray);
                return null;
            }
            String string = Messages.getFallback("ContinueGame[i18n]: Continue Game");
            String string3 = Messages.get("ContinueGameETC[i18n]: Do you want to continue the existing game?");
            String[] stringArray = new String[]{Messages.get("Continue[i18n]: Continue"), Messages.get("NewGame[i18n]: New Game")};
            this.showDialog(1, string, string3, stringArray);
            return null;
        }
        switch (i2) {
            case -9999: {
                return this.showPurchaseDialog(9999);
            }
            case -1001: {
                this.resetCampaign();
                return null;
            }
            case -1000: {
                String string = Messages.getFallback("CampaignReset[i18n]: Campaign Reset");
                String string4 = Messages.get("CampaignResetETC[i18n]: Do you really want to reset the campaign and start from the beginning?");
                String[] stringArray = new String[]{Messages.get("Reset[i18n]: Reset"), Messages.get("Cancel[i18n]: Cancel")};
                this.showDialog(2, string, string4, stringArray);
                return null;
            }
            case -101: {
                ListBox listBox = (ListBox)widget;
                GameConfig.getCampaignGameMode().startCampaign(Difficulty.values()[listBox.getSelectedIndex()]);
                return null;
            }
            case -100: {
                ListBox listBox = (ListBox)widget;
                this.selectedDifficulty = Difficulty.values()[listBox.getSelectedIndex()];
                return null;
            }
            case -2: {
                if (this.newGameConfirmStep == 0) {
                    ++this.newGameConfirmStep;
                } else {
                    CampaignGameMode campaignGameMode = GameConfig.getCampaignGameMode();
                    campaignGameMode.startCampaign(this.selectedDifficulty);
                    this.selectedDifficulty = null;
                    this.newGameConfirmStep = 0;
                }
                this.rebuildMissionList();
                return null;
            }
        }
        OsfLog.error("Action not implemented: " + i2);
        return null;
    }

    @Override
    protected BaseScreen handleAction(int i1, int i2) {
        if (i1 == 1) {
            switch (i2) {
                case 0: {
                    CampaignGameMode campaignGameMode = GameConfig.getCampaignGameMode();
                    return new LoadGameScreen(campaignGameMode);
                }
                case 1: {
                    CampaignGameMode campaignGameMode = GameConfig.getCampaignGameMode();
                    campaignGameMode.selectMission(campaignGameMode.getSelectedMission());
                    return new LoadGameScreen(campaignGameMode);
                }
            }
            OsfLog.error("Action not implemented: " + i2);
            return null;
        }
        if (i1 == 2) {
            switch (i2) {
                case 0: {
                    this.resetCampaign();
                    return null;
                }
                case 1: {
                    return null;
                }
            }
            OsfLog.error("Action not implemented: " + i2);
            return null;
        }
        if (i1 == 3) {
            switch (i2) {
                case 0: {
                    return null;
                }
                case 1: {
                    CampaignGameMode campaignGameMode = GameConfig.getCampaignGameMode();
                    campaignGameMode.selectMission(this.pendingMission);
                    return new LoadGameScreen(campaignGameMode);
                }
            }
            OsfLog.error("Action not implemented: " + i2);
            return null;
        }
        if (i1 == 9999) {
            return this.handlePurchaseAction(i2);
        }
        OsfLog.error("Reference not implemented: " + i1);
        return null;
    }

    private void resetCampaign() {
        CampaignGameMode campaignGameMode = GameConfig.getCampaignGameMode();
        campaignGameMode.resetCampaign();
        this.newGameConfirmStep = 0;
        this.selectedDifficulty = null;
        this.rebuildMissionList();
    }
}


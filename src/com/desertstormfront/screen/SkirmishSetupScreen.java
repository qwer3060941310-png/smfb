/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.app.support.TextFormatter;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.ScenarioType;
import com.desertstormfront.game.World;
import com.desertstormfront.game.player.Difficulty;
import com.desertstormfront.game.player.Faction;
import com.desertstormfront.game.player.FactionList;
import com.desertstormfront.io.WorldSaveInfo;
import com.desertstormfront.io.WorldSerializer;
import com.desertstormfront.screen.BaseMenuScreen;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.LoadGameScreen;
import com.desertstormfront.screen.MainMenuScreen;
import com.desertstormfront.session.StartableSessionMode;
import com.desertstormfront.session.impl.MultiplayerGameMode;
import com.desertstormfront.session.impl.SkirmishGameMode;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.ListBox;
import com.desertstormfront.ui.NinePatchImage;
import com.desertstormfront.ui.ScrollPane;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.math.MathHelper;

public final class SkirmishSetupScreen
extends BaseMenuScreen {
    private final int minRandomMapSize = 32;
    private final int randomMapSizeStep = 8;
    private WorldSaveInfo[] skirmishSaves;
    private int randomMapVariants;
    private int inMemoryRandomMapCount = 3;
    private WorldSaveInfo[] campaignSaves;
    private NinePatchImage backgroundImage;
    private ScrollPane scrollPane;
    private Container contentContainer;
    private NinePatchImage winsPanel;
    private Label winsCaption;
    private Label winsValue;
    private NinePatchImage gamesPlayedPanel;
    private Label gamesPlayedCaption;
    private Label gamesPlayedValue;
    private Container existingGameContainer;
    private Label existingGameMessageLabel;
    private NinePatchImage buttonRowPanel;
    private Image continueRowHighlight;
    private Image deleteRowHighlight;
    private Button continueButton;
    private Button deleteButton;
    private Container optionsContainer;
    private NinePatchImage actionRowPanel;
    private Image playRowHighlight;
    private Image randomizeRowHighlight;
    private Button playButton;
    private Button randomizeButton;
    private ListBox mapListBox;
    private ListBox nationListBox;
    private ListBox playersListBox;
    private ListBox teamsListBox;
    private ListBox viewListBox;
    private ListBox commandoListBox;
    private ListBox moneyListBox;
    private ListBox incomeListBox;
    private ListBox timeLimitListBox;
    private ListBox difficultyListBox;
    private StartableSessionMode gameMode;

    public SkirmishSetupScreen(StartableSessionMode startableSessionMode) {
        this.gameMode = startableSessionMode;
    }

    public SkirmishSetupScreen(BaseMenuScreen baseMenuScreen, StartableSessionMode startableSessionMode) {
        super(baseMenuScreen);
        this.gameMode = startableSessionMode;
    }

    @Override
    public boolean isBackEnabled(Container container) {
        int i7;
        int n;
        this.skirmishSaves = WorldSerializer.listSaves("skirmish_");
        this.randomMapVariants = 7;
        this.campaignSaves = WorldSerializer.listSaves("campaign_");
        this.backgroundImage = new NinePatchImage(GuiAssets.getBackgroundNinePatch());
        container.addChild(this.backgroundImage);
        this.scrollPane = new ScrollPane(GuiAssets.getScrollBarStyle());
        container.addChild(this.scrollPane);
        this.contentContainer = new Container();
        this.scrollPane.setContent(this.contentContainer);
        boolean i2 = GameConfig.isDarkTheme();
        if (this.gameMode instanceof SkirmishGameMode) {
            this.winsPanel = new NinePatchImage(GuiAssets.getMenuPanelNinePatch());
            container.addChild(this.winsPanel);
            this.winsCaption = i2 ? new Label(GuiAssets.getDefaultFont(), false) : new Label(GuiAssets.getFontDokchampa15());
            this.winsCaption.a(-8355712);
            this.winsCaption.setText(i2 ? Messages.get("GamesWon[i18n]: Games Won") : Messages.getFallback("GamesWon[i18n]: Games Won"));
            container.addChild(this.winsCaption);
            this.winsValue = new Label(GuiAssets.getFontDungeon34());
            this.winsValue.a(-65536);
            this.winsValue.setText(String.valueOf(((SkirmishGameMode)this.gameMode).getWins()));
            this.winsValue.setAlign(Align.RIGHT);
            container.addChild(this.winsValue);
            this.gamesPlayedPanel = new NinePatchImage(GuiAssets.getMenuPanelNinePatch());
            container.addChild(this.gamesPlayedPanel);
            this.gamesPlayedCaption = i2 ? new Label(GuiAssets.getDefaultFont(), false) : new Label(GuiAssets.getFontDokchampa15());
            this.gamesPlayedCaption.a(-8355712);
            this.gamesPlayedCaption.setText(i2 ? Messages.get("GamesPlayed[i18n]: Games Played") : Messages.getFallback("GamesPlayed[i18n]: Games Played"));
            container.addChild(this.gamesPlayedCaption);
            this.gamesPlayedValue = new Label(GuiAssets.getFontDungeon34());
            this.gamesPlayedValue.a(-65536);
            this.gamesPlayedValue.setText(String.valueOf(((SkirmishGameMode)this.gameMode).getGamesPlayed()));
            this.gamesPlayedValue.setAlign(Align.RIGHT);
            container.addChild(this.gamesPlayedValue);
        }
        this.existingGameContainer = new Container();
        this.contentContainer.addChild(this.existingGameContainer);
        this.existingGameMessageLabel = this.createLabel(5, 5, Messages.get("ExistingGameETC[i18n]: You have an existing game running. Press [Continue] to continue playing or [Delete] to create a new game."));
        this.existingGameContainer.addChild(this.existingGameMessageLabel);
        this.buttonRowPanel = new NinePatchImage(GuiAssets.getRowNinePatch());
        this.buttonRowPanel.setHeight(55.0f);
        this.existingGameContainer.addChild(this.buttonRowPanel);
        this.continueRowHighlight = new Image(GuiAssets.getRowHighlightRegion());
        this.existingGameContainer.addChild(this.continueRowHighlight);
        this.deleteRowHighlight = new Image(GuiAssets.getRowHighlightRegion());
        this.existingGameContainer.addChild(this.deleteRowHighlight);
        this.continueButton = this.createButton(1, 0, 0, Messages.get("Continue[i18n]: Continue"));
        this.existingGameContainer.addChild(this.continueButton);
        this.deleteButton = this.createButton(2, 0, 0, Messages.get("Delete[i18n]: Delete"));
        this.existingGameContainer.addChild(this.deleteButton);
        this.optionsContainer = new Container();
        this.contentContainer.addChild(this.optionsContainer);
        this.mapListBox = this.createListBox(100, 5, 5);
        String[] stringArray = new String[this.skirmishSaves.length + 3 * this.randomMapVariants + this.campaignSaves.length];
        int n2 = 0;
        while (n2 < stringArray.length) {
            if (n2 < this.skirmishSaves.length) {
                String string = this.skirmishSaves[n2].b;
                if (GameConfig.isDarkTheme()) {
                    if (string.indexOf("Map") >= 0) {
                        n = string.indexOf("Map");
                        i7 = "Map".length();
                        string = String.valueOf(string.substring(0, n)) + Messages.get("Map[i18n]: Map") + string.substring(n + i7);
                    }
                    if (string.indexOf("Supremacy") >= 0) {
                        n = string.indexOf("Supremacy");
                        i7 = "Supremacy".length();
                        string = String.valueOf(string.substring(0, n)) + Messages.get("Supremacy[i18n]: Supremacy") + string.substring(n + i7);
                    }
                    if (string.indexOf("Flag Battle") >= 0) {
                        n = string.indexOf("Flag Battle");
                        i7 = "Flag Battle".length();
                        string = String.valueOf(string.substring(0, n)) + Messages.get("FlagBattle[i18n]: Flag Battle") + string.substring(n + i7);
                    }
                }
                stringArray[n2] = string;
            } else if (n2 < this.skirmishSaves.length + 1 * this.randomMapVariants) {
                int n3 = n2 - this.skirmishSaves.length - 0 * this.randomMapVariants;
                n = 32 + 8 * n3;
                stringArray[n2] = n3 < this.inMemoryRandomMapCount ? Messages.format("RandomSupremacyXY[i18n]: Random: Supremacy {0}x{0}", n) : Messages.format("RandomSupremacyXYMem[i18n]: Random: Supremacy {0}x{0} (mem?)", n);
            } else if (n2 < this.skirmishSaves.length + 2 * this.randomMapVariants) {
                int n4 = n2 - this.skirmishSaves.length - 1 * this.randomMapVariants;
                n = 32 + 8 * n4;
                stringArray[n2] = n4 < this.inMemoryRandomMapCount ? Messages.format("RandomCaptureFlagXY[i18n]: Random: Flag Battle {0}x{0}", n) : Messages.format("RandomCaptureFlagXYMem[i18n]: Random: Flag Battle {0}x{0} (mem?)", n);
            } else if (n2 < this.skirmishSaves.length + 3 * this.randomMapVariants) {
                int n5 = n2 - this.skirmishSaves.length - 2 * this.randomMapVariants;
                n = 32 + 8 * n5;
                stringArray[n2] = n5 < this.inMemoryRandomMapCount ? Messages.format("RandomDefenseXY[i18n]: Random: Defense {0}x{0}", n) : Messages.format("RandomDefenseXYMem[i18n]: Random: Defense {0}x{0} (mem?)", n);
            } else {
                stringArray[n2] = GameConfig.cleanContent ? Messages.format("CampaignMissionX[i18n]: Campaign Mission {0}", n2 - this.skirmishSaves.length - 3 * this.randomMapVariants + 1) : this.campaignSaves[n2 - this.skirmishSaves.length - 3 * this.randomMapVariants].b;
            }
            ++n2;
        }
        this.mapListBox.setItems(stringArray);
        this.optionsContainer.addChild(this.mapListBox);
        this.nationListBox = this.createListBox(101, 5, 65);
        FactionList factionList = GameConfig.getMapDefinition().getFactions();
        String[] stringArray2 = new String[factionList.size() + 1];
        stringArray2[0] = Messages.format("NationX[i18n]: Nation: {0}", Messages.get("Default[i18n]: Default"));
        n = 0;
        while (n < factionList.size()) {
            stringArray2[n + 1] = Messages.format("NationX[i18n]: Nation: {0}", Messages.get(((Faction)factionList.get(n)).getName()));
            ++n;
        }
        this.nationListBox.setItems(stringArray2);
        this.optionsContainer.addChild(this.nationListBox);
        this.playersListBox = this.createListBox(102, 5, 125);
        this.playersListBox.setItems(new String[]{Messages.format("PlayersX[i18n]: Players: {0} ", Messages.get("Default[i18n]: Default")), Messages.format("PlayersX[i18n]: Players: {0} ", 2), Messages.format("PlayersX[i18n]: Players: {0} ", 3), Messages.format("PlayersX[i18n]: Players: {0} ", 4), Messages.format("PlayersX[i18n]: Players: {0} ", 5), Messages.format("PlayersX[i18n]: Players: {0} ", 6), Messages.format("PlayersX[i18n]: Players: {0} ", 7), Messages.format("PlayersX[i18n]: Players: {0} ", 8)});
        this.optionsContainer.addChild(this.playersListBox);
        this.teamsListBox = this.createListBox(103, 5, 185);
        this.teamsListBox.setItems(new String[]{Messages.format("TeamsX[i18n]: Teams: {0}", Messages.get("Default[i18n]: Default")), Messages.format("TeamsX[i18n]: Teams: {0}", Messages.get("None[i18n]: None")), Messages.format("TeamsX[i18n]: Teams: {0}", 2), Messages.format("TeamsX[i18n]: Teams: {0}", 3), Messages.format("TeamsX[i18n]: Teams: {0}", 4)});
        this.optionsContainer.addChild(this.teamsListBox);
        this.viewListBox = this.createListBox(104, 5, 245);
        this.viewListBox.setItems(new String[]{Messages.get("ViewDefault[i18n]: View: Default"), Messages.get("ViewExplorationAndFog[i18n]: View: Exploration + Fog"), Messages.get("ViewExplorationOnly[i18n]: View: Exploration Only"), Messages.get("ViewFogOnly[i18n]: View: Fog Only"), Messages.get("ViewNoExplorationFog[i18n]: View: No Exploration/Fog")});
        this.optionsContainer.addChild(this.viewListBox);
        this.commandoListBox = this.createListBox(105, 5, 305);
        this.commandoListBox.setItems(new String[]{Messages.format("CommandoUnitX[i18n]: Commando Unit: {0}", Messages.get("Default[i18n]: Default")), Messages.format("CommandoUnitX[i18n]: Commando Unit: {0}", Messages.get("Enabled[i18n]: Enabled")), Messages.format("CommandoUnitX[i18n]: Commando Unit: {0}", Messages.get("Disabled[i18n]: Disabled"))});
        this.optionsContainer.addChild(this.commandoListBox);
        this.moneyListBox = this.createListBox(106, 5, 365);
        this.moneyListBox.setItems(new String[]{Messages.format("MoneyX[i18n]: Money: {0}", Messages.get("Default[i18n]: Default")), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(0L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(2500L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(5000L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(7500L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(10000L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(12500L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(15000L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(17500L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(20000L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(22500L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(25000L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(27500L)), Messages.format("MoneyX[i18n]: Money: {0}", TextFormatter.formatAmount(30000L))});
        this.optionsContainer.addChild(this.moneyListBox);
        this.incomeListBox = this.createListBox(107, 5, 425);
        this.incomeListBox.setItems(new String[]{Messages.format("IncomeX[i18n]: Income: {0}", Messages.get("Default[i18n]: Default")), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(0L)) + "/t"), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(50L)) + "/t"), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(100L)) + "/t"), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(150L)) + "/t"), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(200L)) + "/t"), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(250L)) + "/t"), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(300L)) + "/t"), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(350L)) + "/t"), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(400L)) + "/t"), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(450L)) + "/t"), Messages.format("IncomeX[i18n]: Income: {0}", String.valueOf(TextFormatter.formatAmount(500L)) + "/t")});
        this.optionsContainer.addChild(this.incomeListBox);
        this.timeLimitListBox = this.createListBox(103, 5, 495);
        this.timeLimitListBox.setItems(new String[]{Messages.format("TimeLimitX[i18n]: Time Limit: {0}", Messages.get("Default[i18n]: Default")), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", Messages.get("None[i18n]: None")), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(300.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(600.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(900.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(1200.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(1500.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(1800.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(2100.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(2400.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(2700.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(3000.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(3300.0f)), Messages.format("TimeLimitX[i18n]: Time Limit: {0}", TextFormatter.formatTime(3600.0f))});
        this.optionsContainer.addChild(this.timeLimitListBox);
        this.difficultyListBox = this.createListBox(199, 5, 555);
        String[] stringArray3 = new String[Difficulty.values().length];
        i7 = 0;
        while (i7 < Difficulty.values().length) {
            stringArray3[i7] = Messages.format("DifficultyX[i18n]: Difficulty: {0}", Messages.get(Difficulty.values()[i7].getName()));
            ++i7;
        }
        this.difficultyListBox.setItems(stringArray3);
        this.difficultyListBox.setSelectedIndex(Difficulty.Normal.ordinal());
        this.optionsContainer.addChild(this.difficultyListBox);
        this.actionRowPanel = new NinePatchImage(GuiAssets.getRowNinePatch());
        this.actionRowPanel.setY(615.0f);
        this.actionRowPanel.setHeight(55.0f);
        this.optionsContainer.addChild(this.actionRowPanel);
        this.playRowHighlight = new Image(GuiAssets.getRowHighlightRegion());
        this.playRowHighlight.setY(this.actionRowPanel.getY() + 2.0f);
        this.optionsContainer.addChild(this.playRowHighlight);
        this.randomizeRowHighlight = new Image(GuiAssets.getRowHighlightRegion());
        this.randomizeRowHighlight.setY(this.actionRowPanel.getY() + 2.0f);
        this.optionsContainer.addChild(this.randomizeRowHighlight);
        this.playButton = this.createButton(200, 0, (int)this.playRowHighlight.getY() + 2, Messages.get("Play[i18n]: Play"));
        this.optionsContainer.addChild(this.playButton);
        this.randomizeButton = this.createButton(99, 0, (int)this.randomizeRowHighlight.getY() + 2, Messages.get("Randomize[i18n]: Randomize"));
        this.optionsContainer.addChild(this.randomizeButton);
        i7 = this.gameMode.hasSavedGame() ? 1 : 0;
        this.existingGameContainer.setVisible(i7 != 0);
        this.optionsContainer.setVisible(i7 == 0);
        return true;
    }

    @Override
    protected void createContent(Container container) {
        float f4;
        float f3;
        float f2;
        this.setupScrollPane(container, this.backgroundImage, this.scrollPane, this.gameMode instanceof SkirmishGameMode ? 70 : 0);
        if (this.gameMode instanceof SkirmishGameMode) {
            f2 = 25.0f;
            f3 = this.backgroundImage.getX() + f2;
            f4 = this.backgroundImage.getWidth() - f2 - f2;
            this.winsPanel.setX(f3);
            this.winsPanel.setY(this.backgroundImage.getY() - 75.0f);
            this.winsPanel.setWidth((f4 - f2) / 2.0f);
            this.winsPanel.setHeight(65.0f);
            this.gamesPlayedPanel.setX(this.winsPanel.getX() + this.winsPanel.getWidth() + f2);
            this.gamesPlayedPanel.setY(this.winsPanel.getY());
            this.gamesPlayedPanel.setWidth((f4 - f2) / 2.0f);
            this.gamesPlayedPanel.setHeight(65.0f);
            this.winsCaption.setX(this.winsPanel.getX() + 30.0f);
            this.winsCaption.setY(this.winsPanel.getY() + 23.0f);
            this.winsValue.setMaxWidth((int)this.winsPanel.getWidth() - 30);
            this.winsValue.setX(this.winsPanel.getX());
            this.winsValue.setY(this.winsPanel.getY() + 8.0f);
            this.gamesPlayedCaption.setX(this.gamesPlayedPanel.getX() + 30.0f);
            this.gamesPlayedCaption.setY(this.gamesPlayedPanel.getY() + 23.0f);
            this.gamesPlayedValue.setMaxWidth((int)this.gamesPlayedPanel.getWidth() - 30);
            this.gamesPlayedValue.setX(this.gamesPlayedPanel.getX());
            this.gamesPlayedValue.setY(this.gamesPlayedPanel.getY() + 8.0f);
        }
        f2 = this.scrollPane.getWidth();
        if (this.playButton.getY() + this.playButton.getHeight() >= this.scrollPane.getHeight()) {
            f2 -= (float)this.scrollPane.getScrollBarWidth();
        }
        this.mapListBox.setLabelMaxWidth(Math.round(f2 - (float)this.mapListBox.getArrowWidth() - 10.0f));
        this.nationListBox.setLabelMaxWidth(Math.round(f2 - (float)this.nationListBox.getArrowWidth() - 10.0f));
        this.playersListBox.setLabelMaxWidth(Math.round(f2 - (float)this.playersListBox.getArrowWidth() - 10.0f));
        this.teamsListBox.setLabelMaxWidth(Math.round(f2 - (float)this.teamsListBox.getArrowWidth() - 10.0f));
        this.viewListBox.setLabelMaxWidth(Math.round(f2 - (float)this.viewListBox.getArrowWidth() - 10.0f));
        this.commandoListBox.setLabelMaxWidth(Math.round(f2 - (float)this.commandoListBox.getArrowWidth() - 10.0f));
        this.moneyListBox.setLabelMaxWidth(Math.round(f2 - (float)this.moneyListBox.getArrowWidth() - 10.0f));
        this.incomeListBox.setLabelMaxWidth(Math.round(f2 - (float)this.incomeListBox.getArrowWidth() - 10.0f));
        this.timeLimitListBox.setLabelMaxWidth(Math.round(f2 - (float)this.timeLimitListBox.getArrowWidth() - 10.0f));
        this.difficultyListBox.setLabelMaxWidth(Math.round(f2 - (float)this.difficultyListBox.getArrowWidth() - 10.0f));
        f3 = this.playRowHighlight.getWidth() + this.randomizeRowHighlight.getWidth();
        f4 = (f2 - f3 - 8.0f) / 2.0f;
        this.actionRowPanel.setWidth(f2);
        this.playRowHighlight.setX(f4);
        this.randomizeRowHighlight.setX(f4 + this.playRowHighlight.getWidth() + 8.0f);
        this.playButton.setX(this.playRowHighlight.getX() + 2.0f);
        this.randomizeButton.setX(this.randomizeRowHighlight.getX() + 2.0f);
        this.existingGameMessageLabel.setMaxWidth(Math.round(this.scrollPane.getWidth() - (float)this.scrollPane.getScrollBarWidth() - 10.0f));
        float f5 = this.scrollPane.getWidth();
        if ((float)(this.existingGameMessageLabel.getTextWidth() + 30) + this.continueButton.getHeight() >= this.scrollPane.getHeight()) {
            f5 -= (float)this.scrollPane.getScrollBarWidth();
        }
        float f6 = this.continueRowHighlight.getWidth() + this.continueRowHighlight.getWidth();
        float f7 = (f5 - f6 - 8.0f) / 2.0f;
        this.buttonRowPanel.setY((float)(this.existingGameMessageLabel.getTextWidth() + 15));
        this.buttonRowPanel.setWidth(f5);
        this.continueRowHighlight.setX(f7);
        this.continueRowHighlight.setY(this.buttonRowPanel.getY() + 2.0f);
        this.deleteRowHighlight.setX(f7 + this.continueRowHighlight.getWidth() + 8.0f);
        this.deleteRowHighlight.setY(this.buttonRowPanel.getY() + 2.0f);
        this.continueButton.setX(this.continueRowHighlight.getX() + 2.0f);
        this.continueButton.setY(this.continueRowHighlight.getY() + 2.0f);
        this.deleteButton.setX(this.deleteRowHighlight.getX() + 2.0f);
        this.deleteButton.setY(this.deleteRowHighlight.getY() + 2.0f);
        container.pack();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    protected BaseScreen handleWidgetAction(Widget widget) {
        int i2 = (Integer)widget.getData();
        switch (i2) {
            case 0: {
                return new MainMenuScreen(this);
            }
            case 1: {
                return new LoadGameScreen(this.gameMode);
            }
            case 2: {
                String string = Messages.getFallback("DeleteGame[i18n]: Delete Game");
                String string2 = Messages.get("DeleteGameETC[i18n]: Do you really want to delete the current game?");
                String[] stringArray = new String[]{Messages.get("Delete[i18n]: Delete"), Messages.get("Cancel[i18n]: Cancel")};
                this.showDialog(1, string, string2, stringArray);
                return null;
            }
            case 99: {
                int n = (int)(MathHelper.randomFloat() * (float)this.mapListBox.getItems().length);
                if (n >= this.skirmishSaves.length) {
                    if (n < this.skirmishSaves.length + 1 * this.randomMapVariants) {
                        n = this.skirmishSaves.length + 0 * this.randomMapVariants + (n - this.skirmishSaves.length - 0 * this.randomMapVariants) % this.inMemoryRandomMapCount;
                    } else if (n < this.skirmishSaves.length + 2 * this.randomMapVariants) {
                        n = this.skirmishSaves.length + 1 * this.randomMapVariants + (n - this.skirmishSaves.length - 1 * this.randomMapVariants) % this.inMemoryRandomMapCount;
                    } else if (n < this.skirmishSaves.length + 3 * this.randomMapVariants) {
                        n = this.skirmishSaves.length + 2 * this.randomMapVariants + (n - this.skirmishSaves.length - 2 * this.randomMapVariants) % this.inMemoryRandomMapCount;
                    }
                }
                this.mapListBox.setSelectedIndex(n);
                this.nationListBox.setSelectedIndex((int)(MathHelper.randomFloat() * (float)this.nationListBox.getItems().length));
                this.playersListBox.setSelectedIndex((int)(MathHelper.randomFloat() * (float)this.playersListBox.getItems().length));
                this.teamsListBox.setSelectedIndex((int)(MathHelper.randomFloat() * (float)this.teamsListBox.getItems().length));
                this.viewListBox.setSelectedIndex((int)(MathHelper.randomFloat() * (float)this.viewListBox.getItems().length));
                this.commandoListBox.setSelectedIndex((int)(MathHelper.randomFloat() * (float)this.commandoListBox.getItems().length));
                this.moneyListBox.setSelectedIndex((int)(MathHelper.randomFloat() * (float)this.moneyListBox.getItems().length));
                this.incomeListBox.setSelectedIndex((int)(MathHelper.randomFloat() * (float)this.incomeListBox.getItems().length));
                return null;
            }
            case 100: {
                return null;
            }
            case 101: {
                return null;
            }
            case 102: {
                return null;
            }
            case 103: {
                return null;
            }
            case 104: {
                return null;
            }
            case 105: {
                return null;
            }
            case 106: {
                return null;
            }
            case 107: {
                return null;
            }
            case 199: {
                return null;
            }
            case 200: {
                if (!GameConfig.isLiteMode() && !GameConfig.isMacBuild()) {
                    int i25;
                    World world;
                    String n5;
                    float f21;
                    boolean i20;
                    boolean i19;
                    boolean i18;
                    long l16;
                    boolean i15;
                    long l13;
                    boolean i11;
                    boolean i10;
                    boolean i9;
                    boolean i8;
                    int i7;
                    boolean i6;
                    int n;
                    boolean bl;
                    Faction faction = this.nationListBox.getSelectedIndex() == 0 ? null : (Faction)GameConfig.getMapDefinition().getFactions().get(this.nationListBox.getSelectedIndex() - 1);
                    if (this.playersListBox.getSelectedIndex() == 0) {
                        bl = true;
                        n = 4;
                    } else {
                        bl = false;
                        n = this.playersListBox.getSelectedIndex() + 1;
                    }
                    if (this.teamsListBox.getSelectedIndex() == 0) {
                        i6 = true;
                        i7 = 0;
                    } else {
                        i6 = false;
                        int n2 = i7 = this.teamsListBox.getSelectedIndex() == 1 ? 0 : this.teamsListBox.getSelectedIndex();
                        if (i7 > n) {
                            i7 = n;
                        }
                    }
                    if (this.viewListBox.getSelectedIndex() == 0) {
                        i8 = true;
                        i9 = false;
                        i10 = true;
                        i11 = false;
                    } else {
                        int n3 = this.viewListBox.getSelectedIndex();
                        i8 = false;
                        i9 = n3 == 1 || n3 == 2;
                        i10 = false;
                        boolean bl2 = i11 = n3 == 1 || n3 == 3;
                    }
                    if (this.moneyListBox.getSelectedIndex() == 0) {
                        boolean bl3 = true;
                        l13 = 0L;
                    } else {
                        boolean bl4 = false;
                        l13 = (this.moneyListBox.getSelectedIndex() - 1) * 2500;
                    }
                    if (this.incomeListBox.getSelectedIndex() == 0) {
                        i15 = true;
                        l16 = 0L;
                    } else {
                        i15 = false;
                        l16 = (this.incomeListBox.getSelectedIndex() - 1) * 50;
                    }
                    if (this.commandoListBox.getSelectedIndex() == 0) {
                        i18 = true;
                        i19 = false;
                    } else {
                        i18 = false;
                        boolean bl5 = i19 = this.commandoListBox.getSelectedIndex() == 1;
                    }
                    if (this.timeLimitListBox.getSelectedIndex() == 0) {
                        i20 = true;
                        f21 = 0.0f;
                    } else {
                        i20 = false;
                        f21 = (float)(this.timeLimitListBox.getSelectedIndex() - 1) * 300.0f;
                    }
                    Difficulty difficulty = Difficulty.values()[this.difficultyListBox.getSelectedIndex()];
                    if (this.mapListBox.getSelectedIndex() < this.skirmishSaves.length) {
                        n5 = this.skirmishSaves[this.mapListBox.getSelectedIndex()].a;
                        world = WorldSerializer.load(n5);
                    } else if (this.mapListBox.getSelectedIndex() < this.skirmishSaves.length + 1 * this.randomMapVariants) {
                        int n6 = this.mapListBox.getSelectedIndex() - this.skirmishSaves.length - 0 * this.randomMapVariants;
                        i25 = 32 + 8 * n6;
                        world = World.create(GameConfig.getMapDefinition(), "Supremacy " + i25 + "x" + i25, ScenarioType.Supremacy, i25, i25, n);
                    } else if (this.mapListBox.getSelectedIndex() < this.skirmishSaves.length + 2 * this.randomMapVariants) {
                        int n4 = this.mapListBox.getSelectedIndex() - this.skirmishSaves.length - 1 * this.randomMapVariants;
                        i25 = 32 + 8 * n4;
                        world = World.create(GameConfig.getMapDefinition(), "Capture the Flag " + i25 + "x" + i25, ScenarioType.CaptureTheFlag, i25, i25, n);
                    } else if (this.mapListBox.getSelectedIndex() < this.skirmishSaves.length + 3 * this.randomMapVariants) {
                        int n6 = this.mapListBox.getSelectedIndex() - this.skirmishSaves.length - 2 * this.randomMapVariants;
                        i25 = 32 + 8 * n6;
                        world = World.create(GameConfig.getMapDefinition(), "Defense " + i25 + "x" + i25, ScenarioType.Survival, i25, i25, n);
                    } else {
                        n5 = this.campaignSaves[this.mapListBox.getSelectedIndex() - this.skirmishSaves.length - 3 * this.randomMapVariants].a;
                        world = WorldSerializer.load(n5);
                    }
                    if (this.gameMode instanceof MultiplayerGameMode) {
                        int n7 = 0;
                        while (n7 < n) {
                            world.activateHumanPlayer();
                            ++n7;
                        }
                    } else {
                        world.activateHumanPlayer(faction);
                    }
                    world.setupPlayers(bl, n, i6, i7, i18, i19, (this.moneyListBox.getSelectedIndex() == 0), l13, i15, l16, i8, i9, i10, i11, true, null, i20, f21, difficulty);
                    this.gameMode.startSession(world);
                    return new LoadGameScreen(this.gameMode);
                }
                return this.showPurchaseDialog(0);
            }
        }
        OsfLog.error("Action not implemented: " + i2);
        return null;
    }

    @Override
    protected BaseScreen handleAction(int i1, int i2) {
        if (i1 == 0) {
            return this.handlePurchaseAction(i2);
        }
        if (i1 == 1) {
            switch (i2) {
                case 0: {
                    this.gameMode.clearSaves();
                    this.existingGameContainer.setVisible(false);
                    this.optionsContainer.setVisible(true);
                    this.layout();
                    return null;
                }
                case 1: {
                    return null;
                }
            }
            OsfLog.error("Action not implemented: " + i2);
            return null;
        }
        OsfLog.error("Reference not implemented: " + i1);
        return null;
    }
}



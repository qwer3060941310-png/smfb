/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.screen.BaseMenuScreen;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.MainMenuScreen;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.NinePatchImage;
import com.desertstormfront.ui.ScrollPane;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import com.noblemaster.lib.data.Language;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.log.OsfLog;

public final class ManualScreen
extends BaseMenuScreen {
    private NinePatchImage backgroundImage;
    private ScrollPane scrollPane;
    private Label textLabel;

    public ManualScreen(BaseMenuScreen baseMenuScreen) {
        super(baseMenuScreen);
    }

    @Override
    protected boolean isBackEnabled(Container container) {
        this.backgroundImage = new NinePatchImage(GuiAssets.getBackgroundNinePatch());
        container.addChild(this.backgroundImage);
        this.scrollPane = new ScrollPane(GuiAssets.getScrollBarStyle());
        container.addChild(this.scrollPane);
        this.textLabel = this.createLabel(0, 0, String.valueOf(Messages.get("Manual[i18n]: Manual")) + " - " + GameConfig.getTitle() + "\n" + "\n" + GameConfig.getAppOverview() + "\n" + "\n" + GameConfig.getUnitsInfo() + "\n" + "\n" + GameConfig.getStructuresInfo() + "\n" + "\n" + GameConfig.getMoveablesInfo() + "\n" + "\n" + Messages.get("InformationHostingETC[i18n]: Mobile units that are hosted within a structure or one of the transport units are repaired (and refueled) automatically. Air units that are set to auto-navigate conduct reconnaissance and attack enemy units within range on their own.Please note each mobile unit displays 4 mini strength bars on the input panel that represent the unit's strengths and weeknesses against (a) ground (b) air (c) sea and (d) submergible enemies.") + "\n" + "\n" + Messages.get("InformationFunctionsETC[i18n]: GUI Functions (i.e. buttons):\n - MOVE: move/attack\n - PATROL: patrol between 2 points\n - REPAIR: repair & come back\n - STOP: stops a unit\n - GROUP: group units\n - AUTO: single unit auto-attack/patrol\n - mini MOVE: unload all units\n - mini AUTO: all units auto-attack/patrol\n - mini RALLY: rally point for structures\n - mini SPLIT: ungroup units\n - Squad [1]-[8] click: select/create squad\n - Squad [1]-[8] double-click: center screen\n - Unit HUD [x]: close unit HUD\n") + "\n" + "\n" + Messages.get("InformationControlsETC[i18n]: Controls (Mouse and Touch):\n - Key ESC: pause game\n - Key A/S/D/F: scroll screen\n - Key 1-8: select squad\n - Key SHIFT 1-8: set as squad squad\n - Key CTRL 1-8: add to squad squad\n - Key Z: stop unit\n - Key X: repair unit\n - Key C: unit patrol\n - Key Q: group units\n - Key E: un-group units\n - Key SPACEBAR: deselect\n - Mouse LEFT: select\n - Mouse RIGHT (units): move/attack\n - Mouse RIGHT (structure): rally point\n - Mouse RIGHT + SHIFT: unload all\n - Mouse LEFT double-click: select same\n - Mouse (SHIFT) LEFT drag: box select\n - Mouse CTRL LEFT drag: add to selection\n - Touch: (1) select (2) move/attack\n - Touch double-click: stop unit\n - Touch drag (after squad sel.): grouping\n\nPlease note, keys and mouse settings can be changed in the [Options] screen. Select mouse \"RTS Mode\" for best experience on Desktop. Disable \"RTS Mode\" for touch screen devices.") + "\n" + "\n" + Messages.format("InformationXPPointsETC[i18n]: Experience Points (XP) are recorded for each unit and are gained by eliminating enemy troops. Destroying {0} hostiles will move the unit up to 1-Star level. Destroying {1} hostiles to 2-Star and {2} to 3-Star level. Each XP level gives both a 25% offense and defense bonus.", 4, 9, 15) + "\n" + "\n" + Messages.format("InformationMultiplayerETC[i18n]: If playing multiplayer games using WiFi or cable is recommended. Using 3G or the like is possible if your provider supports streaming data. If your provider does not support streaming data, it will run incredibly delayed. Please also try during off peak hours (very very late night) if your only option is to play via 2G or 3G. If you play over a LAN, your game should run at about the same responsiveness as playing single player. If you are in a local network (LAN), and you would like to host games over the internet, make sure you enable port forwarding for port {0} to your computer (port adjustable in [Options] screen). The game also tries to open the port via UPnP for you automatically, but depending on your router it might fail. Please be aware that port forwarding might not be possible in a company or academic network, so your only option is to join rather than host games. The game will notify you if a hosted game is only playable via LAN. If you don't receive such a message and host a game, you can safely assume that the game is playable over the internet. For questions and problems, please refer to the forums.", String.valueOf(UserConfig.getMultiplayerPort())) + "\n" + "\n" + Messages.get("InformationAIETC[i18n]: The AI is very good at micromanaging the various units. The AI's weakness is planning and coordinating large-scale attacks amonst various unit groups as well as using terrain features to its advantage. The AI is not programmed to cheat, but will get more resource if difficulty \"Hard\" or \"Extreme\" is selected. The resources are reduced for \"Easy\" difficulty. If you choose \"Normal\", it's all fair game!") + "\n" + "\n" + Messages.get("FurtherInformationETC[i18n]: Please don't forget to visit our website for the latest news and updates. Also visit our forums for discussions and more.") + "\n" + "\n" + GameConfig.getSubtitle() + " " + GameConfig.getVersion() + "\n" + GameConfig.getWebsiteUrl());
        this.scrollPane.setContent(this.textLabel);
        this.showWebsiteAndForumButtons();
        return true;
    }

    @Override
    protected void createContent(Container container) {
        this.setupScrollPane(container, this.backgroundImage, this.scrollPane);
        this.textLabel.setMaxWidth(Math.round(this.scrollPane.getWidth() - (float)this.scrollPane.getScrollBarWidth()));
        container.pack();
    }

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
                return new MainMenuScreen(this);
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


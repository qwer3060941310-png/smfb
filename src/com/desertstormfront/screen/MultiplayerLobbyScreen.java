/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.config.GameConfig;
import com.desertstormfront.screen.BaseMenuScreen;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.LoadGameScreen;
import com.desertstormfront.screen.MainMenuScreen;
import com.desertstormfront.screen.SkirmishSetupScreen;
import com.desertstormfront.session.impl.MultiplayerGameMode;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.NinePatchImage;
import com.desertstormfront.ui.ScrollPane;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import com.noblemaster.lib.data.Language;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.net.match.MatchList;
import com.noblemaster.lib.net.match.MatchRecord;
import java.util.ArrayList;
import java.util.List;

public final class MultiplayerLobbyScreen
extends BaseMenuScreen
implements Runnable {
    private String initialMessage;
    private NinePatchImage backgroundImage;
    private ScrollPane scrollPane;
    private Container contentContainer;
    private List lobbyButtons;
    private List lobbyLabels;
    private List lobbyDetailLabels;
    private Label statusLabel;
    private String externalAddress;
    private Object lock;
    private boolean hasScanned;
    private MatchList matchList;
    private Thread scanThread;

    public MultiplayerLobbyScreen() {
        this((String)null);
    }

    public MultiplayerLobbyScreen(Exception exception) {
        this(exception.getMessage());
    }

    public MultiplayerLobbyScreen(String string) {
        this.initialMessage = string;
    }

    public MultiplayerLobbyScreen(BaseMenuScreen baseMenuScreen) {
        super(baseMenuScreen);
    }

    @Override
    protected boolean isBackEnabled(Container container) {
        String[] stringArray;
        Object object;
        this.backgroundImage = new NinePatchImage(GuiAssets.getBackgroundNinePatch());
        container.addChild(this.backgroundImage);
        this.scrollPane = new ScrollPane(GuiAssets.getScrollBarStyle());
        container.addChild(this.scrollPane);
        this.contentContainer = new Container();
        this.scrollPane.setContent(this.contentContainer);
        this.lobbyButtons = new ArrayList();
        this.lobbyLabels = new ArrayList();
        this.lobbyDetailLabels = new ArrayList();
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
        int n = 0;
        while (n < 64) {
            Button lobbyButton = new Button(n == 0 ? buttonStyle : buttonStyle2);
            lobbyButton.setData(n == 0 ? Integer.valueOf(1) : null);
            lobbyButton.setX(5.0f);
            this.lobbyButtons.add(lobbyButton);
            Label lobbyLabel = this.createLabel(Math.round(lobbyButton.getX() + (float)lobbyButton.getStyle().width + 5.0f), 0);
            this.lobbyLabels.add(lobbyLabel);
            Label label = this.createLabel(Math.round(lobbyButton.getX() + (float)lobbyButton.getStyle().width + 5.0f), 0);
            label.a(-7303024);
            label.setFont(GuiAssets.getFontDokchampa15());
            this.lobbyDetailLabels.add(label);
            this.contentContainer.addChild(lobbyButton);
            this.contentContainer.addChild(lobbyLabel);
            this.contentContainer.addChild(label);
            lobbyButton.setVisible(n == 0);
            lobbyLabel.setVisible(n == 0);
            label.setVisible(n == 0);
            if (n == 0) {
                ((Label)this.lobbyLabels.get(0)).setText(Messages.get("HostAGame[i18n]: Host a Game"));
                String string = Messages.getLanguage() == Language.SPANISH ? Messages.format("GameWillBeHostedAsXGame[i18n]: Game will be hosted as \"Game: {0}\".", System.getProperty("user.name")) : Messages.formatFallback("GameWillBeHostedAsXGame[i18n]: Game will be hosted as \"Game: {0}\".", System.getProperty("user.name"));
                ((Label)this.lobbyDetailLabels.get(0)).setText(string);
            }
            ++n;
        }
        this.statusLabel = this.createLabel(0, 0, Messages.get("ScanningGames[i18n]: Scanning for Games..."));
        this.statusLabel.setAlign(Align.CENTER);
        this.statusLabel.a(-7303024);
        this.contentContainer.addChild(this.statusLabel);
        this.lock = new Object();
        this.hasScanned = false;
        this.matchList = new MatchList();
        this.scanThread = new Thread(this);
        this.scanThread.start();
        if (this.initialMessage != null) {
            String string = Messages.getFallback("ConnectionMessage[i18n]: Connection Message");
            if (GameConfig.getMultiplayerGameMode().hasSavedGame()) {
                object = String.valueOf(Messages.get(this.initialMessage)) + "\n\n" + Messages.get("ReconnectMultiplayerGameETC[i18n]: Do you want to try to re-connect or re-setup the game?");
                stringArray = new String[]{Messages.get("Resume[i18n]: Resume"), Messages.get("Delete[i18n]: Delete")};
                this.showDialog(1, string, (String)object, stringArray);
            } else {
                object = Messages.get(this.initialMessage);
                stringArray = new String[]{Messages.get("OK[i18n]: OK")};
                this.showDialog(0, string, (String)object, stringArray);
            }
            this.initialMessage = null;
        } else if (GameConfig.getMultiplayerGameMode().hasSavedGame()) {
            String string = Messages.getFallback("ExistingGame[i18n]: Existing Game");
            object = Messages.get("ExistingMultiplayerGameETC[i18n]: You have an existing multiplayer game. Do you want to resume the game?");
            stringArray = new String[]{Messages.get("Resume[i18n]: Resume"), Messages.get("Delete[i18n]: Delete")};
            this.showDialog(1, string, (String)object, stringArray);
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public BaseScreen update() {
        MatchList matchList;
        Object object = this.lock;
        synchronized (object) {
            matchList = this.matchList;
        }
        int n = 0;
        int i3 = 0;
        int i4 = 1;
        while (i4 < this.lobbyButtons.size()) {
            boolean i5 = false;
            while (!i5 && n < matchList.size()) {
                MatchRecord matchRecord = (MatchRecord)matchList.get(n);
                String string = Integer.parseInt(matchRecord.getLabel().substring(0, matchRecord.getLabel().indexOf("|"))) != 4 ? null : (this.externalAddress.equals(matchRecord.getExternalAddress()) ? String.valueOf(Messages.get("LANGameETC[i18n]: LAN Game")) + ": " + matchRecord.getLabel().substring(matchRecord.getLabel().indexOf("|") + 1) : (matchRecord.isPortOpen() ? String.valueOf(Messages.get("InternetGame[i18n]: Internet Game")) + ": " + matchRecord.getLabel().substring(matchRecord.getLabel().indexOf("|") + 1) : null));
                if (string != null) {
                    ((Button)this.lobbyButtons.get(i4)).setVisible(true);
                    ((Button)this.lobbyButtons.get(i4)).setData(matchRecord);
                    ((Label)this.lobbyLabels.get(i4)).setText(matchRecord.getTitle());
                    ((Label)this.lobbyLabels.get(i4)).setVisible(true);
                    ((Label)this.lobbyDetailLabels.get(i4)).setText(string);
                    ((Label)this.lobbyDetailLabels.get(i4)).setVisible(true);
                    ++i3;
                    i5 = true;
                }
                ++n;
            }
            if (!i5) {
                ((Button)this.lobbyButtons.get(i4)).setVisible(false);
                ((Label)this.lobbyLabels.get(i4)).setVisible(false);
                ((Label)this.lobbyDetailLabels.get(i4)).setVisible(false);
            }
            ++i4;
        }
        this.statusLabel.setVisible(i3 == 0);
        if (this.hasScanned) {
            this.statusLabel.setText(Messages.get("NoGamesFoundETC[i18n]: No Games Found - Scanning..."));
        }
        this.layout();
        return super.update();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        this.externalAddress = this.getAddressProvider().getExternalAddress();
        Thread thread = Thread.currentThread();
        while (this.scanThread == thread) {
            try {
                MatchList matchList = GameConfig.getMatchMakingClient().listMatches(GameConfig.getProductId());
                Object object = this.lock;
                synchronized (object) {
                    this.matchList = matchList;
                }
                this.hasScanned = true;
            }
            catch (Exception exception) {
                OsfLog.info("Error retrieving matches list: " + exception);
                OsfLog.logException(exception);
            }
            try {
                Thread.sleep(5000L);
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    @Override
    protected void createContent(Container container) {
        this.setupScrollPane(container, this.backgroundImage, this.scrollPane, 0, false);
        int i2 = 5;
        int i3 = (int)(this.scrollPane.getWidth() - (float)this.scrollPane.getScrollBarWidth());
        int i4 = 0;
        while (i4 < this.lobbyButtons.size()) {
            if (((Button)this.lobbyButtons.get(i4)).isVisible()) {
                ((Button)this.lobbyButtons.get(i4)).setY((float)i2);
                ((Label)this.lobbyLabels.get(i4)).setY((float)(i2 + 2));
                ((Label)this.lobbyDetailLabels.get(i4)).setY((float)(i2 + 25));
                i2 = (int)((float)i2 + (((Button)this.lobbyButtons.get(i4)).getHeight() + 5.0f));
            } else {
                ((Button)this.lobbyButtons.get(i4)).setY(0.0f);
                ((Label)this.lobbyLabels.get(i4)).setY(0.0f);
                ((Label)this.lobbyDetailLabels.get(i4)).setY(0.0f);
            }
            ((Label)this.lobbyDetailLabels.get(i4)).setMaxWidth(i3 - (int)((Button)this.lobbyButtons.get(0)).getWidth() - 5);
            ++i4;
        }
        if (this.statusLabel.isVisible()) {
            this.statusLabel.setX(0.0f);
            this.statusLabel.setWidth(this.scrollPane.getWidth());
            this.statusLabel.setMaxWidth((int)this.scrollPane.getWidth());
            this.statusLabel.setY(this.scrollPane.getHeight() / 2.0f);
        }
        container.pack();
        if (this.scrollPane.getContent().getY() < this.scrollPane.getHeight() - this.scrollPane.getContent().getHeight()) {
            if (this.scrollPane.getHeight() >= this.scrollPane.getContent().getHeight()) {
                this.scrollPane.getContent().setY(0.0f);
            } else {
                this.scrollPane.getContent().setY(this.scrollPane.getHeight() - this.scrollPane.getContent().getHeight());
            }
        }
    }

    @Override
    protected BaseScreen handleWidgetAction(Widget widget) {
        if (widget.getData() instanceof MatchRecord) {
            MatchRecord matchRecord = (MatchRecord)widget.getData();
            MultiplayerGameMode multiplayerGameMode = GameConfig.getMultiplayerGameMode();
            multiplayerGameMode.joinGame(this.getAddressProvider(), matchRecord);
            return new LoadGameScreen(multiplayerGameMode);
        }
        int n = (Integer)widget.getData();
        switch (n) {
            case 1: {
                return new SkirmishSetupScreen(this, GameConfig.getMultiplayerGameMode());
            }
            case 0: {
                return new MainMenuScreen(this);
            }
        }
        OsfLog.error("Action not implemented: " + n);
        return null;
    }

    @Override
    protected BaseScreen handleAction(int i1, int i2) {
        if (i1 == 0) {
            return null;
        }
        if (i1 == 1) {
            switch (i2) {
                case 0: {
                    String string = Messages.getFallback("JoinOrHost[i18n]: Join or Host");
                    String string2 = Messages.get("ExistingMultiplayerJoinOrHostETC[i18n]: Do you want to try to re-join the game or host the game?");
                    String[] stringArray = new String[]{Messages.get("Join[i18n]: Join"), Messages.get("Host[i18n]: Host")};
                    this.showDialog(2, string, string2, stringArray);
                    return null;
                }
                case 1: {
                    GameConfig.getMultiplayerGameMode().clearSaves();
                    return null;
                }
            }
            OsfLog.error("Action not implemented: " + i2);
            return null;
        }
        if (i1 == 2) {
            switch (i2) {
                case 0: {
                    MultiplayerGameMode multiplayerGameMode = GameConfig.getMultiplayerGameMode();
                    multiplayerGameMode.joinGame(this.getAddressProvider(), multiplayerGameMode.getMatchRecord());
                    return new LoadGameScreen(multiplayerGameMode);
                }
                case 1: {
                    MultiplayerGameMode multiplayerGameMode = GameConfig.getMultiplayerGameMode();
                    multiplayerGameMode.startFromSave();
                    return new LoadGameScreen(multiplayerGameMode);
                }
            }
            OsfLog.error("Action not implemented: " + i2);
            return null;
        }
        OsfLog.error("Reference not implemented: " + i1);
        return null;
    }

    @Override
    public void dispose() {
        this.scanThread = null;
        super.dispose();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.desertstormfront.config.GameConfig;
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

public final class CreditsScreen
extends BaseMenuScreen {
    private NinePatchImage backgroundImage;
    private ScrollPane scrollPane;
    private Label textLabel;

    public CreditsScreen(BaseMenuScreen baseMenuScreen) {
        super(baseMenuScreen);
    }

    @Override
    protected boolean isBackEnabled(Container container) {
        this.backgroundImage = new NinePatchImage(GuiAssets.getBackgroundNinePatch());
        container.addChild(this.backgroundImage);
        this.scrollPane = new ScrollPane(GuiAssets.getScrollBarStyle());
        container.addChild(this.scrollPane);
        this.textLabel = this.createLabel(0, 0, String.valueOf(Messages.get("Credits[i18n]: Credits")) + " - " + GameConfig.getSubtitle() + "\n" + "\n" + GameConfig.getCreditsText() + "\n" + GameConfig.getTechText() + "\n" + GameConfig.getLicenseText() + "\n" + GameConfig.getSubtitle() + " " + GameConfig.getVersion() + (GameConfig.isDebugEnabled() ? "D" : "") + "\n" + GameConfig.getWebsiteUrl() + "\n" + GameConfig.getAboutText() + "\n" + "\n" + "Build: " + GameConfig.getBuildDate().formatUtc("{yyyy}{MM}{dd}.{hh}{mm}") + "\n" + "Store: " + this.getMarket().getName() + " (" + this.getLicense().getName() + ")");
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
        OsfLog.info("Action not implemented: " + i2);
        return null;
    }

    @Override
    protected BaseScreen handleAction(int i1, int i2) {
        return null;
    }
}


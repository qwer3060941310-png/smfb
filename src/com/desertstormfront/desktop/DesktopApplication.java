/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.desertstormfront.app.GameApplication;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.desktop.DesktopLauncher;
import com.desertstormfront.desktop.DesktopPortMapper;
import com.desertstormfront.desktop.MissingGlyphDetector;
import com.desertstormfront.desktop.PlatformCursor;
import com.desertstormfront.desktop.SystemHook;
import com.noblemaster.lib.license.License;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.market.Market;
import com.noblemaster.lib.net.match.AddressProvider;
import com.noblemaster.lib.net.match.DefaultAddressProvider;
import com.noblemaster.lib.net.match.PortMappingCallback;
import java.awt.Desktop;
import java.net.URI;
import java.util.Locale;
import org.lwjgl.input.Mouse;

class DesktopApplication
extends GameApplication {
    DesktopApplication(Market market, License license) {
        super(market, license);
    }

    @Override
    public void create() {
        if (GameConfig.isDebugEnabled()) {
            boolean i1 = GameConfig.cleanContent;
            GameConfig.cleanContent = false;
            GameConfig.getGameInfo().logLocalizedNames();
            GameConfig.cleanContent = i1;
        }
        if (GameConfig.isDebugEnabled()) {
            MissingGlyphDetector.main(null);
        }
        super.create();
    }

    @Override
    public String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    @Override
    public boolean isDesktopMode() {
        return !SystemHook.isTabletMode();
    }

    @Override
    public AddressProvider getAddressProvider() {
        return new DefaultAddressProvider();
    }

    @Override
    public PortMappingCallback getPortMappingCallback() {
        return new DesktopPortMapper(this);
    }

    @Override
    public void openUrl(String string) {
        Desktop desktop;
        if (!Desktop.isDesktopSupported()) {
            OsfLog.info("Desktop is not supported (fatal)");
        }
        if (!(desktop = Desktop.getDesktop()).isSupported(Desktop.Action.BROWSE)) {
            OsfLog.info("Desktop doesn't support the browse action (fatal)");
        }
        try {
            URI uRI = new URI(string);
            desktop.browse(uRI);
        }
        catch (Exception exception) {
            OsfLog.error("Cannot open URL: " + exception);
            OsfLog.logException(exception);
        }
    }

    @Override
    public void resetCursor() {
        DesktopLauncher.resetCursor();
    }

    @Override
    public void setCustomCursor() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("data/shared/cursor.gif"));
        PlatformCursor platformCursor = DesktopLauncher.createCursor(1, 1, pixmap);
        DesktopLauncher.applyCursor(platformCursor);
    }

    @Override
    public int clampMouseX(int i1, int i2) {
        if (i1 + Mouse.getEventDX() < 12) {
            i1 = 0;
        } else if (i1 + Mouse.getEventDX() > i2 - 12) {
            i1 = i2;
        }
        return i1;
    }

    @Override
    public int clampMouseY(int i1, int i2) {
        if (i1 - Mouse.getEventDY() < 12) {
            i1 = 0;
        } else if (i1 - Mouse.getEventDY() > i2 - 12) {
            i1 = i2;
        }
        return i1;
    }
}


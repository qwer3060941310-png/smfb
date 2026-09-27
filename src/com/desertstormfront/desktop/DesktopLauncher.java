/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.desktop.BuildInfo;
import com.desertstormfront.desktop.DesktopApplication;
import com.desertstormfront.desktop.DesktopCursor;
import com.desertstormfront.desktop.PlatformCursor;
import com.desertstormfront.desktop.ShutdownHookThread;
import com.desertstormfront.desktop.SystemHook;
import com.noblemaster.lib.data.DateTime;
import com.noblemaster.lib.data.Version;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.license.AcceptedLicense;
import com.noblemaster.lib.license.ExpiringLicense;
import com.noblemaster.lib.license.License;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.market.AmazonMarket;
import com.noblemaster.lib.market.ChinaMobileMarket;
import com.noblemaster.lib.market.DarkGameMarket;
import com.noblemaster.lib.market.DesuraMarket;
import com.noblemaster.lib.market.FireFlowerMarket;
import com.noblemaster.lib.market.GamersGateMarket;
import com.noblemaster.lib.market.ITunesMarket;
import com.noblemaster.lib.market.IWinMarket;
import com.noblemaster.lib.market.ImmanitasMarket;
import com.noblemaster.lib.market.IntelMarket;
import com.noblemaster.lib.market.JoyMoaMarket;
import com.noblemaster.lib.market.MacGameStoreMarket;
import com.noblemaster.lib.market.Market;
import com.noblemaster.lib.market.NobleMasterMarket;
import com.noblemaster.lib.market.PlayismMarket;
import com.noblemaster.lib.market.ReviewMarket;
import com.noblemaster.lib.market.UbuntuMarket;
import java.awt.Frame;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public final class DesktopLauncher {
    private static boolean a = false;

    private DesktopLauncher() {
    }

    public static void main(String[] stringArray) {
        int i12;
        int i11;
        Market market;
        String string;
        SystemHook.setDisplayOrientationPreference(5);
        Frame[] frameArray = Frame.getFrames();
        String string2 = null;
        String string3 = null;
        boolean i4 = false;
        boolean i5 = false;
        int n = 0;
        while (n < stringArray.length) {
            String string4 = stringArray[n].trim();
            if (string4.startsWith("-debug")) {
                i4 = true;
            } else if (string4.startsWith("-lite")) {
                i5 = true;
            } else if (string4.startsWith("-m")) {
                string3 = string4.substring(2);
            } else if (string4.startsWith("-c")) {
                string2 = string4.substring(2).toLowerCase();
            }
            ++n;
        }
        String string5 = string = string2 != null ? string2 : BuildInfo.getProductCode();
        if (System.getProperty("ikvmmarket") != null) {
            string3 = System.getProperty("ikvmmarket");
        }
        boolean bl = false;
        if (string3 == null || string3.equals("Review")) {
            market = new ReviewMarket();
        } else if (string3.equals("NobleMaster") || string3.equals("Main")) {
            market = new NobleMasterMarket("http://www.operationstormfront.com/purchase.html");
        } else if (string3.equals("Desura")) {
            market = new DesuraMarket(string == null || string.equals("tsf") ? "Tropical Stormfront" : "Desert Stormfront");
        } else if (string3.equals("GamersGate")) {
            market = new GamersGateMarket();
        } else if (string3.equals("Ubuntu")) {
            market = new UbuntuMarket();
        } else if (string3.equals("MacGameStore")) {
            market = new MacGameStoreMarket();
        } else if (string3.equals("Immanitas")) {
            market = new ImmanitasMarket();
        } else if (string3.equals("DarkGame")) {
            market = new DarkGameMarket();
        } else if (string3.equals("FireFlower")) {
            market = new FireFlowerMarket();
        } else if (string3.equals("Amazon")) {
            market = new AmazonMarket();
        } else if (string3.equals("JoyMoa")) {
            market = new JoyMoaMarket();
        } else if (string3.equals("iWin")) {
            market = new IWinMarket();
        } else if (string3.equals("iTunes")) {
            market = new ITunesMarket();
            bl = true;
            GameConfig.cleanContent = true;
        } else if (string3.equals("Intel")) {
            market = new IntelMarket();
            GameConfig.cleanContent = true;
        } else if (string3.equals("Playism")) {
            market = new PlayismMarket();
            GameConfig.cleanContent = true;
        } else if (string3.equals("ChinaMobile")) {
            market = new ChinaMobileMarket();
            GameConfig.cleanContent = true;
        } else {
            throw new RuntimeException("Error: no market defined");
        }
        Version version = BuildInfo.getVersion();
        DateTime dateTime = BuildInfo.getBuildDate();
        if (i4) {
            i11 = 1280;
            i12 = 800;
        } else {
            i11 = LwjglApplicationConfiguration.getDesktopDisplayMode().width - 256;
            i12 = LwjglApplicationConfiguration.getDesktopDisplayMode().height - 256;
            if (i11 < 1280) {
                i11 = 1280;
            }
            if (i12 < 800) {
                i12 = 800;
            }
        }
        GameConfig.legacyDataDir = false;
        GameConfig.configure(string, version, dateTime, i4, i5, i11, i12, bl);
        License license = i5 ? new AcceptedLicense() : (market.getName().equals("Review") ? new ExpiringLicense(GameConfig.getBuildDate(), 90) : new AcceptedLicense());
        DesktopApplication desktopApplication = new DesktopApplication(market, license);
        System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");
        LwjglApplicationConfiguration lwjglApplicationConfiguration = new LwjglApplicationConfiguration();
        lwjglApplicationConfiguration.useGL30 = false;
        lwjglApplicationConfiguration.title = GameConfig.getSubtitle();
        lwjglApplicationConfiguration.width = GameConfig.getScreenWidth();
        lwjglApplicationConfiguration.height = GameConfig.getScreenHeight();
        lwjglApplicationConfiguration.backgroundFPS = 60;
        lwjglApplicationConfiguration.foregroundFPS = 60;
        lwjglApplicationConfiguration.vSyncEnabled = true;
        lwjglApplicationConfiguration.fullscreen = false;
        lwjglApplicationConfiguration.resizable = true;
        GameFile gameFile = GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "icon_128x128.png");
        lwjglApplicationConfiguration.addIcon(gameFile.getPath(), gameFile.getFileType());
        gameFile = GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "icon_64x64.png");
        lwjglApplicationConfiguration.addIcon(gameFile.getPath(), gameFile.getFileType());
        gameFile = GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "icon_32x32.png");
        lwjglApplicationConfiguration.addIcon(gameFile.getPath(), gameFile.getFileType());
        gameFile = GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "icon_16x16.png");
        lwjglApplicationConfiguration.addIcon(gameFile.getPath(), gameFile.getFileType());
        new LwjglApplication((ApplicationListener)desktopApplication, lwjglApplicationConfiguration);
        OsfLog.logSystemInfo(String.valueOf(GameConfig.getTitle()) + " " + GameConfig.getVersion().toString());
        int n2 = 0;
        while (n2 < frameArray.length) {
            frameArray[n2].setVisible(false);
            ++n2;
        }
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
        while (!a) {
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException interruptedException) {
                OsfLog.info("Error in getdown wait loop:" + interruptedException);
                OsfLog.logException(interruptedException);
            }
        }
        System.runFinalization();
        if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
            Runtime.getRuntime().halt(0);
        }
    }

    public static void resetCursor() {
    }

    private static PlatformCursor buildCursor(int i0, int i1, Pixmap pixmap) {
        return DesktopLauncher.createAnimatedCursor(i0, i1, new Pixmap[]{pixmap}, null);
    }

    private static PlatformCursor createAnimatedCursor(int i0, int i1, Pixmap[] pixmapArray, int[] nArray) {
        int i4 = pixmapArray[0].getWidth();
        int i5 = pixmapArray[0].getHeight();
        IntBuffer intBuffer = IntBuffer.allocate(pixmapArray.length * i4 * i5);
        int n = 0;
        while (n < pixmapArray.length) {
            int n2 = i5 - 1;
            while (n2 >= 0) {
                int n3 = 0;
                while (n3 < i4) {
                    int i10 = pixmapArray[n].getPixel(n3, n2);
                    int i11 = i10 >>> 8 | i10 << 24;
                    intBuffer.put(i11);
                    ++n3;
                }
                --n2;
            }
            ++n;
        }
        intBuffer.position(0);
        IntBuffer intBuffer2 = null;
        if (nArray != null) {
            intBuffer2 = IntBuffer.wrap(nArray);
            intBuffer2.position(0);
        }
        DesktopCursor desktopCursor = new DesktopCursor();
        try {
            desktopCursor.a = new Cursor(i4, i5, i0, i5 - i1, pixmapArray.length, intBuffer, intBuffer2);
        }
        catch (LWJGLException lWJGLException) {
            Gdx.app.error("Cursor", "Cannot create cursor via LWJGL.", lWJGLException);
        }
        return desktopCursor;
    }

    private static void applyNativeCursor(PlatformCursor platformCursor) {
        try {
            Mouse.setNativeCursor(((DesktopCursor)platformCursor).a);
        }
        catch (LWJGLException lWJGLException) {
            Gdx.app.error("Cursor", "Cannot display cursor via LWJGL.", lWJGLException);
        }
    }

    static /* synthetic */ PlatformCursor createCursor(int i0, int i1, Pixmap pixmap) {
        return DesktopLauncher.buildCursor(i0, i1, pixmap);
    }

    static /* synthetic */ void applyCursor(PlatformCursor platformCursor) {
        DesktopLauncher.applyNativeCursor(platformCursor);
    }

    static /* synthetic */ void setExitRequested(boolean bl) {
        a = bl;
    }
}


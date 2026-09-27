/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.desertstormfront.app.GameApplication;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.TouchDeviceFlags;
import com.desertstormfront.screen.ScreenInputProcessor;
import com.desertstormfront.ui.BatchRenderer;
import com.desertstormfront.ui.GlTexture;
import com.desertstormfront.ui.hud.GuiAssets;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.license.License;
import com.noblemaster.lib.market.Market;
import com.noblemaster.lib.net.match.AddressProvider;
import com.noblemaster.lib.net.match.PortMappingCallback;

public abstract class BaseScreen {
    private GameApplication application;
    private static String hudRendererFontSuffix;
    private static BatchRenderer hudRenderer;
    private int width;
    private int height;
    private int frameCounter = 0;
    private boolean touchDown;
    private int touchDownFrame;
    private int touchDownUntilFrame;
    private boolean touchUp;
    private int touchUpFrame;
    private int touchUpUntilFrame;
    private int touchX;
    private int touchY;
    private static BatchRenderer menuBackgroundRenderer;
    private static GlTexture tilesTexture;
    private static GlTexture unitsTexture;

    public final void setApplication(GameApplication gameApplication) {
        this.application = gameApplication;
        Gdx.input.setInputProcessor(new ScreenInputProcessor(this));
    }

    public boolean isTouchDown() {
        return this.touchDown;
    }

    public boolean isTouchUp() {
        return this.touchUp;
    }

    public int getTouchX() {
        return this.touchX;
    }

    public int getTouchY() {
        return this.touchY;
    }

    public abstract BaseScreen update();

    public abstract void show();

    public abstract void createGlResources();

    public final void resize(int i1, int i2) {
        this.width = i1;
        this.height = i2;
        this.layout();
    }

    protected abstract void layout();

    public License getLicense() {
        return this.application.getLicense();
    }

    public Market getMarket() {
        return this.application.getMarket();
    }

    public final BatchRenderer getHudRenderer() {
        if (hudRenderer == null || !hudRendererFontSuffix.equals(GuiAssets.getFontSuffix())) {
            hudRendererFontSuffix = GuiAssets.getFontSuffix();
            if (GameConfig.cleanContent) {
                GameFile[] gameFileArray = GuiAssets.getGuiTextureFiles();
                GameFile[] gameFileArray2 = new GameFile[gameFileArray.length + 1];
                int i3 = 0;
                while (i3 < gameFileArray.length) {
                    gameFileArray2[i3] = gameFileArray[i3];
                    ++i3;
                }
                gameFileArray2[gameFileArray2.length - 1] = GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "texture_heads.png");
                int[] nArray = new int[2];
                nArray[1] = 1359;
                hudRenderer = new BatchRenderer(new GlTexture(gameFileArray2, nArray, new int[]{1297, 1098}, 33071, 33071, 9729, 9729), 2048);
            } else {
                hudRenderer = new BatchRenderer(new GlTexture(GuiAssets.getGuiTextureFiles(), new int[1], new int[]{1297}, 33071, 33071, 9729, 9729), 2048);
            }
        }
        return hudRenderer;
    }

    public final void disposeHudRenderer() {
        if (hudRenderer != null) {
            hudRenderer.dispose();
            hudRenderer = null;
        }
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public void render() {
        ++this.frameCounter;
        if (this.touchDownUntilFrame < this.frameCounter) {
            this.touchDown = false;
        }
        if (this.touchUpUntilFrame < this.frameCounter) {
            this.touchUp = false;
        }
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            TouchDeviceFlags.setTouchDevice(this.application.isDesktopMode());
        }
    }

    public abstract void pause();

    public abstract void dispose();

    public String getLanguage() {
        return this.application.getLanguage();
    }

    public AddressProvider getAddressProvider() {
        return this.application.getAddressProvider();
    }

    public PortMappingCallback getPortMappingCallback() {
        return this.application.getPortMappingCallback();
    }

    public final void openUrl(String string) {
        this.application.openUrl(string);
    }

    public final void resetCursor() {
        this.application.resetCursor();
    }

    public final void exitApplication() {
        this.application.exit();
    }

    protected int accumulatePointerX(int i1, int i2) {
        return this.application.clampMouseX(i1, i2);
    }

    protected int accumulatePointerY(int i1, int i2) {
        return this.application.clampMouseY(i1, i2);
    }

    protected final BatchRenderer getMenuBackgroundRenderer() {
        if (menuBackgroundRenderer == null) {
            menuBackgroundRenderer = new BatchRenderer(new GlTexture(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "menu_background.png"), 33071, 33071, 9729, 9729), 256);
        }
        return menuBackgroundRenderer;
    }

    protected final void disposeMenuBackgroundRenderer() {
        if (Gdx.app.getType() != Application.ApplicationType.iOS) {
            menuBackgroundRenderer.dispose();
            menuBackgroundRenderer = null;
        }
    }

    protected final GlTexture getTilesTexture() {
        if (tilesTexture == null) {
            tilesTexture = new GlTexture(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "texture_tiles.png"), 33071, 33071, 9728, 9728);
        }
        return tilesTexture;
    }

    protected final void disposeTilesTexture() {
        if (Gdx.app.getType() != Application.ApplicationType.iOS) {
            tilesTexture.dispose();
            tilesTexture = null;
        }
    }

    protected final GlTexture getUnitsTexture() {
        if (unitsTexture == null) {
            String string = GameConfig.cleanContent ? "" : "_xxx";
            unitsTexture = new GlTexture(new GameFile[]{GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "texture_units.png"), GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "texture_flags" + string + ".png")}, new int[]{1792}, new int[]{1024}, 33071, 33071, 9729, 9729);
        }
        return unitsTexture;
    }

    protected final void disposeUnitsTexture() {
        if (Gdx.app.getType() != Application.ApplicationType.iOS) {
            unitsTexture.dispose();
            unitsTexture = null;
        }
    }

    static /* synthetic */ int getFrameCounter(BaseScreen baseScreen) {
        return baseScreen.frameCounter;
    }

    static /* synthetic */ void setTouchDownUntilFrame(BaseScreen baseScreen, int i1) {
        baseScreen.touchDownUntilFrame = i1;
    }

    static /* synthetic */ int getTouchDownUntilFrame(BaseScreen baseScreen) {
        return baseScreen.touchDownUntilFrame;
    }

    static /* synthetic */ int getTouchDownFrame(BaseScreen baseScreen) {
        return baseScreen.touchDownFrame;
    }

    static /* synthetic */ void setTouchUpUntilFrame(BaseScreen baseScreen, int i1) {
        baseScreen.touchUpUntilFrame = i1;
    }

    static /* synthetic */ int getTouchUpUntilFrame(BaseScreen baseScreen) {
        return baseScreen.touchUpUntilFrame;
    }

    static /* synthetic */ int getTouchUpFrame(BaseScreen baseScreen) {
        return baseScreen.touchUpFrame;
    }

    static /* synthetic */ void setTouchX(BaseScreen baseScreen, int i1) {
        baseScreen.touchX = i1;
    }

    static /* synthetic */ void setTouchY(BaseScreen baseScreen, int i1) {
        baseScreen.touchY = i1;
    }

    static /* synthetic */ void setTouchDown(BaseScreen baseScreen, boolean bl) {
        baseScreen.touchDown = bl;
    }

    static /* synthetic */ void setTouchDownFrame(BaseScreen baseScreen, int i1) {
        baseScreen.touchDownFrame = i1;
    }

    static /* synthetic */ void setTouchUp(BaseScreen baseScreen, boolean bl) {
        baseScreen.touchUp = bl;
    }

    static /* synthetic */ void setTouchUpFrame(BaseScreen baseScreen, int i1) {
        baseScreen.touchUpFrame = i1;
    }
}


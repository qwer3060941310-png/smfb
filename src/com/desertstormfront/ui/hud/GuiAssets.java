/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.audio.AudioClip;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.player.Faction;
import com.desertstormfront.ui.BitmapFont;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.CheckboxStyle;
import com.desertstormfront.ui.NinePatch;
import com.desertstormfront.ui.ScrollBarStyle;
import com.desertstormfront.ui.TextureRegion;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.io.GameFile;

/**
 * The global registry of shared UI assets (fonts, bitmap regions, nine-patches and widget styles),
 * loaded once from the GUI texture atlas and the data directory.
 *
 * <p>Most members are already meaningfully named (the {@code font*} bitmaps, {@link #defaultFont},
 * {@link #flagRegions}, {@link #clickSound} and the {@code get*}/{@code resolve*} helpers). The
 * remaining anonymous widget-style accessors are named from where they are consumed: the panel
 * background nine-patch, the scroll-bar style, the dialog / unit-slot / production / list-box /
 * checkbox styles. Several other single-letter accessors have no distinguishable role from their
 * call sites and are intentionally left as-is.
 */
public final class GuiAssets {
    private static String defaultFontLocale;
    private static BitmapFont defaultFont;
    private static final BitmapFont fontDokchampa15;
    private static final BitmapFont fontXirod17;
    private static final BitmapFont fontDungeon34;
    private static final BitmapFont fontDungeon25;
    private static final BitmapFont fontDungeon15Outline;
    private static final NinePatch backgroundNinePatch;
    private static final int[] dialogContentInsets;
    private static final ScrollBarStyle scrollBarStyle;
    private static final ButtonStyle dialogButtonStyle;
    private static final ButtonStyle unitSlotButtonStyle;
    private static final ButtonStyle productionButtonStyle;
    private static final ButtonStyle fullCountButtonStyle;
    private static final CheckboxStyle o;
    private static final ButtonStyle listBoxItemStyle;
    private static final ButtonStyle listBoxSelectionStyle;
    private static final TextureRegion[] flagRegions;
    private static final ButtonStyle menuButtonStyle;
    private static final NinePatch listPanelNinePatch;
    private static final NinePatch rowNinePatch;
    private static final TextureRegion rowHighlightRegion;
    private static final CheckboxStyle checkboxStyle;
    private static final ButtonStyle menuListBoxItemStyle;
    private static final ButtonStyle menuListBoxSelectionStyle;
    private static final NinePatch menuPanelNinePatch;
    private static final NinePatch optionRowNinePatch;
    private static final NinePatch hintBoxNinePatch;
    private static final ButtonStyle hintBoxButtonStyle;
    private static final TextureRegion hintPointerRegion;
    private static final AudioClip clickSound;

    static {
        backgroundNinePatch = new NinePatch(1508, 1775, 1813, 2048, 349, 507, 557, 708);
        dialogContentInsets = new int[]{49, 48, 48, 46};
        scrollBarStyle = new ScrollBarStyle(new int[]{885, 914, 943}, new int[]{1050, 1050, 1050}, 28, 16, new int[]{885, 914, 943}, new int[]{1067, 1067, 1067}, 28, 16, new int[]{885, 914, 943}, new int[]{1084, 1084, 1084}, 28, 16);
        dialogButtonStyle = new ButtonStyle(new int[4], new int[]{629, 678, 727, 776}, 192, 48);
        int[] nArray = new int[4];
        nArray[1] = 81;
        nArray[2] = 162;
        nArray[3] = 243;
        unitSlotButtonStyle = new ButtonStyle(nArray, new int[]{433, 433, 433, 433}, 80, 48);
        int[] nArray2 = new int[4];
        nArray2[1] = 81;
        nArray2[2] = 162;
        nArray2[3] = 243;
        productionButtonStyle = new ButtonStyle(nArray2, new int[]{482, 482, 482, 482}, 80, 48);
        int[] nArray3 = new int[4];
        nArray3[0] = 672;
        nArray3[1] = 753;
        nArray3[2] = 834;
        fullCountButtonStyle = new ButtonStyle(nArray3, new int[]{528, 528, 528, 528}, 80, 48);
        int[] nArray4 = new int[8];
        nArray4[1] = 49;
        nArray4[2] = 98;
        nArray4[3] = 147;
        nArray4[4] = 196;
        nArray4[5] = 245;
        nArray4[6] = 294;
        nArray4[7] = 343;
        o = new CheckboxStyle(nArray4, new int[]{580, 580, 580, 580, 580, 580, 580, 580}, 48, 48);
        listBoxItemStyle = new ButtonStyle(new int[]{193, 242, 291, 340}, new int[]{678, 678, 678, 678}, 48, 48);
        listBoxSelectionStyle = new ButtonStyle(new int[]{193, 242, 291, 340}, new int[]{629, 629, 629, 629}, 48, 48);
        flagRegions = new TextureRegion[]{new TextureRegion(1359, 1098, 68, 69), new TextureRegion(1428, 1098, 68, 69), new TextureRegion(1497, 1098, 68, 69), new TextureRegion(1566, 1098, 68, 69), new TextureRegion(1635, 1098, 68, 69), new TextureRegion(1704, 1098, 68, 69), new TextureRegion(1773, 1098, 68, 69), new TextureRegion(1842, 1098, 68, 69), new TextureRegion(1911, 1098, 68, 69), new TextureRegion(1980, 1098, 68, 69)};
        menuButtonStyle = new ButtonStyle(new int[]{774, 774, 774, 774}, new int[]{840, 889, 938, 987}, 192, 48);
        listPanelNinePatch = new NinePatch(719, 736, 756, 773, 926, 943, 963, 980);
        rowNinePatch = new NinePatch(719, 736, 756, 773, 981, 998, 1018, 1035);
        rowHighlightRegion = new TextureRegion(770, 690, 196, 51);
        checkboxStyle = new CheckboxStyle(new int[]{1116, 1165, 1214, 1263, 1312, 1361, 1410, 1459}, new int[]{506, 506, 506, 506, 506, 506, 506, 506}, 48, 48);
        menuListBoxItemStyle = new ButtonStyle(new int[]{771, 820, 869, 918}, new int[]{791, 791, 791, 791}, 48, 48);
        menuListBoxSelectionStyle = new ButtonStyle(new int[]{771, 820, 869, 918}, new int[]{742, 742, 742, 742}, 48, 48);
        menuPanelNinePatch = new NinePatch(84, 152, 171, 240, 304, 335, 357, 390);
        optionRowNinePatch = new NinePatch(150, 175, 180, 193, 110, 120, 125, 145);
        hintBoxNinePatch = new NinePatch(208, 218, 223, 234, 1, 11, 16, 27);
        hintBoxButtonStyle = new ButtonStyle(new int[]{390, 522, 654, 654}, new int[]{1036, 1036, 1036, 1036}, 131, 64);
        hintPointerRegion = new TextureRegion(549, 403, 63, 70);
        fontDokchampa15 = new BitmapFont(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "font_dokchampa_15.fnt"), 995, 557);
        fontXirod17 = new BitmapFont(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "font_xirod_17.fnt"), 254, 217);
        fontDungeon34 = new BitmapFont(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "font_dungeon_34.fnt"), 972, 1043);
        fontDungeon34.setAscent(4);
        fontDungeon25 = new BitmapFont(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "font_dungeon_25.fnt"), 972, 1011);
        fontDungeon25.setAscent(3);
        fontDungeon15Outline = new BitmapFont(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "font_dungeon_15_outline.fnt"), 972, 1081);
        fontDungeon15Outline.setAscent(4);
        clickSound = new AudioClip(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "audio_click.wav"));
    }

    public static String getFontSuffix() {
        return GuiAssets.resolveFontSuffix();
    }

    public static GameFile[] getGuiTextureFiles() {
        return new GameFile[]{GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "texture_gui.png"), GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "texture_font" + GuiAssets.resolveFontSuffix() + ".png")};
    }

    public static BitmapFont getDefaultFont() {
        String string = GuiAssets.resolveFontSuffix();
        if (defaultFontLocale == null || !defaultFontLocale.equals(string)) {
            defaultFontLocale = string;
            defaultFont = new BitmapFont(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + "font_default" + string + ".fnt"), 0, 1297);
            defaultFont.setAscent(-8 + (Messages.getLanguage() != null && !Messages.getLanguage().getCode().equals("en") ? 1 : 0));
            defaultFont.setLineHeight(22);
        }
        return defaultFont;
    }

    private static String resolveFontSuffix() {
        String string = Messages.getLanguage().getCode();
        if (string.equals("ko")) {
            return "_ko";
        }
        if (string.equals("zh")) {
            return "_zh";
        }
        return "";
    }

    public static BitmapFont getFontDokchampa15() {
        return fontDokchampa15;
    }

    public static BitmapFont getFontXirod17() {
        return fontXirod17;
    }

    public static BitmapFont getFontDungeon34() {
        return fontDungeon34;
    }

    public static BitmapFont getFontDungeon25() {
        return fontDungeon25;
    }

    public static BitmapFont getFontDungeon15Outline() {
        return fontDungeon15Outline;
    }

    /** The shared panel-background nine-patch, used as the backdrop of most menu/dialog screens. */
    public static NinePatch getBackgroundNinePatch() {
        return backgroundNinePatch;
    }

    public static int[] getDialogContentInsets() {
        return dialogContentInsets;
    }

    /** The scroll-bar style used by every {@code ScrollPane} in the UI. */
    public static ScrollBarStyle getScrollBarStyle() {
        return scrollBarStyle;
    }

    /** Button style for the action buttons inside dialogs (OK/cancel-style confirmations). */
    public static ButtonStyle getDialogButtonStyle() {
        return dialogButtonStyle;
    }

    /** Button style for individual unit/sub-unit slots in the HUD panels. */
    public static ButtonStyle getUnitSlotButtonStyle() {
        return unitSlotButtonStyle;
    }

    /** Button style for the production-choice buttons (what a building can build). */
    public static ButtonStyle getProductionButtonStyle() {
        return productionButtonStyle;
    }

    /** Button style whose NORMAL/HOVER/PRESSED states are copied onto a full-capacity sub-unit button. */
    public static ButtonStyle getFullCountButtonStyle() {
        return fullCountButtonStyle;
    }

    /** The list-box item (row) style. */
    public static ButtonStyle getListBoxItemStyle() {
        return listBoxItemStyle;
    }

    /** The list-box selection (highlighted row) style. */
    public static ButtonStyle getListBoxSelectionStyle() {
        return listBoxSelectionStyle;
    }

    public static TextureRegion getFlagRegion(Faction faction) {
        return flagRegions[faction.getId()];
    }

    public static ButtonStyle getMenuButtonStyle() {
        return menuButtonStyle;
    }

    public static NinePatch getListPanelNinePatch() {
        return listPanelNinePatch;
    }

    public static NinePatch getRowNinePatch() {
        return rowNinePatch;
    }

    public static TextureRegion getRowHighlightRegion() {
        return rowHighlightRegion;
    }

    /** The only checkbox style, used by toggle/checkbox widgets. */
    public static CheckboxStyle getCheckboxStyle() {
        return checkboxStyle;
    }

    public static ButtonStyle getMenuListBoxItemStyle() {
        return menuListBoxItemStyle;
    }

    public static ButtonStyle getMenuListBoxSelectionStyle() {
        return menuListBoxSelectionStyle;
    }

    public static NinePatch getMenuPanelNinePatch() {
        return menuPanelNinePatch;
    }

    public static NinePatch getOptionRowNinePatch() {
        return optionRowNinePatch;
    }

    public static NinePatch getHintBoxNinePatch() {
        return hintBoxNinePatch;
    }

    public static ButtonStyle getHintBoxButtonStyle() {
        return hintBoxButtonStyle;
    }

    public static TextureRegion getHintPointerRegion() {
        return hintPointerRegion;
    }

    public static void playClickSound() {
        clickSound.play();
    }
}


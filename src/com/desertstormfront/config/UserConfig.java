/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.config;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.TouchDeviceFlags;
import com.desertstormfront.game.player.PlayerStatistics;
import com.noblemaster.lib.data.Language;
import com.noblemaster.lib.data.OrderedProperties;
import com.noblemaster.lib.data.Version;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.log.OsfLog;

/**
 * User-specific, persisted game settings. Backed by the {@code user.config} properties file and
 * exposed through static accessors (e.g. {@link #getLanguage()}, {@link #getMultiplayerPort()}).
 * Holds presentation options (language, full-screen, audio/music volume, detail rendering), the
 * world speed factor, the multiplayer port, and the touch-device / squad-select hot keys. Values
 * are loaded once on start-up and mutated through the matching {@code set*} methods from options
 * screens such as {@code OptionsScreen}.
 */
public final class UserConfig {
    private static final GameFile userConfigFile = GameConfig.getDataFileResolver().getExternal("user.config");
    private static final String[] squadSelectKeyNames = new String[]{"action_key_select_squad_1", "action_key_select_squad_2", "action_key_select_squad_3", "action_key_select_squad_4", "action_key_select_squad_5", "action_key_select_squad_6", "action_key_select_squad_7", "action_key_select_squad_8"};
    private static Language language;
    private static boolean fullScreen;
    private static float audioVolume;
    private static float musicVolume;
    private static float worldSpeedFactor;
    private static boolean renderDetails;
    private static int multiplayerPort;
    private static boolean touchScroll;
    private static boolean mouseControl;
    private static int scrollSpeed;
    private static int scrollKeyLeft;
    private static int scrollKeyRight;
    private static int scrollKeyUp;
    private static int scrollKeyDown;
    private static int[] squadSelectKeys;
    private static int actionKeyStop;
    private static int actionKeyRepair;
    private static int actionKeyPatrol;
    private static int actionKeyGroup;
    private static int actionKeyUngroup;
    private static int actionKeyDeselect;
    private static boolean displayAi;
    private static boolean showTutorialDialog;
    private static Version newsSeenVersion;
    private static long playingTime;
    private static PlayerStatistics statistics;

    static {
        squadSelectKeys = new int[squadSelectKeyNames.length];
    }

    /**
     * Loads all persisted settings from {@code user.config} into the static fields. Delegates to
     * {@link #loadFromProperties()}; safe to call once during start-up.
     */
    public static void load() {
        UserConfig.loadFromProperties();
    }

    /**
     * Reads the {@code user.config} properties and maps each key onto its static field, applying a
     * sensible default when the key is absent (e.g. {@code volume_music=0.50},
     * {@code multiplayer_port=2300}). Missing or corrupt files fall back to an empty
     * {@link OrderedProperties} so the game still boots with defaults.
     */
    private static void loadFromProperties() {
        OrderedProperties orderedProperties;
        try {
            orderedProperties = userConfigFile.getFileHandle().exists() ? OrderedProperties.parse(userConfigFile.getFileHandle().readString()) : new OrderedProperties();
        }
        catch (Exception exception) {
            OsfLog.info("Cannot load (" + userConfigFile + "): " + exception);
            OsfLog.logException(exception);
            orderedProperties = new OrderedProperties();
        }
        String string = orderedProperties.get("locale_language", "");
        language = string != null && !string.equals("") ? Language.fromCode(string) : null;
        fullScreen = Boolean.valueOf(orderedProperties.get("full_screen", "false"));
        audioVolume = Float.valueOf(orderedProperties.get("volume_audio", "1.00")).floatValue();
        musicVolume = Float.valueOf(orderedProperties.get("volume_music", "0.50")).floatValue();
        worldSpeedFactor = Float.valueOf(orderedProperties.get("world_speed_factor", "1.00")).floatValue();
        renderDetails = Boolean.valueOf(orderedProperties.get("render_details", String.valueOf(Gdx.app.getType() == Application.ApplicationType.Desktop)));
        multiplayerPort = Integer.valueOf(orderedProperties.get("multiplayer_port", "2300"));
        touchScroll = Boolean.valueOf(orderedProperties.get("touch_scroll", String.valueOf(!TouchDeviceFlags.isTouchDevice())));
        mouseControl = Boolean.valueOf(orderedProperties.get("mouse_control", String.valueOf(TouchDeviceFlags.isTouchDevice())));
        scrollSpeed = Integer.valueOf(orderedProperties.get("scroll_speed", "600"));
        scrollKeyLeft = Integer.valueOf(orderedProperties.get("scroll_key_left", String.valueOf(29)));
        scrollKeyRight = Integer.valueOf(orderedProperties.get("scroll_key_right", String.valueOf(32)));
        scrollKeyUp = Integer.valueOf(orderedProperties.get("scroll_key_up", String.valueOf(51)));
        scrollKeyDown = Integer.valueOf(orderedProperties.get("scroll_key_down", String.valueOf(47)));
        UserConfig.squadSelectKeys[0] = Integer.valueOf(orderedProperties.get(squadSelectKeyNames[0], String.valueOf(8)));
        UserConfig.squadSelectKeys[1] = Integer.valueOf(orderedProperties.get(squadSelectKeyNames[1], String.valueOf(9)));
        UserConfig.squadSelectKeys[2] = Integer.valueOf(orderedProperties.get(squadSelectKeyNames[2], String.valueOf(10)));
        UserConfig.squadSelectKeys[3] = Integer.valueOf(orderedProperties.get(squadSelectKeyNames[3], String.valueOf(11)));
        UserConfig.squadSelectKeys[4] = Integer.valueOf(orderedProperties.get(squadSelectKeyNames[4], String.valueOf(12)));
        UserConfig.squadSelectKeys[5] = Integer.valueOf(orderedProperties.get(squadSelectKeyNames[5], String.valueOf(13)));
        UserConfig.squadSelectKeys[6] = Integer.valueOf(orderedProperties.get(squadSelectKeyNames[6], String.valueOf(14)));
        UserConfig.squadSelectKeys[7] = Integer.valueOf(orderedProperties.get(squadSelectKeyNames[7], String.valueOf(15)));
        actionKeyStop = Integer.valueOf(orderedProperties.get("action_key_stop", String.valueOf(54)));
        actionKeyRepair = Integer.valueOf(orderedProperties.get("action_key_repair", String.valueOf(52)));
        actionKeyPatrol = Integer.valueOf(orderedProperties.get("action_key_patrol", String.valueOf(31)));
        actionKeyGroup = Integer.valueOf(orderedProperties.get("action_key_group", String.valueOf(45)));
        actionKeyUngroup = Integer.valueOf(orderedProperties.get("action_key_ungroup", String.valueOf(33)));
        actionKeyDeselect = Integer.valueOf(orderedProperties.get("action_key_deselect", String.valueOf(62)));
        displayAi = Boolean.valueOf(orderedProperties.get("display_ai", "false"));
        showTutorialDialog = Boolean.valueOf(orderedProperties.get("show_tutorial_dialog", "true"));
        newsSeenVersion = new Version(orderedProperties.get("news_seen", Version.getUnknown().getValue()));
        playingTime = Long.valueOf(orderedProperties.get("playing_time", "0"));
        statistics = new PlayerStatistics();
        statistics.setDestroyedUnits(Integer.valueOf(orderedProperties.get("playing_stat_units_destroyed", "0")));
        statistics.setLostUnits(Integer.valueOf(orderedProperties.get("playing_stat_units_lost", "0")));
        statistics.setDamageInflicted(Integer.valueOf(orderedProperties.get("playing_stat_damage_inflicted", "0")));
        statistics.setDamageReceived(Integer.valueOf(orderedProperties.get("playing_stat_damage_received", "0")));
        statistics.setBasesCaptured(Integer.valueOf(orderedProperties.get("playing_bases_captured", "0")));
        statistics.setBasesLost(Integer.valueOf(orderedProperties.get("playing_bases_lost", "0")));
        fullScreen = false;
    }

    /**
     * Serializes the current static fields back into an {@link OrderedProperties} and writes it to
     * {@code user.config}, preserving key order. Called by every {@code set*} once a value actually
     * changes, so the file always reflects the live configuration.
     */
    private static void saveToProperties() {
        OrderedProperties orderedProperties = new OrderedProperties();
        orderedProperties.put("locale_language", language != null ? language.getCode() : "");
        orderedProperties.put("full_screen", String.valueOf(fullScreen));
        orderedProperties.put("volume_audio", String.valueOf(audioVolume));
        orderedProperties.put("volume_music", String.valueOf(musicVolume));
        orderedProperties.put("world_speed_factor", String.valueOf(worldSpeedFactor));
        orderedProperties.put("render_details", String.valueOf(renderDetails));
        orderedProperties.put("multiplayer_port", String.valueOf(multiplayerPort));
        orderedProperties.put("touch_scroll", String.valueOf(touchScroll));
        orderedProperties.put("mouse_control", String.valueOf(mouseControl));
        orderedProperties.put("scroll_speed", String.valueOf(scrollSpeed));
        orderedProperties.put("scroll_key_left", String.valueOf(scrollKeyLeft));
        orderedProperties.put("scroll_key_right", String.valueOf(scrollKeyRight));
        orderedProperties.put("scroll_key_up", String.valueOf(scrollKeyUp));
        orderedProperties.put("scroll_key_down", String.valueOf(scrollKeyDown));
        int n = 0;
        while (n < squadSelectKeyNames.length) {
            orderedProperties.put(squadSelectKeyNames[n], String.valueOf(squadSelectKeys[n]));
            ++n;
        }
        orderedProperties.put("action_key_stop", String.valueOf(actionKeyStop));
        orderedProperties.put("action_key_repair", String.valueOf(actionKeyRepair));
        orderedProperties.put("action_key_patrol", String.valueOf(actionKeyPatrol));
        orderedProperties.put("action_key_group", String.valueOf(actionKeyGroup));
        orderedProperties.put("action_key_ungroup", String.valueOf(actionKeyUngroup));
        orderedProperties.put("action_key_deselect", String.valueOf(actionKeyDeselect));
        orderedProperties.put("display_ai", String.valueOf(displayAi));
        orderedProperties.put("show_tutorial_dialog", String.valueOf(showTutorialDialog));
        orderedProperties.put("news_seen", newsSeenVersion.getValue());
        orderedProperties.put("playing_time", String.valueOf(playingTime));
        orderedProperties.put("playing_stat_units_destroyed", String.valueOf(statistics.getDestroyedUnits()));
        orderedProperties.put("playing_stat_units_lost", String.valueOf(statistics.getLostUnits()));
        orderedProperties.put("playing_stat_damage_inflicted", String.valueOf(statistics.getDamageInflicted()));
        orderedProperties.put("playing_stat_damage_received", String.valueOf(statistics.getDamageReceived()));
        orderedProperties.put("playing_bases_captured", String.valueOf(statistics.getBasesCaptured()));
        orderedProperties.put("playing_bases_lost", String.valueOf(statistics.getBasesLost()));
        try {
            userConfigFile.getFileHandle().writeString(OrderedProperties.serialize(orderedProperties), false);
        }
        catch (Exception exception) {
            OsfLog.info("Cannot save (" + userConfigFile + "): " + exception);
            OsfLog.logException(exception);
        }
    }

    public static Language getLanguage() {
        return language;
    }

    public static void setLanguage(Language language) {
        UserConfig.language = language;
        UserConfig.saveToProperties();
    }

    public static boolean isFullScreen() {
        return fullScreen;
    }

    public static void setFullScreen(boolean bl) {
        fullScreen = bl;
        UserConfig.saveToProperties();
    }

    public static float getAudioVolume() {
        return audioVolume;
    }

    public static void setAudioVolume(float f0) {
        audioVolume = f0;
        UserConfig.saveToProperties();
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float f0) {
        musicVolume = f0;
        UserConfig.saveToProperties();
    }

    public static float getWorldSpeedFactor() {
        return worldSpeedFactor;
    }

    public static void setWorldSpeedFactor(float f0) {
        worldSpeedFactor = f0;
        UserConfig.saveToProperties();
    }

    public static boolean isRenderDetails() {
        return renderDetails;
    }

    public static void setRenderDetails(boolean bl) {
        renderDetails = bl;
        UserConfig.saveToProperties();
    }

    public static int getMultiplayerPort() {
        return multiplayerPort;
    }

    public static void setMultiplayerPort(int i0) {
        multiplayerPort = i0;
    }

    public static boolean isTouchScroll() {
        return touchScroll;
    }

    public static void setTouchScroll(boolean bl) {
        touchScroll = bl;
        UserConfig.saveToProperties();
    }

    public static boolean isMouseControl() {
        return mouseControl;
    }

    public static void setMouseControl(boolean bl) {
        mouseControl = bl;
    }

    public static int getScrollSpeed() {
        return scrollSpeed;
    }

    public static void setScrollSpeed(int i0) {
        scrollSpeed = i0;
        UserConfig.saveToProperties();
    }

    public static int getScrollKeyLeft() {
        return scrollKeyLeft;
    }

    public static void setScrollKeyLeft(int i0) {
        scrollKeyLeft = i0;
        UserConfig.saveToProperties();
    }

    public static int getScrollKeyRight() {
        return scrollKeyRight;
    }

    public static void setScrollKeyRight(int i0) {
        scrollKeyRight = i0;
        UserConfig.saveToProperties();
    }

    public static int getScrollKeyUp() {
        return scrollKeyUp;
    }

    public static void setScrollKeyUp(int i0) {
        scrollKeyUp = i0;
        UserConfig.saveToProperties();
    }

    public static int getScrollKeyDown() {
        return scrollKeyDown;
    }

    public static void setScrollKeyDown(int i0) {
        scrollKeyDown = i0;
        UserConfig.saveToProperties();
    }

    public static int[] getSquadSelectKeys() {
        return squadSelectKeys;
    }

    public static int getActionKeyStop() {
        return actionKeyStop;
    }

    public static void setActionKeyStop(int i0) {
        actionKeyStop = i0;
    }

    public static int getActionKeyRepair() {
        return actionKeyRepair;
    }

    public static void setActionKeyRepair(int i0) {
        actionKeyRepair = i0;
    }

    public static int getActionKeyPatrol() {
        return actionKeyPatrol;
    }

    public static void setActionKeyPatrol(int i0) {
        actionKeyPatrol = i0;
    }

    public static int getActionKeyGroup() {
        return actionKeyGroup;
    }

    public static void setActionKeyGroup(int i0) {
        actionKeyGroup = i0;
    }

    public static int getActionKeyUngroup() {
        return actionKeyUngroup;
    }

    public static void setActionKeyUngroup(int i0) {
        actionKeyUngroup = i0;
    }

    public static int getActionKeyDeselect() {
        return actionKeyDeselect;
    }

    public static boolean isDisplayAi() {
        return displayAi;
    }

    public static void setDisplayAi(boolean bl) {
        displayAi = bl;
        UserConfig.saveToProperties();
    }

    public static boolean isShowTutorialDialog() {
        return showTutorialDialog;
    }

    public static void setShowTutorialDialog(boolean bl) {
        showTutorialDialog = bl;
        UserConfig.saveToProperties();
    }

    public static Version getNewsSeenVersion() {
        return newsSeenVersion;
    }

    public static void setNewsSeenVersion(Version version) {
        newsSeenVersion = version;
        UserConfig.saveToProperties();
    }

    public static long getPlayingTime() {
        return playingTime;
    }

    public static void setPlayingTime(long l0) {
        playingTime = l0;
        UserConfig.saveToProperties();
    }

    public static PlayerStatistics getStatistics() {
        return statistics;
    }

    public static void setStatistics(PlayerStatistics playerStatistics) {
        statistics = playerStatistics;
        UserConfig.saveToProperties();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.config;

import com.desertstormfront.config.AbstractGameInfo;
import com.desertstormfront.config.SubConfigDSF;
import com.desertstormfront.config.SubConfigTSF;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.session.impl.CampaignGameMode;
import com.desertstormfront.session.impl.FileSaveStore;
import com.desertstormfront.session.impl.MemorySaveStore;
import com.desertstormfront.session.impl.MultiplayerGameMode;
import com.desertstormfront.session.impl.SkirmishGameMode;
import com.desertstormfront.session.impl.TestScenarioMode;
import com.desertstormfront.session.impl.TutorialGameMode;
import com.noblemaster.lib.data.DateTime;
import com.noblemaster.lib.data.Language;
import com.noblemaster.lib.data.Version;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.io.DataFileResolver;
import com.noblemaster.lib.market.Market;
import com.noblemaster.lib.net.match.MatchMakingClient;

public final class GameConfig {
    public static boolean cleanContent = false;
    private static DataFileResolver dataFileResolver;
    private static AbstractGameInfo gameInfo;
    private static Version version;
    private static DateTime buildDate;
    private static boolean debugEnabled;
    private static boolean liteMode;
    private static int screenWidth;
    private static int screenHeight;
    private static TutorialGameMode tutorialGameMode;
    private static TestScenarioMode testScenarioGameMode;
    private static CampaignGameMode campaignGameMode;
    private static SkirmishGameMode skirmishGameMode;
    private static MultiplayerGameMode multiplayerGameMode;
    private static MatchMakingClient matchMakingClient;
    private static boolean macBuild;
    public static boolean legacyDataDir;
    public static Market market;

    static {
        macBuild = false;
        legacyDataDir = true;
    }

    public static void configure(String string, Version version, DateTime dateTime, boolean bl, boolean bl2, int i5, int i6, boolean bl3) {
        if (string.equals("tsf")) {
            gameInfo = new SubConfigTSF();
        } else if (string.equals("dsf")) {
            gameInfo = new SubConfigDSF();
        } else {
            throw new RuntimeException("Configuration not available: " + string);
        }
        GameConfig.version = version;
        buildDate = dateTime;
        debugEnabled = bl;
        liteMode = bl2;
        screenWidth = i5;
        screenHeight = i6;
        if (gameInfo.getGameCode().equals("tsf") && legacyDataDir) {
            dataFileResolver = new DataFileResolver(bl2 ? ".tropical_stormfront_lite_" : ".tropical_stormfront_");
        } else if (bl3) {
            String string2;
            String string3;
            if (string.equals("tsf")) {
                if (bl2) {
                    string3 = "com.tropicalstormfront.mac.lite";
                    string2 = "Tropical Stormfront LITE";
                } else {
                    string3 = "com.tropicalstormfront.mac.full";
                    string2 = "Tropical Stormfront";
                }
            } else if (bl2) {
                string3 = "com.desertstormfront.mac.lite";
                string2 = "Desert Stormfront LITE";
            } else {
                string3 = "com.desertstormfront.mac.full";
                string2 = "Desert Stormfront";
            }
            dataFileResolver = new DataFileResolver("/Library/Containers/" + string3 + "/Data/Library/Application Support/" + string2 + "/");
        } else {
            dataFileResolver = new DataFileResolver(".config/" + (bl2 ? gameInfo.getGameIdLite() : gameInfo.getGameId()) + "/");
        }
    }

    public static AbstractGameInfo getGameInfo() {
        return gameInfo;
    }

    public static DataFileResolver getDataFileResolver() {
        return dataFileResolver;
    }

    public static String getSharedDir() {
        return "shared/";
    }

    public static String getConfigDir() {
        return "config_" + gameInfo.getGameCode() + "/";
    }

    public static MapDefinition getMapDefinition() {
        return gameInfo.getDefaultMap();
    }

    public static String[][] getTutorialTexts() {
        return gameInfo.getTutorials();
    }

    public static boolean isDarkTheme() {
        return Messages.getLanguage() == Language.CHINESE || Messages.getLanguage() == Language.SPANISH && market != null && market.getName().equals("DarkGame");
    }

    public static String getTitle() {
        return Messages.get(liteMode ? gameInfo.getTitleLite() : gameInfo.getTitle());
    }

    public static String getSubtitle() {
        return Messages.getFallback(liteMode ? gameInfo.getTitleLite() : gameInfo.getTitle());
    }

    public static String getWebsiteUrl() {
        return gameInfo.getWebsiteUrl();
    }

    public static String getForumUrl() {
        return gameInfo.getForumUrl();
    }

    public static String getAboutText() {
        return GameConfig.getAboutText(false, false);
    }

    public static String getAboutText(boolean bl, boolean bl2) {
        if (bl & bl2) {
            return String.valueOf(gameInfo.getCopyright()) + "\n" + "<a href=\"" + gameInfo.getCompanyUrl() + "\">" + gameInfo.getCompanyUrl() + "</a>";
        }
        return String.valueOf(gameInfo.getCopyright()) + "\n" + gameInfo.getCompanyUrl();
    }

    public static String[] getMissionDescriptions() {
        return gameInfo.getMissionDescriptions();
    }

    public static int[] getCampaignMissionIndices() {
        return gameInfo.getCampaignMissionIndices();
    }

    public static String getCampaignStory() {
        return gameInfo.getCampaignStory();
    }

    public static String getCampaignCompleteText() {
        return gameInfo.getCampaignCompleteMessage();
    }

    public static String getCreditsText() {
        return GameConfig.getCreditsText(false, false);
    }

    public static String getCreditsText(boolean bl, boolean bl2) {
        return GameConfig.formatCredits(gameInfo.getCredits(), bl, bl2);
    }

    public static String getTechText() {
        return GameConfig.getTechText(false, false);
    }

    public static String getTechText(boolean bl, boolean bl2) {
        return GameConfig.formatCredits(gameInfo.getTechInfo(), bl, bl2);
    }

    private static String formatCredits(String[][] stringArray, boolean bl, boolean bl2) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bl) {
            String string = "";
            int i5 = 0;
            while (i5 < stringArray.length) {
                if (!stringArray[i5][0].equals(string)) {
                    string = stringArray[i5][0];
                    stringBuilder.append("<b>").append(string).append("</b>");
                    stringBuilder.append("<br>");
                }
                stringBuilder.append("&nbsp;&nbsp;&nbsp; - ");
                stringBuilder.append(stringArray[i5][1]);
                if (bl2 && stringArray[i5][2] != null) {
                    String string2 = stringArray[i5][2].contains("@") ? "mailto:" + stringArray[i5][2] : stringArray[i5][2];
                    stringBuilder.append(", ");
                    stringBuilder.append("<a href=\"" + string2 + "\">");
                    stringBuilder.append(stringArray[i5][2]);
                    stringBuilder.append("</a>");
                }
                stringBuilder.append("<br>");
                ++i5;
            }
        } else {
            int n = 0;
            while (n < stringArray.length) {
                stringBuilder.append(stringArray[n][0]).append(" - ");
                stringBuilder.append(stringArray[n][1]);
                stringBuilder.append('\n');
                ++n;
            }
        }
        return stringBuilder.toString();
    }

    public static String getLicenseText() {
        return gameInfo.getLicenses();
    }

    public static String getAppOverview() {
        return Messages.get(gameInfo.getAppOverview());
    }

    public static String getUnitsInfo() {
        return Messages.format(gameInfo.getInformationUnits(), 80);
    }

    public static String getStructuresInfo() {
        return Messages.get(gameInfo.getInformationStructures());
    }

    public static String getMoveablesInfo() {
        return Messages.get(gameInfo.getInformationMoveables());
    }

    public static String[] getMusicTracks() {
        return gameInfo.getMusicFiles();
    }

    public static String getBuyFullVersionText() {
        return Messages.getFallback(gameInfo.getBuyFullVersionText());
    }

    public static String getBuyFullVersionDetails() {
        return Messages.get(gameInfo.getBuyFullVersionDetails());
    }

    public static Version getVersion() {
        return version;
    }

    public static DateTime getBuildDate() {
        return buildDate;
    }

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    public static boolean isLiteMode() {
        return liteMode;
    }

    public static boolean isMacBuild() {
        return macBuild;
    }

    public static void setMacBuild(boolean bl) {
        macBuild = bl;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static TutorialGameMode getTutorialGameMode() {
        if (tutorialGameMode == null) {
            tutorialGameMode = new TutorialGameMode(GameConfig.getDataFileResolver().getExternal("tutorial.data"), new MemorySaveStore());
        }
        return tutorialGameMode;
    }

    public static TestScenarioMode getTestScenarioGameMode() {
        if (testScenarioGameMode == null) {
            testScenarioGameMode = new TestScenarioMode(GameConfig.getDataFileResolver().getExternal("testing.data"), new MemorySaveStore());
        }
        return testScenarioGameMode;
    }

    public static CampaignGameMode getCampaignGameMode() {
        if (campaignGameMode == null) {
            campaignGameMode = new CampaignGameMode(GameConfig.getDataFileResolver().getExternal("campaign.data"), new FileSaveStore(GameConfig.getDataFileResolver().getExternal("campaign_world.data")));
        }
        return campaignGameMode;
    }

    public static SkirmishGameMode getSkirmishGameMode() {
        if (skirmishGameMode == null) {
            skirmishGameMode = new SkirmishGameMode(GameConfig.getDataFileResolver().getExternal("skirmish.data"), new FileSaveStore(GameConfig.getDataFileResolver().getExternal("skirmish_world.data")), new FileSaveStore(GameConfig.getDataFileResolver().getExternal("skirmish_world_start.data")));
        }
        return skirmishGameMode;
    }

    public static MultiplayerGameMode getMultiplayerGameMode() {
        if (multiplayerGameMode == null) {
            multiplayerGameMode = new MultiplayerGameMode(GameConfig.getDataFileResolver().getExternal("multiplayer.data"), new FileSaveStore(GameConfig.getDataFileResolver().getExternal("multiplayer_world.data")));
        }
        return multiplayerGameMode;
    }

    public static MatchMakingClient getMatchMakingClient() {
        if (matchMakingClient == null) {
            matchMakingClient = new MatchMakingClient("match.operationstormfront.com", 2399);
        }
        return matchMakingClient;
    }

    public static String getProductId() {
        return "operationstormfront_" + gameInfo.getGameCode();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.config;

import com.desertstormfront.game.model.TerrainType;
import com.desertstormfront.game.model.TerrainTypeList;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.game.player.Faction;
import com.desertstormfront.map.MapDefinition;
import com.noblemaster.lib.data.Language;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.log.OsfLog;
import java.util.ArrayList;

public abstract class AbstractGameInfo {
    public abstract String getGameCode();

    public abstract String getGameId();

    public abstract String getGameIdLite();

    public abstract MapDefinition getDefaultMap();

    public abstract String[][] getTutorials();

    public abstract String getTitle();

    public abstract String getTitleLite();

    public abstract String getKeywords();

    public abstract String getWebsiteUrl();

    public abstract String getForumUrl();

    public abstract String getCopyright();

    public abstract String getCompanyUrl();

    public abstract String[] getMissionDescriptions();

    public abstract int[] getCampaignMissionIndices();

    public abstract String getCampaignStory();

    public abstract String getCampaignCompleteMessage();

    public abstract String[][] getCredits();

    public abstract String[][] getTechInfo();

    public abstract String getLicenses();

    public abstract String getAppOverview();

    public abstract String getAppDescription();

    public abstract String getMiscFeatures();

    public abstract String getMiscNotes();

    public abstract String getMiscPromo();

    public abstract String getInformationUnits();

    public abstract String getInformationStructures();

    public abstract String getInformationMoveables();

    public abstract String[] getMusicFiles();

    public abstract String getBuyFullVersionText();

    public abstract String getBuyFullVersionDetails();

    public void logLocalizedNames() {
        AbstractGameInfo.logLocalizedNames(this);
    }

    protected static void logLocalizedNames(AbstractGameInfo abstractGameInfo) {
        Object object;
        Language language = Messages.getLanguage();
        String[] stringArray = new String[]{"en", "ru", "de", "es", "ja", "ko", "fr", "tr", "zh"};
        String[] object2 = stringArray;
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            object = object2[n2];
            Messages.load(Language.fromCode((String)object));
            OsfLog.print("-- NAME ---------------------------------------------------------------");
            OsfLog.print(Messages.get(abstractGameInfo.getTitle()));
            OsfLog.print(Messages.get(abstractGameInfo.getTitleLite()));
            OsfLog.print("-- DESCRIPTION --------------------------------------------------------");
            OsfLog.print(Messages.get(abstractGameInfo.getAppDescription()));
            OsfLog.print("-- NOTES --------------------------------------------------------------");
            OsfLog.print(Messages.get(abstractGameInfo.getMiscNotes()));
            OsfLog.print("-- FEATURES -----------------------------------------------------------");
            OsfLog.print(Messages.get(abstractGameInfo.getMiscFeatures()));
            OsfLog.print("-- PROMO --------------------------------------------------------------");
            OsfLog.print(Messages.get(abstractGameInfo.getMiscPromo()));
            OsfLog.print("-- KEYWORDS -----------------------------------------------------------");
            OsfLog.print(Messages.get(abstractGameInfo.getKeywords()));
            OsfLog.print("-----------------------------------------------------------------------");
            OsfLog.print("\n");
            ++n2;
        }
        Messages.load(language);
        OsfLog.print("-- SETUP ----------------------------------------------------------------");
        OsfLog.print("# Nations:");
        OsfLog.info("#");
        object = abstractGameInfo.getDefaultMap().getFactions();
        n2 = 0;
        while (n2 < ((ArrayList)object).size()) {
            OsfLog.info(" " + ((Faction)((ArrayList)object).get(n2)).getKey());
            ++n2;
        }
        OsfLog.print("\n");
        OsfLog.print("# Sites:");
        TerrainTypeList terrainTypeList = abstractGameInfo.getDefaultMap().getTerrainTypes();
        n = 0;
        while (n < terrainTypeList.size()) {
            TerrainType terrainType = (TerrainType)terrainTypeList.get(n);
            OsfLog.print("# '" + terrainType.getCode() + "' = " + terrainType.getKey() + " (" + terrainType.getSpriteCount() + " tiles)");
            ++n;
        }
        OsfLog.print("# Units:");
        UnitTypeList unitTypeList = abstractGameInfo.getDefaultMap().getUnitTypes();
        int n3 = 0;
        while (n3 < unitTypeList.size()) {
            UnitType unitType = (UnitType)unitTypeList.get(n3);
            OsfLog.print("# '" + unitType.getCode() + "' = " + unitType.getKey());
            ++n3;
        }
        OsfLog.print("# Unit Configurations:");
        n3 = (abstractGameInfo.getDefaultMap().getUnitTypeCount() + 1) * 4;
        int n4 = 0;
        while (n4 < n3) {
            String string;
            char i8 = n4 < 10 ? (char)(48 + n4) : (char)(65 + (n4 - 10));
            int i9 = n4 % 4;
            switch (i9) {
                case 0: {
                    string = "000";
                    break;
                }
                case 1: {
                    string = "090";
                    break;
                }
                case 2: {
                    string = "180";
                    break;
                }
                case 3: {
                    string = "270";
                    break;
                }
                default: {
                    string = "invalid";
                }
            }
            int i11 = n4 / 4 - 1;
            String string2 = " | hosted:";
            if (i11 >= 0) {
                UnitTypeList unitTypeList2 = abstractGameInfo.getDefaultMap().getCargoUnits(i11);
                int i14 = 0;
                while (i14 < unitTypeList2.size()) {
                    string2 = String.valueOf(string2) + " " + ((UnitType)unitTypeList2.get(i14)).getKey();
                    ++i14;
                }
            } else {
                string2 = String.valueOf(string2) + " none";
            }
            OsfLog.print("# '" + i8 + "' = " + string + "deg" + string2);
            ++n4;
        }
        OsfLog.print("-----------------------------------------------------------------------");
    }
}


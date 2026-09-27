/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.config;

import com.desertstormfront.config.AbstractGameInfo;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.TouchDeviceFlags;
import com.desertstormfront.map.MapDefinition;

/**
 * Ruleset metadata for the base "Desert Stormfront" game (game id {@code desertstormfront}).
 * Supplies everything the shell needs to present the title: localized title/lite title, genre and
 * keywords, the campaign story and its 30 mission descriptions, the credits and tech-info tables,
 * the bundled license text and store/overview copy. Extends {@link AbstractGameInfo}, which defines
 * the general game-info contract; the actual unit/map rules live in the {@code dsf} ruleset loaded
 * via {@link MapDefinition#getRuleset(String)}.
 */
final class SubConfigDSF
extends AbstractGameInfo {
    private final String gameId = "desertstormfront";
    private final String gameIdLite = "desertstormfront_lite";
    private final MapDefinition defaultMap = MapDefinition.getRuleset("dsf");
    private final String title = "DesertStormfront[i18n]: Desert Stormfront";
    private final String titleLite = "DesertStormfrontLite[i18n]: Desert Stormfront LITE";
    private final String genre = "RealTimeStrategy[i18n]: Real-Time Strategy";
    private final String keywords = "KeywordsDesertStormfrontETC[i18n]: war, real-time, strategy, game, battle, RTS, defense, campaign, tank, capture, survival, capture the flag, sea, land, air, desert, multiplayer, LAN, internet";
    private final String websiteUrl = "http://www.desertstormfront.com";
    private final String forumUrl = "http://www.multiplayerhub.com/board/viewforum.php?f=60";
    private final String copyright = "Copyright \u00a9 2012 by Noble Master LLC";
    private final String companyUrl = "http://www.noblemaster.com";
    private final String[] missionDescriptions = new String[]{"Mission001DescriptionDesertStormfrontV01ETC[i18n]: Deploying from nearby Kuwait, Abadan (Iran) becomes the USA's first target.", "Mission002DescriptionDesertStormfrontV01ETC[i18n]: There is a local rebel uprising. Eradicate all rebel forces! France is on its way to help.", "Mission003DescriptionDesertStormfrontV01ETC[i18n]: Transition to our next objective, Shiraz (Iran), is halted by a destroyed bridge. Find a way around while keeping the supply truck safe.", "Mission004DescriptionDesertStormfrontV01ETC[i18n]: A nuclear enrichment facility has been located outside of Abadan. It is crucial that we stop all production in the facility.", "Mission005DescriptionDesertStormfrontV01ETC[i18n]: Afghanistan and Iraq have pledged alliance to Iran. England attacks its first target, Az Zubayr (Iraq).", "Mission006DescriptionDesertStormfrontV01ETC[i18n]: France invades Bandar Abbas (Iran) but quickly finds itself overwhelmed. France must hold out until help arrives.", "Mission007DescriptionDesertStormfrontV01ETC[i18n]: Continuing the journey from Abadan to Shiraz, the USA find themselves ambushed and must hold out in a small town.", "Mission008DescriptionDesertStormfrontV01ETC[i18n]: A key oil field outside of Shiraz must be captured before we can proceed further.", "Mission009DescriptionDesertStormfrontV01ETC[i18n]: The USA have arrived at Shiraz but must quickly get a supply truck into town and set up base before enemy reinforcements arrive.", "Mission010DescriptionDesertStormfrontV01ETC[i18n]: Iran attempts to reclaim Shiraz with the help of Iraq. Survive the onslaught until your allies arrive to your aid.", "Mission011DescriptionDesertStormfrontV01ETC[i18n]: A triple assault is unleashed on Basra (Iraq) by the USA, France and England.", "Mission012DescriptionDesertStormfrontV01ETC[i18n]: Saudi Arabia has joined alliance with Iraq and Iran. A Saudi Arabian fleet attacks France, England and the USA in the Persian Gulf.", "Mission013DescriptionDesertStormfrontV01ETC[i18n]: Advancing forward, the USA attack Baghdad (Iraq). Conquer the city.", "Mission014DescriptionDesertStormfrontV01ETC[i18n]: The USA attack an oil refining facility in Iran. Iraq aides Iran in their defense.", "Mission015DescriptionDesertStormfrontV01ETC[i18n]: England brings the fight to Saudi Arabia by attacking the city of Jeddah (Saudi Arabia).", "Mission016DescriptionDesertStormfrontV01ETC[i18n]: England advances to attack Mecca (Saudi Arabia).", "Mission017DescriptionDesertStormfrontV01ETC[i18n]: Italy joins the war and assaults Medina (Saudi Arabia). France joins the battle.", "Mission018DescriptionDesertStormfrontV01ETC[i18n]: A nuclear enrichment facility in Qom (Iran) is attacked by the USA.", "Mission019DescriptionDesertStormfrontV01ETC[i18n]: Egypt joins Saudi Arabia in an attempt to reclaim Jeddah from England.", "Mission020DescriptionDesertStormfrontV01ETC[i18n]: France launches an attack on Farah (Afghanistan).", "Mission021DescriptionDesertStormfrontV01ETC[i18n]: Iran attempts to reclaim several oil fields from the USA.", "Mission022DescriptionDesertStormfrontV01ETC[i18n]: The USA target an oil field off the coast of Iran.", "Mission023DescriptionDesertStormfrontV01ETC[i18n]: Italy and France join up to attack Cairo (Egypt).", "Mission024DescriptionDesertStormfrontV01ETC[i18n]: After capturing Cairo, Egypt switches side. Riyadh (Saudi Arabia) is attacked by Italy, France and Egypt.", "Mission025DescriptionDesertStormfrontV01ETC[i18n]: Saudi Arabia has surrendered. France, England and the USA attack Kabul (Afghanistan).", "Mission026DescriptionDesertStormfrontV01ETC[i18n]: Afghanistan is attempting to destroy its own capital to prevent extraction of military intelligence. Do not let a single truck reach the target zone!", "Mission027DescriptionDesertStormfrontV01ETC[i18n]: Accepting failure, Afghanistan surrenders. The USA target a major airfield base in Kermanshah (Iran).", "Mission028DescriptionDesertStormfrontV01ETC[i18n]: The USA defend against a massive assault from Iraq and Iran in Baghdad. Escort the diplomats in the trucks to the Airfield.", "Mission029DescriptionDesertStormfrontV01ETC[i18n]: Iraq surrenders. The USA move to claim Tehran (Iran). The USA must attack and reclaim the outskirts of the town.", "Mission030DescriptionDesertStormfrontV01ETC[i18n]: The USA must set out to conquer the rest of Tehran. Iran desperately allies with local Rebels. Defeat the enemy to claim ultimate victory!"};
    private final String[] missionDescriptionsClean = new String[]{"Mission001DescriptionDesertStormfrontV01CleanETC[i18n]: You are deploying and having your first enemy encounter. Take over all enemy bases.", "Mission002DescriptionDesertStormfrontV01CleanETC[i18n]: There is a local rebel uprising. Eradicate all rebel forces! Help is on its way.", "Mission003DescriptionDesertStormfrontV01CleanETC[i18n]: Transition to your next objective is halted by a destroyed bridge. Find a way around while keeping the supply truck safe.", "Mission004DescriptionDesertStormfrontV01CleanETC[i18n]: A nuclear enrichment facility has been located. It is crucial that we stop all production in the facility.", "Mission005DescriptionDesertStormfrontV01CleanETC[i18n]: You are launching your first attack. Good luck!", "Mission006DescriptionDesertStormfrontV01CleanETC[i18n]: You are starting an invasion but quickly find yourself overwhelmed. Hold out until help arrives.", "Mission007DescriptionDesertStormfrontV01CleanETC[i18n]: Continuing the journey you find yourself ambushed and must hold out in a small town.", "Mission008DescriptionDesertStormfrontV01CleanETC[i18n]: A key oil field must be captured before we can proceed further.", "Mission009DescriptionDesertStormfrontV01CleanETC[i18n]: You must quickly get a supply truck into town and set up base before enemy reinforcements arrive.", "Mission010DescriptionDesertStormfrontV01CleanETC[i18n]: Your enemy attempts to reclaim land. Survive the onslaught until your allies arrive to your aid.", "Mission011DescriptionDesertStormfrontV01CleanETC[i18n]: You are part of a triple assault against your enemy. Be victorious!", "Mission012DescriptionDesertStormfrontV01CleanETC[i18n]: Sea battle: you are being attacked by a huge enemy fleet. Destroy all enemy naval forces", "Mission013DescriptionDesertStormfrontV01CleanETC[i18n]: Advancing forward you need to conquer a major city to continue your quest.", "Mission014DescriptionDesertStormfrontV01CleanETC[i18n]: You attack an oil refining facility. The location is heavily defended.", "Mission015DescriptionDesertStormfrontV01CleanETC[i18n]: You are fighting on enemy territory for another key city. All the best!", "Mission016DescriptionDesertStormfrontV01CleanETC[i18n]: You need to advance to claim additional land.", "Mission017DescriptionDesertStormfrontV01CleanETC[i18n]: You are launching a large-scale assault on a big city. Try not to lose!", "Mission018DescriptionDesertStormfrontV01CleanETC[i18n]: A nuclear enrichment facility has been detected and needs to be taken out.", "Mission019DescriptionDesertStormfrontV01CleanETC[i18n]: You are under heavy enemy attack. Survive until reinforcements arrive.", "Mission020DescriptionDesertStormfrontV01CleanETC[i18n]: You are delivering important supplies to the front. Protect the supply trucks under all circumstances while you move forward.", "Mission021DescriptionDesertStormfrontV01CleanETC[i18n]: Your enemy tries to reclaim several of its oil fields. Ward off the attack!", "Mission022DescriptionDesertStormfrontV01CleanETC[i18n]: You are targeting an oil field off the coast.", "Mission023DescriptionDesertStormfrontV01CleanETC[i18n]: You are launching an offensive against a fortified enemy position. Do your Best!", "Mission024DescriptionDesertStormfrontV01CleanETC[i18n]: Having surrouded the enemy, go in for the kill!", "Mission025DescriptionDesertStormfrontV01CleanETC[i18n]: Joining force, go in and take over a central enemy stronghold.", "Mission026DescriptionDesertStormfrontV01CleanETC[i18n]: Your enemy is attempting to destroy its own capital to prevent extraction of military intelligence. Do not let a single truck reach the target zone!", "Mission027DescriptionDesertStormfrontV01CleanETC[i18n]: You are targeting  a major airfield base. Destroy all enemy forces", "Mission028DescriptionDesertStormfrontV01CleanETC[i18n]: You are defending against a massive assault. Escort the diplomats in the trucks to the Airfield.", "Mission029DescriptionDesertStormfrontV01CleanETC[i18n]: You are moving to reclaim the last major enemy city. Attack and take over the outskirts of the town.", "Mission030DescriptionDesertStormfrontV01CleanETC[i18n]: Set out to conquer the rest of the town. Defeat the enemy to claim ultimate victory!"};
    private final int[] campaignMissionIndices = new int[]{1, 7, 18, 24};
    private final String campaignStory = "CampaignStoryBeginDesertStormfrontV01ETC[i18n]: The Middle East has often been a land of conflict. Various motives brought war to the region, but in more recent years the Middle East has been a target for its resources. The most abundant of all, oil, is highly sought after. The blood of modern societies and with it, nations would crumble.\n\nIt's the dawn of war. Iran has started to defy demands of the Western World, halting oil exports to several countries. Desperate to gain power, the nation has began work on building a nuclear arsenal. Several bombings in the USA and France have been directly linked to the Iranian government. Actions had to be taken.\n\nWith war on their mind, USA and France have banded together to devise a plan with the goal to capture major cities, oil fields and military structures in Iran including surrounding regions. Once key locations were secured, the Western coalition would negotiate with the governments and relinquish the land back to the people. But they would find that the Middle Eastern nations would not give in so easily...\n";
    private final String campaignStoryClean = "CampaignStoryBeginDesertStormfrontV01CleanETC[i18n]: Desert areas have often been a land of conflict. Various motives brought war, but most of all they has been a target for its resources. The most abundant of all, oil, is highly sought after. The blood of modern societies and with it, nations would crumble.\n\nIt's the dawn of war. The desert people have started to defy demands of the water people, halting oil exports to several countries. Desperate to gain power, the desert nations have began work on building a weapons arsenal ready to strike anywhere and any time. Actions had to be taken.\n\nWith war on their mind, the water nations have banded together to devise a plan with the goal to capture major cities, oil fields and military structures. Once key locations were secured, the coalition would negotiate with the governments and relinquish the land back to the people. But they would find that the desert nations would not give in so easily...\n";
    private final String campaignCompleteMessage = "CampaignStoreEndedDesertStormfrontV01ETC[i18n]: You have successfully completed all missions! Your strategies have proven extremely effective. You have defeated all enemy forces. Points awarded: {0}\n\nWe thank you for your service! We will consider your presence on future missions. Please stand by on future communication from us.\n\nCongratulations!\nHigh Command";
    private final String[][] credits;
    private final String[][] techInfo;
    private final String licenses = "Licenses: libgx is licensed under the Apache License, Version 2.0. LWJGL is Copyright (c) 2002-2007 Lightweight Java Game Library Project. All rights reserved. \n";
    private final String appOverview = "AppOverviewDesertStormfrontETC[i18n]: Desert Stormfront is a Real-Time Strategy (RTS) game situated in the Middle East. You play on hot desert landscapes for power and oil. Units at your disposal include Humvees, Tanks, Artillery, Mechanics, Helicopters, Planes, Ships and Submarines amongst others. Desert Stormfront supports multiplayer games over LAN and internet including co-op multiplayer.";
    private final String appOverviewClean = "AppOverviewDesertStormfrontCleanETC[i18n]: Desert Stormfront is a Real-Time Strategy (RTS) game situated in the desert. You play on hot desert landscapes for power and oil. Units at your disposal include Humvees, Tanks, Artillery, Mechanics, Helicopters, Planes, Ships and Submarines amongst others. Desert Stormfront supports multiplayer games over LAN and internet including co-op multiplayer.";
    private final String appDescription = "AppDescriptionDesertStormfrontETC[i18n]: Desert Stormfront is a Real-Time Strategy (RTS) game situated in the Middle East. You play on hot desert landscapes for power and oil. Units at your disposal include Humvees, Tanks, Artillery, Mechanics, Helicopters, Planes, Ships and Submarines amongst others. Desert Stormfront supports multiplayer games over LAN and internet including co-op multiplayer.\n\nThe game features 30 pre-defined campaign missions where you take command of the Western coalition and fight battles in the Middle East for control of major cities, oil fields and military structures. Nations in play are the USA, England, France, Italy, Iraq, Iran, Saudi Arabia, Egypt, Afghanistan plus a rebel faction.\n\nDesert Stormfront contains a random map generator in addition to the 30 campaign missions. Scenarios include eliminating enemy forces, capturing the flag, defending against incoming troops, convoy missions, sea and air battles as well as tank fights. The game keeps track of high scores and playing statistics.";
    private final String appDescriptionClean = "AppDescriptionDesertStormfrontCleanETC[i18n]: Desert Stormfront is a Real-Time Strategy (RTS) game situated in the desert. You play on hot desert landscapes for power and oil. Units at your disposal include Humvees, Tanks, Artillery, Mechanics, Helicopters, Planes, Ships and Submarines amongst others. Desert Stormfront supports multiplayer games over LAN and internet including co-op multiplayer.\n\nThe game features 30 pre-defined campaign missions where you take command and fight battles in the desert for control of major cities, oil fields and military structures.\n\nDesert Stormfront contains a random map generator in addition to the 30 campaign missions. Scenarios include eliminating enemy forces, capturing the flag, defending against incoming troops, convoy missions, sea and air battles as well as tank fights. The game keeps track of high scores and playing statistics.";
    private final String miscFeatures = "MiscFeaturesDesertStormfrontETC[i18n]: - Real-Time Strategy (RTS) in the Middle East\n- Single Player and Multiplayer (over the LAN and Internet)\n- 30 Campaign Missions\n- Random Map Generator\n- 16 Mobile Units including: Humvees, Tanks, Artillery, 4x4, Mechanic, Trucks, Planes, Chinook, Gunboat, Submarine, Cruiser and Carrier\n- 1 Special Unit: Super-Hovercraft\n- 4 Land Structures: Base Station, Shipyard, Airfield and Oil Field\n- Nations: USA, England, France, Italy, Iraq, Iran, Saudi Arabia, Egypt, Afghanistan plus a rebel faction\n- Team-Play (included in multiplayer)\n- High Score and Playing Statistics\n- Fog of War/Exploration Fog\n- Challenging AI with 4 Difficulty Settings\n- Enganging Music and Sound Effects\n";
    private final String miscFeaturesClean = "MiscFeaturesDesertStormfrontCleanETC[i18n]: - Real-Time Strategy (RTS) in the Desert\n- Single Player and Multiplayer (over the LAN and Internet)\n- 30 Campaign Missions\n- Random Map Generator\n- 16 Mobile Units including: Humvees, Tanks, Artillery, 4x4, Mechanic, Trucks, Planes, Chinook, Gunboat, Submarine, Cruiser and Carrier\n- 1 Special Unit: Super-Hovercraft\n- 4 Land Structures: Base Station, Shipyard, Airfield and Oil Field\n- Team-Play (included in multiplayer)\n- High Score and Playing Statistics\n- Fog of War/Exploration Fog\n- Challenging AI with 4 Difficulty Settings\n- Enganging Music and Sound Effects\n";
    private final String miscNotes = "MiscNotesDesertStormfrontETC[i18n]: IMPORTANT: Please note that the game requires a screen resolution of 800x480 pixels or higher. Although the game still runs on a lower resolution, not all GUI elements will be rendered properly. The game has been thoroughly tested and runs at about 30+ frames/seconds. It is possible though that the application is slow on some devices. Please try the LITE version before purchase to verify proper function. If you encounter any problems running the game, try (1) a device restart (i.e. turn off completely) as well as (2) a complete re-install.\n\nThe LITE version of the game includes 4 campaign mission to allow you to evaluate the game. There is no time limit and the game does not contain any spyware, malware or third party software of such kind. Again, please try the LITE version first to verify the application works on your device. If you are having any problems or are not happy with your purchase, feel free to contact us via email at any time. For a full refund please include your order# with your message.\n\nAlso try our other strategy game Tropical Stormfront if you like to play the game in a more tropical environment :)\n\nThank you & Enjoy the Game!\nnoblemaster\n\nTwitter: http://twitter.com/noblemaster";
    private final String miscPromo = "MiscPromoDesertStormfrontETC[i18n]: Desert Stormfront, real-time battles in the desert!";
    private final String informationUnits = "InformationUnitsDesertStormfrontETC[i18n]: The game features 17 mobile units as well as 4 land structures. Structures enable you to build new units. The oil field generates a fixed income every {0} seconds. The only unit that can be used to take over structures is the Humvee.";
    private final String informationStructures = "InformationListStructuresDesertStormfrontETC[i18n]: BASE STATION:\n - can build/host ground units\nSHIPYARD:\n - can build/host ships and submarines\nAIRFIELD:\n - can build/host air units\nOILFIELD:\n - generates income\n";
    private final String informationMoveables = "InformationListMoveablesDesertStormfrontETC[i18n]: HUMVEE:\n - can take over structures\nBATTLE TANK:\n - strong against ground units\nMISSILE TANK:\n - strong against air units\nARTILLERY:\n - can attack from a far distance\n4X4:\n - fast but weak attack unit\n - produced by oil field\nMECHANIC:\n - auto-repairs units in the vincinity\nTRUCK:\n - for escort missions\n - no attack capability\nGENERAL/COMMANDO UNIT:\n - you lose if your general is killed\nFIGHTER PLANE:\n - for reconnaissance\n - strong against air units\n - limited fuel\nHELICOPTER:\n - strong against ground units\n - weak against fighter plane\n - limited fuel\nCHINOOK:\n - can carry one ground unit\n - no range limit, but generally weak\nGUNBOAT:\n - strong against submarines\nSUBMARINE:\n - strong against ships except gunboat\nCRUISER:\n - strong against ships and ground units\n - weak against submarine\nCARRIER:\n - can carry air units\n - weak against submarine\nTRANSPORT SHIP:\n - carries ground units\nHOVERCRAFT:\n - super-unit (very strong)\n - can travel on land and water\n - cannot be produced\n";
    private final String[] musicFiles;
    private final String buyFullVersionText = "BuyTheFullVersion[i18n]: Buy the Full Version";
    private final String buyFullVersionDetails = "BuyTheFullVersionETC[i18n]: This LITE version is limited in scope. Please consider buying the full version with enhanced options:\n\n- play all the campaign missions\n- host multiplayer games\n- setup and play skirmish games\n- play all the nations (skirmish)\n- various setup options (skirmish)\n- stronger AI (variable difficulty)\n\nThank you!\nnoblemaster";

    SubConfigDSF() {
        String[][] stringArrayArray = new String[38][];
        stringArrayArray[0] = new String[]{"Developer", "Noble Master LLC", "http://www.noblemaster.com"};
        stringArrayArray[1] = new String[]{"Design", "Christoph Aschwanden", "ceo@noblemaster.com"};
        stringArrayArray[2] = new String[]{"Design", "Nick Lee", "http://nickrlee.carbonmade.com"};
        stringArrayArray[3] = new String[]{"Program", "Christoph Aschwanden", "ceo@noblemaster.com"};
        stringArrayArray[4] = new String[]{"Graphics and Art", "Nick Lee", "http://nickrlee.carbonmade.com"};
        stringArrayArray[5] = new String[]{"Theme Music", "Sean Beeson", "http://seanbeeson.com/"};
        stringArrayArray[6] = new String[]{"Sound FX", "Matthew Myers", "http://www.2eastmusic.com"};
        String[] stringArray = new String[3];
        stringArray[0] = "Modding";
        stringArray[1] = "Travis Bowling";
        stringArrayArray[7] = stringArray;
        String[] stringArray2 = new String[3];
        stringArray2[0] = "Portraits";
        stringArray2[1] = "Antony Hager";
        stringArrayArray[8] = stringArray2;
        String[] stringArray3 = new String[3];
        stringArray3[0] = "Animation";
        stringArray3[1] = "Steve Rowlands";
        stringArrayArray[9] = stringArray3;
        String[] stringArray4 = new String[3];
        stringArray4[0] = "Publisher (Spanish)";
        stringArray4[1] = "Dark Game, http://www.darkgame.es";
        stringArrayArray[10] = stringArray4;
        String[] stringArray5 = new String[3];
        stringArray5[0] = "Soporte en espa\u00f1ol";
        stringArray5[1] = "soporte@darkgame.es";
        stringArrayArray[11] = stringArray5;
        String[] stringArray6 = new String[3];
        stringArray6[0] = "Support in Spanish";
        stringArray6[1] = "soporte@darkgame.es";
        stringArrayArray[12] = stringArray6;
        String[] stringArray7 = new String[3];
        stringArray7[0] = "Translation JA";
        stringArray7[1] = "Hiroyuki Kobuna";
        stringArrayArray[13] = stringArray7;
        String[] stringArray8 = new String[3];
        stringArray8[0] = "Translation JA";
        stringArray8[1] = "Minori Iio";
        stringArrayArray[14] = stringArray8;
        String[] stringArray9 = new String[3];
        stringArray9[0] = "Translation JA";
        stringArray9[1] = "Shunsuke Fuji";
        stringArrayArray[15] = stringArray9;
        String[] stringArray10 = new String[3];
        stringArray10[0] = "Translation JA";
        stringArray10[1] = "Yuka Shimotori";
        stringArrayArray[16] = stringArray10;
        String[] stringArray11 = new String[3];
        stringArray11[0] = "Translation RU";
        stringArray11[1] = "Alexander Lee";
        stringArrayArray[17] = stringArray11;
        String[] stringArray12 = new String[3];
        stringArray12[0] = "Translation KO";
        stringArray12[1] = "JinSeog Hong";
        stringArrayArray[18] = stringArray12;
        stringArrayArray[19] = new String[]{"Translation KO", "Sookhee Im", "http://www.jibnetworks.com"};
        String[] stringArray13 = new String[3];
        stringArray13[0] = "Translation DE";
        stringArray13[1] = "Sabine Aschwanden";
        stringArrayArray[20] = stringArray13;
        String[] stringArray14 = new String[3];
        stringArray14[0] = "Translation FR";
        stringArray14[1] = "Patrice Gilbert";
        stringArrayArray[21] = stringArray14;
        String[] stringArray15 = new String[3];
        stringArray15[0] = "Translation ES";
        stringArray15[1] = "Dark Game, http://www.darkgame.es";
        stringArrayArray[22] = stringArray15;
        String[] stringArray16 = new String[3];
        stringArray16[0] = "Translation ZH";
        stringArray16[1] = "Immanitas";
        stringArrayArray[23] = stringArray16;
        String[] stringArray17 = new String[3];
        stringArray17[0] = "Translation TR";
        stringArray17[1] = "Batuhan Arslan";
        stringArrayArray[24] = stringArray17;
        String[] stringArray18 = new String[3];
        stringArray18[0] = "Translation TR";
        stringArray18[1] = "Aykut Yilmaz";
        stringArrayArray[25] = stringArray18;
        String[] stringArray19 = new String[3];
        stringArray19[0] = "Testing";
        stringArray19[1] = "Steven Ebert";
        stringArrayArray[26] = stringArray19;
        String[] stringArray20 = new String[3];
        stringArray20[0] = "Testing";
        stringArray20[1] = "Leif A Jernstad Jr.";
        stringArrayArray[27] = stringArray20;
        String[] stringArray21 = new String[3];
        stringArray21[0] = "Testing";
        stringArray21[1] = "Branko Kotur";
        stringArrayArray[28] = stringArray21;
        String[] stringArray22 = new String[3];
        stringArray22[0] = "Testing";
        stringArray22[1] = "J Scott Hammond";
        stringArrayArray[29] = stringArray22;
        String[] stringArray23 = new String[3];
        stringArray23[0] = "Testing";
        stringArray23[1] = "Axachu Odna";
        stringArrayArray[30] = stringArray23;
        String[] stringArray24 = new String[3];
        stringArray24[0] = "Testing";
        stringArray24[1] = "Sabine Aschwanden";
        stringArrayArray[31] = stringArray24;
        String[] stringArray25 = new String[3];
        stringArray25[0] = "Testing";
        stringArray25[1] = "Andrew Moore";
        stringArrayArray[32] = stringArray25;
        String[] stringArray26 = new String[3];
        stringArray26[0] = "Testing";
        stringArray26[1] = "Joshua Bona";
        stringArrayArray[33] = stringArray26;
        String[] stringArray27 = new String[3];
        stringArray27[0] = "Testing";
        stringArray27[1] = "Nick Lee";
        stringArrayArray[34] = stringArray27;
        String[] stringArray28 = new String[3];
        stringArray28[0] = "Testing";
        stringArray28[1] = "Javier Batista";
        stringArrayArray[35] = stringArray28;
        String[] stringArray29 = new String[3];
        stringArray29[0] = "Testing";
        stringArray29[1] = "Sean Beeson";
        stringArrayArray[36] = stringArray29;
        String[] stringArray30 = new String[3];
        stringArray30[0] = "Testing";
        stringArray30[1] = "Stian A.S. Johansen";
        stringArrayArray[37] = stringArray30;
        this.credits = stringArrayArray;
        this.techInfo = new String[][]{{"Programming Language", "Java", "http://www.java.com"}, {"Game Development Library", "libgdx", "http://code.google.com/p/libgdx/"}, {"Development Environment", "Eclipse", "http://www.eclipse.org"}, {"Audio Editing", "Audacity", "http://audacity.sourceforge.net"}, {"Graphics Editing", "Photoshop", "http://www.photoshop.com"}, {"Android Runtime", "Android", "http://www.android.com"}, {"iOS Runtime", "RoboVM", "http://www.robovm.org"}, {"Desktop Runtime", "Java", "http://www.java.com"}, {"Desktop OpenGL", "LWJGL", "http://www.lwjgl.org/"}};
        this.musicFiles = new String[]{"music_ingame_black_monolith.mp3", "music_ingame_call_for_heroes.mp3", "music_ingame_dark_lords.mp3", "music_ingame_demigod.mp3", "music_ingame_divine_power.mp3", "music_ingame_doomed_to_agony.mp3", "music_ingame_down_to_nothing.mp3", "music_ingame_fist_of_might.mp3", "music_ingame_glorious_victory.mp3", "music_ingame_glory_hunters.mp3", "music_ingame_heroic_tale.mp3", "music_ingame_impossible_rise.mp3", "music_ingame_into_the_fray.mp3", "music_ingame_kings_valor.mp3", "music_ingame_spirit_of_light.mp3", "music_ingame_vast_lands.mp3"};
    }

    @Override
    public String[][] getTutorials() {
        String[][] stringArray = new String[24][];
        String[] stringArray2 = new String[4];
        stringArray2[0] = "Tutorial00DesertStormfrontETC[i18n]: Welcome to the tutorial! Let's find out if we can explain the basic game-play options to you ;-)";
        stringArray2[1] = "C:0:C:0";
        stringArray[0] = stringArray2;
        String[] stringArray3 = new String[4];
        stringArray3[0] = "Tutorial01DesertStormfrontETC[i18n]: Desert Stormfront is a real-time strategy game. Your goal is to complete the mission in the black text box below.\n\nClick [OK] to start the mission.";
        stringArray3[1] = "C:0:CB:10";
        stringArray3[2] = "AcceptMission";
        stringArray[1] = stringArray3;
        String[] stringArray4 = new String[4];
        stringArray4[0] = "Tutorial02DesertStormfrontETC[i18n]: The game needs to initialize the map to provide for smooth gameplay. Please stand by...";
        stringArray4[1] = "C:0:CB:-50";
        stringArray4[2] = "InitComplete";
        stringArray[2] = stringArray4;
        String[] stringArray5 = new String[4];
        stringArray5[0] = "Tutorial03DesertStormfrontETC[i18n]: The number above represents the monetary funds at your disposal to purchase units. Income is generated every 80 seconds from all the oil fields you own.";
        stringArray5[1] = "L:0:T:50";
        stringArray[3] = stringArray5;
        String[] stringArray6 = new String[4];
        stringArray6[0] = "Tutorial04DesertStormfrontETC[i18n]: The buttons below allows grouping units. To group, press the button and then drag select the units on the map.";
        stringArray6[1] = "R:0:B:-73";
        stringArray[4] = stringArray6;
        String[] stringArray7 = new String[4];
        stringArray7[0] = "Tutorial05DesertStormfrontETC[i18n]: The structure marked with the green arrow is yours. It's the base station and can build ground units.";
        stringArray7[1] = "R:0:T:0";
        stringArray7[3] = "AD:1";
        stringArray[5] = stringArray7;
        String[] stringArray8 = new String[4];
        stringArray8[0] = "Tutorial06DesertStormfrontETC[i18n]: The following base station belongs to an enemy.";
        stringArray8[1] = "R:0:T:0";
        stringArray8[3] = "AD:2";
        stringArray[6] = stringArray8;
        String[] stringArray9 = new String[4];
        stringArray9[0] = "Tutorial07DesertStormfrontETC[i18n]: This a neutral oil field. Oil fields are the only structures that generate income.";
        stringArray9[1] = "R:0:T:0";
        stringArray9[3] = "AD:5";
        stringArray[7] = stringArray9;
        stringArray[8] = new String[]{"Tutorial08DesertStormfrontETC[i18n]: Let's build a Humvee, so we take over the oil field. Please note the Humvee is the only unit that can take over structures.\n\nTask: Click on your base station and select the blue button to build a Humvee.", "R:0:T:0", "BuildGround", "AD:1"};
        String[] stringArray10 = new String[4];
        stringArray10[0] = "Tutorial09DesertStormfrontETC[i18n]: Great job! You can close the base station panel by clicking on the [x] button.\n\nTask: Close the panel for the base station.";
        stringArray10[1] = "R:0:T:0";
        stringArray10[2] = "ClosePanel";
        stringArray[9] = stringArray10;
        String[] stringArray11 = new String[4];
        stringArray11[0] = !TouchDeviceFlags.isTouchDevice() ? "Tutorial10DesertStormfrontETC[i18n]: While we wait for the Humvee to be built, let's practice how to change the view. You can change the visible part of the map with your pointer.\n\nTask: Drag the map." : "Tutorial10BDesertStormfrontETC[i18n]: While we wait for the Humvee to be build, let's practice how to change the view. You can change the visible part of the map with your pointer.\n\nTask: Press ASDW on the keyboard or move your mouse to the edges of the screen. Mouse and keyboard settings can be adjusted in the [Options] screen.";
        stringArray11[1] = "R:0:T:0";
        stringArray11[2] = "MoveMap";
        stringArray[10] = stringArray11;
        String[] stringArray12 = new String[4];
        stringArray12[0] = !TouchDeviceFlags.isTouchDevice() ? "Tutorial11DesertStormfrontETC[i18n]: Excellent! Below is a transport ship at your disposal. We'll use it to ferry your newly built Humvee over to the oil field." : "Tutorial11BDesertStormfrontETC[i18n]: Excellent! Below is a transport ship at your disposal. We'll use it to ferry your newly built Humvee over to the oil field.\n\nNote: select units with the LEFT mouse button and make them move (targeting) with the RIGHT mouse button.";
        stringArray12[1] = "R:0:T:0";
        stringArray12[3] = "AD:3";
        stringArray[11] = stringArray12;
        stringArray[12] = new String[]{"Tutorial12DesertStormfrontETC[i18n]: Your Humvee should be built be now. Let's move it into your transport ship.\n\nTask: Click on your base station and select the tank by pressing the yellow button.", "R:0:T:0", "SelectGround", "AD:1"};
        stringArray[13] = new String[]{"Tutorial13DesertStormfrontETC[i18n]: Let's load the Humvee into the transport ship.\n\nTask: Click on your transport ship to move your Humvee there.", "R:0:T:0", "MoveGroundToMobileHost", "AS:4:T:3"};
        String[] stringArray13 = new String[4];
        stringArray13[0] = "Tutorial14DesertStormfrontETC[i18n]: Good Good! Let's wait until the Humvee has entered the transport ship...";
        stringArray13[1] = "R:0:T:0";
        stringArray13[2] = "LoadGroundIntoMobileHost";
        stringArray[14] = stringArray13;
        stringArray[15] = new String[]{"Tutorial15DesertStormfrontETC[i18n]: Let's move your transport ship towards the oil field situated to the north.\n\nTask: Click on your transport and target the enemy beach to the north.", "R:0:T:0", "MoveMobileHost", "AS:3:T:Z031301130213"};
        stringArray[16] = new String[]{"Tutorial16DesertStormfrontETC[i18n]: Yep, that's it! Let's wait for the transport ship to reach the coast...", "R:0:T:0", "LandMobileHost", "Z031301130213"};
        stringArray[17] = new String[]{"Tutorial17DesertStormfrontETC[i18n]: Let's capture the oil field with your Humvee.\n\nTask: Click on your transport ship and select your Humvee.", "R:0:T:0", "SelectGroundInMobileHost", "AD:3"};
        stringArray[18] = new String[]{"Tutorial18DesertStormfrontETC[i18n]: Order the Humvee to capture the oil field.\n\nTask: Click on the oil field to move your tank there.", "R:0:T:0", "MoveGroundToEnemyFixed", "AS:4:T:5"};
        String[] stringArray14 = new String[4];
        stringArray14[0] = "Tutorial19DesertStormfrontETC[i18n]: Well done! Sit back and relax. Let's wait for your Humvee to take over the oil field...";
        stringArray14[1] = "R:0:T:0";
        stringArray14[2] = "CapturedNeutral";
        stringArray[19] = stringArray14;
        String[] stringArray15 = new String[4];
        stringArray15[0] = "Tutorial20DesertStormfrontETC[i18n]: Great job! That wasn't hard, was it? You have successfully completed the tutorial by taking over the oil field :)";
        stringArray15[1] = "C:0:C:0";
        stringArray[20] = stringArray15;
        String[] stringArray16 = new String[4];
        stringArray16[0] = "Tutorial21DesertStormfrontETC[i18n]: The same way you have taken over the oil field, you can attack enemy units and military structures as well.\n\nBe aware, if there is a general in play make sure to protect him/her. If you lose your general, you lose the game.";
        stringArray16[1] = "C:0:C:0";
        stringArray[21] = stringArray16;
        String[] stringArray17 = new String[4];
        stringArray17[0] = "Tutorial22DesertStormfrontETC[i18n]: Other unit actions available are [Patrol] which makes a unit patrol between two positions. [Repair] will make a unit go repair itself and return back to it's original location thereafter. [Stop] stops a unit.\n\nAir units can be set to [Auto] mode which will make them move on their own. Use the [Rally] button to set rally points for structures.";
        stringArray17[1] = "C:0:C:0";
        stringArray[22] = stringArray17;
        String[] stringArray18 = new String[4];
        stringArray18[0] = "Tutorial23DesertStormfrontETC[i18n]: Please have a look at the manual and visit the web site and forums for more information regarding game play and strategies. Don't forget to sign up for our Twitter feed (@noblemaster) either.\n\nGood luck with your future endeavors!";
        stringArray18[1] = "C:0:C:0";
        stringArray[23] = stringArray18;
        return stringArray;
    }

    @Override
    public String getGameCode() {
        return "dsf";
    }

    @Override
    public String getGameId() {
        return "desertstormfront";
    }

    @Override
    public String getGameIdLite() {
        return "desertstormfront_lite";
    }

    @Override
    public MapDefinition getDefaultMap() {
        return this.defaultMap;
    }

    @Override
    public String getTitle() {
        return "DesertStormfront[i18n]: Desert Stormfront";
    }

    @Override
    public String getTitleLite() {
        return "DesertStormfrontLite[i18n]: Desert Stormfront LITE";
    }

    @Override
    public String getKeywords() {
        return "KeywordsDesertStormfrontETC[i18n]: war, real-time, strategy, game, battle, RTS, defense, campaign, tank, capture, survival, capture the flag, sea, land, air, desert, multiplayer, LAN, internet";
    }

    @Override
    public String getWebsiteUrl() {
        return "http://www.desertstormfront.com";
    }

    @Override
    public String getForumUrl() {
        return "http://www.multiplayerhub.com/board/viewforum.php?f=60";
    }

    @Override
    public String getCopyright() {
        return "Copyright \u00a9 2012 by Noble Master LLC";
    }

    @Override
    public String getCompanyUrl() {
        return "http://www.noblemaster.com";
    }

    @Override
    public String[] getMissionDescriptions() {
        return GameConfig.cleanContent ? this.missionDescriptionsClean : this.missionDescriptions;
    }

    @Override
    public int[] getCampaignMissionIndices() {
        return this.campaignMissionIndices;
    }

    @Override
    public String getCampaignStory() {
        return GameConfig.cleanContent ? "CampaignStoryBeginDesertStormfrontV01CleanETC[i18n]: Desert areas have often been a land of conflict. Various motives brought war, but most of all they has been a target for its resources. The most abundant of all, oil, is highly sought after. The blood of modern societies and with it, nations would crumble.\n\nIt's the dawn of war. The desert people have started to defy demands of the water people, halting oil exports to several countries. Desperate to gain power, the desert nations have began work on building a weapons arsenal ready to strike anywhere and any time. Actions had to be taken.\n\nWith war on their mind, the water nations have banded together to devise a plan with the goal to capture major cities, oil fields and military structures. Once key locations were secured, the coalition would negotiate with the governments and relinquish the land back to the people. But they would find that the desert nations would not give in so easily...\n" : "CampaignStoryBeginDesertStormfrontV01ETC[i18n]: The Middle East has often been a land of conflict. Various motives brought war to the region, but in more recent years the Middle East has been a target for its resources. The most abundant of all, oil, is highly sought after. The blood of modern societies and with it, nations would crumble.\n\nIt's the dawn of war. Iran has started to defy demands of the Western World, halting oil exports to several countries. Desperate to gain power, the nation has began work on building a nuclear arsenal. Several bombings in the USA and France have been directly linked to the Iranian government. Actions had to be taken.\n\nWith war on their mind, USA and France have banded together to devise a plan with the goal to capture major cities, oil fields and military structures in Iran including surrounding regions. Once key locations were secured, the Western coalition would negotiate with the governments and relinquish the land back to the people. But they would find that the Middle Eastern nations would not give in so easily...\n";
    }

    @Override
    public String getCampaignCompleteMessage() {
        return "CampaignStoreEndedDesertStormfrontV01ETC[i18n]: You have successfully completed all missions! Your strategies have proven extremely effective. You have defeated all enemy forces. Points awarded: {0}\n\nWe thank you for your service! We will consider your presence on future missions. Please stand by on future communication from us.\n\nCongratulations!\nHigh Command";
    }

    @Override
    public String[][] getCredits() {
        return this.credits;
    }

    @Override
    public String[][] getTechInfo() {
        return this.techInfo;
    }

    @Override
    public String getLicenses() {
        return "Licenses: libgx is licensed under the Apache License, Version 2.0. LWJGL is Copyright (c) 2002-2007 Lightweight Java Game Library Project. All rights reserved. \n";
    }

    @Override
    public String getAppOverview() {
        return GameConfig.cleanContent ? "AppOverviewDesertStormfrontCleanETC[i18n]: Desert Stormfront is a Real-Time Strategy (RTS) game situated in the desert. You play on hot desert landscapes for power and oil. Units at your disposal include Humvees, Tanks, Artillery, Mechanics, Helicopters, Planes, Ships and Submarines amongst others. Desert Stormfront supports multiplayer games over LAN and internet including co-op multiplayer." : "AppOverviewDesertStormfrontETC[i18n]: Desert Stormfront is a Real-Time Strategy (RTS) game situated in the Middle East. You play on hot desert landscapes for power and oil. Units at your disposal include Humvees, Tanks, Artillery, Mechanics, Helicopters, Planes, Ships and Submarines amongst others. Desert Stormfront supports multiplayer games over LAN and internet including co-op multiplayer.";
    }

    @Override
    public String getAppDescription() {
        return GameConfig.cleanContent ? "AppDescriptionDesertStormfrontCleanETC[i18n]: Desert Stormfront is a Real-Time Strategy (RTS) game situated in the desert. You play on hot desert landscapes for power and oil. Units at your disposal include Humvees, Tanks, Artillery, Mechanics, Helicopters, Planes, Ships and Submarines amongst others. Desert Stormfront supports multiplayer games over LAN and internet including co-op multiplayer.\n\nThe game features 30 pre-defined campaign missions where you take command and fight battles in the desert for control of major cities, oil fields and military structures.\n\nDesert Stormfront contains a random map generator in addition to the 30 campaign missions. Scenarios include eliminating enemy forces, capturing the flag, defending against incoming troops, convoy missions, sea and air battles as well as tank fights. The game keeps track of high scores and playing statistics." : "AppDescriptionDesertStormfrontETC[i18n]: Desert Stormfront is a Real-Time Strategy (RTS) game situated in the Middle East. You play on hot desert landscapes for power and oil. Units at your disposal include Humvees, Tanks, Artillery, Mechanics, Helicopters, Planes, Ships and Submarines amongst others. Desert Stormfront supports multiplayer games over LAN and internet including co-op multiplayer.\n\nThe game features 30 pre-defined campaign missions where you take command of the Western coalition and fight battles in the Middle East for control of major cities, oil fields and military structures. Nations in play are the USA, England, France, Italy, Iraq, Iran, Saudi Arabia, Egypt, Afghanistan plus a rebel faction.\n\nDesert Stormfront contains a random map generator in addition to the 30 campaign missions. Scenarios include eliminating enemy forces, capturing the flag, defending against incoming troops, convoy missions, sea and air battles as well as tank fights. The game keeps track of high scores and playing statistics.";
    }

    @Override
    public String getMiscFeatures() {
        return GameConfig.cleanContent ? "MiscFeaturesDesertStormfrontCleanETC[i18n]: - Real-Time Strategy (RTS) in the Desert\n- Single Player and Multiplayer (over the LAN and Internet)\n- 30 Campaign Missions\n- Random Map Generator\n- 16 Mobile Units including: Humvees, Tanks, Artillery, 4x4, Mechanic, Trucks, Planes, Chinook, Gunboat, Submarine, Cruiser and Carrier\n- 1 Special Unit: Super-Hovercraft\n- 4 Land Structures: Base Station, Shipyard, Airfield and Oil Field\n- Team-Play (included in multiplayer)\n- High Score and Playing Statistics\n- Fog of War/Exploration Fog\n- Challenging AI with 4 Difficulty Settings\n- Enganging Music and Sound Effects\n" : "MiscFeaturesDesertStormfrontETC[i18n]: - Real-Time Strategy (RTS) in the Middle East\n- Single Player and Multiplayer (over the LAN and Internet)\n- 30 Campaign Missions\n- Random Map Generator\n- 16 Mobile Units including: Humvees, Tanks, Artillery, 4x4, Mechanic, Trucks, Planes, Chinook, Gunboat, Submarine, Cruiser and Carrier\n- 1 Special Unit: Super-Hovercraft\n- 4 Land Structures: Base Station, Shipyard, Airfield and Oil Field\n- Nations: USA, England, France, Italy, Iraq, Iran, Saudi Arabia, Egypt, Afghanistan plus a rebel faction\n- Team-Play (included in multiplayer)\n- High Score and Playing Statistics\n- Fog of War/Exploration Fog\n- Challenging AI with 4 Difficulty Settings\n- Enganging Music and Sound Effects\n";
    }

    @Override
    public String getMiscNotes() {
        return "MiscNotesDesertStormfrontETC[i18n]: IMPORTANT: Please note that the game requires a screen resolution of 800x480 pixels or higher. Although the game still runs on a lower resolution, not all GUI elements will be rendered properly. The game has been thoroughly tested and runs at about 30+ frames/seconds. It is possible though that the application is slow on some devices. Please try the LITE version before purchase to verify proper function. If you encounter any problems running the game, try (1) a device restart (i.e. turn off completely) as well as (2) a complete re-install.\n\nThe LITE version of the game includes 4 campaign mission to allow you to evaluate the game. There is no time limit and the game does not contain any spyware, malware or third party software of such kind. Again, please try the LITE version first to verify the application works on your device. If you are having any problems or are not happy with your purchase, feel free to contact us via email at any time. For a full refund please include your order# with your message.\n\nAlso try our other strategy game Tropical Stormfront if you like to play the game in a more tropical environment :)\n\nThank you & Enjoy the Game!\nnoblemaster\n\nTwitter: http://twitter.com/noblemaster";
    }

    @Override
    public String getMiscPromo() {
        return "MiscPromoDesertStormfrontETC[i18n]: Desert Stormfront, real-time battles in the desert!";
    }

    @Override
    public String getInformationUnits() {
        return "InformationUnitsDesertStormfrontETC[i18n]: The game features 17 mobile units as well as 4 land structures. Structures enable you to build new units. The oil field generates a fixed income every {0} seconds. The only unit that can be used to take over structures is the Humvee.";
    }

    @Override
    public String getInformationStructures() {
        return "InformationListStructuresDesertStormfrontETC[i18n]: BASE STATION:\n - can build/host ground units\nSHIPYARD:\n - can build/host ships and submarines\nAIRFIELD:\n - can build/host air units\nOILFIELD:\n - generates income\n";
    }

    @Override
    public String getInformationMoveables() {
        return "InformationListMoveablesDesertStormfrontETC[i18n]: HUMVEE:\n - can take over structures\nBATTLE TANK:\n - strong against ground units\nMISSILE TANK:\n - strong against air units\nARTILLERY:\n - can attack from a far distance\n4X4:\n - fast but weak attack unit\n - produced by oil field\nMECHANIC:\n - auto-repairs units in the vincinity\nTRUCK:\n - for escort missions\n - no attack capability\nGENERAL/COMMANDO UNIT:\n - you lose if your general is killed\nFIGHTER PLANE:\n - for reconnaissance\n - strong against air units\n - limited fuel\nHELICOPTER:\n - strong against ground units\n - weak against fighter plane\n - limited fuel\nCHINOOK:\n - can carry one ground unit\n - no range limit, but generally weak\nGUNBOAT:\n - strong against submarines\nSUBMARINE:\n - strong against ships except gunboat\nCRUISER:\n - strong against ships and ground units\n - weak against submarine\nCARRIER:\n - can carry air units\n - weak against submarine\nTRANSPORT SHIP:\n - carries ground units\nHOVERCRAFT:\n - super-unit (very strong)\n - can travel on land and water\n - cannot be produced\n";
    }

    @Override
    public String[] getMusicFiles() {
        return this.musicFiles;
    }

    @Override
    public String getBuyFullVersionText() {
        return "BuyTheFullVersion[i18n]: Buy the Full Version";
    }

    @Override
    public String getBuyFullVersionDetails() {
        return "BuyTheFullVersionETC[i18n]: This LITE version is limited in scope. Please consider buying the full version with enhanced options:\n\n- play all the campaign missions\n- host multiplayer games\n- setup and play skirmish games\n- play all the nations (skirmish)\n- various setup options (skirmish)\n- stronger AI (variable difficulty)\n\nThank you!\nnoblemaster";
    }
}


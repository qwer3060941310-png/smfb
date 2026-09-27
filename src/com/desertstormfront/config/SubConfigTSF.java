/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.config;

import com.desertstormfront.config.AbstractGameInfo;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.TouchDeviceFlags;
import com.desertstormfront.map.MapDefinition;

/**
 * Ruleset metadata for the "Tropical Stormfront" game (game id {@code tropicalstormfront}) - the
 * Pacific / island campaign where the United Democratic Alliance faces the ODO alliance. Mirrors
 * {@link SubConfigDSF}: localized titles, the 25-mission campaign story and descriptions, credits
 * and tech-info, license text and store copy. Extends {@link AbstractGameInfo}; the unit/map rules
 * come from the {@code tsf} ruleset via {@link MapDefinition#getRuleset(String)}.
 */
public final class SubConfigTSF
extends AbstractGameInfo {
    private final String gameId = "tropicalstormfront";
    private final String gameIdLite = "tropicalstormfront_lite";
    private final MapDefinition defaultMap = MapDefinition.getRuleset("tsf");
    private final String title = "TropicalStormfront[i18n]: Tropical Stormfront";
    private final String titleLite = "TropicalStormfrontLite[i18n]: Tropical Stormfront LITE";
    private final String genre = "RealTimeStrategy[i18n]: Real-Time Strategy";
    private final String keywords = "KeywordsTropicalStormfrontETC[i18n]: war, real-time, strategy, game, battle, RTS, defense, campaign, tank, capture, survival, capture the flag, sea, land, air, multiplayer, LAN, internet";
    private final String websiteUrl = "http://www.tropicalstormfront.com";
    private final String forumUrl = "http://www.multiplayerhub.com/board/viewforum.php?f=60";
    private final String copyright = "Copyright \u00a9 2011 by Noble Master LLC";
    private final String companyUrl = "http://www.noblemaster.com";
    private final String[] missionDescriptions = new String[]{"Mission001DescriptionTropicalStormfrontV01ETC[i18n]: China has sent a commander to Isla Guadalupe to set up a small forward base. The USA will need to take the base out before it can become a real threat.", "Mission002DescriptionTropicalStormfrontV01ETC[i18n]: The presence of China has been detected on Palmyra Atoll. From the USA's small base, they will need to build up a naval force and attack the Chinese.", "Mission003DescriptionTropicalStormfrontV01ETC[i18n]: China and their ally Japan have refitted a base on Johnston Atoll and are beginning the production of aerial units. The USA will have to lead a naval squadron in and wipe them out.", "Mission004DescriptionTropicalStormfrontV01ETC[i18n]: Brazil has come under fire from China and Germany in the Falkland Islands. They are overwhelmed and must last until reinforcements can arrive.", "Mission005DescriptionTropicalStormfrontV01ETC[i18n]: Having survived against China and Germany, Brazil now takes the fight to them, alongside their American reinforcements.", "Mission006DescriptionTropicalStormfrontV01ETC[i18n]: While the Americans are busy elsewhere, Russia has offered to defend American Samoa. From Fiji and Tonga, China, India, and Japan all prepare to fight Russia.", "Mission007DescriptionTropicalStormfrontV01ETC[i18n]: England finds its first true conflict in the war. In the Pitcairn Islands, Japan and Germany must be taken out, so that UDA can continue West.", "Mission008DescriptionTropicalStormfrontV01ETC[i18n]: After success in the Pitcairn Islands, England faces Japan again, in New Caledonia.", "Mission009DescriptionTropicalStormfrontV01ETC[i18n]: The Americans must defend Hawaii at all costs against a massive assault from China, Japan, and Germany.", "Mission010DescriptionTropicalStormfrontV01ETC[i18n]: With Hawaii secured from further threats, USA moves to reclaim Midway Islands from China.", "Mission011DescriptionTropicalStormfrontV01ETC[i18n]: England meets up with USA and they move on to capture Minami Tori Shima from Japan.", "Mission012DescriptionTropicalStormfrontV01ETC[i18n]: Russia moves to capture the Philippines, where India has a massive presence.", "Mission013DescriptionTropicalStormfrontV01ETC[i18n]: With Russia now in control of the northern area of The Philippines, India launches a counterstrike in attempt to regain land.", "Mission014DescriptionTropicalStormfrontV01ETC[i18n]: In the Northern Mariana Islands, Japan and Germany launch an attack on USA and England.", "Mission015DescriptionTropicalStormfrontV01ETC[i18n]: USA and Brazil plan an attack against Japan around Daito Shoto. The attack has already began, and forward bases have been captured.", "Mission016DescriptionTropicalStormfrontV01ETC[i18n]: USA and China vie for control of Chichi-Shima.", "Mission017DescriptionTropicalStormfrontV01ETC[i18n]: England's previous victory at Pitcairn Islands is threatened. China, Japan, and Germany seek to recapture the area.", "Mission018DescriptionTropicalStormfrontV01ETC[i18n]: Hoping England's resources have run low, Japan and India prepare for their last major offensive.", "Mission019DescriptionTropicalStormfrontV01ETC[i18n]: China prepares a massive assault against USA on Midway Islands.", "Mission020DescriptionTropicalStormfrontV01ETC[i18n]: The conflict between Russia and India in the Philippines comes close to an end. Whichever side can hold the capital will claim victory here. It is likely India will surrender, should they be defeated.", "Mission021DescriptionTropicalStormfrontV01ETC[i18n]: USA and England launch an attack to claim Ryukyu Islands from Japan.", "Mission022DescriptionTropicalStormfrontV01ETC[i18n]: The fight has come to Japan. The USA moves in a squadron to attack Kyushu, defended by Japan, China, and Germany. Japan will be forced to surrender, should they lose here.", "Mission023DescriptionTropicalStormfrontV01ETC[i18n]: Russia, still in control of the American Samoa, Fiji, and Tonga area, prepares a defense against China and Germany's last major offensive.", "Mission024DescriptionTropicalStormfrontV01ETC[i18n]: USA plans to liberate the people of Taiwan. China and Germany defend. Germany's small presence here suggests they are withered and beaten, and will likely surrender soon.", "Mission025DescriptionTropicalStormfrontV01ETC[i18n]: With Taiwan liberated, USA prepares fresh forces. English and Russian naval squadrons come to assist in defeating China and ending the war."};
    private final String[] missionDescriptionsClean = new String[]{"Mission001DescriptionTropicalStormfrontV01CleanETC[i18n]: Your enemy has sent a commander to set up a small forward base. You will need to take the base out before it can become a real threat.", "Mission002DescriptionTropicalStormfrontV01CleanETC[i18n]: The presence of your enemy has been detected in your vincinity. From your small base, you will need to build up a naval force and engage the target.", "Mission003DescriptionTropicalStormfrontV01CleanETC[i18n]: Your enemy has refitted a base is beginning the production of aerial units. You will have to lead a naval squadron in and wipe them out.", "Mission004DescriptionTropicalStormfrontV01CleanETC[i18n]: You have come under fire from the enemy. You must last until reinforcements can arrive.", "Mission005DescriptionTropicalStormfrontV01CleanETC[i18n]: Having survived against the enemy attack, you will take the fight to them.", "Mission006DescriptionTropicalStormfrontV01CleanETC[i18n]: You are going into an offensive to bring the fight to your enemy. Take over their bases on the nearby islands.", "Mission007DescriptionTropicalStormfrontV01CleanETC[i18n]: You must enter into enemy territory and take out their bases in order to continue your quest.", "Mission008DescriptionTropicalStormfrontV01CleanETC[i18n]: After success battle, you face your enemy again.", "Mission009DescriptionTropicalStormfrontV01CleanETC[i18n]: You must protect your territory from a massive assault. Good luck!", "Mission010DescriptionTropicalStormfrontV01CleanETC[i18n]: Having survived the threat you have to advance and reclaim more territory.", "Mission011DescriptionTropicalStormfrontV01CleanETC[i18n]: You need to capture a strategic island from your enemy to continue the campaign.", "Mission012DescriptionTropicalStormfrontV01CleanETC[i18n]: You have to capture a heavily fortified half island or else.", "Mission013DescriptionTropicalStormfrontV01CleanETC[i18n]: Having taken control, your enemy launches a counterstrike in attempt to regain land.", "Mission014DescriptionTropicalStormfrontV01CleanETC[i18n]: Protect your island chain against a sea attack from multiple directions. Hold out until reinforcements arrive!", "Mission015DescriptionTropicalStormfrontV01CleanETC[i18n]: You are being attacked! Some of your forward bases have been captured. Take back what is yours and lanch a counter-strike.", "Mission016DescriptionTropicalStormfrontV01CleanETC[i18n]: You vie for control of an important island.", "Mission017DescriptionTropicalStormfrontV01CleanETC[i18n]: Your enemy is trying to recapture land they have lost previously. Ward off the attack", "Mission018DescriptionTropicalStormfrontV01CleanETC[i18n]: Your enemy is preparing for a major offensive. Be ready!", "Mission019DescriptionTropicalStormfrontV01CleanETC[i18n]: A massive assault is being prepared against you. Don't lose!", "Mission020DescriptionTropicalStormfrontV01CleanETC[i18n]: The conflict is slowly coming to an end. Claim victory now to ultimately force your enemy to surrender.", "Mission021DescriptionTropicalStormfrontV01CleanETC[i18n]: You launch another attack to yet claim territory from your enemy.", "Mission022DescriptionTropicalStormfrontV01CleanETC[i18n]: You move in a squadron to attack the enemy in its home location.", "Mission023DescriptionTropicalStormfrontV01CleanETC[i18n]: You must survive against a last major offensive in order to continue your quest.", "Mission024DescriptionTropicalStormfrontV01CleanETC[i18n]: Your enemy has been severly withered and beaten, and will likely surrender soon. Go in with full force!", "Mission025DescriptionTropicalStormfrontV01CleanETC[i18n]: Launch a naval attack to take over your enemy's last position to finally end the war."};
    private final int[] campaignMissionIndices = new int[]{1, 2, 9, 11};
    private final String campaignStory = "CampaignStoryBeginTropicalStormfrontV01ETC[i18n]: Anger, hate, greed, revenge, it was inevitable. But it was not until now that it became so clear who was fighting who, and what for. China, the red nation poised on domination, began to attack lesser third world countries. The imperialistic war machine imposing their power and beliefs. The world cried for help, and there were some who rose to the call. The United States of America, Russia, and England all began to openly work against China under the name United Democratic Alliance, or UDA for short. While strong, the Chinese government sought allies. Japan and Germany shared many of their views and began to aid them, first supplies, then with military action. Together they formed the alliance of Order, Discipline, and Obedience, or ODO.\n\nThe centerpiece of the war quickly became the Pacific. The large body of water cluttered with islands was the only thing between USA and China, the two powerhouses in the war. While warfare was waged elsewhere, it became clear that whichever side could hold the Pacific would have the upper hand. With Russia's main focus on their border with China, UDA needed more allies. Brazil rallied to their side after a great deal of persuasion. On the other side of the world, India became an ally to China, for fear that if they did not, they would be consumed by them.\n\nThe sides were drawn, the stage set. The war would now truly begin and no one knew how it would end. Will ODO win and demand the world change to their imperialistic ways? Would the UDA be able to stop them and drive them back to their homelands? Lead the UDA on an exciting campaign as you try to push back the tide of war, so that the world may see peace once again.\n";
    private final String campaignStoryClean = "CampaignStoryBeginTropicalStormfrontCleanV01ETC[i18n]: Anger, hate, greed, revenge, it was inevitable. But it was not until now that it became so clear who was fighting who, and what for. One nation poised on domination, began to attack lesser countries. An imperialistic war machine imposing their power and beliefs. The world cried for help, and there were some who rose to the call.\n\nThe centerpiece of the war, a large body of water cluttered with islands was the only thing between the powers involved in the war. It became clear that whichever side could hold the sea would have the upper hand.\n\nThe sides were drawn, the stage set. The war would now truly begin and no one knew how it would end. Take command in an epic power struggle and push back the tide of war so that the world may see peace once again.\n";
    private final String campaignCompleteMessage = "CampaignStoreEndedTropicalStormfrontV01ETC[i18n]: You have successfully completed all missions! Your strategies have proven highly effective. You have defeated all enemy forces. Points awarded: {0}\n\nWe thank you for your service! We will consider your presence on future missions. Please stand by on future communication from us.\n\nCongratulations!\nHigh Command";
    private final String[][] credits;
    private final String[][] techInfo;
    private final String licenses = "Licenses: libgx is licensed under the Apache License, Version 2.0. LWJGL is Copyright (c) 2002-2007 Lightweight Java Game Library Project. All rights reserved. \n";
    private final String appOverview = "AppOverviewTropicalStormfrontETC[i18n]: Tropical Stormfront is a Real-Time Strategy (RTS) game in a tropical setting. Take command and join the ultimate war of good versus evil. Defend your freedom against the forces of darkness. Fight real-time battles in the tropics and become the ultimate leader. Play multiplayer games over LAN and internet including co-op multiplayer.";
    private final String appOverviewClean = "AppOverviewTropicalStormfrontCleanETC[i18n]: Tropical Stormfront is a Real-Time Strategy (RTS) game in a tropical setting. Play real-time battles over tropical archipelagos and become the ultimate leader. Units at your disposal include Humvees, Tanks, Artillery, Helicopters, Planes, Ships and Submarines amongst others. Tropical Stormfront supports multiplayer games over LAN and internet including co-op multiplayer.";
    private final String appDescription = "AppDescriptionTropicalStormfrontETC[i18n]: Tropical Stormfront is Real-Time Strategy (RTS) game in a tropical setting. Take command and join the ultimate war of good versus evil. Defend your freedom against the forces of darkness. Fight real-time battles in the tropics and become the ultimate leader. Play multiplayer games over LAN and internet including co-op multiplayer.\n\nThe game features a multitude of pre-defined missions such as survival mode, eliminate all the enemy forces, capture the flag, hold the flag, defend against incoming forces, capture the enemy general, sea battles, air battles as well as tank fights.\n\nTo complete the objectives you take command of the United Democratic Alliance (UDA) and fight against the evil forces of Order, Discipline and Obedience (ODO). Choose the right strategies for each of the battles and you will claim ultimate victory!";
    private final String appDescriptionClean = "AppDescriptionTropicalStormfrontCleanETC[i18n]: Tropical Stormfront is a Real-Time Strategy (RTS) game in a tropical setting. Play real-time battles over tropical archipelagos and become the ultimate leader. Units at your disposal include Humvees, Tanks, Artillery, Helicopters, Planes, Ships and Submarines amongst others. Tropical Stormfront supports multiplayer games over LAN and internet including co-op multiplayer.\n\nThe game features 25 pre-defined campaign missions where you take command and fight battles in the tropics for control of major islands and military structures. Fight battles on land, sea and in the air.\n\nTropical Stormfront contains a random map generator in addition to the 25 campaign missions. Scenarios include eliminating enemy forces, capturing the flag, defending against incoming troops, sea and air battles as well as tank fights. The game keeps track of high scores and playing statistics.";
    private final String miscFeatures = "MiscFeaturesTropicalStormfrontETC[i18n]: - Real-Time Strategy (RTS) in the Tropics\n- Multiplayer over LAN and Internet\n- Campaign Missions\n- Skirmish Games (Random Setup)\n- Challenging AI\n- Land: Battle Tank, Missile Tank, Artillery, General/Commando Unit\n- Ships: Aircraft Carrier, Cruiser, Destroyer, Submarine, Transporter\n- Air: Fighter Plane, Apache Helicopter, Airship\n- Nations Include: USA, England, Russia, China, Japan, India and Brazil\n- Team-Play (together with the AI)\n- Fog of War/Exploration Fog\n- High Score and Playing Statistics\n- Engaging Music and Sound Effects";
    private final String miscFeaturesClean = "MiscFeaturesTropicalStormfrontCleanETC[i18n]: - Real-Time Strategy (RTS) in the Tropics\n- Multiplayer over LAN and Internet\n- Campaign Missions\n- Skirmish Games (Random Setup)\n- Challenging AI\n- Land: Battle Tank, Missile Tank, Artillery, General/Commando Unit\n- Ships: Aircraft Carrier, Cruiser, Destroyer, Submarine, Transporter\n- Air: Fighter Plane, Apache Helicopter, Airship\n- Team-Play (together with the AI)\n- Fog of War/Exploration Fog\n- High Score and Playing Statistics\n- Engaging Music and Sound Effects";
    private final String miscNotes = "MiscNotesTropicalStormfrontETC[i18n]: IMPORTANT: Please note that the game requires a screen resolution of 800x480 pixels or higher. Although the game still runs on a lower resolution, not all GUI elements will be rendered properly. The game has been thoroughly tested and runs at about 30+ frames/seconds. It is possible though that the application is slow on some devices. Please try the LITE version before purchase to verify proper function. If you encounter any problems running the game, try (1) a device restart (i.e. turn off completely) as well as (2) a complete re-install.\n\nThe LITE version of the game includes 4 campaign mission to allow you to evaluate the game. There is no time limit and the game does not contain any spyware, malware or third party software of such kind. Again, please try the LITE version first to verify the application works on your device. If you are having any problems or are not happy with your purchase, feel free to contact us via email at any time. For a full refund please include your order# with your message.\n\nThank you & Enjoy the Game!\nnoblemaster\n\nTwitter: http://twitter.com/noblemaster";
    private final String miscPromo = "MiscPromoTropicalStormfrontETC[i18n]: Tropical Stormfront, real-time battles in the tropics!";
    private final String informationUnits = "InformationUnitsTropicalStormfrontETC[i18n]: The game features 12 mobile units as well as 3 land structures. Structures enable you to build new units and generate a fixed income every {0} seconds. The only unit that can be used to take over structures is the battle tank.";
    private final String informationStructures = "InformationListStructuresTropicalStormfrontETC[i18n]: BASE STATION:\n - can build/host ground units\nSHIPYARD:\n - can build/host ships and submarines\nAIRFIELD:\n - can build/host air units\n";
    private final String informationMoveables = "InformationListMoveablesTropicalStormfrontETC[i18n]: BATTLE TANK:\n - can take over structures\nMISSILE TANK:\n - strong against air units\nARTILLERY:\n - can attack from a far distance\nGENERAL/COMMANDO UNIT:\n - you lose if your general is killed\nFIGHTER PLANE:\n - for reconnaissance\n - strong against air units\n - limited fuel\nHELICOPTER:\n - strong against ground units\n - weak against fighter plane\n - limited fuel\nAIRSHIP:\n - can carry one ground unit\n - no range limit, but generally weak\nDESTROYER:\n - strong against submarines\nSUBMARINE:\n - strong against ships except destroyer\nCRUISER:\n - strong against ships and ground units\n - weak against submarine\nCARRIER:\n - can carry air units\n - weak against submarine\nTRANSPORT SHIP:\n - carries ground units\n";
    private final String[] musicFiles;
    private final String buyFullVersionText = "BuyTheFullVersion[i18n]: Buy the Full Version";
    private final String buyFullVersionDetails = "BuyTheFullVersionETC[i18n]: This LITE version is limited in scope. Please consider buying the full version with enhanced options:\n\n- play all the campaign missions\n- host multiplayer games\n- setup and play skirmish games\n- play all the nations (skirmish)\n- various setup options (skirmish)\n- stronger AI (variable difficulty)\n\nThank you!\nnoblemaster";

    public SubConfigTSF() {
        String[][] stringArrayArray = new String[43][];
        stringArrayArray[0] = new String[]{"Developer", "Noble Master LLC", "http://www.noblemaster.com"};
        stringArrayArray[1] = new String[]{"Design", "Christoph Aschwanden", "ceo@noblemaster.com"};
        stringArrayArray[2] = new String[]{"Design", "Nick Lee", "http://nickrlee.carbonmade.com"};
        stringArrayArray[3] = new String[]{"Program", "Christoph Aschwanden", "ceo@noblemaster.com"};
        stringArrayArray[4] = new String[]{"Graphics and Art", "Nick Lee", "http://nickrlee.carbonmade.com"};
        String[] stringArray = new String[3];
        stringArray[0] = "Animation";
        stringArray[1] = "Steve Rowlands";
        stringArrayArray[5] = stringArray;
        stringArrayArray[6] = new String[]{"Music", "Sean Beeson", "http://seanbeeson.com/"};
        stringArrayArray[7] = new String[]{"Sound FX", "Matthew Myers", "http://www.2eastmusic.com"};
        String[] stringArray2 = new String[3];
        stringArray2[0] = "Modding";
        stringArray2[1] = "Travis Bowling";
        stringArrayArray[8] = stringArray2;
        String[] stringArray3 = new String[3];
        stringArray3[0] = "Trailer Video";
        stringArray3[1] = "Kieth Jones";
        stringArrayArray[9] = stringArray3;
        String[] stringArray4 = new String[3];
        stringArray4[0] = "Portraits";
        stringArray4[1] = "Dan Malone";
        stringArrayArray[10] = stringArray4;
        String[] stringArray5 = new String[3];
        stringArray5[0] = "Publisher (Spanish)";
        stringArray5[1] = "Dark Game, http://www.darkgame.es";
        stringArrayArray[11] = stringArray5;
        String[] stringArray6 = new String[3];
        stringArray6[0] = "Soporte en espa\u00f1ol";
        stringArray6[1] = "soporte@darkgame.es";
        stringArrayArray[12] = stringArray6;
        String[] stringArray7 = new String[3];
        stringArray7[0] = "Support in Spanish";
        stringArray7[1] = "soporte@darkgame.es";
        stringArrayArray[13] = stringArray7;
        String[] stringArray8 = new String[3];
        stringArray8[0] = "Translation JA";
        stringArray8[1] = "Hiroyuki Kobuna";
        stringArrayArray[14] = stringArray8;
        String[] stringArray9 = new String[3];
        stringArray9[0] = "Translation JA";
        stringArray9[1] = "Minori Iio";
        stringArrayArray[15] = stringArray9;
        String[] stringArray10 = new String[3];
        stringArray10[0] = "Translation JA";
        stringArray10[1] = "Shunsuke Fuji";
        stringArrayArray[16] = stringArray10;
        String[] stringArray11 = new String[3];
        stringArray11[0] = "Translation JA";
        stringArray11[1] = "Yuka Shimotori";
        stringArrayArray[17] = stringArray11;
        String[] stringArray12 = new String[3];
        stringArray12[0] = "Translation RU";
        stringArray12[1] = "Alexander Lee";
        stringArrayArray[18] = stringArray12;
        String[] stringArray13 = new String[3];
        stringArray13[0] = "Translation KO";
        stringArray13[1] = "JinSeog Hong";
        stringArrayArray[19] = stringArray13;
        stringArrayArray[20] = new String[]{"Translation KO", "Sookhee Im", "http://www.jibnetworks.com"};
        String[] stringArray14 = new String[3];
        stringArray14[0] = "Translation DE";
        stringArray14[1] = "Sabine Aschwanden";
        stringArrayArray[21] = stringArray14;
        String[] stringArray15 = new String[3];
        stringArray15[0] = "Translation FR";
        stringArray15[1] = "Patrice Gilbert";
        stringArrayArray[22] = stringArray15;
        String[] stringArray16 = new String[3];
        stringArray16[0] = "Translation ES";
        stringArray16[1] = "Dark Game, http://www.darkgame.es";
        stringArrayArray[23] = stringArray16;
        String[] stringArray17 = new String[3];
        stringArray17[0] = "Translation ZH";
        stringArray17[1] = "Immanitas";
        stringArrayArray[24] = stringArray17;
        String[] stringArray18 = new String[3];
        stringArray18[0] = "Translation TR";
        stringArray18[1] = "Batuhan Arslan";
        stringArrayArray[25] = stringArray18;
        String[] stringArray19 = new String[3];
        stringArray19[0] = "Translation TR";
        stringArray19[1] = "Aykut Yilmaz";
        stringArrayArray[26] = stringArray19;
        String[] stringArray20 = new String[3];
        stringArray20[0] = "Text";
        stringArray20[1] = "Nick's Dad";
        stringArrayArray[27] = stringArray20;
        stringArrayArray[28] = new String[]{"Testing", "Nick Lee", "http://nickrlee.carbonmade.com"};
        String[] stringArray21 = new String[3];
        stringArray21[0] = "Testing";
        stringArray21[1] = "Travis Bowling";
        stringArrayArray[29] = stringArray21;
        String[] stringArray22 = new String[3];
        stringArray22[0] = "Testing";
        stringArray22[1] = "Axachu Odna";
        stringArrayArray[30] = stringArray22;
        String[] stringArray23 = new String[3];
        stringArray23[0] = "Testing";
        stringArray23[1] = "Chad Moore";
        stringArrayArray[31] = stringArray23;
        String[] stringArray24 = new String[3];
        stringArray24[0] = "Testing";
        stringArray24[1] = "Branko Kotur";
        stringArrayArray[32] = stringArray24;
        String[] stringArray25 = new String[3];
        stringArray25[0] = "Testing";
        stringArray25[1] = "Paul Antonucci";
        stringArrayArray[33] = stringArray25;
        String[] stringArray26 = new String[3];
        stringArray26[0] = "Testing";
        stringArray26[1] = "Edward Smith";
        stringArrayArray[34] = stringArray26;
        String[] stringArray27 = new String[3];
        stringArray27[0] = "Testing";
        stringArray27[1] = "ArcticViper";
        stringArrayArray[35] = stringArray27;
        String[] stringArray28 = new String[3];
        stringArray28[0] = "Testing";
        stringArray28[1] = "Sabine Aschwanden";
        stringArrayArray[36] = stringArray28;
        String[] stringArray29 = new String[3];
        stringArray29[0] = "Testing";
        stringArray29[1] = "Lara Aschwanden";
        stringArrayArray[37] = stringArray29;
        String[] stringArray30 = new String[3];
        stringArray30[0] = "Testing";
        stringArray30[1] = "Dr. Curtis Ikehara";
        stringArrayArray[38] = stringArray30;
        String[] stringArray31 = new String[3];
        stringArray31[0] = "Testing";
        stringArray31[1] = "Shunsuke Fuji";
        stringArrayArray[39] = stringArray31;
        String[] stringArray32 = new String[3];
        stringArray32[0] = "Testing";
        stringArray32[1] = "Minori Iio";
        stringArrayArray[40] = stringArray32;
        stringArrayArray[41] = new String[]{"Testing", "Ping-Yi Ho", "http://www.facebook.com/isis.ho1"};
        stringArrayArray[42] = new String[]{"Testing", "Team \"Reddit\"", "http://www.reddit.com"};
        this.credits = stringArrayArray;
        this.techInfo = new String[][]{{"Programming Language", "Java", "http://www.java.com"}, {"Game Development Library", "libgdx", "http://code.google.com/p/libgdx/"}, {"Development Environment", "Eclipse", "http://www.eclipse.org"}, {"Audio Editing", "Audacity", "http://audacity.sourceforge.net"}, {"Graphics Editing", "Photoshop", "http://www.photoshop.com"}, {"Android Runtime", "Android", "http://www.android.com"}, {"iOS Runtime", "RoboVM", "http://www.robovm.org"}, {"Desktop Runtime", "Java", "http://www.java.com"}, {"Desktop OpenGL", "LWJGL", "http://www.lwjgl.org/"}};
        this.musicFiles = new String[]{"music_ingame_apocalypse.mp3", "music_ingame_apocalypse_battle_drums.mp3", "music_ingame_dark_intruder.mp3", "music_ingame_destined_to_glory.mp3", "music_ingame_disaster_and_rescue.mp3", "music_ingame_eternal_fire.mp3", "music_ingame_fatal_fight.mp3", "music_ingame_forged_in_fire.mp3", "music_ingame_paradise_lost.mp3", "music_ingame_searching_for_the_enemy.mp3", "music_ingame_the_dark_hero.mp3", "music_ingame_kats_quest.mp3"};
    }

    @Override
    public String[][] getTutorials() {
        String[][] stringArray = new String[23][];
        String[] stringArray2 = new String[4];
        stringArray2[0] = "Tutorial00TropicalStormfrontETC[i18n]: Welcome to the tutorial. Let's find out if we can explain the basic game-play options to you ;-)";
        stringArray2[1] = "C:0:C:0";
        stringArray[0] = stringArray2;
        String[] stringArray3 = new String[4];
        stringArray3[0] = "Tutorial01TropicalStormfrontETC[i18n]: Tropical Stormfront is a real-time strategy game. Your goal is to complete the mission in the black text box below.\n\nClick [OK] to start the mission.";
        stringArray3[1] = "C:0:CB:10";
        stringArray3[2] = "AcceptMission";
        stringArray[1] = stringArray3;
        String[] stringArray4 = new String[4];
        stringArray4[0] = "Tutorial02TropicalStormfrontETC[i18n]: The game needs to initialize the map to provide for smooth gameplay. Apologies for the delay. Please stand by...";
        stringArray4[1] = "C:0:CB:-50";
        stringArray4[2] = "InitComplete";
        stringArray[2] = stringArray4;
        String[] stringArray5 = new String[4];
        stringArray5[0] = "Tutorial03TropicalStormfrontETC[i18n]: The number above represents the monetary funds at your disposal to purchase units. Income is generated every 80 seconds from all the structures you own.";
        stringArray5[1] = "L:0:T:50";
        stringArray[3] = stringArray5;
        String[] stringArray6 = new String[4];
        stringArray6[0] = "Tutorial04TropicalStormfrontETC[i18n]: The buttons below allow to group units. To group, press the button and then drag select the units on the map.";
        stringArray6[1] = "R:0:B:-73";
        stringArray[4] = stringArray6;
        String[] stringArray7 = new String[4];
        stringArray7[0] = "Tutorial05TropicalStormfrontETC[i18n]: The structure marked with the green arrow belongs to you. It's the base station and can build ground units.";
        stringArray7[1] = "R:0:T:0";
        stringArray7[3] = "AD:1";
        stringArray[5] = stringArray7;
        String[] stringArray8 = new String[4];
        stringArray8[0] = "Tutorial06TropicalStormfrontETC[i18n]: The following base station belongs to your enemy.";
        stringArray8[1] = "R:0:T:0";
        stringArray8[3] = "AD:2";
        stringArray[6] = stringArray8;
        stringArray[7] = new String[]{"Tutorial07TropicalStormfrontETC[i18n]: Let's build a battle tank, so we can attack the enemy. Please note the battle tank is the only unit that can take over structures.\n\nTask: (1) Click on your base station and (2) select the blue button to build a tank.", "R:0:T:0", "BuildGround", "AD:1"};
        String[] stringArray9 = new String[4];
        stringArray9[0] = "Tutorial08TropicalStormfrontETC[i18n]: Excellent! You can close the base station panel by clicking on the [x] button.\n\nTask: Close the panel for the base station.";
        stringArray9[1] = "R:0:T:0";
        stringArray9[2] = "ClosePanel";
        stringArray[8] = stringArray9;
        String[] stringArray10 = new String[4];
        stringArray10[0] = !TouchDeviceFlags.isTouchDevice() ? "Tutorial09TropicalStormfrontETC[i18n]: While we wait for the tank to be build, let's practice how to change the view. You can change the visible part of the map with your pointer.\n\nTask: Move the map." : "Tutorial09BTropicalStormfrontETC[i18n]: While we wait for the tank to be build, let's practice how to change the view. You can change the visible part of the map with your pointer.\n\nTask: Press ASDW on the keyboard or move your mouse to the edges of the screen. Mouse and keyboard settings can be adjusted in the [Options] screen.";
        stringArray10[1] = "R:0:T:0";
        stringArray10[2] = "MoveMap";
        stringArray[9] = stringArray10;
        String[] stringArray11 = new String[4];
        stringArray11[0] = !TouchDeviceFlags.isTouchDevice() ? "Tutorial10TropicalStormfrontETC[i18n]: Good job! Below is a transport ship at your disposal. We'll use it to ferry your newly built tank over to the enemy position." : "Tutorial10BTropicalStormfrontETC[i18n]: Good job! Below is a transport ship at your disposal. We'll use it to ferry your newly built tank over to the enemy position.\n\nNote: select units with the LEFT mouse button and make them move (targeting) with the RIGHT mouse button.";
        stringArray11[1] = "R:0:T:0";
        stringArray11[3] = "AD:3";
        stringArray[10] = stringArray11;
        stringArray[11] = new String[]{"Tutorial11TropicalStormfrontETC[i18n]: Your tank should be built be now. Let's move it into your transport thip.\n\nTask: (1) Click on your base station and (2) select the tank by pressing the yellow button.", "R:0:T:0", "SelectGround", "AD:1"};
        stringArray[12] = new String[]{"Tutorial12TropicalStormfrontETC[i18n]: Let's load the tank into the transport thip.\n\nTask: Click on your transport ship to move your tank there.", "R:0:T:0", "MoveGroundToMobileHost", "AS:4:T:3"};
        String[] stringArray12 = new String[4];
        stringArray12[0] = "Tutorial13TropicalStormfrontETC[i18n]: Great work! Let's wait until the tank has entered the transport ship...";
        stringArray12[1] = "R:0:T:0";
        stringArray12[2] = "LoadGroundIntoMobileHost";
        stringArray[13] = stringArray12;
        stringArray[14] = new String[]{"Tutorial14TropicalStormfrontETC[i18n]: Let's move your transport ship towards the enemy situated on the eastern island.\n\nTask: (1) Click on your transport and (2) target the enemy beach to the east.", "R:0:T:0", "MoveMobileHost", "AS:3:T:Z080807080707"};
        stringArray[15] = new String[]{"Tutorial15TropicalStormfrontETC[i18n]: Yep, that's it! Let's wait for the transport ship to reach the enemy coast...", "R:0:T:0", "LandMobileHost", "Z080807080707"};
        stringArray[16] = new String[]{"Tutorial16TropicalStormfrontETC[i18n]: Let's attack and capture the enemy base station with your tank.\n\nTask: (1) Click on your transport ship and (2) select your tank.", "R:0:T:0", "SelectGroundInMobileHost", "AD:3"};
        stringArray[17] = new String[]{"Tutorial17TropicalStormfrontETC[i18n]: Order the tank to attack the enemy base station.\n\nTask: Click on the enemy base station to move your tank there.", "R:0:T:0", "MoveGroundToEnemyFixed", "AS:4:T:2"};
        String[] stringArray13 = new String[4];
        stringArray13[0] = "Tutorial18TropicalStormfrontETC[i18n]: Well done! Sit back and relax. Let's wait for your tank to destroy the enemy base station...";
        stringArray13[1] = "R:0:T:0";
        stringArray13[2] = "DestroyEnemyFixed";
        stringArray[18] = stringArray13;
        String[] stringArray14 = new String[4];
        stringArray14[0] = "Tutorial19TropicalStormfrontETC[i18n]: Great job! That wasn't hard, wasn't it? You have successfully completed the tutorial by destroying the enemy player :)";
        stringArray14[1] = "C:0:C:0";
        stringArray[19] = stringArray14;
        String[] stringArray15 = new String[4];
        stringArray15[0] = "Tutorial20TropicalStormfrontETC[i18n]: Please note, the same way you have attacked the enemy base station, you can attack enemy mobile units as well.\n\nBe aware, if there is a general in play make sure to protect him/her. You lose your general, you lose the game.";
        stringArray15[1] = "C:0:C:0";
        stringArray[20] = stringArray15;
        String[] stringArray16 = new String[4];
        stringArray16[0] = "Tutorial21TropicalStormfrontETC[i18n]: Other unit actions available are [Patrol] which makes a unit patrol between two positions. [Repair] will make a unit go repair itself and return back to it's original location thereafter. A double-click will [Stop] a unit. Air units can be set to [Auto] mode which will make them to move on their own.";
        stringArray16[1] = "C:0:C:0";
        stringArray[21] = stringArray16;
        String[] stringArray17 = new String[4];
        stringArray17[0] = "Tutorial22TropicalStormfrontETC[i18n]: Please have a look at the manual and visit the web site and forums for more information regarding game play and strategies.\n\nGood luck with your future endeavors!";
        stringArray17[1] = "C:0:C:0";
        stringArray[22] = stringArray17;
        return stringArray;
    }

    @Override
    public String getGameCode() {
        return "tsf";
    }

    @Override
    public String getGameId() {
        return "tropicalstormfront";
    }

    @Override
    public String getGameIdLite() {
        return "tropicalstormfront_lite";
    }

    @Override
    public MapDefinition getDefaultMap() {
        return this.defaultMap;
    }

    @Override
    public String getTitle() {
        return "TropicalStormfront[i18n]: Tropical Stormfront";
    }

    @Override
    public String getTitleLite() {
        return "TropicalStormfrontLite[i18n]: Tropical Stormfront LITE";
    }

    @Override
    public String getKeywords() {
        return "KeywordsTropicalStormfrontETC[i18n]: war, real-time, strategy, game, battle, RTS, defense, campaign, tank, capture, survival, capture the flag, sea, land, air, multiplayer, LAN, internet";
    }

    @Override
    public String getWebsiteUrl() {
        return "http://www.tropicalstormfront.com";
    }

    @Override
    public String getForumUrl() {
        return "http://www.multiplayerhub.com/board/viewforum.php?f=60";
    }

    @Override
    public String getCopyright() {
        return "Copyright \u00a9 2011 by Noble Master LLC";
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
        return GameConfig.cleanContent ? "CampaignStoryBeginTropicalStormfrontCleanV01ETC[i18n]: Anger, hate, greed, revenge, it was inevitable. But it was not until now that it became so clear who was fighting who, and what for. One nation poised on domination, began to attack lesser countries. An imperialistic war machine imposing their power and beliefs. The world cried for help, and there were some who rose to the call.\n\nThe centerpiece of the war, a large body of water cluttered with islands was the only thing between the powers involved in the war. It became clear that whichever side could hold the sea would have the upper hand.\n\nThe sides were drawn, the stage set. The war would now truly begin and no one knew how it would end. Take command in an epic power struggle and push back the tide of war so that the world may see peace once again.\n" : "CampaignStoryBeginTropicalStormfrontV01ETC[i18n]: Anger, hate, greed, revenge, it was inevitable. But it was not until now that it became so clear who was fighting who, and what for. China, the red nation poised on domination, began to attack lesser third world countries. The imperialistic war machine imposing their power and beliefs. The world cried for help, and there were some who rose to the call. The United States of America, Russia, and England all began to openly work against China under the name United Democratic Alliance, or UDA for short. While strong, the Chinese government sought allies. Japan and Germany shared many of their views and began to aid them, first supplies, then with military action. Together they formed the alliance of Order, Discipline, and Obedience, or ODO.\n\nThe centerpiece of the war quickly became the Pacific. The large body of water cluttered with islands was the only thing between USA and China, the two powerhouses in the war. While warfare was waged elsewhere, it became clear that whichever side could hold the Pacific would have the upper hand. With Russia's main focus on their border with China, UDA needed more allies. Brazil rallied to their side after a great deal of persuasion. On the other side of the world, India became an ally to China, for fear that if they did not, they would be consumed by them.\n\nThe sides were drawn, the stage set. The war would now truly begin and no one knew how it would end. Will ODO win and demand the world change to their imperialistic ways? Would the UDA be able to stop them and drive them back to their homelands? Lead the UDA on an exciting campaign as you try to push back the tide of war, so that the world may see peace once again.\n";
    }

    @Override
    public String getCampaignCompleteMessage() {
        return "CampaignStoreEndedTropicalStormfrontV01ETC[i18n]: You have successfully completed all missions! Your strategies have proven highly effective. You have defeated all enemy forces. Points awarded: {0}\n\nWe thank you for your service! We will consider your presence on future missions. Please stand by on future communication from us.\n\nCongratulations!\nHigh Command";
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
        return GameConfig.cleanContent ? "AppOverviewTropicalStormfrontCleanETC[i18n]: Tropical Stormfront is a Real-Time Strategy (RTS) game in a tropical setting. Play real-time battles over tropical archipelagos and become the ultimate leader. Units at your disposal include Humvees, Tanks, Artillery, Helicopters, Planes, Ships and Submarines amongst others. Tropical Stormfront supports multiplayer games over LAN and internet including co-op multiplayer." : "AppOverviewTropicalStormfrontETC[i18n]: Tropical Stormfront is a Real-Time Strategy (RTS) game in a tropical setting. Take command and join the ultimate war of good versus evil. Defend your freedom against the forces of darkness. Fight real-time battles in the tropics and become the ultimate leader. Play multiplayer games over LAN and internet including co-op multiplayer.";
    }

    @Override
    public String getAppDescription() {
        return GameConfig.cleanContent ? "AppDescriptionTropicalStormfrontCleanETC[i18n]: Tropical Stormfront is a Real-Time Strategy (RTS) game in a tropical setting. Play real-time battles over tropical archipelagos and become the ultimate leader. Units at your disposal include Humvees, Tanks, Artillery, Helicopters, Planes, Ships and Submarines amongst others. Tropical Stormfront supports multiplayer games over LAN and internet including co-op multiplayer.\n\nThe game features 25 pre-defined campaign missions where you take command and fight battles in the tropics for control of major islands and military structures. Fight battles on land, sea and in the air.\n\nTropical Stormfront contains a random map generator in addition to the 25 campaign missions. Scenarios include eliminating enemy forces, capturing the flag, defending against incoming troops, sea and air battles as well as tank fights. The game keeps track of high scores and playing statistics." : "AppDescriptionTropicalStormfrontETC[i18n]: Tropical Stormfront is Real-Time Strategy (RTS) game in a tropical setting. Take command and join the ultimate war of good versus evil. Defend your freedom against the forces of darkness. Fight real-time battles in the tropics and become the ultimate leader. Play multiplayer games over LAN and internet including co-op multiplayer.\n\nThe game features a multitude of pre-defined missions such as survival mode, eliminate all the enemy forces, capture the flag, hold the flag, defend against incoming forces, capture the enemy general, sea battles, air battles as well as tank fights.\n\nTo complete the objectives you take command of the United Democratic Alliance (UDA) and fight against the evil forces of Order, Discipline and Obedience (ODO). Choose the right strategies for each of the battles and you will claim ultimate victory!";
    }

    @Override
    public String getMiscFeatures() {
        return GameConfig.cleanContent ? "MiscFeaturesTropicalStormfrontCleanETC[i18n]: - Real-Time Strategy (RTS) in the Tropics\n- Multiplayer over LAN and Internet\n- Campaign Missions\n- Skirmish Games (Random Setup)\n- Challenging AI\n- Land: Battle Tank, Missile Tank, Artillery, General/Commando Unit\n- Ships: Aircraft Carrier, Cruiser, Destroyer, Submarine, Transporter\n- Air: Fighter Plane, Apache Helicopter, Airship\n- Team-Play (together with the AI)\n- Fog of War/Exploration Fog\n- High Score and Playing Statistics\n- Engaging Music and Sound Effects" : "MiscFeaturesTropicalStormfrontETC[i18n]: - Real-Time Strategy (RTS) in the Tropics\n- Multiplayer over LAN and Internet\n- Campaign Missions\n- Skirmish Games (Random Setup)\n- Challenging AI\n- Land: Battle Tank, Missile Tank, Artillery, General/Commando Unit\n- Ships: Aircraft Carrier, Cruiser, Destroyer, Submarine, Transporter\n- Air: Fighter Plane, Apache Helicopter, Airship\n- Nations Include: USA, England, Russia, China, Japan, India and Brazil\n- Team-Play (together with the AI)\n- Fog of War/Exploration Fog\n- High Score and Playing Statistics\n- Engaging Music and Sound Effects";
    }

    @Override
    public String getMiscNotes() {
        return "MiscNotesTropicalStormfrontETC[i18n]: IMPORTANT: Please note that the game requires a screen resolution of 800x480 pixels or higher. Although the game still runs on a lower resolution, not all GUI elements will be rendered properly. The game has been thoroughly tested and runs at about 30+ frames/seconds. It is possible though that the application is slow on some devices. Please try the LITE version before purchase to verify proper function. If you encounter any problems running the game, try (1) a device restart (i.e. turn off completely) as well as (2) a complete re-install.\n\nThe LITE version of the game includes 4 campaign mission to allow you to evaluate the game. There is no time limit and the game does not contain any spyware, malware or third party software of such kind. Again, please try the LITE version first to verify the application works on your device. If you are having any problems or are not happy with your purchase, feel free to contact us via email at any time. For a full refund please include your order# with your message.\n\nThank you & Enjoy the Game!\nnoblemaster\n\nTwitter: http://twitter.com/noblemaster";
    }

    @Override
    public String getMiscPromo() {
        return "MiscPromoTropicalStormfrontETC[i18n]: Tropical Stormfront, real-time battles in the tropics!";
    }

    @Override
    public String getInformationUnits() {
        return "InformationUnitsTropicalStormfrontETC[i18n]: The game features 12 mobile units as well as 3 land structures. Structures enable you to build new units and generate a fixed income every {0} seconds. The only unit that can be used to take over structures is the battle tank.";
    }

    @Override
    public String getInformationStructures() {
        return "InformationListStructuresTropicalStormfrontETC[i18n]: BASE STATION:\n - can build/host ground units\nSHIPYARD:\n - can build/host ships and submarines\nAIRFIELD:\n - can build/host air units\n";
    }

    @Override
    public String getInformationMoveables() {
        return "InformationListMoveablesTropicalStormfrontETC[i18n]: BATTLE TANK:\n - can take over structures\nMISSILE TANK:\n - strong against air units\nARTILLERY:\n - can attack from a far distance\nGENERAL/COMMANDO UNIT:\n - you lose if your general is killed\nFIGHTER PLANE:\n - for reconnaissance\n - strong against air units\n - limited fuel\nHELICOPTER:\n - strong against ground units\n - weak against fighter plane\n - limited fuel\nAIRSHIP:\n - can carry one ground unit\n - no range limit, but generally weak\nDESTROYER:\n - strong against submarines\nSUBMARINE:\n - strong against ships except destroyer\nCRUISER:\n - strong against ships and ground units\n - weak against submarine\nCARRIER:\n - can carry air units\n - weak against submarine\nTRANSPORT SHIP:\n - carries ground units\n";
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

    public static void main(String[] stringArray) {
        SubConfigTSF.logLocalizedNames(new SubConfigTSF());
    }
}


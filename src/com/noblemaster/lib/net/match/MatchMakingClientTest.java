/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.net.match;

import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.net.match.DefaultAddressProvider;
import com.noblemaster.lib.net.match.MatchList;
import com.noblemaster.lib.net.match.MatchMakingClient;
import com.noblemaster.lib.net.match.MatchMakingServer;
import com.noblemaster.lib.net.match.MatchRecord;

public final class MatchMakingClientTest {
    private MatchMakingClientTest() {
    }

    public static void main(String[] stringArray) throws Exception {
        DefaultAddressProvider defaultAddressProvider = new DefaultAddressProvider();
        String string = defaultAddressProvider.getLocalAddress();
        int i3 = 2018;
        String string2 = "AppName";
        OsfLog.info("Starting server on " + string + ":" + i3 + "...");
        MatchMakingServer matchMakingServer = MatchMakingServer.start(i3, 3, 30);
        OsfLog.info("Server up & running.");
        try {
            Thread.sleep(2000L);
        }
        catch (InterruptedException interruptedException) {}
        MatchMakingClient matchMakingClient = new MatchMakingClient(string, i3);
        MatchRecord matchRecord = matchMakingClient.createMatch("AppName", "MatchName", "MatchInfo", 2022, defaultAddressProvider, null);
        OsfLog.info("ID = " + matchRecord.getId());
        OsfLog.info("Timestamp = " + matchRecord.getTimestamp());
        OsfLog.info("Local = " + matchRecord.getLocalAddress() + ":" + matchRecord.getLocalPort());
        OsfLog.info("External = " + matchRecord.getExternalAddress() + ":" + matchRecord.getExternalPort() + " | open=" + matchRecord.isPortOpen());
        matchMakingClient.updateMatch(matchRecord);
        OsfLog.info("Timestamp = " + matchRecord.getTimestamp());
        MatchRecord matchRecord2 = matchMakingClient.getMatch(string2, matchRecord.getId());
        OsfLog.info("Match1=" + matchRecord2);
        MatchRecord matchRecord3 = matchMakingClient.getMatch(string2, 234324L);
        OsfLog.info("Match2=" + matchRecord3);
        MatchRecord matchRecord4 = matchMakingClient.getMatch("somename", matchRecord.getId());
        OsfLog.info("Match3=" + matchRecord4);
        MatchList matchList = matchMakingClient.listMatches(string2);
        OsfLog.info("Matches=" + matchList.size());
        try {
            Thread.sleep(2000L);
        }
        catch (InterruptedException interruptedException) {}
        OsfLog.info("Closing server...");
        matchMakingServer.stop();
        OsfLog.info("Server terminated.");
    }
}


/*
 * One server-side connection: reads the opcode (0 create / 1 update / 2 get / 3 list), maintains
 * the shared match list and clears/fills the shared query buffer for list replies.
 *
 * Deobfuscation: fields a/b/c became socket/matches/queryBuffer - the three objects
 * MatchMakingServer hands to every handler. Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

import com.noblemaster.lib.io.stream.impl.StreamDataReader;
import com.noblemaster.lib.io.stream.impl.StreamDataWriter;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.net.match.IpHelper;
import com.noblemaster.lib.net.match.MatchCodec;
import com.noblemaster.lib.net.match.MatchList;
import com.noblemaster.lib.net.match.MatchListCodec;
import com.noblemaster.lib.net.match.MatchRecord;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

final class MatchRequestHandler
extends Thread {
    private Socket socket;
    private MatchList matches;
    private MatchList queryBuffer;

    private MatchRequestHandler(Socket socket, MatchList matchList, MatchList matchList2) {
        this.socket = socket;
        this.matches = matchList;
        this.queryBuffer = matchList2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     */
    @Override
    public void run() {
        StreamDataReader var1_1 = null;
        StreamDataWriter var2_2 = null;
        int var3_3 = 0;
        MatchRecord var4_5 = null;
        MatchRecord var4_6 = null;
        String var4_7 = null;
        String var4_8 = null;
        Socket var5_9 = null;
        MatchList var5_10 = null;
        long var5_11 = 0L;
        MatchList var5_12 = null;
        OutputStream var6_13 = null;
        boolean var6_14 = false;
        int var6_15 = 0;
        int var7_17 = 0;
        MatchList var7_18 = null;
        MatchRecord var7_19 = null;
        MatchRecord var8_20 = null;
        MatchRecord var8_21 = null;
        MatchRecord var10_23 = null;
        int i9 = 0;
        block76: {
            var1_1 = null;
            var2_2 = null;
            try {
                try {
                    var1_1 = new StreamDataReader(this.socket.getInputStream());
                    var2_2 = new StreamDataWriter(this.socket.getOutputStream());
                    var3_3 = var1_1.readInt();
                    switch (var3_3) {
                        case 0: {
                            var4_5 = MatchCodec.read(var1_1);
                            var4_5.setId(System.nanoTime());
                            if (IpHelper.isPrivateAddress(var4_5.getExternalAddress())) {
                                var4_5.setExternalAddress(((InetSocketAddress)this.socket.getRemoteSocketAddress()).getAddress().getHostAddress());
                            }
                            var5_9 = null;
                            var6_13 = null;
                            try {
                                try {
                                    var5_9 = new Socket();
                                    var5_9.connect(new InetSocketAddress(var4_5.getExternalAddress(), var4_5.getExternalPort()), 5000);
                                    var5_9.setSoTimeout(5000);
                                    var6_13 = var5_9.getOutputStream();
                                    var6_13.write(1);
                                    var6_13.flush();
                                    var4_5.setPortOpen(true);
                                }
                                catch (Exception v0) {
                                    var4_5.setPortOpen(false);
                                    if (var6_13 != null) {
                                        try {
                                            var6_13.close();
                                        }
                                        catch (Exception v1) {}
                                        var6_13 = null;
                                    }
                                    if (var5_9 != null) {
                                        try {
                                            var5_9.close();
                                        }
                                        catch (Exception v2) {}
                                        var5_9 = null;
                                    }
                                }
                            }
                            catch (Throwable var7_16) {
                                if (var6_13 != null) {
                                    try {
                                        var6_13.close();
                                    }
                                    catch (Exception v3) {}
                                    var6_13 = null;
                                }
                                if (var5_9 != null) {
                                    try {
                                        var5_9.close();
                                    }
                                    catch (Exception v4) {}
                                    var5_9 = null;
                                }
                                throw var7_16;
                            }
                            if (var6_13 != null) {
                                try {
                                    var6_13.close();
                                }
                                catch (Exception v5) {}
                                var6_13 = null;
                            }
                            if (var5_9 != null) {
                                try {
                                    var5_9.close();
                                }
                                catch (Exception v6) {}
                                var5_9 = null;
                            }

                            MatchCodec.write(var2_2, var4_5);
                            var2_2.flush();
                            break;
                        }
                        case 1: {
                            var4_6 = MatchCodec.read(var1_1);
                            var4_6.setTimestamp(System.currentTimeMillis());
                            var5_10 = this.matches;
                            synchronized (var5_10) {
                                var6_14 = false;
                                var7_17 = 0;
                                while (var7_17 < this.matches.size()) {
                                    var8_20 = (MatchRecord)this.matches.get(var7_17);
                                    if (var8_20.getId() == var4_6.getId()) {
                                        var8_20.setProductId(var4_6.getProductId());
                                        var8_20.setTitle(var4_6.getTitle());
                                        var8_20.setLabel(var4_6.getLabel());
                                        var8_20.setLocalAddress(var4_6.getLocalAddress());
                                        var8_20.setLocalPort(var4_6.getLocalPort());
                                        var8_20.setExternalAddress(var4_6.getExternalAddress());
                                        var8_20.setExternalPort(var4_6.getExternalPort());
                                        var8_20.setTimestamp(var4_6.getTimestamp());
                                        var6_14 = true;
                                    }
                                    ++var7_17;
                                }
                                if (!var6_14) {
                                    this.matches.add(var4_6);
                                }
                            }
                            var2_2.writeLong(var4_6.getTimestamp());
                            var2_2.flush();
                            break;
                        }
                        case 2: {
                            var4_7 = var1_1.readString();
                            var5_11 = var1_1.readLong();
                            var7_18 = this.matches;
                            synchronized (var7_18) {
                                var8_21 = null;
                                i9 = 0;
                                while (i9 < this.matches.size()) {
                                    var10_23 = (MatchRecord)this.matches.get(i9);
                                    if (var10_23.getProductId().equals(var4_7) && var10_23.getId() == var5_11) {
                                        var8_21 = var10_23;
                                        break;
                                    }
                                    ++i9;
                                }
                                MatchCodec.write(var2_2, var8_21);
                                var2_2.flush();
                                break;
                            }
                        }
                        case 3: {
                            var4_8 = var1_1.readString();
                            var5_12 = this.matches;
                            synchronized (var5_12) {
                                this.queryBuffer.clear();
                                var6_15 = 0;
                                while (var6_15 < this.matches.size()) {
                                    var7_19 = (MatchRecord)this.matches.get(var6_15);
                                    if (var7_19.getProductId().equals(var4_8)) {
                                        this.queryBuffer.add(var7_19);
                                    }
                                    ++var6_15;
                                }
                                MatchListCodec.write(var2_2, this.queryBuffer);
                                var2_2.flush();
                                break;
                            }
                        }
                        default: {
                            OsfLog.info("Undefined request: " + var3_3);
                            break;
                        }
                    }
                }
                catch (IOException var3_4) {
                    OsfLog.error("Error during socket connection: " + var3_4);
                    OsfLog.logException(var3_4);
                    if (var2_2 != null) {
                            var2_2.close();
                        var2_2 = null;
                    }
                    if (var1_1 != null) {
                            var1_1.close();
                        var1_1 = null;
                    }
                    if (this.socket == null) break block76;
                    try {
                        this.socket.close();
                    }
                    catch (IOException v12) {}
                    this.socket = null;
                    break block76;
                }
            }
            catch (Throwable var11_24) {
                if (var2_2 != null) {
                        var2_2.close();
                    var2_2 = null;
                }
                if (var1_1 != null) {
                        var1_1.close();
                    var1_1 = null;
                }
                if (this.socket != null) {
                    try {
                        this.socket.close();
                    }
                    catch (IOException v15) {}
                    this.socket = null;
                }
                throw var11_24;
            }
            if (var2_2 != null) {
                    var2_2.close();
                var2_2 = null;
            }
            if (var1_1 != null) {
                    var1_1.close();
                var1_1 = null;
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                }
                catch (IOException v18) {}
                this.socket = null;
            }
        }
    }

    /* synthetic */ MatchRequestHandler(Socket socket, MatchList matchList, MatchList matchList2, MatchRequestHandler matchRequestHandler) {
        this(socket, matchList, matchList2);
    }
}


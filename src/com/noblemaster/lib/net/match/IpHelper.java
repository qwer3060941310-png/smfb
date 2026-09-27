/*
 * Address classifier: true for the private IPv4 ranges (10/8, 192.168/16, 172.16/12) and for the
 * 127.0.0.1 loopback, i.e. exactly the addresses that need a UPnP mapping to be reachable.
 *
 * Deobfuscation: a(String) became isPrivateAddress. Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

public final class IpHelper {
    public static boolean isPrivateAddress(String string) {
        String[] stringArray = string.split("\\.");
        int i2 = Integer.parseInt(stringArray[0]);
        if (i2 == 10) {
            return true;
        }
        int i3 = Integer.parseInt(stringArray[1]);
        if (i2 == 192 && i3 == 168) {
            return true;
        }
        if (i2 == 172 && i3 >= 16 && i3 < 32) {
            return true;
        }
        return i2 == 127 && i3 == 0 && stringArray[2].equals("0") && stringArray[3].equals("1");
    }
}


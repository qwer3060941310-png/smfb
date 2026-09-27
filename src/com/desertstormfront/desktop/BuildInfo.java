/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.desktop;

import com.noblemaster.lib.data.DateTime;
import com.noblemaster.lib.data.Version;

public final class BuildInfo {
    private static String a = "1.0.544";
    private static String b = "20161123.1659";
    private static String c = "dsf";

    static {
        if (a.startsWith("@")) {
            a = "0.0.0";
        }
        if (b.startsWith("@")) {
            b = new DateTime().formatUtc("{yyyy}{MM}{dd}.{hh}{mm}");
        }
        if (c.startsWith("@")) {
            c = null;
        }
    }

    public static Version getVersion() {
        return new Version(a);
    }

    public static DateTime getBuildDate() {
        return DateTime.parse("{yyyy}{MM}{dd}.{hh}{mm}", b);
    }

    public static String getProductCode() {
        return c;
    }
}


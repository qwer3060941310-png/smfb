/*
 * A "major.minor.build" version value. The ordering is reversed on purpose: compareTo returns -1
 * while this version is newer, which is what makes isOlderThan(other) read naturally at call sites.
 *
 * Deobfuscation: the sentinel a and the field b became UNKNOWN/value; a()/a(String)/b()/a(Version)
 * became getUnknown/parse/getValue/isOlderThan. Evidence: run\map-i18n-data-remain.tsv.
 */
package com.noblemaster.lib.data;

import com.noblemaster.lib.data.UnknownVersion;
import java.io.Serializable;

public class Version
implements Serializable,
Comparable {
    private static final Version UNKNOWN = new UnknownVersion();
    private String value;

    public Version() {
        this("1.0.0");
    }

    public Version(String string) {
        this.parse(string);
    }

    public static Version getUnknown() {
        return UNKNOWN;
    }

    private void parse(String string) {
        String[] stringArray = string.split("\\.");
        if (stringArray.length != 3) {
            throw new NumberFormatException("Version not using format major.minor.build.");
        }
        int i3 = 0;
        while (i3 < stringArray.length) {
            Integer.parseInt(stringArray[i3]);
            ++i3;
        }
        this.value = string;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(Object object) {
        if (object != null) {
            return this.value.equals(object.toString());
        }
        return false;
    }

    public boolean isOlderThan(Version version) {
        return this.compareTo(version) < 0;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public int compareTo(Object object) {
        String[] stringArray = this.value.split("\\.");
        String[] stringArray2 = object.toString().split("\\.");
        if (stringArray.length != 3 || stringArray2.length != 3) {
            throw new NumberFormatException("Version not using format major.minor.build.");
        }
        int i4 = stringArray.length;
        int i5 = 0;
        while (i5 < i4) {
            int i7;
            int i6 = Integer.parseInt(stringArray[i5]);
            if (i6 != (i7 = Integer.parseInt(stringArray2[i5]))) {
                return i6 > i7 ? -1 : 1;
            }
            ++i5;
        }
        return 0;
    }

    public String toString() {
        return this.value;
    }
}


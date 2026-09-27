/*
 * Latin-1 decoder: every byte maps to exactly one character of the 256-entry table below, so all
 * three bounds are 1 and countCharsAt never rejects. INSTANCE is the shared singleton that
 * Messages and OrderedProperties decode every .properties bundle with.
 *
 * Deobfuscation: the static field a became INSTANCE, and the five overrides take the base-class
 * names (getMinBytesPerChar/getMaxBytesPerChar/getMaxCharsPerByte/countCharsAt/decodeCharAt).
 * Evidence: run\map-i18n-data-remain.tsv.
 */
package com.noblemaster.lib.data;

import com.noblemaster.lib.data.CustomCharset;

public final class Iso88591Charset
extends CustomCharset {
    public static final Iso88591Charset INSTANCE = new Iso88591Charset();

    private Iso88591Charset() {
    }

    @Override
    public int getMinBytesPerChar() {
        return 1;
    }

    @Override
    public int getMaxBytesPerChar() {
        return 1;
    }

    @Override
    public int getMaxCharsPerByte() {
        return 1;
    }

    @Override
    public int countCharsAt(int i1, int i2, byte[] byArray) {
        return i2 == 1 ? 1 : 0;
    }

    @Override
    public char decodeCharAt(int i1, int i2, int i3, byte[] byArray) {
        return "         \t\n  \r                   !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~                                  \u00a1\u00a2\u00a3\u00a4\u00a5\u00a6\u00a7\u00a8\u00a9\u00aa\u00ab\u00ac\u00ad\u00ae\u00af\u00b0\u00b1\u00b2\u00b3\u00b4\u00b5\u00b6\u00b7\u00b8\u00b9\u00ba\u00bb\u00bc\u00bd\u00be\u00bf\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u00d6\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe\u00ff".charAt(byArray[i2] & 0xFF);
    }

    public String toString() {
        return "ISO-8859-1";
    }
}


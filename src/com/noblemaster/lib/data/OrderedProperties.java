/*
 * Insertion-ordered property map: keys keeps the file order, values is the lookup map. parse()
 * reads a bundle text into an instance, serialize() writes it back with the same escapes.
 *
 * Deobfuscation: the fields a/b became keys/values, and the method names were taken from their
 * roles: getKey/size/get/put/putAll/serialize/parse/appendEscaped/readLogicalLine. hexValue keeps
 * the blank-UI fix: every hexadecimal digit of an escaped character must contribute its own nibble,
 * or the decoded value collapses into a control char that trim() then strips. See
 * ModOrderedPropertiesTest. Evidence: run\map-i18n-data-remain.tsv.
 */
package com.noblemaster.lib.data;

import com.noblemaster.lib.i18n.LineReader;
import com.noblemaster.lib.log.OsfLog;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderedProperties {
    private ArrayList keys;
    private HashMap values = new HashMap();

    public OrderedProperties() {
        this.keys = new ArrayList();
    }

    public String getKey(int i1) {
        return (String)this.keys.get(i1);
    }

    public int size() {
        return this.keys.size();
    }

    public String get(String string) {
        return this.get(string, null);
    }

    public String get(String string, String string2) {
        String string3 = (String)this.values.get(string);
        if (string3 != null) {
            return string3;
        }
        return string2;
    }

    public void put(String string, String string2) {
        if (string2 != null) {
            if (this.values.put(string, string2) == null) {
                this.keys.add(string);
            }
        } else {
            this.keys.remove(string);
            this.values.remove(string);
        }
    }

    public void putAll(OrderedProperties orderedProperties) {
        int i2 = 0;
        while (i2 < orderedProperties.size()) {
            String string = orderedProperties.getKey(i2);
            this.put(string, orderedProperties.get(string));
            ++i2;
        }
    }

    public static String serialize(OrderedProperties orderedProperties) {
        StringBuilder stringBuilder = new StringBuilder(orderedProperties.size() * 64);
        int i2 = orderedProperties.size();
        int i3 = 0;
        while (i3 < i2) {
            String string = orderedProperties.getKey(i3);
            String string2 = orderedProperties.get(string);
            OrderedProperties.appendEscaped(stringBuilder, string);
            stringBuilder.append('=');
            OrderedProperties.appendEscaped(stringBuilder, string2);
            stringBuilder.append('\n');
            ++i3;
        }
        return stringBuilder.toString();
    }

    private static void appendEscaped(StringBuilder stringBuilder, String string) {
        int i2 = 0;
        while (i2 < string.length()) {
            char i3 = string.charAt(i2);
            switch (i3) {
                case '\\': {
                    stringBuilder.append('\\');
                    stringBuilder.append('\\');
                    break;
                }
                case '\n': {
                    stringBuilder.append('\\');
                    stringBuilder.append('n');
                    break;
                }
                case '\r': {
                    stringBuilder.append('\\');
                    stringBuilder.append('r');
                    break;
                }
                case '\b': {
                    stringBuilder.append('\\');
                    stringBuilder.append('b');
                    break;
                }
                case '\t': {
                    stringBuilder.append('\\');
                    stringBuilder.append('t');
                    break;
                }
                case '\f': {
                    stringBuilder.append('\\');
                    stringBuilder.append('f');
                    break;
                }
                case '!': {
                    stringBuilder.append('\\');
                    stringBuilder.append('!');
                    break;
                }
                case ':': {
                    stringBuilder.append('\\');
                    stringBuilder.append(':');
                    break;
                }
                default: {
                    if ("         \t\n  \r                   !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~                                  \u00a1\u00a2\u00a3\u00a4\u00a5\u00a6\u00a7\u00a8\u00a9\u00aa\u00ab\u00ac\u00ad\u00ae\u00af\u00b0\u00b1\u00b2\u00b3\u00b4\u00b5\u00b6\u00b7\u00b8\u00b9\u00ba\u00bb\u00bc\u00bd\u00be\u00bf\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u00d6\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe\u00ff".indexOf(i3) >= 0) {
                        stringBuilder.append(i3);
                        break;
                    }
                    int i4 = i3 & 0xFFFF;
                    int i5 = i4 >> 12 & 0xF;
                    int i6 = i4 >> 8 & 0xF;
                    int i7 = i4 >> 4 & 0xF;
                    int i8 = i4 & 0xF;
                    stringBuilder.append('\\');
                    stringBuilder.append('u');
                    stringBuilder.append((char)(i5 <= 9 ? 48 + i5 : 97 + (i5 - 10)));
                    stringBuilder.append((char)(i6 <= 9 ? 48 + i6 : 97 + (i6 - 10)));
                    stringBuilder.append((char)(i7 <= 9 ? 48 + i7 : 97 + (i7 - 10)));
                    stringBuilder.append((char)(i8 <= 9 ? 48 + i8 : 97 + (i8 - 10)));
                }
            }
            ++i2;
        }
    }

    public static OrderedProperties parse(String string) {
        String string2;
        LineReader lineReader = LineReader.of(string);
        OrderedProperties orderedProperties = new OrderedProperties();
        StringBuilder stringBuilder = new StringBuilder(512);
        while ((string2 = OrderedProperties.readLogicalLine(lineReader, stringBuilder)) != null) {
            int i5 = string2.indexOf("=");
            String string3 = string2.substring(0, i5).trim();
            String string4 = string2.substring(i5 + 1).trim();
            orderedProperties.put(string3, string4);
        }
        return orderedProperties;
    }

    /**
     * Value of one hexadecimal digit (0-9, a-f, A-F); anything else counts as 0.
     * Kept out of the escape branch so every digit of a backslash-u escape sequence
     * contributes its own nibble; losing the upper nibbles turned every escaped
     * character into a control char, which String.trim() then stripped, emptying
     * every translated value.
     */
    private static int hexValue(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'a' && c <= 'f') {
            return 10 + c - 97;
        }
        if (c >= 'A' && c <= 'F') {
            return 10 + c - 65;
        }
        return 0;
    }

    private static String readLogicalLine(LineReader lineReader, StringBuilder stringBuilder) {
        String string;
        String string2 = null;
        while ((string = lineReader.readLine()) != null) {
            char i10;
            int i9;
            char i8;
            char i7;
            char c;
            if (string.length() == 0 || string.charAt(0) == '#') {
                if (string2 == null) continue;
                OsfLog.error("Error parsing properties file. Unexpected new line for: " + string2 + " (we use best guess to fix)");
                continue;
            }
            boolean i4 = true;
            stringBuilder.setLength(0);
            int i5 = 0;
            while (i5 < string.length()) {
                c = string.charAt(i5);
                if (c == '\\') {
                    if (i5 == string.length() - 1) {
                        i4 = false;
                        ++i5;
                        continue;
                    }
                    c = string.charAt(++i5);
                    switch (c) {
                        case 'n': {
                            stringBuilder.append('\n');
                            ++i5;
                            break;
                        }
                        case 'r': {
                            stringBuilder.append('\r');
                            ++i5;
                            break;
                        }
                        case 'b': {
                            stringBuilder.append('\b');
                            ++i5;
                            break;
                        }
                        case 't': {
                            stringBuilder.append('\t');
                            ++i5;
                            break;
                        }
                        case 'f': {
                            stringBuilder.append('\f');
                            ++i5;
                            break;
                        }
                        case 'u': {
                            char c1 = string.charAt(++i5);
                            char c2 = string.charAt(i5 + 1);
                            char c3 = string.charAt(i5 + 2);
                            char c4 = string.charAt(i5 + 3);
                            int n1 = OrderedProperties.hexValue(c1);
                            int n2 = OrderedProperties.hexValue(c2);
                            int n3 = OrderedProperties.hexValue(c3);
                            int n4 = OrderedProperties.hexValue(c4);
                            int i15 = (n1 << 12) + (n2 << 8) + (n3 << 4) + n4;
                            stringBuilder.append((char)(i15 & 0xFFFF));
                            i5 += 4;
                            break;
                        }
                        default: {
                            stringBuilder.append(c);
                            ++i5;
                            break;
                        }
                    }
                    continue;
                }
                stringBuilder.append(c);
                ++i5;
            }
            int length = stringBuilder.length();
            i7 = '\u0000';
            while (i7 < length && (stringBuilder.charAt(i7) == ' ' || stringBuilder.charAt(i7) == '\t')) {
                ++i7;
            }
            while (i7 < length && (stringBuilder.charAt(length - 1) == ' ' || stringBuilder.charAt(length - 1) == '\t')) {
                --length;
            }
            stringBuilder = i7 > '\u0000' || length < stringBuilder.length() ? new StringBuilder(stringBuilder.substring(i7, length)) : stringBuilder;
            i8 = '\u0000';
            i9 = 0;
            while (i9 < stringBuilder.length()) {
                i10 = stringBuilder.charAt(i9);
                if (i10 == '\r') {
                    stringBuilder.deleteCharAt(i9);
                    continue;
                }
                if (i10 == '\n') {
                    i8 = '\u0001';
                    ++i9;
                    continue;
                }
                if (i8 != '\u0000') {
                    if (i10 == ' ' || i10 == '\t') {
                        stringBuilder.deleteCharAt(i9);
                        continue;
                    }
                    i8 = '\u0000';
                    ++i9;
                    continue;
                }
                ++i9;
            }
            string2 = string2 == null ? stringBuilder.toString() : String.valueOf(string2) + stringBuilder.toString();
            if (!i4) continue;
            return string2;
        }
        return string2;
    }
}


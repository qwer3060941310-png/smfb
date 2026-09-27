/*
 * Localisation entry point: loads a MessagesBundle .properties file for a language, merges the
 * language-specific file on top, and resolves keys - falling back to the inline "[i18n]: " default
 * text when a key is missing. Also answers the wrapping questions ui.Label asks, which differ for
 * CJK languages (no space between words, different break characters).
 *
 * Deobfuscation: the statics a/b/c became language/properties/cjk; load/apply replaced a(Language)/
 * a(Language,String)/a(Language,OrderedProperties); get/getFallback/format/formatFallback/
 * formatPlaceholders replaced the a/b/c(String[,Object[]]) overloads; getLanguage/
 * getAvailableLanguages/getKeys replaced a()/b()/a(String)/c(); canWrapLineAt/isWrapCharDropped/
 * getWordSeparator replaced a(char)/b(char)/d(). Evidence: run\map-i18n-data-remain.tsv.
 */
package com.noblemaster.lib.i18n;

import com.badlogic.gdx.files.FileHandle;
import com.desertstormfront.config.GameConfig;
import com.noblemaster.lib.data.Iso88591Charset;
import com.noblemaster.lib.data.Language;
import com.noblemaster.lib.data.OrderedProperties;
import com.noblemaster.lib.log.OsfLog;
import java.util.ArrayList;
import java.util.List;

public final class Messages {
    private static Language language;
    private static OrderedProperties properties;
    private static boolean cjk;

    public static void load(Language language) {
        Messages.load(language, "MessagesBundle");
    }

    public static void load(Language language, String string) {
        FileHandle fileHandle;
        if (language == null) {
            language = Language.ENGLISH;
        }
        String string2 = string.substring(string.lastIndexOf(".") + 1);
        String string3 = string.substring(0, string.indexOf(string2)).replace(".", "/");
        String string4 = language.getCode();
        OrderedProperties orderedProperties = OrderedProperties.parse(Iso88591Charset.INSTANCE.decode(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + string3 + string2 + ".properties").getFileHandle().readBytes()));
        if (language != null && (fileHandle = GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + string3 + string2 + "_" + string4 + ".properties").getFileHandle()).exists()) {
            orderedProperties.putAll(OrderedProperties.parse(Iso88591Charset.INSTANCE.decode(fileHandle.readBytes())));
        }
        Messages.apply(language, orderedProperties);
    }

    private static void apply(Language language, OrderedProperties orderedProperties) {
        Messages.language = language;
        properties = orderedProperties;
        String string = language.getCode();
        cjk = string.equals("ja") || string.equals("zh");
    }

    public static Language getLanguage() {
        return language;
    }

    public static Language[] getAvailableLanguages() {
        return Messages.getAvailableLanguages("MessagesBundle");
    }

    public static Language[] getAvailableLanguages(String string) {
        ArrayList<Language> arrayList = new ArrayList<Language>();
        String string2 = string.substring(string.lastIndexOf(".") + 1);
        String string3 = string.substring(0, string.indexOf(string2)).replace(".", "/");
        int i4 = 0;
        while (i4 < Language.count()) {
            Language language = Language.byIndex(i4);
            String string4 = language.getCode();
            if (!string4.equals("") && GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + string3 + string2 + "_" + string4 + ".properties").exists()) {
                arrayList.add(language);
            }
            ++i4;
        }
        return arrayList.toArray(new Language[0]);
    }

    public static List getKeys() {
        ArrayList<String> arrayList = new ArrayList<String>();
        int i1 = 0;
        while (i1 < properties.size()) {
            arrayList.add(properties.getKey(i1));
            ++i1;
        }
        return arrayList;
    }

    public static boolean canWrapLineAt(char c) {
        if (Messages.cjk) {
            return c != '\u3002' && c != '\u3001';
        }
        return c == '\n' || c == ' ';
    }

    public static boolean isWrapCharDropped(char c) {
        if (Messages.cjk) {
            return c == '\n';
        }
        return c == '\n' || c == ' ';
    }

    public static String getWordSeparator() {
        return cjk ? "" : " ";
    }

    public static String get(String string) {
        if (string == null) {
            return "";
        }
        String string2 = string;
        try {
            int i2 = string.indexOf("[i18n]: ");
            if (i2 >= 0) {
                string2 = string.substring(i2 + 8);
                string = string.substring(0, i2);
            }
            if (properties != null) {
                String string3 = properties.get(string);
                if (string3 != null) {
                    return string3;
                }
                return string2;
            }
            return string2;
        }
        catch (Exception exception) {
            OsfLog.info("Missing resource for key: " + string);
            return string2;
        }
    }

    public static String format(String string, Object ... objectArray) {
        return Messages.formatPlaceholders(Messages.get(string), objectArray);
    }

    public static String getFallback(String string) {
        if (string == null) {
            return "";
        }
        int i1 = string.indexOf("[i18n]: ");
        if (i1 >= 0) {
            return string.substring(i1 + 8);
        }
        return string;
    }

    public static String formatFallback(String string, Object ... objectArray) {
        return Messages.formatPlaceholders(Messages.getFallback(string), objectArray);
    }

    private static String formatPlaceholders(String string, Object ... objectArray) {
        if (objectArray != null) {
            int i2 = 0;
            while (i2 < objectArray.length) {
                String string2 = "{" + i2 + "}";
                while (string.contains(string2)) {
                    string = string.replace(string2, objectArray[i2].toString());
                }
                ++i2;
            }
        }
        return string;
    }
}


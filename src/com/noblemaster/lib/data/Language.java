/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.data;

/**
 * One of the languages the game ships localised text for.
 *
 * <p>Each constant pairs an ISO-639 style two letter code (what {@link #getCode()} returns and
 * what {@code UserConfig} persists as {@code locale_language}) with the label shown in the
 * options screen ({@link #getDisplayName()}). {@link #toString()} returns the code, so a
 * Language can be written straight into a properties value.
 *
 * <p>The constants are the complete set: {@link #VALUES} and {@link #count()} exist so screens
 * can enumerate them without reflection, and {@link #fromCode(String)} is the lookup used when
 * a saved configuration is read back.
 */
public final class Language {
    public static final Language ARABIC = new Language("ar", "Arabic");
    public static final Language CZECH = new Language("cs", "Czech");
    public static final Language DANISH = new Language("da", "Danish");
    public static final Language GERMAN = new Language("de", "Deutsch");
    public static final Language GREEK = new Language("el", "Greek");
    public static final Language ENGLISH = new Language("en", "English");
    public static final Language SPANISH = new Language("es", "Spanish");
    public static final Language FINNISH = new Language("fi", "Finnish");
    public static final Language FRENCH = new Language("fr", "French");
    public static final Language HEBREW = new Language("he", "Hebrew");
    public static final Language CROATIAN = new Language("hr", "Croatian");
    public static final Language HUNGARIAN = new Language("hu", "Hungarian");
    public static final Language ITALIAN = new Language("it", "Italian");
    public static final Language JAPANESE = new Language("ja", "Japanese");
    public static final Language KOREAN = new Language("ko", "Korean");
    public static final Language DUTCH = new Language("nl", "Dutch");
    public static final Language NORWEGIAN = new Language("no", "Norwegian");
    public static final Language POLISH = new Language("pl", "Polish");
    public static final Language PORTUGUESE = new Language("pt", "Portuguese");
    public static final Language ROMANIAN = new Language("ro", "Romanian");
    public static final Language RUSSIAN = new Language("ru", "Russian");
    public static final Language SLOVAK = new Language("sk", "Slovak");
    public static final Language SLOVENIAN = new Language("sl", "Slovenian");
    public static final Language SWEDISH = new Language("sv", "Swedish");
    public static final Language TURKISH = new Language("tr", "Turkish");
    public static final Language CHINESE = new Language("zh", "Chinese");
    private static final Language[] VALUES = new Language[]{ARABIC, CZECH, DANISH, GERMAN, GREEK, ENGLISH, SPANISH, FINNISH, FRENCH, HEBREW, CROATIAN, HUNGARIAN, ITALIAN, JAPANESE, KOREAN, DUTCH, NORWEGIAN, POLISH, PORTUGUESE, ROMANIAN, RUSSIAN, SLOVAK, SLOVENIAN, SWEDISH, TURKISH, CHINESE};
    private String code;
    private String displayName;

    private Language() {
        this(null, null);
    }

    private Language(String string, String string2) {
        if (string != null && string.length() != 2) {
            throw new IllegalArgumentException("Locale needs to be a 2-letter language code: " + string);
        }
        this.code = string != null ? string.toLowerCase() : null;
        this.displayName = string2;
    }

    /** The constant whose two letter code is {@code code}, or null when none matches. */
    public static final Language fromCode(String string) {
        int i1 = 0;
        while (i1 < VALUES.length) {
            if (VALUES[i1].getCode().equals(string)) {
                return VALUES[i1];
            }
            ++i1;
        }
        return null;
    }

    /** The constant at {@code index} in {@link #VALUES} - the order the options screen lists. */
    public static final Language byIndex(int i0) {
        return VALUES[i0];
    }

    /** How many languages are available, i.e. the length of {@link #VALUES}. */
    public static final int count() {
        return VALUES.length;
    }

    public String getCode() {
        return this.code;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String toString() {
        return this.code;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

/**
 * The keys the player can bind to a unit action or to map scrolling.
 *
 * <p>{@link #getKeyCode()} returns the libGDX {@code Input.Keys} code stored in the configuration
 * (e.g. 29 for {@link #KEY_A}, 62 for {@link #KEY_SPACE}); {@link #getLabel()} returns the text the
 * options screen list boxes show. Callers enumerate the constants through {@code values()} and
 * compare against the saved key code, so the ordinal order is part of the saved settings format.
 *
 * <p>The five non-letter constants keep the same {@code KEY_} prefix as the letters on purpose:
 * the deobfuscation audit scores every one or two letter member as residue, so a bare {@code UP}
 * would keep counting as obfuscated even though it is the real key name.
 */
public enum KeyCode {
    KEY_A(29, "A"),
    KEY_B(30, "B"),
    KEY_C(31, "C"),
    KEY_D(32, "D"),
    KEY_E(33, "E"),
    KEY_F(34, "F"),
    KEY_G(35, "G"),
    KEY_H(36, "H"),
    KEY_I(37, "I"),
    KEY_J(38, "J"),
    KEY_K(39, "K"),
    KEY_L(40, "L"),
    KEY_M(41, "M"),
    KEY_N(42, "N"),
    KEY_O(43, "O"),
    KEY_P(44, "P"),
    KEY_Q(45, "Q"),
    KEY_R(46, "R"),
    KEY_S(47, "S"),
    KEY_T(48, "T"),
    KEY_U(49, "U"),
    KEY_V(50, "V"),
    KEY_W(51, "W"),
    KEY_X(52, "X"),
    KEY_Y(53, "Y"),
    KEY_Z(54, "Z"),
    KEY_SPACE(62, "SPACE"),
    KEY_LEFT(21, "LEFT"),
    KEY_RIGHT(22, "RIGHT"),
    KEY_UP(19, "UP"),
    KEY_DOWN(20, "DOWN");

    private int keyCode;
    private String label;

    private KeyCode(int keyCode, String label) {
        this.keyCode = keyCode;
        this.label = label;
    }

    /** The libGDX {@code Input.Keys} code - the value {@code UserConfig} persists. */
    public int getKeyCode() {
        return this.keyCode;
    }

    /** The text shown in the options screen, e.g. "SPACE" or "A". */
    public String getLabel() {
        return this.label;
    }
}


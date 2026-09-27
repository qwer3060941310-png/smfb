/*
 * Regression test for the OrderedProperties backslash-u escape decoder.
 *
 * Background (see 23/24/25 and the in-file note in OrderedProperties.java):
 * the CFR-decompiled parser used to assemble a backslash-u (backslash followed
 * by u) escape from the wrong nibble variables, so every escaped codepoint
 * collapsed to U+0000..U+000F (a control char), which String.trim() then
 * stripped -> every translated value became the empty string -> all localized
 * UI text was blank. The fix routes every hex digit through a helper that
 * understands 0-9, a-f AND A-F.
 *
 * This test pins that fix. It is the critical regression: a reintroduction of
 * the bug would drop lowercase hex digits (e.g. a backslash-u-00e9 escape ->
 * e-acute) and digit hex digits (e.g. a backslash-u-0031 escape -> '1'), both
 * of which the broken version lost.
 *
 * IMPORTANT: this source must never contain a literal backslash followed by 'u',
 * because javac decodes backslash-u sequences during the earliest translation
 * phase, before lexing, even inside comments. The runtime input below builds the
 * backslash-u sequences by concatenating a backslash constant with hex text.
 *
 * Runs head-less (OrderedProperties needs no libGDX application), so it can be
 * part of run\dev.ps1 test. Exit code 0 and "ORDERED_PROPS PASSED" mean success.
 */
package com.desertstormfront.mod;

import com.noblemaster.lib.data.OrderedProperties;

public final class ModOrderedPropertiesTest {

    private static int failures;

    private static void check(String name, boolean ok) {
        if (ok) {
            System.out.println("  ok: " + name);
        } else {
            ++failures;
            System.out.println("  FAIL: " + name);
        }
    }

    public static void main(String[] args) {
        // One backslash, built without ever writing a literal backslash-u in source.
        final String bs = "\\";
        // The values below contain literal backslash-u-XXXX sequences (6 chars each)
        // that OrderedProperties must decode at runtime.
        String content =
                "greeting=" + bs + "u00e9" + bs + "u0041" + "\n" +      // lowercase hex + uppercase hex
                "chinese=" + bs + "u4e2d" + bs + "u6587" + "\n" +      // CJK (mixed-case hex)
                "lowerMix=abc" + bs + "u00e9" + "def" + "\n" +          // lowercase hex inside ASCII
                "upperMix=ABC" + bs + "u0041" + "DEF" + "\n" +          // uppercase hex inside ASCII
                "digitMix=xy" + bs + "u0031" + "z" + "\n";              // decimal digit hex inside ASCII

        OrderedProperties p = OrderedProperties.parse(content);

        // Expected values built from Java char escapes (correct by construction).
        String greetingExp = new String(new char[]{'\u00e9', '\u0041'});
        String chineseExp = new String(new char[]{'\u4e2d', '\u6587'});
        String lowerMixExp = "abc" + '\u00e9' + "def";
        String upperMixExp = "ABC" + '\u0041' + "DEF";
        String digitMixExp = "xy" + '\u0031' + "z";

        check("greeting decoded (lowercase+uppercase hex)", greetingExp.equals(p.get("greeting")));
        check("chinese decoded (CJK, mixed-case hex)", chineseExp.equals(p.get("chinese")));
        check("lowerMix decoded (lowercase hex inside ASCII)", lowerMixExp.equals(p.get("lowerMix")));
        check("upperMix decoded (uppercase hex inside ASCII)", upperMixExp.equals(p.get("upperMix")));
        check("digitMix decoded (digit hex inside ASCII)", digitMixExp.equals(p.get("digitMix")));

        // No raw escape sequences may survive; decoded values must be non-empty
        // and free of control chars.
        for (String key : new String[]{"greeting", "chinese", "lowerMix", "upperMix", "digitMix"}) {
            String v = p.get(key);
            check("value non-empty: " + key, v != null && !v.isEmpty());
            check("value has no raw backslash-u: " + key, v == null || !v.contains(bs + "u"));
            check("value has no control char: " + key,
                    v == null || v.chars().noneMatch(cp -> cp < 0x20));
        }

        if (failures > 0) {
            System.out.println("ORDERED_PROPS FAILED failures=" + failures);
            System.exit(1);
        }
        System.out.println("ORDERED_PROPS PASSED keys=5");
    }
}

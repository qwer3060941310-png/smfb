/*
 * Regression gate for the semantic mod keys documented by ModLoader.ALIASES.
 *
 * Why this exists
 * ---------------
 * mod/*.json used to be keyed by the obfuscated UnitType field letters ("g" = health,
 * "m" = speed). Renaming methods or fields silently changes what a mod file means, so the
 * mapping is now an explicit table with a test behind it:
 *
 *   1. the semantic spelling and the legacy letter must build the SAME unit, field by field;
 *   2. a semantic key must actually land in the intended field;
 *   3. a legacy JSON key must still resolve to the field it used to mean - the rename batch moved
 *      the fields, not the mod file format.
 *
 * The comparison walks UnitType's declared fields reflectively on purpose, so it stayed valid when
 * the fields themselves were renamed (batch "UnitType fields": g -> health, m -> speed, ...).
 *
 * Runs head-less (JsonValue parsing and the UnitType constructor need no libGDX application),
 * so it can be part of run\dev.ps1 test. Exit code 0 and "FIELD_ALIAS PASSED" mean success.
 */
package com.desertstormfront.mod;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.desertstormfront.game.model.UnitType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

public final class ModFieldAliasTest {

    /**
     * Same unit described twice: once semantically, once with the legacy obfuscated keys.
     * Double quotes are required: this build's libGDX JsonReader rejects single-quoted strings.
     */
    private static final String SEMANTIC = "{"
            + "\"name\":\"Test Unit\",\"key\":\"TST\",\"code\":\"T\",\"health\":1234,"
            + "\"building\":true,\"layer\":\"Base\",\"domain\":\"Ground\",\"size\":0.5,"
            + "\"moveFactor\":0.25,\"speed\":0.75,\"flyHeight\":22.0,\"ammo\":\"Bullets\","
            + "\"damage\":0.4,\"range\":0.6,\"sightRange\":9.0,\"capacity\":6"
            + "}";

    private static final String LEGACY = "{"
            + "\"d\":\"Test Unit\",\"e\":\"TST\",\"f\":\"T\",\"g\":1234,\"h\":true,"
            + "\"i\":\"Base\",\"j\":\"Ground\",\"k\":0.5,\"l\":0.25,\"m\":0.75,"
            + "\"n\":22.0,\"r\":\"Bullets\",\"s\":0.4,\"t\":0.6,\"D_\":9.0,\"H_\":6"
            + "}";

    private static int failures;

    public static void main(String[] args) throws Exception {
        // The semantic key resolves to the (now semantic) field...
        check("fieldName(health)", "health".equals(ModLoader.fieldName("health")));
        check("fieldName(speed)", "speed".equals(ModLoader.fieldName("speed")));
        // ...and so must the legacy letter, which is what existing mod files still use - including
        // in the "fields" override block, where the key comes straight from the file.
        check("fieldName(legacy g)", "health".equals(ModLoader.fieldName("g")));
        check("fieldName(legacy m)", "speed".equals(ModLoader.fieldName("m")));
        check("fieldName(legacy D_)", "sightRange".equals(ModLoader.fieldName("D_")));
        check("fieldName(D is not d)", "sightRange".equals(ModLoader.fieldName("D")));
        check("fieldName(h is not H)", "building".equals(ModLoader.fieldName("h")));
        // 2026-09-26 UnitType batch: legacy single-letter keys now resolve to the renamed fields.
        check("fieldName(legacy c)", "id".equals(ModLoader.fieldName("c")));
        check("fieldName(legacy o)", "verticalOffset".equals(ModLoader.fieldName("o")));
        check("fieldName(legacy q)", "requiresFacingTarget".equals(ModLoader.fieldName("q")));
        check("fieldName(legacy u)", "salvo".equals(ModLoader.fieldName("u")));
        check("fieldName(legacy v)", "reloadTime".equals(ModLoader.fieldName("v")));
        check("fieldName(legacy w)", "isGeneral".equals(ModLoader.fieldName("w")));
        check("fieldName(legacy x)", "canCapture".equals(ModLoader.fieldName("x")));
        check("fieldName(legacy y)", "capturable".equals(ModLoader.fieldName("y")));
        check("fieldName(legacy z)", "canRepair".equals(ModLoader.fieldName("z")));
        check("fieldName(legacy C)", "rangeInTiles".equals(ModLoader.fieldName("C")));
        check("fieldName(legacy C_)", "rangeInTiles".equals(ModLoader.fieldName("C_")));
        check("fieldName(legacy E)", "producer".equals(ModLoader.fieldName("E")));
        check("fieldName(legacy F)", "producedBy".equals(ModLoader.fieldName("F")));
        check("fieldName(legacy G)", "canProduce".equals(ModLoader.fieldName("G")));
        check("fieldName(legacy I)", "canCarryAircraft".equals(ModLoader.fieldName("I")));
        check("fieldName(legacy I_)", "canCarryAircraft".equals(ModLoader.fieldName("I_")));
        check("fieldName(legacy J)", "sortWeight".equals(ModLoader.fieldName("J")));
        check("fieldName(legacy a)", "allUnitTypes".equals(ModLoader.fieldName("a")));
        check("fieldName(legacy b)", "damageMatrix".equals(ModLoader.fieldName("b")));
        check("fieldName(semantic id)", "id".equals(ModLoader.fieldName("id")));
        check("fieldName(semantic canRepair)", "canRepair".equals(ModLoader.fieldName("canRepair")));
        // "p" stays an un-mapped (still obfuscated) field, so it must still pass through unchanged.
        check("fieldName(unknown still passes through)", "p".equals(ModLoader.fieldName("p")));

        UnitType semantic = ModLoader.buildUnit(new JsonReader().parse(SEMANTIC), 7);
        UnitType legacy = ModLoader.buildUnit(new JsonReader().parse(LEGACY), 7);

        // 1. Both spellings describe the same unit.
        int compared = 0;
        for (Field field : UnitType.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            Object a = field.get(semantic);
            Object b = field.get(legacy);
            if (!Objects.equals(a, b)) {
                check("field '" + field.getName() + "' matches (semantic=" + a + " legacy=" + b + ")",
                        false);
            }
            compared++;
        }
        check("fields compared", compared > 10);

        // 2. The semantic key really lands in the intended field (not merely a default).
        check("health -> field health", Long.valueOf(1234L).equals(read(semantic, "health")));
        check("speed -> field speed", Float.valueOf(0.75f).equals(read(semantic, "speed")));
        check("damage -> field damage", Float.valueOf(0.4f).equals(read(semantic, "damage")));
        check("range -> field range", Float.valueOf(0.6f).equals(read(semantic, "range")));
        // sightRange comes from the legacy "D_" key in the other document, so it also proves that
        // the upper-case legacy key still lands in the right field.
        check("sightRange -> field sightRange",
                Float.valueOf(9.0f).equals(read(semantic, "sightRange")));
        check("capacity -> field capacity", Integer.valueOf(6).equals(read(semantic, "capacity")));
        check("name -> field name", "Test Unit".equals(read(semantic, "name")));
        check("key -> field key", "TST".equals(read(semantic, "key")));

        if (failures > 0) {
            System.out.println("FIELD_ALIAS FAILED failures=" + failures);
            System.exit(1);
        }
        System.out.println("FIELD_ALIAS PASSED compared=" + compared);
    }

    private static Object read(UnitType unit, String fieldName) throws Exception {
        Field field = UnitType.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(unit);
    }

    private static void check(String what, boolean ok) {
        if (!ok) {
            failures++;
            System.out.println("FAIL " + what);
        }
    }
}

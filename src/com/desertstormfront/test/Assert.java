/*
 * Minimal, dependency-free assertion helper shared by the core-logic unit tests.
 *
 * Why not JUnit: the project builds and runs completely offline (run\build.ps1 compiles
 * src\ with only the lib\ jars on the classpath) and the release must stay portable, so
 * no test framework jar is available. This follows the established gate pattern used by
 * ModOrderedPropertiesTest / ModEventFlowTest: a standalone main() that prints per-check
 * results and ends with a "<MARKER> PASSED" line, exiting non-zero on any failure so
 * run\test.ps1 can assert on it.
 *
 * Every test main routes its checks through this class and finishes with report(...).
 */
package com.desertstormfront.test;

public final class Assert {

    private static int failures;

    private Assert() {
    }

    /** Records a boolean check; prints "ok"/"FAIL" so a failing case is visible in the log. */
    public static void check(String name, boolean ok) {
        if (ok) {
            System.out.println("  ok: " + name);
        } else {
            ++failures;
            System.out.println("  FAIL: " + name);
        }
    }

    /** Float equality within an absolute epsilon (the math helpers are table-approximated). */
    public static void close(String name, float actual, float expected, float eps) {
        check(name + " (got=" + actual + ", want=" + expected + ")",
                Math.abs(actual - expected) <= eps);
    }

    /** Reference/Object equality. */
    public static void equal(String name, Object actual, Object expected) {
        check(name + " (got=" + actual + ", want=" + expected + ")",
                actual == null ? expected == null : actual.equals(expected));
    }

    /** Prints the final marker; exits with status 1 when anything failed. */
    public static void report(String marker, String detail) {
        if (failures > 0) {
            System.out.println(marker + " FAILED failures=" + failures);
            System.exit(1);
        }
        System.out.println(marker + " PASSED " + detail);
    }
}

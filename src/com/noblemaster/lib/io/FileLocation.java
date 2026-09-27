/*
 * Where a GameFile comes from: INTERNAL is the bundled data/ directory (absolute when getdown
 * reports an install path), CLASSPATH the bundled jar, EXTERNAL the writable user directory.
 *
 * Deobfuscation: the two constructor booleans (formerly d and e) are never read anywhere in the
 * tree, so they are deliberately left unnamed instead of guessed. Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.io;

public enum FileLocation {
    INTERNAL(true, false),
    CLASSPATH(true, false),
    EXTERNAL(true, true);

    private boolean d;
    private boolean e;

    /*
     * WARNING - void declaration
     */
    private FileLocation(boolean var3_1, boolean var4_2) {
        this.d = var3_1;
        this.e = var4_2;
    }
}


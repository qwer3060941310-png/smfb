/*
 * Deterministic coordinate hash: hash(value) is the avalanche step, hash(x, y) mixes the two
 * coordinates. Used for stable per-tile variation (e.g. which road variant a tile shows).
 *
 * Deobfuscation: both a(int) and a(int, int) became hash, distinguished by their descriptors.
 * Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.util;

public final class HashUtils {
    public static int hash(int i0) {
        i0 = i0 ^ 0x3D ^ i0 >> 16;
        i0 += i0 << 3;
        i0 ^= i0 >> 4;
        i0 *= 668265261;
        i0 ^= i0 >> 15;
        return i0;
    }

    public static int hash(int i0, int i1) {
        return HashUtils.hash(i0 * 0x1F1F1F1F ^ i1);
    }
}


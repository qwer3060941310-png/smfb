/*
 * Fast 48-bit linear congruential generator. nextBits() advances the state and returns the top
 * bits; nextInt(bound)/nextBoolean()/nextFloat() are built on it. Fully deterministic per seed,
 * which is what save/replay relies on.
 *
 * Deobfuscation: the field a became seed; a() -> getSeed, the private b(int) -> nextBits,
 * b() -> nextBoolean, a(int) -> nextInt, c() -> nextFloat. Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.util;

public strictfp class FastRandom {
    private long seed;

    public FastRandom() {
        this(System.nanoTime());
    }

    public FastRandom(long l1) {
        this.seed = l1 & 0xFFFFFFFFFFFFL;
    }

    public long getSeed() {
        return this.seed;
    }

    private long nextBits(int i1) {
        this.seed = this.seed * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
        return this.seed >>> 48 - i1;
    }

    public boolean nextBoolean() {
        return this.nextBits(1) == 0L;
    }

    public int nextInt(int i1) {
        return (int)(this.nextBits(31) % (long)i1);
    }

    public float nextFloat() {
        return (float)this.nextBits(31) / 2.14748365E9f;
    }
}


/*
 * Minimal read contract for the world/save serialisation: one method per primitive the serializer
 * writes, plus readString for its chunked UTF format and close.
 *
 * Deobfuscation: a()/b()/c()/d()/e()/f()/g() became readString/readBoolean/readByte/readInt/
 * readLong/readFloat/close, following the implementation in StreamDataReader.
 * Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.io.stream;

public interface DataReader {
    public String readString();

    public boolean readBoolean();

    public byte readByte();

    public int readInt();

    public long readLong();

    public float readFloat();

    public void close();
}


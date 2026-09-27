/*
 * Minimal write contract for the world/save serialisation: one method per primitive, a chunked
 * writeString (a single UTF string is limited to 65535 bytes) plus flush and close.
 *
 * Deobfuscation: the a(...) overloads and a()/b() became writeString/writeBoolean/writeByte/
 * writeInt/writeLong/writeFloat/flush/close. Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.io.stream;

public interface DataWriter {
    public void writeString(String var1);

    public void writeBoolean(boolean var1);

    public void writeByte(byte var1);

    public void writeInt(int var1);

    public void writeLong(long var1);

    public void writeFloat(float var1);

    public void flush();

    public void close();
}


/*
 * Robustness test for com.noblemaster.lib.net.match.MatchCodec.
 *
 * MatchCodec is the wire/file format for a match record (multiplayer lobby state). It is parsed
 * from network sockets and from the save file, i.e. from data the game does not control, so its
 * behaviour on malformed input matters as much as its round-trip fidelity.
 *
 * What this pins (T04):
 *  - a record survives an encode -> decode round-trip unchanged;
 *  - a null record encodes as a header-only stream that decodes back to null;
 *  - a truncated payload fails loudly instead of yielding a corrupt record;
 *  - a large (>1 chunk) string round-trips through the chunked UTF path;
 *  - a stream stamped with an unsupported format version is rejected with a readable error
 *    (the version int used to be read and discarded).
 *
 * Runs head-less (pure java.io, no libGDX application), so it is part of run\dev.ps1 test.
 * Exit code 0 and "MATCHCODEC PASSED" mean success.
 *
 * MatchRecord's no-arg constructor is package-private, so it is created reflectively here to keep
 * this test out of the library package.
 */
package com.desertstormfront.test;

import com.noblemaster.lib.io.stream.impl.StreamDataReader;
import com.noblemaster.lib.io.stream.impl.StreamDataWriter;
import com.noblemaster.lib.net.match.MatchCodec;
import com.noblemaster.lib.net.match.MatchRecord;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;

public final class MatchCodecTest {

    private MatchCodecTest() {
    }

    public static void main(String[] args) {
        try {
            run();
        } catch (Throwable t) {
            System.out.println("MATCHCODEC FAILED unexpected=" + t);
            t.printStackTrace(System.out);
            System.exit(1);
        }
    }

    private static MatchRecord newRecord() throws Exception {
        Constructor<MatchRecord> ctor = MatchRecord.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        return ctor.newInstance();
    }

    private static byte[] encode(MatchRecord record) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamDataWriter writer = new StreamDataWriter(buffer);
        MatchCodec.write(writer, record);
        writer.flush();
        return buffer.toByteArray();
    }

    private static MatchRecord decode(byte[] bytes) {
        return MatchCodec.read(new StreamDataReader(new ByteArrayInputStream(bytes)));
    }

    private static void run() throws Exception {
        // --- round-trip with distinct values on every field ----------------------
        MatchRecord out = newRecord();
        out.setId(123456789L);
        out.setProductId("app");
        out.setTitle("match");
        out.setLabel("info");
        out.setLocalAddress("10.0.0.1");
        out.setLocalPort(2022);
        out.setExternalAddress("1.2.3.4");
        out.setExternalPort(2018);
        out.setPortOpen(true);
        out.setTimestamp(999L);

        byte[] bytes = encode(out);
        MatchRecord in = decode(bytes);

        Assert.check("record decoded (non-null)", in != null);
        Assert.check("id", in.getId() == out.getId());
        Assert.check("appName", "app".equals(in.getProductId()));
        Assert.check("matchName", "match".equals(in.getTitle()));
        Assert.check("matchInfo", "info".equals(in.getLabel()));
        Assert.check("localAddress", "10.0.0.1".equals(in.getLocalAddress()));
        Assert.check("localPort", in.getLocalPort() == 2022);
        Assert.check("externalAddress", "1.2.3.4".equals(in.getExternalAddress()));
        Assert.check("externalPort", in.getExternalPort() == 2018);
        Assert.check("portOpen", in.isPortOpen());
        Assert.check("timestamp", in.getTimestamp() == 999L);

        // --- null record -> header-only stream -> null ---------------------------
        Assert.check("null record round-trips to null", decode(encode(null)) == null);

        // --- truncated payload fails loudly --------------------------------------
        byte[] truncated = new byte[bytes.length / 2];
        System.arraycopy(bytes, 0, truncated, 0, truncated.length);
        boolean threwOnTruncated = false;
        try {
            decode(truncated);
        } catch (Throwable t) {
            threwOnTruncated = true;
        }
        Assert.check("truncated input fails loudly", threwOnTruncated);

        // --- large (>1 chunk of 21845 chars) string round-trips -------------------
        StringBuilder big = new StringBuilder();
        for (int i = 0; i < 50000; ++i) {
            big.append('x');
        }
        MatchRecord bigOut = newRecord();
        bigOut.setProductId("big");
        bigOut.setTitle(big.toString());
        MatchRecord bigIn = decode(encode(bigOut));
        Assert.check("multi-chunk string round-trips", bigIn != null && big.toString().equals(bigIn.getTitle()));

        // --- unsupported version is rejected with a readable error ---------------
        ByteArrayOutputStream versionBuffer = new ByteArrayOutputStream();
        StreamDataWriter versionWriter = new StreamDataWriter(versionBuffer);
        versionWriter.writeBoolean(true);       // record header present
        versionWriter.writeInt(2);          // unsupported version (supported is MatchCodec.VERSION)
        versionWriter.writeLong(0L);         // a little payload so the read reaches the version check
        versionWriter.flush();
        String message = null;
        try {
            decode(versionBuffer.toByteArray());
        } catch (Throwable t) {
            message = String.valueOf(t.getMessage());
        }
        Assert.check("unsupported version rejected", message != null && message.contains("version"));
        Assert.check("unsupported version message is readable",
                message != null && message.contains(String.valueOf(MatchCodec.VERSION)));

        Assert.report("MATCHCODEC", "round-trip/null/truncation/multichunk/version ok");
    }
}

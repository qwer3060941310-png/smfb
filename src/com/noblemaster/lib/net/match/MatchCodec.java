/*
 * Wire codec for MatchRecord: read/write carry the nullable flag used by the stream protocol,
 * readBody/writeBody are the raw payload (version int + the ten fields in declaration order).
 *
 * Deobfuscation: a(DataReader)/a(DataWriter,MatchRecord)/a(DataReader,MatchRecord)/
 * b(DataWriter,MatchRecord) became read/write/readBody/writeBody. Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import com.noblemaster.lib.net.match.MatchRecord;

public final class MatchCodec {
    public static final MatchRecord read(DataReader dataReader) {
        if (dataReader.readBoolean()) {
            MatchRecord matchRecord = new MatchRecord();
            MatchCodec.readBody(dataReader, matchRecord);
            return matchRecord;
        }
        return null;
    }

    public static final void write(DataWriter dataWriter, MatchRecord matchRecord) {
        if (matchRecord != null) {
            dataWriter.writeBoolean(true);
            MatchCodec.writeBody(dataWriter, matchRecord);
        } else {
            dataWriter.writeBoolean(false);
        }
    }

    /** Format version written by {@link #b(DataWriter, MatchRecord)}; readers reject anything else. */
    public static final int VERSION = 1;

    public static final void readBody(DataReader dataReader, MatchRecord matchRecord) {
        // The first int is the record format version. It used to be read and discarded, so a
        // stream from a different version (or corrupt input) was parsed as if it were current,
        // producing a silently wrong record. Reject it with a readable error instead.
        int version = dataReader.readInt();
        if (version != VERSION) {
            throw new IllegalArgumentException("Unsupported match record version: " + version
                    + " (expected " + VERSION + ")");
        }
        matchRecord.setId(dataReader.readLong());
        matchRecord.setProductId(dataReader.readString());
        matchRecord.setTitle(dataReader.readString());
        matchRecord.setLabel(dataReader.readString());
        matchRecord.setLocalAddress(dataReader.readString());
        matchRecord.setLocalPort(dataReader.readInt());
        matchRecord.setExternalAddress(dataReader.readString());
        matchRecord.setExternalPort(dataReader.readInt());
        matchRecord.setPortOpen(dataReader.readBoolean());
        matchRecord.setTimestamp(dataReader.readLong());
    }

    public static final void writeBody(DataWriter dataWriter, MatchRecord matchRecord) {
        dataWriter.writeInt(1);
        dataWriter.writeLong(matchRecord.getId());
        dataWriter.writeString(matchRecord.getProductId());
        dataWriter.writeString(matchRecord.getTitle());
        dataWriter.writeString(matchRecord.getLabel());
        dataWriter.writeString(matchRecord.getLocalAddress());
        dataWriter.writeInt(matchRecord.getLocalPort());
        dataWriter.writeString(matchRecord.getExternalAddress());
        dataWriter.writeInt(matchRecord.getExternalPort());
        dataWriter.writeBoolean(matchRecord.isPortOpen());
        dataWriter.writeLong(matchRecord.getTimestamp());
    }
}


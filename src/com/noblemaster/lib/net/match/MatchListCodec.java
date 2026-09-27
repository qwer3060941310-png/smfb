/*
 * List codec for the match list reply: read/write handle the nullable flag, readBody/writeBody
 * write a count followed by that many MatchCodec records.
 *
 * Deobfuscation: the four single-letter methods became read/write/readBody/writeBody, mirroring
 * MatchCodec. Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import com.noblemaster.lib.net.match.MatchCodec;
import com.noblemaster.lib.net.match.MatchList;
import com.noblemaster.lib.net.match.MatchRecord;

public final class MatchListCodec {
    public static MatchList read(DataReader dataReader) {
        if (dataReader.readBoolean()) {
            MatchList matchList = new MatchList();
            MatchListCodec.readBody(dataReader, matchList);
            return matchList;
        }
        return null;
    }

    public static void write(DataWriter dataWriter, MatchList matchList) {
        if (matchList != null) {
            dataWriter.writeBoolean(true);
            MatchListCodec.writeBody(dataWriter, matchList);
        } else {
            dataWriter.writeBoolean(false);
        }
    }

    public static void readBody(DataReader dataReader, MatchList matchList) {
        int i2 = dataReader.readInt();
        int i3 = 0;
        while (i3 < i2) {
            matchList.add(MatchCodec.read(dataReader));
            ++i3;
        }
    }

    public static void writeBody(DataWriter dataWriter, MatchList matchList) {
        int i2 = matchList.size();
        dataWriter.writeInt(i2);
        int i3 = 0;
        while (i3 < i2) {
            MatchCodec.write(dataWriter, (MatchRecord)matchList.get(i3));
            ++i3;
        }
    }
}


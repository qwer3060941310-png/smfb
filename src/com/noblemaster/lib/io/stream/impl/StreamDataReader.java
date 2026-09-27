/*
 * InputStream-backed DataReader. readString mirrors StreamDataWriter.writeString: a boolean
 * "not null" flag, a short chunk count and 21845-character UTF chunks.
 *
 * Deobfuscation: the fields a/b became inputStream/dataInput, and the DataReader methods took their
 * semantic names. The UncheckedDataInputStream wrapper below converts IOExceptions into
 * RuntimeExceptions so the save format stays unchecked. Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.io.stream.impl;

import com.noblemaster.lib.io.stream.impl.AbstractDataReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamDataReader
extends AbstractDataReader {
    protected InputStream inputStream;
    private UncheckedDataInputStream dataInput;

    public StreamDataReader(InputStream inputStream) {
        this.inputStream = inputStream;
        this.dataInput = new UncheckedDataInputStream(inputStream);
    }

    /**
     * 婵犵數鍋為崹鍫曞箰閸濄儳鐭撻柛顭戝亝閸欏繘鐓崶銊р槈闁活厽顨嗘穱濠囧Χ閸曨叀绐楀┑顔硷工婢т粙鍩€椤掍緡鍟忛柛鐘崇墵瀹曟劙骞栨担鐟颁函闂佸壊鍋呭ú姗€宕?DataInputStream 闂傚倷绀侀幉锟犳偋閺囥垹绠犻柟鎯у殺婢跺绡€闁搞儯鍔嶅▍鏍煟韫囨洖浠╂俊顐㈠瀵剝顦归柡灞剧洴婵＄柉顦茬€涙繈姊?StreamDataWriter闂?
     */
    private static final class UncheckedDataInputStream {
        private final DataInputStream delegate;

        UncheckedDataInputStream(InputStream inputStream) {
            this.delegate = new DataInputStream(inputStream);
        }

        public int read() {
            try {
                return this.delegate.read();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public boolean readBoolean() {
            try {
                return this.delegate.readBoolean();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public byte readByte() {
            try {
                return this.delegate.readByte();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public short readShort() {
            try {
                return this.delegate.readShort();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public int readInt() {
            try {
                return this.delegate.readInt();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public long readLong() {
            try {
                return this.delegate.readLong();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public float readFloat() {
            try {
                return this.delegate.readFloat();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public double readDouble() {
            try {
                return this.delegate.readDouble();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public String readUTF() {
            try {
                return this.delegate.readUTF();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void close() {
            try {
                this.delegate.close();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }
    }

    public String readString() {
        if (this.dataInput.readBoolean()) {
            int i1 = this.dataInput.readShort();
            if (i1 == 0) {
                return "";
            }
            if (i1 == 1) {
                return this.dataInput.readUTF();
            }
            StringBuffer stringBuffer = new StringBuffer();
            int i3 = 0;
            while (i3 < i1) {
                stringBuffer.append(this.dataInput.readUTF());
                ++i3;
            }
            return stringBuffer.toString();
        }
        return null;
    }

    public boolean readBoolean() {
        return this.dataInput.readBoolean();
    }

    public byte readByte() {
        return this.dataInput.readByte();
    }

    public int readInt() {
        return this.dataInput.readInt();
    }

    public long readLong() {
        return this.dataInput.readLong();
    }

    public float readFloat() {
        return this.dataInput.readFloat();
    }

    public void close() {
        this.dataInput.close();
    }
}


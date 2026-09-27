/*
 * OutputStream-backed DataWriter. writeString splits at 21845 characters because
 * DataOutputStream.writeUTF cannot encode more than 65535 bytes at once.
 *
 * Deobfuscation: the fields a/b became outputStream/dataOutput, and the DataWriter methods took
 * their semantic names (writeString/writeBoolean/writeByte/writeInt/writeLong/writeFloat/flush/
 * close). Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.io.stream.impl;

import com.noblemaster.lib.io.stream.impl.AbstractDataWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StreamDataWriter
extends AbstractDataWriter {
    protected OutputStream outputStream;
    private UncheckedDataOutputStream dataOutput;

    public StreamDataWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.dataOutput = new UncheckedDataOutputStream(outputStream);
    }

    /**
     * 婵犵數鍋為崹鍫曞箰閸濄儳鐭撻柛顭戝亝閸欏繘鐓崶銊р槈闁活厽顨嗘穱濠囧Χ閸曨叀绐楀┑顔硷工婢т粙鍩€椤掍緡鍟忛柛鐘崇墵瀹曟劙骞栨担鐟颁函闂佸壊鍋呭ú姗€宕?DataOutputStream 闂傚倷绀侀幉锟犳偋閺囥垹绠犻柟鎯у殺婢跺绡€闁搞儺鐏?
     * 婵犵數鍋為崹鍫曞箰閹间焦鏅濋柕澶嗘櫆閸嬪鏌涢幘鑼额唹闁稿鎸搁埥澶娾枎濡崵鏆┑鐘殿暯濡插懘宕ｉ崘顔肩畾鐎广儱妫欐刊鎾煟閹寸儐鐒介柛鎴滅矙濮婅櫣鎲撮崟顏囧焻闂佺瀛╅幐鎶藉箚閸愵喗鍋勯柛蹇曞帶娴?DataOutputStream 闂傚倷鐒﹂惇褰掑礉瀹€鈧埀顒佸嚬閸撶喎鐣烽鐐茬閻犲洤寮堕ˉ婵嬫煟閻斿摜鎳冮悗姘煎櫍婵″瓨绻濋崒妤侇潔闂佽鍎抽悺銊╁煕閹扮増鐓熼柕澶堝劜閸ゅ洨鈧鍠栭悘姘嚗閸曨剙绶為悗锝庝簻缁ㄣ儱鈹?try/catch
     * 闂傚倷鐒︾€笛呯矙閹达附鍋嬮柛鈩冾殢閸ゆ洟鏌ｉ敐鍛伇缁惧彞绮欓弻鈥愁吋鎼粹€愁潾濠碘€虫▕閸ㄤ即鍩ユ径鎰闁瑰墎鐡旈埀顒侇殜閹粙顢涢敐鍛凹闂?jar 闂傚倷鑳堕～瀣礃閳哄倹鍊锋俊鐐€愰弫顏堝礃閻愵剚銆冮梻渚€娼ч悧鍡欐崲閹烘梻鐭嗛柛鈩冪⊕閻撱儲绻涢幋鐑嗙劷闁圭晫瀵磛a 濠电姷鏁告慨宥夊礋椤愩埄娼曢梻浣虹帛閻楁鍒掗幘鎰佸殨妞ゆ洍鍋撻柟顔规櫅閻ｇ兘宕堕妷銏犱壕濠电姴娲﹂崑鐘崇箾閹寸偟澧┑顔瑰亾闂備浇顫夊鎸庣椤忓牆鏋佺€广儱娲ｅ▽顏堟煟閹伴潧澧柣婵囧哺濮婃椽妫冮埡鍕槹闂佸憡鏌ㄧ粔褰掋€佸Δ鈧埞鎴犫偓锝庝簽閿涙稑鈹戦绛嬬劸濞存粠鍓熼獮鏍偂楠炵喎缍婇幃鈺呭箵閹烘挃鈺呮⒑濮瑰洤濡界紒璇插楠炲螖閸涱厼绐涘銈嗗姉閸犲孩绂?
     */
    private static final class UncheckedDataOutputStream {
        private final DataOutputStream delegate;

        UncheckedDataOutputStream(OutputStream outputStream) {
            this.delegate = new DataOutputStream(outputStream);
        }

        public void write(int value) {
            try {
                this.delegate.write(value);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void write(byte[] data) {
            try {
                this.delegate.write(data);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void write(byte[] data, int offset, int length) {
            try {
                this.delegate.write(data, offset, length);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void writeBoolean(boolean value) {
            try {
                this.delegate.writeBoolean(value);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void writeByte(int value) {
            try {
                this.delegate.writeByte(value);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void writeShort(int value) {
            try {
                this.delegate.writeShort(value);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void writeInt(int value) {
            try {
                this.delegate.writeInt(value);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void writeLong(long value) {
            try {
                this.delegate.writeLong(value);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void writeFloat(float value) {
            try {
                this.delegate.writeFloat(value);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void writeDouble(double value) {
            try {
                this.delegate.writeDouble(value);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void writeUTF(String value) {
            try {
                this.delegate.writeUTF(value);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }

        public void flush() {
            try {
                this.delegate.flush();
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

    public void writeString(String string) {
        if (string != null) {
            this.dataOutput.writeBoolean(true);
            int i2 = (string.length() + 21845 - 1) / 21845;
            this.dataOutput.writeShort(i2);
            if (i2 == 1) {
                this.dataOutput.writeUTF(string);
            } else {
                int i3 = 0;
                int i4 = 0;
                while (i4 < i2) {
                    int i5 = i3 + 21845;
                    if (i5 > string.length()) {
                        i5 = string.length();
                    }
                    this.dataOutput.writeUTF(string.substring(i3, i5));
                    i3 += 21845;
                    ++i4;
                }
            }
        } else {
            this.dataOutput.writeBoolean(false);
        }
    }

    public void writeBoolean(boolean bl) {
        this.dataOutput.writeBoolean(bl);
    }

    public void writeByte(byte by) {
        this.dataOutput.writeByte(by);
    }

    public void writeInt(int i1) {
        this.dataOutput.writeInt(i1);
    }

    public void writeLong(long l1) {
        this.dataOutput.writeLong(l1);
    }

    public void writeFloat(float f1) {
        this.dataOutput.writeFloat(f1);
    }

    public void flush() {
        this.dataOutput.flush();
    }

    public void close() {
        this.dataOutput.close();
    }
}


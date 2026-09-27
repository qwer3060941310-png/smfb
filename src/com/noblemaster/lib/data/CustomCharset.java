/*
 * Byte-to-text decoder base class: decode(byte[]) walks the input trying every byte length from
 * getMinBytesPerChar() to getMaxBytesPerChar(); the subclass answers how many characters that
 * length yields at a given offset (countCharsAt) and returns each decoded char (decodeCharAt).
 *
 * Deobfuscation: a(byte[]) -> decode ; a()/b()/c() -> getMinBytesPerChar/getMaxBytesPerChar/
 * getMaxCharsPerByte ; a(int,int,byte[]) -> countCharsAt ; a(int,int,int,byte[]) -> decodeCharAt.
 * Evidence: run\map-i18n-data-remain.tsv.
 */
package com.noblemaster.lib.data;

public abstract class CustomCharset {
    public String decode(byte[] byArray) {
        StringBuilder stringBuilder = new StringBuilder(byArray.length * this.getMaxCharsPerByte() / this.getMinBytesPerChar() + 1);
        int i3 = 0;
        block0: while (i3 < byArray.length) {
            int i4 = this.getMinBytesPerChar();
            while (i4 <= this.getMaxBytesPerChar()) {
                int i5 = this.countCharsAt(i3, i4, byArray);
                if (i5 > 0) {
                    int i6 = 0;
                    while (i6 < i5) {
                        stringBuilder.append(this.decodeCharAt(i6, i3, i4, byArray));
                        ++i6;
                    }
                    i3 += i4;
                    continue block0;
                }
                ++i4;
            }
        }
        return stringBuilder.toString();
    }

    public abstract int getMinBytesPerChar();

    public abstract int getMaxBytesPerChar();

    public abstract int getMaxCharsPerByte();

    public abstract int countCharsAt(int var1, int var2, byte[] var3);

    public abstract char decodeCharAt(int var1, int var2, int var3, byte[] var4);
}


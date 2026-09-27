/*
 * Line-oriented reader over an in-memory string: returns one line per call, skipping CR/LF and
 * returning null at the end of the text.
 *
 * Deobfuscation: the fields a/b became text/position and a(String)/a() became of/readLine, named
 * after their only caller (OrderedProperties.readLogicalLine). Evidence: run\map-i18n-data-remain.tsv.
 */
package com.noblemaster.lib.i18n;

public final class LineReader {
    private String text;
    private int position;

    private LineReader(String string) {
        this.text = string;
        this.position = 0;
    }

    public static LineReader of(String string) {
        return new LineReader(string);
    }

    public String readLine() {
        int i1 = this.text.indexOf(10, this.position);
        int i2 = this.text.indexOf(13, this.position);
        if (i1 == -1 && i2 == -1) {
            if (this.position == this.text.length()) {
                return null;
            }
            String string = this.text.substring(this.position, this.text.length());
            this.position = this.text.length();
            return string;
        }
        int n = i1 == -1 ? i2 : (i2 == -1 ? i1 : (i1 < i2 ? i1 : i2));
        String string = this.text.substring(this.position, n);
        this.position = n;
        while (this.position < this.text.length() && (this.text.charAt(this.position) == '\n' || this.text.charAt(this.position) == '\r')) {
            ++this.position;
        }
        return string;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.data;

import com.noblemaster.lib.math.MathHelper;
import java.util.Date;

/**
 * An immutable point in time wrapped around a single {@code long} millisecond count, with helpers to
 * pull the calendar fields apart and to format/parse them.
 *
 * <p>Two views of the same instant are exposed. {@code getYear}/{@code getMonth}/{@code getDay}/
 * {@code getHour}/{@code getMinute}/{@code getSecond}/{@code getMillisecond} (and {@link #toString()})
 * go through a cached local {@link Date}, i.e. the wall-clock fields in the JVM timezone; the matching
 * {@code ...Utc} getters go through a cached UTC {@link Date}. {@link #create} builds the instant from
 * UTC fields while {@link #createLocal} builds it from local fields, so the two pairs are not
 * interchangeable near a timezone boundary.
 *
 * <p>{@link #MIN} and {@link #MAX} are the supported bounds; {@link #yearOneMillis} is the millisecond
 * value of year 1, used as the BC/AD floor, and {@link #daysInMonthNormal}/{@link #daysInMonthLeap}
 * back the range checks in {@link #validate}.
 */
public final class DateTime
implements Comparable {
    /** The earliest representable instant ({@code create(-1000000, 1, 1, 0, 0, 0, 0)}). */
    public static final DateTime MIN;
    /** The latest representable instant ({@code create(1000000, 12, 31, 23, 59, 59, 999)}). */
    public static final DateTime MAX;
    /** Millisecond value of year 1, the BC/AD floor used by the year getters. */
    private static final long yearOneMillis;
    /** Days per month in a normal year, indexed by month minus one. */
    private static final int[] daysInMonthNormal;
    /** Days per month in a leap year, indexed by month minus one. */
    private static final int[] daysInMonthLeap;
    /** The wrapped instant in milliseconds. */
    private long timeMillis;
    /** Cached {@link Date} for the local (wall-clock) view, lazily built by {@link #getLocalDate()}. */
    private Date localDate;
    /** Cached {@link Date} for the UTC view, lazily built by {@link #getUtcDate()}. */
    private Date utcDate;

    static {
        yearOneMillis = -62135596800000L;
        daysInMonthNormal = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        daysInMonthLeap = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        MIN = DateTime.create(-1000000, 1, 1, 0, 0, 0, 0);
        MAX = DateTime.create(1000000, 12, 31, 23, 59, 59, 999);
    }

    public DateTime() {
        this(System.currentTimeMillis());
    }

    public DateTime(long l1) {
        this.timeMillis = l1;
    }

    /** The current instant in the default timezone. */
    public static DateTime now() {
        return new DateTime();
    }

    /** Cached {@link Date} for the wall-clock (local) view of {@link #timeMillis}. */
    private synchronized Date getLocalDate() {
        if (this.localDate == null) {
            this.localDate = new Date();
            this.setTimeMillis(this.timeMillis);
        }
        return this.localDate;
    }

    /** Cached {@link Date} for the UTC view of {@link #timeMillis}. */
    private synchronized Date getUtcDate() {
        if (this.utcDate == null) {
            this.utcDate = new Date();
            this.setTimeMillis(this.timeMillis);
        }
        return this.utcDate;
    }

    /** Builds an instant from UTC calendar fields (year, month, day, hour, minute, second, millis). */
    public static DateTime create(int i0, int i1, int i2, int i3, int i4, int i5, int i6) {
        DateTime dateTime = new DateTime();
        dateTime.setUtc(i0, i1, i2, i3, i4, i5, i6);
        return dateTime;
    }

    /** Builds an instant from local (wall-clock) calendar fields; not equal to {@link #create} near a tz boundary. */
    public static DateTime createLocal(int i0, int i1, int i2, int i3, int i4, int i5, int i6) {
        DateTime dateTime = new DateTime();
        dateTime.setLocal(i0, i1, i2, i3, i4, i5, i6);
        return dateTime;
    }

    /** Parses a text form produced by {@link #format}; only the two documented patterns are supported. */
    public static DateTime parse(String string, String string2) {
        if (string.equals("{yyyy}-{MM}-{dd} {hh}:{mm}:{ss}")) {
            int i2 = (string2.charAt(0) - 48) * 1000 + (string2.charAt(1) - 48) * 100 + (string2.charAt(2) - 48) * 10 + (string2.charAt(3) - 48);
            int i3 = (string2.charAt(5) - 48) * 10 + (string2.charAt(6) - 48);
            int i4 = (string2.charAt(8) - 48) * 10 + (string2.charAt(9) - 48);
            int i5 = (string2.charAt(11) - 48) * 10 + (string2.charAt(12) - 48);
            int i6 = (string2.charAt(14) - 48) * 10 + (string2.charAt(15) - 48);
            int i7 = (string2.charAt(17) - 48) * 10 + (string2.charAt(18) - 48);
            return DateTime.createLocal(i2, i3, i4, i5, i6, i7, 0);
        }
        if (string.equals("{yyyy}{MM}{dd}.{hh}{mm}")) {
            int i2 = (string2.charAt(0) - 48) * 1000 + (string2.charAt(1) - 48) * 100 + (string2.charAt(2) - 48) * 10 + (string2.charAt(3) - 48);
            int i3 = (string2.charAt(4) - 48) * 10 + (string2.charAt(5) - 48);
            int i4 = (string2.charAt(6) - 48) * 10 + (string2.charAt(7) - 48);
            int i5 = (string2.charAt(9) - 48) * 10 + (string2.charAt(10) - 48);
            int i6 = (string2.charAt(11) - 48) * 10 + (string2.charAt(12) - 48);
            return DateTime.createLocal(i2, i3, i4, i5, i6, 0, 0);
        }
        throw new IllegalArgumentException("Parsing pattern not (yet) supported: " + string);
    }

    /** Replaces the wrapped instant and refreshes the two cached {@link Date}s (local view stays shifted by tz offset). */
    public void setTimeMillis(long l1) {
        this.timeMillis = l1;
        if (this.utcDate != null) {
            this.utcDate.setTime(l1);
        }
        if (this.localDate != null) {
            this.localDate.setTime(l1);
            long l3 = (long)this.localDate.getTimezoneOffset() * 60L * 1000L;
            this.localDate.setTime(l1 + l3);
        }
    }

    /** The wrapped instant in milliseconds. */
    public long getMillis() {
        return this.timeMillis;
    }

    /** Sets the instant from UTC fields, computing the millisecond value with {@link Date#UTC}. */
    public void setUtc(int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.validate(i1, i2, i3, i4, i5, i6, i7);
        this.timeMillis = Date.UTC(i1 - 1900, i2 - 1, i3, i4, i5, i6);
        this.setTimeMillis(this.timeMillis + (long)i7);
    }

    /** Sets the instant from local fields through the cached UTC {@link Date}. */
    public void setLocal(int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.validate(i1, i2, i3, i4, i5, i6, i7);
        Date date = this.getUtcDate();
        date.setTime(0L);
        date.setYear(i1 - 1900);
        date.setMonth(i2 - 1);
        date.setDate(i3);
        date.setHours(i4);
        date.setMinutes(i5);
        date.setSeconds(i6);
        this.setTimeMillis(date.getTime() + (long)i7);
    }

    /** Range-checks the seven calendar fields and throws {@link IllegalArgumentException} on any violation. */
    private void validate(int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        int[] nArray;
        if (i1 < -1000000 || i1 > 1000000) {
            throw new IllegalArgumentException("The year is out of range.");
        }
        if (i2 < 1 || i2 > 12) {
            throw new IllegalArgumentException("The month is out of range.");
        }
        if (i4 < 0 || i4 > 23) {
            throw new IllegalArgumentException("The hour is out of range.");
        }
        if (i5 < 0 || i5 > 59) {
            throw new IllegalArgumentException("The minute is out of range.");
        }
        if (i6 < 0 || i6 > 59) {
            throw new IllegalArgumentException("The second is out of range.");
        }
        if (i7 < 0 || i7 > 999) {
            throw new IllegalArgumentException("The millisecond is out of range.");
        }
        int[] nArray2 = nArray = DateTime.isLeapYear(i1) ? daysInMonthLeap : daysInMonthNormal;
        if (i3 < 1 || i3 > nArray[i2 - 1]) {
            throw new IllegalArgumentException("The day is out of range.");
        }
    }

    /** True when the UTC year/month/day equals the given values. */
    public boolean isSameDate(int i1, int i2, int i3) {
        return this.getYearUtc() == i1 && this.getMonthUtc() == i2 && this.getDayUtc() == i3;
    }

    /** True when the UTC month/day equals the given values in the current UTC year. */
    public boolean isSameMonthDay(int i1, int i2) {
        return this.isSameDate(this.getYearUtc(), i1, i2);
    }

    // The calendar getters come in two parallel families: the plain names read the local (wall-clock)
    // view via getLocalDate(), while the ...Utc names read the UTC view via getUtcDate(). Both apply
    // the yearOneMillis floor so years before 1 are reported as 1 - n (BC). getMillisecond/
    // getMillisecondUtc are identical because the remainder modulo 1000 is timezone independent.
    public int getYear() {
        int i1 = this.getLocalDate().getYear() + 1900;
        if (this.timeMillis < yearOneMillis) {
            return 1 - i1;
        }
        return i1;
    }

    public int getYearUtc() {
        int i1 = this.getUtcDate().getYear() + 1900;
        if (this.timeMillis < yearOneMillis) {
            return 1 - i1;
        }
        return i1;
    }

    /** Proleptic-Gregorian leap year rule (every 4th year, except centuries unless divisible by 400). */
    public static boolean isLeapYear(int i0) {
        if (i0 % 4 != 0) {
            return false;
        }
        if (i0 < 1582) {
            return true;
        }
        if (i0 % 400 == 0) {
            return true;
        }
        return i0 % 100 != 0;
    }

    public int getMonth() {
        return this.getLocalDate().getMonth() + 1;
    }

    public int getMonthUtc() {
        return this.getUtcDate().getMonth() + 1;
    }

    public int getDay() {
        return this.getLocalDate().getDate();
    }

    public int getDayUtc() {
        return this.getUtcDate().getDate();
    }

    public int getHour() {
        return this.getLocalDate().getHours();
    }

    public int getHourUtc() {
        return this.getUtcDate().getHours();
    }

    public int getMinute() {
        return this.getLocalDate().getMinutes();
    }

    public int getMinuteUtc() {
        return this.getUtcDate().getMinutes();
    }

    public int getSecond() {
        return this.getLocalDate().getSeconds();
    }

    public int getSecondUtc() {
        return this.getUtcDate().getSeconds();
    }

    public int getMillisecond() {
        return (int)(MathHelper.abs(this.timeMillis) % 1000L);
    }

    public int getMillisecondUtc() {
        return (int)(MathHelper.abs(this.timeMillis) % 1000L);
    }

    /** Shifts the instant by the given number of 24-hour days. */
    public void addDays(int i1) {
        this.setTimeMillis(this.timeMillis + (long)i1 * 24L * 3600L * 1000L);
    }

    /** Strict after comparison on the wrapped millisecond values. */
    public boolean isAfter(DateTime dateTime) {
        return this.timeMillis > dateTime.timeMillis;
    }

    /** Formats using the local (wall-clock) fields; the default {@link #toString()} uses this. */
    public String format(String string) {
        return this.formatImpl(string, this.getYear(), this.getMonth(), this.getDay(), this.getHour(), this.getMinute(), this.getSecond(), this.getMillisecond());
    }

    /** Formats using the UTC fields. */
    public String formatUtc(String string) {
        return this.formatImpl(string, this.getYearUtc(), this.getMonthUtc(), this.getDayUtc(), this.getHourUtc(), this.getMinuteUtc(), this.getSecondUtc(), this.getMillisecondUtc());
    }

    /** Substitutes the documented {@code {yyyy}{MM}{dd}{hh}{mm}{ss}{SSS}} tokens into the pattern. */
    private String formatImpl(String string, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        String string2;
        if (string.contains("{yG}")) {
            string2 = i2 < 1 ? String.valueOf(String.valueOf(-i2 + 1)) + "BC" : String.valueOf(String.valueOf(i2)) + "AD";
            string = string.replace("{yG}", string2);
        }
        if (string.contains("{y_G}")) {
            string2 = i2 < 1 ? String.valueOf(String.valueOf(-i2 + 1)) + " BC" : String.valueOf(String.valueOf(i2)) + " AD";
            string = string.replace("{y_G}", string2);
        }
        if (string.contains("{yyyy}")) {
            string2 = String.valueOf(i2);
            while (string2.length() < 4) {
                string2 = i2 < 0 ? "-0" + string2.substring(1) : "0" + string2;
            }
            string = string.replace("{yyyy}", string2);
        }
        if (string.contains("{yy}")) {
            string2 = String.valueOf(i2);
            while (string2.length() < 2) {
                string2 = i2 < 0 ? "-0" + string2.substring(1) : "0" + string2;
            }
            string = string.replace("{yy}", string2);
        }
        if ((string = string.replace("y}", String.valueOf(i2))).contains("{MM}")) {
            string2 = String.valueOf(i3);
            while (string2.length() < 2) {
                string2 = "0" + string2;
            }
            string = string.replace("{MM}", string2);
        }
        if ((string = string.replace("{M}", String.valueOf(i3))).contains("{dd}")) {
            string2 = String.valueOf(i4);
            while (string2.length() < 2) {
                string2 = "0" + string2;
            }
            string = string.replace("{dd}", string2);
        }
        if ((string = string.replace("{d}", String.valueOf(i4))).contains("{hh}")) {
            string2 = String.valueOf(i5);
            while (string2.length() < 2) {
                string2 = "0" + string2;
            }
            string = string.replace("{hh}", string2);
        }
        if ((string = string.replace("{h}", String.valueOf(i5))).contains("{mm}")) {
            string2 = String.valueOf(i6);
            while (string2.length() < 2) {
                string2 = "0" + string2;
            }
            string = string.replace("{mm}", string2);
        }
        if ((string = string.replace("{m}", String.valueOf(i6))).contains("{ss}")) {
            string2 = String.valueOf(i7);
            while (string2.length() < 2) {
                string2 = "0" + string2;
            }
            string = string.replace("{ss}", string2);
        }
        if ((string = string.replace("{s}", String.valueOf(i7))).contains("{SSS}")) {
            string2 = String.valueOf(i8);
            while (string2.length() < 3) {
                string2 = "0" + string2;
            }
            string = string.replace("{SSS}", string2);
        }
        string = string.replace("{S}", String.valueOf(i8));
        return string;
    }

    public String toString() {
        return this.format("{yyyy}-{MM}-{dd} {hh}:{mm}:{ss}");
    }

    /** A new {@link DateTime} sharing the same wrapped instant. */
    public DateTime copy() {
        return new DateTime(this.timeMillis);
    }

    public boolean equals(Object object) {
        if (object != null) {
            if (object instanceof DateTime) {
                return this.timeMillis == ((DateTime)object).timeMillis;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return (int)(this.timeMillis ^ this.timeMillis >>> 32);
    }

    public int compareTo(Object object) {
        return new Long(this.timeMillis).compareTo(new Long(((DateTime)object).timeMillis));
    }

    public /* synthetic */ Object clone() {
        return this.copy();
    }
}


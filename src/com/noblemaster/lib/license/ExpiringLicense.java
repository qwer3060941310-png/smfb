/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.license;

import com.noblemaster.lib.data.DateTime;
import com.noblemaster.lib.license.License;
import com.noblemaster.lib.license.LicenseCallback;

public class ExpiringLicense
implements License {
    private boolean a;

    public ExpiringLicense(DateTime dateTime, int i2) {
        DateTime dateTime2 = new DateTime();
        DateTime dateTime3 = new DateTime(dateTime.getMillis());
        dateTime3.addDays(i2);
        this.a = dateTime2.isAfter(dateTime3);
    }

    @Override
    public String getName() {
        return "Expiring";
    }

    @Override
    public boolean isValid() {
        return !this.a;
    }

    @Override
    public boolean isMacBuild() {
        return false;
    }

    @Override
    public boolean d() {
        return false;
    }

    @Override
    public void check(LicenseCallback licenseCallback) {
        throw new RuntimeException("N/A!");
    }

    @Override
    public void dispose() {
    }
}


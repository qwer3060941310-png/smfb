/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.license;

import com.noblemaster.lib.license.License;
import com.noblemaster.lib.license.LicenseCallback;

public class AcceptedLicense
implements License {
    @Override
    public String getName() {
        return "Accept";
    }

    @Override
    public boolean isValid() {
        return true;
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


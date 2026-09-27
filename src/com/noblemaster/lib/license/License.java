/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.license;

import com.noblemaster.lib.license.LicenseCallback;

public interface License {
    public String getName();

    public boolean isValid();

    public boolean isMacBuild();

    public boolean d();

    public void check(LicenseCallback var1);

    public void dispose();
}


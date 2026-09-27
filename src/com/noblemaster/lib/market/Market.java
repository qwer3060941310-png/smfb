/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.market;

public abstract class Market {
    public abstract String getName();

    public abstract String getUrl();

    public boolean hasUrl() {
        return this.getUrl() != null;
    }
}


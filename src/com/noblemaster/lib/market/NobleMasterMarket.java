/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.market;

import com.noblemaster.lib.market.Market;

public class NobleMasterMarket
extends Market {
    private String a;

    public NobleMasterMarket(String string) {
        this.a = string;
    }

    @Override
    public String getName() {
        return "NobleMaster";
    }

    @Override
    public String getUrl() {
        return this.a;
    }
}


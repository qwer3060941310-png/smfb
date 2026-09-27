/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.market;

import com.noblemaster.lib.market.Market;

public class AmazonMarket
extends Market {
    private String a = "http://www.amazon.com";

    @Override
    public String getName() {
        return "Amazon";
    }

    @Override
    public String getUrl() {
        return this.a;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.market;

import com.noblemaster.lib.market.Market;

public class ITunesMarket
extends Market {
    @Override
    public String getName() {
        return "iTunes";
    }

    @Override
    public String getUrl() {
        return "http://itunes.apple.com";
    }
}


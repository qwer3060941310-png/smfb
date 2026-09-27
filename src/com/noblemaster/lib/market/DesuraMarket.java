/*
 * Decompiled with CFR 0.152.
 */
package com.noblemaster.lib.market;

import com.noblemaster.lib.market.Market;

public class DesuraMarket
extends Market {
    private String a;

    public DesuraMarket(String string) {
        this.a = string;
    }

    @Override
    public String getName() {
        return "Desura";
    }

    @Override
    public String getUrl() {
        return "http://www.desura.com/games/" + this.a.toLowerCase().replace(" ", "-");
    }
}


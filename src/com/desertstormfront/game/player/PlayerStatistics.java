/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

public strictfp final class PlayerStatistics {
    private int destroyedUnits;
    private int lostUnits;
    private int damageInflicted;
    private int damageReceived;
    private int basesCaptured;
    private int basesLost;

    public final void setLostUnits(int i1) {
        this.lostUnits = i1;
    }

    public final int getLostUnits() {
        return this.lostUnits;
    }

    public final int getDestroyedUnits() {
        return this.destroyedUnits;
    }

    public final void setDestroyedUnits(int i1) {
        this.destroyedUnits = i1;
    }

    public final int getUnitsRating() {
        if (this.lostUnits == 0) {
            return 3;
        }
        int i1 = this.destroyedUnits * 3 / (this.lostUnits * 2);
        if (i1 > 3) {
            i1 = 3;
        }
        return i1;
    }

    public final int getDamageInflicted() {
        return this.damageInflicted;
    }

    public final void setDamageInflicted(int i1) {
        this.damageInflicted = i1;
    }

    public final int getDamageReceived() {
        return this.damageReceived;
    }

    public final void setDamageReceived(int i1) {
        this.damageReceived = i1;
    }

    public final int getDamageRating() {
        if (this.damageReceived == 0) {
            return 3;
        }
        int i1 = this.damageInflicted * 3 / (this.damageReceived * 2);
        if (i1 > 3) {
            i1 = 3;
        }
        return i1;
    }

    public final int getBasesCaptured() {
        return this.basesCaptured;
    }

    public final void setBasesCaptured(int i1) {
        this.basesCaptured = i1;
    }

    public final int getBasesLost() {
        return this.basesLost;
    }

    public final void setBasesLost(int i1) {
        this.basesLost = i1;
    }

    public final int getBasesRating() {
        if (this.basesLost == 0) {
            return 3;
        }
        int i1 = this.basesCaptured * 3 / (this.basesLost * 2);
        if (i1 > 3) {
            i1 = 3;
        }
        return i1;
    }
}


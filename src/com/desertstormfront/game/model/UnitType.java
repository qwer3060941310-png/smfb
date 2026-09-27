/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.AmmoType;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Layer;
import com.desertstormfront.game.model.UnitTypeList;

/**
 * One unit type: the immutable template every unit of that kind is built from.
 *
 * <p>Each ruleset ({@code TsfRuleset}, {@code DsfRuleset}) builds a complete set of these in its
 * constructor, and mods add or override entries through {@code ModLoader}, which fills the fields
 * reflectively from JSON. Because that path is reflective, renaming a field here is a visible
 * change for mod files: the legacy single-letter JSON keys keep working through the alias table in
 * {@code ModLoader}, and both spellings are pinned by {@code ModFieldAliasTest}.
 *
 * <p>Combat numbers live in a damage matrix rather than in per-pair objects: for two types X and Y
 * the matrix holds what Y deals to X. {@link #getDamageAgainst} reads what this type deals to
 * another, while {@link #isStrongAgainst} and {@link #isWeakAgainst} compare both directions.
 */
public strictfp class UnitType {
    private UnitTypeList allUnitTypes;
    private int[][] damageMatrix;
    private int id;
    private String name;
    private String key;
    private char code;
    private long health;
    private boolean building;
    private Layer layer;
    private Domain domain;
    private float size;
    private float moveFactor;
    private float speed;
    private float flyHeight;
    private float verticalOffset;
    private boolean p;
    private boolean requiresFacingTarget;
    private AmmoType ammo;
    private float damage;
    private float range;
    private int salvo;
    private float reloadTime;
    private boolean isGeneral;
    private boolean canCapture;
    private boolean capturable;
    private boolean canRepair;
    private boolean A;
    private int B;
    private int rangeInTiles;
    private float sightRange;
    private UnitType producer;
    private UnitTypeList producedBy;
    private UnitTypeList canProduce;
    private int capacity;
    private boolean canCarryAircraft;
    private int sortWeight;

    public UnitType(int i1, String string, String string2, char c, long l5, boolean bl, Layer layer, Domain domain, float f10, float f11, float f12, float f13, boolean bl2, boolean bl3, AmmoType ammoType, float f17, float f18, int i19, float f20, boolean bl4, boolean bl5, boolean bl6, boolean bl7, boolean bl8, int i26, int i27, float f28, UnitTypeList unitTypeList, UnitTypeList unitTypeList2, int i31, boolean bl9) {
        this.id = i1;
        this.name = string;
        this.key = string2;
        this.code = c;
        this.health = l5;
        this.building = bl;
        this.layer = layer;
        this.domain = domain;
        this.speed = f12;
        this.size = f10 * 0.35f;
        this.moveFactor = f11;
        this.flyHeight = f13;
        this.p = bl2;
        this.requiresFacingTarget = bl3;
        this.ammo = ammoType;
        this.damage = f17;
        this.range = f18;
        this.salvo = i19;
        this.reloadTime = f20;
        this.isGeneral = bl4;
        this.canCapture = bl5;
        this.capturable = bl6;
        this.canRepair = bl7;
        this.A = bl8;
        this.B = i26;
        this.rangeInTiles = i27;
        this.sightRange = f28;
        this.producedBy = unitTypeList;
        this.canProduce = unitTypeList2;
        this.capacity = i31;
        this.canCarryAircraft = bl9;
        this.speed = f12;
    }

    public void initialize(UnitTypeList unitTypeList, int[][] nArray) {
        this.allUnitTypes = unitTypeList;
        this.damageMatrix = nArray;
        this.producer = null;
        int n = 0;
        while (n < unitTypeList.size()) {
            UnitType unitType = (UnitType)unitTypeList.get(n);
            if (unitType.getProducedBy().contains(this)) {
                this.producer = unitType;
                break;
            }
            ++n;
        }
        if (this.isFlying()) {
            float f = 0.0f;
            int n2 = 0;
            while (n2 < unitTypeList.size()) {
                UnitType unitType = (UnitType)unitTypeList.get(n2);
                if (unitType.getCanProduce().contains(this) && unitType.getSpeed() > f) {
                    f = unitType.getSpeed();
                }
                ++n2;
            }
            this.verticalOffset = (this.speed + f) / (2.0f * this.speed) * this.flyHeight + 0.04f;
        } else {
            this.verticalOffset = 0.0f;
        }
        this.sortWeight = 0;
        if (this.capacity > 0) {
            this.sortWeight += this.canProduce.hasCapturingUnit() ? 200000 : 0;
            this.sortWeight += 20000 * this.capacity;
        }
        if (this.isGeneral) {
            this.sortWeight += 15000;
        }
        if (this.A) {
            this.sortWeight += 14000;
        }
        if (this.canRepair) {
            this.sortWeight += 13000;
        }
        switch (this.domain) {
            case Water: {
                this.sortWeight += 10000;
                break;
            }
            case Ground: {
                this.sortWeight += 5000;
            }
        }
        this.sortWeight = (int)((long)this.sortWeight + this.health);
    }

    public final int getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final String getKey() {
        return this.key;
    }

    public final char getCode() {
        return this.code;
    }

    public final long getHealth() {
        return this.health;
    }

    public final boolean isBuilding() {
        return this.building;
    }

    public static final int getSlotLimit() {
        return 24;
    }

    public final int getHealthInt() {
        return (int)this.health;
    }

    public final Layer getLayer() {
        return this.layer;
    }

    public final Domain getDomain() {
        return this.domain;
    }

    public final boolean isImmobile() {
        return this.speed == 0.0f;
    }

    public final float getSpeed() {
        return this.speed;
    }

    public final float getSize() {
        return this.size;
    }

    public final float getMoveFactor() {
        return this.moveFactor;
    }

    public final boolean isFlying() {
        return this.flyHeight != 0.0f;
    }

    public final float getAltitude() {
        return this.flyHeight;
    }

    public final float getVerticalOffset() {
        return this.verticalOffset;
    }

    public final boolean r() {
        return this.p;
    }

    public final boolean requiresFacingTarget() {
        return this.requiresFacingTarget;
    }

    public final AmmoType getAmmoType() {
        return this.ammo;
    }

    public final float getDamage() {
        return this.damage;
    }

    public final float getRange() {
        return this.range;
    }

    public final boolean hasSalvo() {
        return this.salvo != 0;
    }

    public final int getSalvo() {
        return this.salvo;
    }

    public final float getReloadTime() {
        return this.reloadTime;
    }

    public final int getHealthScale() {
        return 1000;
    }

    public final boolean isGeneral() {
        return this.isGeneral;
    }

    public final boolean canCapture() {
        return this.canCapture;
    }

    public final boolean isCapturable() {
        return this.capturable;
    }

    public final boolean canRepair() {
        return this.canRepair;
    }

    public final boolean isTruck() {
        return this.A;
    }

    public final int getVisionRadiusInTiles() {
        return this.B;
    }

    public final int getRangeInTiles() {
        return this.rangeInTiles;
    }

    public final float getSightRange() {
        return this.sightRange;
    }

    public final int getDamageAgainst(Domain domain, Layer layer) {
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i5 < this.allUnitTypes.size()) {
            UnitType unitType = (UnitType)this.allUnitTypes.get(i5);
            if (unitType.getDomain() == domain && unitType.getLayer() == layer) {
                i3 += this.damageMatrix[unitType.getId()][this.getId()];
                ++i4;
            }
            ++i5;
        }
        return i3 / i4;
    }

    public final int getDamageFrom(Domain domain, Layer layer) {
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i5 < this.allUnitTypes.size()) {
            UnitType unitType = (UnitType)this.allUnitTypes.get(i5);
            if (unitType.getDomain() == domain && unitType.getLayer() == layer) {
                i3 += this.damageMatrix[this.getId()][unitType.getId()];
                ++i4;
            }
            ++i5;
        }
        return i3 / i4;
    }

    public final boolean isStrongAgainst(UnitType unitType) {
        return this.damageMatrix[unitType.getId()][this.getId()] - this.damageMatrix[this.getId()][unitType.getId()] >= 200;
    }

    public final boolean isWeakAgainst(UnitType unitType) {
        return this.damageMatrix[this.getId()][unitType.getId()] - this.damageMatrix[unitType.getId()][this.getId()] >= 200;
    }

    public final int getDamageAgainst(UnitType unitType) {
        return this.damageMatrix[unitType.getId()][this.getId()];
    }

    public final UnitType getProducer() {
        return this.producer;
    }

    public final UnitTypeList getProducedBy() {
        return this.producedBy;
    }

    public final boolean canCarry() {
        return this.capacity > 0;
    }

    public final UnitTypeList getCanProduce() {
        return this.canProduce;
    }

    public final int getCapacity() {
        return this.capacity;
    }

    public final boolean canCarryAircraft() {
        return this.canCarryAircraft;
    }

    public final int getSortWeight() {
        return this.sortWeight;
    }

    public final String toString() {
        return this.name;
    }
}


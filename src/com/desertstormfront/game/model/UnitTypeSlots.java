/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.UnitType;

/**
 * The 21 unit slots a ruleset fills, so the game code can refer to "the transport" without knowing
 * which concrete {@link UnitType} the active campaign uses.
 *
 * <p>The order is fixed by the constructor and is part of the ruleset format: {@code DsfRuleset}
 * passes general, humvee, battleTank, artillery, missileTank, fourByFour, baseStation, shipyard,
 * airfield, oilField, transport, carrier, chinook, fighterPlane, helicopter, cruiser, submarine,
 * gunboat, mechanic, hovercraft, truck; {@code TsfRuleset} fills two slots differently (battleTank
 * where Desert Stormfront has the humvee, destroyer where it has the gunboat) and leaves the slots
 * it does not use null. Slot names here follow Desert Stormfront, the primary ruleset.
 *
 * <p>A mod supplies the same 21 entries as a JSON array, matched by position, never by name - see
 * {@code ModRuleset.readSlots} - so the field names are internal and free to change.
 */
public strictfp final class UnitTypeSlots {
    private UnitType general;
    private UnitType humvee;
    private UnitType battleTank;
    private UnitType artillery;
    private UnitType missileTank;
    private UnitType fourByFour;
    private UnitType baseStation;
    private UnitType shipyard;
    private UnitType airfield;
    private UnitType oilField;
    private UnitType transport;
    private UnitType carrier;
    private UnitType airTransport;
    private UnitType fighterPlane;
    private UnitType helicopter;
    private UnitType cruiser;
    private UnitType submarine;
    private UnitType gunboat;
    private UnitType mechanic;
    private UnitType hovercraft;
    private UnitType truck;

    public UnitTypeSlots(UnitType unitType, UnitType unitType2, UnitType unitType3, UnitType unitType4, UnitType unitType5, UnitType unitType6, UnitType unitType7, UnitType unitType8, UnitType unitType9, UnitType unitType10, UnitType unitType11, UnitType unitType12, UnitType unitType13, UnitType unitType14, UnitType unitType15, UnitType unitType16, UnitType unitType17, UnitType unitType18, UnitType unitType19, UnitType unitType20, UnitType unitType21) {
        this.general = unitType;
        this.humvee = unitType2;
        this.battleTank = unitType3;
        this.artillery = unitType4;
        this.missileTank = unitType5;
        this.fourByFour = unitType6;
        this.baseStation = unitType7;
        this.shipyard = unitType8;
        this.airfield = unitType9;
        this.oilField = unitType10;
        this.transport = unitType11;
        this.carrier = unitType12;
        this.airTransport = unitType13;
        this.fighterPlane = unitType14;
        this.helicopter = unitType15;
        this.cruiser = unitType16;
        this.submarine = unitType17;
        this.gunboat = unitType18;
        this.mechanic = unitType19;
        this.hovercraft = unitType20;
        this.truck = unitType21;
    }

    public UnitType getGeneral() {
        return this.general;
    }

    /**
     * The light ground vehicle - the unit the base station produces, which survival and
     * capture-the-flag modes track. {@code TsfRuleset} puts its battle tank in this slot.
     */
    public UnitType getHumvee() {
        return this.humvee;
    }

    public UnitType getBattleTank() {
        return this.battleTank;
    }

    public UnitType getArtillery() {
        return this.artillery;
    }

    public UnitType getMissileTank() {
        return this.missileTank;
    }

    /** The four wheel drive scout vehicle; unused by {@code TsfRuleset}. */
    public UnitType getFourByFour() {
        return this.fourByFour;
    }

    public UnitType getBaseStation() {
        return this.baseStation;
    }

    public UnitType getShipyard() {
        return this.shipyard;
    }

    public UnitType getAirfield() {
        return this.airfield;
    }

    /** The capturable oil field structure; unused by {@code TsfRuleset}. */
    public UnitType getOilField() {
        return this.oilField;
    }

    public UnitType getTransport() {
        return this.transport;
    }

    public UnitType getCarrier() {
        return this.carrier;
    }

    public UnitType getAirTransport() {
        return this.airTransport;
    }

    public UnitType getFighterPlane() {
        return this.fighterPlane;
    }

    public UnitType getHelicopter() {
        return this.helicopter;
    }

    public UnitType getCruiser() {
        return this.cruiser;
    }

    public UnitType getSubmarine() {
        return this.submarine;
    }

    /**
     * The light surface warship - the slot {@code AssignDestroyerPlan} builds its group from.
     * {@code TsfRuleset} puts its destroyer here, Desert Stormfront its gunboat.
     */
    public UnitType getGunboat() {
        return this.gunboat;
    }

    /** The repair unit; unused by {@code TsfRuleset}. */
    public UnitType getMechanic() {
        return this.mechanic;
    }

    /** The supply truck, counted against {@code IntentType.Truck}. */
    public UnitType getTruck() {
        return this.truck;
    }
}


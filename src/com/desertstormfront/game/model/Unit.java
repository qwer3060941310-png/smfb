/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.AmmoType;
import com.desertstormfront.game.model.Direction;
import com.desertstormfront.game.model.EffectTimer;
import com.desertstormfront.game.model.Layer;
import com.desertstormfront.game.model.PositionedSortable;
import com.desertstormfront.game.model.Sortable;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitOrderMode;
import com.desertstormfront.game.model.UnitSortProxy;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.Volley;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.UnitGroup;
import com.desertstormfront.world.UnitPosition;
import com.noblemaster.lib.math.MathHelper;

/**
 * A single unit or structure on the map: one instance of a {@link UnitType}, owned by a
 * {@link Player}, placed at a position and facing an angle.
 *
 * <p>Behaviour is expressed as data rather than as subclasses. A unit carries an order mode
 * (idle, move, attack-move, guard, ...), optionally a guard or rally target - either another unit
 * or a fixed position - and optionally a move target. The {@code canXxx} predicates answer what is
 * legal at this moment given fog of war, ownership and capacity: {@link #canAttackUnit},
 * {@link #canCaptureUnit}, {@link #canGuard}, {@link #canEmbark} and {@link #canProduceOrCarry}.
 * Their union is {@link #canInteractWith}, which is what the UI consults to decide what a click
 * on another unit means.
 *
 * <p>State that changes during play: health, production count and progress, the volley currently
 * in flight, the effect timer, and the time from which the unit may act again. Transports and
 * carriers hold the units they carry in the sub-unit list, whose size is bounded by the capacity
 * of their type.
 */
public strictfp final class Unit
implements Sortable {
    private int id;
    private UnitType unitType;
    private Player owner;
    private UnitPosition position;
    private UnitOrderMode orderMode;
    private UnitPosition guardPosition;
    private boolean guarding;
    private UnitPosition rallyPosition;
    private boolean hasRally;
    private float angle;
    private Direction direction;
    private float l;
    private int count;
    private float progress;
    private int health;
    private int salvoCount;
    private int q;
    private int r;
    private UnitList subUnits;
    private boolean t;
    private boolean hasMoveTarget;
    private UnitPosition moveTargetPosition;
    private int w;
    private final PositionedSortable sortProxy = new UnitSortProxy(this);
    private UnitList guardFollowers;
    private UnitGroup unitGroup;
    private Volley volley;
    private boolean volleyActive;
    private float readyTime;
    private EffectTimer effectTimer;
    private boolean effectActive;

    static final Unit create(int i0, UnitType unitType, Player player) {
        Unit unit = new Unit();
        unit.setId(i0);
        unit.setUnitType(unitType);
        unit.setOwner(player);
        unit.position = new UnitPosition();
        unit.guardPosition = new UnitPosition();
        unit.guarding = false;
        unit.rallyPosition = new UnitPosition();
        unit.hasRally = false;
        unit.moveTargetPosition = new UnitPosition();
        unit.hasMoveTarget = false;
        unit.w = 0;
        unit.setAngle(0.0f);
        unit.setOrderMode(UnitOrderMode.a);
        unit.setCount(0);
        unit.setProgress(unitType.getAltitude());
        unit.setHealth(unitType.isImmobile() && player == null ? 0 : unitType.getHealthScale());
        unit.setSalvoCount(unitType.getSalvo());
        unit.setKillCount(0);
        unit.setRank(0);
        unit.c(new UnitList(8));
        unit.setHostedAuto(false);
        unit.d(new UnitList(8));
        unit.setUnitGroup((UnitGroup)null);
        unit.b(Volley.create());
        unit.setReadyTime(0.0f);
        unit.resetEffectTimer(EffectTimer.create());
        return unit;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i1) {
        this.id = i1;
    }

    public final UnitType getUnitType() {
        return this.unitType;
    }

    public final void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public final boolean isHighValue() {
        if (this.unitType.isGeneral()) {
            return true;
        }
        int i1 = 0;
        while (i1 < this.subUnits.size()) {
            if (((Unit)this.subUnits.get(i1)).isHighValue()) {
                return true;
            }
            ++i1;
        }
        return false;
    }

    @Override
    public final boolean isSite() {
        return false;
    }

    public final boolean isImmobile() {
        return this.unitType.isImmobile();
    }

    @Override
    public final boolean isMoveTargetMarker() {
        return false;
    }

    @Override
    public final float getSortValue() {
        return this.position.getX() + this.position.getY();
    }

    public final Player getOwner() {
        return this.owner;
    }

    public final void setOwner(Player player) {
        this.owner = player;
    }

    public final float getAngle() {
        return this.angle;
    }

    public final void setAngle(float f1) {
        this.angle = f1;
        this.direction = Direction.values()[MathHelper.round((float)((double)(f1 * 4.0f) / Math.PI)) + 8 & 7];
    }

    public final Direction getDirection() {
        return this.direction;
    }

    public final UnitPosition getPosition() {
        return this.position;
    }

    public final void dockInto(Unit unit) {
        if (this.position.getUnit() != null) {
            this.position.getUnit().getSubUnits().remove(this);
        }
        unit.getSubUnits().add(this);
        this.position.bindTo(unit);
        if (this.unitType.isFlying()) {
            this.setProgress(0.01f);
        }
        while (this.guardFollowers.size() > 0) {
            ((Unit)this.guardFollowers.get(0)).guardUnit(unit);
        }
    }

    public final void setPosition(float f1, float f2) {
        if (this.position.getUnit() != null) {
            this.position.getUnit().getSubUnits().remove(this);
        }
        this.position.setX(f1);
        this.position.setY(f2);
    }

    public final boolean isGuarding() {
        return this.guarding;
    }

    public final UnitPosition getGuardPositionOrNull() {
        if (this.guarding) {
            return this.guardPosition;
        }
        return null;
    }

    public final UnitPosition getRallyPositionOrNull() {
        if (this.hasRally) {
            return this.rallyPosition;
        }
        return null;
    }

    public final void guardUnit(Unit unit) {
        this.stopGuarding();
        this.guarding = true;
        this.guardPosition.bindTo(unit);
        if (this.canGuard(unit)) {
            if (unit.getGuardPositionOrNull() != null && unit.getGuardPositionOrNull().getUnit() != null && unit.canGuard(unit.getGuardPositionOrNull().getUnit())) {
                if (this == unit.getGuardPositionOrNull().getUnit()) {
                    unit.stopGuarding();
                    while (this.guardFollowers.size() > 0) {
                        ((Unit)this.guardFollowers.get(0)).guardUnit(unit);
                    }
                    unit.getGuardFollowers().add(this);
                } else {
                    this.guardUnit(unit.getGuardPositionOrNull().getUnit());
                }
            } else {
                unit.getGuardFollowers().add(this);
                while (this.guardFollowers.size() > 0) {
                    ((Unit)this.guardFollowers.get(0)).guardUnit(unit);
                }
            }
        }
    }

    public final void guardPosition(float f1, float f2) {
        this.stopGuarding();
        this.guarding = true;
        this.guardPosition.setX(f1);
        this.guardPosition.setY(f2);
    }

    public final void setRallyToUnit(Unit unit) {
        this.hasRally = true;
        this.rallyPosition.bindTo(unit);
    }

    public final void setRallyToPosition(float f1, float f2) {
        this.hasRally = true;
        this.rallyPosition.setX(f1);
        this.rallyPosition.setY(f2);
    }

    public final void executeOrder() {
        if (this.orderMode == UnitOrderMode.b) {
            Unit unit = this.rallyPosition.getUnit();
            float f2 = this.rallyPosition.getX();
            float f3 = this.rallyPosition.getY();
            if (this.guardPosition.getUnit() != null) {
                this.setRallyToUnit(this.guardPosition.getUnit());
            } else {
                this.setRallyToPosition(this.guardPosition.getX(), this.guardPosition.getY());
            }
            if (unit != null) {
                this.guardUnit(unit);
            } else {
                this.guardPosition(f2, f3);
            }
        } else if (this.hasRally) {
            if (this.rallyPosition.getUnit() != null) {
                this.guardUnit(this.rallyPosition.getUnit());
            } else {
                this.guardPosition(this.rallyPosition.getX(), this.rallyPosition.getY());
            }
            this.clearRally();
        } else {
            this.stopGuarding();
        }
    }

    public final void stopGuarding() {
        Unit unit;
        if (this.guarding && this.guardPosition.getUnit() != null && this.canGuard(unit = this.guardPosition.getUnit())) {
            unit.getGuardFollowers().remove(this);
        }
        this.guarding = false;
        this.guardPosition.setX(0.0f);
        this.guardPosition.setY(0.0f);
    }

    public final void clearRally() {
        this.hasRally = false;
        this.rallyPosition.setX(0.0f);
        this.rallyPosition.setY(0.0f);
    }

    public final boolean hasFriendlyGuardTarget() {
        return this.guardPosition != null && this.guardPosition.getUnit() != null && this.canGuard(this.guardPosition.getUnit()) && !this.guardPosition.getUnit().canEmbark(this);
    }

    public final boolean hasAttackableGuardTarget() {
        return this.guardPosition != null && this.guardPosition.getUnit() != null && this.canAttackUnit(this.guardPosition.getUnit());
    }

    public final boolean hasCapturableGuardTarget() {
        return this.guardPosition != null && this.guardPosition.getUnit() != null && this.canCaptureUnit(this.guardPosition.getUnit());
    }

    public UnitOrderMode getOrderMode() {
        return this.orderMode;
    }

    public void setOrderMode(UnitOrderMode unitOrderMode) {
        this.orderMode = unitOrderMode;
    }

    public final float getMoveProgress() {
        return this.l;
    }

    public final void setMoveProgress(float f1) {
        this.l = f1;
    }

    public final boolean isCountZero() {
        return this.count == 0;
    }

    public final boolean isCountFull() {
        return this.count == this.unitType.getHealthInt();
    }

    public final int getCount() {
        return this.count;
    }

    public final void setCount(int i1) {
        this.count = i1;
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f1) {
        this.progress = f1;
    }

    public final int getHealth() {
        return this.health;
    }

    public final void setHealth(int i1) {
        this.health = i1;
    }

    public final boolean isDeployed() {
        return this.position.getUnit() == null || this.health == 1000 && (!this.unitType.isFlying() || this.progress == this.unitType.getAltitude());
    }

    public final boolean isDestroyed() {
        return this.health == 0 && !this.unitType.isCapturable();
    }

    public final int getSalvoCount() {
        return this.salvoCount;
    }

    public final void setSalvoCount(int i1) {
        this.salvoCount = i1;
    }

    public int getKillCount() {
        return this.q;
    }

    public void setKillCount(int i1) {
        this.q = i1;
    }

    public int getRank() {
        return this.r;
    }

    public void setRank(int i1) {
        this.r = i1;
    }

    public final boolean canInteractWith(Unit unit) {
        return unit.canEmbark(this) || this.canGuard(unit) || this.canCaptureUnit(unit) || this.canAttackUnit(unit);
    }

    public final boolean canEmbark(Unit unit) {
        return this.canProduceOrCarry(unit) && this.subUnits.size() < this.unitType.getCapacity();
    }

    public final boolean canProduceOrCarry(Unit unit) {
        return !this.isDestroyed() && (this.subUnits.contains(unit) || this.owner == unit.getOwner() && this.unitType.getCanProduce().contains(unit.getUnitType()) && this.getPosition().getUnit() == null);
    }

    public final boolean canGuard(Unit unit) {
        return this.owner == unit.getOwner() && this.unitType.getDomain() == unit.getUnitType().getDomain() && !this.unitType.isFlying() && !unit.getUnitType().isFlying() && !unit.isImmobile();
    }

    public final boolean canCaptureUnit(Unit unit) {
        return this.owner != unit.getOwner() && this.unitType.canCapture() && unit.getUnitType().isCapturable();
    }

    public final boolean canAttackUnit(Unit unit) {
        return this.owner != unit.getOwner() && unit.getOwner() != null && !unit.isDestroyed() && (this.owner.getTeam() == null || this.owner.getTeam() != unit.getOwner().getTeam()) && this.owner.getFogOfWar().isUnitVisible(unit) && unit.getPosition().getUnit() == null && this.unitType.getDamageAgainst(unit.getUnitType()) > 0;
    }

    public final UnitList getSubUnits() {
        return this.subUnits;
    }

    private final void c(UnitList unitList) {
        this.subUnits = unitList;
    }

    public boolean isHostedAuto() {
        return this.t;
    }

    public void setHostedAuto(boolean bl) {
        this.t = bl;
    }

    public boolean hasMoveTarget() {
        return this.hasMoveTarget;
    }

    public UnitPosition getMoveTargetPosition() {
        return this.moveTargetPosition;
    }

    public void setMoveTargetPosition(float f1, float f2) {
        this.moveTargetPosition.setX(f1);
        this.moveTargetPosition.setY(f2);
        this.hasMoveTarget = true;
    }

    public void clearMoveTarget() {
        this.hasMoveTarget = false;
    }

    public int getRallySpreadIndex() {
        return this.w;
    }

    public void setRallySpreadIndex(int i1) {
        this.w = i1;
    }

    public Sortable getSortProxy() {
        return this.sortProxy;
    }

    public final boolean isGuardingActive() {
        return this.guardPosition != null && this.guardPosition.getUnit() != null && this.canGuard(this.guardPosition.getUnit());
    }

    public final UnitList getGuardFollowers() {
        return this.guardFollowers;
    }

    private final void d(UnitList unitList) {
        this.guardFollowers = unitList;
    }

    public UnitGroup getUnitGroup() {
        return this.unitGroup;
    }

    public void setUnitGroup(UnitGroup unitGroup) {
        this.unitGroup = unitGroup;
    }

    public final Volley getActiveVolley() {
        return this.volleyActive ? this.volley : null;
    }

    private final void b(Volley volley) {
        this.volley = volley;
        this.volleyActive = false;
    }

    public final boolean isVolleyActive() {
        return this.volleyActive;
    }

    public final void fireAt(Unit unit) {
        AmmoType ammoType = this.unitType.getAmmoType();
        if (unit.getUnitType().getLayer() == Layer.Under) {
            ammoType = AmmoType.Torpedo;
        }
        float f3 = this.direction.getAngle();
        float f4 = this.position.getX() + MathHelper.cos(f3) * this.unitType.getDamage();
        float f5 = this.position.getY() + MathHelper.sin(f3) * this.unitType.getDamage();
        float f6 = this.unitType.getRange();
        this.volley.setAmmoType(ammoType);
        this.volley.setTargetUnit(unit);
        this.volley.getOrigin().set(f4, f5, f6);
        this.volley.getDirection().set(MathHelper.cos(f3), MathHelper.sin(f3), 0.0f);
        this.volley.setIntensity(0.0f);
        this.volley.getProjectiles().clearAll();
        this.volleyActive = true;
    }

    public final void clearVolley() {
        this.volley.setTargetUnit((Unit)null);
        this.volleyActive = false;
    }

    public final float getReadyTime() {
        return this.readyTime;
    }

    public final void setReadyTime(float f1) {
        this.readyTime = f1;
    }

    public final EffectTimer getActiveEffectTimer() {
        return this.effectActive ? this.effectTimer : null;
    }

    private final void resetEffectTimer(EffectTimer effectTimer) {
        this.effectTimer = effectTimer;
        this.effectActive = false;
    }

    public final boolean isEffectActive() {
        return this.effectActive;
    }

    public final void activateEffectTimer() {
        this.effectTimer.setTime(0.0f);
        this.effectActive = true;
    }

    public final void deactivateEffectTimer() {
        this.effectActive = false;
    }

    public final String toString() {
        return this.unitType.toString();
    }

    public final UnitPosition Y() {
        return this.position;
    }

    public final void setPosition(UnitPosition unitPosition) {
        this.position = unitPosition;
    }

    public final boolean Z() {
        return this.guarding;
    }

    public final void setGuarding(boolean bl) {
        this.guarding = bl;
    }

    public final UnitPosition getGuardPosition() {
        return this.guardPosition;
    }

    public final void b(UnitPosition unitPosition) {
        this.guardPosition = unitPosition;
    }

    public final boolean hasRally() {
        return this.hasRally;
    }

    public final void setRally(boolean bl) {
        this.hasRally = bl;
    }

    public final UnitPosition getRallyPosition() {
        return this.rallyPosition;
    }

    public final void setRallyPosition(UnitPosition unitPosition) {
        this.rallyPosition = unitPosition;
    }

    public final UnitList ad() {
        return this.subUnits;
    }

    public final void setSubUnits(UnitList unitList) {
        this.subUnits = unitList;
    }

    public boolean ae() {
        return this.hasMoveTarget;
    }

    public void setMoveTargetActive(boolean bl) {
        this.hasMoveTarget = bl;
    }

    public UnitPosition af() {
        return this.moveTargetPosition;
    }

    public void setMoveTargetPosition(UnitPosition unitPosition) {
        this.moveTargetPosition = unitPosition;
    }

    public final UnitList ag() {
        return this.guardFollowers;
    }

    public final void setGuardFollowers(UnitList unitList) {
        this.guardFollowers = unitList;
    }

    public final Volley getVolley() {
        return this.volley;
    }

    public final void setVolley(Volley volley) {
        this.volley = volley;
    }

    public final boolean ai() {
        return this.volleyActive;
    }

    public final void setVolleyActive(boolean bl) {
        this.volleyActive = bl;
    }

    public final EffectTimer getEffectTimer() {
        return this.effectTimer;
    }

    public final void setEffectTimer(EffectTimer effectTimer) {
        this.effectTimer = effectTimer;
    }

    public final boolean ak() {
        return this.effectActive;
    }

    public final void setEffectActive(boolean bl) {
        this.effectActive = bl;
    }

    static /* synthetic */ UnitPosition k(Unit unit) {
        return unit.moveTargetPosition;
    }
}


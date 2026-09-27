/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.command;

import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.World;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitOrderMode;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.UnitGroup;
import com.desertstormfront.game.player.UnitGroupSet;
import com.desertstormfront.world.TerrainGrid;
import com.desertstormfront.world.UnitPosition;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.math.MathHelper;

/**
 * Issues orders to units on behalf of one {@link Player} in one {@link World}: the single entry
 * point through which intent - from the UI or from the AI - becomes a change of unit state.
 *
 * <p>The abstract surface is deliberately small and stated in game terms (move, attack, guard,
 * form a {@link UnitGroup}) so that callers do not care how an order travels. Two subclasses
 * provide that transport: {@code DirectUnitCommander} applies orders straight to the local world,
 * while {@code NetworkedUnitCommander} sends them off and lets the authoritative simulation echo
 * the result back. Every path first asks {@link #isCommandable} whether this player may order this
 * unit at all.
 *
 * <p>The {@code GameCommand} subclasses are the serialisable record of an order; this class is the
 * actor that validates and performs them.
 */
public strictfp abstract class UnitCommander {
    public abstract World getWorld();

    public abstract Player getPlayer();

    public abstract boolean isCommandable(Unit var1);

    public abstract boolean hasCommandableUnit(UnitList var1);

    public abstract boolean moveUnitTo(Unit var1, float var2, float var3);

    public abstract boolean moveUnitsTo(UnitList var1, float var2, float var3);

    public abstract boolean targetUnit(Unit var1, Unit var2);

    public abstract boolean targetUnits(UnitList var1, Unit var2);

    public abstract boolean b(Unit var1);

    public abstract boolean attackMoveUnitTo(Unit var1, float var2, float var3);

    public abstract boolean attackUnit(Unit var1, Unit var2);

    public abstract boolean canRepair(Unit var1);

    public abstract boolean hasRepairableUnit(UnitList var1);

    public abstract boolean repairUnit(Unit var1);

    public abstract boolean repairUnits(UnitList var1);

    public abstract boolean canSetAuto(Unit var1);

    public abstract boolean setAuto(Unit var1);

    public abstract boolean toggleHostedAuto(Unit var1);

    public abstract boolean toggleHostedAutoUnits(UnitList var1);

    public abstract boolean canStop(Unit var1);

    public abstract boolean canStopAny(UnitList var1);

    public abstract boolean stopUnit(Unit var1);

    public abstract boolean stopUnits(UnitList var1);

    public abstract boolean formSquad(UnitGroup var1, UnitList var2);

    public abstract boolean canAssignSquad(UnitList var1);

    public abstract boolean assignSquad(UnitList var1);

    public abstract boolean leaveSquad(Unit var1);

    public abstract boolean leaveSquadUnits(UnitList var1);

    public abstract boolean setRallyPoint(Unit var1, float var2, float var3);

    public abstract boolean clearRallyPoint(Unit var1);

    public abstract boolean canBuildUnit(Unit var1, UnitType var2);

    public abstract boolean buildUnit(Unit var1, UnitType var2);

    public abstract boolean cancelBuild(Unit var1);

    static final boolean isCommandable(World world, Player player, Unit unit) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (!unit.isCountZero() || !unit.isDeployed()) {
            return false;
        }
        if (unit.getUnitType().isFlying() && unit.getPosition().getUnit() == null) {
            return false;
        }
        return unit.getOwner() == player;
    }

    static final boolean hasCommandableUnit(World world, Player player, UnitList unitList) {
        int i3 = 0;
        while (i3 < unitList.size()) {
            if (UnitCommander.isCommandable(world, player, (Unit)unitList.get(i3))) {
                return true;
            }
            ++i3;
        }
        return false;
    }

    static final boolean canMoveUnitTo(World world, Player player, Unit unit, float f3, float f4) {
        return UnitCommander.moveUnitToInternal(world, player, unit, f3, f4, true);
    }

    static final boolean canAnyUnitMoveTo(World world, Player player, UnitList unitList, float f3, float f4) {
        if (unitList.size() == 0) {
            return false;
        }
        int i5 = 0;
        while (i5 < unitList.size()) {
            if (UnitCommander.moveUnitToInternal(world, player, (Unit)unitList.get(i5), f3, f4, true)) {
                return true;
            }
            ++i5;
        }
        return false;
    }

    static final boolean moveUnitTo(World world, Player player, Unit unit, float f3, float f4) {
        return UnitCommander.moveUnitToInternal(world, player, unit, f3, f4, false);
    }

    static final boolean moveUnitsTo(World world, Player player, UnitList unitList, float f3, float f4) {
        if (unitList.size() == 0) {
            return false;
        }
        boolean i5 = false;
        int i6 = 0;
        int i7 = 0;
        while (i7 < unitList.size()) {
            Unit unit = (Unit)unitList.get(i7);
            if (unit != null && !unit.isDestroyed()) {
                float f10;
                float f9;
                if (i6 == 0) {
                    f9 = f3;
                    f10 = f4;
                } else {
                    float f11 = 1.35f;
                    int i12 = 7;
                    int i13 = i6 - 1;
                    while (i13 >= i12) {
                        i13 -= i12;
                        i12 += 4;
                        f11 += 1.35f;
                    }
                    float f14 = (float)i13 / (float)i12 * ((float)Math.PI * 2);
                    f9 = f3 + MathHelper.cos(f14) * f11;
                    f10 = f4 + MathHelper.sin(f14) * f11;
                }
                if (UnitCommander.moveUnitToInternal(world, player, unit, f9, f10, true) && UnitCommander.moveUnitToInternal(world, player, unit, f9, f10, false)) {
                    ++i6;
                    i5 = true;
                }
            }
            ++i7;
        }
        return i5;
    }

    private static final boolean moveUnitToInternal(World world, Player player, Unit unit, float f3, float f4, boolean bl) {
        float f10;
        float f9;
        float f8;
        Object object;
        if (!UnitCommander.isCommandable(world, player, unit)) {
            if (GameConfig.isDebugEnabled() && !bl) {
                OsfLog.info("Cannot TARGET (check failed): " + unit);
            }
            return false;
        }
        TerrainGrid terrainGrid = world.getTerrainGrid();
        if (terrainGrid.isInsideGrid(f3, f4) && unit.getPosition().getUnit() != null && !terrainGrid.canDomainEnter(unit.getUnitType().getDomain(), (int)f3, (int)f4) && MathHelper.abs((int)unit.getPosition().getX() - (int)f3) <= 1 && MathHelper.abs((int)unit.getPosition().getY() - (int)f4) <= 1 && terrainGrid.getUnitAtTile((int)f3, (int)f4) != null && terrainGrid.getUnitAtTile((int)f3, (int)f4).canEmbark(unit) && terrainGrid.getUnitAtTile((int)f3, (int)f4) != null && unit.canAttackUnit(terrainGrid.getUnitAtTile((int)f3, (int)f4)) && terrainGrid.getUnitAtTile((int)f3, (int)f4) != null && unit.canCaptureUnit(terrainGrid.getUnitAtTile((int)f3, (int)f4))) {
            if (GameConfig.isDebugEnabled() && !bl) {
                OsfLog.info("Cannot target (neighbor field is blocked): " + unit + " -> (" + f3 + ", " + f4 + ")");
            }
            return false;
        }
        if (!terrainGrid.isInsideGrid(f3, f4) || !terrainGrid.hasPathToPosition(unit, f3, f4)) {
            object = unit.getUnitType().getDomain();
            f8 = unit.getPosition().getX();
            f9 = unit.getPosition().getY();
            f10 = f3;
            float f11 = f4;
            float f12 = f10 - f8;
            float f13 = f11 - f9;
            float f14 = MathHelper.sqrt(f12 * f12 + f13 * f13);
            float f15 = Float.MAX_VALUE;
            float f16 = f8;
            float f17 = f9;
            float f18 = f14 + 1.5f;
            while ((double)f18 >= 0.2) {
                float f20;
                float f19;
                if (MathHelper.abs(f18 - f14) < f15 && terrainGrid.isInsideGrid(f19 = f8 + f18 * f12 / f14, f20 = f9 + f18 * f13 / f14) && terrainGrid.canDomainEnter((Domain)((Object)object), (int)f19, (int)f20) && terrainGrid.hasPathToPosition(unit, f19, f20)) {
                    f16 = f19;
                    f17 = f20;
                    f15 = MathHelper.abs(f18 - f14);
                }
                f18 -= 0.2f;
            }
            if (unit.getPosition().getUnit() != null && f15 == Float.MAX_VALUE) {
                if (GameConfig.isDebugEnabled() && !bl) {
                    OsfLog.info("Cannot target (exit from host is blocked): " + unit + " -> (" + f3 + ", " + f4 + ")");
                }
                return false;
            }
            f3 = f16;
            f4 = f17;
        }
        if (terrainGrid.isCoastTile((int)f3, (int)f4)) {
            float[] wind = (float[])world.getWind();
            terrainGrid.clampPositionToDomain(wind, unit.getUnitType().getDomain(), f3, f4);
            f3 = wind[0];
            f4 = wind[1];
        }
        if (!unit.getUnitType().isFlying()) {
            if (!bl) {
                unit.setOrderMode(UnitOrderMode.a);
                unit.guardPosition(f3, f4);
                unit.clearRally();
            }
        } else {
            float f = unit.getPosition().distanceTo(f3, f4);
            f8 = unit.getPosition().getX();
            f9 = unit.getPosition().getY();
            f10 = unit.getUnitType().getAltitude() - unit.getUnitType().getVerticalOffset();
            if (f > f10) {
                f3 = f8 + (f3 - f8) * f10 / f;
                f4 = f9 + (f4 - f9) * f10 / f;
            }
            if (!bl) {
                unit.setOrderMode(UnitOrderMode.a);
                unit.guardPosition(f3, f4);
                if (unit.getPosition().getUnit() != null) {
                    unit.setRallyToUnit(unit.getPosition().getUnit());
                } else {
                    unit.setRallyToPosition(unit.getPosition().getX(), unit.getPosition().getY());
                }
            }
        }
        return true;
    }

    static final boolean canTargetUnit(World world, Player player, Unit unit, Unit unit2) {
        return UnitCommander.targetUnitInternal(world, player, unit, unit2, true);
    }

    static final boolean hasUnitThatCanTarget(World world, Player player, UnitList unitList, Unit unit) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (unitList.size() == 0) {
            return false;
        }
        int i4 = 0;
        while (i4 < unitList.size()) {
            Unit unit2 = (Unit)unitList.get(i4);
            if (unit2 != null && !unit2.isDestroyed()) {
                if (unit.canEmbark(unit2) && UnitCommander.targetUnitInternal(world, player, unit2, unit, true)) {
                    return true;
                }
                if (UnitCommander.moveUnitToInternal(world, player, unit2, unit.getPosition().getX(), unit.getPosition().getY(), true)) {
                    return true;
                }
            }
            ++i4;
        }
        return false;
    }

    static final boolean targetUnit(World world, Player player, Unit unit, Unit unit2) {
        return UnitCommander.targetUnitInternal(world, player, unit, unit2, false);
    }

    static final boolean targetUnits(World world, Player player, UnitList unitList, Unit unit) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (unitList.size() == 0) {
            return false;
        }
        boolean i4 = false;
        int i5 = 0;
        float f6 = unit.getPosition().getX();
        float f7 = unit.getPosition().getY();
        int i8 = 0;
        int i9 = 0;
        while (i9 < unitList.size()) {
            Unit unit2 = (Unit)unitList.get(i9);
            if (unit2 != null && !unit2.isDestroyed()) {
                if (unit.canEmbark(unit2) && UnitCommander.targetUnitInternal(world, player, unit2, unit, true) && UnitCommander.targetUnitInternal(world, player, unit2, unit, false)) {
                    i4 = true;
                } else if (i8 < 3 && UnitCommander.targetUnitInternal(world, player, unit2, unit, true) && UnitCommander.targetUnitInternal(world, player, unit2, unit, false)) {
                    ++i8;
                    if (i5 == 0) {
                        i5 = 1;
                    }
                    i4 = true;
                } else {
                    float f12;
                    float f11;
                    if (i5 == 0) {
                        f11 = f6;
                        f12 = f7;
                    } else {
                        float f13 = 1.35f;
                        int i14 = 7;
                        int i15 = i5 - 1;
                        while (i15 >= i14) {
                            i15 -= i14;
                            i14 += 4;
                            f13 += 1.35f;
                        }
                        float f16 = (float)i15 / (float)i14 * ((float)Math.PI * 2);
                        f11 = f6 + MathHelper.cos(f16) * f13;
                        f12 = f7 + MathHelper.sin(f16) * f13;
                    }
                    if (UnitCommander.moveUnitToInternal(world, player, unit2, f11, f12, true) && UnitCommander.moveUnitToInternal(world, player, unit2, f11, f12, false)) {
                        ++i5;
                        i4 = true;
                    }
                }
            }
            ++i9;
        }
        return i4;
    }

    private static final boolean targetUnitInternal(World world, Player player, Unit unit, Unit unit2, boolean bl) {
        int i7;
        int i6;
        if (!UnitCommander.isCommandable(world, player, unit)) {
            if (GameConfig.isDebugEnabled() && !bl) {
                OsfLog.info("Cannot TARGET (check failed): " + unit);
            }
            return false;
        }
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (unit2 == null || unit2.isDestroyed() || unit == unit2) {
            return false;
        }
        if (unit.getPosition().getUnit() == unit2) {
            if (GameConfig.isDebugEnabled() && !bl) {
                OsfLog.info("Cannot target (already there): " + unit);
            }
            return false;
        }
        TerrainGrid terrainGrid = world.getTerrainGrid();
        if (terrainGrid.isInsideGrid((float)(i6 = (int)unit2.getPosition().getX()), (float)(i7 = (int)unit2.getPosition().getY())) && unit.getPosition().getUnit() != null && !terrainGrid.canDomainEnter(unit.getUnitType().getDomain(), i6, i7) && MathHelper.abs((int)unit.getPosition().getX() - i6) <= 1 && MathHelper.abs((int)unit.getPosition().getY() - i7) <= 1 && terrainGrid.getUnitAtTile(i6, i7) != null && terrainGrid.getUnitAtTile(i6, i7).canEmbark(unit) && terrainGrid.getUnitAtTile(i6, i7) != null && unit.canAttackUnit(terrainGrid.getUnitAtTile(i6, i7)) && terrainGrid.getUnitAtTile(i6, i7) != null && unit.canCaptureUnit(terrainGrid.getUnitAtTile(i6, i7))) {
            if (GameConfig.isDebugEnabled() && !bl) {
                OsfLog.info("Cannot target (direct neighbor field blocked): " + unit + " -> (" + i6 + ", " + i7 + ")");
            }
            return false;
        }
        if (!unit.canInteractWith(unit2)) {
            if (GameConfig.isDebugEnabled() && !bl) {
                OsfLog.info("Cannot target (not a targetable target unit): " + unit + "(" + unit.getPosition().getX() + ", " + unit.getPosition().getY() + ")" + " -> " + unit2 + "(" + i6 + ", " + i7 + ")");
            }
            return false;
        }
        if (!terrainGrid.hasPathToUnit(unit, unit2) && unit2.isImmobile() && unit2.canEmbark(unit)) {
            if (GameConfig.isDebugEnabled() && !bl) {
                OsfLog.info("Cannot target (cannot reach target unit): " + unit + "(" + unit.getPosition().getX() + ", " + unit.getPosition().getY() + ")" + " -> " + unit2 + "(" + i6 + ", " + i7 + ")");
            }
            return false;
        }
        float f8 = unit.getPosition().distanceTo(unit2.getPosition());
        float f9 = unit.getUnitType().getAltitude();
        float f10 = unit.getUnitType().getVerticalOffset();
        if (!unit.getUnitType().isFlying() || f8 < f9 - f10 || unit2.canEmbark(unit) && f8 < f9) {
            if (!bl) {
                unit.setOrderMode(UnitOrderMode.a);
                unit.guardUnit(unit2);
                unit.clearRally();
            }
        } else {
            if (GameConfig.isDebugEnabled() && !bl) {
                OsfLog.info("Cannot target (limited fuel - target too far out): " + unit + " -> (" + i6 + ", " + i7 + ")");
            }
            return false;
        }
        return true;
    }

    static final boolean b(World world, Player player, Unit unit) {
        return UnitCommander.isCommandable(world, player, unit);
    }

    static final boolean canAttackMoveUnitTo(World world, Player player, Unit unit, float f3, float f4) {
        return UnitCommander.attackMoveUnitToInternal(world, player, unit, f3, f4, true);
    }

    static final boolean attackMoveUnitTo(World world, Player player, Unit unit, float f3, float f4) {
        return UnitCommander.attackMoveUnitToInternal(world, player, unit, f3, f4, false);
    }

    private static final boolean attackMoveUnitToInternal(World world, Player player, Unit unit, float f3, float f4, boolean bl) {
        if (!UnitCommander.moveUnitToInternal(world, player, unit, f3, f4, bl)) {
            return false;
        }
        if (!bl) {
            unit.setOrderMode(UnitOrderMode.b);
            if (unit.getPosition().getUnit() != null) {
                unit.setRallyToUnit(unit.getPosition().getUnit());
            } else {
                unit.setRallyToPosition(unit.getPosition().getX(), unit.getPosition().getY());
            }
        }
        return true;
    }

    static final boolean canAttackUnit(World world, Player player, Unit unit, Unit unit2) {
        return UnitCommander.attackUnitInternal(world, player, unit, unit2, true);
    }

    static final boolean attackUnit(World world, Player player, Unit unit, Unit unit2) {
        return UnitCommander.attackUnitInternal(world, player, unit, unit2, false);
    }

    private static final boolean attackUnitInternal(World world, Player player, Unit unit, Unit unit2, boolean bl) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (unit2 == null || unit2.isDestroyed()) {
            return false;
        }
        if (unit.getOwner() == unit2.getOwner() && !unit2.isImmobile()) {
            return false;
        }
        if (!UnitCommander.targetUnitInternal(world, player, unit, unit2, bl)) {
            return false;
        }
        if (!bl) {
            unit.setOrderMode(UnitOrderMode.b);
            if (unit.getPosition().getUnit() != null) {
                unit.setRallyToUnit(unit.getPosition().getUnit());
            } else {
                unit.setRallyToPosition(unit.getPosition().getX(), unit.getPosition().getY());
            }
        }
        return true;
    }

    static final boolean canRepair(World world, Player player, Unit unit) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (!unit.isCountZero() || !unit.isDeployed()) {
            return false;
        }
        if (unit.getOwner() != player) {
            return false;
        }
        if (unit.getHealth() >= unit.getUnitType().getHealthScale()) {
            return false;
        }
        if (unit.getUnitType().isFlying()) {
            return false;
        }
        if (unit.getPosition().getUnit() != null || unit.getOrderMode() == UnitOrderMode.c) {
            return false;
        }
        TerrainGrid terrainGrid = world.getTerrainGrid();
        UnitList unitList = world.getUnits();
        int i5 = 0;
        while (i5 < unitList.size()) {
            Unit unit2 = (Unit)unitList.get(i5);
            if (unit2.canEmbark(unit) && terrainGrid.hasPathToUnit(unit, unit2)) {
                return true;
            }
            ++i5;
        }
        return false;
    }

    static final boolean hasRepairableUnit(World world, Player player, UnitList unitList) {
        int i3 = 0;
        while (i3 < unitList.size()) {
            if (UnitCommander.canRepair(world, player, (Unit)unitList.get(i3))) {
                return true;
            }
            ++i3;
        }
        return false;
    }

    static final boolean repairUnit(World world, Player player, Unit unit) {
        if (!UnitCommander.canRepair(world, player, unit)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot REPAIR (check failed): " + unit);
            }
            return false;
        }
        TerrainGrid terrainGrid = world.getTerrainGrid();
        Unit unit2 = null;
        float f5 = Float.MAX_VALUE;
        UnitPosition unitPosition = unit.getPosition();
        UnitList unitList = world.getUnits();
        int n = 0;
        while (n < unitList.size()) {
            float f10;
            Unit unit3 = (Unit)unitList.get(n);
            if (unit3.canEmbark(unit) && terrainGrid.hasPathToUnit(unit, unit3) && (f10 = unit3.getPosition().distanceSquaredTo(unitPosition)) < f5) {
                unit2 = unit3;
                f5 = f10;
            }
            ++n;
        }
        UnitGroup unitGroup = unit.getUnitGroup();
        if (unitGroup != null) {
            unitGroup.getUnits().remove(unit);
            unit.setUnitGroup((UnitGroup)null);
        }
        UnitCommander.targetUnit(world, player, unit, unit2);
        unit.setOrderMode(UnitOrderMode.c);
        return true;
    }

    static final boolean repairUnits(World world, Player player, UnitList unitList) {
        boolean i3 = false;
        int i4 = 0;
        while (i4 < unitList.size()) {
            if (UnitCommander.canRepair(world, player, (Unit)unitList.get(i4)) && UnitCommander.repairUnit(world, player, (Unit)unitList.get(i4))) {
                i3 = true;
            }
            ++i4;
        }
        return i3;
    }

    static final boolean canSetAuto(World world, Player player, Unit unit) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (!unit.isCountZero()) {
            return false;
        }
        if (unit.getOwner() != player) {
            return false;
        }
        return unit.getOrderMode() != UnitOrderMode.d;
    }

    static final boolean setAuto(World world, Player player, Unit unit) {
        if (!UnitCommander.canSetAuto(world, player, unit)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot AUTO (check failed): " + unit);
            }
            return false;
        }
        unit.setOrderMode(UnitOrderMode.d);
        if (!unit.getUnitType().isFlying()) {
            unit.stopGuarding();
            unit.clearRally();
        } else if (unit.getGuardPositionOrNull() != null && unit.getGuardPositionOrNull().getUnit() != null && unit.getGuardPositionOrNull().getUnit().canEmbark(unit)) {
            unit.clearRally();
        }
        return true;
    }

    static final boolean canToggleHostedAuto(World world, Player player, Unit unit) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (!unit.isCountZero()) {
            return false;
        }
        if (unit.getOwner() != player) {
            return false;
        }
        return unit.getUnitType().canCarryAircraft();
    }

    static final boolean canToggleHostedAutoAny(World world, Player player, UnitList unitList) {
        int i3 = 0;
        while (i3 < unitList.size()) {
            if (UnitCommander.canToggleHostedAuto(world, player, (Unit)unitList.get(i3))) {
                return true;
            }
            ++i3;
        }
        return false;
    }

    static final boolean toggleHostedAuto(World world, Player player, Unit unit) {
        if (!UnitCommander.canToggleHostedAuto(world, player, unit)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot AUTO hosted units (check failed): " + unit);
            }
            return false;
        }
        boolean i3 = !unit.isHostedAuto();
        unit.setHostedAuto(i3);
        UnitList unitList = unit.getSubUnits();
        int n = 0;
        while (n < unitList.size()) {
            Unit unit2 = (Unit)unitList.get(n);
            if (i3 && unit2.getOrderMode() != UnitOrderMode.d && unit2.isCountZero()) {
                unit2.setOrderMode(UnitOrderMode.d);
            } else if (!i3 && unit2.getOrderMode() == UnitOrderMode.d) {
                unit2.setOrderMode(UnitOrderMode.a);
            }
            ++n;
        }
        UnitList unitList2 = world.getUnits();
        int n2 = 0;
        while (n2 < unitList2.size()) {
            Unit unit3 = (Unit)unitList2.get(n2);
            if (unit.canEmbark(unit3) && (unit3.getGuardPositionOrNull() != null && unit3.getGuardPositionOrNull().getUnit() == unit || unit3.getRallyPositionOrNull() != null && unit3.getRallyPositionOrNull().getUnit() == unit)) {
                if (i3 && unit3.getOrderMode() != UnitOrderMode.d && unit3.isCountZero()) {
                    unit3.setOrderMode(UnitOrderMode.d);
                } else if (!i3 && unit3.getOrderMode() == UnitOrderMode.d) {
                    unit3.setOrderMode(UnitOrderMode.a);
                }
            }
            ++n2;
        }
        return true;
    }

    static final boolean toggleHostedAutoUnits(World world, Player player, UnitList unitList) {
        Unit unit;
        boolean i3 = false;
        boolean i4 = false;
        int i5 = 0;
        while (i5 < unitList.size()) {
            unit = (Unit)unitList.get(i5);
            if (unit != null && !unit.isDestroyed() && unit.getUnitType().canCarryAircraft() && !unit.isHostedAuto()) {
                i4 = true;
            }
            ++i5;
        }
        i5 = 0;
        while (i5 < unitList.size()) {
            unit = (Unit)unitList.get(i5);
            if (unit != null && !unit.isDestroyed() && UnitCommander.canToggleHostedAuto(world, player, unit) && unit.isHostedAuto() != i4 && UnitCommander.toggleHostedAuto(world, player, unit)) {
                i3 = true;
            }
            ++i5;
        }
        return i3;
    }

    static final boolean canStop(World world, Player player, Unit unit) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (!unit.isCountZero()) {
            return false;
        }
        if (unit.getOwner() != player) {
            return false;
        }
        if (unit.getOrderMode() != UnitOrderMode.d && !unit.isDeployed()) {
            return false;
        }
        return unit.getOrderMode() == UnitOrderMode.d || unit.getGuardPositionOrNull() != null && (!unit.getUnitType().isFlying() || unit.getOrderMode() != UnitOrderMode.a);
    }

    static final boolean canStopAny(World world, Player player, UnitList unitList) {
        int i3 = 0;
        while (i3 < unitList.size()) {
            if (UnitCommander.canStop(world, player, (Unit)unitList.get(i3))) {
                return true;
            }
            ++i3;
        }
        return false;
    }

    static final boolean stopUnit(World world, Player player, Unit unit) {
        if (!UnitCommander.canStop(world, player, unit)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot STOP (check failed): " + unit);
            }
            return false;
        }
        unit.setOrderMode(UnitOrderMode.a);
        if (!unit.getUnitType().isFlying()) {
            unit.stopGuarding();
            unit.clearRally();
        } else if (unit.getGuardPositionOrNull() != null && unit.getGuardPositionOrNull().getUnit() != null && unit.getGuardPositionOrNull().getUnit().canEmbark(unit)) {
            unit.clearRally();
        }
        return true;
    }

    static final boolean stopUnits(World world, Player player, UnitList unitList) {
        boolean i3 = false;
        int i4 = 0;
        while (i4 < unitList.size()) {
            if (UnitCommander.canStop(world, player, (Unit)unitList.get(i4)) && UnitCommander.stopUnit(world, player, (Unit)unitList.get(i4))) {
                i3 = true;
            }
            ++i4;
        }
        return i3;
    }

    static final boolean canFormSquad(World world, Player player, UnitGroup unitGroup, UnitList unitList) {
        if (unitGroup == null) {
            return false;
        }
        return unitList.size() != 0;
    }

    static final boolean formSquad(World world, Player player, UnitGroup unitGroup, UnitList unitList) {
        if (!UnitCommander.canFormSquad(world, player, unitGroup, unitList)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot SQUAD (check failed): " + unitList.size());
            }
            return false;
        }
        int i4 = 0;
        while (i4 < unitGroup.getUnits().size()) {
            ((Unit)unitGroup.getUnits().get(i4)).setUnitGroup((UnitGroup)null);
            ++i4;
        }
        unitGroup.getUnits().clear();
        i4 = 0;
        while (i4 < unitList.size()) {
            Unit unit = (Unit)unitList.get(i4);
            if (unit != null && !unit.isDestroyed()) {
                if (unit.getUnitGroup() != null) {
                    unit.getUnitGroup().getUnits().remove(unit);
                    unit.setUnitGroup((UnitGroup)null);
                }
                unit.setUnitGroup(unitGroup);
                unitGroup.getUnits().add(unit);
            }
            ++i4;
        }
        return true;
    }

    static final boolean canAssignSquad(World world, Player player, UnitList unitList) {
        int n = 0;
        while (n < unitList.size()) {
            UnitGroup unitGroup;
            Unit unit = (Unit)unitList.get(n);
            if (unit != null && !unit.isDestroyed() && (unitGroup = unit.getUnitGroup()) != null) {
                boolean i6 = true;
                UnitList unitList2 = unitGroup.getUnits();
                int i8 = 0;
                while (i8 < unitList2.size()) {
                    if (!unitList.contains(unitList2.get(i8))) {
                        i6 = false;
                        break;
                    }
                    ++i8;
                }
                if (i6) {
                    return true;
                }
            }
            ++n;
        }
        UnitGroupSet unitGroupSet = player.getUnitGroups();
        int n2 = 0;
        while (n2 < unitGroupSet.size()) {
            if (unitGroupSet.get(n2).getUnits().size() == 0) {
                return UnitCommander.canFormSquad(world, player, unitGroupSet.get(n2), unitList);
            }
            ++n2;
        }
        return false;
    }

    static final boolean assignSquad(World world, Player player, UnitList unitList) {
        if (!UnitCommander.canAssignSquad(world, player, unitList)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot SQUAD (check failed): " + unitList.size());
            }
            return false;
        }
        int n = 0;
        while (n < unitList.size()) {
            UnitGroup unitGroup;
            Unit unit = (Unit)unitList.get(n);
            if (unit != null && !unit.isDestroyed() && (unitGroup = unit.getUnitGroup()) != null) {
                boolean i6 = true;
                UnitList unitList2 = unitGroup.getUnits();
                int i8 = 0;
                while (i8 < unitList2.size()) {
                    if (!unitList.contains(unitList2.get(i8))) {
                        i6 = false;
                        break;
                    }
                    ++i8;
                }
                if (i6) {
                    i8 = 0;
                    while (i8 < unitList.size()) {
                        Unit unit2 = (Unit)unitList.get(i8);
                        if (unit2 != null && !unit2.isDestroyed()) {
                            if (unit2.getUnitGroup() != null) {
                                unit2.getUnitGroup().getUnits().remove(unit2);
                            }
                            unitGroup.getUnits().add(unit2);
                            unit2.setUnitGroup(unitGroup);
                        }
                        ++i8;
                    }
                    return true;
                }
            }
            ++n;
        }
        UnitGroupSet unitGroupSet = player.getUnitGroups();
        int n2 = 0;
        while (n2 < unitGroupSet.size()) {
            if (unitGroupSet.get(n2).getUnits().size() == 0) {
                return UnitCommander.formSquad(world, player, unitGroupSet.get(n2), unitList);
            }
            ++n2;
        }
        return false;
    }

    static final boolean hasSquad(World world, Player player, Unit unit) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        return unit.getUnitGroup() != null;
    }

    static final boolean hasSquadAny(World world, Player player, UnitList unitList) {
        int i3 = 0;
        while (i3 < unitList.size()) {
            if (UnitCommander.hasSquad(world, player, (Unit)unitList.get(i3))) {
                return true;
            }
            ++i3;
        }
        return false;
    }

    static final boolean leaveSquad(World world, Player player, Unit unit) {
        if (!UnitCommander.hasSquad(world, player, unit)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot UN-SQUAD (check failed): " + unit);
            }
            return false;
        }
        UnitGroup unitGroup = unit.getUnitGroup();
        unitGroup.getUnits().remove(unit);
        unit.setUnitGroup((UnitGroup)null);
        return true;
    }

    static final boolean leaveSquadUnits(World world, Player player, UnitList unitList) {
        boolean i3 = false;
        int i4 = 0;
        while (i4 < unitList.size()) {
            if (UnitCommander.leaveSquad(world, player, (Unit)unitList.get(i4))) {
                i3 = true;
            }
            ++i4;
        }
        return i3;
    }

    static final boolean canSetRallyPoint(World world, Player player, Unit unit, float f3, float f4) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (unit.getOwner() != player) {
            return false;
        }
        if (!unit.isImmobile()) {
            return false;
        }
        return world.getTerrainGrid().canDomainEnter(((UnitType)unit.getUnitType().getProducedBy().get(0)).getDomain(), (int)f3, (int)f4);
    }

    static final boolean setRallyPoint(World world, Player player, Unit unit, float f3, float f4) {
        if (!UnitCommander.canSetRallyPoint(world, player, unit, f3, f4)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot RALLY (check failed): " + unit);
            }
            return false;
        }
        unit.setMoveTargetPosition(f3, f4);
        return true;
    }

    static final boolean isImmobile(World world, Player player, Unit unit) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (unit.getOwner() != player) {
            return false;
        }
        return unit.isImmobile();
    }

    static final boolean clearRallyPoint(World world, Player player, Unit unit) {
        if (!UnitCommander.isImmobile(world, player, unit)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot UN-RALLY (check failed): " + unit);
            }
            return false;
        }
        unit.clearMoveTarget();
        return true;
    }

    static final boolean canBuildUnit(World world, Player player, Unit unit, UnitType unitType) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (unit.getOwner() != player) {
            return false;
        }
        if (unit.getOwner().getResources() < unitType.getHealth()) {
            return false;
        }
        if (!unit.getUnitType().getProducedBy().contains(unitType)) {
            return false;
        }
        if (unit.getOwner() != null && !unit.getOwner().getAvailableUnitTypes().contains(unitType)) {
            return false;
        }
        return unit.getSubUnits().size() < unit.getUnitType().getCapacity();
    }

    static final boolean buildUnit(World world, Player player, Unit unit, UnitType unitType) {
        if (!UnitCommander.canBuildUnit(world, player, unit, unitType)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot BUILD (check failed): " + unit);
            }
            return false;
        }
        player.setResources(player.getResources() - unitType.getHealth());
        world.addHostedUnit(unitType, unit, player);
        return true;
    }

    static final boolean canCancelBuild(World world, Player player, Unit unit) {
        if (unit == null || unit.isDestroyed()) {
            return false;
        }
        if (unit.getOwner() != player) {
            return false;
        }
        return !unit.isCountZero() && unit.isCountFull();
    }

    static final boolean cancelBuild(World world, Player player, Unit unit) {
        if (!UnitCommander.canCancelBuild(world, player, unit)) {
            if (GameConfig.isDebugEnabled()) {
                OsfLog.info("Cannot CANCEL (check failed): " + unit);
            }
            return false;
        }
        player.setResources(player.getResources() + unit.getUnitType().getHealth());
        world.removeUnit(unit);
        return true;
    }
}


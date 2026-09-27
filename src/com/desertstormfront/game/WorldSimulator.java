/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game;

import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.game.GameEventListener;
import com.desertstormfront.game.NullGameEventListener;
import com.desertstormfront.game.World;
import com.desertstormfront.game.mode.CaptureTheFlagMode;
import com.desertstormfront.game.mode.GameMode;
import com.desertstormfront.game.model.AmmoType;
import com.desertstormfront.game.model.Bird;
import com.desertstormfront.game.model.BirdList;
import com.desertstormfront.game.model.BirdType;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.EffectTimer;
import com.desertstormfront.game.model.Projectile;
import com.desertstormfront.game.model.ProjectileList;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitOrderMode;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.Volley;
import com.desertstormfront.game.player.FogOfWar;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerList;
import com.desertstormfront.game.player.PlayerStatistics;
import com.desertstormfront.game.player.UnitGroup;
import com.desertstormfront.map.MapDefinition;
import com.desertstormfront.world.Neighbor;
import com.desertstormfront.world.TerrainGrid;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;
import com.desertstormfront.world.Vec3;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.math.MathHelper;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Advances {@link World} by one fixed simulation step: the authoritative, frame-rate independent
 * update that turns orders into outcomes.
 *
 * <p>What it drives each step: unit movement along their current orders, combat (a unit with a
 * target builds a {@link Volley}, whose {@link Projectile}s and {@link EffectTimer}s are resolved
 * here), production and repair progress, structure capture, player income and statistics, and each
 * player's {@link FogOfWar}. Decorative motion ({@link Bird}) is simulated alongside.
 *
 * <p>Two rules decide most combat outcomes and are easy to miss, and both come from the unit's
 * {@link UnitType} rather than from this class: a unit may only fire once its own timer has
 * elapsed, and the tracked types must first turn toward the target and stay inside a narrow firing
 * arc.
 *
 * <p>What differs per scenario lives behind {@link GameMode} ({@link CaptureTheFlagMode} is one of
 * them). Everything the presentation layer observes is reported through {@link GameEventListener},
 * which is why a headless run can plug in {@link NullGameEventListener} instead.
 */
public final class WorldSimulator {
    private static final GameEventListener NULL_LISTENER = new NullGameEventListener();
    private static final int[][] NEIGHBOR_OFFSETS;
    private static final float[][] GUARD_FOLLOWER_SPREADS;
    private static final float[][] RALLY_SPREADS;

    static {
        int[][] nArrayArray = new int[8][];
        int[] nArray = new int[2];
        nArray[0] = -1;
        nArrayArray[0] = nArray;
        int[] nArray2 = new int[2];
        nArray2[0] = 1;
        nArrayArray[1] = nArray2;
        int[] nArray3 = new int[2];
        nArray3[1] = -1;
        nArrayArray[2] = nArray3;
        int[] nArray4 = new int[2];
        nArray4[1] = 1;
        nArrayArray[3] = nArray4;
        nArrayArray[4] = new int[]{-1, -1};
        nArrayArray[5] = new int[]{1, 1};
        nArrayArray[6] = new int[]{-1, 1};
        nArrayArray[7] = new int[]{1, -1};
        NEIGHBOR_OFFSETS = nArrayArray;
        GUARD_FOLLOWER_SPREADS = new float[][]{{1.5f, 1.5707964f}, {1.5f, 4.712389f}, {1.5f, 0.0f}, {1.5f, (float)Math.PI}, {2.12f, 0.7853982f}, {2.12f, 5.4977875f}, {2.12f, 2.3561945f}, {2.12f, 3.926991f}, {3.0f, 1.5707964f}, {3.0f, 4.712389f}, {3.0f, 0.0f}, {3.0f, (float)Math.PI}, {4.24f, 0.7853982f}, {4.24f, 5.4977875f}, {4.24f, 2.3561945f}, {4.24f, 3.926991f}};
        RALLY_SPREADS = new float[][]{{0.4f, 0.7853982f}, {0.4f, 3.926991f}, {0.4f, 2.3561945f}, {0.4f, 5.4977875f}, {1.2f, 0.0f}, {1.2f, (float)Math.PI}, {1.2f, 1.5707964f}, {1.2f, 4.712389f}, {1.2f, 0.7853982f}, {1.2f, 3.926991f}, {1.2f, 2.3561945f}, {1.2f, 5.4977875f}};
    }

    public static final strictfp void tick(World world, int i1) {
        WorldSimulator.tick(world, NULL_LISTENER, i1);
    }

    public static final strictfp void tick(World world, GameEventListener gameEventListener, int i2) {
        WorldSimulator.tick(world, gameEventListener, (float)i2 / 1000.0f);
    }

    public static final strictfp void tick(World world, GameEventListener gameEventListener, float f2) {
        long l;
        float unitList2;
        Object object;
        float f3;
        Object object2;
        long l3 = System.nanoTime();
        float f5 = world.getGameTime();
        float f6 = f5 + f2;
        world.setGameTime(f6);
        int i7 = world.getTurn() + 1;
        world.setTurn(i7);
        boolean i8 = false;
        boolean i9 = false;
        boolean i10 = false;
        boolean i11 = false;
        int i12 = (int)(f5 * 10.0f);
        int i13 = (int)(f6 * 10.0f);
        if (i12 != i13 && i13 % 2 == 0) {
            i8 = true;
            if (i13 % 10 == 0) {
                i9 = true;
                if (i13 % 50 == 0) {
                    i10 = true;
                }
                if (i13 % 800 == 0) {
                    i11 = true;
                }
            }
        }
        float f14 = world.getSpeedFactor();
        MapDefinition mapDefinition = world.getMapDefinition();
        TerrainGrid terrainGrid = world.getTerrainGrid();
        UnitList unitList = world.getUnits();
        int i18 = 0;
        while (i18 < unitList.size()) {
            Object f20;
            float f4;
            float f7;
            float f8;
            float f9;
            float f10;
            float f11 = 0.0f;
            Object object4;
            Unit unit = (Unit)unitList.get(i18);
            UnitType unitType = unit.getUnitType();
            if (!unit.isImmobile() && unit.getPosition().getUnit() == null) {
                terrainGrid.removeUnitFromTile(unit);
            }
            if (unit.isDestroyed() && !unit.isEffectActive() && !unit.isVolleyActive()) {
                unitList.remove(i18);
                if (!unit.getUnitType().isGeneral() || unitList.hasGeneral((Player)(object4 = unit.getOwner()))) continue;
                int n = 0;
                while (n < unitList.size()) {
                    if (((Unit)unitList.get(n)).getOwner() == object4) {
                        WorldSimulator.destroyUnit((Unit)unitList.get(n));
                        if (((Unit)unitList.get(n)).getUnitType().isCapturable()) {
                            ((Unit)unitList.get(n)).setOwner((Player)null);
                        }
                    }
                    ++n;
                }
                continue;
            }
            if (unit.isEffectActive()) {
                object4 = unit.getActiveEffectTimer();
                float f12 = ((EffectTimer)object4).getTime() + f2;
                if (f12 > 1.5f) {
                    unit.deactivateEffectTimer();
                } else {
                    ((EffectTimer)object4).setTime(f12);
                }
            }
            if (unit.isVolleyActive()) {
                object4 = unit.getActiveVolley();
                Unit unit2 = ((Volley)object4).getTargetUnit();
                if (unit.getOwner() == unit2.getOwner()) {
                    unit.clearVolley();
                } else if (unit2.isDestroyed() || unit2.getOwner() == null) {
                    unit.clearVolley();
                } else if (unit2.getPosition().getUnit() != null) {
                    unit.clearVolley();
                } else if (((Volley)object4).getIntensity() >= unitType.getSightRange()) {
                    unit.clearVolley();
                } else {
                    object2 = ((Volley)object4).getAmmoType();
                    f11 = ((AmmoType)((Object)object2)).getSpeed() * f2;
                    f10 = f11 * f11;
                    ((Volley)object4).setIntensity(((Volley)object4).getIntensity() + f11);
                    f3 = ((Volley)object4).getOrigin().getX();
                    f9 = ((Volley)object4).getOrigin().getY();
                    f8 = unit2.getPosition().getX();
                    f7 = unit2.getPosition().getY();
                    f4 = (f3 - f8) * (f3 - f8) + (f9 - f7) * (f9 - f7);
                    if (f4 <= f10 + 0.12f) {
                        unit.clearVolley();
                        int n = unit2.getHealth();
                        int n2 = unitType.getDamageAgainst(unit2.getUnitType());
                        switch (unit.getRank()) {
                            case 1: {
                                n2 = n2 * 5 / 4;
                                break;
                            }
                            case 2: {
                                n2 = n2 * 6 / 4;
                                break;
                            }
                            case 3: {
                                n2 = n2 * 7 / 4;
                            }
                        }
                        switch (unit2.getRank()) {
                            case 1: {
                                n2 = n2 * 3 / 4;
                                break;
                            }
                            case 2: {
                                n2 = n2 * 2 / 4;
                                break;
                            }
                            case 3: {
                                n2 = n2 * 1 / 4;
                            }
                        }
                        if (n2 <= 0) {
                            n2 = 1;
                        }
                        object = unit.getOwner().getStatistics();
                        PlayerStatistics playerStatistics = unit2.getOwner().getStatistics();
                        ((PlayerStatistics)object).setDamageInflicted(((PlayerStatistics)object).getDamageInflicted() + n2);
                        playerStatistics.setDamageReceived(playerStatistics.getDamageReceived() + n2);
                        if ((n -= n2) <= 0) {
                            CaptureTheFlagMode f21;
                            Player f17 = unit2.getOwner();
                            int f18 = (unit2.isImmobile() ? 0 : 1) + unit2.getSubUnits().size();
                            ((PlayerStatistics)object).setDestroyedUnits(((PlayerStatistics)object).getDestroyedUnits() + f18);
                            playerStatistics.setLostUnits(playerStatistics.getLostUnits() + f18);
                            int f19 = unit.getKillCount() + 1;
                            unit.setKillCount(f19);
                            switch (unit.getRank()) {
                                case 0: {
                                    if (f19 < 4) break;
                                    unit.setRank(1);
                                    break;
                                }
                                case 1: {
                                    if (f19 < 9) break;
                                    unit.setRank(2);
                                    break;
                                }
                                case 2: {
                                    if (f19 < 15) break;
                                    unit.setRank(3);
                                }
                            }
                            WorldSimulator.destroyUnit(unit2);
                            if (unit2.getUnitType().isCapturable()) {
                                unit2.setOwner((Player)null);
                                playerStatistics.setBasesLost(playerStatistics.getBasesLost() + 1);
                            } else {
                                unit2.activateEffectTimer();
                            }
                            WorldSimulator.spawnBirds(world, unit2.getPosition());
                            gameEventListener.onUnitDestroyed(unit2, unit.getOwner(), f17);
                            f20 = world.getGameMode();
                            if (f20 instanceof CaptureTheFlagMode && (f21 = (CaptureTheFlagMode)f20).getFlag() == unit2) {
                                gameEventListener.onFlagLost(f17);
                            }
                        } else {
                            unit2.setHealth(n);
                            unit2.activateEffectTimer();
                            WorldSimulator.spawnBirds(world, unit2.getPosition());
                            gameEventListener.onUnitHit(unit2, unit.getOwner());
                        }
                    } else {
                        if (UserConfig.isRenderDetails() && ((AmmoType)((Object)object2)).hasSubProjectiles()) {
                            ProjectileList projectileList = ((Volley)object4).getProjectiles();
                            if (!((Projectile)projectileList.get(0)).isActive()) {
                                Projectile projectile = (Projectile)projectileList.get(0);
                                projectile.setLaunchTime(f6);
                                projectile.getPosition().set(((Volley)object4).getOrigin());
                                projectile.setActive(true);
                            } else if (((Projectile)projectileList.get(((AmmoType)((Object)object2)).getSubProjectileCount() - 1)).isActive()) {
                                if (f6 - ((Projectile)projectileList.get(((AmmoType)((Object)object2)).getSubProjectileCount() - 1)).getLaunchTime() > ((AmmoType)((Object)object2)).getAnimationDuration()) {
                                    Projectile projectile = (Projectile)projectileList.remove(0);
                                    projectileList.add(((AmmoType)((Object)object2)).getSubProjectileCount() - 1, projectile);
                                    projectile.setLaunchTime(f6);
                                    projectile.getPosition().set(((Volley)object4).getOrigin());
                                }
                            } else {
                                int n = 1;
                                while (n < projectileList.size()) {
                                    object = (Projectile)projectileList.get(n);
                                    if (!((Projectile)object).isActive()) {
                                        if (!(f6 - ((Projectile)projectileList.get(n - 1)).getLaunchTime() > ((AmmoType)((Object)object2)).getAnimationDuration())) break;
                                        ((Projectile)object).setLaunchTime(f6);
                                        ((Projectile)object).getPosition().set(((Volley)object4).getOrigin());
                                        ((Projectile)object).setActive(true);
                                        break;
                                    }
                                    ++n;
                                }
                            }
                        }
                        Vec3 vec3 = ((Volley)object4).getDirection();
                        float f13 = MathHelper.atan2(vec3.getY(), vec3.getX());
                        float f15 = f7 - f9;
                        float f16 = f8 - f3;
                        float f30 = MathHelper.atan2(f15, f16);
                        float f = MathHelper.angleDifference(f30, f13);
                        if (MathHelper.abs(f) > ((AmmoType)((Object)object2)).getTurnRate() * f2) {
                            f = f >= 0.0f ? ((AmmoType)((Object)object2)).getTurnRate() : -((AmmoType)((Object)object2)).getTurnRate();
                            f *= f2;
                        }
                        float f12 = MathHelper.sqrt(f4);
                        float f33 = (unit2.getUnitType().getMoveFactor() - ((Volley)object4).getOrigin().getZ()) / f12;
                        vec3.setX(MathHelper.cos(f13 += f));
                        vec3.setY(MathHelper.sin(f13));
                        vec3.setZ(f33);
                        vec3.normalize();
                        ((Volley)object4).getOrigin().add(vec3.getX() * f11, vec3.getY() * f11, vec3.getZ() * f11);
                        float f34 = ((Volley)object4).getOrigin().getX();
                        unitList2 = ((Volley)object4).getOrigin().getY();
                        if (!terrainGrid.isInsideGrid(f34, unitList2)) {
                            ((Volley)object4).getOrigin().setX(f3);
                            ((Volley)object4).getOrigin().setY(f9);
                            ((Volley)object4).setIntensity(unitType.getSightRange());
                        } else if (object2 == AmmoType.Torpedo && (terrainGrid.isAllLandTile((int)f34, (int)unitList2) || terrainGrid.isCoastTile((int)f34, (int)unitList2) && terrainGrid.isLandAtPoint(f34, unitList2))) {
                            ((Volley)object4).setIntensity(unitType.getSightRange());
                        }
                    }
                }
            }
            if (!unit.isDestroyed()) {
                object4 = unit.getPosition();
                Unit unit3 = ((UnitPosition)object4).getUnit();
                float f22 = unitType.getSpeed() * f14;
                if (unitType.getDomain() == Domain.Amphibian && terrainGrid.isAllWaterTileAtPosition((Vec2)object4)) {
                    f22 *= 1.7f;
                }
                float f = unit3 != null ? 0.84f : (f11 = f22 * f2 * (unit.getHealth() <= 250 ? 0.75f : 1.0f));
                if (unit3 != null && (unit.getHealth() < unitType.getHealthScale() || unit.getProgress() < unitType.getAltitude() || unit.getSalvoCount() < unitType.getSalvo())) {
                    f11 = 0.0f;
                }
                if (f11 > 0.0f && unit.getGuardPositionOrNull() != null) {
                    float unitList3;
                    float f13;
                    Object f38;
                    float f44;
                    float f27;
                    float f28;
                    boolean bl;
                    float f29;
                    f10 = ((Vec2)object4).distanceTo(unit.getGuardPositionOrNull());
                    int n = unitType.getRangeInTiles();
                    if ((unit.hasAttackableGuardTarget() || unit.hasCapturableGuardTarget() && unit.getGuardPositionOrNull().getUnit().getOwner() != null) && f10 <= (float)n) {
                        if (!unitType.r() && unit.getPosition().getUnit() == null) {
                            f9 = 0.5f;
                            if (f10 > (float)n - f9) {
                                if (f11 > f10 - ((float)n - f9)) {
                                    f11 = f10 - ((float)n - f9);
                                }
                            } else {
                                f11 = 0.0f;
                            }
                        }
                        if (!(unit.getPosition().getUnit() != null || unit.isVolleyActive() || !(unit.getReadyTime() < f6) || unitType.hasSalvo() && unit.getSalvoCount() <= 0)) {
                            unit.fireAt(unit.getGuardPositionOrNull().getUnit());
                            unit.setReadyTime(f6 + unit.getUnitType().getReloadTime());
                            if (unitType.hasSalvo()) {
                                int n5 = unit.getSalvoCount() - 1;
                                if (n5 < 0) {
                                    n5 = 0;
                                }
                                unit.setSalvoCount(n5);
                            }
                            WorldSimulator.spawnBirds(world, unit.getPosition());
                            gameEventListener.onVolleyFired(unit);
                        }
                    }
                    f9 = ((UnitPosition)object4).getX();
                    f8 = ((UnitPosition)object4).getY();
                    if (unit.getGuardPositionOrNull().getUnit() != null && unit.canGuard(unit.getGuardPositionOrNull().getUnit()) && !unit.getGuardPositionOrNull().getUnit().canEmbark(unit)) {
                        int n6 = unit.getGuardPositionOrNull().getUnit().getGuardFollowers().indexOf(unit);
                        float[] spread = (float[])GUARD_FOLLOWER_SPREADS[n6 % GUARD_FOLLOWER_SPREADS.length];
                        float scale = spread[0] * (unitType.getDomain() == Domain.Ground ? 0.7f : 1.0f);
                        float f31 = unit.getGuardPositionOrNull().getUnit().getAngle() + spread[1];
                        float f26 = scale * MathHelper.cos(f31);
                        float f32 = scale * MathHelper.sin(f31);
                        f20 = unit.getGuardPositionOrNull().getUnit().getPosition();
                        f4 = ((UnitPosition)f20).getX() + f26;
                        if (!terrainGrid.isInsideGrid((float)((int)f4), (float)((int)(f29 = ((UnitPosition)f20).getY() + f32))) || !terrainGrid.hasPathToPosition(unit, f4, f29)) {
                            f4 = ((UnitPosition)f20).getX();
                            f29 = ((UnitPosition)f20).getY();
                        }
                        bl = unit.getPosition().distanceSquaredTo(f4, f29) < 3.0f;
                    } else if (unit.getGuardPositionOrNull().getUnit() != null && unit.getGuardPositionOrNull().getUnit().canProduceOrCarry(unit) && unit.getGuardPositionOrNull().getUnit().getSubUnits().size() >= unit.getGuardPositionOrNull().getUnit().getUnitType().getCapacity()) {
                        bl = false;
                        f4 = f9;
                        f29 = f8;
                    } else {
                        bl = false;
                        f4 = unit.getGuardPositionOrNull().getX();
                        f29 = unit.getGuardPositionOrNull().getY();
                    }
                    Neighbor neighbor = terrainGrid.findReachableNeighbor(unitType.getDomain(), f9, f8, f4, f29);
                    if (neighbor.isValid()) {
                        if (neighbor != Neighbor.Center) {
                            f28 = (float)((int)f9 + neighbor.getDx()) + 0.5f;
                            f27 = (float)((int)f8 + neighbor.getDy()) + 0.5f;
                            if ((int)f28 == (int)f4 && (int)f27 == (int)f29) {
                                f28 = f4;
                                f27 = f29;
                            } else {
                                neighbor = terrainGrid.findReachableNeighbor(unitType.getDomain(), f28, f27, f4, f29);
                                if (neighbor != Neighbor.Center) {
                                    f28 = (float)((int)f28) + neighbor.getEdgeOffsetX();
                                    f27 = (float)((int)f27) + neighbor.getEdgeOffsetY();
                                } else if (MathHelper.abs((int)f4 - (int)f28) <= 1 && MathHelper.abs((int)f29 - (int)f27) <= 1) {
                                    neighbor = Neighbor.of((int)f4 - (int)f28, (int)f29 - (int)f27);
                                    f28 = (float)((int)f28) + neighbor.getEdgeOffsetX();
                                    f27 = (float)((int)f27) + neighbor.getEdgeOffsetY();
                                }
                            }
                        } else {
                            f28 = f4;
                            f27 = f29;
                        }
                    } else {
                        f28 = f9;
                        f27 = f8;
                    }
                    float n16 = ((Vec2)object4).distanceTo(f28, f27);
                    float f45 = f28 - f9;
                    float f41 = f27 - f8;
                    float f46 = bl ? unit.getGuardPositionOrNull().getUnit().getAngle() : (unitType == mapDefinition.getUnitTypeSlots().getTransport() && unit.getGuardPositionOrNull().getUnit() == null && (int)f28 == (int)f4 && (int)f27 == (int)f29 && terrainGrid.isCoastTile((int)f28, (int)f27) ? terrainGrid.getCoastAngle((int)f28, (int)f27) : MathHelper.atan2(f41, f45));
                    if (f11 < n16) {
                        f44 = f11;
                        if (n16 > 1.0E-4f) {
                            f45 = f45 * f44 / n16;
                            f41 = f41 * f44 / n16;
                        }
                    } else {
                        f44 = n16;
                    }
                    if (f44 > 1.0E-4f && unitType.getSize() > 0.0f) {
                        UnitList unitList4 = world.getScratchUnits();
                        unitList4.clear();
                        UnitList unitList5 = WorldSimulator.collectNearbyUnits(unitList4, terrainGrid, (int)f9, (int)f8);
                        unitList5.remove(unit);
                        f38 = unitType.getLayer();
                        f13 = MathHelper.atan2(f41, f45);
                        float vec2 = unitType.getSize();
                        float playerStatistics = f9 + f45;
                        float gameMode = f8 + f41;
                        int captureTheFlagMode = unitList5.size();
                        int i48 = 0;
                        while (i48 < captureTheFlagMode) {
                            Unit unit2 = (Unit)unitList5.get(i48);
                            UnitType unitType2 = unit2.getUnitType();
                            if (f38 == unitType2.getLayer()) {
                                float f51 = vec2 + unitType2.getSize();
                                float f52 = f51 * f51;
                                UnitPosition unitPosition = unit2.getPosition();
                                if (unitPosition.distanceSquaredTo(playerStatistics, gameMode) < f52) {
                                    if (!unitType2.isFlying() && unitPosition.distanceSquaredTo(f4, f29) < f52) {
                                        f45 = 0.0f;
                                        f41 = 0.0f;
                                    } else {
                                        float f54 = unitPosition.getX() - f9;
                                        float f55 = unitPosition.getY() - f8;
                                        float f56 = MathHelper.sqrt(f54 * f54 + f55 * f55);
                                        float f57 = MathHelper.atan2(f55, f54) - f13;
                                        if ((f54 = MathHelper.cos(f57) * f56) > 0.0f && MathHelper.abs(f55 = MathHelper.sin(f57) * f56) < f51) {
                                            float f60;
                                            float f62;
                                            float f61;
                                            float f63;
                                            float f58 = MathHelper.sqrt(f52 - f55 * f55);
                                            float f59 = f54 - f58;
                                            if (f59 <= 0.0f) {
                                                f59 = f54 + f58;
                                            }
                                            if ((f63 = MathHelper.sqrt((f61 = f44 * (-f51 - f59) * 0.8f) * f61 + (f62 = f44 * -(f60 = f55 >= 0.0f ? f55 + 0.25f : f55 - 0.25f) * 2.5f) * f62)) > 1.0E-4f) {
                                                float f64 = MathHelper.atan2(f62, f61) + f13;
                                                f45 += MathHelper.cos(f64) * f63;
                                                f41 += MathHelper.sin(f64) * f63;
                                            }
                                        }
                                    }
                                }
                            }
                            ++i48;
                        }
                    }
                    if (terrainGrid.isInsideGrid(unitList2 = f9 + f45, unitList3 = f8 + f41) && terrainGrid.canDomainEnter(unitType.getDomain(), (int)unitList2, (int)unitList3)) {
                        if (terrainGrid.isCoastTile((int)unitList2, (int)unitList3)) {
                            float[] wind = (float[])world.getWind();
                            terrainGrid.clampPositionToDomain(wind, unit.getUnitType().getDomain(), unitList2, unitList3);
                            unitList2 = (unitList2 + 0.01f * wind[0]) / 1.01f;
                            unitList3 = (unitList3 + 0.01f * wind[1]) / 1.01f;
                        }
                        unit.setMoveProgress(unit.getMoveProgress() + f44);
                        unit.setPosition(unitList2, unitList3);
                        if (f45 * f45 + f41 * f41 > f11 * f11 * 0.9f) {
                            if (unit3 != null) {
                                unit.setAngle(f46);
                            } else {
                                float f15 = MathHelper.angleDifference(f46, unit.getAngle());
                                f13 = (float)Math.PI * 2 * f2;
                                if (MathHelper.abs(f15) > f13) {
                                    f46 = unit.getAngle() + (f15 > 0.0f ? f13 : -f13);
                                }
                                unit.setAngle(f46);
                            }
                        }
                    }
                    UnitPosition unitPosition = unit.getGuardPositionOrNull();
                    Unit f25 = unitPosition.getUnit();
                    if (unitPosition.distanceSquaredTo((Vec2)object4) <= 0.70559996f && f25 != null) {
                        Vec2 unit6 = terrainGrid.getDomainPosition((Vec2)f25.getPosition(), unit.getUnitType().getDomain());
                        if (!f25.isImmobile() || unit6 != null && unit6.equalsInt(unit.getPosition())) {
                            if (f25.canEmbark(unit)) {
                                unit.dockInto(unit.getGuardPositionOrNull().getUnit());
                                unit.executeOrder();
                            } else if (f25.getUnitType().isCapturable() && unitType.canCapture() && f25.getOwner() == null) {
                                CaptureTheFlagMode captureTheFlagMode;
                                f25.setOwner(unit.getOwner());
                                f25.setHealth(f25.getUnitType().getHealthScale() / 3);
                                WorldSimulator.destroyUnit(unit);
                                PlayerStatistics playerStatistics = unit.getOwner().getStatistics();
                                playerStatistics.setBasesCaptured(playerStatistics.getBasesCaptured() + 1);
                                gameEventListener.onStructureCaptured(f25);
                                GameMode gameMode = world.getGameMode();
                                if (gameMode instanceof CaptureTheFlagMode && (captureTheFlagMode = (CaptureTheFlagMode)gameMode).getFlag() == f25) {
                                    captureTheFlagMode.setHoldStartTime(world.getGameTime());
                                    gameEventListener.onFlagTaken(f25.getOwner());
                                }
                            }
                        }
                    }
                    if (!unit.isDestroyed()) {
                        if (f25 != null && (f25.isDestroyed() || !unit.canInteractWith(f25))) {
                            if (unitType.isFlying() || !f25.canProduceOrCarry(unit)) {
                                if (unit.getOrderMode() == UnitOrderMode.b) {
                                    unit.setOrderMode(UnitOrderMode.a);
                                    unit.executeOrder();
                                } else if (unit.getOrderMode() == UnitOrderMode.c) {
                                    Unit unit4 = WorldSimulator.findEmbarkTarget(world, unit);
                                    if (unit4 != null) {
                                        unit.guardUnit(unit4);
                                    } else {
                                        unit.setOrderMode(UnitOrderMode.a);
                                        unit.stopGuarding();
                                        unit.clearRally();
                                    }
                                } else {
                                    unit.executeOrder();
                                }
                            }
                        } else if (((UnitPosition)object4).equals(unit.getGuardPositionOrNull()) && !unit.hasFriendlyGuardTarget() && !unit.hasAttackableGuardTarget() && !unit.hasCapturableGuardTarget()) {
                            unit.executeOrder();
                            if (!unit.isGuarding() && unit.getOrderMode() == UnitOrderMode.c) {
                                unit.setOrderMode(UnitOrderMode.a);
                            }
                        }
                    }
                }
                if (!unit.isDestroyed() && unitType.isFlying() && unit.getPosition().getUnit() == null) {
                    f10 = unit.getProgress() - f11;
                    if (f10 < 0.0f) {
                        f10 = 0.0f;
                    }
                    unit.setProgress(f10);
                    if (f10 == 0.0f) {
                        WorldSimulator.destroyUnit(unit);
                        unit.activateEffectTimer();
                    } else {
                        Unit unit7;
                        if (unit.getGuardPositionOrNull() != null && (unitType.hasSalvo() && unit.getSalvoCount() == 0 || unit.getProgress() < unitType.getVerticalOffset() || unit.getPosition().equalsInt(unit.getGuardPositionOrNull())) && ((unit7 = unit.getGuardPositionOrNull().getUnit()) == null || !unit7.canEmbark(unit))) {
                            if (unit.getRallyPositionOrNull() != null) {
                                Unit unit8 = unit.getRallyPositionOrNull().getUnit();
                                if (unit8 != null && unit8.canEmbark(unit)) {
                                    unit.executeOrder();
                                } else {
                                    if (unit.getOrderMode() == UnitOrderMode.b) {
                                        unit.setOrderMode(UnitOrderMode.a);
                                    }
                                    unit.executeOrder();
                                }
                            } else if (unit7 != null) {
                                if (unit.getOrderMode() == UnitOrderMode.b) {
                                    unit.setOrderMode(UnitOrderMode.a);
                                }
                                unit.stopGuarding();
                                unit.clearRally();
                            }
                        }
                        if (!unit.isGuarding()) {
                            Unit unit9 = WorldSimulator.findEmbarkTarget(world, unit);
                            if (unit9 != null) {
                                unit.guardUnit(unit9);
                            } else {
                                float f39;
                                float f40;
                                UnitPosition unitPosition = unit.getPosition();
                                f8 = unitPosition.getX();
                                f7 = unitPosition.getY();
                                while (!terrainGrid.isInsideGrid(f4 = f8 + 3.0f * MathHelper.cos(f40 = world.getRandom().nextFloat() * ((float)Math.PI * 2)), f39 = f7 + 3.0f * MathHelper.sin(f40)) || !terrainGrid.hasPathToPosition(unit, f4, f39)) {
                                }
                                unit.guardPosition(f4, f39);
                            }
                        }
                    }
                }
            }
            if (!unit.isImmobile() && unit.getPosition().getUnit() == null) {
                terrainGrid.addUnitToTile(unit);
            }
            ++i18;
        }
        int n = unitList.size();
        int n8 = 31;
        int n9 = i7 + 16 & n8;
        int n10 = 0;
        while (n10 < n) {
            block192: {
                block194: {
                    block193: {
                        float f;
                        Player player;
                        int n11;
                        UnitType unitType;
                        block195: {
                            object2 = (Unit)unitList.get(n10);
                            if ((((Unit)object2).getId() & n8) != n9) break block192;
                            if (((Unit)object2).getOrderMode() != UnitOrderMode.d || ((Unit)object2).getGuardPositionOrNull() != null || !((Unit)object2).isCountZero() || !((Unit)object2).isDeployed()) break block193;
                            UnitPosition unitPosition = ((Unit)object2).getPosition();
                            if (unitPosition.getUnit() == null || unitPosition.getUnit().getPosition().getUnit() != null) break block194;
                            FogOfWar fogOfWar = ((Unit)object2).getOwner().getFogOfWar();
                            unitType = ((Unit)object2).getUnitType();
                            n11 = (int)(unitType.getAltitude() - unitType.getVerticalOffset());
                            int n12 = n11 * n11;
                            int n13 = 0;
                            int n14 = n12;
                            Object object6 = null;
                            int n15 = 0;
                            while (n15 < n) {
                                int f43;
                                object = (Unit)unitList.get(n15);
                                int n17 = (int)unitPosition.distanceSquaredTo(((Unit)object).getPosition());
                                if (n17 <= n12 && ((f43 = ((Unit)object2).getUnitType().getDamageAgainst(((Unit)object).getUnitType())) > n13 || f43 == n13 && n17 <= n14) && fogOfWar.isUnitVisible((Unit)object) && ((Unit)object2).canAttackUnit((Unit)object)) {
                                    object6 = object;
                                    n13 = f43;
                                    n14 = n17;
                                }
                                ++n15;
                            }
                            if (object6 == null) break block195;
                            if (((Unit)object6).getUnitType() == mapDefinition.getUnitTypeSlots().getFighterPlane()) {
                                UnitPosition unitPosition2 = ((Unit)object6).getPosition();
                                ((Unit)object2).guardPosition(unitPosition2.getX(), unitPosition2.getY());
                                ((Unit)object2).setRallyToUnit(unitPosition.getUnit());
                            } else {
                                ((Unit)object2).guardUnit((Unit)object6);
                                ((Unit)object2).setRallyToUnit(unitPosition.getUnit());
                            }
                            break block194;
                        }
                        if (unitType != mapDefinition.getUnitTypeSlots().getFighterPlane() || !(player = ((Unit)object2).getOwner()).isExplorationEnabled() && !player.isFogEnabled()) break block194;
                        object = ((Unit)object2).getPosition();
                        float f42 = ((UnitPosition)object).getX();
                        float f57 = ((UnitPosition)object).getY();
                        float f16 = (float)((int)(f6 * 10000.0f) % 360) / 360.0f * ((float)Math.PI * 2);
                        float f17 = MathHelper.cos(f16) * (float)n11;
                        float f18 = f42 + f17;
                        if (!terrainGrid.isInsideGrid(f18, unitList2 = f57 + (f = MathHelper.sin(f16) * (float)n11))) break block194;
                        ((Unit)object2).guardPosition(f18, unitList2);
                        ((Unit)object2).setRallyToUnit(((Unit)object2).getPosition().getUnit());
                        break block194;
                    }
                    if (((Unit)object2).isHostedAuto()) {
                        UnitList unitList4 = ((Unit)object2).getSubUnits();
                        int n18 = 0;
                        while (n18 < unitList4.size()) {
                            Unit unit = (Unit)unitList4.get(n18);
                            if (unit.getOrderMode() != UnitOrderMode.d && unit.isCountZero()) {
                                unit.setOrderMode(UnitOrderMode.d);
                            }
                            ++n18;
                        }
                    }
                }
                if (((Unit)object2).getPosition().getUnit() != null && ((Unit)object2).getPosition().getUnit().hasMoveTarget() && ((Unit)object2).getGuardPositionOrNull() == null && ((Unit)object2).isCountZero() && ((Unit)object2).isDeployed()) {
                    UnitPosition unitPosition = ((Unit)object2).getPosition().getUnit().getMoveTargetPosition();
                    if (unitPosition.getUnit() != null) {
                        ((Unit)object2).guardUnit(unitPosition.getUnit());
                    } else {
                        int n19 = ((Unit)object2).getPosition().getUnit().getRallySpreadIndex();
                        ((Unit)object2).getPosition().getUnit().setRallySpreadIndex(n19 + 1);
                        float[] fArray = RALLY_SPREADS[n19 % RALLY_SPREADS.length];
                        float f47 = fArray[0] * (((Unit)object2).getUnitType().getDomain() == Domain.Ground ? 1.0f : 1.3f);
                        float f48 = fArray[1];
                        float f49 = f47 * MathHelper.cos(f48);
                        float f50 = f47 * MathHelper.sin(f48);
                        float f51 = unitPosition.getX() + f49;
                        float f52 = unitPosition.getY() + f50;
                        if (!terrainGrid.isInsideGrid((float)((int)f51), (float)((int)f52)) || !terrainGrid.hasPathToPosition((Unit)object2, f51, f52)) {
                            f51 = unitPosition.getX();
                            f52 = unitPosition.getY();
                        }
                        ((Unit)object2).guardPosition(f51, f52);
                    }
                }
            }
            ++n10;
        }
        n10 = 31;
        int n20 = i7 & n10;
        int n21 = 0;
        while (n21 < n) {
            Unit unit;
            UnitType unitType;
            Unit unit10 = (Unit)unitList.get(n21);
            if (!((unit10.getId() & n10) != n20 || (unitType = unit10.getUnitType()).hasSalvo() && unit10.getSalvoCount() <= 0 || unit10.getGuardPositionOrNull() != null && unit10.getGuardPositionOrNull().getUnit() != null && unit10.getGuardPositionOrNull().getUnit().getOwner() != unit10.getOwner() || unit10.isDestroyed() || !unit10.isCountZero() || unit10.getOwner() == null || unit10.isVolleyActive() || !(unit10.getReadyTime() < f6) || unit10.getPosition().getUnit() != null && (!unit10.getPosition().getUnit().isImmobile() || unitType.getDomain() != Domain.Ground || unitType.getAmmoType() != AmmoType.Shell && unitType.getAmmoType() != AmmoType.Cannonball) || (unit = WorldSimulator.findAttackTarget(world, unit10)) == null)) {
                boolean bl;
                if (unitType.requiresFacingTarget()) {
                    UnitPosition unitPosition = unit10.getPosition();
                    UnitPosition unitPosition3 = unit.getPosition();
                    float f53 = unitPosition3.getX() - unitPosition.getX();
                    float f54 = unitPosition3.getY() - unitPosition.getY();
                    float f55 = MathHelper.atan2(f54, f53);
                    if (!unit10.isGuarding()) {
                        float f56 = MathHelper.angleDifference(f55, unit10.getAngle());
                        float n37 = (float)Math.PI * 2 * f2 * (float)(n10 + 1);
                        if (MathHelper.abs(f56) > n37) {
                            f56 = f56 > 0.0f ? n37 : -n37;
                        }
                        unit10.setAngle(unit10.getAngle() + f56);
                    }
                    bl = Math.abs(MathHelper.angleDifference(f55, unit10.getAngle())) < 0.7853982f;
                } else {
                    boolean bl2 = bl = !unitType.hasSalvo() || !unit10.hasAttackableGuardTarget() && !unit10.hasCapturableGuardTarget();
                }
                if (bl) {
                    unit10.fireAt(unit);
                    unit10.setReadyTime(f6 + unitType.getReloadTime());
                    if (unitType.hasSalvo()) {
                        int n22 = unit10.getSalvoCount() - 1;
                        if (n22 < 0) {
                            n22 = 0;
                        }
                        unit10.setSalvoCount(n22);
                    }
                    WorldSimulator.spawnBirds(world, unit10.getPosition());
                    gameEventListener.onVolleyFired(unit10);
                }
            }
            ++n21;
        }
        if (i7 % 8 == 0) {
            PlayerList playerList = world.getPlayers();
            Player player = (Player)playerList.get(i7 / 8 % playerList.size());
            world.updateFogOfWar(player);
        }
        if (i8) {
            int n23 = 0;
            while (n23 < n) {
                Unit unit = (Unit)unitList.get(n23);
                if (!unit.isDestroyed()) {
                    if (unit.getPosition().getUnit() != null) {
                        UnitType unitType = unit.getUnitType();
                        int n24 = unit.getHealth() + 15;
                        if (n24 > unitType.getHealthScale()) {
                            n24 = unitType.getHealthScale();
                        }
                        unit.setHealth(n24);
                        if (unitType.isFlying()) {
                            float f58 = unit.getProgress() + 0.5f;
                            if (f58 > unitType.getAltitude()) {
                                f58 = unitType.getAltitude();
                            }
                            unit.setProgress(f58);
                        }
                        if (unitType.hasSalvo()) {
                            unit.setSalvoCount(unitType.getSalvo());
                        }
                    } else {
                        UnitList unitList5 = unit.getSubUnits();
                        int n25 = 0;
                        while (n25 < unitList5.size()) {
                            Unit unit11 = (Unit)unitList5.get(n25);
                            if (!unit11.isCountZero()) {
                                int n26 = unit11.getCount() - UnitType.getSlotLimit() / 5;
                                if (n26 <= 0) {
                                    n26 = 0;
                                    gameEventListener.onUnitConstructed(unit11);
                                }
                                unit11.setCount(n26);
                                break;
                            }
                            ++n25;
                        }
                    }
                }
                ++n23;
            }
        }
        if (i10) {
            int n27 = 0;
            while (n27 < n) {
                Unit unit = (Unit)unitList.get(n27);
                if (unit.isImmobile() && unit.getOwner() != null) {
                    int n28 = unit.getHealth() + 25;
                    if (n28 > unit.getUnitType().getHealthScale()) {
                        n28 = unit.getUnitType().getHealthScale();
                    }
                    unit.setHealth(n28);
                }
                ++n27;
            }
        }
        if (i11) {
            int n29 = 0;
            while (n29 < n) {
                Unit unit = (Unit)unitList.get(n29);
                UnitType unitType = unit.getUnitType();
                if (unitType.isBuilding() && unit.getOwner() != null) {
                    unit.getOwner().setResources(unit.getOwner().getResources() + unit.getOwner().getIncomePerBuilding());
                }
                ++n29;
            }
            gameEventListener.onGameTick();
        }
        if (i9) {
            PlayerList playerList = world.getPlayers();
            int n30 = playerList.size();
            int n31 = 0;
            while (n31 < n30) {
                Player player = (Player)playerList.get(n31);
                if (player.isAlive()) {
                    boolean bl = false;
                    int n32 = 0;
                    while (n32 < n) {
                        if (((Unit)unitList.get(n32)).getOwner() == player) {
                            bl = true;
                            break;
                        }
                        ++n32;
                    }
                    if (!bl) {
                        player.setAlive(false);
                        player.setResources(0L);
                        gameEventListener.onPlayerEliminated(player);
                    }
                }
                ++n31;
            }
        }
        BirdList birdList = world.getBirds();
        float f59 = -1.0f;
        f3 = -1.0f;
        float f60 = (float)terrainGrid.getWidth() + 1.0f;
        float f61 = (float)terrainGrid.getHeight() + 1.0f;
        i18 = 0;
        while (i18 < birdList.size()) {
            Bird bird = (Bird)birdList.get(i18);
            Vec3 vec3 = bird.getPosition();
            float f62 = bird.getType().getSpeed() * f2;
            float f63 = bird.getAngle();
            float f64 = vec3.getX() + f62 * MathHelper.cos(f63);
            float f65 = vec3.getY() + f62 * MathHelper.sin(f63);
            if (f64 < f59 || f65 < f3 || f64 > f60 || f65 > f61) {
                birdList.remove(i18);
                continue;
            }
            vec3.setX(f64);
            vec3.setY(f65);
            bird.setDistance(bird.getDistance() + f62);
            ++i18;
        }
        if (i8) {
            int n33 = 15;
            int n34 = i13 >> 1 & n33;
            int n35 = 0;
            while (n35 < n) {
                Unit unit = (Unit)unitList.get(n35);
                if ((unit.getId() & n33) == n34) {
                    int n36 = unit.getUnitType().getHealthScale();
                    if (!unit.isDestroyed() && unit.getHealth() < n36 && unit.getPosition().getUnit() == null && WorldSimulator.findRepairUnit(world, unit) != null) {
                        int n2;
                        int n3 = 0;
                        int n38 = 12000 / (int)unit.getUnitType().getHealth();
                        if (n38 == 0) {
                            n3 = 1;
                        }
                        if ((n2 = unit.getHealth() + n3) > n36) {
                            n2 = n36;
                        }
                        unit.setHealth(n2);
                    }
                }
                ++n35;
            }
        }
        if (GameConfig.isDebugEnabled() && (l = System.nanoTime() - l3) > 2000000L) {
            OsfLog.info("Slow World Update: " + l / 1000000L + "." + l / 100000L % 10L + "ms");
        }
    }

    private static final strictfp void destroyUnit(Unit unit) {
        unit.setHealth(0);
        UnitList unitList = unit.getSubUnits();
        int n = 0;
        while (n < unitList.size()) {
            WorldSimulator.destroyUnit((Unit)unitList.get(n));
            ++n;
        }
        unitList.clear();
        if (unit.getUnitGroup() != null) {
            UnitGroup unitGroup = unit.getUnitGroup();
            unitGroup.getUnits().remove(unit);
            unit.setUnitGroup((UnitGroup)null);
        }
        unit.setHostedAuto(false);
        unit.setOrderMode(UnitOrderMode.a);
        unit.stopGuarding();
        unit.clearRally();
        unit.clearMoveTarget();
        UnitList unitList2 = unit.getGuardFollowers();
        while (unitList2.size() > 0) {
            ((Unit)unitList2.get(0)).executeOrder();
        }
    }

    private static final strictfp void spawnBirds(World world, UnitPosition unitPosition) {
        BirdList birdList;
        if (UserConfig.isRenderDetails() && ((birdList = world.getBirds()).size() == 0 || ((Bird)birdList.get(birdList.size() - 1)).getDistance() > 10.0f)) {
            TerrainGrid terrainGrid = world.getTerrainGrid();
            int i4 = -1;
            int n = 0;
            while (n < NEIGHBOR_OFFSETS.length) {
                int i7;
                int i6 = (int)unitPosition.getX() + NEIGHBOR_OFFSETS[n][0];
                if (terrainGrid.isInsideGrid((float)i6, (float)(i7 = (int)unitPosition.getY() + NEIGHBOR_OFFSETS[n][1])) && terrainGrid.getSite(i6, i7) != null) {
                    i4 = n;
                    break;
                }
                ++n;
            }
            if (i4 >= 0) {
                Bird bird = Bird.create(terrainGrid.isAllLandTile((int)unitPosition.getX(), (int)unitPosition.getY()) ? BirdType.Sparrows : BirdType.Seagulls);
                bird.setAngle(1.5707964f * (float)((int)world.getGameTime() % 4) + (world.getGameTime() - (float)((int)world.getGameTime())) * 0.7853982f - 0.3926991f);
                bird.setPosition(new Vec3(unitPosition.getX() + (float)NEIGHBOR_OFFSETS[i4][0] + 0.5f, unitPosition.getY() + (float)NEIGHBOR_OFFSETS[i4][1] + 0.5f, 0.0f));
                world.getBirds().add(bird);
            }
        }
    }

    private static final strictfp UnitList collectNearbyUnits(UnitList unitList, TerrainGrid terrainGrid, int i2, int i3) {
        int i4 = i3 - 1;
        while (i4 <= i3 + 1) {
            int i5 = i2 - 1;
            while (i5 <= i2 + 1) {
                if (terrainGrid.isInsideGrid((float)i5, (float)i4)) {
                    unitList.addAll((Collection)terrainGrid.getUnitsAtTile(i5, i4));
                }
                ++i5;
            }
            ++i4;
        }
        return unitList;
    }

    private static final strictfp Unit findEmbarkTarget(World world, Unit unit) {
        TerrainGrid terrainGrid = world.getTerrainGrid();
        Unit unit2 = null;
        float f4 = Float.MAX_VALUE;
        UnitPosition unitPosition = unit.getPosition();
        UnitList unitList = world.getUnits();
        int i7 = 0;
        while (i7 < unitList.size()) {
            float f9;
            Unit unit3 = (Unit)unitList.get(i7);
            if (unit3.canEmbark(unit) && terrainGrid.hasPathToUnit(unit, unit3) && (f9 = unit3.getPosition().distanceSquaredTo(unitPosition)) < f4) {
                unit2 = unit3;
                f4 = f9;
            }
            ++i7;
        }
        return unit2;
    }

    private static final strictfp Unit findAttackTarget(World world, Unit unit) {
        UnitPosition unitPosition = unit.getPosition();
        boolean i3 = unitPosition.getUnit() != null;
        TerrainGrid terrainGrid = world.getTerrainGrid();
        int i5 = terrainGrid.getWidth();
        int i6 = terrainGrid.getHeight();
        float f7 = unit.getPosition().getX();
        float f8 = unit.getPosition().getY();
        int i9 = (int)f7;
        int i10 = (int)f8;
        int i11 = unit.getUnitType().getRangeInTiles();
        float f12 = i11;
        float f13 = f12 * f12;
        int i14 = 0;
        Object object = null;
        int i16 = 1;
        while (i16 < i11) {
            int i17 = i16 << 1;
            int i18 = i16 << 2;
            int i19 = i17 + i18;
            int i20 = i9 - i16;
            int i21 = i10 - i16;
            int i22 = i16 << 3;
            int i23 = 0;
            while (i23 < i22) {
                if (i20 >= 0 && i20 < i5 && i21 >= 0 && i21 < i6) {
                    int i25;
                    Object object2;
                    if (!i3 && (object2 = terrainGrid.getUnitAtTile(i20, i21)) != null && ((Unit)object2).getPosition().distanceSquaredTo(unitPosition) < f13 && (i25 = unit.getUnitType().getDamageAgainst(((Unit)object2).getUnitType())) > i14 && unit.canAttackUnit((Unit)object2)) {
                        object = object2;
                        i14 = i25;
                    }
                    object2 = terrainGrid.getUnitsAtTile(i20, i21);
                    i25 = ((ArrayList)object2).size();
                    int i26 = 0;
                    while (i26 < i25) {
                        int i28;
                        Unit unit2 = (Unit)((ArrayList)object2).get(i26);
                        if (unit2.getPosition().distanceSquaredTo(unitPosition) < f13 && (i28 = unit.getUnitType().getDamageAgainst(unit2.getUnitType())) > i14 && unit.canAttackUnit(unit2)) {
                            object = unit2;
                            i14 = i28;
                        }
                        ++i26;
                    }
                }
                if (i23 < i17) {
                    ++i20;
                } else if (i23 < i18) {
                    ++i21;
                } else if (i23 < i19) {
                    --i20;
                } else {
                    --i21;
                }
                ++i23;
            }
            ++i16;
        }
        return (Unit)object;
    }

    private static final strictfp Unit findRepairUnit(World world, Unit unit) {
        Player player = unit.getOwner();
        Domain domain = unit.getUnitType().getDomain();
        UnitPosition unitPosition = unit.getPosition();
        TerrainGrid terrainGrid = world.getTerrainGrid();
        int i6 = terrainGrid.getWidth();
        int i7 = terrainGrid.getHeight();
        int i8 = (int)unit.getPosition().getX();
        int i9 = (int)unit.getPosition().getY();
        int i10 = 3;
        float f11 = i10;
        float f12 = f11 * f11;
        int i13 = i9 - i10;
        while (i13 <= i9 + i10) {
            int i14 = i8 - i10;
            while (i14 <= i8 + i10) {
                if (i14 >= 0 && i14 < i6 && i13 >= 0 && i13 < i7) {
                    UnitList unitList = terrainGrid.getUnitsAtTile(i14, i13);
                    int i16 = unitList.size();
                    int i17 = 0;
                    while (i17 < i16) {
                        Unit unit2 = (Unit)unitList.get(i17);
                        UnitType unitType = unit2.getUnitType();
                        if (unitType.canRepair() && domain == unitType.getDomain() && player == unit2.getOwner() && unit2.getPosition().distanceSquaredTo(unitPosition) < f12) {
                            return unit2;
                        }
                        ++i17;
                    }
                }
                ++i14;
            }
            ++i13;
        }
        return null;
    }
}


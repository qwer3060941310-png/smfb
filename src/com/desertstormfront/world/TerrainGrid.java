/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.desertstormfront.app.support.ProgressCallback;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Site;
import com.desertstormfront.game.model.TerrainType;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.world.DirectionFieldPathFinder;
import com.desertstormfront.world.GridCell;
import com.desertstormfront.world.Neighbor;
import com.desertstormfront.world.PathFinder;
import com.desertstormfront.world.RoadTile;
import com.desertstormfront.world.TerrainGridAccessor;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.math.MathHelper;
import java.util.ArrayList;

/**
 * The terrain of one world: a fixed-size grid of {@link GridCell}s together with the derived data
 * that movement and placement need.
 *
 * <p>A grid is built once per map - from the map definition, reporting progress through a
 * {@link ProgressCallback} while loading - and is afterwards read far more often than it is
 * written. That asymmetry is why the rest of the game reaches it through the narrow
 * {@link TerrainGridAccessor} interface rather than through this class.
 *
 * <p>Next to the cells it holds the supporting data callers query: a {@link PathFinder} and the
 * {@link DirectionFieldPathFinder} behind flow-field movement, plus road tiles, terrain types and
 * building sites.
 */
public strictfp final class TerrainGrid
implements TerrainGridAccessor {
    private int width;
    private int height;
    private boolean[] c;
    private int d;
    private int e;
    private GridCell[] cells;
    private Vec2[] g;
    private PathFinder pathFinder;
    private Vec2[] i;
    private Vec2[] j;
    private Vec2[][] k;
    private static /* synthetic */ int[] l;

    public TerrainGrid(int i1, int i2) {
        this.width = i1;
        this.height = i2;
        this.d = i1 + 1;
        this.e = i2 + 1;
        this.c = new boolean[this.e * this.d];
        this.cells = new GridCell[i2 * i1];
        this.g = new Vec2[i2 * i1];
        int i3 = 0;
        while (i3 < i2) {
            int i4 = 0;
            while (i4 < i1) {
                this.cells[i3 * i1 + i4] = new GridCell();
                this.g[i3 * i1 + i4] = new Vec2((float)i4 + 0.5f, (float)i3 + 0.5f);
                ++i4;
            }
            ++i3;
        }
    }

    public final void buildTerrain(ProgressCallback progressCallback) {
        this.buildTerrainWithPathFinder(new DirectionFieldPathFinder(), progressCallback);
    }

    public final void buildTerrainWithPathFinder(PathFinder pathFinder, ProgressCallback progressCallback) {
        int i5;
        int i4;
        this.pathFinder = pathFinder;
        this.pathFinder.build(this, progressCallback);
        int n = 0;
        while (n < this.height) {
            i4 = 0;
            while (i4 < this.width) {
                if (this.c[n * this.d + i4] && !this.c[n * this.d + (i4 + 1)] && !this.c[(n + 1) * this.d + i4] && this.c[(n + 1) * this.d + (i4 + 1)] || !this.c[n * this.d + i4] && this.c[n * this.d + (i4 + 1)] && this.c[(n + 1) * this.d + i4] && !this.c[(n + 1) * this.d + (i4 + 1)]) {
                    OsfLog.error("Illegal tile configuration at (" + i4 + ", " + n + ")");
                }
                ++i4;
            }
            ++n;
        }
        this.i = new Vec2[this.height * this.width];
        this.j = new Vec2[this.height * this.width];
        n = 0;
        while (n < this.height) {
            i4 = 0;
            while (i4 < this.width) {
                i5 = 0;
                while (i5 < Domain.values().length) {
                    int n2;
                    int n3;
                    Unit unit;
                    if (this.canDomainEnter(Domain.Ground, i4, n)) {
                        this.i[n * this.width + i4] = this.g[n * this.width + i4];
                    } else if (this.getUnitAtTile(i4, n) != null) {
                        unit = this.getUnitAtTile(i4, n);
                        if (unit.getUnitType().getProducedBy().hasDomain(Domain.Water)) {
                            n3 = unit.getDirection().getOpposite().getDx();
                            n2 = unit.getDirection().getOpposite().getDy();
                            this.i[n * this.width + i4] = this.g[(n + n2) * this.width + (i4 + n3)];
                        } else {
                            n3 = unit.getDirection().getDx();
                            n2 = unit.getDirection().getDy();
                            this.i[n * this.width + i4] = this.g[(n + n2) * this.width + (i4 + n3)];
                        }
                    } else {
                        this.i[n * this.width + i4] = null;
                    }
                    if (this.canDomainEnter(Domain.Water, i4, n)) {
                        this.j[n * this.width + i4] = this.g[n * this.width + i4];
                    } else if (this.getUnitAtTile(i4, n) != null) {
                        unit = this.getUnitAtTile(i4, n);
                        if (unit.getUnitType().getProducedBy().hasDomain(Domain.Water)) {
                            n3 = unit.getDirection().getDx();
                            n2 = unit.getDirection().getDy();
                            this.j[n * this.width + i4] = this.g[(n + n2) * this.width + (i4 + n3)];
                        } else {
                            this.j[n * this.width + i4] = null;
                        }
                    } else {
                        this.j[n * this.width + i4] = null;
                    }
                    ++i5;
                }
                ++i4;
            }
            ++n;
        }
        ArrayList<Vec2> arrayList = new ArrayList<Vec2>();
        this.k = new Vec2[this.height * this.width][];
        i4 = this.height >= this.width ? this.height : this.width;
        i5 = 0;
        while (i5 < this.height) {
            int n4 = 0;
            while (n4 < this.width) {
                int i9;
                arrayList.clear();
                Vec2 vec2 = this.i[i5 * this.width + n4];
                if (vec2 != null) {
                    int n5 = 1;
                    while (n5 < i4) {
                        i9 = n5 << 1;
                        int i10 = n5 << 2;
                        int i11 = i9 + i10;
                        int i12 = n4 - n5;
                        int i13 = i5 - n5;
                        int i14 = n5 << 3;
                        int i15 = 0;
                        while (i15 < i14) {
                            if (i12 >= 0 && i12 < this.width && i13 >= 0 && i13 < this.height && this.isCoastTile(i12, i13) && this.canDomainEnter(Domain.Ground, i12, i13) && this.hasPathForDomain(Domain.Ground, vec2.getX(), vec2.getY(), (float)i12, (float)i13)) {
                                arrayList.add(this.g[i13 * this.width + i12]);
                                if (arrayList.size() >= 12) break;
                            }
                            if (i15 < i9) {
                                ++i12;
                            } else if (i15 < i10) {
                                ++i13;
                            } else if (i15 < i11) {
                                --i12;
                            } else {
                                --i13;
                            }
                            ++i15;
                        }
                        ++n5;
                    }
                }
                if (arrayList.size() > 0) {
                    Vec2[] vec2Array = new Vec2[arrayList.size()];
                    i9 = 0;
                    while (i9 < vec2Array.length) {
                        vec2Array[i9] = (Vec2)arrayList.get(i9);
                        ++i9;
                    }
                    this.k[i5 * this.width + n4] = vec2Array;
                } else {
                    this.k[i5 * this.width + n4] = null;
                }
                ++n4;
            }
            ++i5;
        }
    }

    public final void shareFrom(TerrainGrid terrainGrid) {
        this.pathFinder = terrainGrid.pathFinder;
        this.i = terrainGrid.i;
        this.j = terrainGrid.j;
        this.k = terrainGrid.k;
    }

    @Override
    public final int getWidth() {
        return this.width;
    }

    @Override
    public final int getHeight() {
        return this.height;
    }

    public final Vec2 getCenter(int i1, int i2) {
        return this.g[i2 * this.width + i1];
    }

    public final RoadTile getRoadTile(int i1, int i2) {
        return this.cells[i2 * this.width + i1].getRoadTile();
    }

    public final void setRoadTile(int i1, int i2, RoadTile roadTile) {
        this.cells[i2 * this.width + i1].setRoadTile(roadTile);
    }

    public final Site getSite(int i1, int i2) {
        return this.cells[i2 * this.width + i1].getSite();
    }

    public final void setSite(int i1, int i2, TerrainType terrainType, int i4) {
        this.cells[i2 * this.width + i1].setSite(Site.of(terrainType, i4, this.g[i2 * this.width + i1]));
    }

    public final Unit getUnitAtPosition(Vec2 vec2) {
        return this.cells[(int)vec2.getY() * this.width + (int)vec2.getX()].getUnit();
    }

    public final Unit getUnitAtTile(int i1, int i2) {
        return this.cells[i2 * this.width + i1].getUnit();
    }

    public final void setUnitAtPosition(Vec2 vec2, Unit unit) {
        this.cells[(int)vec2.getY() * this.width + (int)vec2.getX()].setUnit(unit);
    }

    public final void setUnitAtTile(int i1, int i2, Unit unit) {
        this.cells[i2 * this.width + i1].setUnit(unit);
    }

    public final Vec2 getDomainPosition(Vec2 vec2, Domain domain) {
        return this.getDomainPositionAtTile((int)vec2.getX(), (int)vec2.getY(), domain);
    }

    public final Vec2 getDomainPositionAtTile(int i1, int i2, Domain domain) {
        switch (TerrainGrid.getDomainSwitchMap()[domain.ordinal()]) {
            case 1: {
                return this.i[i2 * this.width + i1];
            }
            case 2: {
                return this.j[i2 * this.width + i1];
            }
        }
        return this.g[i2 * this.width + i1];
    }

    public final UnitList getUnitsAtTile(int i1, int i2) {
        return this.cells[i2 * this.width + i1].getUnits();
    }

    public final void addUnitToTile(Unit unit) {
        UnitPosition unitPosition = unit.getPosition();
        this.cells[(int)((Vec2)unitPosition).getY() * this.width + (int)((Vec2)unitPosition).getX()].addUnit(unit);
    }

    public final void removeUnitFromTile(Unit unit) {
        UnitPosition unitPosition = unit.getPosition();
        this.cells[(int)((Vec2)unitPosition).getY() * this.width + (int)((Vec2)unitPosition).getX()].removeUnit(unit);
    }

    public final boolean isInsideGrid(float f1, float f2) {
        return f1 >= 0.0f && f2 >= 0.0f && f1 < (float)this.width && f2 < (float)this.height;
    }

    public final boolean isCellBlocked(float f1, float f2) {
        return this.cells[(int)f2 * this.width + (int)f1].isBlocked();
    }

    public final boolean isCornerLand(int i1, int i2) {
        return this.c[i2 * this.d + i1];
    }

    public final void setCornerLand(int i1, int i2, boolean bl) {
        this.c[i2 * this.d + i1] = bl;
    }

    public final boolean isAllLandTileAtPosition(Vec2 vec2) {
        return this.isAllLandTile((int)vec2.getX(), (int)vec2.getY());
    }

    public final boolean isAllLandTile(int i1, int i2) {
        return this.c[i2 * this.d + i1] && this.c[i2 * this.d + (i1 + 1)] && this.c[(i2 + 1) * this.d + i1] && this.c[(i2 + 1) * this.d + (i1 + 1)];
    }

    public final boolean isAllWaterTileAtPosition(Vec2 vec2) {
        return this.isAllWaterTile((int)vec2.getX(), (int)vec2.getY());
    }

    public final boolean isAllWaterTile(int i1, int i2) {
        return !this.c[i2 * this.d + i1] && !this.c[i2 * this.d + (i1 + 1)] && !this.c[(i2 + 1) * this.d + i1] && !this.c[(i2 + 1) * this.d + (i1 + 1)];
    }

    public final boolean isCoastTile(int i1, int i2) {
        return this.c[i2 * this.d + i1] ^ this.c[i2 * this.d + (i1 + 1)] || this.c[i2 * this.d + i1] ^ this.c[(i2 + 1) * this.d + i1] || this.c[(i2 + 1) * this.d + i1] ^ this.c[(i2 + 1) * this.d + (i1 + 1)];
    }

    public final boolean isLandAtPoint(float f1, float f2) {
        int i3 = (int)f1;
        int i4 = (int)f2;
        int i5 = i3 + 1;
        int i6 = i4 + 1;
        int i7 = (this.c[i4 * this.d + i3] ? 0 : 1) + (this.c[i4 * this.d + i5] ? 0 : 2) + (this.c[i6 * this.d + i5] ? 0 : 4) + (this.c[i6 * this.d + i3] ? 0 : 8);
        switch (i7) {
            case 1: {
                float f8 = f1 - (float)i3;
                float f9 = f2 - (float)i4;
                float f10 = f8 * f8 + f9 * f9;
                return f10 > 0.25f;
            }
            case 2: {
                float f8 = f1 - (float)i5;
                float f9 = f2 - (float)i4;
                float f10 = f8 * f8 + f9 * f9;
                return f10 > 0.25f;
            }
            case 3: {
                return f2 - (float)i4 > 0.5f;
            }
            case 4: {
                float f8 = f1 - (float)i5;
                float f9 = f2 - (float)i6;
                float f10 = f8 * f8 + f9 * f9;
                return f10 > 0.25f;
            }
            case 6: {
                return (float)i5 - f1 > 0.5f;
            }
            case 7: {
                float f8 = f1 - (float)i3;
                float f9 = f2 - (float)i6;
                float f10 = f8 * f8 + f9 * f9;
                return f10 < 0.25f;
            }
            case 8: {
                float f8 = f1 - (float)i3;
                float f9 = f2 - (float)i6;
                float f10 = f8 * f8 + f9 * f9;
                return f10 > 0.25f;
            }
            case 9: {
                return f1 - (float)i3 > 0.5f;
            }
            case 11: {
                float f8 = f1 - (float)i5;
                float f9 = f2 - (float)i6;
                float f10 = f8 * f8 + f9 * f9;
                return f10 < 0.25f;
            }
            case 12: {
                return (float)i6 - f2 > 0.5f;
            }
            case 13: {
                float f8 = f1 - (float)i5;
                float f9 = f2 - (float)i4;
                float f10 = f8 * f8 + f9 * f9;
                return f10 < 0.25f;
            }
            case 14: {
                float f8 = f1 - (float)i3;
                float f9 = f2 - (float)i4;
                float f10 = f8 * f8 + f9 * f9;
                return f10 < 0.25f;
            }
        }
        return true;
    }

    public final float getCoastAngle(int i1, int i2) {
        int i3 = (this.c[i2 * this.d + i1] ? 1 : 0) + (this.c[i2 * this.d + (i1 + 1)] ? 2 : 0) + (this.c[(i2 + 1) * this.d + (i1 + 1)] ? 4 : 0) + (this.c[(i2 + 1) * this.d + i1] ? 8 : 0);
        switch (i3) {
            case 1: {
                return 0.7853982f;
            }
            case 2: {
                return 2.3561945f;
            }
            case 3: {
                return 1.5707964f;
            }
            case 4: {
                return 3.9269907f;
            }
            case 6: {
                return (float)Math.PI;
            }
            case 7: {
                return 2.3561945f;
            }
            case 8: {
                return 5.497787f;
            }
            case 9: {
                return 0.0f;
            }
            case 11: {
                return 0.7853982f;
            }
            case 12: {
                return 4.712389f;
            }
            case 13: {
                return 5.497787f;
            }
            case 14: {
                return 3.9269907f;
            }
        }
        return 0.0f;
    }

    /*
     * Enabled aggressive block sorting
     */
    public final void clampPositionToDomain(float[] fArray, Domain domain, float f3, float f4) {
        switch (TerrainGrid.getDomainSwitchMap()[domain.ordinal()]) {
            case 1: {
                int i5 = (int)f3;
                int i6 = (int)f4;
                int i7 = i5 + 1;
                int i8 = i6 + 1;
                int i9 = (this.c[i6 * this.d + i5] ? 1 : 0) + (this.c[i6 * this.d + i7] ? 2 : 0) + (this.c[i8 * this.d + i7] ? 4 : 0) + (this.c[i8 * this.d + i5] ? 8 : 0);
                switch (i9) {
                    case 1: {
                        float f10 = f3 - (float)i5;
                        float f11 = f4 - (float)i6;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 > 0.0625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i5 + f10 * 0.25f / f12;
                        f4 = (float)i6 + f11 * 0.25f / f12;
                        break;
                    }
                    case 2: {
                        float f10 = f3 - (float)i7;
                        float f11 = f4 - (float)i6;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 > 0.0625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i7 + f10 * 0.25f / f12;
                        f4 = (float)i6 + f11 * 0.25f / f12;
                        break;
                    }
                    case 3: {
                        if (!(f4 - (float)i6 > 0.25f)) break;
                        f4 = (float)i6 + 0.25f;
                        break;
                    }
                    case 4: {
                        float f10 = f3 - (float)i7;
                        float f11 = f4 - (float)i8;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 > 0.0625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i7 + f10 * 0.25f / f12;
                        f4 = (float)i8 + f11 * 0.25f / f12;
                        break;
                    }
                    case 6: {
                        if (!((float)i7 - f3 > 0.25f)) break;
                        f3 = (float)i7 - 0.25f;
                        break;
                    }
                    case 7: {
                        float f10 = f3 - (float)i5;
                        float f11 = f4 - (float)i8;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 < 0.5625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i5 + f10 * 0.75f / f12;
                        f4 = (float)i8 + f11 * 0.75f / f12;
                        break;
                    }
                    case 8: {
                        float f10 = f3 - (float)i5;
                        float f11 = f4 - (float)i8;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 > 0.0625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i5 + f10 * 0.25f / f12;
                        f4 = (float)i8 + f11 * 0.25f / f12;
                        break;
                    }
                    case 9: {
                        if (!(f3 - (float)i5 > 0.25f)) break;
                        f3 = (float)i5 + 0.25f;
                        break;
                    }
                    case 11: {
                        float f10 = f3 - (float)i7;
                        float f11 = f4 - (float)i8;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 < 0.5625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i7 + f10 * 0.75f / f12;
                        f4 = (float)i8 + f11 * 0.75f / f12;
                        break;
                    }
                    case 12: {
                        if (!((float)i8 - f4 > 0.25f)) break;
                        f4 = (float)i8 - 0.25f;
                        break;
                    }
                    case 13: {
                        float f10 = f3 - (float)i7;
                        float f11 = f4 - (float)i6;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 < 0.5625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i7 + f10 * 0.75f / f12;
                        f4 = (float)i6 + f11 * 0.75f / f12;
                        break;
                    }
                    case 14: {
                        float f10 = f3 - (float)i5;
                        float f11 = f4 - (float)i6;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 < 0.5625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i5 + f10 * 0.75f / f12;
                        f4 = (float)i6 + f11 * 0.75f / f12;
                        break;
                    }
                }
                break;
            }
            case 2: {
                int i5 = (int)f3;
                int i6 = (int)f4;
                int i7 = i5 + 1;
                int i8 = i6 + 1;
                int i9 = (this.c[i6 * this.d + i5] ? 0 : 1) + (this.c[i6 * this.d + i7] ? 0 : 2) + (this.c[i8 * this.d + i7] ? 0 : 4) + (this.c[i8 * this.d + i5] ? 0 : 8);
                switch (i9) {
                    case 1: {
                        float f10 = f3 - (float)i5;
                        float f11 = f4 - (float)i6;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 > 0.0625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i5 + f10 * 0.25f / f12;
                        f4 = (float)i6 + f11 * 0.25f / f12;
                        break;
                    }
                    case 2: {
                        float f10 = f3 - (float)i7;
                        float f11 = f4 - (float)i6;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 > 0.0625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i7 + f10 * 0.25f / f12;
                        f4 = (float)i6 + f11 * 0.25f / f12;
                        break;
                    }
                    case 3: {
                        if (!(f4 - (float)i6 > 0.25f)) break;
                        f4 = (float)i6 + 0.25f;
                        break;
                    }
                    case 4: {
                        float f10 = f3 - (float)i7;
                        float f11 = f4 - (float)i8;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 > 0.0625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i7 + f10 * 0.25f / f12;
                        f4 = (float)i8 + f11 * 0.25f / f12;
                        break;
                    }
                    case 6: {
                        if (!((float)i7 - f3 > 0.25f)) break;
                        f3 = (float)i7 - 0.25f;
                        break;
                    }
                    case 7: {
                        float f10 = f3 - (float)i5;
                        float f11 = f4 - (float)i8;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 < 0.5625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i5 + f10 * 0.75f / f12;
                        f4 = (float)i8 + f11 * 0.75f / f12;
                        break;
                    }
                    case 8: {
                        float f10 = f3 - (float)i5;
                        float f11 = f4 - (float)i8;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 > 0.0625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i5 + f10 * 0.25f / f12;
                        f4 = (float)i8 + f11 * 0.25f / f12;
                        break;
                    }
                    case 9: {
                        if (!(f3 - (float)i5 > 0.25f)) break;
                        f3 = (float)i5 + 0.25f;
                        break;
                    }
                    case 11: {
                        float f10 = f3 - (float)i7;
                        float f11 = f4 - (float)i8;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 < 0.5625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i7 + f10 * 0.75f / f12;
                        f4 = (float)i8 + f11 * 0.75f / f12;
                        break;
                    }
                    case 12: {
                        if (!((float)i8 - f4 > 0.25f)) break;
                        f4 = (float)i8 - 0.25f;
                        break;
                    }
                    case 13: {
                        float f10 = f3 - (float)i7;
                        float f11 = f4 - (float)i6;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 < 0.5625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i7 + f10 * 0.75f / f12;
                        f4 = (float)i6 + f11 * 0.75f / f12;
                        break;
                    }
                    case 14: {
                        float f10 = f3 - (float)i5;
                        float f11 = f4 - (float)i6;
                        float f12 = f10 * f10 + f11 * f11;
                        if (!(f12 < 0.5625f)) break;
                        f12 = MathHelper.sqrt(f12);
                        f3 = (float)i5 + f10 * 0.75f / f12;
                        f4 = (float)i6 + f11 * 0.75f / f12;
                    }
                }
                break;
            }
        }
        fArray[0] = f3;
        fArray[1] = f4;
    }

    public final boolean canDomainEnter(Domain domain, int i2, int i3) {
        switch (TerrainGrid.getDomainSwitchMap()[domain.ordinal()]) {
            case 1: {
                return !this.isCellBlocked((float)i2, (float)i3) && !this.isAllWaterTile(i2, i3);
            }
            case 2: {
                return !this.isCellBlocked((float)i2, (float)i3) && !this.isAllLandTile(i2, i3);
            }
            case 4: {
                return !this.isCellBlocked((float)i2, (float)i3);
            }
        }
        return true;
    }

    @Override
    public final boolean canDomainMoveBetween(Domain domain, int i2, int i3, int i4, int i5) {
        if (!this.canDomainEnter(domain, i4, i5)) {
            return false;
        }
        int i6 = i4 - i2;
        int i7 = i5 - i3;
        if (i6 != 0 && i7 != 0) {
            int i8 = i2 + i6;
            int i9 = i3;
            if (!this.canDomainEnter(domain, i8, i9)) {
                return false;
            }
            i8 = i2;
            i9 = i3 + i7;
            if (!this.canDomainEnter(domain, i8, i9)) {
                return false;
            }
        }
        switch (TerrainGrid.getDomainSwitchMap()[domain.ordinal()]) {
            case 1: {
                if (i3 == i5) {
                    if (i2 == i4) {
                        return true;
                    }
                    if (i2 < i4) {
                        return this.c[i3 * this.d + i4] || this.c[(i3 + 1) * this.d + i4];
                    }
                    return this.c[i3 * this.d + i2] || this.c[(i3 + 1) * this.d + i2];
                }
                if (i2 == i4) {
                    if (i3 < i5) {
                        return this.c[i5 * this.d + i2] || this.c[i5 * this.d + (i2 + 1)];
                    }
                    return this.c[i3 * this.d + i2] || this.c[i3 * this.d + (i2 + 1)];
                }
                if (i2 < i4) {
                    if (i3 < i5) {
                        return this.c[i5 * this.d + i4];
                    }
                    return this.c[i3 * this.d + i4];
                }
                if (i3 < i5) {
                    return this.c[i5 * this.d + i2];
                }
                return this.c[i3 * this.d + i2];
            }
            case 2: {
                if (i3 == i5) {
                    if (i2 == i4) {
                        return true;
                    }
                    if (i2 < i4) {
                        return !this.c[i3 * this.d + i4] || !this.c[(i3 + 1) * this.d + i4];
                    }
                    return !this.c[i3 * this.d + i2] || !this.c[(i3 + 1) * this.d + i2];
                }
                if (i2 == i4) {
                    if (i3 < i5) {
                        return !this.c[i5 * this.d + i2] || !this.c[i5 * this.d + (i2 + 1)];
                    }
                    return !this.c[i3 * this.d + i2] || !this.c[i3 * this.d + (i2 + 1)];
                }
                if (i2 < i4) {
                    if (i3 < i5) {
                        return !this.c[i5 * this.d + i4];
                    }
                    return !this.c[i3 * this.d + i4];
                }
                if (i3 < i5) {
                    return !this.c[i5 * this.d + i2];
                }
                return !this.c[i3 * this.d + i2];
            }
        }
        return true;
    }

    @Override
    public final int getMovementCost(int i1, int i2, int i3, int i4) {
        if (i1 != i3 && i2 != i4) {
            return 14142;
        }
        return 10000;
    }

    public final Vec2[] getSurroundingTiles(Vec2 vec2) {
        return this.k[(int)vec2.getY() * this.width + (int)vec2.getX()];
    }

    public final Vec2 getNearestSurroundingTile(Vec2 vec2, Vec2 vec22) {
        Vec2[] vec2Array = this.k[(int)vec2.getY() * this.width + (int)vec2.getX()];
        if (vec2Array != null) {
            Vec2 vec23 = vec2Array[0];
            float f5 = vec23.distanceSquaredTo(vec22);
            int i6 = 1;
            while (i6 < vec2Array.length) {
                float f7 = vec2Array[i6].distanceSquaredTo(vec22);
                if (f7 < f5) {
                    f5 = f7;
                    vec23 = vec2Array[i6];
                }
                ++i6;
            }
            return vec23;
        }
        return null;
    }

    public final boolean hasPathToUnit(Unit unit, Unit unit2) {
        return this.findNeighbor(unit.getUnitType().getDomain(), unit.getPosition().getX(), unit.getPosition().getY(), unit2.getPosition().getX(), unit2.getPosition().getY()).isValid();
    }

    public final boolean hasPathToPosition(Unit unit, float f2, float f3) {
        return this.findNeighbor(unit.getUnitType().getDomain(), unit.getPosition().getX(), unit.getPosition().getY(), f2, f3).isValid();
    }

    public final boolean hasPathForDomain(Domain domain, float f2, float f3, float f4, float f5) {
        return this.findNeighbor(domain, f2, f3, f4, f5).isValid();
    }

    public final Neighbor findNeighbor(Domain domain, float f2, float f3, float f4, float f5) {
        Vec2 vec2;
        Vec2 vec22;
        switch (TerrainGrid.getDomainSwitchMap()[domain.ordinal()]) {
            case 1: {
                vec22 = this.i[(int)f3 * this.width + (int)f2];
                vec2 = this.i[(int)f5 * this.width + (int)f4];
                break;
            }
            case 2: {
                vec22 = this.j[(int)f3 * this.width + (int)f2];
                vec2 = this.j[(int)f5 * this.width + (int)f4];
                break;
            }
            case 4: {
                vec22 = this.isAllLandTile((int)f2, (int)f3) ? this.i[(int)f3 * this.width + (int)f2] : this.j[(int)f3 * this.width + (int)f2];
                if (this.isAllLandTile((int)f4, (int)f5)) {
                    vec2 = this.i[(int)f5 * this.width + (int)f4];
                    break;
                }
                vec2 = this.j[(int)f5 * this.width + (int)f4];
                break;
            }
            default: {
                vec22 = this.g[(int)f3 * this.width + (int)f2];
                vec2 = this.g[(int)f5 * this.width + (int)f4];
            }
        }
        if (vec22 != null && vec2 != null) {
            Neighbor neighbor = this.pathFinder.findDirection(domain, (int)vec22.getX(), (int)vec22.getY(), (int)vec2.getX(), (int)vec2.getY());
            if (neighbor.isValid()) {
                if ((int)vec22.getX() == (int)f2 && (int)vec22.getY() == (int)f3) {
                    return neighbor;
                }
                return Neighbor.of((int)vec22.getX() - (int)f2, (int)vec22.getY() - (int)f3);
            }
            if (domain == Domain.Amphibian) {
                if (this.isAllLandTile((int)vec22.getX(), (int)vec22.getY()) ? (vec2 = this.getNearestSurroundingTile(vec22, vec2)) == null : (vec2 = this.getNearestSurroundingTile(vec2, vec2)) == null) {
                    return Neighbor.None;
                }
                neighbor = this.pathFinder.findDirection(domain, (int)vec22.getX(), (int)vec22.getY(), (int)vec2.getX(), (int)vec2.getY());
                if (neighbor.isValid()) {
                    if ((int)vec22.getX() == (int)f2 && (int)vec22.getY() == (int)f3) {
                        return neighbor;
                    }
                    return Neighbor.of((int)vec22.getX() - (int)f2, (int)vec22.getY() - (int)f3);
                }
                return Neighbor.None;
            }
            return Neighbor.None;
        }
        return Neighbor.None;
    }

    public final Neighbor findReachableNeighbor(Domain domain, float f2, float f3, float f4, float f5) {
        Neighbor neighbor = this.findNeighbor(domain, f2, f3, f4, f5);
        if (!neighbor.isValid()) {
            float f7 = f4 - f2;
            float f8 = f5 - f3;
            float f9 = MathHelper.sqrt(f7 * f7 + f8 * f8);
            float f10 = f9 - 0.5f;
            while ((double)f10 >= 0.5) {
                float f11 = f2 + f10 * f7 / f9;
                float f12 = f3 + f10 * f8 / f9;
                neighbor = this.findNeighbor(domain, f2, f3, f11, f12);
                if (neighbor.isValid()) {
                    return neighbor;
                }
                f10 -= 0.5f;
            }
            return Neighbor.None;
        }
        return neighbor;
    }

    static /* synthetic */ int[] getDomainSwitchMap() {
        if (l != null) {
            return l;
        }
        int[] nArray = new int[Domain.values().length];
        try {
            nArray[Domain.Air.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[Domain.Amphibian.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[Domain.Ground.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[Domain.Water.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        l = nArray;
        return nArray;
    }
}


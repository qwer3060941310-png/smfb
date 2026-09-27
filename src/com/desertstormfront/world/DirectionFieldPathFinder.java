/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.desertstormfront.app.support.ProgressCallback;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.world.DijkstraNode;
import com.desertstormfront.world.Neighbor;
import com.desertstormfront.world.PathFinder;
import com.desertstormfront.world.SortedNodeQueue;
import com.desertstormfront.world.TerrainGridAccessor;
import com.noblemaster.lib.math.MathHelper;

public strictfp final class DirectionFieldPathFinder
implements PathFinder {
    private byte[] a;
    private int b;
    private int c;
    private int d;
    private static /* synthetic */ int[] e;

    @Override
    public final void build(TerrainGridAccessor terrainGridAccessor, ProgressCallback progressCallback) {
        int i18;
        int n;
        int i15;
        Domain domain;
        int i13;
        int i12;
        if (progressCallback != null) {
            progressCallback.onProgress(0.0f);
        }
        int i3 = terrainGridAccessor.getWidth();
        int i4 = terrainGridAccessor.getHeight();
        this.a = new byte[i3 * i4 * i3 * i4];
        this.b = i3 * i4 * i3;
        this.c = i3 * i4;
        this.d = i3;
        DijkstraNode[] dijkstraNodeArray = new DijkstraNode[i4 * i3];
        int n2 = 0;
        while (n2 < i4) {
            int n3 = 0;
            while (n3 < i3) {
                dijkstraNodeArray[n2 * i3 + n3] = new DijkstraNode(n3, n2, null);
                ++n3;
            }
            ++n2;
        }
        SortedNodeQueue sortedNodeQueue = new SortedNodeQueue(null);
        boolean[] blArray = new boolean[i4 * i3];
        boolean[] blArray2 = new boolean[i4 * i3];
        Domain[] domainArray = new Domain[]{Domain.Ground, Domain.Water};
        short[][] sArray = new short[domainArray.length][i3 * i4];
        int i11 = 0;
        while (i11 < i4) {
            i12 = 0;
            while (i12 < i3) {
                i13 = 0;
                while (i13 < domainArray.length) {
                    domain = domainArray[i13];
                    i15 = -1;
                    while (i15 <= 1) {
                        int n4 = -1;
                        while (n4 <= 1) {
                            n = i12 + i15;
                            i18 = i11 + n4;
                            if (n >= 0 && n < i3 && i18 >= 0 && i18 < i4 && terrainGridAccessor.canDomainMoveBetween(domain, i12, i11, n, i18)) {
                                sArray[i13][i11 * i3 + i12] = (short)(sArray[i13][i11 * i3 + i12] | 1 << (n4 + 1 << 2) + (i15 + 1));
                            }
                            ++n4;
                        }
                        ++i15;
                    }
                    ++i13;
                }
                ++i12;
            }
            ++i11;
        }
        i11 = 0;
        while (i11 < i4) {
            i12 = 0;
            while (i12 < i3) {
                if (progressCallback != null) {
                    progressCallback.onProgress((float)(i11 * i3 + i12) / ((float)(i3 * i4) + 1.0f));
                }
                i13 = 0;
                while (i13 < domainArray.length) {
                    domain = domainArray[i13];
                    int n5 = i15 = domain == Domain.Ground ? 0 : 4;
                    if ((sArray[i13][i11 * i3 + i12] & 0x20) != 0) {
                        int i25;
                        int i23;
                        int i22;
                        int i20;
                        int n6 = 0;
                        while (n6 < i4) {
                            n = 0;
                            while (n < i3) {
                                DijkstraNode.setCost(dijkstraNodeArray[n6 * i3 + n], Integer.MAX_VALUE);
                                blArray[n6 * i3 + n] = false;
                                blArray2[n6 * i3 + n] = false;
                                ++n;
                            }
                            ++n6;
                        }
                        DijkstraNode dijkstraNode = dijkstraNodeArray[i11 * i3 + i12];
                        DijkstraNode.setCost(dijkstraNode, 0);
                        sortedNodeQueue.clear();
                        sortedNodeQueue.insert(dijkstraNode);
                        blArray[DijkstraNode.getY((DijkstraNode)dijkstraNode) * i3 + DijkstraNode.getX((DijkstraNode)dijkstraNode)] = true;
                        while (sortedNodeQueue.size() > 0) {
                            DijkstraNode dijkstraNode2 = sortedNodeQueue.poll();
                            i18 = DijkstraNode.getX(dijkstraNode2);
                            int n7 = DijkstraNode.getY(dijkstraNode2);
                            sortedNodeQueue.remove(dijkstraNode2);
                            blArray[n7 * i3 + i18] = false;
                            blArray2[n7 * i3 + i18] = true;
                            i20 = -1;
                            while (i20 <= 1) {
                                int n8 = -1;
                                while (n8 <= 1) {
                                    if (i20 != 0 || n8 != 0) {
                                        i22 = i18 + i20;
                                        i23 = n7 + n8;
                                        if (i22 >= 0 && i22 < i3 && i23 >= 0 && i23 < i4 && (sArray[i13][n7 * i3 + i18] & 1 << (n8 + 1 << 2) + (i20 + 1)) != 0) {
                                            boolean i27;
                                            boolean bl;
                                            DijkstraNode dijkstraNode3 = dijkstraNodeArray[i23 * i3 + i22];
                                            i25 = DijkstraNode.getCost(dijkstraNode2) + terrainGridAccessor.getMovementCost(i18, n7, i22, i23);
                                            if (i25 < DijkstraNode.getCost(dijkstraNode3)) {
                                                if (blArray[i23 * i3 + i22]) {
                                                    sortedNodeQueue.remove(dijkstraNode3);
                                                    blArray[i23 * i3 + i22] = false;
                                                } else if (blArray2[i23 * i3 + i22]) {
                                                    blArray2[i23 * i3 + i22] = false;
                                                }
                                                bl = false;
                                                i27 = false;
                                            } else {
                                                bl = blArray[i23 * i3 + i22];
                                                i27 = !bl ? blArray2[i23 * i3 + i22] : false;
                                            }
                                            if (!bl && !i27) {
                                                DijkstraNode.setCost(dijkstraNode3, i25);
                                                sortedNodeQueue.insert(dijkstraNode3);
                                                blArray[i23 * i3 + i22] = true;
                                            }
                                        }
                                    }
                                    ++n8;
                                }
                                ++i20;
                            }
                        }
                        n = 0;
                        while (n < i4) {
                            i18 = 0;
                            while (i18 < i3) {
                                Neighbor neighbor;
                                DijkstraNode dijkstraNode4 = dijkstraNodeArray[n * i3 + i18];
                                i20 = DijkstraNode.getCost(dijkstraNode4);
                                if (i20 == Integer.MAX_VALUE) {
                                    neighbor = Neighbor.None;
                                } else {
                                    i22 = -1;
                                    while (i22 <= 1) {
                                        i23 = -1;
                                        while (i23 <= 1) {
                                            if (i22 != 0 || i23 != 0) {
                                                DijkstraNode dijkstraNode5;
                                                int n9 = i18 + i22;
                                                i25 = n + i23;
                                                if (n9 >= 0 && n9 < i3 && i25 >= 0 && i25 < i4 && DijkstraNode.getCost(dijkstraNode5 = dijkstraNodeArray[i25 * i3 + n9]) < i20 && (sArray[i13][n * i3 + i18] & 1 << (i23 + 1 << 2) + (i22 + 1)) != 0) {
                                                    dijkstraNode4 = dijkstraNode5;
                                                    i20 = DijkstraNode.getCost(dijkstraNode5);
                                                }
                                            }
                                            ++i23;
                                        }
                                        ++i22;
                                    }
                                    neighbor = Neighbor.of(MathHelper.sign(DijkstraNode.getX(dijkstraNode4) - i18), MathHelper.sign(DijkstraNode.getY(dijkstraNode4) - n));
                                }
                                this.writeField(i15, i18, n, i12, i11, neighbor.getIndex());
                                ++i18;
                            }
                            ++n;
                        }
                    } else {
                        int n10 = 0;
                        while (n10 < i4) {
                            n = 0;
                            while (n < i3) {
                                this.writeField(i15, n, n10, i12, i11, Neighbor.None.getIndex());
                                ++n;
                            }
                            ++n10;
                        }
                    }
                    ++i13;
                }
                ++i12;
            }
            ++i11;
        }
        if (progressCallback != null) {
            progressCallback.onProgress(1.0f);
        }
    }

    @Override
    public final Neighbor findDirection(Domain domain, int i2, int i3, int i4, int i5) {
        switch (DirectionFieldPathFinder.a()[domain.ordinal()]) {
            case 1: {
                return Neighbor.fromIndex(this.readField(0, i2, i3, i4, i5));
            }
            case 2: {
                return Neighbor.fromIndex(this.readField(4, i2, i3, i4, i5));
            }
            case 4: {
                Neighbor neighbor = this.findDirection(Domain.Ground, i2, i3, i4, i5);
                if (neighbor.isValid()) {
                    return neighbor;
                }
                return this.findDirection(Domain.Water, i2, i3, i4, i5);
            }
        }
        return Neighbor.of(MathHelper.sign(i4 - i2), MathHelper.sign(i5 - i3));
    }

    private final int readField(int i1, int i2, int i3, int i4, int i5) {
        int i6 = i5 * this.b + i4 * this.c + i3 * this.d + i2;
        return this.a[i6] + 127 >> i1 & 0xF;
    }

    private final void writeField(int i1, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i5 * this.b + i4 * this.c + i3 * this.d + i2;
        this.a[i7] = (byte)((this.a[i7] + 127 & 15 << 4 - i1) + (i6 << i1) - 127);
    }

    static /* synthetic */ int[] a() {
        if (e != null) {
            return e;
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
        e = nArray;
        return nArray;
    }
}


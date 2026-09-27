/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

strictfp final class DijkstraNode {
    private final int a;
    private final int b;
    private DijkstraNode c;
    private int d;

    private DijkstraNode() {
        this(0, 0);
    }

    private DijkstraNode(int i1, int i2) {
        this.a = i1;
        this.b = i2;
    }

    /* synthetic */ DijkstraNode(DijkstraNode dijkstraNode) {
        this();
    }

    static /* synthetic */ DijkstraNode getNext(DijkstraNode dijkstraNode) {
        return dijkstraNode.c;
    }

    static /* synthetic */ int getCost(DijkstraNode dijkstraNode) {
        return dijkstraNode.d;
    }

    static /* synthetic */ void setNext(DijkstraNode dijkstraNode, DijkstraNode dijkstraNode2) {
        dijkstraNode.c = dijkstraNode2;
    }

    /* synthetic */ DijkstraNode(int i1, int i2, DijkstraNode dijkstraNode) {
        this(i1, i2);
    }

    static /* synthetic */ void setCost(DijkstraNode dijkstraNode, int i1) {
        dijkstraNode.d = i1;
    }

    static /* synthetic */ int getY(DijkstraNode dijkstraNode) {
        return dijkstraNode.b;
    }

    static /* synthetic */ int getX(DijkstraNode dijkstraNode) {
        return dijkstraNode.a;
    }
}


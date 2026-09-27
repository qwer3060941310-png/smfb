/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.world;

import com.desertstormfront.world.DijkstraNode;

strictfp final class SortedNodeQueue {
    private DijkstraNode a = new DijkstraNode(null);
    private int b;

    private SortedNodeQueue() {
    }

    public final DijkstraNode poll() {
        return DijkstraNode.getNext(this.a);
    }

    public final void clear() {
        this.b = 0;
    }

    public final void insert(DijkstraNode dijkstraNode) {
        int i2 = DijkstraNode.getCost(dijkstraNode);
        DijkstraNode dijkstraNode2 = this.a;
        DijkstraNode dijkstraNode3 = DijkstraNode.getNext(dijkstraNode2);
        int i5 = 0;
        while (i5 < this.b) {
            if (i2 <= DijkstraNode.getCost(dijkstraNode3)) {
                DijkstraNode.setNext(dijkstraNode2, dijkstraNode);
                DijkstraNode.setNext(dijkstraNode, dijkstraNode3);
                ++this.b;
                return;
            }
            dijkstraNode2 = dijkstraNode3;
            dijkstraNode3 = DijkstraNode.getNext(dijkstraNode2);
            ++i5;
        }
        DijkstraNode.setNext(dijkstraNode2, dijkstraNode);
        ++this.b;
    }

    public final void remove(DijkstraNode dijkstraNode) {
        DijkstraNode dijkstraNode2 = this.a;
        DijkstraNode dijkstraNode3 = DijkstraNode.getNext(dijkstraNode2);
        int i4 = 0;
        while (i4 < this.b) {
            if (dijkstraNode3 == dijkstraNode) {
                DijkstraNode.setNext(dijkstraNode2, DijkstraNode.getNext(dijkstraNode));
                --this.b;
                return;
            }
            dijkstraNode2 = dijkstraNode3;
            dijkstraNode3 = DijkstraNode.getNext(dijkstraNode2);
            ++i4;
        }
    }

    public final int size() {
        return this.b;
    }

    /* synthetic */ SortedNodeQueue(SortedNodeQueue sortedNodeQueue) {
        this();
    }
}


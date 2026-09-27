/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.Sortable;

final class DrawQueueNode {
    private DrawQueueNode a;
    private Sortable b;

    private DrawQueueNode() {
    }

    /* synthetic */ DrawQueueNode(DrawQueueNode drawQueueNode) {
        this();
    }

    static /* synthetic */ DrawQueueNode getNext(DrawQueueNode drawQueueNode) {
        return drawQueueNode.a;
    }

    static /* synthetic */ void setNext(DrawQueueNode drawQueueNode, DrawQueueNode drawQueueNode2) {
        drawQueueNode.a = drawQueueNode2;
    }

    static /* synthetic */ Sortable getValue(DrawQueueNode drawQueueNode) {
        return drawQueueNode.b;
    }

    static /* synthetic */ void setValue(DrawQueueNode drawQueueNode, Sortable sortable) {
        drawQueueNode.b = sortable;
    }
}


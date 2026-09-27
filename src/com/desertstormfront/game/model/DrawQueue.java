/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.model;

import com.desertstormfront.game.model.DrawQueueNode;
import com.desertstormfront.game.model.Sortable;
import java.util.ArrayList;
import java.util.List;

public final class DrawQueue {
    private DrawQueueNode a = new DrawQueueNode(null);
    private int b;
    private List c;

    public DrawQueue(int i1) {
        this.c = new ArrayList(i1);
        int i2 = 0;
        while (i2 < i1) {
            this.c.add(new DrawQueueNode(null));
            ++i2;
        }
    }

    public final Sortable poll() {
        if (this.b > 0) {
            DrawQueueNode drawQueueNode = DrawQueueNode.getNext(this.a);
            DrawQueueNode.setNext(this.a, DrawQueueNode.getNext(drawQueueNode));
            DrawQueueNode.setNext(drawQueueNode, (DrawQueueNode)null);
            Sortable sortable = DrawQueueNode.getValue(drawQueueNode);
            DrawQueueNode.setNext(drawQueueNode, (DrawQueueNode)null);
            --this.b;
            this.c.add(drawQueueNode);
            return sortable;
        }
        return null;
    }

    public final void add(Sortable sortable) {
        float f2 = sortable.getSortValue();
        DrawQueueNode drawQueueNode = this.a;
        DrawQueueNode drawQueueNode2 = DrawQueueNode.getNext(drawQueueNode);
        int n = 0;
        while (n < this.b) {
            if (f2 <= DrawQueueNode.getValue(drawQueueNode2).getSortValue()) {
                DrawQueueNode drawQueueNode3 = this.c.size() > 0 ? (DrawQueueNode)this.c.remove(this.c.size() - 1) : new DrawQueueNode(null);
                DrawQueueNode.setNext(drawQueueNode3, drawQueueNode2);
                DrawQueueNode.setValue(drawQueueNode3, sortable);
                DrawQueueNode.setNext(drawQueueNode, drawQueueNode3);
                ++this.b;
                return;
            }
            drawQueueNode = drawQueueNode2;
            drawQueueNode2 = DrawQueueNode.getNext(drawQueueNode);
            ++n;
        }
        DrawQueueNode drawQueueNode4 = this.c.size() > 0 ? (DrawQueueNode)this.c.remove(this.c.size() - 1) : new DrawQueueNode(null);
        DrawQueueNode.setValue(drawQueueNode4, sortable);
        DrawQueueNode.setNext(drawQueueNode, drawQueueNode4);
        ++this.b;
    }
}


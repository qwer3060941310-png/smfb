/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.ListBox;
import com.desertstormfront.ui.Widget;

class ListBoxListener
implements ActionListener {
    final /* synthetic */ ListBox a;

    ListBoxListener(ListBox listBox) {
        this.a = listBox;
    }

    @Override
    public void onAction(Widget widget) {
        int i2 = (Integer)widget.getData();
        if (i2 == 0) {
            this.a.setSelectedIndex(ListBox.getSelectedIndex(this.a) - 1);
        } else {
            this.a.setSelectedIndex(ListBox.getSelectedIndex(this.a) + 1);
        }
        ListBox.setChanged(this.a, true);
    }
}


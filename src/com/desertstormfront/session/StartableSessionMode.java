/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.session;

import com.desertstormfront.game.World;
import com.desertstormfront.session.SessionMode;

public interface StartableSessionMode
extends SessionMode {
    public void startSession(World var1);

    public void clearSaves();
}


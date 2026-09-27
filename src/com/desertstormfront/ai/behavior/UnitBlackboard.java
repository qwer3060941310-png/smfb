/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.behavior;

import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.game.model.Unit;

/**
 * 单位黑板，承载单个单位的 AI 共享数据
 */
public strictfp final class UnitBlackboard {
    private AIContext context;
    private UnitCommander commander;
    private Unit unit;
    private IntentGroup intentGroup;

    public UnitBlackboard(AIContext aIContext, UnitCommander unitCommander) {
        this.context = aIContext;
        this.commander = unitCommander;
    }

    public final void setUnit(Unit unit) {
        this.unit = unit;
    }

    public final AIContext getContext() {
        return this.context;
    }

    public final boolean refreshIntentGroup() {
        this.intentGroup = this.context.getIntents().findGroup(this.unit);
        return this.intentGroup != null;
    }

    public final IntentGroup getIntentGroup() {
        return this.intentGroup;
    }

    public final Unit getUnit() {
        return this.unit;
    }

    public final boolean moveUnitTo(float f1, float f2) {
        return this.commander.moveUnitTo(this.unit, f1, f2);
    }

    public final boolean targetUnit(Unit unit) {
        return this.commander.targetUnit(this.unit, unit);
    }

    public final boolean canStop() {
        return this.commander.canStop(this.unit);
    }

    public final boolean stopUnit() {
        return this.commander.stopUnit(this.unit);
    }
}


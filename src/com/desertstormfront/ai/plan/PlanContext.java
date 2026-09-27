/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.plan;

import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;

/**
 * 规划上下文，保存单次规划的状态与参数
 */
public strictfp final class PlanContext {
    private AIContext aiContext;
    private UnitCommander commander;
    private Intent intent;

    public PlanContext(AIContext aIContext, UnitCommander unitCommander) {
        this.aiContext = aIContext;
        this.commander = unitCommander;
    }

    public final void setIntent(Intent intent) {
        this.intent = intent;
    }

    public final AIContext getAiContext() {
        return this.aiContext;
    }

    public final Intent getIntent() {
        return this.intent;
    }

    public final boolean canBuild(Unit unit, UnitType unitType) {
        return this.commander.canBuildUnit(unit, unitType);
    }

    public final boolean build(Unit unit, UnitType unitType) {
        return this.commander.buildUnit(unit, unitType);
    }
}


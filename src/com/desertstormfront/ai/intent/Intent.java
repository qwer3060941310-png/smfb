/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai.intent;

import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.intent.IntentType;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;

/**
 * 意图实体，描述 AI 想达成的目标
 */
public strictfp final class Intent {
    private IntentType type;
    private IntentGroupList groupList;
    private UnitPosition position;
    private boolean hasTarget;

    public static Intent create(IntentType intentType) {
        Intent intent = new Intent();
        intent.setType(intentType);
        intent.setGroupList(new IntentGroupList());
        intent.position = new UnitPosition();
        intent.hasTarget = false;
        return intent;
    }

    public IntentType getType() {
        return this.type;
    }

    public void setType(IntentType intentType) {
        this.type = intentType;
    }

    public IntentGroupList getGroupList() {
        return this.groupList;
    }

    public void setGroupList(IntentGroupList intentGroupList) {
        this.groupList = intentGroupList;
    }

    public Vec2 getTargetPosition() {
        if (this.hasTarget) {
            return this.position;
        }
        return null;
    }

    public Unit getTargetUnit() {
        if (this.hasTarget) {
            return this.position.getUnit();
        }
        return null;
    }

    public void setTargetPosition(float f1, float f2) {
        this.hasTarget = true;
        this.position.setX(f1);
        this.position.setY(f2);
    }

    public void setTargetUnit(Unit unit) {
        this.hasTarget = true;
        this.position.bindTo(unit);
    }

    public void clearTarget() {
        this.hasTarget = false;
    }
}


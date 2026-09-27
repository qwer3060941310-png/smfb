/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ai;

import com.desertstormfront.ai.behavior.BehaviorNode;
import com.desertstormfront.ai.behavior.BehaviorTreePool;
import com.desertstormfront.ai.behavior.BlackboardPool;
import com.desertstormfront.ai.behavior.SelectorNode;
import com.desertstormfront.ai.behavior.SequenceNode;
import com.desertstormfront.ai.behavior.UnitBlackboard;
import com.desertstormfront.ai.behavior.nodes.AdvanceToTargetNode;
import com.desertstormfront.ai.behavior.nodes.AttackNearestNode;
import com.desertstormfront.ai.behavior.nodes.AutoEngageNode;
import com.desertstormfront.ai.behavior.nodes.DeployUnloadNode;
import com.desertstormfront.ai.behavior.nodes.EmbarkTransportNode;
import com.desertstormfront.ai.behavior.nodes.EvadeThreatNode;
import com.desertstormfront.ai.behavior.nodes.HasIntentGroup;
import com.desertstormfront.ai.behavior.nodes.HasTargetInRange;
import com.desertstormfront.ai.behavior.nodes.IsAdvanceMode;
import com.desertstormfront.ai.behavior.nodes.IsDeployMode;
import com.desertstormfront.ai.behavior.nodes.IsEmbarkMode;
import com.desertstormfront.ai.behavior.nodes.IsThreatened;
import com.desertstormfront.ai.behavior.nodes.NeedsRepair;
import com.desertstormfront.ai.behavior.nodes.ReturnToBaseNode;
import com.desertstormfront.ai.intent.AIContext;
import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.intent.IntentEvaluator;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.intent.IntentList;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanContextList;
import com.desertstormfront.ai.plan.PlanNode;
import com.desertstormfront.ai.plan.PlanNodeList;
import com.desertstormfront.ai.plan.PlanResult;
import com.desertstormfront.ai.plan.SelectorPlanNode;
import com.desertstormfront.ai.plan.SequencePlanNode;
import com.desertstormfront.ai.plan.nodes.AssignAirPlan;
import com.desertstormfront.ai.plan.nodes.AssignCarrierPlan;
import com.desertstormfront.ai.plan.nodes.AssignCruiserPlan;
import com.desertstormfront.ai.plan.nodes.AssignDestroyerPlan;
import com.desertstormfront.ai.plan.nodes.AssignGuardPlan;
import com.desertstormfront.ai.plan.nodes.AssignTransportPlan;
import com.desertstormfront.ai.plan.nodes.InitAttackPlan;
import com.desertstormfront.ai.plan.nodes.InitCommandPlan;
import com.desertstormfront.ai.plan.nodes.InitConquerPlan;
import com.desertstormfront.ai.plan.nodes.InitExplorePlan;
import com.desertstormfront.ai.plan.nodes.InitRoguePlan;
import com.desertstormfront.ai.plan.nodes.InitTruckPlan;
import com.desertstormfront.ai.plan.nodes.IsAttackCondition;
import com.desertstormfront.ai.plan.nodes.IsCommandCondition;
import com.desertstormfront.ai.plan.nodes.IsConquerCondition;
import com.desertstormfront.ai.plan.nodes.IsDefendCondition;
import com.desertstormfront.ai.plan.nodes.IsExploreCondition;
import com.desertstormfront.ai.plan.nodes.IsPatrolCondition;
import com.desertstormfront.ai.plan.nodes.IsRogueCondition;
import com.desertstormfront.ai.plan.nodes.IsTruckCondition;
import com.desertstormfront.ai.plan.nodes.LoadPlan;
import com.desertstormfront.ai.plan.nodes.MonitorEngagePlan;
import com.desertstormfront.ai.plan.nodes.MonitorReachPlan;
import com.desertstormfront.ai.plan.nodes.MoveRoguePlan;
import com.desertstormfront.ai.plan.nodes.MoveToTargetPlan;
import com.desertstormfront.ai.plan.nodes.RequireObjectiveNode;
import com.desertstormfront.ai.plan.nodes.ResetPlan;
import com.desertstormfront.ai.plan.nodes.UnloadPlan;
import com.desertstormfront.ai.plan.nodes.VerifyConquerPlan;
import com.desertstormfront.ai.plan.nodes.WaitBuildPlan;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.World;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.player.FogOfWar;
import com.noblemaster.lib.log.OsfLog;

/**
 * AI 规划器总控：编排「意图评估 → HTN 规划 → 行为树执行」三层 AI，持有 UnitCommander 下发指令
 */
public strictfp final class AIPlanner {
    private int phase;
    private int index;
    private UnitCommander commander;
    private AIContext context;
    private IntentEvaluator evaluator;
    private PlanNodeList planNodes;
    private PlanContextList planContexts;
    private PlanNodeList planNodePool;
    private PlanContextList planContextPool;
    private BehaviorTreePool behaviorTrees;
    private BlackboardPool blackboards;
    private BehaviorTreePool behaviorTreePool;
    private BlackboardPool blackboardPool;
    private long slowThresholdNanos;

    public AIPlanner(UnitCommander unitCommander) {
        this.commander = unitCommander;
        this.phase = 1;
        this.index = 0;
        this.context = AIContext.create(unitCommander);
        this.evaluator = new IntentEvaluator(this.context);
        this.planNodes = new PlanNodeList();
        this.planContexts = new PlanContextList();
        this.behaviorTrees = new BehaviorTreePool();
        this.blackboards = new BlackboardPool();
        this.slowThresholdNanos = 5000000L;
        this.planNodePool = new PlanNodeList();
        this.planContextPool = new PlanContextList();
        int i2 = 0;
        while (i2 < 32) {
            this.createPlanTree();
            ++i2;
        }
        this.behaviorTreePool = new BehaviorTreePool();
        this.blackboardPool = new BlackboardPool();
        i2 = 0;
        while (i2 < this.planNodePool.size() * 16) {
            this.createBehaviorTree();
            ++i2;
        }
    }

    public AIContext getContext() {
        return this.context;
    }

    public PlanNodeList getPlanNodes() {
        return this.planNodes;
    }

    public PlanContextList getPlanContexts() {
        return this.planContexts;
    }

    public void update() {
        if ((float)this.commander.getPlayer().o() > this.commander.getWorld().getGameTime()) {
            return;
        }
        int i1 = 0;
        while (i1 < 2) {
            long l;
            long l2 = System.nanoTime();
            String string = null;
            switch (this.phase) {
                case 1: {
                    int i11;
                    IntentList intentList = this.context.getIntents();
                    Object object = this.context.getPlayer().getFogOfWar();
                    int n = 0;
                    while (n < intentList.size()) {
                        Intent intent = (Intent)intentList.get(n);
                        Unit unit = intent.getTargetUnit();
                        if (unit != null) {
                            if (unit.isDestroyed() && !unit.isImmobile()) {
                                intent.clearTarget();
                            } else if (!((FogOfWar)object).isUnitVisible(unit)) {
                                intent.setTargetPosition(unit.getPosition().getX(), unit.getPosition().getY());
                            }
                        }
                        IntentGroupList intentGroupList = intent.getGroupList();
                        i11 = 0;
                        while (i11 < intentGroupList.size()) {
                            IntentGroup intentGroup = (IntentGroup)intentGroupList.get(i11);
                            unit = intentGroup.getTargetUnit();
                            if (unit != null && (unit.isDestroyed() && !unit.isImmobile() || !((FogOfWar)object).isUnitVisible(unit))) {
                                intentGroup.setTargetPosition(unit.getPosition().getX(), unit.getPosition().getY());
                            }
                            ++i11;
                        }
                        ++n;
                    }
                    if (GameConfig.isDebugEnabled()) {
                        string = "EVALUATOR";
                    }
                    if (this.evaluator.evaluate()) {
                        ++this.index;
                        break;
                    }
                    ++this.phase;
                    this.index = 0;
                    break;
                }
                case 2: {
                    if (GameConfig.isDebugEnabled()) {
                        string = "UPDATE_PLANS";
                    }
                    IntentList intentList = this.context.getIntents();
                    int n = 0;
                    while (n < intentList.size()) {
                        Intent intent = (Intent)intentList.get(n);
                        boolean bl = false;
                        int n2 = 0;
                        while (!bl && n2 < this.planNodes.size()) {
                            if (((PlanContext)this.planContexts.get(n2)).getIntent() == intent) {
                                bl = true;
                                break;
                            }
                            ++n2;
                        }
                        if (!bl) {
                            this.allocatePlan(intent);
                        }
                        ++n;
                    }
                    n = 0;
                    while (n < this.planNodes.size()) {
                        if (!intentList.contains(((PlanContext)this.planContexts.get(n)).getIntent())) {
                            this.recyclePlan(n);
                            continue;
                        }
                        ++n;
                    }
                    ++this.phase;
                    this.index = 0;
                    break;
                }
                case 3: {
                    Object object;
                    if (this.index < this.planNodes.size()) {
                        if (GameConfig.isDebugEnabled()) {
                            string = "EXECUTE_PLANS / " + ((PlanNode)this.planNodes.get(this.index)).getActiveNode((PlanContext)this.planContexts.get(this.index)).getClass().getSimpleName();
                        }
                        PlanResult planResult = ((PlanNode)this.planNodes.get(this.index)).run((PlanContext)this.planContexts.get(this.index));
                        if (GameConfig.isDebugEnabled() && planResult == PlanResult.FAILURE && ((PlanContext)this.planContexts.get(this.index)).getIntent().getGroupList().hasUnits() && !string.contains("Condition")) {
                            object = ((PlanContext)this.planContexts.get(this.index)).getIntent();
                            OsfLog.info("Plan Failed: " + string + " (" + ((Intent)object).getType().toString() + " " + ((Intent)object).getTargetPosition().toString() + ")");
                        }
                    }
                    ++this.index;
                    if (this.index >= this.planNodes.size()) {
                        ++this.phase;
                        this.index = 0;
                    }
                    if (!GameConfig.isDebugEnabled() || string != null) break;
                    string = "EXECUTE_PLANS / -";
                    break;
                }
                case 4: {
                    int i11;
                    if (GameConfig.isDebugEnabled()) {
                        string = "UPDATE_TASKS";
                    }
                    World world = this.commander.getWorld();
                    Object object = this.commander.getPlayer();
                    UnitList unitList = world.getUnits();
                    int n = 0;
                    while (n < unitList.size()) {
                        Unit unit = (Unit)unitList.get(n);
                        if (!unit.isImmobile() && unit.getOwner() == object) {
                            boolean bl = false;
                            i11 = 0;
                            while (!bl && i11 < this.behaviorTrees.size()) {
                                if (((UnitBlackboard)this.blackboards.get(i11)).getUnit() == unit) {
                                    bl = true;
                                    break;
                                }
                                ++i11;
                            }
                            if (!bl) {
                                this.allocateTask(unit);
                            }
                        }
                        ++n;
                    }
                    n = 0;
                    while (n < this.behaviorTrees.size()) {
                        if (!unitList.contains(((UnitBlackboard)this.blackboards.get(n)).getUnit())) {
                            this.recycleTask(n);
                            continue;
                        }
                        ++n;
                    }
                    ++this.phase;
                    this.index = 0;
                    break;
                }
                case 5: {
                    if (this.index < this.behaviorTrees.size()) {
                        UnitBlackboard unitBlackboard;
                        if (GameConfig.isDebugEnabled()) {
                            string = "EXECUTE_TASKS / " + ((BehaviorNode)this.behaviorTrees.get(this.index)).getActiveNode((UnitBlackboard)this.blackboards.get(this.index)).getClass().getSimpleName();
                        }
                        if (!(unitBlackboard = (UnitBlackboard)this.blackboards.get(this.index)).getUnit().isDestroyed()) {
                            ((BehaviorNode)this.behaviorTrees.get(this.index)).run((UnitBlackboard)this.blackboards.get(this.index));
                        }
                    }
                    ++this.index;
                    if (this.index >= this.behaviorTrees.size()) {
                        this.phase = 1;
                        this.index = 0;
                    }
                    if (!GameConfig.isDebugEnabled() || string != null) break;
                    string = "EXECUTE_TASKS / -";
                    break;
                }
                default: {
                    OsfLog.info("AI mode not implemented: " + this.phase);
                    this.phase = 1;
                }
            }
            if (GameConfig.isDebugEnabled() && ((l = System.nanoTime() - l2) > this.slowThresholdNanos / 2L || l > 1000000L)) {
                if (l > this.slowThresholdNanos) {
                    this.slowThresholdNanos = l;
                }
                OsfLog.info("Slow AI Update: " + l / 1000000L + "." + l / 100000L % 10L + "ms (" + string + ")");
            }
            ++i1;
        }
    }

    private void allocatePlan(Intent intent) {
        if (this.planNodePool.size() == 0) {
            this.createPlanTree();
        }
        PlanNode planNode = (PlanNode)this.planNodePool.remove(this.planNodePool.size() - 1);
        planNode.reset();
        PlanContext planContext = (PlanContext)this.planContextPool.remove(this.planContextPool.size() - 1);
        planContext.setIntent(intent);
        this.planNodes.add(planNode);
        this.planContexts.add(planContext);
    }

    private void createPlanTree() {
        SelectorPlanNode selectorPlanNode = new SelectorPlanNode();
        SequencePlanNode sequencePlanNode = new SequencePlanNode();
        selectorPlanNode.addChild(new IsConquerCondition(sequencePlanNode));
        sequencePlanNode.addChild(new ResetPlan());
        sequencePlanNode.addChild(new InitConquerPlan());
        SequencePlanNode sequencePlanNode2 = new SequencePlanNode();
        sequencePlanNode.addChild(new RequireObjectiveNode(sequencePlanNode2));
        sequencePlanNode2.addChild(new AssignTransportPlan());
        sequencePlanNode2.addChild(new AssignGuardPlan());
        sequencePlanNode2.addChild(new AssignCarrierPlan());
        sequencePlanNode2.addChild(new AssignCruiserPlan());
        sequencePlanNode2.addChild(new AssignAirPlan());
        sequencePlanNode2.addChild(new AssignDestroyerPlan());
        sequencePlanNode2.addChild(new WaitBuildPlan());
        sequencePlanNode2.addChild(new VerifyConquerPlan());
        sequencePlanNode2.addChild(new LoadPlan());
        sequencePlanNode2.addChild(new MoveToTargetPlan());
        sequencePlanNode2.addChild(new UnloadPlan());
        sequencePlanNode2.addChild(new MonitorEngagePlan());
        SequencePlanNode sequencePlanNode3 = new SequencePlanNode();
        selectorPlanNode.addChild(new IsAttackCondition(sequencePlanNode3));
        sequencePlanNode3.addChild(new ResetPlan());
        sequencePlanNode3.addChild(new InitAttackPlan());
        SequencePlanNode sequencePlanNode4 = new SequencePlanNode();
        sequencePlanNode3.addChild(new RequireObjectiveNode(sequencePlanNode4));
        sequencePlanNode4.addChild(new AssignTransportPlan());
        sequencePlanNode4.addChild(new AssignGuardPlan());
        sequencePlanNode4.addChild(new AssignCarrierPlan());
        sequencePlanNode4.addChild(new AssignCruiserPlan());
        sequencePlanNode4.addChild(new AssignAirPlan());
        sequencePlanNode4.addChild(new AssignDestroyerPlan());
        sequencePlanNode4.addChild(new WaitBuildPlan());
        sequencePlanNode4.addChild(new LoadPlan());
        sequencePlanNode4.addChild(new MoveToTargetPlan());
        sequencePlanNode4.addChild(new UnloadPlan());
        sequencePlanNode4.addChild(new MonitorEngagePlan());
        SequencePlanNode sequencePlanNode5 = new SequencePlanNode();
        selectorPlanNode.addChild(new IsCommandCondition(sequencePlanNode5));
        sequencePlanNode5.addChild(new ResetPlan());
        sequencePlanNode5.addChild(new InitCommandPlan());
        sequencePlanNode5.addChild(new MonitorReachPlan());
        SequencePlanNode sequencePlanNode6 = new SequencePlanNode();
        selectorPlanNode.addChild(new IsExploreCondition(sequencePlanNode6));
        sequencePlanNode6.addChild(new ResetPlan());
        sequencePlanNode6.addChild(new InitExplorePlan());
        sequencePlanNode6.addChild(new AssignTransportPlan());
        sequencePlanNode6.addChild(new AssignGuardPlan());
        sequencePlanNode6.addChild(new AssignCarrierPlan());
        sequencePlanNode6.addChild(new AssignCruiserPlan());
        sequencePlanNode6.addChild(new AssignAirPlan());
        sequencePlanNode6.addChild(new AssignDestroyerPlan());
        sequencePlanNode6.addChild(new WaitBuildPlan());
        sequencePlanNode6.addChild(new LoadPlan());
        sequencePlanNode6.addChild(new MoveToTargetPlan());
        sequencePlanNode6.addChild(new UnloadPlan());
        sequencePlanNode6.addChild(new MonitorReachPlan());
        SequencePlanNode sequencePlanNode7 = new SequencePlanNode();
        selectorPlanNode.addChild(new IsTruckCondition(sequencePlanNode7));
        sequencePlanNode7.addChild(new ResetPlan());
        sequencePlanNode7.addChild(new InitTruckPlan());
        sequencePlanNode7.addChild(new AssignTransportPlan());
        sequencePlanNode7.addChild(new AssignGuardPlan());
        sequencePlanNode7.addChild(new AssignCarrierPlan());
        sequencePlanNode7.addChild(new AssignCruiserPlan());
        sequencePlanNode7.addChild(new AssignAirPlan());
        sequencePlanNode7.addChild(new AssignDestroyerPlan());
        sequencePlanNode7.addChild(new WaitBuildPlan());
        sequencePlanNode7.addChild(new LoadPlan());
        sequencePlanNode7.addChild(new MoveToTargetPlan());
        sequencePlanNode7.addChild(new UnloadPlan());
        sequencePlanNode7.addChild(new MonitorReachPlan());
        SequencePlanNode sequencePlanNode8 = new SequencePlanNode();
        selectorPlanNode.addChild(new IsDefendCondition(sequencePlanNode8));
        SequencePlanNode sequencePlanNode9 = new SequencePlanNode();
        selectorPlanNode.addChild(new IsPatrolCondition(sequencePlanNode9));
        SequencePlanNode sequencePlanNode10 = new SequencePlanNode();
        selectorPlanNode.addChild(new IsRogueCondition(sequencePlanNode10));
        sequencePlanNode10.addChild(new ResetPlan());
        sequencePlanNode10.addChild(new InitRoguePlan());
        sequencePlanNode10.addChild(new WaitBuildPlan());
        sequencePlanNode10.addChild(new MoveRoguePlan());
        this.planNodePool.add(selectorPlanNode);
        this.planContextPool.add(new PlanContext(this.context, this.commander));
    }

    private void recyclePlan(int i1) {
        this.planNodePool.add((PlanNode)this.planNodes.remove(i1));
        this.planContextPool.add((PlanContext)this.planContexts.remove(i1));
    }

    private void allocateTask(Unit unit) {
        if (this.behaviorTreePool.size() == 0) {
            this.createBehaviorTree();
        }
        BehaviorNode behaviorNode = (BehaviorNode)this.behaviorTreePool.remove(this.behaviorTreePool.size() - 1);
        behaviorNode.reset();
        UnitBlackboard unitBlackboard = (UnitBlackboard)this.blackboardPool.remove(this.blackboardPool.size() - 1);
        unitBlackboard.setUnit(unit);
        this.behaviorTrees.add(behaviorNode);
        this.blackboards.add(unitBlackboard);
    }

    private void createBehaviorTree() {
        SelectorNode selectorNode = new SelectorNode();
        SequenceNode sequenceNode = new SequenceNode();
        selectorNode.addChild(new NeedsRepair(sequenceNode));
        sequenceNode.addChild(new ReturnToBaseNode());
        SequenceNode sequenceNode2 = new SequenceNode();
        selectorNode.addChild(new IsThreatened(sequenceNode2));
        sequenceNode2.addChild(new EvadeThreatNode());
        SequenceNode sequenceNode3 = new SequenceNode();
        selectorNode.addChild(new HasTargetInRange(sequenceNode3));
        sequenceNode3.addChild(new AttackNearestNode());
        SelectorNode selectorNode2 = new SelectorNode();
        selectorNode.addChild(new HasIntentGroup(selectorNode2));
        SequenceNode sequenceNode4 = new SequenceNode();
        selectorNode2.addChild(new IsAdvanceMode(sequenceNode4));
        sequenceNode4.addChild(new AdvanceToTargetNode());
        SequenceNode sequenceNode5 = new SequenceNode();
        selectorNode2.addChild(new IsEmbarkMode(sequenceNode5));
        sequenceNode5.addChild(new EmbarkTransportNode());
        SequenceNode sequenceNode6 = new SequenceNode();
        selectorNode2.addChild(new IsDeployMode(sequenceNode6));
        sequenceNode6.addChild(new DeployUnloadNode());
        SequenceNode sequenceNode7 = new SequenceNode();
        selectorNode.addChild(sequenceNode7);
        sequenceNode7.addChild(new AutoEngageNode());
        this.behaviorTreePool.add(selectorNode);
        this.blackboardPool.add(new UnitBlackboard(this.context, this.commander));
    }

    private void recycleTask(int i1) {
        this.behaviorTreePool.add((BehaviorNode)this.behaviorTrees.remove(i1));
        this.blackboardPool.add((UnitBlackboard)this.blackboards.remove(i1));
    }
}


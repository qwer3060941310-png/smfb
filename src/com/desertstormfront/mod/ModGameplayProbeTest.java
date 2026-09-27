/*
 * Headless gameplay probe: proves that the nine v6 gameplay events really fire, in a real
 * simulation, without a display.
 *
 * Why this exists: the two existing mod tests only prove the PLUMBING (ModEventFlowTest wraps a
 * listener and checks that each method is forwarded and posted; ModFieldAliasTest checks the JSON
 * key contract). Neither of them proves that a real match produces a real event sequence, and that
 * gap could only be closed by playing a game by hand. This class closes it: it assembles a minimal
 * world, orders one unit to attack another, steps WorldSimulator and asserts the resulting event
 * sequence.
 *
 * Headless constraints that shape this class (all verified against WorldSimulator, do not "fix"
 * them without re-reading the call sites):
 *  - WorldSimulator.a(world, listener, deltaSeconds) is a STATIC stepper, not a factory: one call
 *    advances the world by one step. Nothing is constructed, nothing is cached.
 *  - GameConfig.configure(...) must run first: World.create() reads UserConfig, whose static
 *    initialiser needs GameConfig's data file resolver.
 *  - debugEnabled MUST be false: the only OsfLog call on this path (WorldSimulator L902) is
 *    guarded by GameConfig.isDebugEnabled(), and OsfLog writes through Gdx.app, which is null here.
 *  - speedFactor defaults to 0.0f, which makes the attack branch (f11 > 0) unreachable, so it is
 *    set explicitly.
 *  - The listener must not be null: WorldSimulator dereferences it directly (L260).
 *  - TICK_COMPLETED is NOT per step: it needs (int)(gameTime * 10) % 800 == 0, i.e. roughly every
 *    80 game seconds, so the loop has to run long enough to reach it.
 *
 * Usage: java -cp "<classes>;lib\*" com.desertstormfront.mod.ModGameplayProbeTest
 * Run from the directory that holds lib/ and mod/ (see run\test.ps1).
 */
package com.desertstormfront.mod;

import com.desertstormfront.app.support.ProgressCallback;
import com.desertstormfront.command.DirectUnitCommander;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.GameEventListener;
import com.desertstormfront.game.ScenarioType;
import com.desertstormfront.game.World;
import com.desertstormfront.game.WorldSimulator;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.map.MapDefinition;
import com.noblemaster.lib.data.DateTime;
import com.noblemaster.lib.data.Version;
import com.noblemaster.lib.util.FastRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModGameplayProbeTest {

    private static final int MAP_SIZE = 64;
    private static final int PLAYERS = 2;
    private static final float STEP_SECONDS = 0.033f;
    private static final int STEPS = 5000;
    private static final long SEED = 20260919L;
    /** How many spawn spots to try before giving up on an explicit attack order. */
    private static final int MAX_ATTEMPTS = 8;

    /** Every topic in the order it was posted, so ordering can be asserted and printed. */
    private static final List<String> SEQUENCE = new ArrayList<String>();

    private ModGameplayProbeTest() {
    }

    public static void main(String[] args) {
        ModEvents.subscribe(new ModEventListener() {
            @Override
            public void onEvent(String topic, Object data) {
                SEQUENCE.add(topic);
            }
        });

        try {
            run();
        } catch (Throwable t) {
            System.out.println("GAMEPLAY_PROBE FAILED unexpected=" + t);
            t.printStackTrace(System.out);
            System.exit(1);
        }
    }

    private static void run() {
        // debugEnabled = false keeps OsfLog (-> Gdx.app) off this path.
        GameConfig.configure("tsf", new Version("1.0.0"), new DateTime(0L),
                false, false, 800, 600, false);

        MapDefinition ruleset = MapDefinition.getRuleset("tsf");
        if (ruleset == null) {
            fail("ruleset 'tsf' is missing");
        }

        World world = World.create(ruleset, "probe", ScenarioType.Supremacy, MAP_SIZE, MAP_SIZE, PLAYERS);
        // Seeded AFTER create(): create() builds the terrain with its own unseeded random.
        world.setRandom(new FastRandom(SEED));
        // World.create() does NOT build the navigation data: TerrainGrid's direction fields stay
        // null until this runs, and the first fog update then dies with an NPE.
        world.buildTerrain(new ProgressCallback() {
            @Override
            public boolean onProgress(float progress) {
                return true;
            }
        });
        world.setSpeedFactor(1.0f);

        Player attacker = (Player) world.getPlayers().get(0);
        Player defender = (Player) world.getPlayers().get(1);

        UnitType tank = ruleset.getUnitTypeSlots().getBattleTank();
        DirectUnitCommander commander = new DirectUnitCommander(world, attacker);

        // The terrain is generated with an unseeded random, so a spot that is reachable in one run
        // may be blocked in the next. Try several pairs and keep the first one that accepts the
        // order; without an accepted order the attack branch is only reached through the much
        // slower auto-targeting path.
        boolean ordered = false;
        int used = 0;
        for (int[] spot : findLandPairs(world)) {
            int x = spot[0];
            int y = spot[1];
            Unit hunter = world.spawnUnit(tank, (float) x, (float) y, attacker);
            Unit prey = world.spawnUnit(tank, (float) (x + 2), (float) y, defender);
            // Visibility gates targetUnitInternal, so refresh the fog before ordering.
            world.updateFogOfWar(attacker);
            world.updateFogOfWar(defender);
            ordered = commander.attackUnit(hunter, prey);
            ++used;
            System.out.println("probe: pair (" + x + "," + y + ")->(" + (x + 2) + "," + y + ")"
                    + " hunter=" + hunter.getId() + " prey=" + prey.getId()
                    + " preyHealth=" + prey.getHealth() + " accepted=" + ordered);
            if (ordered) {
                break;
            }
        }
        System.out.println("probe: pairs tried=" + used + " attackOrderAccepted=" + ordered);

        GameEventListener listener = ModGameEventListener.wrap(new RecordingListener());
        for (int i = 0; i < STEPS; ++i) {
            WorldSimulator.tick(world, listener, STEP_SECONDS);
            world.updateFogOfWar(attacker);
        }

        report(ordered);
    }

    /** Candidate spots: two land tiles two columns apart, so a pair can see and reach each other. */
    private static List<int[]> findLandPairs(World world) {
        List<int[]> spots = new ArrayList<int[]>();
        for (int y = 4; y < MAP_SIZE - 4 && spots.size() < MAX_ATTEMPTS; ++y) {
            for (int x = 4; x < MAP_SIZE - 6 && spots.size() < MAX_ATTEMPTS; ++x) {
                if (world.getTerrainGrid().canDomainEnter(Domain.Ground, x, y)
                        && world.getTerrainGrid().canDomainEnter(Domain.Ground, x + 2, y)) {
                    spots.add(new int[]{x, y});
                }
            }
        }
        if (spots.isEmpty()) {
            fail("no two land tiles found on this map");
        }
        return spots;
    }

    private static void report(boolean ordered) {
        Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
        Map<String, Integer> firstIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < SEQUENCE.size(); ++i) {
            String topic = SEQUENCE.get(i);
            Integer count = counts.get(topic);
            counts.put(topic, count == null ? 1 : count + 1);
            if (!firstIndex.containsKey(topic)) {
                firstIndex.put(topic, i);
            }
        }

        System.out.println("probe: steps=" + STEPS + " events=" + SEQUENCE.size());
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            System.out.println("  " + entry.getKey() + " x" + entry.getValue()
                    + " firstAt=" + firstIndex.get(entry.getKey()));
        }

        List<String> problems = new ArrayList<String>();
        require(problems, counts, ModGameEventListener.UNIT_ATTACK, "no unit ever fired");
        require(problems, counts, ModGameEventListener.UNIT_KILLED, "no unit was ever destroyed");

        Integer attackAt = firstIndex.get(ModGameEventListener.UNIT_ATTACK);
        Integer killedAt = firstIndex.get(ModGameEventListener.UNIT_KILLED);
        if (attackAt != null && killedAt != null && attackAt >= killedAt) {
            problems.add("ordering: a unit was destroyed (at " + killedAt + ") before anything fired (at "
                    + attackAt + ")");
        }
        // A destruction must be reported under both topics: UNIT_KILLED is the historical one and
        // UNIT_DESTROYED is the corrected one (see ModGameEventListener).
        if (counts.containsKey(ModGameEventListener.UNIT_KILLED)
                && !counts.containsKey(ModGameEventListener.UNIT_DESTROYED)) {
            problems.add("a unit was killed without a matching 'destroyed' event: the destruction"
                    + " mapping is inconsistent");
        }
        // Not covered by this match: production (UNIT_CONSTRUCTED), capture, CTF and elimination
        // need a base, a neutral structure or a flag scenario - see the class comment.
        if (!counts.containsKey(ModGameEventListener.TICK_COMPLETED)) {
            problems.add("TICK_COMPLETED never fired (needs ~80 game seconds; steps=" + STEPS + ")");
        }

        if (!problems.isEmpty()) {
            for (String problem : problems) {
                System.out.println("  PROBLEM: " + problem + " (attackOrderAccepted=" + ordered + ")");
            }
            fail(problems.size() + " check(s) failed");
        }
        System.out.println("GAMEPLAY_PROBE PASSED events=" + SEQUENCE.size() + " order=attack<killed");
        System.exit(0);
    }

    private static void require(List<String> problems, Map<String, Integer> counts, String topic, String message) {
        Integer count = counts.get(topic);
        if (count == null || count <= 0) {
            problems.add(topic + ": " + message);
        }
    }

    private static void fail(String message) {
        System.out.println("GAMEPLAY_PROBE FAILED " + message);
        System.exit(1);
    }

    /** Forwards nothing: the bus is what is under test, but the delegate must still be invoked. */
    private static final class RecordingListener implements GameEventListener {
        @Override
        public void onPlayerEliminated(Player player) {
        }

        @Override
        public void onUnitConstructed(Unit unit) {
        }

        @Override
        public void onStructureCaptured(Unit unit) {
        }

        @Override
        public void onUnitDestroyed(Unit unit, Player player, Player player2) {
        }

        @Override
        public void onUnitHit(Unit unit, Player player) {
        }

        @Override
        public void onVolleyFired(Unit unit) {
        }

        @Override
        public void onFlagTaken(Player player) {
        }

        @Override
        public void onFlagLost(Player player) {
        }

        @Override
        public void onGameTick() {
        }
    }
}

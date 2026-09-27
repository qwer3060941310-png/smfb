/*
 * Event flow verification (headless, no GUI needed).
 *
 * Verifies the v6 hook plumbing end to end:
 *   game listener call -> ModGameEventListener forwards to delegate -> event posted on
 *   ModEvents -> delivered to every subscribed ModEventListener.
 *
 * This proves the wiring and the topic mapping. It does NOT replace observing the real
 * firing order inside an actual match (which needs the GUI); see 22_事件流验证.md.
 *
 * Usage: java -cp <classes;lib/*> com.desertstormfront.mod.ModEventFlowTest
 */
package com.desertstormfront.mod;

import com.desertstormfront.game.GameEventListener;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public final class ModEventFlowTest {

    private final List<String> forwarded = new ArrayList<String>();
    private final List<String> received = new ArrayList<String>();
    private int failures = 0;

    private ModEventFlowTest() {
    }

    public static void main(String[] args) {
        ModEventFlowTest test = new ModEventFlowTest();
        test.run();
        if (test.failures > 0) {
            System.out.println("EVENT_FLOW FAILED failures=" + test.failures);
            System.exit(1);
        }
        System.out.println("EVENT_FLOW PASSED");
    }

    private void run() {
        GameEventListener recorder = new RecordingListener();
        GameEventListener wrapped = ModGameEventListener.wrap(recorder);

        ModEvents.subscribe(new ModEventListener() {
            @Override
            public void onEvent(String topic, Object data) {
                received.add(topic);
            }
        });

        // Exercise every listener method (nulls are fine: the delegate only records).
        wrapped.onPlayerEliminated((Player) null);
        wrapped.onUnitConstructed((Unit) null);
        wrapped.onStructureCaptured((Unit) null);
        wrapped.onUnitDestroyed((Unit) null, (Player) null, (Player) null);
        wrapped.onUnitHit((Unit) null, (Player) null);
        wrapped.onVolleyFired((Unit) null);
        wrapped.onFlagTaken((Player) null);
        wrapped.onFlagLost((Player) null);
        wrapped.onGameTick();

        checkForwarded("a(Player)", 1);
        checkForwarded("a(Unit)", 1);
        checkForwarded("b(Unit)", 1);
        checkForwarded("a(Unit,Player,Player)", 1);
        checkForwarded("a(Unit,Player)", 1);
        checkForwarded("c(Unit)", 1);
        checkForwarded("b(Player)", 1);
        checkForwarded("c(Player)", 1);
        checkForwarded("a()", 1);

        checkEvent(ModGameEventListener.PLAYER_DEFEATED);
        checkEvent(ModGameEventListener.UNIT_CONSTRUCTED);
        checkEvent(ModGameEventListener.UNIT_DESTROYED);
        checkEvent(ModGameEventListener.SITE_CAPTURED);
        checkEvent(ModGameEventListener.UNIT_KILLED);
        checkEvent(ModGameEventListener.UNIT_LOST);
        checkEvent(ModGameEventListener.UNIT_ATTACK);
        checkEvent(ModGameEventListener.PLAYER_OBJECTIVE);
        checkEvent(ModGameEventListener.FLAG_LOST);
        checkEvent(ModGameEventListener.TICK_COMPLETED);

        System.out.println("forwarded=" + forwarded.size() + " events=" + received.size());
    }

    private void checkForwarded(String name, int expected) {
        int actual = 0;
        for (String entry : forwarded) {
            if (entry.equals(name)) {
                ++actual;
            }
        }
        if (actual != expected) {
            ++failures;
            System.out.println("FAIL forward " + name + " expected=" + expected + " actual=" + actual);
        }
    }

    private void checkEvent(String topic) {
        if (!received.contains(topic)) {
            ++failures;
            System.out.println("FAIL event not delivered: " + topic);
        }
    }

    private final class RecordingListener implements GameEventListener {
        @Override
        public void onPlayerEliminated(Player player) {
            forwarded.add("a(Player)");
        }

        @Override
        public void onUnitConstructed(Unit unit) {
            forwarded.add("a(Unit)");
        }

        @Override
        public void onStructureCaptured(Unit unit) {
            forwarded.add("b(Unit)");
        }

        @Override
        public void onUnitDestroyed(Unit unit, Player player, Player player2) {
            forwarded.add("a(Unit,Player,Player)");
        }

        @Override
        public void onUnitHit(Unit unit, Player player) {
            forwarded.add("a(Unit,Player)");
        }

        @Override
        public void onVolleyFired(Unit unit) {
            forwarded.add("c(Unit)");
        }

        @Override
        public void onFlagTaken(Player player) {
            forwarded.add("b(Player)");
        }

        @Override
        public void onFlagLost(Player player) {
            forwarded.add("c(Player)");
        }

        @Override
        public void onGameTick() {
            forwarded.add("a()");
        }
    }
}

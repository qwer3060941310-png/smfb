/*
 * v6 gameplay event hook (wraps the game's own listener).
 *
 * Forwards EVERY call to the delegate first, so game behaviour is unchanged; afterwards the
 * event is published on the mod event bus. Semantics were derived from WorldSimulator call
 * sites plus GameEventSoundListener - see 20_玩法事件与command语义勘察.md for the evidence
 * and confidence level of each event.
 */
package com.desertstormfront.mod;

import com.desertstormfront.game.GameEventListener;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.player.Player;

public final class ModGameEventListener implements GameEventListener {

    public static final String UNIT_ATTACK = "game.unit.attack";
    /**
     * A unit left the board: fired from {@code onUnitDestroyed}, i.e. WorldSimulator L260, the
     * branch where the damage subtracted from the health reaches zero.
     *
     * <p>History: this topic used to be posted from {@code onUnitConstructed}, which is the
     * PRODUCTION callback (WorldSimulator L793, "remaining build ticks reached zero"). That was a
     * mislabel, not a design choice - the headless probe (ModGameplayProbeTest) made it visible.
     * Construction now has its own topic below, and this one means what its name says.
     */
    public static final String UNIT_DESTROYED = "game.unit.destroyed";
    public static final String UNIT_KILLED = "game.unit.killed";
    /** A unit finished being produced (WorldSimulator L793). */
    public static final String UNIT_CONSTRUCTED = "game.unit.constructed";
    public static final String UNIT_LOST = "game.unit.lost";
    public static final String SITE_CAPTURED = "game.site.captured";
    public static final String PLAYER_DEFEATED = "game.player.defeated";
    public static final String PLAYER_OBJECTIVE = "game.player.objective";
    public static final String TICK_COMPLETED = "game.tick.completed";
    public static final String FLAG_LOST = "game.flag.lost";

    private final GameEventListener delegate;

    private ModGameEventListener(GameEventListener delegate) {
        this.delegate = delegate;
    }

    /**
     * Wrap a listener; returns null for null input so existing behaviour is preserved.
     * The wrapper is cached per delegate because the call site runs every frame - without
     * this, a new wrapper would be allocated on each frame.
     */
    private static final java.util.Map<GameEventListener, ModGameEventListener> CACHE =
            new java.util.IdentityHashMap<GameEventListener, ModGameEventListener>();

    public static GameEventListener wrap(GameEventListener listener) {
        if (listener == null) {
            return null;
        }
        if (listener instanceof ModGameEventListener) {
            return listener;
        }
        synchronized (CACHE) {
            ModGameEventListener existing = CACHE.get(listener);
            if (existing == null) {
                existing = new ModGameEventListener(listener);
                CACHE.put(listener, existing);
            }
            return existing;
        }
    }

    @Override
    public void onPlayerEliminated(Player player) {
        this.delegate.onPlayerEliminated(player);
        ModEvents.post(PLAYER_DEFEATED, player);
    }

    @Override
    public void onUnitConstructed(Unit unit) {
        this.delegate.onUnitConstructed(unit);
        ModEvents.post(UNIT_CONSTRUCTED, unit);
    }

    @Override
    public void onStructureCaptured(Unit unit) {
        this.delegate.onStructureCaptured(unit);
        ModEvents.post(SITE_CAPTURED, unit);
    }

    @Override
    public void onUnitDestroyed(Unit unit, Player player, Player player2) {
        this.delegate.onUnitDestroyed(unit, player, player2);
        // Both topics describe this one event; UNIT_KILLED is kept for existing listeners.
        ModEvents.post(UNIT_KILLED, unit);
        ModEvents.post(UNIT_DESTROYED, unit);
    }

    @Override
    public void onUnitHit(Unit unit, Player player) {
        this.delegate.onUnitHit(unit, player);
        ModEvents.post(UNIT_LOST, unit);
    }

    @Override
    public void onVolleyFired(Unit unit) {
        this.delegate.onVolleyFired(unit);
        ModEvents.post(UNIT_ATTACK, unit);
    }

    @Override
    public void onFlagTaken(Player player) {
        this.delegate.onFlagTaken(player);
        ModEvents.post(PLAYER_OBJECTIVE, player);
    }

    @Override
    public void onFlagLost(Player player) {
        this.delegate.onFlagLost(player);
        // CTF: fired when the destroyed unit is the flag (WorldSimulator L244-246).
        ModEvents.post(FLAG_LOST, player);
    }

    @Override
    public void onGameTick() {
        this.delegate.onGameTick();
        ModEvents.post(TICK_COMPLETED, null);
    }
}

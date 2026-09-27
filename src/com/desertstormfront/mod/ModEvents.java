/*
 * Mod event bus (v6): a tiny, dependency-free publish/subscribe hub.
 *
 * Why Java listeners instead of scripts: the bundled JDK is 17, which removed Nashorn, and
 * this environment is offline, so no scripting engine can be added. External mods therefore
 * hook in by implementing ModEventListener and listing their class in mod/plugins.txt.
 *
 * NOTE: must not use OsfLog for early events - the libGDX application may not exist yet.
 */
package com.desertstormfront.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ModEvents {

    /** A mod file was loaded. */
    public static final String MOD_LOADED = "mod.loaded";
    /** Unit stat overrides were applied. */
    public static final String OVERRIDES_APPLIED = "mod.overrides.applied";
    /** New units were appended to a ruleset. */
    public static final String UNITS_ADDED = "mod.units.added";
    /** The cost matrix was rebuilt. */
    public static final String MATRIX_REBUILT = "mod.matrix.rebuilt";
    /** A custom ruleset was registered. */
    public static final String RULESET_REGISTERED = "mod.ruleset.registered";
    /** All mod files were re-read and re-applied (v7 hot reload). */
    public static final String MOD_RELOADED = "mod.reloaded";

    private static final Map<String, List<ModEventListener>> LISTENERS = new HashMap<String, List<ModEventListener>>();
    private static final List<ModEventListener> ALL = new ArrayList<ModEventListener>();

    private ModEvents() {
    }

    public static void subscribe(ModEventListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (ALL) {
            if (!ALL.contains(listener)) {
                ALL.add(listener);
            }
        }
    }

    public static void subscribe(String topic, ModEventListener listener) {
        if (listener == null || topic == null) {
            return;
        }
        synchronized (LISTENERS) {
            List<ModEventListener> list = LISTENERS.get(topic);
            if (list == null) {
                list = new ArrayList<ModEventListener>();
                LISTENERS.put(topic, list);
            }
            if (!list.contains(listener)) {
                list.add(listener);
            }
        }
    }

    public static void post(String topic, Object data) {
        if (topic == null) {
            return;
        }
        List<ModEventListener> topicListeners;
        synchronized (LISTENERS) {
            topicListeners = LISTENERS.get(topic);
            if (topicListeners != null) {
                topicListeners = new ArrayList<ModEventListener>(topicListeners);
            }
        }
        List<ModEventListener> global;
        synchronized (ALL) {
            global = new ArrayList<ModEventListener>(ALL);
        }
        dispatch(topicListeners, topic, data);
        dispatch(global, topic, data);
    }

    private static void dispatch(List<ModEventListener> listeners, String topic, Object data) {
        if (listeners == null) {
            return;
        }
        for (ModEventListener listener : listeners) {
            try {
                listener.onEvent(topic, data);
            } catch (Throwable t) {
                System.out.println("[ModEvents] listener failed for '" + topic + "': " + t);
            }
        }
    }
}

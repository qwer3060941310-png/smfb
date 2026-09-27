/*
 * Listener interface for the mod event bus (v6).
 * Implement this in an external mod class and list it in mod/plugins.txt to receive events.
 */
package com.desertstormfront.mod;

public interface ModEventListener {

    /** Called for every event the listener is subscribed to. */
    void onEvent(String topic, Object data);
}

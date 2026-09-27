/*
 * Sample v6 plugin: logs mod events.
 * Enabled by listing this class name in mod/plugins.txt.
 *
 * NOTE: game.tick.completed fires once per frame. Printing it would flood stdout and stall
 * the game, so it is skipped here (log it yourself only if you throttle it).
 */
package com.desertstormfront.mod;

public class SampleModPlugin implements ModEventListener {

    @Override
    public void onEvent(String topic, Object data) {
        if (ModGameEventListener.TICK_COMPLETED.equals(topic)) {
            return;
        }
        System.out.println("[SampleModPlugin] event=" + topic + " data=" + data);
    }
}

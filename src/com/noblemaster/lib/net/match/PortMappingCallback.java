/*
 * UPnP hook: maps a game port on the router and returns the address to advertise, or throws when
 * the mapping fails (MatchMakingClient then falls back to the external address).
 *
 * Deobfuscation: a(String, int) became mapPort. Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

public interface PortMappingCallback {
    public String mapPort(String var1, int var2);
}


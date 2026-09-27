/*
 * Supplies the two addresses the match protocol advertises: the local (LAN) address and the
 * external (internet) address.
 *
 * Deobfuscation: a()/b() became getLocalAddress/getExternalAddress, matching the two
 * DefaultAddressProvider strategies. Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

public interface AddressProvider {
    public String getLocalAddress();

    public String getExternalAddress();
}


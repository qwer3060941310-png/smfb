/*
 * One advertised match: identity (id/productId), display strings (title/label) and the two
 * address/port pairs a client may dial - the local pair for same-LAN play, the external pair
 * for internet play (portOpen is true once the UPnP mapping succeeded). timestamp is refreshed by
 * the match server on every update and drives its expiry sweep.
 *
 * Deobfuscation: fields a..j became id/productId/title/label/localAddress/localPort/
 * externalAddress/externalPort/portOpen/timestamp; the getters/setters take the same names. The
 * mapping is proven by MatchMakingClientTest ("ID"/"Timestamp"/"Local = e:f"/"External = g:h
 * open=i") and by GameClient, which dials local when its own external address equals
 * getExternalAddress(). Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

public class MatchRecord {
    private long id;
    private String productId;
    private String title;
    private String label;
    private String localAddress;
    private int localPort;
    private String externalAddress;
    private int externalPort;
    private boolean portOpen;
    private long timestamp;

    MatchRecord() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long l1) {
        this.id = l1;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String string) {
        this.productId = string;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String string) {
        this.title = string;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String string) {
        this.label = string;
    }

    public String getLocalAddress() {
        return this.localAddress;
    }

    public void setLocalAddress(String string) {
        this.localAddress = string;
    }

    public int getLocalPort() {
        return this.localPort;
    }

    public void setLocalPort(int i1) {
        this.localPort = i1;
    }

    public String getExternalAddress() {
        return this.externalAddress;
    }

    public void setExternalAddress(String string) {
        this.externalAddress = string;
    }

    public int getExternalPort() {
        return this.externalPort;
    }

    public void setExternalPort(int i1) {
        this.externalPort = i1;
    }

    public boolean isPortOpen() {
        return this.portOpen;
    }

    public void setPortOpen(boolean bl) {
        this.portOpen = bl;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long l1) {
        this.timestamp = l1;
    }
}


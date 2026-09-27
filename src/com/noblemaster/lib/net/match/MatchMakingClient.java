/*
 * Client half of the match-making protocol: each call opens a TCP socket to the match server, sends
 * an opcode (0 create / 1 update / 2 get / 3 list) plus the payload, and reads the reply.
 *
 * Deobfuscation: fields a/b became host/port; the single-letter methods became createMatch
 * (public and host/port form), updateMatch, getMatch, listMatches and the shared closeQuietly
 * helper, named after the opcode that MatchRequestHandler dispatches on.
 * Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.noblemaster.lib.io.stream.DataReader;
import com.noblemaster.lib.io.stream.DataWriter;
import com.noblemaster.lib.io.stream.impl.StreamDataReader;
import com.noblemaster.lib.io.stream.impl.StreamDataWriter;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.net.match.AddressProvider;
import com.noblemaster.lib.net.match.IpHelper;
import com.noblemaster.lib.net.match.MatchCodec;
import com.noblemaster.lib.net.match.MatchList;
import com.noblemaster.lib.net.match.MatchListCodec;
import com.noblemaster.lib.net.match.MatchRecord;
import com.noblemaster.lib.net.match.PortMappingCallback;
import com.noblemaster.lib.net.match.PortProbeServer;
import java.io.IOException;

public class MatchMakingClient {
    private String host;
    private int port;

    public MatchMakingClient(String string, int i2) {
        this.host = string;
        this.port = i2;
    }

    public MatchRecord createMatch(String string, String string2, String string3, int i4, AddressProvider addressProvider, PortMappingCallback portMappingCallback) {
        return this.createMatch(this.host, this.port, string, string2, string3, i4, addressProvider, portMappingCallback);
    }

    public void updateMatch(MatchRecord matchRecord) {
        MatchMakingClient.updateMatch(this.host, this.port, matchRecord);
    }

    public MatchRecord getMatch(String string, long l2) {
        return MatchMakingClient.getMatch(this.host, this.port, string, l2);
    }

    public MatchList listMatches(String string) {
        return MatchMakingClient.listMatches(this.host, this.port, string);
    }

    private MatchRecord createMatch(String string, int i2, String string2, String string3, String string4, int i6, AddressProvider addressProvider, PortMappingCallback portMappingCallback) {
        MatchRecord matchRecord;
        Socket socket = null;
        StreamDataReader streamDataReader = null;
        StreamDataWriter streamDataWriter = null;
        try {
            SocketHints socketHints = new SocketHints();
            socketHints.connectTimeout = 5000;
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, string, i2, new SocketHints());
            PortProbeServer portProbeServer = new PortProbeServer(this, i6);
            portProbeServer.start();
            try {
                Thread.sleep(500L);
            }
            catch (InterruptedException interruptedException) {}
            streamDataWriter = new StreamDataWriter(socket.getOutputStream());
            streamDataWriter.writeInt(0);
            MatchRecord matchRecord2 = new MatchRecord();
            matchRecord2.setProductId(string2);
            matchRecord2.setTitle(string3);
            matchRecord2.setLabel(string4);
            matchRecord2.setLocalAddress(addressProvider.getLocalAddress());
            matchRecord2.setLocalPort(i6);
            if (IpHelper.isPrivateAddress(matchRecord2.getLocalAddress())) {
                try {
                    String string5 = portMappingCallback.mapPort(string2, i6);
                    matchRecord2.setExternalAddress(string5);
                    matchRecord2.setExternalPort(i6);
                    matchRecord2.setPortOpen(true);
                }
                catch (Exception exception) {
                    OsfLog.info("Cannot create UPnP mapping.");
                    String string6 = addressProvider.getExternalAddress();
                    if (string6 == null) {
                        string6 = matchRecord2.getLocalAddress();
                    }
                    matchRecord2.setExternalAddress(string6);
                    matchRecord2.setExternalPort(i6);
                    matchRecord2.setPortOpen(true);
                }
            } else {
                matchRecord2.setExternalAddress(matchRecord2.getLocalAddress());
                matchRecord2.setExternalPort(i6);
            }
            matchRecord2.setTimestamp(Long.MIN_VALUE);
            MatchCodec.write(streamDataWriter, matchRecord2);
            streamDataWriter.flush();
            streamDataReader = new StreamDataReader(socket.getInputStream());
            matchRecord = MatchCodec.read(streamDataReader);
        }
        catch (Throwable throwable) {
            MatchMakingClient.closeQuietly(socket, streamDataReader, streamDataWriter);
            try {
                Thread.sleep(7000L);
            }
            catch (InterruptedException interruptedException) {}
            throw throwable;
        }
        MatchMakingClient.closeQuietly(socket, streamDataReader, streamDataWriter);
        try {
            Thread.sleep(7000L);
        }
        catch (InterruptedException interruptedException) {}
        return matchRecord;
    }

    private static void updateMatch(String string, int i1, MatchRecord matchRecord) {
        Socket socket = null;
        StreamDataReader streamDataReader = null;
        StreamDataWriter streamDataWriter = null;
        try {
            SocketHints socketHints = new SocketHints();
            socketHints.connectTimeout = 5000;
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, string, i1, socketHints);
            streamDataWriter = new StreamDataWriter(socket.getOutputStream());
            streamDataWriter.writeInt(1);
            MatchCodec.write(streamDataWriter, matchRecord);
            streamDataWriter.flush();
            streamDataReader = new StreamDataReader(socket.getInputStream());
            matchRecord.setTimestamp(streamDataReader.readLong());
        }
        catch (Throwable throwable) {
            MatchMakingClient.closeQuietly(socket, streamDataReader, streamDataWriter);
            throw throwable;
        }
        MatchMakingClient.closeQuietly(socket, streamDataReader, streamDataWriter);
    }

    private static MatchRecord getMatch(String string, int i1, String string2, long l3) {
        MatchRecord matchRecord;
        Socket socket = null;
        StreamDataReader streamDataReader = null;
        StreamDataWriter streamDataWriter = null;
        try {
            SocketHints socketHints = new SocketHints();
            socketHints.connectTimeout = 5000;
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, string, i1, socketHints);
            streamDataWriter = new StreamDataWriter(socket.getOutputStream());
            streamDataWriter.writeInt(2);
            streamDataWriter.writeString(string2);
            streamDataWriter.writeLong(l3);
            streamDataWriter.flush();
            streamDataReader = new StreamDataReader(socket.getInputStream());
            matchRecord = MatchCodec.read(streamDataReader);
        }
        catch (Throwable throwable) {
            MatchMakingClient.closeQuietly(socket, streamDataReader, streamDataWriter);
            throw throwable;
        }
        MatchMakingClient.closeQuietly(socket, streamDataReader, streamDataWriter);
        return matchRecord;
    }

    private static MatchList listMatches(String string, int i1, String string2) {
        MatchList matchList;
        Socket socket = null;
        StreamDataReader streamDataReader = null;
        StreamDataWriter streamDataWriter = null;
        try {
            SocketHints socketHints = new SocketHints();
            socketHints.connectTimeout = 5000;
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, string, i1, socketHints);
            streamDataWriter = new StreamDataWriter(socket.getOutputStream());
            streamDataWriter.writeInt(3);
            streamDataWriter.writeString(string2);
            streamDataWriter.flush();
            streamDataReader = new StreamDataReader(socket.getInputStream());
            matchList = MatchListCodec.read(streamDataReader);
        }
        catch (Throwable throwable) {
            MatchMakingClient.closeQuietly(socket, streamDataReader, streamDataWriter);
            throw throwable;
        }
        MatchMakingClient.closeQuietly(socket, streamDataReader, streamDataWriter);
        return matchList;
    }

    private static void closeQuietly(Socket socket, DataReader dataReader, DataWriter dataWriter) {
        if (dataReader != null) {
                dataReader.close();
            dataReader = null;
        }
        if (dataWriter != null) {
                dataWriter.close();
            dataWriter = null;
        }
        if (socket != null) {
            try {
                socket.dispose();
            }
            catch (Exception exception) {}
            socket = null;
        }
    }
}


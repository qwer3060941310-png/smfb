/*
 * Short-lived server that verifies a UPnP mapping actually works: it listens on the mapped port,
 * accepts the probe connection the caller opens and logs whether it arrived.
 *
 * Deobfuscation: the synthetic outer reference a became client and b became port.
 * Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Disposable;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.net.match.MatchMakingClient;
import java.io.InputStream;

class PortProbeServer
extends Thread {
    final /* synthetic */ MatchMakingClient client;
    private final /* synthetic */ int port;

    PortProbeServer(MatchMakingClient matchMakingClient, int i2) {
        this.client = matchMakingClient;
        this.port = i2;
    }

    @Override
    public void run() {
        block32: {
            ServerSocket serverSocket = null;
            Socket socket = null;
            InputStream inputStream = null;
            try {
                try {
                    ServerSocketHints serverSocketHints = new ServerSocketHints();
                    serverSocketHints.reuseAddress = true;
                    serverSocketHints.backlog = 1;
                    serverSocketHints.acceptTimeout = 5000;
                    serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, this.port, serverSocketHints);
                    SocketHints socketHints = new SocketHints();
                    socket = serverSocket.accept(socketHints);
                    inputStream = socket.getInputStream();
                    while (inputStream.available() > 0) {
                        inputStream.read();
                    }
                    OsfLog.info("Port mapping test server info: SUCCESS");
                }
                catch (Exception exception) {
                    OsfLog.info("Port mapping test server error: " + exception);
                    OsfLog.logException(exception);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        }
                        catch (Exception exception2) {}
                        inputStream = null;
                    }
                    if (socket != null) {
                        try {
                            socket.dispose();
                        }
                        catch (Exception exception3) {}
                        socket = null;
                    }
                    if (serverSocket == null) break block32;
                    try {
                        serverSocket.dispose();
                    }
                    catch (Exception exception4) {}
                    serverSocket = null;
                    break block32;
                }
            }
            catch (Throwable throwable) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (Exception exception) {}
                    inputStream = null;
                }
                if (socket != null) {
                    try {
                        socket.dispose();
                    }
                    catch (Exception exception) {}
                    socket = null;
                }
                if (serverSocket != null) {
                    try {
                        serverSocket.dispose();
                    }
                    catch (Exception exception) {}
                    serverSocket = null;
                }
                throw throwable;
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (Exception exception) {}
                inputStream = null;
            }
            if (socket != null) {
                try {
                    socket.dispose();
                }
                catch (Exception exception) {}
                socket = null;
            }
            if (serverSocket != null) {
                try {
                    serverSocket.dispose();
                }
                catch (Exception exception) {}
                serverSocket = null;
            }
        }
    }
}


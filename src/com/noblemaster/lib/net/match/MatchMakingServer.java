/*
 * Match-making service: accepts connections on a ServerSocket (one MatchRequestHandler thread per
 * client), keeps the live match list and periodically drops records whose timestamp is older than
 * expirySeconds. queryBuffer is the second list, a reusable reply buffer shared with the handlers.
 *
 * Deobfuscation: fields a..e became expirySeconds/matches/queryBuffer/serverSocket/thread; the
 * factory a(port, backlog, expirySeconds) became start and a() became stop (it clears the thread
 * reference, which ends the accept loop). Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.net.match.MatchList;
import com.noblemaster.lib.net.match.MatchRecord;
import com.noblemaster.lib.net.match.MatchRequestHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public final class MatchMakingServer
implements Runnable {
    private int expirySeconds;
    private MatchList matches;
    private MatchList queryBuffer;
    private ServerSocket serverSocket;
    private Thread thread;

    private MatchMakingServer(int i1, int i2, int i3)  throws IOException {
        this.expirySeconds = i3;
        this.matches = new MatchList(1024);
        this.queryBuffer = new MatchList(1024);
        this.serverSocket = new ServerSocket(i1, i2);
        this.serverSocket.setSoTimeout(5000);
        this.thread = new Thread(this);
        this.thread.start();
    }

    public static MatchMakingServer start(int i0, int i1, int i2) throws IOException {
        return new MatchMakingServer(i0, i1, i2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        long l1 = Long.MIN_VALUE;
        long l3 = Long.MIN_VALUE;
        Thread thread = Thread.currentThread();
        while (thread == this.thread) {
            try {
                Socket socket = this.serverSocket.accept();
                socket.setSoTimeout(5000);
                MatchRequestHandler matchRequestHandler = new MatchRequestHandler(socket, this.matches, this.queryBuffer, null);
                matchRequestHandler.start();
            }
            catch (SocketTimeoutException socketTimeoutException) {
            }
            catch (IOException iOException) {
                OsfLog.info("Connection error: " + iOException);
                OsfLog.logException(iOException);
            }
            if (System.currentTimeMillis() < l1) continue;
            l1 = System.currentTimeMillis() + (long)(this.expirySeconds / 8 * 1000);
            try {
                long l = System.currentTimeMillis() - (long)(this.expirySeconds * 1000);
                MatchList matchList = this.matches;
                synchronized (matchList) {
                    int i9 = 0;
                    while (i9 < this.matches.size()) {
                        MatchRecord matchRecord = (MatchRecord)this.matches.get(i9);
                        if (matchRecord.getTimestamp() < l) {
                            this.matches.remove(i9);
                            continue;
                        }
                        ++i9;
                    }
                }
            }
            catch (Exception exception) {
                OsfLog.info("Error refreshing matches: " + exception);
                OsfLog.logException(exception);
            }
            if (System.currentTimeMillis() < l3) continue;
            l3 = System.currentTimeMillis() + 120000L;
            MatchList matchList = this.matches;
            synchronized (matchList) {
                OsfLog.info("Matches in match making service: " + this.matches.size());
            }
        }
        try {
            this.serverSocket.close();
            this.serverSocket = null;
        }
        catch (IOException iOException) {}
    }

    public void stop() {
        this.thread = null;
    }
}


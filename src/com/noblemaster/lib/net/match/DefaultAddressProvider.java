/*
 * Default AddressProvider: getLocalAddress walks the NetworkInterfaces for the first site-local
 * (non-loopback, non-virtual) IPv4 address and falls back to InetAddress.getLocalHost();
 * getExternalAddress asks four "what is my IP" http services in turn.
 *
 * Deobfuscation: a()/b() became getLocalAddress/getExternalAddress (the interface names) and the
 * private a(String url) that reads one line -> fetchUrl. Evidence: run\map-net-match.tsv.
 */
package com.noblemaster.lib.net.match;

import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.net.match.AddressProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;

public final class DefaultAddressProvider
implements AddressProvider {
    @Override
    public String getLocalAddress() {
        String string = null;
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) continue;
                for (InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses())) {
                    if (!inetAddress.isSiteLocalAddress() || string != null && networkInterface.getDisplayName().contains("Virtual")) continue;
                    string = inetAddress.getHostAddress();
                }
            }
        }
        catch (Exception exception) {
            OsfLog.info("Cannot obtain local IP address via NetworkInterface. Using default method: " + exception);
            OsfLog.logException(exception);
        }
        if (string != null) {
            return string;
        }
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception exception) {
            OsfLog.info("Cannot obtain local IP address via InetAddress. Using local IP: " + exception);
            OsfLog.logException(exception);
            return "127.0.0.1";
        }
    }

    @Override
    public String getExternalAddress() {
        String[] stringArray = new String[]{"http://www.noblemaster.com/remote.html", "http://icanhazip.com/", "http://b10m.swal.org/ip", "http://whatismyip.akamai.com/"};
        int i2 = 0;
        while (i2 < stringArray.length) {
            String string = this.fetchUrl(stringArray[i2]);
            if (string != null) {
                return string;
            }
            ++i2;
        }
        return null;
    }

    private String fetchUrl(String string) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new URL(string).openStream()));
            String string2 = bufferedReader.readLine();
            return string2;
        }
        catch (IOException iOException) {
            OsfLog.info("Cannot obtain external IP address: " + iOException);
            OsfLog.logException(iOException);
            return null;
        }
        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    bufferedReader = null;
                }
                catch (IOException iOException) {}
            }
        }
    }
}


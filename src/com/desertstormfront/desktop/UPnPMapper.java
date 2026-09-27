/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.desktop;

import com.desertstormfront.desktop.PortProtocol;
import com.noblemaster.lib.log.OsfLog;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import net.sbbi.upnp.impls.InternetGatewayDevice;
import net.sbbi.upnp.messages.UPNPResponseException;

public final class UPnPMapper {
    private static Map a = new HashMap();

    private UPnPMapper() {
    }

    public static String mapPort(String string, String string2, int i2)  throws IOException {
        return UPnPMapper.mapPort(string, string2, i2, i2, 0);
    }

    public static String mapPort(String string, String string2, int i2, int i3, int i4)  throws IOException {
        return UPnPMapper.mapPort(string, string2, i2, i3, i4, PortProtocol.TCP);
    }

    private static String mapPort(String string, String string2, int i2, int i3, int i4, PortProtocol portProtocol)  throws IOException {
        try {
            InternetGatewayDevice internetGatewayDevice = UPnPMapper.discoverGateway();
            String string3 = portProtocol == PortProtocol.TCP ? "TCP" : "UDP";
            String string4 = null;
            boolean i9 = internetGatewayDevice.addPortMapping(string, string3, string4, i3, string2, i2, i4);
            if (!i9) {
                throw new IOException("error.MappingNotSuccessful[i18n]: Mapping not successful.");
            }
            Object[] objectArray = new Object[]{internetGatewayDevice, new Integer(i3), portProtocol};
            a.put(string, objectArray);
            return internetGatewayDevice.getExternalIPAddress();
        }
        catch (UPNPResponseException uPNPResponseException) {
            throw new IOException(uPNPResponseException);
        }
        catch (IOException iOException) {
            throw new IOException(iOException);
        }
    }

    public static void unmapPort(String string)  throws IOException {
        if (!a.containsKey(string)) {
            throw new IOException("error.MappingNotDefined[i18n]: Mapping not defined.");
        }
        Object[] objectArray = (Object[])a.get(string);
        InternetGatewayDevice internetGatewayDevice = (InternetGatewayDevice)objectArray[0];
        int i3 = (Integer)objectArray[1];
        PortProtocol portProtocol = (PortProtocol)((Object)objectArray[2]);
        UPnPMapper.deleteMapping(internetGatewayDevice, i3, portProtocol);
    }

    private static void deleteMapping(InternetGatewayDevice internetGatewayDevice, int i1, PortProtocol portProtocol)  throws IOException {
        try {
            String string = portProtocol == PortProtocol.TCP ? "TCP" : "UDP";
            String string2 = null;
            internetGatewayDevice.deletePortMapping(string2, i1, string);
        }
        catch (UPNPResponseException uPNPResponseException) {
            throw new IOException(uPNPResponseException);
        }
        catch (IOException iOException) {
            throw new IOException(iOException);
        }
    }

    private static InternetGatewayDevice discoverGateway()  throws IOException {
        try {
            int n = 1000;
            InternetGatewayDevice[] internetGatewayDeviceArray = InternetGatewayDevice.getDevices(n);
            if (internetGatewayDeviceArray == null) {
                throw new IOException("error.NoInternetGatewayDeviceFound[i18n]: No internet gateway devices found.");
            }
            return internetGatewayDeviceArray[0];
        }
        catch (IOException iOException) {
            throw new IOException(iOException);
        }
    }

    public static void main(String[] stringArray)  throws IOException {
        String string = "TestMap74";
        int i2 = 7331;
        String string2 = UPnPMapper.mapPort(string, "127.0.0.1", i2, i2, 300);
        OsfLog.info("Local port " + i2 + " mapped to " + string2 + ":" + i2);
        OsfLog.info("Use http://www.yougetsignal.com/tools/open-ports/ to check port " + i2 + "...");
        OsfLog.info("Also check your router for mapping: \"" + string + "\".");
        ServerSocket serverSocket = new ServerSocket(i2);
        serverSocket.setSoTimeout(60000);
        try {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            while (inputStream.available() > 0) {
                OsfLog.info("byte : " + inputStream.read());
            }
            OsfLog.info("SUCCESS: remote connection established.");
        }
        catch (SocketTimeoutException socketTimeoutException) {
            OsfLog.info("Timed out (FAILURE?): mapping did not work?");
        }
        UPnPMapper.unmapPort(string);
        OsfLog.info("Mapping removed.");
        OsfLog.info("Check your router and http://www.yougetsignal.com/tools/open-ports/ to verify mapping is gone.");
    }
}


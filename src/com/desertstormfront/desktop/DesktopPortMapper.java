/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.desktop;

import com.desertstormfront.desktop.DesktopApplication;
import com.desertstormfront.desktop.UPnPMapper;
import com.noblemaster.lib.net.match.PortMappingCallback;
import java.io.IOException;

class DesktopPortMapper
implements PortMappingCallback {
    final /* synthetic */ DesktopApplication a;

    DesktopPortMapper(DesktopApplication desktopApplication) {
        this.a = desktopApplication;
    }

    @Override
    public String mapPort(String string, int i2) {
        try {
            return UPnPMapper.mapPort(string, this.a.getAddressProvider().getLocalAddress(), i2);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }
}


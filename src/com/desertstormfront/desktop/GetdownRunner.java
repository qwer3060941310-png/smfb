/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.threerings.getdown.launcher.Getdown
 */
package com.desertstormfront.desktop;

import com.noblemaster.lib.io.FileUtils;
import com.noblemaster.lib.log.OsfLog;
import com.threerings.getdown.launcher.Getdown;
import java.io.File;

public final class GetdownRunner {
    private GetdownRunner() {
    }

    public static void main(String[] stringArray) throws Exception {
        String string = System.getProperty("user.dir");
        String string2 = String.valueOf(System.getProperty("user.home")) + "/.config/" + stringArray[0] + "/bin";
        File file = new File(String.valueOf(string2) + "/" + "getdown.txt");
        if (!file.exists()) {
            OsfLog.info("Copying getdown files: " + string + " >> " + string2);
            FileUtils.copyDirectory(new File(string), new File(string2));
            OsfLog.info("Copying getdown files completed.");
        }
        System.setProperty("getdown.runner.install.path", String.valueOf(string2) + "/");
        OsfLog.info("Starting getdown...");
        String[] stringArray2 = new String[2 + (stringArray.length - 1)];
        stringArray2[0] = string2;
        stringArray2[1] = "main";
        int i5 = 0;
        while (i5 < stringArray.length - 1) {
            stringArray2[i5 + 2] = stringArray[i5 + 1];
            ++i5;
        }
        Getdown.main((String[])stringArray2);
    }
}


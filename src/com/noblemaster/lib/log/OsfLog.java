/*
 * Logging facade over Gdx.app: logSystemInfo dumps the JRE/OS/user environment once at startup,
 * info/error map to Gdx.app.log/error, while print and logException are suppressed on iOS.
 *
 * Deobfuscation: a(String)/b(String)/c(String)/d(String)/a(Throwable) became
 * logSystemInfo/print/info/error/logException. Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.log;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import java.nio.charset.Charset;

public final class OsfLog {
    public static void logSystemInfo(String string) {
        Gdx.app.log("OSF", "Application\n  application = " + string);
        Gdx.app.log("OSF", "Java Runtime Environment\n  java.runtime.name = " + System.getProperty("java.runtime.name") + "\n" + "  java.runtime.version = " + System.getProperty("java.runtime.version") + "\n" + "  java.vendor = " + System.getProperty("java.vendor") + "\n" + "  java.vendor.url = " + System.getProperty("java.vendor.url") + "\n" + "  java.specification.name = " + System.getProperty("java.specification.name") + "\n" + "  java.specification.version = " + System.getProperty("java.specification.version") + "\n" + "  java.specification.vendor = " + System.getProperty("java.specification.vendor") + "\n" + "  java.home = " + System.getProperty("java.home"));
        Gdx.app.log("OSF", "Java Virtual Machine\n  java.vm.name = " + System.getProperty("java.vm.name") + "\n" + "  java.vm.version = " + System.getProperty("java.vm.version") + "\n" + "  java.vm.vendor = " + System.getProperty("java.vm.vendor") + "\n" + "  java.vm.specification.name = " + System.getProperty("java.vm.specification.name") + "\n" + "  java.vm.specification.version = " + System.getProperty("java.vm.specification.version") + "\n" + "  java.vm.specification.vendor = " + System.getProperty("java.vm.specification.vendor") + "\n" + "  java.class.version = " + System.getProperty("java.class.version"));
        Gdx.app.log("OSF", "Operating System\n  os.name = " + System.getProperty("os.name") + "\n" + "  os.version = " + System.getProperty("os.version") + "\n" + "  os.arch = " + System.getProperty("os.arch") + "\n" + "  file.encoding = " + System.getProperty("file.encoding") + " (default charset: " + Charset.defaultCharset().displayName() + ")");
        Gdx.app.log("OSF", "User Environment\n  user.name = " + System.getProperty("user.name") + "\n" + "  user.home = " + System.getProperty("user.home") + "\n" + "  user.dir = " + System.getProperty("user.dir") + "\n" + "  user.country = " + System.getProperty("user.country") + "\n" + "  user.language = " + System.getProperty("user.language") + "\n" + "  user.timezone = " + System.getProperty("user.timezone") + "\n" + "  user.variant = " + System.getProperty("user.variant"));
    }

    public static void print(String string) {
        if (Gdx.app.getType() != Application.ApplicationType.iOS) {
            System.out.println(string);
        }
    }

    public static void info(String string) {
        Gdx.app.log("OSF", string);
    }

    public static void error(String string) {
        Gdx.app.error("OSF", string);
    }

    public static void logException(Throwable throwable) {
        if (Gdx.app.getType() != Application.ApplicationType.iOS) {
            throwable.printStackTrace();
        }
    }
}


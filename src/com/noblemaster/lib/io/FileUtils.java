/*
 * File-system helpers: a chunked file copy (FileChannel.transferTo in 64K blocks) and a recursive
 * directory copy that reuses it.
 *
 * Deobfuscation: a(File,File) / b(File,File) became copyFile / copyDirectory.
 * Evidence: run\map-lib-io-util-log.tsv.
 */
package com.noblemaster.lib.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public final class FileUtils {
    public static void copyFile(File file, File file2)  throws Exception {
        try {
            if (!file2.exists()) {
                file2.createNewFile();
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            FileChannel fileChannel = fileInputStream.getChannel();
            FileChannel fileChannel2 = fileOutputStream.getChannel();
            try {
                long l6 = file.length();
                long l8 = 0L;
                long l10 = 0L;
                long l12 = Math.min(65536L, l6);
                do {
                    l10 = fileChannel.transferTo(l8, l12, fileChannel2);
                    l8 += l10;
                } while ((l6 -= l10) > 0L);
            }
            catch (Throwable throwable) {
                try {
                    fileChannel.close();
                }
                catch (IOException iOException) {}
                try {
                    fileChannel2.close();
                }
                catch (IOException iOException) {}
                try {
                    fileInputStream.close();
                }
                catch (IOException iOException) {}
                try {
                    fileOutputStream.close();
                }
                catch (IOException iOException) {}
                throw throwable;
            }
            try {
                fileChannel.close();
            }
            catch (IOException iOException) {}
            try {
                fileChannel2.close();
            }
            catch (IOException iOException) {}
            try {
                fileInputStream.close();
            }
            catch (IOException iOException) {}
            try {
                fileOutputStream.close();
            }
            catch (IOException iOException) {}
        }
        catch (Exception exception) {
            throw new Exception("Error Copying File: " + file + " > " + file2 + " (" + exception + ")");
        }
    }

    public static void copyDirectory(File file, File file2) throws Exception {
        File[] fileArray;
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File[] fileArray2 = fileArray = file.listFiles();
        int i5 = fileArray.length;
        int i4 = 0;
        while (i4 < i5) {
            File file3 = fileArray2[i4];
            String string = file3.getName();
            File file4 = new File(file2, string);
            if (file3.isDirectory()) {
                FileUtils.copyDirectory(file3, file4);
            } else {
                FileUtils.copyFile(file3, file4);
            }
            ++i4;
        }
    }
}


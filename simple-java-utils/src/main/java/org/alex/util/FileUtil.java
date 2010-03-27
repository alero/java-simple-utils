package org.alex.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */                               
public class FileUtil {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private FileUtil() {
    }


    public static void move(
            File fileIn,
            File fileOut)
            throws IOException {
        copy(fileIn, fileOut);
        fileIn.delete();
    }

    public static void copy(
            File fileIn,
            File fileOut)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int n = 0;
        InputStream input = new FileInputStream(fileIn);
        OutputStream output = new FileOutputStream(fileOut);
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        output.flush();
        SocketCloser.close(input);
        SocketCloser.close(output);
    }
}
/*
 * Copyright (c) 2010.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.directus.util;


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
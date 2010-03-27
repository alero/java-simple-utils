package org.alex.util;

import org.alex.logging.SimpleLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class SocketCloser {

    private SocketCloser() {
    }

    private static final SimpleLogger LOGGER = SimpleLogger.getInstance(SocketCloser.class);

    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }

    }

    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }

    }

    public static void close(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }

    public static void close(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }
}

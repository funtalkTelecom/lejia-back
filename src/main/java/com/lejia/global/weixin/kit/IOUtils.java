package com.lejia.global.weixin.kit;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

public abstract class IOUtils {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    public IOUtils() {
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
            ;
        }

    }

    public static String toString(InputStream input) throws IOException {
        return toString(input, Charset.forName("UTF-8"));
    }

    public static String toString(InputStream input, Charset charset) throws IOException {
        InputStreamReader in = new InputStreamReader(input, charset);
        StringBuffer out = new StringBuffer();
        char[] c = new char[4096];

        int n;
        while((n = in.read(c)) != -1) {
            out.append(new String(c, 0, n));
        }

        closeQuietly(in);
        closeQuietly(input);
        return out.toString();
    }

    public static void toFile(InputStream input, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        byte[] buffer = new byte[4096];

        while((bytesRead = input.read(buffer, 0, 4096)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        closeQuietly(os);
        closeQuietly(input);
    }
}

package com.nuttwarunyu.blankbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dell-NB on 24/12/2558.
 */
public class Utils {
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.nuttwarunyu.blankbook;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Dell-NB on 24/12/2558.
 */
public class MemoryCache {

    private static String TAG = "MemoryCache";
    // Last argument true for LRU ordering
    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
    // Current allocated size
    private long size = 0;
    // Max memory in bytes
    private long limit = 1000000;

    public MemoryCache() {
        // Use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long new_limit) {
        limit = new_limit;
        Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
    }

    public Bitmap get(String id) {
        try {
            if (!cache.containsKey(id)){
                Log.d("MemoryCache get. . . .", "  cache is " + cache.containsKey(id));
                return null;}
            return cache.get(id);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bitmap) {
        try {
            if (cache.containsKey(id)) {
                size -= getSizeInBytes(cache.get(id));
                cache.put(id, bitmap);
                size += getSizeInBytes(bitmap);
                checkSize();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void checkSize() {
        Log.i(TAG, "cache size=" + size + " length=" + cache.size());
        if (size > limit) {
            // Least recently accessed item will be the first one iterated
            Iterator<Entry<String, Bitmap>> entryIterator = cache.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Entry<String, Bitmap> entry = entryIterator.next();
                size -= getSizeInBytes(entry.getValue());
                entryIterator.remove();
                if (size <= limit)
                    break;
            }
            Log.i(TAG, "Clean cache. New size " + cache.size());
        }
    }

    long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public void clear() {
        try {
            cache.clear();
            size = 0;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}

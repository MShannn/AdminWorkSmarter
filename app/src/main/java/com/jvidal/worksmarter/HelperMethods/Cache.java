package com.jvidal.worksmarter.HelperMethods;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

public class Cache {

    private static Cache instance;
    private LruCache<Object, Object> lru;
    private LruCache<String, Bitmap> imageCache;


    private Cache() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            lru = new LruCache<Object, Object>(1024);
        }

    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            imageCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return imageCache.get(key);
    }

    public static Cache getInstance() {

        if (instance == null) {

            instance = new Cache();
        }

        return instance;

    }

    public LruCache<Object, Object> getLru() {
        return lru;
    }
}
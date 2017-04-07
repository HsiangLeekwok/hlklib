package com.hlk.hlklib.etc;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

/**
 * <b>功能：</b>字体加载和缓存服务类<br />
 * <b>作者：</b>Hsiang Leekwok <br />
 * <b>时间：</b>2016/07/08 16:26 <br />
 * <b>邮箱：</b>xiang.l.g@gmail.com <br />
 */
public class TypefaceHelper {

    private static final String TAG = "TypefaceHelper";

    private static final Hashtable<String, Typeface> cache = new Hashtable<>();

    public static Typeface get(Context context, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e(TAG, "could not get typeface \"" + assetPath + "\" because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
}

package com.gusar.datingapp.imagesdownloader;

import java.io.File;
import android.content.Context;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        String cachePath = null;
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            cachePath = "JsonParseTutorialCache";
//            if (context.getExternalCacheDir() != null ) {
//                cachePath = context.getExternalCacheDir().getPath();
//            } else {
//                if (context.getCacheDir() != null) {
//                    cachePath = context.getCacheDir().getPath();
//                }
//            }
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    cachePath);
            //              "JsonParseTutorialCache");
            //              context.getExternalFilesDir(null).toString());
        }
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        // String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}

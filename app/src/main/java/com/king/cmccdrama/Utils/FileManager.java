package com.king.cmccdrama.Utils;

import android.util.Log;

import java.io.File;

public class FileManager {
    /**
     * 创建文件夹
     */
    public static boolean makeDir(String filePath) {
        File file;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                return file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 文件/文件夹是否存在
     */
    public static boolean fileExists(String file) {
        File f = new File(file);
        return f.exists();
    }

    /**
     * 删除文件
     * @param file
     * @return
     */
    public static boolean delFile(String file) {
        File f = new File(file);
        return f.delete();
    }

    /**
     * 删除path下的所有文件
     * @param path
     */
    public static void delFiles(String path) {
        File dir = new File(path);
        final File[] jpgFiles = dir.listFiles();
        for (File file :jpgFiles) {
            if (file.isFile()) {
                Log.i("king","删除文件：" + file.getAbsolutePath());
                file.delete();
            }
        }
    }
}

package com.bbk.util;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class SDUtil {


    private static final String FOLDER = "1607" + File.separator + "cacheDemo";


    public File getDir() {
        if (!Environment.MEDIA_MOUNTED.equals(
                Environment.getExternalStorageState())) {
            //如果没有挂载
            throw new IllegalStateException("SD卡未挂载");
        }

        File root = Environment.getExternalStorageDirectory();

        String realPath = root.getAbsolutePath() + File.separator + FOLDER;

        File file = new File(realPath);

        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }


    /**
     * 获取图片数据
     *
     * @param key 图片地址
     * @return 图片数据的字节数组
     */
    public byte[] get(String key) {
        byte[] ret = null;


        String fileName = getFileName(key);
        if (TextUtils.isEmpty(fileName)) {
            return ret;
        }

        File dir = getDir();

        File file = new File(dir, fileName);

        if (!file.exists()) {
            //如果不存在，直接返回。没法获取
            return ret;
        }

        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();

            int len = 0;
            byte[] buffer = new byte[512];

            while (-1 != (len = fis.read(buffer))) {
                bos.write(buffer, 0, len);
            }

            ret = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return ret;
    }

    /**
     * 存储图片
     *
     * @param key   图片地址
     * @param value 图片数据
     */
    public void put(String key, byte[] value) {
        String fileName = getFileName(key);
        File dir = getDir();

        File file = new File(dir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(value);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //  http://www.baidu.com .这样，它返回的null.
    public String getFileName(String key) {
        String last = Uri.parse(key).getLastPathSegment();
        return last;
    }

}

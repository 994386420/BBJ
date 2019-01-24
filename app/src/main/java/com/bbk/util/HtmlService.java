package com.bbk.util;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 获取HTML数据
 *
 * @author David
 *
 */
public class HtmlService {

    /**
     * 获取网址的html
     * @throws Exception
     */
    public static byte[] testGetHtml(String urlpath) throws Exception
    {
        URL url=new URL(urlpath);
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(6*1000);  //设置链接超时时间6s

        conn.setRequestMethod("GET");

        if(conn.getResponseCode()==200)
        {
            InputStream inputStream=conn.getInputStream();
            byte[] data=readStream(inputStream);
            return data;
        }
        return null;
    }

    /**
     * 读取数据
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inputStream) throws Exception
    {
        byte[] buffer=new byte[1024];
        int len=-1;
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        while((len=inputStream.read(buffer))!=-1)
        {
            byteArrayOutputStream.write(buffer,0,len);
        }

        inputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
    /**
     * 读取输入流，得到html的二进制数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}
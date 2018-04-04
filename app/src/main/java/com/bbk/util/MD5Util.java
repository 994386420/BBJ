package com.bbk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

public class MD5Util {
	
	public static String phonemd5(String phone) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmm");
		String date = sDateFormat.format(new java.util.Date());
		String v = Md5(phone+date);
		v = ("worinimama"+v);
		v = Md5(v);
		v = (v+"cao");
		v = Md5(v);
		v = v.substring(0,16);
		v = "bbj"+v;
		v = Md5(v);
		return v;
	}
	
	
	public static String Md5(String plainText ) { 
		String md5Result = "";
		try { 
		MessageDigest md = MessageDigest.getInstance("MD5"); 
		md.update(plainText.getBytes()); 
		byte b[] = md.digest(); 

		int i; 

		StringBuffer buf = new StringBuffer(""); 
		for (int offset = 0; offset < b.length; offset++) { 
		i = b[offset]; 
		if(i<0) i+= 256; 
		if(i<16) 
		buf.append("0"); 
		buf.append(Integer.toHexString(i)); 
		} 


		md5Result = buf.toString();//16位的加密 

		} catch (NoSuchAlgorithmException e) { 

		}
		return md5Result; 
	} 
	/** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
  
    }
	public static void main(String[] args) {

String s = "1";
System.out.println(Md5(s));
System.out.println(Md5("c4ca4238a0b923820dcc509a6f75849b"));
System.out.println(phonemd5("18008099823"));
	}
	
	/*** 
     * MD5加码 生成32位md5码 
     */  
    public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = (md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
  
    }  
}

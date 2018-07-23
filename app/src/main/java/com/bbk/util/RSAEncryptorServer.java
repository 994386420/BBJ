package com.bbk.util;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;

public class RSAEncryptorServer {
    static String private_key=    
             "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ6XniVgd+e7yQXPx059U8Di5puh3N5R6x74WibnOXoUIMutHot266RbH83ftBgRdajrSpZhQ/sm5qZGfajOxwyuuplWoNw2zIqTRc7X296tl+TSLPdPHbmoS/p9WXkYF7J0sxEET+1ryvpgj8t+k1U6ssmAn9gEFxLKnjovZJxXAgMBAAECgYEAlhf2o3c2ZAo0iZyJC37PamkUSc0aJQqwlKIFaHZDzocINtuEYgfNosJfSk/iuTik+5acA9DQLv01I77pJH92n8uy7KFHPt+sVU+vyAV/7xKxWrEQRx+3IjxnT4MR08aL8MVnIzXn6imi4tij8J1dXn+GIaMclhF5eM2LT64z6PECQQDNXzjgqz2oBTC9hGYaGDCS2j46qdHfj9+0ia09MIK/UWZx6WEqVfcinbIV2LVebP7V30OgsnYKer5psAwLX9fJAkEAxbAvQfFXwMj5GP+KJJTYMErMATTADzMbdaUfIkwO1TljI6Vs6vKYK9woL+AzxF+or43FD0FABedjhqh/OIEjHwJBAJBAr0HX5hHru2WS5uizTwMHeqhX+guvEjHMdrvBJEVf1rZyeAZ1pYZR//GqaxKtOn5dOTJZx+Hqf27a8krXAkkCQC/CXdhnjxTDOPLiIaSu1P/twhV0ggXqCfNFHh1yFI9M2vesqoCkdUD7YjYWy0rg9WbcMER8NhR3D2Nj58Bl6m8CQDl+kAJk5N09lbjmTdOuMwxtlKK+eqepRAoOd3H8VoaHI+gTq9w8WoPWbhBeNGJbZQjrlIz6OMO1UYF9/t14awI=" ;    
     

    public static void main(String[] args) throws Exception{  
        try {
            // NSLog the encrypt string from Xcode , and paste it here.
            // 请粘贴来自IOS端加密后的字符串
            String rsaBase46StringFromIOS =
                    "ZOpmIn1Jor9BFjbImdHe1PSIVqSrjrk7rs3KbtdqCqCUmYTLQh0AjJY+da4YZqf7MECvnYu/jCVSomOZPErbM8x9jpz7JKPiVrJN4IVHnuA7++gYY+eVVmJu4H2M4Rj/mqq2FDiurKX1yaM4+yHvDEi+qhSjfsFEpjKlYdWFumQ=";
            
            String decryptStringFromIOS = decryptWithBase64(rsaBase46StringFromIOS);
            System.out.println("Decrypt result from ios client: \n" + decryptStringFromIOS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String decryptWithBase64(String base64String){
    	 try {  
//             BASE64Decoder base64Decoder= new BASE64Decoder();
             byte[] buffer= Base64.decode(private_key,Base64.DEFAULT);
             PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);  
             KeyFactory keyFactory= KeyFactory.getInstance("RSA");  
             RSAPrivateKey privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
             byte[] binaryData = decrypt(privateKey, new BASE64Decoder().decodeBuffer(base64String) /*org.apache.commons.codec.binary.Base64.decodeBase64(base46String.getBytes())*/);
             String string = new String(binaryData);
             return string;
         }  catch (Exception e) {  
            
         }  
    	
    	 return "";
       
    }


    /** 
     * 私钥 
     */  
    private RSAPrivateKey privateKey;  


    /** 
     * 获取私钥 
     * @return 当前的私钥对象 
     */  
    public RSAPrivateKey getPrivateKey() {  
        return privateKey;  
    }  





    /** 
     * 加密过程 
     * @param publicKey 公钥 
     * @param plainTextData 明文数据 
     * @return 
     * @throws Exception 加密过程中的异常信息 
     */  
    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception{  
        if(publicKey== null){  
            throw new Exception("加密公钥为空, 请设置");  
        }  
        Cipher cipher= null;  
        try {  
            cipher= Cipher.getInstance("RSA");//, new BouncyCastleProvider());  
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
            byte[] output= cipher.doFinal(plainTextData);  
            return output;  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此加密算法");  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        }catch (InvalidKeyException e) {  
            throw new Exception("加密公钥非法,请检查");  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("明文长度非法");  
        } catch (BadPaddingException e) {  
            throw new Exception("明文数据已损坏");  
        }  
    }  

    /** 
     * 解密过程 
     * @param privateKey 私钥 
     * @param cipherData 密文数据 
     * @return 明文 
     * @throws Exception 解密过程中的异常信息 
     */  
    private static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception{  
        if (privateKey== null){  
            throw new Exception("解密私钥为空, 请设置");  
        }  
        Cipher cipher= null;  
        try {  
            cipher= Cipher.getInstance("RSA");//, new BouncyCastleProvider());  
            cipher.init(Cipher.DECRYPT_MODE, privateKey);  
            byte[] output= cipher.doFinal(cipherData);  
            return output;  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此解密算法");  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        }catch (InvalidKeyException e) {  
            throw new Exception("解密私钥非法,请检查");  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("密文长度非法");  
        } catch (BadPaddingException e) {  
            throw new Exception("密文数据已损坏");  
        }         
    }  



    /** 
     * 字节数据转字符串专用集合 
     */  
    private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'}; 

    /** 
     * 字节数据转十六进制字符串 
     * @param data 输入数据 
     * @return 十六进制内容 
     */  
    public static String byteArrayToString(byte[] data){  
        StringBuilder stringBuilder= new StringBuilder();  
        for (int i=0; i<data.length; i++){  
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移  
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);  
            //取出字节的低四位 作为索引得到相应的十六进制标识符  
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);  
            if (i<data.length-1){  
                stringBuilder.append(' ');  
            }  
        }  
        return stringBuilder.toString();  
    }  

}
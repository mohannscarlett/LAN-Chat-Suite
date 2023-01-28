package main.backend.utilities;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/27/2023
 */
public class EncryptString {
    
	public static String encryptString (String dataToEncrypt,String UNICODE_FORMAT, SecretKey key){
    	try{
            Cipher cipher;
            cipher = Cipher.getInstance("AES");
            byte[] text = dataToEncrypt.getBytes(UNICODE_FORMAT);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] textEncrypted = cipher.doFinal(text);
            return Base64.getEncoder().encodeToString(textEncrypted);
        }catch (Exception m){
            return null;
        }

    }

    public static String decryptString (String dataToDecrypt, SecretKey key) {
        try {
        	byte[] decodedKey = Base64.getDecoder().decode(dataToDecrypt);
            Cipher cipher;
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] textDecrypted = cipher.doFinal(decodedKey);
            String result = new String(textDecrypted);
            return result;
        } catch (Exception n) {
        	System.out.println(n);
            return null;
        }
    }
 }
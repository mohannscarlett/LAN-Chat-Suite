package main.serverbackend.utilities;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/**
 *  
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/25/2023
 */
public class EncryptString {
    
	//method for encrypting a string using AES with a given key and format
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

	//method for decrypting a string under AES with a given key
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
/**
 * 
 */
package com.project.encrypt.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author user on 2022-09-06 13:12:54.496
 *
 */
public class EncryptionBouncyCastelUtil {
	
	private final static String KEY_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    private final static String MODE_PADDING = "/CBC/PKCS5Padding";
    private final static int DEFAULT_SALT_SIZE_BYTES = 16;
    
    private static SecureRandom rand = new SecureRandom();
    
    public EncryptionBouncyCastelUtil() throws Exception {
    	rand = SecureRandom.getInstance("SHA1PRNG");
        
    }
	
	private static byte[] generateSalt(int size) {
        byte[] salt = new byte[size];
        rand.nextBytes(salt);
        
        return salt;
    }
    
    private static SecretKey generateKey(String algorithm, String secretKey, int keySize, byte[] salt) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException{
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(secretKey.toCharArray(), salt, 100000);
        SecretKey tmpKey = factory.generateSecret(pbeKeySpec);
        byte[] keyBytes = new byte[keySize / 8];
        System.arraycopy(tmpKey.getEncoded(), 0, keyBytes, 0, keyBytes.length);
        
        return new SecretKeySpec(keyBytes, algorithm);
    }
    
    private static byte[] generateIV(Cipher cipher) {
        byte[] iv = new byte[cipher.getBlockSize()];
        rand.nextBytes(iv);
        
        return iv;
    }

    private static byte[] appendArrays(byte[] firstArray, byte[] secondArray) {
        final byte[] result = new byte[firstArray.length + secondArray.length];
        
        System.arraycopy(firstArray, 0, result, 0, firstArray.length);
        System.arraycopy(secondArray, 0, result, firstArray.length, secondArray.length);
        
            return result;
    }

    
    public static byte[] encrypt(String algorithm, int keySize, final byte[] message, String secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm + MODE_PADDING);

        // The salt size for the chosen algorithm is set to be equal 
        // to the algorithm's block size (if it is a block algorithm).
        int saltSizeBytes = DEFAULT_SALT_SIZE_BYTES;
        int algorithmBlockSize = cipher.getBlockSize();
        if (algorithmBlockSize > 0) {
            saltSizeBytes = algorithmBlockSize;
        }

        // Create salt
        final byte[] salt = generateSalt(saltSizeBytes);

        SecretKey key = generateKey(algorithm, secretKey, keySize, salt);

        // create a new IV for each encryption
        final IvParameterSpec ivParamSpec = new IvParameterSpec(generateIV(cipher));

        // Perform encryption using the Cipher
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParamSpec);
        byte[] encryptedMessage = cipher.doFinal(message);

        // append the IV and salt
        encryptedMessage = appendArrays(ivParamSpec.getIV(), encryptedMessage);
        encryptedMessage = appendArrays(salt, encryptedMessage);

        return encryptedMessage;
    }
    
    public static byte[] decrypt(String algorithm, int keySize, final byte[] encryptedMessage, String secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm + MODE_PADDING);
        
        // determine the salt size for the first layer of encryption
        int saltSizeBytes = DEFAULT_SALT_SIZE_BYTES;
        int algorithmBlockSize = cipher.getBlockSize();
        if (algorithmBlockSize > 0) {
            saltSizeBytes = algorithmBlockSize;
        }
        
        byte[] decryptedMessage = new byte[encryptedMessage.length];
        System.arraycopy(encryptedMessage, 0, decryptedMessage, 0, encryptedMessage.length);

        // extract the salt and IV from the incoming message
        byte[] salt = null;
        byte[] iv = null;
        byte[] encryptedMessageKernel = null;
        final int saltStart = 0;
        final int saltSize = (saltSizeBytes < decryptedMessage.length ? saltSizeBytes : decryptedMessage.length);
        final int ivStart = (saltSizeBytes < decryptedMessage.length ? saltSizeBytes : decryptedMessage.length);
        final int ivSize = cipher.getBlockSize();
        final int encMesKernelStart = (saltSizeBytes + ivSize < decryptedMessage.length ? saltSizeBytes + ivSize : decryptedMessage.length);
        final int encMesKernelSize = (saltSizeBytes + ivSize < decryptedMessage.length ? (decryptedMessage.length - saltSizeBytes - ivSize) : 0);

        salt = new byte[saltSize];
        iv = new byte[ivSize];
        encryptedMessageKernel = new byte[encMesKernelSize];

        System.arraycopy(decryptedMessage, saltStart, salt, 0, saltSize);
        System.arraycopy(decryptedMessage, ivStart, iv, 0, ivSize);
        System.arraycopy(decryptedMessage, encMesKernelStart, encryptedMessageKernel, 0, encMesKernelSize);
        
        SecretKey key = generateKey(algorithm, secretKey, keySize, salt);
        
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);

        // Perform decryption using the Cipher
        cipher.init(Cipher.DECRYPT_MODE, key, ivParamSpec);
        decryptedMessage = cipher.doFinal(encryptedMessageKernel);

        // Return the results
        return decryptedMessage;
    }

}

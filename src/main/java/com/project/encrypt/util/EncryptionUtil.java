/**
 * 
 */
package com.project.encrypt.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author user on 2022-09-05 16:02:51.960
 *
 */
public class EncryptionUtil {
	
	public static byte[] digest(byte[] input, String algorithm) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(input);
        return result;
    }
	
	public static String bytesToHex1(byte[] bytes) {
	    StringBuilder hexString = new StringBuilder(2 * bytes.length);
	    for (int i = 0; i < bytes.length; i++) {
	        String hex = Integer.toHexString(0xff & bytes[i]);
	        if(hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	public static String bytesToHex2(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
	
	public static String bytesToHex3(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
	    int i;

	    for (i = 0; i < buf.length; i++) {
	     if (((int) buf[i] & 0xff) < 0x10)
	    strbuf.append("0");

	     strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
	    }

	    return strbuf.toString();
    }
	
	public static byte[] stringToBytes(String str) {
        final byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

}

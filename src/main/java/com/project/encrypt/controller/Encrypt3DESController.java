/**
 * 
 */
package com.project.encrypt.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.encrypt.DTO.EncryptRequest;
import com.project.encrypt.DTO.GeneralEncryptDecryptResponse;
import com.project.encrypt.constant.EncryptionValueConstant;
import com.project.encrypt.enumeration.EncryptionOperationEnum;
import com.project.encrypt.util.EncryptionUtil;

/**
 * @author user on 2022-09-05 16:50:48.731
 *
 */

@RestController
public class Encrypt3DESController {
	
	@PostMapping("/3des")
	public ResponseEntity<GeneralEncryptDecryptResponse> encrypt3DES(@RequestBody EncryptRequest request) {

		GeneralEncryptDecryptResponse resp = new GeneralEncryptDecryptResponse();
		resp.setStatus(EncryptionValueConstant.STATUS_ERROR);
		resp.setMessage(EncryptionValueConstant.STATUS_BAD_REQUEST);
		if (StringUtils.hasText(request.getOperationMode()) 
				&& StringUtils.hasText(request.getInput())
				&& StringUtils.hasText(request.getSecret())) {
			String encryptStr = null;
			try {
				switch (EncryptionOperationEnum.of(request.getOperationMode())) {
				case ENCRYPT:
					encryptStr = encrypt(request.getInput(), request.getSecret());
					break;
				case DECRYPT:
					encryptStr = decrypt(request.getInput(), request.getSecret());
					break;
				default:
					break;
				}

				if (StringUtils.hasText(encryptStr)) {
					resp.setStatus(EncryptionValueConstant.STATUS_SUCCESS);
					resp.setMessage(EncryptionValueConstant.STATUS_SUCCESS);
					resp.setEncrypt(encryptStr);
					return ResponseEntity.ok(resp);
				}
				
				resp.setMessage(EncryptionValueConstant.STATUS_UNKNOWN_OPERATION_MODE);
				return ResponseEntity.ok(resp);
			} catch (Exception e) {
				
				resp.setMessage(e.getMessage());
				return ResponseEntity.ok(resp);
			}
		} else {
			return ResponseEntity.ok(resp);
		}
	}
	
	private String encrypt(String input, String secret) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest(secret
                .getBytes(StandardCharsets.UTF_8));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, EncryptionValueConstant.ENCRYPTION_SCHEME_TRIPLEDES);
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher cipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        final byte[] plainTextBytes = input.getBytes(StandardCharsets.UTF_8);
        final byte[] cipherText = cipher.doFinal(plainTextBytes);

        return EncryptionUtil.bytesToHex1(cipherText);
    }

    private String decrypt(String input, String secret) throws Exception {
    	
    	byte[] inputByte = EncryptionUtil.stringToBytes(input);
    	
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest(secret
                .getBytes(StandardCharsets.UTF_8));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, EncryptionValueConstant.ENCRYPTION_SCHEME_TRIPLEDES);
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher decipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, key, iv);

        final byte[] plainText = decipher.doFinal(inputByte);

        return new String(plainText, StandardCharsets.UTF_8);
    }

}

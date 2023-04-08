/**
 * 
 */
package com.project.encrypt.controller;

import javax.crypto.Cipher;
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
public class EncryptBlowfishController {
	
	@PostMapping("/blowfish")
	public ResponseEntity<?> encryptBlowfish(@RequestBody EncryptRequest request) {

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
				
				e.printStackTrace();
				resp.setMessage(e.getMessage());
				return ResponseEntity.ok(resp);
			}
		} else {
			return ResponseEntity.ok(resp);
		}
	}
	
	private String encrypt(String input, String secret) throws Exception {
		
		byte[] secretByte = secret.getBytes();
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretByte, EncryptionValueConstant.ENCRYPTION_SCHEME_BLOWFISH);
		Cipher cipher = Cipher.getInstance(EncryptionValueConstant.ENCRYPTION_SCHEME_BLOWFISH);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		byte[] encrypt = cipher.doFinal(input.getBytes());
//		return new String(Base64.getEncoder().encode(encrypt));
		return EncryptionUtil.bytesToHex3(encrypt);
		
    }

    private String decrypt(String input, String secret) throws Exception {
    	
    	byte[] secretByte = secret.getBytes();
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretByte, EncryptionValueConstant.ENCRYPTION_SCHEME_BLOWFISH);
		Cipher cipher = Cipher.getInstance(EncryptionValueConstant.ENCRYPTION_SCHEME_BLOWFISH);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//		byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(input));
		byte[] decrypt = cipher.doFinal(EncryptionUtil.stringToBytes(input));
		return new String(decrypt);
		
    }

}

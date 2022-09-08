/**
 * 
 */
package com.project.encrypt.controller;

import java.security.Security;
import java.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.encrypt.DTO.EncryptRequest;
import com.project.encrypt.DTO.GeneralEncryptDecryptResponse;
import com.project.encrypt.constant.EncryptionValueConstant;
import com.project.encrypt.enumeration.EncryptionOperationEnum;
import com.project.encrypt.util.EncryptionBouncyCastelUtil;

/**
 * @author user on 2022-09-05 16:50:48.731
 *
 */

@RestController
public class EncryptGostController {
	
	@PostMapping("/gost")
	public ResponseEntity<?> encryptGost(@RequestBody EncryptRequest request) {

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
					encryptStr = encrypt(request.getInput(), request.getSecret(), 256);
					break;
				case DECRYPT:
					encryptStr = decrypt(request.getInput(), request.getSecret(), 256);
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
				// TODO: handle exception
				e.printStackTrace();
				resp.setMessage(e.getMessage());
				return ResponseEntity.ok(resp);
			}
		} else {
			return ResponseEntity.ok(resp);
		}
	}
    
	private String encrypt(String input, String secret, int bitNum) throws Exception {

		Security.addProvider(new BouncyCastleProvider());

		byte[] encryptedMessage = EncryptionBouncyCastelUtil.encrypt(EncryptionValueConstant.ENCRYPTION_SCHEME_GOST,
				bitNum, input.getBytes(), secret);

		return (new String(Base64.getEncoder().encode(encryptedMessage)));

	}

	private String decrypt(String input, String secret, int bitNum) throws Exception {

		Security.addProvider(new BouncyCastleProvider());

		byte[] decryptedMessage = EncryptionBouncyCastelUtil.decrypt(EncryptionValueConstant.ENCRYPTION_SCHEME_GOST,
				bitNum, Base64.getDecoder().decode(input), secret);

		return new String(decryptedMessage);

	}

}

/**
 * 
 */
package com.project.encrypt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.encrypt.DTO.EncryptRequest;
import com.project.encrypt.DTO.GeneralEncryptDecryptResponse;
import com.project.encrypt.constant.EncryptionValueConstant;
import com.project.encrypt.enumeration.EncryptionOperationEnum;
import com.project.encrypt.util.EncryptionRSAUtil;

/**
 * @author user on 2022-09-05 16:50:48.731
 *
 */

@RestController
public class EncryptRSAController {
	
	@PostMapping("/rsa")
	public ResponseEntity<?> encryptRSA(@RequestBody EncryptRequest request) {

		GeneralEncryptDecryptResponse resp = new GeneralEncryptDecryptResponse();
		resp.setStatus(EncryptionValueConstant.STATUS_ERROR);
		resp.setMessage(EncryptionValueConstant.STATUS_BAD_REQUEST);
		if (StringUtils.hasText(request.getOperationMode()) 
				&& StringUtils.hasText(request.getInput())
				&& StringUtils.hasText(request.getPrivateKey())) {
			String encryptStr = null;
			try {
				switch (EncryptionOperationEnum.of(request.getOperationMode())) {
				case ENCRYPT:
					encryptStr = encrypt(request.getInput(), request.getPrivateKey());
					break;
				case DECRYPT:
					encryptStr = decrypt(request.getInput(), request.getPrivateKey());
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
    
	private String encrypt(String input, String secret) throws Exception {

		return EncryptionRSAUtil.encrypt(input, secret);

	}

	private String decrypt(String input, String secret) throws Exception {

		return EncryptionRSAUtil.decrypt(input, secret);

	}

}

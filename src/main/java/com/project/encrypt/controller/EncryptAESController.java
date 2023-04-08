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
import com.project.encrypt.enumeration.EncryptionTypeEnum;
import com.project.encrypt.util.EncryptionBouncyCastelUtil;

/**
 * @author user on 2022-09-05 16:50:48.731
 *
 */

@RestController
public class EncryptAESController {
	
	@PostMapping("/aes")
	public ResponseEntity<?> encryptAES(@RequestBody EncryptRequest request) {

		GeneralEncryptDecryptResponse resp = new GeneralEncryptDecryptResponse();
		resp.setStatus(EncryptionValueConstant.STATUS_ERROR);
		resp.setMessage(EncryptionValueConstant.STATUS_BAD_REQUEST);
		if (StringUtils.hasText(request.getOperationMode()) 
				&& StringUtils.hasText(request.getInput())
				&& StringUtils.hasText(request.getBit())) {
			String encryptStr = null;
			try {
				
				int bitNum = 0;
				EncryptionTypeEnum enc = EncryptionTypeEnum.of(request.getBit());
				switch (enc) {
				case ONE_TWO_EIGHT:
					bitNum = Integer.valueOf(enc.getSize());
					break;
				case ONE_NINE_TWO:
					bitNum = Integer.valueOf(enc.getSize());
					break;
				case TWO_FIVE_SIX:
					bitNum = Integer.valueOf(enc.getSize());
					break;
				default:
					break;
				}
				
				if (bitNum == 0) {
					resp.setMessage("Unknown Bit");
					return ResponseEntity.ok(resp);
				}
				
				switch (EncryptionOperationEnum.of(request.getOperationMode())) {
				case ENCRYPT:
					encryptStr = encrypt(request.getInput(), request.getSecret(), bitNum);
					break;
				case DECRYPT:
					encryptStr = decrypt(request.getInput(), request.getSecret(), bitNum);
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
    
	private String encrypt(String input, String secret, int bitNum) throws Exception {

		Security.addProvider(new BouncyCastleProvider());

		byte[] encryptedMessage = EncryptionBouncyCastelUtil.encrypt(EncryptionValueConstant.ENCRYPTION_SCHEME_AES,
				bitNum, input.getBytes(), secret);

		return (new String(Base64.getEncoder().encode(encryptedMessage)));

	}

	private String decrypt(String input, String secret, int bitNum) throws Exception {

		Security.addProvider(new BouncyCastleProvider());

		byte[] decryptedMessage = EncryptionBouncyCastelUtil.decrypt(EncryptionValueConstant.ENCRYPTION_SCHEME_AES,
				bitNum, Base64.getDecoder().decode(input), secret);

		return new String(decryptedMessage);

	}

}

/**
 * 
 */
package com.project.encrypt.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.encrypt.DTO.EncryptRequest;
import com.project.encrypt.DTO.GeneralEncryptDecryptResponse;
import com.project.encrypt.constant.EncryptionValueConstant;
import com.project.encrypt.enumeration.EncryptionTypeEnum;
import com.project.encrypt.util.EncryptionUtil;

/**
 * @author user on 2022-09-04 06:03:38.093
 *
 */

@RestController
public class EncryptSHA3Controller {

	/*
	 * SHA3-224 SHA3-256 SHA3-384 SHA3-512
	 */
	@PostMapping("/sha-3")
	public ResponseEntity<?> encryptSHA3(@RequestBody EncryptRequest request) {

		GeneralEncryptDecryptResponse resp = new GeneralEncryptDecryptResponse();
		resp.setStatus(EncryptionValueConstant.STATUS_ERROR);
		resp.setMessage(EncryptionValueConstant.STATUS_BAD_REQUEST);
		try {
			
			EncryptionTypeEnum enc = EncryptionTypeEnum.of(request.getSize());

			MessageDigest digest = null;
			
			switch (enc) {
			case TWO_TWO_FOUR:
				digest = MessageDigest.getInstance("SHA3-224");
				break;
			case TWO_FIVE_SIX:
				digest = MessageDigest.getInstance("SHA3-256");
				break;
			case THREE_EIGHT_FOUR:
				digest = MessageDigest.getInstance("SHA3-384");
				break;
			case FIVE_ONE_TWO:
				digest = MessageDigest.getInstance("SHA3-512");
				break;
			default:
				break;
			}

			if (digest == null) {
				resp.setMessage("Unknown size");
				return ResponseEntity.ok(resp);
			}
			
			byte[] hash = digest.digest(request.getInput().getBytes(StandardCharsets.UTF_8));
			String encoded = EncryptionUtil.bytesToHex2(hash);

			resp.setStatus(EncryptionValueConstant.STATUS_SUCCESS);
			resp.setMessage(EncryptionValueConstant.STATUS_SUCCESS);
			resp.setEncrypt(encoded);

			return ResponseEntity.ok(resp);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setMessage(e.getMessage());
			return ResponseEntity.ok(resp);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resp.setMessage(e.getMessage());
			return ResponseEntity.ok(resp);
		}
	}

}

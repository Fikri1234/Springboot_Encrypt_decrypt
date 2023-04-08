/**
 * 
 */
package com.project.encrypt.controller;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.encrypt.DTO.EncryptRequest;
import com.project.encrypt.DTO.RSAGenResponse;
import com.project.encrypt.constant.EncryptionValueConstant;
import com.project.encrypt.enumeration.EncryptionTypeEnum;
import com.project.encrypt.service.RedisService;

/**
 * @author user on 2022-09-04 06:03:38.093
 *
 */

@RestController
public class EncryptRSAGenController {
	
	@Autowired
	private RedisService redisService;

	/*
	 * 512 1024 2048 4096
	 */
	@PostMapping("/rsa-gen")
	public ResponseEntity<?> encryptRSAGen(@RequestBody EncryptRequest request) {

		RSAGenResponse resp = new RSAGenResponse();
		resp.setStatus(EncryptionValueConstant.STATUS_ERROR);
		resp.setMessage(EncryptionValueConstant.STATUS_BAD_REQUEST);
		try {
			
			if (StringUtils.hasText(request.getBit())) {
				EncryptionTypeEnum enc = EncryptionTypeEnum.of(request.getBit());

				int initNum = 0; 
				
				switch (enc) {
				case FIVE_ONE_TWO:
					initNum = Integer.valueOf(enc.getSize());
					break;
				case ONE_ZERO_TWO_FOUR:
					initNum = Integer.valueOf(enc.getSize());
					break;
				case TWO_ZERO_FOUR_EIGHT:
					initNum = Integer.valueOf(enc.getSize());
					break;
				case FOUR_ZERO_NINE_SIX:
					initNum = Integer.valueOf(enc.getSize());
					break;
				default:
					break;
				
				}
				
				if (initNum == 0) {
					resp.setMessage("Unknown Bit");
					return ResponseEntity.ok(resp);
				}
				
				RSAGenResponse respRedis = redisService.getValue(String.valueOf(initNum));
				
				if (respRedis == null) {
				
					KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
					
					kpg.initialize(initNum);
					KeyPair kp = kpg.generateKeyPair();
					
					Key pub = kp.getPublic();
					Key pvt = kp.getPrivate();
					
					String encodedPub = Base64.getEncoder().encodeToString(pub.getEncoded());
					String encodedPvt = Base64.getEncoder().encodeToString(pvt.getEncoded());
		
					resp.setStatus(EncryptionValueConstant.STATUS_SUCCESS);
					resp.setMessage(EncryptionValueConstant.STATUS_SUCCESS);
					resp.setPublicKey(encodedPub);
					resp.setPrivateKey(encodedPvt);
					
					redisService.setValue(String.valueOf(initNum), resp);
					
				} else {
					resp = respRedis;
				}
	
				return ResponseEntity.ok(resp);
			}
			
			resp.setMessage("Bit has no value");
			return ResponseEntity.ok(resp);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setMessage(e.getMessage());
			return ResponseEntity.ok(resp);
		} catch (Exception e) {
			
			e.printStackTrace();
			resp.setMessage(e.getMessage());
			return ResponseEntity.ok(resp);
		}
	}

}

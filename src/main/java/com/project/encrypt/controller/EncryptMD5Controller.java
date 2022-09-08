/**
 * 
 */
package com.project.encrypt.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.encrypt.DTO.EncryptRequest;
import com.project.encrypt.DTO.GeneralEncryptDecryptResponse;
import com.project.encrypt.constant.EncryptionValueConstant;

/**
 * @author user on 2022-09-05 16:50:48.731
 *
 */

@RestController
public class EncryptMD5Controller {
	
	@PostMapping("/md5")
	public ResponseEntity<?> encryptMD5(@RequestBody EncryptRequest request) {

		GeneralEncryptDecryptResponse resp = new GeneralEncryptDecryptResponse();
		resp.setStatus(EncryptionValueConstant.STATUS_ERROR);
		resp.setMessage(EncryptionValueConstant.STATUS_BAD_REQUEST);
		if (StringUtils.hasText(request.getInput())) {
			
			try {
				 
	            // Static getInstance method is called with hashing MD5
	            MessageDigest md = MessageDigest.getInstance("MD5");
	 
	            // digest() method is called to calculate message digest
	            // of an input digest() return array of byte
	            byte[] messageDigest = md.digest(request.getInput().getBytes());
	 
	            // Convert byte array into signum representation
	            BigInteger no = new BigInteger(1, messageDigest);
	 
	            // Convert message digest into hex value
	            String hashtext = no.toString(16);
	            while (hashtext.length() < 32) {
	                hashtext = "0" + hashtext;
	            }
	            
	            resp.setStatus(EncryptionValueConstant.STATUS_SUCCESS);
				resp.setMessage(EncryptionValueConstant.STATUS_SUCCESS);
				resp.setEncrypt(hashtext);
				return ResponseEntity.ok(resp);
				
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            resp.setMessage(e.getMessage());
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

}

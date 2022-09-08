/**
 * 
 */
package com.project.encrypt.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author user on 2022-09-05 14:43:54.617
 *
 */

@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RSAGenResponse extends GeneralResponse {
	
	private String publicKey;
	private String privateKey;

}

/**
 * 
 */
package com.project.encrypt.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author user on 2022-09-04 06:18:11.499
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Encrypt3DESRequest {
	
	private String operationMode;
	private String input;
	private String secret;

}

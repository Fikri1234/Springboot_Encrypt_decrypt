/**
 * 
 */
package com.project.encrypt.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author user on 2022-09-05 14:43:54.617
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse {
	
	private String status;
	private String message;

}

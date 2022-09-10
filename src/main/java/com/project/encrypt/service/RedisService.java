/**
 * 
 */
package com.project.encrypt.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.project.encrypt.DTO.RSAGenResponse;

/**
 * @author user on 2022-09-09 23:49:10.753
 *
 */

@Service
public class RedisService {

	@Value("${redis.expiry}")
	private int defaultRedisExpiry;

	@Qualifier("redisTemplate")
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	Gson gson;

	public void setValue(final String key, RSAGenResponse value) {

		final String expiryEnv = System.getenv("CACHE_EXPIRY");
		int expiry = defaultRedisExpiry;
		if (StringUtils.hasText(expiryEnv)) {
			expiry = Integer.valueOf(expiryEnv);
		}

		redisTemplate.opsForValue().set(key, gson.toJson(value));
		redisTemplate.expire(key, expiry, TimeUnit.SECONDS);
	}

	public RSAGenResponse getValue(final String key) {
		return gson.fromJson(redisTemplate.opsForValue().get(key), RSAGenResponse.class);
	}

	public void deleteKeyFromRedis(String key) {
		redisTemplate.delete(key);
	}

}

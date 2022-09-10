/**
 * 
 */
package com.project.encrypt.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

/**
 * @author user on 2022-09-09 23:16:37.054
 *
 */

@Configuration
@EnableCaching
public class RedisConfiguration {
	
	@Value("${spring.redis.host}")
	private String defaultRedisHost;
	
	@Value("${spring.redis.port}")
	private int defaultRedisPort;

	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		final RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());

		final String hostEnv = System.getenv("CACHE_URL");
		String hostname = defaultRedisHost;
		if (StringUtils.hasText(hostEnv)) {
			hostname = hostEnv;
		}

		final String portEnv = System.getenv("CACHE_PORT");
		int port = defaultRedisPort;
		if (StringUtils.hasText(portEnv)) {
			port = Integer.valueOf(portEnv);
		}
		
		final String passEnv = System.getenv("CACHE_AUTH");
		String pwd = "";
		if (StringUtils.hasText(passEnv)) {
			pwd = passEnv;
		}

		RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration(hostname, port);
		conf.setPassword(pwd);

		JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().build();

		JedisConnectionFactory factory = new JedisConnectionFactory(conf, jedisClientConfiguration);

		factory.afterPropertiesSet();
		redisTemplate.setConnectionFactory(factory);

		return redisTemplate;
	}

}

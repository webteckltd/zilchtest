package com.ravi.zilch;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisCacheConfig {

	private static final Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);
	@Value("${redis.nodes}")
	private String redisNode;

	@Bean
	RedisConnectionFactory connectionFactory() {
		String[] hostPort = redisNode.split(",");
		logger.debug("Redis cluster Host Port = " + hostPort[0] + "  " + hostPort[1] + " " + hostPort[2]);
		List<String> clusterNodes = Arrays.asList(hostPort[0], hostPort[1], hostPort[2]);
		return new JedisConnectionFactory(new RedisClusterConfiguration(clusterNodes));
	}

	@Bean
	RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		return new StringRedisTemplate(factory);
	}

	@Bean(name = "zilchRedisCacheManager")
	@Primary
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		RedisCacheManager cacheManager = RedisCacheManager.create(factory);
		return cacheManager;
	}

}

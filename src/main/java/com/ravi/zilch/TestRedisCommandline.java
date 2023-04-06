package com.ravi.zilch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestRedisCommandline implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(TestRedisCommandline.class);

	
	  @Autowired
	  private RedisTemplate<String, String> redisTemplate;
	 

	@Override
	public void run(String... args) throws Exception {

		for (int i = 0; i < 5; i++) {

			logger.debug("adding temp value to elastic ");

			
			 // redisTemplate.opsForValue().set(java.util.UUID.randomUUID().toString(),java.util.UUID.randomUUID().toString());
			 

			Thread.sleep(100);
		}

	}

}

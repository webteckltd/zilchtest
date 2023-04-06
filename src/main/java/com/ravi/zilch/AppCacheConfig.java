package com.ravi.zilch;

import java.time.Duration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;


import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;

import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ravi.zilch.model.PaymentTransaction;

@Configuration
public class AppCacheConfig {
	
	@Bean
	public CacheManager EhcacheManager() {

		CacheConfiguration<String, PaymentTransaction> cachecConfig = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(String.class, PaymentTransaction.class,
						ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(10, MemoryUnit.MB).build())
				.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(10))).build();

		CachingProvider cachingProvider = Caching.getCachingProvider();
		CacheManager cacheManager = cachingProvider.getCacheManager();

		javax.cache.configuration.Configuration<String, PaymentTransaction> configuration = Eh107Configuration
				.fromEhcacheCacheConfiguration(cachecConfig);
		cacheManager.createCache("transactionStore", configuration);
		return cacheManager;
	}
}

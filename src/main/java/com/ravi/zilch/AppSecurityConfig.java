package com.ravi.zilch;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class AppSecurityConfig {
	
	 @Bean
	    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		 http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors()
					.configurationSource(new CorsConfigurationSource() {

						@Override
						public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
							CorsConfiguration config = new CorsConfiguration();
							config.setAllowedOrigins(Collections.singletonList("*"));
							config.setAllowedMethods(Collections.singletonList("*"));
							config.setAllowCredentials(true);
							config.setAllowedHeaders(Collections.singletonList("*"));
							config.setExposedHeaders(Arrays.asList("Authorization"));
							config.setMaxAge(3600L);
							return config;
						}
					})
					
					.and().csrf().disable()
					
					.authorizeHttpRequests()
	                .requestMatchers("/api/v1/transaction/filter").hasAuthority("SYSTEM")
	                .anyRequest().authenticated()
	                .and().httpBasic();
	        return http.build();
	    }

	 
	 
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

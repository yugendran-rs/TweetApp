package com.tweetapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tweetapp.filter.JwtRequestFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigurer {

	private final String[] publicEndPoints = { "/**/register", "/**/login", "/**/forgot", "/**/refresh", "/actuator/**",
			"/v3/api-docs/**", "/swagger-ui*/**", "/v2/api-docs/**", "/swagger-resources/**", "/swaggerui",
			"/**/swagger*/**", "/**/api-docs*/**" };

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain configure(final HttpSecurity http) throws Exception {
		return http.cors().and().csrf().disable().authorizeRequests().antMatchers(publicEndPoints).anonymous()
				.anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

}

package br.com.ldr.devshe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	@Autowired
	private JWTFilter jwtFilter;

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(5);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("OPTIONS");
		configuration.addAllowedMethod("GET");
		configuration.addAllowedMethod("POST");
		configuration.addAllowedMethod("PUT");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors(Customizer.withDefaults()) // por padrão irá usar o Bean com o nome corsConfigurationSource
				.csrf().disable().exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeHttpRequests()
				.requestMatchers("/api/v1/auth").permitAll()
				.requestMatchers("/api/v1/register").permitAll()
				.requestMatchers("/api/v1/healthCheck").permitAll().anyRequest()
				.authenticated();

		http.headers().cacheControl().disable();
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}

package shop.byeol23.sogra2025.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class DisabledWebSecurityConfig {
	// BCryptPasswordEncoder 빈은 EncoderConfig에 이미 등록되어 있다고 가정합니다.

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// 모든 요청에 대해 보안 필터링을 건너뛰고 접근을 허용합니다.
		http
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(httpBasic -> httpBasic.disable())
			.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));;
		return http.build();
	}
}
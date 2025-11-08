package shop.byeol23.sogra2025.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		// 1. SecurityScheme의 참조 이름 (임의로 지정 가능)
		final String securitySchemeName = "Authorization";

		// 2. 보안 스킴(SecurityScheme) 정의
		SecurityScheme apiKeyScheme = new SecurityScheme()
			.type(SecurityScheme.Type.APIKEY)    // <<< [수정] 타입을 APIKEY로 변경
			.in(SecurityScheme.In.HEADER)       // <<< 토큰이 헤더에 있음을 명시
			.name("Authorization");               // <<< [수정] 실제 헤더 이름을 'AccessToken'으로 지정
		// (scheme("bearer")를 제거했기 때문에 'Bearer ' 접두사가 붙지 않습니다)

		// 3. 전역 보안 요구사항(SecurityRequirement) 정의
		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList(securitySchemeName); // 위에서 정의한 참조 이름을 사용

		// 4. OpenAPI 객체에 반영
		return new OpenAPI()
			.components(new Components()
				.addSecuritySchemes(securitySchemeName, apiKeyScheme)) // 정의한 스킴을 컴포넌트에 추가
			.addSecurityItem(securityRequirement); // 전역 보안 요구사항으로 추가
	}
}
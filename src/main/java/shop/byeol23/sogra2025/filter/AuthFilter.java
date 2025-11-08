package shop.byeol23.sogra2025.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.byeol23.sogra2025.annotation.NoAuth;
import shop.byeol23.sogra2025.context.MemberContext;
import shop.byeol23.sogra2025.member.dto.internal.MemberInfo;
import shop.byeol23.sogra2025.member.service.JWTService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

	private final JWTService jwtService;
	private final RequestMappingHandlerMapping handlerMapping; // 요청을 처리할 핸들러(메서드)를 찾기 위해 주입
	private final ObjectMapper objectMapper; // 에러 응답을 JSON으로 생성하기 위해 주입

	private static final String AUTH_HEADER = "Authorization"; // 요청에서 사용할 헤더 이름

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {


		try {
			String requestURI = request.getRequestURI();
			log.debug("AuthFilter processing request URI: {}", requestURI);
			if (requestURI.startsWith("/swagger-ui") ||
				requestURI.startsWith("/v3/api-docs") ||
				requestURI.startsWith("/swagger-resources")) {

				log.debug("Bypassing authentication for Swagger/API docs path: {}", requestURI);
				filterChain.doFilter(request, response);
				return;
			}

			// 1. 요청을 처리할 핸들러(컨트롤러 메서드) 정보 조회
			HandlerExecutionChain chain = handlerMapping.getHandler(request);
			if (chain == null) {
				// 매핑되는 핸들러가 없는 경우 (e.g., 정적 리소스)
				filterChain.doFilter(request, response);
				return;
			}

			Object handler = chain.getHandler();
			if (!(handler instanceof HandlerMethod handlerMethod)) {
				// HandlerMethod가 아닌 경우 (e.g., ResourceHttpRequestHandler)
				filterChain.doFilter(request, response);
				return;
			}

			// 2. @NoAuth 어노테이션 체크
			NoAuth noAuth = handlerMethod.getMethodAnnotation(NoAuth.class);
			if (noAuth != null) {
				// @NoAuth가 있으면 인증 패스
				log.debug("Bypassing authentication for @NoAuth method: {}", handlerMethod.getMethod().getName());
				filterChain.doFilter(request, response);
				return;
			}

			// --- @NoAuth가 없는 경우, 여기서부터 인증 절차 ---

			// 3. AccessToken 헤더에서 토큰 추출
			String token = request.getHeader(AUTH_HEADER);

			// 4. 토큰 존재 여부 확인 (없으면 403 Forbidden)
			if (token == null || token.isBlank()) {
				log.warn("Authentication failed: {} header is missing.", AUTH_HEADER);
				sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Authentication required. Access token is missing.");
				return; // 필터 체인 중단
			}

			// 5. 토큰 검증 및 MemberInfo 추출
			// jwtService.getMemberInfoFromToken 내부에서 파싱 및 검증(만료, 서명 등)을 수행하고 예외를 던짐
			MemberInfo memberInfo = jwtService.getMemberInfoFromToken(token);

			// 6. ThreadLocal에 MemberInfo 저장
			MemberContext.set(memberInfo);
			log.debug("Successfully authenticated member: {}", memberInfo.loginId());

			// 7. 다음 필터 체인 실행
			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException e) {
			// 5-1. 토큰이 만료된 경우 (401 Unauthorized)
			log.warn("Authentication failed: Token expired. {}", e.getMessage());
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Access token has expired.");
			return; // 필터 체인 중단

		} catch (JwtException | IllegalArgumentException e) {
			// 5-2. 토큰이 유효하지 않은 경우 (서명 오류, 형식 오류 등) (401 Unauthorized)
			log.warn("Authentication failed: Invalid token. {}", e.getMessage());
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token.");
			return; // 필터 체인 중단

		} catch (Exception e) {
			// 1. 핸들러 매핑 등 기타 예외 처리
			log.error("Unexpected error in JwtAuthenticationFilter", e);
			sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An internal error occurred.");
			return; // 필터 체인 중단

		} finally {
			// 8. 요청 처리가 끝나면 항상 ThreadLocal을 비움
			MemberContext.clear();
			log.debug("Cleared MemberInfo from ThreadLocal");
		}
	}

	/**
	 * 필터 레벨에서 인증 에러 발생 시, 클라이언트에게 JSON 형태로 에러 응답을 보냅니다.
	 */
	private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
		response.setStatus(status);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put("timestamp", System.currentTimeMillis());
		errorDetails.put("status", status);
		errorDetails.put("error", getHttpStatusMessage(status));
		errorDetails.put("message", message);

		response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
	}

	private String getHttpStatusMessage(int status) {
		return switch (status) {
			case 401 -> "Unauthorized";
			case 403 -> "Forbidden";
			case 500 -> "Internal Server Error";
			default -> "Error";
		};
	}
}
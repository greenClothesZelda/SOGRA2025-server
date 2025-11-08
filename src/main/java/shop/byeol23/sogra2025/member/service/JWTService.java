package shop.byeol23.sogra2025.member.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import shop.byeol23.sogra2025.member.dto.external.TokenResponse;
import shop.byeol23.sogra2025.member.dto.internal.MemberInfo;

@Slf4j
@Service
public class JWTService {
	private final MemberService memberService;
	private final SecretKey key;
	private final long accessExpirationSeconds;
	private final long refreshExpirationSeconds;
	private final shop.byeol23.sogra2025.security.LoginAttemptService loginAttemptService;

	public JWTService(
		MemberService memberService,
		shop.byeol23.sogra2025.security.LoginAttemptService loginAttemptService,
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.access-expiration-seconds}") long accessExpirationSeconds,
		@Value("${jwt.refresh-expiration-seconds}") long refreshExpirationSeconds){
		this.key = Keys.hmacShaKeyFor(secret.trim().getBytes(StandardCharsets.UTF_8));
		this.accessExpirationSeconds = accessExpirationSeconds;
		this.refreshExpirationSeconds = refreshExpirationSeconds;
		this.memberService = memberService;
		this.loginAttemptService = loginAttemptService;
	}

	public TokenResponse login(String loginId, String password){
		// 차단 여부 확인
		loginAttemptService.checkBlocked(loginId);
		try {
			boolean ok = memberService.authenticate(loginId, password);
			if(!ok){
				loginAttemptService.recordFailure(loginId);
				throw new IllegalArgumentException("Invalid login ID or password");
			}
		} catch (IllegalArgumentException e){
			// 사용자 없음 등 인증 중 예외도 실패로 간주
			loginAttemptService.recordFailure(loginId);
			throw e;
		}
		// 성공 시 초기화
		loginAttemptService.recordSuccess(loginId);
		return createTokens(loginId);
	}

	public TokenResponse refresh(String refreshToken){
		try {
			Jws<Claims> parsed = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(refreshToken);
			Claims claims = parsed.getBody();
			String type = claims.get("type", String.class);
			if (!"refresh".equals(type)) {
				throw new IllegalArgumentException("Provided token is not a refresh token");
			}
			String subject = claims.getSubject();
			return createTokens(subject);
		} catch (JwtException | IllegalArgumentException e) {
			log.warn("Refresh token validation failed: {}", e.getMessage());
			throw new IllegalArgumentException("Invalid refresh token");
		}
	}

	public TokenResponse createTokens(String loginId){
		MemberInfo mi = memberService.getMemberInfo(loginId);
		String access = createAccessToken(loginId, mi);
		String refresh = createRefreshToken(loginId, mi);
		return new TokenResponse(access, refresh, accessExpirationSeconds);
	}

	private String createAccessToken(String loginId, MemberInfo mi){
		long now = System.currentTimeMillis();
		Date issuedAt = new Date(now);
		Date exp = new Date(now + accessExpirationSeconds * 1000L);
		return Jwts.builder()
			.setSubject(loginId)
			.setIssuedAt(issuedAt)
			.setExpiration(exp)
			.claim("type", "access")
			.claim("memberName", mi.memberName())
			.signWith(key)
			.compact();
	}

	private String createRefreshToken(String loginId, MemberInfo mi){
		long now = System.currentTimeMillis();
		Date issuedAt = new Date(now);
		Date exp = new Date(now + refreshExpirationSeconds * 1000L);
		return Jwts.builder()
			.setSubject(loginId)
			.setIssuedAt(issuedAt)
			.setExpiration(exp)
			.claim("type", "refresh")
			.claim("memberName", mi.memberName())
			.signWith(key)
			.compact();
	}

	public Claims parseClaimsThrowing(String token) throws JwtException {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public MemberInfo getMemberInfoFromToken(String token) throws JwtException {
		Claims claims = parseClaimsThrowing(token);
		String loginId = claims.getSubject();
		String memberName = claims.get("memberName", String.class);
		log.info("Parsed MemberInfo from token - loginId: {}, memberName: {}", loginId, memberName);
		return new MemberInfo(loginId, memberName);
	}

	public String parseSubject(String token){
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
			return claims.getSubject();
		} catch (JwtException e) {
			log.warn("Token parse failed: {}", e.getMessage());
			return null;
		}
	}
}

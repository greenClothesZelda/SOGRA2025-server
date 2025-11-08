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
import shop.byeol23.sogra2025.member.repository.MemberRepository;

@Slf4j
@Service
public class JWTService {
	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final SecretKey key;
	private final long accessExpirationSeconds;
	private final long refreshExpirationSeconds;

	public JWTService(
		MemberService memberService,
		MemberRepository memberRepository,
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.access-expiration-seconds}") long accessExpirationSeconds,
		@Value("${jwt.refresh-expiration-seconds}") long refreshExpirationSeconds){
		this.key = Keys.hmacShaKeyFor(secret.trim().getBytes(StandardCharsets.UTF_8));
		this.accessExpirationSeconds = accessExpirationSeconds;
		this.refreshExpirationSeconds = refreshExpirationSeconds;
		this.memberService = memberService;
		this.memberRepository = memberRepository;
	}

	public TokenResponse login(String loginId, String password){
		if(!memberService.authenticate(loginId, password)){
			throw new IllegalArgumentException("Invalid login ID or password");
		}
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

	// 로그인/리프레시 시점에 member 정보를 claim으로 넣어 토큰에서 바로 사용 가능하도록 함
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
			.claim("memberId", mi.memberId())
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
			.claim("memberId", mi.memberId())
			.claim("memberName", mi.memberName())
			.signWith(key)
			.compact();
	}

	// 토큰 파싱을 호출자에게 예외로 전달 - 만료(ExpiredJwtException)와 다른 오류를 구분 가능
	public Claims parseClaimsThrowing(String token) throws JwtException {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	// 토큰에서 MemberInfo를 바로 구성
	public MemberInfo getMemberInfoFromToken(String token) throws JwtException {
		Claims claims = parseClaimsThrowing(token);
		Number idNum = claims.get("memberId", Number.class);
		Long memberId = idNum != null ? idNum.longValue() : null;
		String memberName = claims.get("memberName", String.class);
		return new MemberInfo(memberId, memberName);
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

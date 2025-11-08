package shop.byeol23.sogra2025.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shop.byeol23.sogra2025.annotation.NoAuth;
import shop.byeol23.sogra2025.member.dto.external.LoginRequest;
import shop.byeol23.sogra2025.member.dto.external.RefreshRequest;
import shop.byeol23.sogra2025.member.dto.external.TokenResponse;
import shop.byeol23.sogra2025.member.service.JWTService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JWTService jwtService;

	@NoAuth
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        TokenResponse tokens = jwtService.login(request.loginId(), request.password());
        return ResponseEntity.ok(tokens);
    }
	@NoAuth
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        TokenResponse tokens = jwtService.refresh(request.refreshToken());
        return ResponseEntity.ok(tokens);
    }
}


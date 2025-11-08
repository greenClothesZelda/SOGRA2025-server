package shop.byeol23.sogra2025.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.byeol23.sogra2025.annotation.NoAuth;
import shop.byeol23.sogra2025.member.dto.external.RegisterRequest;
import shop.byeol23.sogra2025.member.service.MemberService;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {
	private final MemberService memberService;

	@NoAuth
	@PostMapping("/register")
	public String registerMember(
		@RequestBody @Valid RegisterRequest registerRequest
	) {
		memberService.createMember(registerRequest.loginId(), registerRequest.password(), registerRequest.name());
		return "Member registered successfully";
	}

	@GetMapping("/details")
	public String getMemberDetails() {
		return "Member details endpoint";
	}
}

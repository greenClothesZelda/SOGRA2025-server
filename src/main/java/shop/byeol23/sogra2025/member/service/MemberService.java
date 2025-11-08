package shop.byeol23.sogra2025.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.byeol23.sogra2025.member.dto.internal.MemberInfo;
import shop.byeol23.sogra2025.member.entity.Member;
import shop.byeol23.sogra2025.member.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public MemberInfo getMemberInfo(String memberId){
		Member member = memberRepository.findByLoginId(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
		return new MemberInfo(member);
	}

	@Transactional
	public void createMember(
		String loginId,
		String password,
		String memberName
	){
		if (memberRepository.existsMemberByLoginId(loginId)) {
			throw new IllegalArgumentException("Login ID already exists");
		}
		memberRepository.save(Member.builder()
			.memberName(memberName)
			.loginId(loginId)
			.loginPassword(passwordEncoder.encode(password))
			.build()
		);
	}

	public boolean authenticate(String loginId, String password) {
		Member member = memberRepository.findByLoginId(loginId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid login ID or password"));
		return passwordEncoder.matches(password, member.getLoginPassword());
	}


}

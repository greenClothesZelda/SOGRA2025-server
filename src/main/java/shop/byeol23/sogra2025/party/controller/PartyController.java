package shop.byeol23.sogra2025.party.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.byeol23.sogra2025.context.MemberContext;
import shop.byeol23.sogra2025.member.dto.internal.MemberInfo;
import shop.byeol23.sogra2025.party.dto.internal.PartyInfo;
import shop.byeol23.sogra2025.party.service.PartyService;

@RequestMapping("/party")
@RestController
@RequiredArgsConstructor
public class PartyController {

	private final PartyService partyService;

	// 파티 생성
	@PostMapping("/{landmarkName}")
	public PartyInfo createParty(
		@PathVariable String landmarkName,
		@RequestParam(value = "maxMembers", defaultValue = "5") int maxMembers,
		@RequestParam(value = "partyName", required = true) String party
	) {
		MemberInfo memberInfo = MemberContext.get();
		return partyService.createParty(memberInfo, landmarkName, maxMembers, party);
	}

	// 파티 참가
	@PostMapping("/{landmarkName}/join")
	public void joinParty(@PathVariable String landmarkName) {
		MemberInfo memberInfo = MemberContext.get();
		partyService.joinParty(memberInfo, landmarkName);
	}

	// 특정 랜드마크(ID) 기준 파티 멤버 조회
	@GetMapping("/{landmarkName}/members")
	public PartyInfo getPartyMembers(@PathVariable String landmarkName) {
		return partyService.getPartyMembers(landmarkName);
	}

	@PostMapping("/{landmarkName}/delete")
	public void deleteParty(@PathVariable String landmarkName){
		String loginId = MemberContext.get().loginId();
		partyService.deleteParty(loginId, landmarkName);
	}
}

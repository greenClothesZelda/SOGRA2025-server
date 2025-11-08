package shop.byeol23.sogra2025.member.dto.internal;

import shop.byeol23.sogra2025.member.entity.Member;

public record MemberInfo(Long memberId, String memberName) {
	public MemberInfo(Member member){
		this(member.getMemberId(), member.getMemberName());
	}
}

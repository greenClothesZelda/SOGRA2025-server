package shop.byeol23.sogra2025.member.dto.internal;

import shop.byeol23.sogra2025.member.entity.Member;

public record MemberInfo(String loginId, String memberName) {
	public MemberInfo(Member member){
		this(member.getLoginId(), member.getMemberName());
	}
}

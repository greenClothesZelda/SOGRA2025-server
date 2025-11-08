package shop.byeol23.sogra2025.party.dto.internal;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import shop.byeol23.sogra2025.member.dto.internal.MemberInfo;

@Getter
public class PartyInfo {

	@JsonIgnore
	private final MemberInfo ownerInfo;

	@JsonIgnore
	private final Set<MemberInfo> memberInfos;

	public PartyInfo(MemberInfo ownerInfo) {
		this.ownerInfo = ownerInfo;
		this.memberInfos = ConcurrentHashMap.newKeySet();
	}

	@JsonProperty("ownerName")
	public String getOwnerName() {
		return ownerInfo == null ? null : ownerInfo.memberName();
	}

	@JsonProperty("memberNames")
	public Set<String> getMemberNames() {
		return memberInfos.stream()
			.map(MemberInfo::memberName)
			.collect(Collectors.toSet());
	}
}

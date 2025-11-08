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

	private final int maxMembers;
	private final String partyName;

	public PartyInfo(MemberInfo ownerInfo, int maxMembers, String partyName) {
		this.ownerInfo = ownerInfo;
		this.memberInfos = ConcurrentHashMap.newKeySet();
		this.maxMembers = maxMembers;
		this.partyName = partyName;
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

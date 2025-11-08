package shop.byeol23.sogra2025.party.service;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import shop.byeol23.sogra2025.landmark.entity.Landmark;
import shop.byeol23.sogra2025.landmark.repository.LandmarkRepository;
import shop.byeol23.sogra2025.member.dto.internal.MemberInfo;
import shop.byeol23.sogra2025.member.repository.MemberRepository;
import shop.byeol23.sogra2025.party.dto.internal.PartyInfo;

@Slf4j
@Service
public class PartyService {
	private final MemberRepository memberRepository;
	private final LandmarkRepository landmarkRepository;
	private final ConcurrentMap<String, PartyInfo> partyCache;

	public PartyService(MemberRepository memberRepository, LandmarkRepository landmarkRepository) {
		this.memberRepository = memberRepository;
		this.landmarkRepository = landmarkRepository;
		this.partyCache = new ConcurrentHashMap<>();
	}

	public PartyInfo getPartyMembers(String landmarkName) {
		Landmark landmark = landmarkRepository.findByLandmarkName(landmarkName)
			.orElseThrow(() -> new IllegalArgumentException("해당 랜드마크를 찾을 수 없습니다. 랜드마크 ID: " + landmarkName));
		return partyCache.get(landmarkName);
	}

	public PartyInfo createParty(MemberInfo memberInfo, String landmarkName){
		Landmark landmark = landmarkRepository.findByLandmarkName(landmarkName)
			.orElseThrow(() -> new IllegalArgumentException("해당 랜드마크를 찾을 수 없습니다. 랜드마크 이름: " + landmarkName));
		PartyInfo partyInfo = new PartyInfo(memberInfo);
		PartyInfo existingParty = partyCache.putIfAbsent(landmarkName, partyInfo);
		if (existingParty != null) {
			throw new IllegalStateException("이미 해당 랜드마크에 파티가 존재합니다. 랜드마크 이름: " + landmarkName);
		}
		log.info("Created new party at landmark {}: {}", landmarkName, partyInfo);
		return partyInfo;
	}

	public void joinParty(MemberInfo memberInfo, String landmarkName){
		Landmark landmark = landmarkRepository.findByLandmarkName(landmarkName)
			.orElseThrow(() -> new IllegalArgumentException("해당 랜드마크를 찾을 수 없습니다. 랜드마크 이름: " + landmarkName));
		PartyInfo partyInfo = partyCache.get(landmarkName);
		partyInfo.getMemberInfos().add(memberInfo);
		log.info("Member {} joined party at landmark {}", memberInfo.memberName(), landmarkName);
	}
}

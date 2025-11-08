package shop.byeol23.sogra2025.landmark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import shop.byeol23.sogra2025.landmark.entity.Landmark;
import shop.byeol23.sogra2025.landmark.entity.LandmarkMember;
import shop.byeol23.sogra2025.landmark.repository.LandmarkMemberRepository;
import shop.byeol23.sogra2025.landmark.repository.LandmarkRepository;
import shop.byeol23.sogra2025.member.entity.Member;
import shop.byeol23.sogra2025.member.repository.MemberRepository;

@Slf4j
@RestController
public class LandmarkService {
	private final LandmarkRepository landmarkRepository;
	private final LandmarkMemberRepository landmarkMemberRepository;
	private final MemberRepository memberRepository;
	private final double SEARCH_DISTANCE_X;
	private final double SEARCH_DISTANCE_Y;
	private final int PAGE_SIZE;

	public LandmarkService(
		LandmarkRepository landmarkRepository,
		LandmarkMemberRepository landmarkMemberRepository,
		MemberRepository memberRepository,
		@Value("${landmark.search.distance.x}") double searchDistanceX,
		@Value ("${landmark.search.distance.y}") double searchDistanceY,
		@Value("${landmark.search.limit}") int pageSize
	) {
		this.landmarkRepository = landmarkRepository;
		this.SEARCH_DISTANCE_X = searchDistanceX;
		this.SEARCH_DISTANCE_Y = searchDistanceY;
		this.PAGE_SIZE = pageSize;
		this.landmarkMemberRepository = landmarkMemberRepository;
		this.memberRepository = memberRepository;
	}

	public Landmark getLandmark(String landmarkName) {
		return landmarkRepository.findByLandmarkName((landmarkName)).orElseThrow(() -> new IllegalArgumentException("랜드마크를 찾을 수 없습니다."));
	}

	public Page<Landmark> getLandmarks(double x, double y, int pageNumber) {
		double minX = x - SEARCH_DISTANCE_X;
		double maxX = x + SEARCH_DISTANCE_X;
		double minY = y - SEARCH_DISTANCE_Y;
		double maxY = y + SEARCH_DISTANCE_Y;
		Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);

		return landmarkRepository.findByXBetweenAndYBetween(minX, maxX, minY, maxY, pageable);
	}

	public void likeLandmark(String loginId, String landmarkName) {
		log.info(loginId);
		Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
		Landmark landmark = landmarkRepository.findByLandmarkName(landmarkName).orElseThrow(() -> new IllegalArgumentException("랜드마크를 찾을 수 없습니다."));
		LandmarkMember landmarkMember = landmarkMemberRepository.findByLandmarkAndMember(landmark, member).orElse(new LandmarkMember(landmark, member));

		landmark.incrementRecommendationCount();
		landmarkRepository.save(landmark);

		landmarkMember.setRecommended(true);
		landmarkMemberRepository.save(landmarkMember);
	}

	public void visitLandmark(String loginId, String landmarkName) {
		Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
		Landmark landmark = landmarkRepository.findByLandmarkName(landmarkName).orElseThrow(() -> new IllegalArgumentException("랜드마크를 찾을 수 없습니다."));
		LandmarkMember landmarkMember = landmarkMemberRepository.findByLandmarkAndMember(landmark, member).orElse(new LandmarkMember(landmark, member));

		landmark.incrementVisitCount();
		landmarkRepository.save(landmark);

		landmarkMember.setVisited(true);
		landmarkMemberRepository.save(landmarkMember);
	}

}

package shop.byeol23.sogra2025.landmark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.byeol23.sogra2025.landmark.entity.Landmark;
import shop.byeol23.sogra2025.landmark.entity.LandmarkMember;
import shop.byeol23.sogra2025.member.entity.Member;

public interface LandmarkMemberRepository extends JpaRepository<LandmarkMember, Long> {
	Optional<LandmarkMember> findByLandmarkAndMember(Landmark landmark, Member member);
}

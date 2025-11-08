package shop.byeol23.sogra2025.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aj.org.objectweb.asm.commons.Remapper;
import shop.byeol23.sogra2025.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Remapper findByMemberId(Long memberId);

	Optional<Member> findByLoginId(String loginId);

	boolean existsMemberByLoginId(String loginId);
}

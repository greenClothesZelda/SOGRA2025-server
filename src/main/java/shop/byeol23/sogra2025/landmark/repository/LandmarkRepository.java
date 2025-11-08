package shop.byeol23.sogra2025.landmark.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import shop.byeol23.sogra2025.landmark.entity.Landmark;

public interface LandmarkRepository extends JpaRepository<Landmark, Long> {
	Optional<Landmark> findByLandmarkName(String landmarkName);

	/**
	 * 주어진 x(경도) 범위와 y(위도) 범위 내에 있는 랜드마크를 페이징하여 조회합니다.
	 *
	 * Spring Data JPA가 메서드 이름을 분석하여
	 * "WHERE x BETWEEN ?1 AND ?2 AND y BETWEEN ?3 AND ?4" 쿼리를 생성합니다.
	 *
	 * @param minX 최소 x 값
	 * @param maxX 최대 x 값
	 * @param minY 최소 y 값
	 * @param maxY 최대 y 값
	 * @param pageable 페이징 및 정렬 정보
	 * @return 페이징된 Landmark 목록 (Page 객체)
	 */
	Page<Landmark> findByXBetweenAndYBetween(double minX, double maxX, double minY, double maxY, Pageable pageable);
}

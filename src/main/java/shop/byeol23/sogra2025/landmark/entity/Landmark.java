package shop.byeol23.sogra2025.landmark.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Landmark {
	@Id
	@Column(nullable = false, length = 100)
	private String landmarkName;

	@Column(nullable = false)
	private double x;

	@Column(nullable = false)
	private double y;

	@Column(columnDefinition = "BIGINT DEFAULT 0", nullable = false)
	private Long recommendationCount;

	@Column(columnDefinition = "BIGINT DEFAULT 0", nullable = false)
	private Long visitCount;

	@Column(nullable = true)
	@Size(min = 1, max = 100)
	private String description;

	@Column(columnDefinition = "VARCHAR(500) DEFAULT 'https://plus.cnu.ac.kr/Upl/en/_main_swap/en_main_swap_0_1723508360.jpg'", nullable = true)
	private String imageUrl;

	@Column(columnDefinition = "VARCHAR(500) DEFAULT '대전시 서구 가리봉동 201-3'", nullable = true)
	private String address;

	public void incrementRecommendationCount() {
		this.recommendationCount++;
	}

	public void incrementVisitCount() {
		this.visitCount++;
	}
}

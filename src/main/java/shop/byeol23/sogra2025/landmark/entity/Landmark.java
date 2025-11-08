package shop.byeol23.sogra2025.landmark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}

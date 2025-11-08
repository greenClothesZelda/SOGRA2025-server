package shop.byeol23.sogra2025.landmark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import shop.byeol23.sogra2025.member.entity.Member;

@Entity
public class LandmarkMember {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Landmark landmark;

	@OneToOne
	private Member member;

	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean isVisited;

	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean isRecommended;
}

package shop.byeol23.sogra2025.landmark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.byeol23.sogra2025.member.entity.Member;

@Getter
@Entity
@NoArgsConstructor
public class LandmarkMember {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Landmark landmark;

	@OneToOne
	private Member member;

	@Setter
	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean isVisited;

	@Setter
	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean isRecommended;

	public LandmarkMember(Landmark landmark, Member member) {
		this.landmark = landmark;
		this.member = member;
		this.isVisited = false;
		this.isRecommended = false;
	}
}

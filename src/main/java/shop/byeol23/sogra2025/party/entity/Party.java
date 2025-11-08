package shop.byeol23.sogra2025.party.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import shop.byeol23.sogra2025.landmark.entity.Landmark;

@Getter
@Entity
public class Party {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long partyId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "landmark_name", nullable = false)
	private Landmark landmark;

	@OneToMany
	private List<PartyMember> partyMember;
}

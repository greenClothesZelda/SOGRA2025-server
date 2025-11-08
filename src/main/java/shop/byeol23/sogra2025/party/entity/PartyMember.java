package shop.byeol23.sogra2025.party.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import shop.byeol23.sogra2025.member.entity.Member;

@Entity
@NoArgsConstructor
public class PartyMember {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long partyMemberId;

	@ManyToOne
	@JoinColumn(name = "party_id", nullable = false)
	private Party party;

	@ManyToOne
	@JoinColumn(name = "login_id", nullable = false)
	private Member member;
}

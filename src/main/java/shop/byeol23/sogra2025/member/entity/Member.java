package shop.byeol23.sogra2025.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Member {
	@Id
	@Column(nullable = false, length = 20)
	private String loginId;

	@Column(nullable = false, length = 255)
	private String loginPassword;

	@Column(nullable = false, length = 50)
	private String memberName;


}

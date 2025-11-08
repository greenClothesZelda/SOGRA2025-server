package shop.byeol23.sogra2025.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.byeol23.sogra2025.party.entity.Party;

public interface PartiRepository extends JpaRepository<Party, Long> {
}

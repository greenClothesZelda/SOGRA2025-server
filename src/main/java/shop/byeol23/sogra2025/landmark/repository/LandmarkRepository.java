package shop.byeol23.sogra2025.landmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.byeol23.sogra2025.landmark.entity.Landmark;

public interface LandmarkRepository extends JpaRepository<Landmark, Long> {
}

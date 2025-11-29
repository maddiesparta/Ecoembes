package ecoembes.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecoembes.entity.RecyclingPlant;

@Repository
public interface RecyclingPlantRepository extends JpaRepository<RecyclingPlant, Long> {
	Optional<RecyclingPlant> findByName(String plant_name);
}

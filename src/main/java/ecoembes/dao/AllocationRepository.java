package ecoembes.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecoembes.entity.Allocation;
import ecoembes.entity.RecyclingPlant;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long>{
	Optional<Allocation> findRecyclingPlant(RecyclingPlant rp);
}

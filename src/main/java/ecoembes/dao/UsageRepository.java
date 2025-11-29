package ecoembes.dao;

import java.time.LocalDate;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecoembes.entity.Dumpster;
import ecoembes.entity.Usage;

@Repository
public interface UsageRepository extends JpaRepository<Usage, Long>{
	Optional<Usage>findByDumpsterDateBetween(Dumpster dumpster, LocalDate startDate, LocalDate endDate);
	Optional<Usage>findByDumpster(Dumpster dumpster);
}

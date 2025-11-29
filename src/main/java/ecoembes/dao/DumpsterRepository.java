package ecoembes.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ecoembes.entity.Dumpster;

@Repository
public interface DumpsterRepository extends JpaRepository<Dumpster, Long> { 
	
	Optional<Dumpster> findByPostalCode(String postal_code);
}

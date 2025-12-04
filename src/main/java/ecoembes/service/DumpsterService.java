package ecoembes.service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ecoembes.dao.DumpsterRepository;
import ecoembes.dao.UsageRepository;
import ecoembes.dto.UsageDTO;
import ecoembes.entity.Dumpster;
import ecoembes.entity.FillLevel;
import ecoembes.entity.Usage;

@Service
public class DumpsterService {
	
	private final DumpsterRepository dumpsterRepository;
	
	private final UsageRepository usageRepository;
	
	public DumpsterService(DumpsterRepository dumpsterRepository, UsageRepository usageRepository) {
		this.dumpsterRepository = dumpsterRepository;
		this.usageRepository = usageRepository;
	}
	

		//Get all dumpsters
		public List<Dumpster> getAllDumpsters() {
			return dumpsterRepository.findAll();
		}

		//Get dumpsters of specific area (by postal code)
		public List<Dumpster> getDumpstersByPostalCode(String postal_code) {			
			List<Dumpster> dumpster = dumpsterRepository.findByPostalCode(postal_code);
			
			if (dumpster.isEmpty()) {
	            throw new RuntimeException("Dumpster not found");
	        }
			
			return dumpster; 
		}
		
		//Get dumpster by id 
		public Dumpster getDumpsterById(Long dumpster_id) {
			Optional<Dumpster> dumpster = dumpsterRepository.findById(dumpster_id);
			
			return dumpster.isPresent() ? dumpster.get() : null;
		}
		
		//Update dumpster TODO !!!
		public void updateDumpster(Long dumpster_id, int container_number, FillLevel fill_level) {
			Optional<Dumpster> dumpsterOp = dumpsterRepository.findById(dumpster_id);
			
			if (dumpsterOp.isPresent()) {
				Usage usage = new Usage();
				usage.setDate(LocalDate.now());
				usage.setFill_level(fill_level);
				usageRepository.save(usage);
				
				Dumpster dumpster = dumpsterOp.get();
				usage.setDumpster(dumpster);
				dumpster.setContainer_number(container_number);
				dumpster.setFill_level(fill_level);
				dumpsterRepository.save(dumpster);
			}
		}
		
		
		//Get usage of a dumpster given a period of 2 dates  
		public List<UsageDTO> getDumpsterUsage(Dumpster dumpster, LocalDate start_date, LocalDate end_date) {
			List<Usage> usage = usageRepository.findByDumpsterAndDateBetween(dumpster, start_date, end_date);
			if (usage.isEmpty()) {
	            return new ArrayList<>();
	        }
			List<UsageDTO> usageDTO = usage.stream().map(UsageDTO::new).toList();
			return usageDTO;
		}
}

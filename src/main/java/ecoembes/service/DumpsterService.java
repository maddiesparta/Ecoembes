package ecoembes.service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	//private static Map<String, Dumpster> dumpsterRepository = new HashMap<>();
	private final DumpsterRepository dumpsterRepository;
	
	//private static Map<Dumpster, List<UsageDTO>> usageRepository = new HashMap<>();
	private final UsageRepository usageRepository;
	
	public DumpsterService(DumpsterRepository dumpsterRepository, UsageRepository usageRepository) {
		this.dumpsterRepository = dumpsterRepository;
		this.usageRepository = usageRepository;
	}
	

		//Get all dumpsters UPDATED
		public List<Dumpster> getAllDumpsters() {
			//return dumpsterRepository.values().stream().toList();
			return dumpsterRepository.findAll();
		}
		
		//Get dumpsters of specific area (by postal code) OLD
//		public List<Dumpster> getDumpstersByPostalCode(String postal_code,List<Dumpster> dumpsters) {
//			List<Dumpster> result = new ArrayList<>();
//			for(Dumpster d : dumpsters) {
//				if(d.getPostal_code().equals(postal_code)) {
//					result.add(d);
//				}
//			}
//			return result;
//		}
		//Get dumpsters of specific area (by postal code) NEW
		public Optional<Dumpster> getDumpstersByPostalCode(String postal_code) {			
			Optional<Dumpster> dumpster = dumpsterRepository.findByPostalCode(postal_code);
			
			if (dumpster.isEmpty()) {
	            throw new RuntimeException("Dumpster not found");
	        }
			
			return dumpster; 
		}
		
		//Get dumpster by id OLD
//		public static Dumpster getDumpsterById(String dumpster_id) {
//			return dumpsterRepository.get(dumpster_id);
//		}
		
		//Get dumpster by id NEW
		public Dumpster getDumpsterById(long dumpster_id) {
			Optional<Dumpster> dumpster = dumpsterRepository.findById(dumpster_id);
			
			return dumpster.isPresent() ? dumpster.get() : null;
		}
		
		
		//Add new dumpster    (NOT NEEDED NOW) 
//		public void addDumpster(Dumpster dumpster) {
//			if(dumpsterRepository.get(dumpster.getDumpster_id()) != null) {
//				throw new IllegalArgumentException("Dumpster with id " + dumpster.getDumpster_id() + " already exists.");
//			}else {
//				dumpsterRepository.put(dumpster.getDumpster_id(), dumpster);
//			}
//			
//		}
			
		
		//Update dumpster TODO !!!
		public void updateDumpster(String dumpster_id, int container_number, FillLevel fill_level) {
			Dumpster dumpster = dumpsterRepository.get(dumpster_id);
			
			if (dumpster != null) {
				dumpsterRepository.remove(dumpster_id);
				UsageDTO usage = new UsageDTO(LocalDate.now(), fill_level);
				if(usageRepository.get(dumpster) == null) {
					usageRepository.put(dumpster, new ArrayList<>());
					usageRepository.get(dumpster).add(usage);
				}else {
					usageRepository.get(dumpster).add(usage);
				}
				dumpster.setContainer_number(container_number);
				dumpster.setFill_level(fill_level);
				dumpsterRepository.put(dumpster_id, dumpster);
			}
		}
		
		
		//Get usage of a dumpster given a period of 2 dates  OLD
//		public List<UsageDTO> getDumpsterUsage(Dumpster dumpster, LocalDate start_date, LocalDate end_date) {
//			List<UsageDTO> result = new ArrayList<>();
//			List<UsageDTO> usages = usageRepository.get(dumpster);
//			if(usages == null) {
//				return result;
//			}
//			for(UsageDTO u : usages) {
//				if(u.getDate().compareTo(start_date) >= 0 && u.getDate().compareTo(end_date) <= 0) {
//					result.add(u);
//				}
//			}
//			return result;
//		}
		
		//Get usage of a dumpster given a period of 2 dates  NEW (NS SI ESTÁ BIEN 100% --> REVISAR)
		public Optional<Usage> getDumpsterUsage(Dumpster dumpster, LocalDate start_date, LocalDate end_date) {
			Optional<Usage> usage = usageRepository.findByDumpsterDateBetween(dumpster, start_date, end_date);
			if (usage.isEmpty()) {
	            throw new RuntimeException("Usage not found");
	        }			
			return usage;
		}
}

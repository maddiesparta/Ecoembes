package ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ecoembes.dto.UsageDTO;
import ecoembes.entity.Dumpster;
import ecoembes.entity.FillLevel;

@Service
public class DumpsterService {
	private static Map<String, Dumpster> dumpsterRepository = new HashMap<>();
	private static Map<Dumpster, List<UsageDTO>> usageRepository = new HashMap<>();

	//Get all dumpsters
		public List<Dumpster> getAllDumpsters() {
			return dumpsterRepository.values().stream().toList();
		}
		
		//Get dumpsters of specific area (by postal code)
		public List<Dumpster> getDumpstersByPostalCode(String postal_code,List<Dumpster> dumpsters) {
			List<Dumpster> result = new ArrayList<>();
			for(Dumpster d : dumpsters) {
				if(d.getPostal_code().equals(postal_code)) {
					result.add(d);
				}
			}
			return result;
		}
		
		//Get dumpster by id
		public static Dumpster getDumpsterById(String dumpster_id) {
			return dumpsterRepository.get(dumpster_id);
		}
		
		//Add new dumpster
		public void addDumpster(Dumpster dumpster) {
			if(dumpsterRepository.get(dumpster.getDumpster_id()) != null) {
				throw new IllegalArgumentException("Dumpster with id " + dumpster.getDumpster_id() + " already exists.");
			}else {
				dumpsterRepository.put(dumpster.getDumpster_id(), dumpster);
			}
			
		}
		
		//Update dumpster
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
		
		//Get usage of a dumpster given a period of 2 dates
		public List<UsageDTO> getDumpsterUsage(Dumpster dumpster, LocalDate start_date, LocalDate end_date) {
			List<UsageDTO> result = new ArrayList<>();
			List<UsageDTO> usages = usageRepository.get(dumpster);
			if(usages == null) {
				return result;
			}
			for(UsageDTO u : usages) {
				if(u.getDate().compareTo(start_date) >= 0 && u.getDate().compareTo(end_date) <= 0) {
					result.add(u);
				}
			}
			return result;
		}
}

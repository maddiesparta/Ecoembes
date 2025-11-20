package ecoembes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ecoembes.entity.Dumpster;

@Service
public class DumpsterService {
	private static Map<String, Dumpster> dumpsterRepository = new HashMap<>();

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
			dumpsterRepository.put(dumpster.getDumpster_id(), dumpster);
		}
}

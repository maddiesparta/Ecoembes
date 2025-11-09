package ecoembes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ecoembes.dto.AreaSnapshotDTO;
import ecoembes.entity.Dumpster;
import ecoembes.entity.RecyclingPlant;

@Service
public class EcoembesService {
	private static Map<String, Dumpster> dumpsterRepository = new HashMap<>();
	private static Map<String, RecyclingPlant> plantRespository = new HashMap<>();
	
	//Get all dumpsters
	public List<Dumpster> getAllDumpsters() {
		return dumpsterRepository.values().stream().toList();
	}
	
	//Get dumpsters of specific area (by postal code)
	public List<Dumpster> getDumpstersByPostalCode(int postal_code) {
		List<Dumpster> result = new ArrayList<>();
		for(Dumpster d : dumpsterRepository.values()) {
			if(d.getPostal_code() == postal_code) {
				result.add(d);
			}
		}
		return result;
	}
	
	//Get recycling plant by id
	public RecyclingPlant getRecyclingPlantById(String plant_id) {
		return plantRespository.get(plant_id);
	}
	
	//Get dumpster by id
	public Dumpster getDumpsterById(String dumpster_id) {
		return dumpsterRepository.get(dumpster_id);
	}
	
	//Add new dumpster
	public void addDumpster(Dumpster dumpster) {
		dumpsterRepository.put(dumpster.getDumpster_id(), dumpster);
	}
	
	//Add new recycling plant
	public void addRecyclingPlant(RecyclingPlant plant) {
		plantRespository.put(String.valueOf(plant.getPlant_id()), plant);
	}
	
	//Assign dumpster to plant
	public void assignDumpsterToPlant(Dumpster dumpster, RecyclingPlant plant) {
		plant.getDumpsters().add(dumpster);
		plant.setCurrent_capacity(plant.getCurrent_capacity() + dumpster.getEstimated_weight());
	}
	//Calculate summary
	public AreaSnapshotDTO calculateAreaSnapshot(List<Dumpster> dumpsters,int postal_code) {
		int total_dumpsters = dumpsters.size();
		int green_count = 0;
		int orange_count = 0;
		int red_count = 0;
		for(Dumpster d : dumpsters) {
			switch(d.getFill_level()) {
				case GREEN -> green_count++;
				case ORANGE -> orange_count++;
				case RED -> red_count++;
			}
		}
		return new AreaSnapshotDTO(postal_code,total_dumpsters, green_count, orange_count, red_count);
	}
}

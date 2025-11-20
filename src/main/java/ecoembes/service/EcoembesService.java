package ecoembes.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ecoembes.entity.Allocation;
import ecoembes.entity.Dumpster;
import ecoembes.entity.RecyclingPlant;


@Service
public class EcoembesService {
	private static Map<String, RecyclingPlant> plantRespository = new HashMap<>();
	
	//Get recycling plant by id
	public RecyclingPlant getRecyclingPlantById(String plant_id) {
		return plantRespository.get(plant_id);
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
	
	
}

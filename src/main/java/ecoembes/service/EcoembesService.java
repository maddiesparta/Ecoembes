package ecoembes.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ecoembes.entity.Allocation;
import ecoembes.entity.Dumpster;
import ecoembes.entity.Employee;
import ecoembes.entity.RecyclingPlant;


@Service
public class EcoembesService {
	private static Map<String, RecyclingPlant> plantRespository = new HashMap<>();
	private static Map<String, Allocation> allocationRepository = new HashMap<>();
	
	//Get recycling plant by id
	public RecyclingPlant getRecyclingPlantById(String plant_id) {
		return plantRespository.get(plant_id);
	}
	
	//Add new recycling plant
	public void addRecyclingPlant(RecyclingPlant plant) {
		if(plantRespository.get(String.valueOf(plant.getPlant_id())) != null) {
			throw new IllegalArgumentException("Recycling plant with id " + plant.getPlant_id() + " already exists.");
		}
		plantRespository.put(String.valueOf(plant.getPlant_id()), plant);
	}
	
	//Assign dumpster to plant
	public void createAllocation(Dumpster dumpster, RecyclingPlant plant,Employee employee) {
		if(plant.getCurrent_capacity() + dumpster.getCapacity() > plant.getTotal_capacity()) {
			throw new IllegalArgumentException("Cannot allocate dumpster: plant capacity exceeded.");
		}else {
			plant.setCurrent_capacity(plant.getCurrent_capacity() + dumpster.getCapacity());
			Allocation allocation = new Allocation();
			allocation.setDumpster(dumpster);
			allocation.setPlant(plant);
			allocation.setEmployee(employee);
			allocationRepository.put(String.valueOf(allocation.getAllocation_id()), allocation);
		}
		
		
	}
	//Get all plants
	public List<RecyclingPlant> getAllPlants() {
		return plantRespository.values().stream().toList();
	}
	
	
}

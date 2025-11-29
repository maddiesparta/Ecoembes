package ecoembes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ecoembes.dao.AllocationRepository;
import ecoembes.dao.RecyclingPlantRepository;
import ecoembes.entity.Allocation;
import ecoembes.entity.Dumpster;
import ecoembes.entity.Employee;
import ecoembes.entity.RecyclingPlant;


@Service
public class EcoembesService {
	
	//private static Map<String, RecyclingPlant> plantRespository = new HashMap<>();
	private final RecyclingPlantRepository recyclingPlantRepository;
	
	//private static Map<String, Allocation> allocationRepository = new HashMap<>();
	private final AllocationRepository allocationRepository;
	
	public EcoembesService(RecyclingPlantRepository recyclingPlantRepository, AllocationRepository allocationRepository) {
    	this.recyclingPlantRepository = recyclingPlantRepository;
    	this.allocationRepository = allocationRepository;
    }
	
	//Get all plants NEW
	public List<RecyclingPlant> getAllPlants() {
		return recyclingPlantRepository.findAll();
	}
	
	//Get recycling plant by id OLD
//	public RecyclingPlant getRecyclingPlantById(String plant_id) {
//		return recyclingPlantRepository.get(plant_id);
//	}
	
	//Get recycling plant by id NEW
	public RecyclingPlant getRecyclingPlantById(long plant_id) {
		Optional<RecyclingPlant> plant = recyclingPlantRepository.findById(plant_id);
		
		return plant.isPresent() ? plant.get() : null;
	}
	
	//Add new recycling plant
//	public void addRecyclingPlant(RecyclingPlant plant) {
//		if(recyclingPlantRepository.get(String.valueOf(plant.getPlant_id())) != null) {
//			throw new IllegalArgumentException("Recycling plant with id " + plant.getPlant_id() + " already exists.");
//		}
//		recyclingPlantRepository.put(String.valueOf(plant.getPlant_id()), plant);
//	}
	
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
			//allocationRepository.put(String.valueOf(allocation.getAllocation_id()), allocation);
			allocationRepository.save(allocation); // Con esto debería funcionar --> REPASAR
		}
		
		
	}
	//Get all plants OLD
//	public List<RecyclingPlant> getAllPlants() {
//		return recyclingPlantRepository.values().stream().toList();
//	}
	
	
}

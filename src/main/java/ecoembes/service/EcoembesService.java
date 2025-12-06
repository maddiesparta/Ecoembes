package ecoembes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ecoembes.dao.AllocationRepository;
import ecoembes.dao.RecyclingPlantRepository;
import ecoembes.entity.Allocation;
import ecoembes.entity.Dumpster;
import ecoembes.entity.Employee;
import ecoembes.entity.LogInType;
import ecoembes.entity.RecyclingPlant;
import ecoembes.factory.GatewayFactory;


@Service
public class EcoembesService {
	
	private final RecyclingPlantRepository recyclingPlantRepository;
	
	
	private final AllocationRepository allocationRepository;
	
	private final GatewayFactory gatewayFactory;
	
	public EcoembesService(RecyclingPlantRepository recyclingPlantRepository, AllocationRepository allocationRepository, GatewayFactory gatewayFactory) {
    	this.recyclingPlantRepository = recyclingPlantRepository;
    	this.allocationRepository = allocationRepository;
		this.gatewayFactory = gatewayFactory;
    }
	
	//Get all plants 
	public List<RecyclingPlant> getAllPlants() {
		return recyclingPlantRepository.findAll();
		
	}

	//Get recycling plant by id 
	public RecyclingPlant getRecyclingPlantById(long plant_id) {
		Optional<RecyclingPlant> plant = recyclingPlantRepository.findById(plant_id);
		
		return plant.isPresent() ? plant.get() : null;
	}
	
	public LogInType getLogInTypeByPlantName(String plant_name) {
	    if (plant_name.toUpperCase().startsWith("PLASSB")) return LogInType.PLASSB;
	    if (plant_name.toUpperCase().startsWith("CONT")) return LogInType.CONTSOCKET;
	    throw new IllegalArgumentException("Unknown plant name: " + plant_name);
	}
	
	public float getPlantCapacity(String plant_name) {
		LogInType p = getLogInTypeByPlantName(plant_name);
		return gatewayFactory.createGateway(p).getCapacity();
	}
	
	public void sendNotification(String plant_name, int dumpsters, int packages, float tons) {
		LogInType p = getLogInTypeByPlantName(plant_name);
		gatewayFactory.createGateway(p).sendNotification(dumpsters, packages, tons);
	}
	
	//Assign dumpster to plant
	public void createAllocation(Dumpster dumpster, RecyclingPlant plant,Employee employee) {
		Allocation allocation = new Allocation();
		allocation.setDumpster(dumpster);
		allocation.setPlant(plant);
		allocation.setEmployee(employee);
		allocationRepository.save(allocation);

	}
	public boolean checkDumpsterAllocation(Dumpster dumpster) {
		List<Allocation> allocated = allocationRepository.findAll();
		for (Allocation allocation : allocated) {
			if(allocation.getDumpster().getDumpster_id() == dumpster.getDumpster_id()) {
				return true;
			}
		}
		return false;
	}
	public boolean createAssignment(List<Dumpster> ds, RecyclingPlant rp, Employee e) {
		int dumpsters = 0;
		int packages = 0;
		float tons = 0;
		for (Dumpster dumpster : ds) {
			dumpsters++;
			packages = dumpster.getContainer_number()+packages;
			tons = dumpster.getEstimated_weight()+tons;
		}
		if(tons<=getPlantCapacity(rp.getPlant_name())) {
			for (Dumpster dumpster : ds) {
				createAllocation(dumpster, rp, e);
			}
			sendNotification(rp.getPlant_name(),dumpsters, packages, tons);
			return true;
		}else {
			return false;
		}
		
	}

	public List<Allocation> getAllAllocations() {
		List<Allocation> a = allocationRepository.findAll();
		return a;
	}
	
}

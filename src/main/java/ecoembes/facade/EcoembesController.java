package ecoembes.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecoembes.dto.AllocationDTO;
import ecoembes.dto.RecyclingPlantDTO;
import ecoembes.entity.Allocation;
import ecoembes.entity.Dumpster;
import ecoembes.entity.Employee;
import ecoembes.entity.RecyclingPlant;
import ecoembes.factory.GatewayFactory;
import ecoembes.service.AuthService;
import ecoembes.service.DumpsterService;
import ecoembes.service.EcoembesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.ValidatedParameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/ecoembes")
public class EcoembesController {

    private final GatewayFactory gatewayFactory;

	private final EcoembesService ecoembesService;
	private final DumpsterService dumpsterService;
	
	public EcoembesController(EcoembesService ecoembesService, DumpsterService dumpsterService, GatewayFactory gatewayFactory) {
		this.ecoembesService = ecoembesService;
		this.dumpsterService = dumpsterService;
		this.gatewayFactory = gatewayFactory;
	}
	//view allocations
	@Operation(
	        summary = "Get all allocations",
	        description = "Retrieve all dumpster allocations to recycling plants",
	        responses={
	            @ApiResponse(responseCode = "200", description = "OK: Successfully retrieved all allocations"),
	            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid or missing authentication token"),
	            @ApiResponse(responseCode = "500", description = "Internal Server error")
	        })
	    @GetMapping("/allocations")
	    public ResponseEntity<List<AllocationDTO>> getAllAllocations(
	    		@RequestHeader("Authorization") String authHeader) {
	        
	        try {
	        	if(authHeader == null || !authHeader.startsWith("Bearer ")) {
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				String token = authHeader.substring(7); //Remove "Bearer " from token
				Employee employee = AuthService.validateToken(token);
				if(employee == null) {
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
	            
	            List<Allocation> allocations = ecoembesService.getAllAllocations();
	            List<AllocationDTO> allocationDTOs = allocations.stream()
	            		.map(this::allocationToDTO)
	            		.collect(Collectors.toList());
	            return new ResponseEntity<>(allocationDTOs, HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	
	
	//POST to assign(allocate) a dumpster to a recycling plant
	@Operation(
		summary = "Assign a dumpster to a recycling plant",
		description = "Assigns a specific dumpster to a recycling plant based on their IDs and capacity.",
		responses = {
				@ApiResponse(responseCode = "200", description = "OK: Successfully assigned the dumpster to the recycling plant"),
				@ApiResponse(responseCode = "400", description = "Bad Request: Capacity exceeded"),
				@ApiResponse(responseCode = "404", description = "Not Found: Dumpster or recycling plant not found"),
				@ApiResponse(responseCode = "401", description = "Unauthorized: Invalid or missing authentication token"),
				@ApiResponse(responseCode = "409", description = "Conflict: Dumpster is already allocated"),
				@ApiResponse(responseCode = "500", description = "Internal Server error")
		}
	)
	@PostMapping("/plants/{plant_id}/assign")
	public ResponseEntity<Void> assignDumpsterToPlant(
			@ValidatedParameter
			@Parameter(name="plant_id",description = "ID of the plant",required=true,example="1") 
			@PathVariable ("plant_id") long plant_id,
			@ValidatedParameter
			@Parameter(name = "dumpster_ids", description = "List of Dumpster IDs", required = true, example = "[4,5,6]")
			@RequestParam(name = "dumpster_ids") Long[] dumpster_ids,
			@RequestHeader("Authorization") String authHeader){
		try {
			if(authHeader == null || !authHeader.startsWith("Bearer ")) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			String token = authHeader.substring(7); //Remove "Bearer " from token
			Employee employee = AuthService.validateToken(token);
			if(employee == null) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			List<Dumpster> dumpsters = new ArrayList<Dumpster>();
			if (dumpster_ids == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			for(Long dumpster_id : Arrays.asList(dumpster_ids)) {
				Dumpster dumpster = dumpsterService.getDumpsterById(dumpster_id);
				dumpsters.add(dumpster);
			}
			if (dumpsters.isEmpty() || dumpsters.contains(null)) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			RecyclingPlant plant = ecoembesService.getRecyclingPlantById(plant_id);
			if (plant == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			for(Dumpster dumpster : dumpsters) {
				if(ecoembesService.checkDumpsterAllocation(dumpster)) {
					
					return new ResponseEntity<>(HttpStatus.CONFLICT);	
				}
				
			}
			
			if(ecoembesService.createAssignment(dumpsters, plant, employee) == false) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(); 
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	//GET all recycling plants (to check capacity)
		@Operation(
			summary = "Get all recycling plants",
			description = "Retrieves a list of all plants in the system.",
			responses = {
					@ApiResponse(responseCode = "200", description = "OK: Successfully retrieved the list of plants"),
					@ApiResponse(responseCode = "204", description = "No Content: No plants found"),
					@ApiResponse(responseCode = "500", description = "Internal Server error")
			}
		)
			
		@GetMapping("/plants")
		public ResponseEntity<List<RecyclingPlantDTO>> getAllRecyclingPlants(
				@RequestHeader("Authorization") String authHeader){
			try {
				if(authHeader == null || !authHeader.startsWith("Bearer ")) {
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				String token = authHeader.substring(7); 
				Employee employee = AuthService.validateToken(token);
				if(employee == null) {
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				List<RecyclingPlant> plants = ecoembesService.getAllPlants();
				if (plants.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				} else {
					List<RecyclingPlantDTO> plantDTOs = new ArrayList<>();
					for(RecyclingPlant plant : plants) {
						RecyclingPlantDTO plantDTO = plantToDTO(plant);
						plantDTOs.add(plantDTO);
					}
					return new ResponseEntity<>(plantDTOs, HttpStatus.OK);
				}
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	//Converts RecyclingPlant to RecyclingPlantDTO
	private RecyclingPlantDTO plantToDTO(RecyclingPlant plant) {
		
		return new RecyclingPlantDTO(
				plant.getPlant_id(),
				plant.getPlant_name(),
				ecoembesService.getPlantCapacity(plant.getPlant_name()),
				plant.getTotal_capacity()
		);
	}
	//Converts Allocation to AllocationDTO
		private AllocationDTO allocationToDTO(Allocation allocation) {
			
			return new AllocationDTO(allocation);
		}
	
	
	//Gets external plants capacity(current)
	@Operation(
			summary = "Get plant capacity",
			description = "Gets an external plants current capacity from the external servers.",
			responses = {
					@ApiResponse(responseCode = "200", description = "OK: Successfully retrieved the list of plants"),
					@ApiResponse(responseCode = "204", description = "No Content: No plants found"),
					@ApiResponse(responseCode = "500", description = "Internal Server error")
			}
		)
	@GetMapping("/plants/{plant_name}/current_capacity")
	public ResponseEntity<Float> getPlantCapacity(
			@ValidatedParameter
			@Parameter(name="plant_name",description = "Name of the plant",required=true,example="PlasSB") 
			@PathVariable ("plant_name") String plant_name,
			@RequestHeader("Authorization") String authHeader){
		try {
			if(authHeader == null || !authHeader.startsWith("Bearer ")) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			String token = authHeader.substring(7); //Quitar el Bearer del token
			Employee employee = AuthService.validateToken(token);
			if(employee == null) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			float plant = ecoembesService.getPlantCapacity(plant_name);
			return new ResponseEntity<Float>(plant, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
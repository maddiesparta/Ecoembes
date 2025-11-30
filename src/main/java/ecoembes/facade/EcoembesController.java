package ecoembes.facade;

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

import ecoembes.dto.DumpsterDTO;
import ecoembes.dto.RecyclingPlantDTO;
import ecoembes.entity.Dumpster;
import ecoembes.entity.Employee;
import ecoembes.entity.RecyclingPlant;
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

	private final EcoembesService ecoembesService;
	private final DumpsterService dumpsterService;
	
	public EcoembesController(EcoembesService ecoembesService, DumpsterService dumpsterService) {
		this.ecoembesService = ecoembesService;
		this.dumpsterService = dumpsterService;
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
				@ApiResponse(responseCode = "500", description = "Internal Server error")
		}
	)
	@PostMapping("/plants/{plant_id}/assign/{dumpster_id}")
	public ResponseEntity<Void> assignDumpsterToPlant(
			@ValidatedParameter
			@Parameter(name = "dumpster_id", description = "ID of the dumpster", required = true, example = "d1")
			@PathVariable ("dumpster_id") Long dumpster_id,
			@ValidatedParameter
			@Parameter(name="plant_id",description = "ID of the plant",required=true,example="p1") 
			@PathVariable ("plant_id") long plant_id,
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
			Dumpster dumpster = dumpsterService.getDumpsterById(dumpster_id);
			if (dumpster == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			RecyclingPlant plant = ecoembesService.getRecyclingPlantById(plant_id);
			if (plant == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			if(plant.getTotal_capacity() < plant.getCurrent_capacity() + dumpster.getCapacity()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}else {
				ecoembesService.createAllocation(dumpster, plant, employee);
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Get recycling plant info by id
	@Operation(
			summary = "Get recycling plant by ID",
			description = "Get information of a recycling plant based on its ID.",
			responses = {
					@ApiResponse(responseCode = "200", description = "OK: Successfully retrieved plant summary"),
					@ApiResponse(responseCode = "204", description = "No Content: No plant found with the specified ID"),
					@ApiResponse(responseCode = "400", description = "Bad Request: ID is invalid"),
					@ApiResponse(responseCode = "500", description = "Internal Server error")
			}
		)
		@GetMapping("/plants/{plant_id}")
		public ResponseEntity<RecyclingPlantDTO> getRecyclingPlantById(
				@ValidatedParameter
				@Parameter(name="plant_id",description = "ID of the plant",required=true,example="p1") 
				@PathVariable ("plant_id") long plant_id,
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
				RecyclingPlant plant = ecoembesService.getRecyclingPlantById(plant_id);
				if (plant == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}else {
					RecyclingPlantDTO plantDTO = plantToDTO(plant);
					return new ResponseEntity<>(plantDTO, HttpStatus.OK);
				}
			} catch (Exception e) {
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
				String token = authHeader.substring(7); //Quitar el Bearer del token
				Employee employee = AuthService.validateToken(token);
				if(employee == null) {
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				List<RecyclingPlant> plants = ecoembesService.getAllPlants();
				if (plants.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				List<RecyclingPlantDTO> plantsDTOs = plants.stream().map(plant -> 
				new RecyclingPlantDTO(plant.getPlant_name(), plant.getCurrent_capacity(),plant.getTotal_capacity())).collect(Collectors.toList());
				return new ResponseEntity<>(plantsDTOs, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	//Converts RecyclingPlant to RecyclingPlantDTO
	private RecyclingPlantDTO plantToDTO(RecyclingPlant plant) {
		return new RecyclingPlantDTO(
				plant.getPlant_name(),
				plant.getCurrent_capacity(),
				plant.getTotal_capacity()
		);
	}
}

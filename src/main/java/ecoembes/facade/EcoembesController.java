package ecoembes.facade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
	
	public EcoembesController(EcoembesService ecoembesService) {
		this.ecoembesService = ecoembesService;
	}
	
	
	
	//POST to assign(allocate) a dumpster to a recycling plant
	@Operation(
		summary = "Assign a dumpster to a recycling plant",
		description = "Assigns a specific dumpster to a recycling plant based on their IDs and capacity.",
		responses = {
				@ApiResponse(responseCode = "200", description = "OK: Successfully assigned the dumpster to the recycling plant"),
				@ApiResponse(responseCode = "400", description = "Bad Request: Capacity exceeded"),
				@ApiResponse(responseCode = "404", description = "Not Found: Dumpster or recycling plant not found"),
				@ApiResponse(responseCode = "405", description = "Unauthorized: Invalid or missing authentication token"),
				@ApiResponse(responseCode = "500", description = "Internal Server error")
		}
	)
	@PostMapping("/dumpsters/{dumpster_id}/assign/{plant_id}")
	public ResponseEntity<Void> assignDumpsterToPlant(
			@ValidatedParameter
			@Parameter(name = "dumpster_id", description = "ID of the dumpster", required = true, example = "d1")
			@PathVariable("dumpster_id") String dumpster_id,
			@ValidatedParameter
			@PathVariable("plant_id") String plant_id,
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
			Dumpster dumpster = DumpsterService.getDumpsterById(dumpster_id);
			if (dumpster == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			RecyclingPlant plant = ecoembesService.getRecyclingPlantById(plant_id);
			if (plant == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			if(plant.getTotal_capacity() < plant.getCurrent_capacity() + dumpster.getCapacity()) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				ecoembesService.createAllocation(dumpster, plant, employee);
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	//Converts RecyclingPlant to RecyclingPlantDTO
	private RecyclingPlantDTO plantToDTO(RecyclingPlant plant) {
		return new RecyclingPlantDTO(
				plant.getPlant_name(),
				plant.getCurrent_capacity()
		);
	}
	
	
}

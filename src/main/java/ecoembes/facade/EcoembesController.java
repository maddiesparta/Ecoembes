package ecoembes.facade;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecoembes.dto.DumpsterDTO;
import ecoembes.dto.RecyclingPlantDTO;
import ecoembes.entity.Dumpster;
import ecoembes.entity.RecyclingPlant;
import ecoembes.service.AuthService;
import ecoembes.service.EcoembesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.ValidatedParameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/ecoembes")
public class EcoembesController {

	private final AuthService authService;
	private final EcoembesService ecoembesService;
	
	public EcoembesController(AuthService authService, EcoembesService ecoembesService) {
		this.authService = authService;
		this.ecoembesService = ecoembesService;
	}
	
	//GET all dumpsters
	@Operation(
		summary = "Get all dumpsters",
		description = "Retrieves a list of all dumpsters in the system.",
		responses = {
				@ApiResponse(responseCode = "200", description = "OK: Successfully retrieved the list of dumpsters"),
				@ApiResponse(responseCode = "204", description = "No Content: No dumpsters found"),
				@ApiResponse(responseCode = "500", description = "Internal Server error")
		}
	)
	
	@GetMapping("/dumpsters")
	public ResponseEntity<List<DumpsterDTO>> getAllDumpsters() {
		try {
			List<Dumpster> dumpsters = ecoembesService.getAllDumpsters();
			if (dumpsters.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			List<DumpsterDTO> dumpsterDTOs = dumpsters.stream()
					.map(dumpster -> new DumpsterDTO(dumpster.getDumpster_id(), dumpster.getLocation(), dumpster.getPostal_code(),
							dumpster.getFill_level(), dumpster.getContainer_number()))
					.collect(Collectors.toList());
			return new ResponseEntity<>(dumpsterDTOs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//GET dumpsters state by postal code
	@Operation(
		summary = "Get dumpsters state by postal code",
		description = "Returns snapshot of dumpster activity in a specific area",
		responses = {
				@ApiResponse(responseCode = "200", description = "OK: Successfully retrieved the list of dumpsters"),
				@ApiResponse(responseCode = "204", description = "No Content: No dumpsters found in the specified postal code"),
				@ApiResponse(responseCode = "500", description = "Internal Server error")
		}
	)
	@GetMapping("/dumpsters/{postal_code}/state")
	public ResponseEntity<List<DumpsterDTO>> getDumpstersByPostalCode(
			@Parameter(name = "dumpster_id", description = "Name of the dumpster", required = true, example = "d1")
			@PathVariable int dumpster_id,
			@Parameter(name = "postal_code", description = "Postal code", required = true, example = "111111")
			@RequestParam int postal_code) {
		try {
			List<Dumpster> dumpsters = ecoembesService.getDumpstersByPostalCode(postal_code);
			if (dumpsters.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			List<DumpsterDTO> dumpsterDTOs = dumpsters.stream()
					.map(dumpster -> new DumpsterDTO(dumpster.getDumpster_id(), dumpster.getLocation(), dumpster.getPostal_code(),
							dumpster.getFill_level(), dumpster.getContainer_number()))
					.collect(Collectors.toList());
			return new ResponseEntity<>(dumpsterDTOs, HttpStatus.OK);
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
				@ApiResponse(responseCode = "500", description = "Internal Server error")
		}
	)
	@PostMapping("/dumpsters/{dumpster_id}/assign/{plant_id}")
	public ResponseEntity<Void> assignDumpsterToPlant(
			@ValidatedParameter
			@Parameter(name = "dumpster_id", description = "ID of the dumpster", required = true, example = "d1")
			@PathVariable String dumpster_id,
			@ValidatedParameter
			@Parameter(name = "plant_id", description = "ID of the recycling plant", required = true, example = "p1")
			@PathVariable String plant_id) {
		try {
			Dumpster dumpster = ecoembesService.getDumpsterById(dumpster_id);
			if (dumpster == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			var plant = ecoembesService.getRecyclingPlantById(plant_id);
			if (plant == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			if(plant.getTotal_capacity() < plant.getCurrent_capacity() + dumpster.getCapacity()) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			ecoembesService.assignDumpsterToPlant(dumpster, plant);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Converts Dumpster to DumpsterDTO
	private DumpsterDTO dumpsterToDTO(Dumpster dumpster) {
		return new DumpsterDTO(
				dumpster.getDumpster_id(),
				dumpster.getLocation(),
				dumpster.getPostal_code(),
				dumpster.getFill_level(),
				dumpster.getContainer_number()	
		);
	}
	
	//Converts RecyclingPlant to RecyclingPlantDTO
	private RecyclingPlantDTO plantToDTO(RecyclingPlant plant) {
		return new RecyclingPlantDTO(
				plant.getPlant_name(),
				plant.getCurrent_capacity()
		);
		}
	
	
}

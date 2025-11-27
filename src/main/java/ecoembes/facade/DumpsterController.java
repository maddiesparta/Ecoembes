package ecoembes.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecoembes.dto.DumpsterDTO;
import ecoembes.dto.UsageDTO;
import ecoembes.entity.Dumpster;
import ecoembes.service.DumpsterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.ValidatedParameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/dumpster")
public class DumpsterController {

	private final DumpsterService dumpsterService;	
	
	public DumpsterController(DumpsterService dumpsterService) {
		this.dumpsterService = dumpsterService;
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
				List<Dumpster> dumpsters = dumpsterService.getAllDumpsters();
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
			description = "Returns summary of the snapshot of dumpster activity in a specific area",
			responses = {
					@ApiResponse(responseCode = "200", description = "OK: Successfully retrieved dumpsters summary"),
					@ApiResponse(responseCode = "204", description = "No Content: No dumpsters found in the specified postal code"),
					@ApiResponse(responseCode = "400", description = "Bad Request: Postal code is invalid"),
					@ApiResponse(responseCode = "500", description = "Internal Server error")
			}
		)
		@GetMapping("/dumpsters/{postal_code}")
		public ResponseEntity<List<DumpsterDTO>> getDumpstersByPostalCode(
				@ValidatedParameter
		        @Parameter(name = "postal_code", description = "Postal code", required = true, example = "111111")
		        @PathVariable("postal_code") String postal_code){
			
		    try {
		        if(postal_code == null || postal_code.trim().isEmpty()) {
		            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		        }

		        List<Dumpster> allDumpsters = dumpsterService.getAllDumpsters();
		        List<Dumpster> dumpsters = dumpsterService.getDumpstersByPostalCode(postal_code, allDumpsters);

		        if (dumpsters.isEmpty()) {
		            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		        }
		        
		        List<DumpsterDTO> dumpsterDTOs = new ArrayList<DumpsterDTO>();
		        for (Dumpster d : dumpsters) {
		            DumpsterDTO dum = dumpsterToDTO(d);
		            dumpsterDTOs.add(dum);
		        }

		        return new ResponseEntity<>(dumpsterDTOs, HttpStatus.OK);
		        
		    } catch (Exception e) {
		        System.out.println("ERROR: " + e.getMessage());
		        e.printStackTrace();
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}
		
		//PUT to update dumpster's fill level and container number
		@Operation(
			summary = "Update dumpster's fill level and container number",
			description = "Updates the fill level and container number of a specific dumpster based on its ID.",
			responses = {
					@ApiResponse(responseCode = "200", description = "OK: Successfully updated the dumpster"),
					@ApiResponse(responseCode = "404", description = "Not Found: Dumpster not found"),
					@ApiResponse(responseCode = "500", description = "Internal Server error")
			}
		)
		@PutMapping("/dumpsters/{dumpster_id}/update/{container_number}/{fill_level}")
		public ResponseEntity<Void> updateDumpster(
				@ValidatedParameter
		        @Parameter(name = "dumpster_id", description = "ID of the dumpster", required = true, example = "d1")
		        @PathVariable("dumpster_id") String dumpster_id,
		        @ValidatedParameter
		        @Parameter(name = "container_number", description = "Container number", required = true, example = "5")
		        @PathVariable("container_number") int container_number,
		        @ValidatedParameter
		        @Parameter(name = "fill_level", description = "Fill level", required = true, example = "GREEN")
		        @PathVariable("fill_level") String fill_level) {
			try {
				Dumpster dumpster = DumpsterService.getDumpsterById(dumpster_id);
				if (dumpster == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				dumpsterService.updateDumpster(dumpster_id, container_number, ecoembes.entity.FillLevel.valueOf(fill_level));
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//GET dumpster usage between two dates
		@Operation(
			summary = "Get dumpster usage between two dates",
			description = "Retrieves the usage history of a specific dumpster within a given date range.",
			responses = {
					@ApiResponse(responseCode = "200", description = "OK: Successfully retrieved the dumpster usage"),
					@ApiResponse(responseCode = "204", description = "No Content: No usage data found for the specified period"),
					@ApiResponse(responseCode = "404", description = "Not Found: Dumpster not found"),
					@ApiResponse(responseCode = "500", description = "Internal Server error")
			}
		)
		@GetMapping("/dumpsters/{dumpster_id}/usage/{start_date}/{end_date}")
		public ResponseEntity<List<UsageDTO>> getDumpsterUsage(
				@ValidatedParameter
		        @Parameter(name = "dumpster_id", description = "ID of the dumpster", required = true, example = "d1")
		        @PathVariable("dumpster_id") String dumpster_id,
		        @ValidatedParameter
		        @Parameter(name = "start_date", description = "Start date in format YYYY-MM-DD", required = true, example = "2023-01-01")
		        @PathVariable("start_date") String start_date,
		        @ValidatedParameter
		        @Parameter(name = "end_date", description = "End date in format YYYY-MM-DD", required = true, example = "2023-01-31")
		        @PathVariable("end_date") String end_date) {
			try {
				Dumpster dumpster = dumpsterService.getDumpsterById(dumpster_id);
				if (dumpster == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				java.sql.Date startDate = java.sql.Date.valueOf(start_date);
				java.sql.Date endDate = java.sql.Date.valueOf(end_date);
				List<UsageDTO> usageDTOs = dumpsterService.getDumpsterUsage(dumpster, startDate, endDate);
				if (usageDTOs.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				return new ResponseEntity<>(usageDTOs, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
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
}

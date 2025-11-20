package ecoembes.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecoembes.dto.DumpsterDTO;
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

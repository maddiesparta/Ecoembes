package ecoembes.facade;

import java.time.LocalDate;
import java.util.ArrayList;
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

import ecoembes.dao.DumpsterRepository;
import ecoembes.dto.DumpsterDTO;
import ecoembes.dto.UsageDTO;
import ecoembes.entity.Dumpster;
import ecoembes.entity.Employee;

import ecoembes.service.AuthService;
import ecoembes.service.DumpsterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.ValidatedParameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/dumpsters")
public class DumpsterController {

    private final DumpsterRepository dumpsterRepository;

	private final DumpsterService dumpsterService;	
	
	public DumpsterController(DumpsterService dumpsterService, DumpsterRepository dumpsterRepository) {
		this.dumpsterService = dumpsterService;
		this.dumpsterRepository = dumpsterRepository;
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
		
		@GetMapping("/all")
		public ResponseEntity<List<DumpsterDTO>> getAllDumpsters(
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
				List<Dumpster> dumpsters = dumpsterService.getAllDumpsters();
				if (dumpsters.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				List<DumpsterDTO> dumpsterDTOs = dumpsters.stream()
					    .map(this::dumpsterToDTO)
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
		@GetMapping("/dumpster/{postal_code}")
		public ResponseEntity<List<DumpsterDTO>> getDumpstersByPostalCode(
				@ValidatedParameter
		        @Parameter(name = "postal_code", description = "Postal code", required = true, example = "111111")
		        @PathVariable("postal_code") String postal_code,
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
		        if(postal_code == null || postal_code.trim().isEmpty()) {
		            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		        }

		        List<Dumpster> dumpsters = dumpsterService.getDumpstersByPostalCode(postal_code);

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
		//Get dumpster info by id
		@Operation(
				summary = "Get dumpster by ID",
				description = "Get information of a dumpster based on its ID.",
				responses = {
						@ApiResponse(responseCode = "200", description = "OK: Successfully retrieved dumpster summary"),
						@ApiResponse(responseCode = "204", description = "No Content: No dumpster found with the specified ID"),
						@ApiResponse(responseCode = "400", description = "Bad Request: ID is invalid"),
						@ApiResponse(responseCode = "500", description = "Internal Server error")
				}
			)
			@GetMapping("/{dumpster_id}")
			public ResponseEntity<DumpsterDTO> getDumpsterByID(
					@Parameter(name = "dumpster_id", description = "ID of the dumpster", required = true, example = "d1")
		    		@PathVariable ("dumpster_id") Long dumpster_id,
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
					}else {
						DumpsterDTO dumpsterDTO = dumpsterToDTO(dumpster);
						return new ResponseEntity<>(dumpsterDTO, HttpStatus.OK);
					}
				} catch (Exception e) {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		
		//addDumpster
		@Operation(
			summary = "Add Dumpster",
			description = "Allows a user to add Dumpster by providing dumpster details.",
			responses = {
				@ApiResponse(responseCode = "200", description = "OK: Dumpster added successfully"),
				@ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials, operation failed"),
			}
		)
		@PostMapping("/add/")
	    public ResponseEntity<Void> addDumpster(
	    		@Parameter(name = "location", description = "location of the dumpster", required = true, example = "Calle Falsa 123")
	    	    @RequestParam ("location") String location,
	    		    
	    	    @Parameter(name = "postal_code", description = "Postal code", required = true, example = "28001")
	    	    @RequestParam ("postal_code") String postal_code,
	    		    
	    	    @Parameter(name = "capacity", description = "Estimated number of containers it can hold", required = true, example = "10")
	    		@RequestParam ("capacity") int capacity,
	    		    
	    	    @Parameter(name = "container_number", description = "Container number", required = true, example = "1")
	    	    @RequestParam ("container_number") int container_number,
	    		    
	   		    @RequestHeader("Authorization") String authHeader)  {
			try {
	            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	            }
	            String token = authHeader.substring(7); //Remove "Bearer " from token
	            Employee employee = AuthService.validateToken(token);
	            if (employee == null) {
	                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	            }
	            if (location == null || location.trim().isEmpty() ||
	                postal_code == null || postal_code.trim().isEmpty()) {
	                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	            }
	        
	            Dumpster dumpster = new Dumpster(
	            		location,
	            		postal_code,
	            		capacity, 
	            		container_number
	            );
	            
	            dumpsterRepository.save(dumpster);
	            
	            return new ResponseEntity<>(HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }  	
	        
		}
		
		//Dumpster usage between 2 dates
		@Operation(
			summary = "Get dumpster usage between 2 dates",
			description = "Returns usage data of a specific dumpster between two dates",
			responses = {
					@ApiResponse(responseCode = "200", description = "OK: Successfully retrieved dumpster usage data"),
					@ApiResponse(responseCode = "204", description = "No Content: No usage data found for the specified dumpster and date range"),
					@ApiResponse(responseCode = "400", description = "Bad Request: Invalid input parameters"),
					@ApiResponse(responseCode = "500", description = "Internal Server error")
			}
		)
		@GetMapping("/usage/{dumpster_id}")
		public ResponseEntity<List<UsageDTO>> getDumpsterUsageBetweenDates(
				@Parameter(name = "dumpster_id", description = "ID of the dumpster", required = true, example = "d1")
	    		@PathVariable ("dumpster_id") Long dumpster_id,
	    		
	    		@Parameter(name = "start_date", description = "Start date in format YYYY-MM-DD", required = true, example = "2023-01-01")
	            @RequestParam("start_date") String start_date, 
	            
	            @Parameter(name = "end_date", description = "End date in format YYYY-MM-DD", required = true, example = "2023-01-31")
	            @RequestParam("end_date") String end_date, 
	    		
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
				LocalDate start_date_parsed = LocalDate.parse(start_date);
				LocalDate end_date_parsed = LocalDate.parse(end_date);
				 
				List<UsageDTO> usage = dumpsterService.getDumpsterUsage(dumpster, start_date_parsed, end_date_parsed);
				
				if (usage.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}else {
					return new ResponseEntity<>(usage, HttpStatus.OK);
				}
				
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
			        dumpster.getContainer_number(),
			        dumpster.getCapacity()
			    );
		}
}
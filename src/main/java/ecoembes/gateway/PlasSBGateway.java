package ecoembes.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ecoembes.dto.RecyclingPlantDTO;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@Service
public class PlasSBGateway implements IGateway{
	private static final Logger log = LoggerFactory.getLogger(PlasSBGateway.class);
	
	private RestTemplate restTemplate;
	private static PlasSBGateway instance;
	
	@Bean
	RestTemplate restTemplate() {
	      return new RestTemplate();
	}
	
	@Value("${spring.server.url}")
	private String serverURL;
	
	@Value("${server.port}")
	private int serverPort;
	
	public static void start() {
		SpringApplication.run(PlasSBGateway.class);	
	}
		
	public PlasSBGateway() {
		// TODO Auto-generated constructor stub
	} 
	
	@Autowired
	public void setInstance(PlasSBGateway instance) {
		PlasSBGateway.instance = instance;
	 }
	public static PlasSBGateway getInstance() {
		return instance;
	}
	@Autowired
	public void setRestTemplate (RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	// TODO HAY QUE HACER ESTE METODO (Pillar RecPlantDTO igual)
	@Override
	public String getCapacity(LocalDate date) {
		// Hay que cambiar endpoint --> /auctions/categories"
		RecyclingPlantDTO plants = restTemplate.getForObject(serverURL + ":" + String.valueOf(serverPort) + "/plants?date=" + date, RecyclingPlantDTO.class);
		  log.info("dumpsters - All dumpsters info ...");
		  
		  if (plants != null) {
			  log.info(plants.toString());
		  }
		  return "plants -  all dumpsters on console (if not null) ";
	}

}

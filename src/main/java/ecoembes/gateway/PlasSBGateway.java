package ecoembes.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ecoembes.dto.NotificationDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PlasSBGateway implements IGateway {
	private static final Logger log = LoggerFactory.getLogger(PlasSBGateway.class);
    private final RestTemplate restTemplate;
    
    @Value("${plassb.server.url:http://localhost:8081}")
    private String serverURL;
    
    @Value("${plassb.plant.name:PlasSB}")
    private String plantName;
    
    public PlasSBGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    public float getCapacity() {
        try {
        	String url = serverURL + "/api/plants/capacity/current?plant_name=" + plantName;
            Float capacity = restTemplate.getForObject(url, Float.class);
            return capacity;
            
        } catch (Exception e) {
        	log.error("Error getting capacity from PlasSB: {}", e.getMessage());
            return -1;
        }
    }

	@Override
	public void sendAssignmentNotification(int dumpsters, int packages, float tons) {
		//Sends notification in the form of a POST request to PlasSB server
		try {
			String url = serverURL + "/api/plants/allocation?plant_name=" + plantName;
			restTemplate.postForObject(url, new NotificationDTO(dumpsters, packages, tons), Void.class);
		} catch (Exception e) {
			log.error("Error sending notification to PlasSB", e);
		}
		
	}
    

}

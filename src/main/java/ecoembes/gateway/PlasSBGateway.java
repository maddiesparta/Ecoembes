package ecoembes.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ecoembes.dto.RecyclingPlantDTO;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PlasSBGateway implements IGateway {
    private final RestTemplate restTemplate;
    
    @Value("${plassb.server.url:http://localhost:8081}")
    private String serverURL;
    
    public PlasSBGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    public float getCapacity(LocalDate date) {
        try {
            String url = serverURL + "/api/plants/capacity?date=" + date;
            RecyclingPlantDTO plant = restTemplate.getForObject(url, RecyclingPlantDTO.class);
            
            if (plant != null) {
                return plant.getCurrent_capacity();
            } else {
                return -1;
            }
            
        } catch (Exception e) {
            return -1;
        }
    }
}

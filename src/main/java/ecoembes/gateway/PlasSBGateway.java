package ecoembes.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PlasSBGateway implements IGateway {
    private static final Logger log = LoggerFactory.getLogger(PlasSBGateway.class);

    private final RestTemplate restTemplate;

    @Value("${plasSB.url}")
    private String serverURL;

    @Value("${plasSB.port}")
    private int serverPort;

    public PlasSBGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    @Override
//    public String getCapacity(LocalDate date) {
//        String url = serverURL + ":" + serverPort + "/plants?date=" + date;
//        RecyclingPlantDTO plants = restTemplate.getForObject(url, RecyclingPlantDTO.class);
//
//        log.info("dumpsters - All dumpsters info ...");
//        if (plants != null) {
//            log.info(plants.toString());
//        }
//        return "plants - all dumpsters on console (if not null)";
//    }
    @Override
    public String getCapacity(LocalDate date) {
        // Esto evita la conexión real y devuelve datos de prueba
    	//Luego DESCOMENTAR el anterior
        return String.format(
            " Plant: PlasSB |  Capacity on %s: %.2f tons |  Total Capacity: %.2f tons",
            date, 50.0, 200.0
        );
    }
}

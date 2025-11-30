package ecoembes.consumer;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ch.qos.logback.classic.Logger;
import ecoembes.dto.RecyclingPlantDTO;

@Component("ApiConsumer")
@Configuration
public class ApiConsumer implements IApiConsumer, ApplicationContextAware {
	private static final Logger log = LoggerFactory.getLogger(ApiConsumer.class);

	private static ApplicationContext context;
	private RestTemplate restTemplate;
	
	@Bean
	RestTemplate restTemplate() {
	      return new RestTemplate();
	}	
	
	//API Server Host and Port NOT hard-coded: Defined in application.properties
	@Value("${spring.server.url}")
	private String serverURL;
	
	@Value("${api.server.port}")
	private int serverPort;

		
	@Autowired
	public void setRestTemplate(RestTemplate rest) {
		 restTemplate = rest;
	 }
	
	public ApiConsumer () {	}
	
	public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }
	
	public static IApiConsumer getApiConsumer(String beanName) {
		return (IApiConsumer)context.getBean(beanName);
	}

	
	/* *
	 * Interface implementation methods
	 * */
	
	@Override
	public String getCapacity(String date) {
		  RecyclingPlantDTO[] plant = restTemplate.getForObject(serverURL + ":" + String.valueOf(serverPort) + "/capacity?date=" + date, RecyclingPlantDTO[].class);
		  log.info("plant - Capacity info ...");
		  
		  if (plant != null) {
			  	for (RecyclingPlantDTO p: plant) {
			  		log.info(p.toString());
			  	}
		  }
		  return "capacities -  all categories on console (if not null) ";
	}

}

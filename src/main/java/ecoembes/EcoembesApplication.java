package ecoembes;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ecoembes.gateway.PlasSBGateway;

@SpringBootApplication
public class EcoembesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcoembesApplication.class, args);
	}
	
	@Bean
    CommandLineRunner testGateway(PlasSBGateway gateway) {
        return args -> {
            System.out.println("Testing PlasSBGateway...");
            System.out.println(gateway.getCapacity(LocalDate.now()));
        };
    }

}

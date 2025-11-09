package ecoembes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ecoembes.service.AuthService;
import ecoembes.service.EcoembesService;
import ecoembes.entity.Dumpster;
import ecoembes.entity.Employee;
import ecoembes.entity.RecyclingPlant;


@Configuration
public class DataInitializer {
	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

	@Bean
	CommandLineRunner initData(EcoembesService ecoembesService, AuthService authService) {
		return args -> {
			//Create employees
			Employee e0 = new Employee(0, "user", "user@gmail.com", "user");
			Employee e1 = new Employee(1, "Mike", "mike@gmail.com", "M1k3P4ss!");
			Employee e2 = new Employee(2, "Sara", "sara@sara.com", "S4r4P4ss!");
			Employee e3 = new Employee(3, "John", "john@gmail.com", "J0hnP4ss!");
			
			authService.addEmployee(e0);
			authService.addEmployee(e1);
			authService.addEmployee(e2);
			authService.addEmployee(e3);
			
			logger.info("Employees saved!");
			
			//Create dumpsters
			Dumpster d1 = new Dumpster("d0", "Calle Nagusia 12", 111111, 25, 10, 200);
			Dumpster d2 = new Dumpster("d1", "Calle Iparragirre 5", 222222, 30, 15, 300);
			Dumpster d3 = new Dumpster("d2", "Calle Gernika 8", 333333, 20, 5, 150);
			Dumpster d4 = new Dumpster("d3", "Calle Bidebarrieta 3", 111111, 40, 20, 400);
			Dumpster d5 = new Dumpster("d4", "Calle Elcano 7", 222222, 35, 18, 350);
			Dumpster d6 = new Dumpster("d5", "Calle Lersundi 10", 111111, 28, 12, 250);
			
			ecoembesService.addDumpster(d1);
			ecoembesService.addDumpster(d2);
			ecoembesService.addDumpster(d3);
			ecoembesService.addDumpster(d4);
			ecoembesService.addDumpster(d5);
			ecoembesService.addDumpster(d6);
			
			logger.info("Dumpsters saved!");
			
			//Create recycling plants
			RecyclingPlant rp1 = new RecyclingPlant("p1", "PlasSB Ltd", 1000);
			RecyclingPlant rp2 = new RecyclingPlant("p2", "Cont Socket Ltd", 1500);
			
			ecoembesService.addRecyclingPlant(rp1);
			ecoembesService.addRecyclingPlant(rp2);
			
			logger.info("Recycling Plants saved!");
			
		};
	}

}

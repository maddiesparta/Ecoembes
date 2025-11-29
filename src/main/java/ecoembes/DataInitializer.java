package ecoembes;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.transaction.Transactional;
import ecoembes.dao.AllocationRepository;
import ecoembes.dao.DumpsterRepository;
import ecoembes.dao.EmployeeRepository;
import ecoembes.dao.RecyclingPlantRepository;
import ecoembes.dao.UsageRepository;
import ecoembes.entity.Dumpster;
import ecoembes.entity.Employee;
import ecoembes.entity.RecyclingPlant;


@Configuration
public class DataInitializer {
	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

	@Bean
	@Transactional
	CommandLineRunner initData(AllocationRepository allocationRepository, DumpsterRepository dumpsterRepository,
			EmployeeRepository employeeRepository, RecyclingPlantRepository recyclingPlantRepository,
			UsageRepository usageRepository) {
		return args -> {
			
			// Database is already initialized (revisar)
            if (dumpsterRepository.count() > 0) {                
                return;
            }
			
			//Create employees (sin id incluido)
			Employee e0 = new Employee("user", "user@gmail.com", "user");
			Employee e1 = new Employee("Mike", "mike@gmail.com", "M1k3P4ss!");
			Employee e2 = new Employee("Sara", "sara@sara.com", "S4r4P4ss!");
			Employee e3 = new Employee("John", "john@gmail.com", "J0hnP4ss!");
						
//			authService.addEmployee(e0);
//			authService.addEmployee(e1);
//			authService.addEmployee(e2);
//			authService.addEmployee(e3);
			
			// Save employees
			employeeRepository.saveAll(List.of(e0,e1,e2,e3));			
			logger.info("Employees saved!");
			
			//Create dumpsters
			Dumpster d1 = new Dumpster("Calle Nagusia 12", "111111", 25, 10);
			Dumpster d2 = new Dumpster("Calle Iparragirre 5", "222222", 30, 15);
			Dumpster d3 = new Dumpster("Calle Gernika 8", "333333", 20, 5);
			Dumpster d4 = new Dumpster("Calle Bidebarrieta 3", "111111", 40, 20);
			Dumpster d5 = new Dumpster("Calle Elcano 7", "222222", 35, 18);
			Dumpster d6 = new Dumpster("Calle Lersundi 10", "111111", 28, 12);
			
//			dumpsterService.addDumpster(d1);
//			dumpsterService.addDumpster(d2);
//			dumpsterService.addDumpster(d3);
//			dumpsterService.addDumpster(d4);
//			dumpsterService.addDumpster(d5);
//			dumpsterService.addDumpster(d6);
			
			// Save dumpsters
			dumpsterRepository.saveAll(List.of(d1,d2,d3,d4,d5,d6));			
			logger.info("Dumpsters saved!");
			
			//Create recycling plants
			RecyclingPlant rp1 = new RecyclingPlant("PlasSB Ltd", 100000);
			RecyclingPlant rp2 = new RecyclingPlant("Cont Socket Ltd", 150000);
			
//			ecoembesService.addRecyclingPlant(rp1);
//			ecoembesService.addRecyclingPlant(rp2);
			
			// Save recycling plants
			recyclingPlantRepository.saveAll(List.of(rp1,rp2));
			logger.info("Recycling Plants saved!");
			
		};
	}

}

package ecoembes.gateway;

import java.time.LocalDate;

public interface IGateway {
	
	String getCapacity(LocalDate date);
}

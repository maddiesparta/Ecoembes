package ecoembes.gateway;

import java.time.LocalDate;

public interface IGateway {
	
	float getCapacity(LocalDate date);
}

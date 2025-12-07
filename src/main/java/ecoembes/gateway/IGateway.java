package ecoembes.gateway;

public interface IGateway {
	
	float getCapacity();
	void sendAssignmentNotification(int dumpsters, int packages, float tons);
}

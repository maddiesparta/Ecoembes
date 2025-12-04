package ecoembes.gateway;

public interface IGateway {
	
	float getCapacity();
	void sendNotification(int dumpsters, int packages, float tons);
}

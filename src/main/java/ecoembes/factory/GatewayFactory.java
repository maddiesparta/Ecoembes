package ecoembes.factory;

import ecoembes.entity.LogInType;

import ecoembes.gateway.ContSocketGateway;
import ecoembes.gateway.PlasSBGateway;
import ecoembes.gateway.IGateway;


public class GatewayFactory {
	
	private static GatewayFactory instance;
	private GatewayFactory() {}
	
	public static GatewayFactory getInstance() {
		if(instance == null) {
			PlasSBGateway.start();
			instance = new GatewayFactory();
		}
		return instance;
	}
	
    public IGateway createGateway(LogInType type) {
        switch (type) {
            case PLASSB:
                return PlasSBGateway.getInstance();
            case CONTSOCKET:
                return ContSocketGateway.getInstance();
            default:
                return null;
        }
    }
}

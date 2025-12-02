package ecoembes.factory;

import org.springframework.stereotype.Service;
import ecoembes.entity.LogInType;
import ecoembes.gateway.ContSocketGateway;
import ecoembes.gateway.PlasSBGateway;
import ecoembes.gateway.IGateway;

@Service
public class GatewayFactory {

    private final PlasSBGateway plasSBGateway;
    private final ContSocketGateway contSocketGateway;

    public GatewayFactory(PlasSBGateway plasSBGateway, ContSocketGateway contSocketGateway) {
        this.plasSBGateway = plasSBGateway;
        this.contSocketGateway = contSocketGateway;
    }

    public IGateway createGateway(LogInType type) {
        switch (type) {
            case PLASSB:
                return plasSBGateway;
            case CONTSOCKET:
                return contSocketGateway;
            default:
                return null;
        }
    }
}

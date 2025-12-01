package ecoembes.gateway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ecoembes.dto.RecyclingPlantDTO;

@Service
public class ContSocketGateway implements IGateway {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${contsocket.host}")
    private String serverIP;

    @Value("${contsocket.port}")
    private int serverPort;

    public ContSocketGateway() {
    }

    @Override
    public float getCapacity(LocalDate date) {
        System.out.println("   - Checking ContSocket dumpster capacity for date: " + date);

        try {
        	
            String requestMessage = createCapacityRequest(date.toString());
            System.out.println(" requestMessage " );
            String jsonResponse = sendRequestAndGetResponse(requestMessage);
            System.out.println(" jsonResponse " );
            RecyclingPlantDTO plantDTO = objectMapper.readValue(jsonResponse, RecyclingPlantDTO.class);
            System.out.println(" plantDTO " );
            return receiveResponse(plantDTO, date.toString());
            

        } catch (Exception e) {
            System.err.println("# ContSocketGateway: Error - " + e.getMessage());
            return -1;
        }
    }

    private String createCapacityRequest(String date) throws Exception {
        java.util.Map<String, String> request = new java.util.HashMap<>();
        request.put("action", "GET_CAPACITY");
        request.put("date", date);
        request.put("plant", "ContSocket");
        return objectMapper.writeValueAsString(request);
    }

    private String sendRequestAndGetResponse(String requestData) {
        try (
            Socket tcpSocket = new Socket(serverIP, serverPort);
            DataInputStream in = new DataInputStream(tcpSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream())
        ) {
            out.writeUTF(requestData);
            System.out.println(" - ContSocketGateway: Sent request -> '" + requestData + "'");
            return in.readUTF();

        } catch (UnknownHostException e) {
            throw new RuntimeException("Socket error: " + e.getMessage(), e);
        } catch (EOFException e) {
            throw new RuntimeException("EOF error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO error: " + e.getMessage(), e);
        }
    }

    private float receiveResponse(RecyclingPlantDTO plantDTO, String requestedDate) {
        if (plantDTO == null) return -1;

        return plantDTO.getCurrent_capacity();
    }
}

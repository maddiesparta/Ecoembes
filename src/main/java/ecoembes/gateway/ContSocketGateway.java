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
    public String getCapacity(LocalDate date) {
        System.out.println("   - Checking ContSocket dumpster capacity for date: " + date);

        try {
            String requestMessage = createCapacityRequest(date.toString());
            String jsonResponse = sendRequestAndGetResponse(requestMessage);
            RecyclingPlantDTO plantDTO = objectMapper.readValue(jsonResponse, RecyclingPlantDTO.class);
            return receiveResponse(plantDTO, date.toString());

        } catch (Exception e) {
            System.err.println("# ContSocketGateway: Error - " + e.getMessage());
            return "Error retrieving capacity from ContSocket: " + e.getMessage();
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

    private String receiveResponse(RecyclingPlantDTO plantDTO, String requestedDate) {
        if (plantDTO == null) return "No valid response received from ContSocket";

        return String.format(
            " Plant: %s |  Capacity on %s: %.2f tons |  Total Capacity: %.2f tons",
            plantDTO.getPlant_name(),
            requestedDate,
            plantDTO.getCurrent_capacity(),
            plantDTO.getTotal_capacity()
        );
    }
}

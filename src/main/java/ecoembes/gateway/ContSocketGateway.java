package ecoembes.gateway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ecoembes.dto.RecyclingPlantDTO;

public class ContSocketGateway implements IGateway {
    
    private static final ContSocketGateway INSTANCE = new ContSocketGateway();
    private static final String SERVERIP = "127.0.0.1";
    private static final int SERVERPORT = 8001;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private ContSocketGateway() {}

    public static ContSocketGateway getInstance() {
        return INSTANCE;
    }

    @Override
    public String getCapacity(LocalDate date) {
        System.out.println("   - Checking ContSocket dumpster capacity for date: " + date);
        
        try {
            // Crear mensaje de solicitud con la fecha
            String requestMessage = createCapacityRequest(date.toString());
            
            // Enviar solicitud y recibir respuesta
            String jsonResponse = sendRequestAndGetResponse(requestMessage);
            
            // Procesar la respuesta JSON a DTO
            RecyclingPlantDTO plantDTO = objectMapper.readValue(jsonResponse, RecyclingPlantDTO.class);
            
            return receiveResponse(plantDTO, date.toString());
            
        } catch (Exception e) {
            System.err.println("# ContSocketGateway: Error - " + e.getMessage());
            return "Error retrieving capacity from ContSocket: " + e.getMessage();
        }
    }

    /**
     * Crea el mensaje de solicitud en formato JSON con la fecha
     */
    private String createCapacityRequest(String date) throws Exception {
        java.util.Map<String, String> request = new java.util.HashMap<>();
        request.put("action", "GET_CAPACITY");
        request.put("date", date);  // Fecha que viene desde Ecoembes
        request.put("plant", "ContSocket");
        
        return objectMapper.writeValueAsString(request);
    }

    /**
     * Envía la solicitud via Socket y recibe la respuesta
     */
    private String sendRequestAndGetResponse(String requestData) {
        try (
            Socket tcpSocket = new Socket(SERVERIP, SERVERPORT);
            DataInputStream in = new DataInputStream(tcpSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream())
        ) {
            // Enviar solicitud
            out.writeUTF(requestData);
            System.out.println(" - ContSocketGateway: Sent request to '" + 
                tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + 
                "' -> '" + requestData + "'");

            // Recibir respuesta
            String response = in.readUTF();
            System.out.println(" - ContSocketGateway: Received response from '" + 
                tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + 
                "' -> '" + response + "'");
            
            return response;
            
        } catch (UnknownHostException e) {
            throw new RuntimeException("Socket error: " + e.getMessage(), e);
        } catch (EOFException e) {
            throw new RuntimeException("EOF error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO error: " + e.getMessage(), e);
        }
    }

    /**
     * Procesa la respuesta y la formatea (incluyendo la fecha que se solicitó)
     */
    private String receiveResponse(RecyclingPlantDTO plantDTO, String requestedDate) {
        if (plantDTO == null) {
            return "No valid response received from ContSocket";
        }
        
        System.out.println("   - Processing ContSocket response for date: " + requestedDate);
        
        // Formatear la respuesta de manera legible incluyendo la fecha solicitada
        return String.format(
            "🏭 Plant: %s | 📊 Capacity on %s: %.2f tons | 📈 Total Capacity: %.2f tons",
            plantDTO.getPlant_name(),
            requestedDate,
            plantDTO.getCurrent_capacity(),
            plantDTO.getTotal_capacity()
        );
    }
}

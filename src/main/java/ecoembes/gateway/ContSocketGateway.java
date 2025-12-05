package ecoembes.gateway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ContSocketGateway implements IGateway {

    private static final Logger log = LoggerFactory.getLogger(ContSocketGateway.class);
    
    @Value("${contsocket.host}")
    private String serverIP;

    @Value("${contsocket.port}")
    private int serverPort;

    @Override
    public float getCapacity() {
       try {
	    	String request = "GET_CAPACITY"; 
	        String response = sendRequestAndGetResponse(request);
	
	        return Float.parseFloat(response.trim());
       }catch(NumberFormatException e) {
    	   log.error("Error parsing capacity response: {}", e.getMessage());
           return -1;
       }
    }

    private String sendRequestAndGetResponse(String requestData) {
        try (
        		Socket tcpSocket = new Socket(serverIP, serverPort);
                DataInputStream in = new DataInputStream(tcpSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream())
            ) {
                out.writeUTF(requestData);
                log.debug("ContSocketGateway sent: '{}'", requestData);
                String response = in.readUTF(); 
                log.debug("ContSocketGateway received: '{}'", response);
                return response;

        } catch (UnknownHostException e) {
            throw new RuntimeException("Socket error: " + e.getMessage(), e);
        } catch (EOFException e) {
            throw new RuntimeException("EOF error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO error: " + e.getMessage(), e);
        }
    }

	@Override
	public void sendNotification(int dumpsters, int packages, float tons) {
		//Sends notfication in the format SEND_NOTIFICATION:<dumpsters>:<packages>:<tons>
		try {
	        String response = sendRequestAndGetResponse("SEND_NOTIFICATION:"+String.valueOf(dumpsters)+":"+String.valueOf(packages)+":"+ String.valueOf(tons));
	        if(response.equals("OK")) {
	        	System.out.println("(ContSocket info sending) Everything went OK");
	        }else {
	        	System.out.println("(ContSocket info sending) Not everything went OK :(");
	        }
	        
       }catch(NumberFormatException e) {
    	   log.error("Error parsing capacity response: {}", e.getMessage());

       }
		
	}


}

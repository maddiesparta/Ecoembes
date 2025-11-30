package ecoembes.gateway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ContSocketGateway implements IGateway{
	
	private static final ContSocketGateway INSTANCE = new ContSocketGateway();
	private static final String SERVERIP = "127.0.0.1";
	private static final int SERVERPORT = 8001;
	
	private ContSocketGateway() {}

	public static ContSocketGateway getInstance() {
		return INSTANCE;
	}

	// TODO HAY QUE HACER ESTE METODO (Pillar RecPlantDTO igual) LO MISMO QUE PlasSBGateway
	@Override
	public String getCapacity() {
		System.out.println("   - Checking dumpster capacity \"");
		return receiveResponse();
	}

	private boolean receiveResponse(String data) {
		try (
				Socket tcpSocket = new Socket(SERVERIP, SERVERPORT);
				DataInputStream in = new DataInputStream(tcpSocket.getInputStream());
				DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream())) {

			out.writeUTF(data);
			System.out.println(" - TCPSocketClient: Sent data to '" + tcpSocket.getInetAddress().getHostAddress() + ":"
					+ tcpSocket.getPort() + "' -> '" + data + "'");

			String response = in.readUTF();
			System.out.println(" - TCPSocketClient: Received data from '" + tcpSocket.getInetAddress().getHostAddress()
					+ ":" + tcpSocket.getPort() + "' -> '" + response + "'");
			// TODO
			return Boolean.valueOf(response);
			
		} catch (UnknownHostException e) {
			System.err.println("# TCPSocketClient: Socket error: " + e.getMessage());
			return false;
		} catch (EOFException e) {
			System.err.println("# TCPSocketClient: EOF error: " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.err.println("# TCPSocketClient: IO error: " + e.getMessage());
			return false;
		}
	}

	
}

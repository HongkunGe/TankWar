package hongkun.tank;
import java.io.*;
import java.net.*;

public class TankClientNetAgent {
	
	private static int UDP_PORT = 2223;
	private ClientFrame clientFrame;
	
	public TankClientNetAgent(ClientFrame clientFrame) {
		this.clientFrame = clientFrame;
	}
	
	public void connect(String IP, int PORT) {
		Socket socket = null;
		try {
			// TCP connection
			socket = new Socket(IP, PORT);
			System.out.println("Connected to server!");
			
			// Send udpPort data to server.
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeInt(TankClientNetAgent.UDP_PORT);
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			int id = dis.readInt();
			clientFrame.tank1.id = id;
			
			socket.close();
			dos.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return the uDP_PORT
	 */
	public static int getUDP_PORT() {
		return UDP_PORT;
	}
}

package hongkun.tank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class TankServer {
	
	public static final int TCP_PORT = 6666;
	public static final int UDP_PORT = 8888;
	public static final int CLIENT_ID_START = 1;
	
	
	// store all clients' ip and udpPort for data distribution.
	private ArrayList<Client> clients = new ArrayList<Client>();
	private int id = CLIENT_ID_START;
	
	class Client {
		private String IPAdress;
		private int port;
		private int id;
		
		/**
		 * @return the iPAdress
		 */
		public String getIPAdress() {
			return IPAdress;
		}
		
		/**
		 * @return the port
		 */
		public int getPort() {
			return port;
		}
	
		/**
		 * @param iPAdress
		 * @param port
		 */
		public Client(String iPAdress, int port, int id) {
			this.IPAdress = iPAdress;
			this.port = port;
			this.id = id;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}
	}

	public void start() {
		// TCP listener
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(TCP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Socket socket = null;
		try {
			while(true) {
				socket = serverSocket.accept();
				
				// collect all udpPort data of clients.
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				int udpPort = dis.readInt();
				String ipAddress = socket.getInetAddress().getHostAddress();
				clients.add(new Client(ipAddress, udpPort, id));
				
				//Send back the id to client.
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeInt(id++);
				socket.close();
				
System.out.println("Connected! Address: " + socket.getInetAddress() + ":" + socket.getPort() + "----HOST Address: " + ipAddress + ":" + udpPort);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			if(serverSocket != null && !serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		// UDP listener
	}
	
	public static void main(String[] args) {
		new TankServer().start();
	}

	
}

package hongkun.tank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class TankServer {
	
	public static final int TCP_PORT = 6666;
	public static final int UDP_PORT = 8888;
	public static final int CLIENT_ID_START = 1;
	
	
	// store all clients' ip and udpPort for data distribution.
	private HashMap<Integer, Client> clients = new HashMap<Integer, Client>();
	private int id = CLIENT_ID_START;
		
	public class UDPThread implements Runnable {

		@Override
		public void run() {
			byte[] buf = new byte[1024]; // 1k
			DatagramSocket ds = null;
			DatagramPacket dpToAddNewClients = new DatagramPacket(buf, buf.length);
			
			byte[] bufToAddOldClients = new byte[1024];
			DatagramPacket dpToAddOldClients = new DatagramPacket(bufToAddOldClients, bufToAddOldClients.length);
			try {
				//If set InetAddress.getLocalHost() here, an exception will be raised.  Cannot assign requested address: Datagram send failed.
				ds = new DatagramSocket(TankServer.UDP_PORT);
System.out.println("UDP Thread start in Client on port " + TankServer.UDP_PORT);
				while(true) {
					
					/**
					 * A new client is added.
					 */
					ds.receive(dpToAddNewClients);
					MessageInfo messageInfo = TankMessage.decodeMessageTop(buf);
					int newlyAddedClientID = messageInfo.idReceived;
System.out.println("A packet received from Tank Client#" + newlyAddedClientID + " message type: " + TankMessage.printMessageType(messageInfo.messageType));
					

					for(HashMap.Entry<Integer,TankServer.Client> client: clients.entrySet()) {
						if(newlyAddedClientID != client.getKey()) {
							
							/*
							 * messageInfo.messageType == TankMessage.TANK_NEWMESSAGE || 
							 * messageInfo.messageType == TankMessage.TANK_KEYPRESSEDMESSAGE || 
							 * messageInfo.messageType == TankMessage.TANK_KEYRELEASEDDMESSAGE
							 * The messages are distributed to all other clients.
							 * */
							dpToAddNewClients.setSocketAddress(new InetSocketAddress(client.getValue().getIPAdress(), client.getValue().getPort()));
							ds.send(dpToAddNewClients);
System.out.println("A packet sent to Tank Client#" + client.getKey() + " messageInfo.messageType = " + messageInfo.messageType);
							
							if(messageInfo.messageType == TankMessage.TANK_NEWMESSAGE){
								// Collect: Wait for the reply of already existing clients, then notify newly added clients about the info of old clients.
								// don't need to care about the address when receiving a datagramPacket.
								ds.receive(dpToAddOldClients);
								
								MessageInfo m1 = TankMessage.decodeMessageTop(buf);
System.out.println("A packet received from Tank Client#" + m1.idReceived + " message type: " + TankMessage.printMessageType(m1.messageType));
								
								// Notify newly added clients about the info of old clients.
								Client newClient = clients.get(newlyAddedClientID);
								dpToAddOldClients.setSocketAddress(new InetSocketAddress(newClient.getIPAdress(), newClient.getPort()));
								ds.send(dpToAddOldClients);
							} 
						}
					}
				}
			} catch (SocketException | UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void start() {
		
		// UDP listener
		UDPThread udpThread = new UDPThread();
		new Thread(udpThread).start();
		
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
				clients.put(id, new Client(ipAddress, udpPort, id));
				
				//Send back the id to client.
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeInt(id++);
				
System.out.println("#" + (id - 1) + " Connected! TCP Address: " + socket.getInetAddress() + ":" + socket.getPort() + "----UDP HOST Address: " + ipAddress + ":" + udpPort);
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
	}
	
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

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Client [IPAdress=" + IPAdress + ", port=" + port + ", id=" + id + "]";
		}
	}

	public static void main(String[] args) {
		new TankServer().start();
	}
	
}

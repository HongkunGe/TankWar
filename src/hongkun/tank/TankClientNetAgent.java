package hongkun.tank;
import java.io.*;
import java.net.*;

public class TankClientNetAgent {
	
	private static int UDP_PORT = 2225;
	private ClientFrame clientFrame;
	
	/*
	 * The datagramSocket should be initialized only one time. Or an "can not bind" exception will be raised.
	 * */
	DatagramSocket datagramSocket = null;
	
	public TankClientNetAgent(ClientFrame clientFrame) {
		this.clientFrame = clientFrame;
		
		//If set InetAddress.getLocalHost() here, Package cannot be received. 
		try {
			datagramSocket = new DatagramSocket(TankClientNetAgent.UDP_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void connect(String IP, int PORT) {		
		
		Socket socket = null;
		try {
			// TCP connection
			socket = new Socket(IP, PORT);
System.out.println("TCP Connected to server! Server IP Address: " + IP + " Port: " + PORT);
			
			// Send udpPort data to server. Now Server knows 
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeInt(TankClientNetAgent.UDP_PORT);
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			int id = dis.readInt();
			clientFrame.tank1.id = id;
System.out.println("Server Replied and Gave me an ID: " + id);
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
		
		/* When TCP connection is finished, we send the tank creation data to server, 
		 *   Using UDP. 
		 */
		TankNewMessage msg = new TankNewMessage(clientFrame.tank1, TankMessage.TANK_NEWMESSAGE);
		send(msg);
		
		
		/* UDP Listener
		 * New a Tank;
		 * */
		UDPThread udpThread = new UDPThread();
		new Thread(udpThread).start();
	}

	public class UDPThread implements Runnable {

		@Override
		public void run() {
			byte[] buf = new byte[1024]; // 1k
			
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			try {
System.out.println("UDP Thread start in Client on port " + TankClientNetAgent.UDP_PORT);
				while(true) {
					datagramSocket.receive(dp);

					ByteArrayInputStream bais = new ByteArrayInputStream(buf);
					DataInputStream dis = new DataInputStream(bais);
					
					TankMessage msg = null;
					msg = new TankNewMessage(clientFrame.tank1, TankMessage.TANK_MESSAGE_DECODE);
					msg.decode(dis); // messageType will be decoded in this step.					
					
					if(msg.messageType == TankMessage.TANK_NEWMESSAGE) {
						/* Every time the client received a newTank message from server, which means the client should add the new tank to the frame
						 * the client will send an "alreadyExist" message back to the server and then the message will be transfered to newly added
						 * client by server.
						 * */
						TankByHuman newTankByHumanOnline = msg.tank;
						clientFrame.tanksByHumanOnline.add(newTankByHumanOnline);
System.out.println("A packet received from Tank Server to New a Tank#" + newTankByHumanOnline.id);
						TankNewMessage msgAlready = new TankNewMessage(clientFrame.tank1, TankMessage.TANK_ALREADYMESSAGE);
						send(msgAlready);
						
					} else if(msg.messageType == TankMessage.TANK_ALREADYMESSAGE) {
						TankByHuman newTankByHumanOnline = msg.tank;
						clientFrame.tanksByHumanOnline.add(newTankByHumanOnline);
System.out.println("A packet received from Tank Server to Add an old Tank#" + newTankByHumanOnline.id);	

					} else if(msg.messageType == TankMessage.TANK_KEYEVENTMESSAGE) {
						
					}

					
					
				}
			} catch (SocketException | UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void send(TankMessage msg) {
		msg.send(datagramSocket, "127.0.0.1", TankServer.UDP_PORT);
	}

	/**
	 * @return the uDP_PORT
	 */
	public static int getUDP_PORT() {
		return UDP_PORT;
	}
}

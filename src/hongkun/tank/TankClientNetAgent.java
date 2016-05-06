package hongkun.tank;
import java.io.*;
import java.net.*;

public class TankClientNetAgent {
	
	public String serverIp;
	private static int UDP_PORT = 2229;
	private ClientFrame clientFrame;
	
	/*
	 * The datagramSocket should be initialized only one time. Or an "can not bind" exception will be raised.
	 * */
	DatagramSocket datagramSocket = null;
	
	public TankClientNetAgent(ClientFrame clientFrame) {
		this.clientFrame = clientFrame;
	}
	
	public void connect(String IP, int PORT) {		
		
		this.serverIp = IP;
		//If set InetAddress.getLocalHost() here, Package cannot be received. 
		try {
			datagramSocket = new DatagramSocket(TankClientNetAgent.UDP_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
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
			clientFrame.tank0.id = id;
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
		TankNewMessage msg = new TankNewMessage(clientFrame.tank0, TankMessage.TANK_NEWMESSAGE);
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
					
					MessageInfo messageInfo = TankMessage.decodeMessageTop(buf); // messageType will be decoded in this step.
String printID = "Client#" + clientFrame.tank0.id + ": ";
					TankMessage msg = null;
					if(messageInfo.messageType == TankMessage.TANK_NEWMESSAGE || messageInfo.messageType == TankMessage.TANK_ALREADYMESSAGE) {
						msg = new TankNewMessage(clientFrame.tank0, TankMessage.TANK_MESSAGE_DECODE);
						msg.decode(dis);
						TankByHuman newTankByHumanOnline = msg.tank;
						
						clientFrame.tanksByHumanOnline.put(newTankByHumanOnline.id, newTankByHumanOnline);
						clientFrame.teamStatistics(newTankByHumanOnline.role, 1);
						
						if(messageInfo.messageType == TankMessage.TANK_NEWMESSAGE) {
							/* Every time the client received a newTank message from server, which means the client should add the new tank to the frame
							 * the client will send an "alreadyExist" message back to the server and then the message will be transfered to newly added
							 * client by server.
							 * */	
							newTankByHumanOnline.setInitialLocationDirection(0, 0);
System.out.println(printID + "A packet received from Tank Server to Add a new Tank from Client#" + newTankByHumanOnline.id);
							TankNewMessage msgAlready = new TankNewMessage(clientFrame.tank0, TankMessage.TANK_ALREADYMESSAGE);
							send(msgAlready);
						
						} else { 
							/*
							 * messageInfo.messageType == TankMessage.TANK_ALREADYMESSAGE, we only need to add the old tank to this newly added client.
							 * */
							clientFrame.tanksByHumanOnline.put(msg.tank.id, newTankByHumanOnline);
System.out.println(printID + "A packet received from Tank Server to Add an old Tank from Client#" + newTankByHumanOnline.id);	
	
						} 
						
					} else if(messageInfo.messageType == TankMessage.TANK_KEYPRESSEDMESSAGE) {
						msg = new TankKeyEventMessage(clientFrame.tank0, TankMessage.TANK_MESSAGE_DECODE);
						msg.decode(dis);
						TankByHuman newTankByHumanOnline = clientFrame.tanksByHumanOnline.get(msg.tank.id);
System.out.println(printID + " keyPressedEventCode Received------" + msg.tank.keyPressedCode + " from Client#" + msg.tank.id);
						newTankByHumanOnline.onlineKeyPressed(msg.tank.keyPressedCode);
						newTankByHumanOnline.setXY(msg.tank.x, msg.tank.y, msg.tank.xDir, msg.tank.yDir);
						
					} else if(messageInfo.messageType == TankMessage.TANK_KEYRELEASEDMESSAGE) {
						msg = new TankKeyEventMessage(clientFrame.tank0, TankMessage.TANK_MESSAGE_DECODE);
						msg.decode(dis);
						TankByHuman newTankByHumanOnline = clientFrame.tanksByHumanOnline.get(msg.tank.id);
System.out.println(printID + " keyReleasedEventCode Received------" + msg.tank.keyReleasedCode + " from Client#" + msg.tank.id);
						newTankByHumanOnline.onlineKeyReleased(msg.tank.keyReleasedCode);

					} else if(messageInfo.messageType == TankMessage.TANK_QUITMESSAGE) {
System.out.println(printID + "A packet received from Tank Server to Remove an old Tank of Client#" + messageInfo.idReceived);

						if(clientFrame.tanksByHumanOnline.containsKey(messageInfo.idReceived)) {
							clientFrame.teamStatistics(clientFrame.tanksByHumanOnline.get(messageInfo.idReceived).role, -1);
							clientFrame.tanksByHumanOnline.remove(messageInfo.idReceived);
System.out.println(printID + "The Tank has been removed. Client#" + messageInfo.idReceived);
						} else {
System.out.println(printID + "Error removing the tank that doesn't exist. Client#" + messageInfo.idReceived);	
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
	
	public void send(TankMessage msg) {
		msg.send(datagramSocket, this.serverIp, TankServer.UDP_PORT);
	}

	/**
	 * @return the uDP_PORT
	 */
	public static int getUDP_PORT() {
		return UDP_PORT;
	}

	/**
	 * @param uDP_PORT the uDP_PORT to set
	 */
	public static void setUDP_PORT(int uDP_PORT) {
		UDP_PORT = uDP_PORT;
	}
}

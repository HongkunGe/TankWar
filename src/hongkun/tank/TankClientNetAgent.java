package hongkun.tank;
import java.io.*;
import java.net.*;

public class TankClientNetAgent {
	
	private static int UDP_PORT = 2225;
	private ClientFrame clientFrame;
	
	public TankClientNetAgent(ClientFrame clientFrame) {
		this.clientFrame = clientFrame;
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
		TankNewMessage msg = new TankNewMessage(clientFrame.tank1);
		send(msg);
		
		
		/* UDP Listener
		 * New a Tank;
		 *  */
		UDPThread udpThread = new UDPThread();
		new Thread(udpThread).start();
	}

	public class UDPThread implements Runnable {

		@Override
		public void run() {
			byte[] buf = new byte[1024]; // 1k
			DatagramSocket ds = null;
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			try {
				//If set InetAddress.getLocalHost() here, Package cannot be received. 
				ds = new DatagramSocket(TankClientNetAgent.UDP_PORT);
System.out.println("UDP Thread start in Client on port " + TankClientNetAgent.UDP_PORT);
				while(true) {
					ds.receive(dp);

					ByteArrayInputStream bais = new ByteArrayInputStream(buf);
					DataInputStream dis = new DataInputStream(bais);

					TankNewMessage msg = new TankNewMessage(clientFrame.tank1);
					TankByHuman newTankByHumanOnline = msg.decodeAndNewTank(dis);
System.out.println("A packet received from TankServer to New a Tank#" + newTankByHumanOnline.id);
					clientFrame.tanksByHumanOnline.add(newTankByHumanOnline);
					
					/* Every time the client received a newTank message from server, which means the client should add the new tank to the frame
					 * the client will send an "alreadyExist" message back to the server and then transfered to newly added client.
					 * */
					
					
					
				}
			} catch (SocketException | UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void send(TankMessage msg) {
		msg.send("127.0.0.1", TankServer.UDP_PORT);
	}

	/**
	 * @return the uDP_PORT
	 */
	public static int getUDP_PORT() {
		return UDP_PORT;
	}
}

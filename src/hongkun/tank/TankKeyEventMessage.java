package hongkun.tank;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TankKeyEventMessage extends TankMessage{

	@Override
	public void send(DatagramSocket datagramSocket, String IP, int port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		// encode the sending data of a new tank.
		try {
			encode(dos);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		byte[] sentTankData = baos.toByteArray();
		DatagramPacket dp = null;
		
		try {
			dp = new DatagramPacket(sentTankData, sentTankData.length,
			        new InetSocketAddress(InetAddress.getLocalHost(), TankServer.UDP_PORT));
			
			datagramSocket.send(dp);
System.out.println("Client#" +  + tank.id + " From Port: " + TankClientNetAgent.getUDP_PORT() + " A packet sent to server");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
//		finally {
//			if(datagramSocket != null && !datagramSocket.isClosed()) {
//				datagramSocket.close();
//			}
//		}
	}

	@Override
	public void encode(DataOutputStream dos) throws IOException {
		dos.writeInt(tank.id);
		dos.writeInt(tank.x);
		dos.writeInt(tank.y);
		dos.writeInt(tank.xDir);
		dos.writeInt(tank.yDir);
		dos.writeInt(tank.xBarrelDirection);
		dos.writeInt(tank.yBarrelDirection);
		dos.writeInt(messageType);
	}

	@Override
	public void decode(DataInputStream dis) throws IOException {
		tank.id = dis.readInt();
		tank.x = dis.readInt();
		tank.y = dis.readInt();
		tank.xDir = dis.readInt();
		tank.yDir = dis.readInt();
		tank.xBarrelDirection = dis.readInt();
		tank.yBarrelDirection = dis.readInt();
		messageType = dis.readInt();
	}

	public TankKeyEventMessage(TankByHuman tank, int messageType) {
		super(tank, messageType);
	}
}

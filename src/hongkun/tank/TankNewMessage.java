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

public class TankNewMessage implements TankMessage{
	TankByHuman tank;

	/**
	 * @param tank
	 */
	public TankNewMessage(TankByHuman tank) {
		this.tank = new TankByHuman(tank);
	}

	public void send(String udpIP, int udpPort) {
		
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
		DatagramSocket ds = null;
		try {
			dp = new DatagramPacket(sentTankData, sentTankData.length,
			        new InetSocketAddress(InetAddress.getLocalHost(), TankServer.UDP_PORT));
			ds = new DatagramSocket(TankClientNetAgent.getUDP_PORT());
			ds.send(dp);
System.out.println("From Port: " + TankClientNetAgent.getUDP_PORT() + " A packet sent to server#" + tank.id);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(ds != null && !ds.isClosed()) {
				ds.close();
			}
		}
	}
	
	public void encode(DataOutputStream dos) throws IOException{
		dos.writeInt(tank.id);
		dos.writeInt(tank.x);
		dos.writeInt(tank.y);
		dos.writeInt(tank.xDir);
		dos.writeInt(tank.yDir);
		dos.writeInt(tank.life);
		dos.writeInt(tank.xBarrelDirection);
		dos.writeInt(tank.yBarrelDirection);
		dos.writeBoolean(tank.role);
		dos.writeBoolean(tank.isLive);
	}
	
	public void decode(DataInputStream dis) throws IOException{
		tank.id = dis.readInt();
		tank.x = dis.readInt();
		tank.y = dis.readInt();
		tank.xDir = dis.readInt();
		tank.yDir = dis.readInt();
		tank.life = dis.readInt();
		tank.xBarrelDirection = dis.readInt();
		tank.yBarrelDirection = dis.readInt();
		tank.role = dis.readBoolean();
		tank.isLive = dis.readBoolean();
	}
	
	public TankByHuman decodeAndNewTank(DataInputStream dis) throws IOException{
		decode(dis);
		return this.tank;
	}
}

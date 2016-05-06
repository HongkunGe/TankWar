package hongkun.tank;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

//import hongkun.tank.TankServer.MessageInfo;

public abstract class TankMessage {
	public static final int TANK_MESSAGE_DECODE = 0;
	public static final int TANK_NEWMESSAGE = 1;
	public static final int TANK_ALREADYMESSAGE = 2;
	public static final int TANK_KEYPRESSEDMESSAGE = 3;
	public static final int TANK_KEYRELEASEDMESSAGE = 4;
	public static final int TANK_QUITMESSAGE = 5;
	
	TankByHuman tank;
	int messageType;
	
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
			        new InetSocketAddress(IP, TankServer.UDP_PORT));
			
			datagramSocket.send(dp);
System.out.println("Client#" +  + tank.id + " :From Port " + TankClientNetAgent.getUDP_PORT() + ", A packet sent to server");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void encode(DataOutputStream dos) throws IOException;
	public abstract void decode(DataInputStream dis) throws IOException;
	
	/**
	 * @param tank
	 * @param messageType
	 */
	public TankMessage(TankByHuman tank, int messageType){
		this.tank = new TankByHuman(tank);
		this.messageType = messageType;
	}
	
	public static MessageInfo decodeMessageTop(byte[] buf) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		DataInputStream dis = new DataInputStream(bais);
		int idReceived = dis.readInt();
		int messageType = dis.readInt();
		dis.close();
		bais.close();
		return new MessageInfo(idReceived, messageType);
	}
	
	public static String printMessageType(int mt) {
		String printtedType = "";
		switch(mt) {
		case TANK_MESSAGE_DECODE:
			printtedType =  "TANK_MESSAGE_DECODE";
			break;
		case TANK_NEWMESSAGE:
			printtedType =  "TANK_NEWMESSAGE";
			break;
		case TANK_ALREADYMESSAGE:
			printtedType =  "TANK_ALREADYMESSAGE";
			break;
		case TANK_KEYPRESSEDMESSAGE:
			printtedType =  "TANK_KEYPRESSEDMESSAGE";
			break;
		case TANK_KEYRELEASEDMESSAGE:
			printtedType =  "TANK_KEYRELEASEDDMESSAGE";
			break;
		case TANK_QUITMESSAGE:
			printtedType =  "TANK_QUITMESSAGE";
			break;
		}
		return printtedType.toLowerCase();
	}
}

class MessageInfo {
	public int idReceived;
	public int messageType;
	
	public MessageInfo(int idReceived, int messageType) {
		this.idReceived = idReceived;
		this.messageType = messageType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageInfo [idReceived=" + idReceived + ", messageType=" + messageType + "]";
	}
}
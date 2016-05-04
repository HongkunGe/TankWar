package hongkun.tank;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;

//import hongkun.tank.TankServer.MessageInfo;

public abstract class TankMessage {
	public static final int TANK_MESSAGE_DECODE = 0;
	public static final int TANK_NEWMESSAGE = 1;
	public static final int TANK_ALREADYMESSAGE = 2;
	public static final int TANK_KEYEVENTMESSAGE = 3;
	
	TankByHuman tank;
	int messageType;
	
	public abstract void send(DatagramSocket datagramSocket, String IP, int port);
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
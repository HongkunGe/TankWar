package hongkun.tank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;

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
	
}

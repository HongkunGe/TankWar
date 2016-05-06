package hongkun.tank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * TankEventMessage is to send the event including ramByOtherTank and hitByMissle 
 */
public class TankEventMessage extends TankMessage {

	public TankEventMessage(TankByHuman tank, int messageType) {
		super(tank, messageType);
	}
	
	@Override
	public void encode(DataOutputStream dos) throws IOException {
		dos.writeInt(tank.id);
		dos.writeInt(messageType);
		dos.writeInt(tank.x);
		dos.writeInt(tank.y);
		dos.writeInt(tank.life);
		dos.writeBoolean(tank.role);
//		dos.writeBoolean(tank.isLive);	
	}

	@Override
	public void decode(DataInputStream dis) throws IOException {
		tank.id = dis.readInt();
		messageType = dis.readInt();
		tank.x = dis.readInt();
		tank.y = dis.readInt();
		tank.life = dis.readInt();
		tank.role = dis.readBoolean();
//		tank.isLive = dis.readBoolean();
	}

}

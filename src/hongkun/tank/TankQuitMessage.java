package hongkun.tank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * TankEventMessage is to send the event including ramByOtherTank and hitByMissle 
 */
public class TankQuitMessage extends TankMessage {

	public TankQuitMessage(TankByHuman tank, int messageType) {
		super(tank, messageType);
	}
	
	@Override
	public void encode(DataOutputStream dos) throws IOException {
		dos.writeInt(tank.id);
		dos.writeInt(messageType);
	}

	@Override
	public void decode(DataInputStream dis) throws IOException {
		tank.id = dis.readInt();
		messageType = dis.readInt();
	}

}

package hongkun.tank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TankKeyEventMessage extends TankMessage{

	@Override
	public void encode(DataOutputStream dos) throws IOException {
		dos.writeInt(tank.id);
		dos.writeInt(messageType);
		if(messageType == TANK_KEYPRESSEDMESSAGE) {
			dos.writeInt(tank.keyPressedCode);
			dos.writeInt(tank.x);
			dos.writeInt(tank.y);
			dos.writeInt(tank.xDir);
			dos.writeInt(tank.yDir);
		} else if(messageType == TANK_KEYRELEASEDMESSAGE) {
			dos.writeInt(tank.keyReleasedCode);
		}
	}

	@Override
	public void decode(DataInputStream dis) throws IOException {
		tank.id = dis.readInt();
		messageType = dis.readInt();
		if(messageType == TANK_KEYPRESSEDMESSAGE) {
			tank.keyPressedCode = dis.readInt();
			tank.x = dis.readInt();
			tank.y = dis.readInt();
			tank.xDir = dis.readInt();
			tank.yDir = dis.readInt();
		} else if(messageType == TANK_KEYRELEASEDMESSAGE) {
			tank.keyReleasedCode = dis.readInt();
		}
	}

	public TankKeyEventMessage(TankByHuman tank, int messageType) {
		super(tank, messageType);
	}
}

package hongkun.tank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TankNewMessage extends TankMessage{
	
	public void encode(DataOutputStream dos) throws IOException{
		dos.writeInt(tank.id);
		dos.writeInt(messageType);
		dos.writeInt(tank.x);
		dos.writeInt(tank.y);
		dos.writeInt(tank.xDir);
		dos.writeInt(tank.yDir);
		dos.writeInt(tank.life);
		dos.writeInt(tank.xBarrelDirection);
		dos.writeInt(tank.yBarrelDirection);
		dos.writeBoolean(tank.role);
//		dos.writeBoolean(tank.isLive);		
	}
	
	public void decode(DataInputStream dis) throws IOException{
		tank.id = dis.readInt();
		messageType = dis.readInt();
		tank.x = dis.readInt();
		tank.y = dis.readInt();
		tank.xDir = dis.readInt();
		tank.yDir = dis.readInt();
		tank.life = dis.readInt();
		tank.xBarrelDirection = dis.readInt();
		tank.yBarrelDirection = dis.readInt();
		tank.role = dis.readBoolean();
//		tank.isLive = dis.readBoolean();		
	}
	
	public TankByHuman decodeAndNewTank(DataInputStream dis) throws IOException{
		decode(dis);
		return this.tank;
	}

	/**
	 * @param tank
	 * @param messageType
	 */
	public TankNewMessage(TankByHuman tank, int messageType) {
		super(tank, messageType);
	}
}

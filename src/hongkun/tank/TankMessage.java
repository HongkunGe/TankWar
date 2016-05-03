package hongkun.tank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface TankMessage {
	public static final int TANK_NEWMESSAGE = 1;
	
	public void send(String IP, int port);
	public void encode(DataOutputStream dos) throws IOException;
	public void decode(DataInputStream dis) throws IOException;
	
}

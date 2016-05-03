package hongkun.tank;

public class TankAlreadyExistMessage {
	TankByHuman tank;

	/**
	 * @param tank
	 */
	public TankAlreadyExistMessage(TankByHuman tank) {
		this.tank = tank;
	}
	
	public void send(String udpIP, int udpPort) {
		
	}
}

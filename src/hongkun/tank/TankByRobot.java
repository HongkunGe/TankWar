package hongkun.tank;
import java.awt.Color;
import java.awt.Graphics;

public class TankByRobot extends Tank{
	
	private final int STEPS_TOTAL = 20;
	private int stepCount = 0;
	public TankByRobot(int x, int y, int width, int height, boolean role, Color c, int life, ClientFrame clientFrame) {
		super(x, y, width, height, role, c, life, clientFrame);
	}
	
	public void getRandomTankDir() {
		int tankDirIndex = TankMath.generateRandom(0, 4);
		
		if(tankDirIndex < 4) {
			Direction dir = DirectionGenerator.getDirection(tankDirIndex * 2 + 1, TANK_STEP);
			xDir = dir.x;
			yDir = dir.y;
		}
	}
	
	public void getRandomMissle() {
		Missle firedMissle = null;
		int missleDirIndex = TankMath.generateRandom(0, 9);
		if(missleDirIndex < 9) {
			Direction dir = DirectionGenerator.getDirection(missleDirIndex, MISSLE_STEP);
			firedMissle = fire(dir.x, dir.y);
		}
		if( firedMissle != null) {
			barrel.add(firedMissle);
		} else {
			this.xBarrelDirection = (int) Math.signum(xDir) * Tank.TANK_WIDTH / 2;
			this.yBarrelDirection = (int) Math.signum(yDir) * Tank.TANK_HEIGHT / 2;
		}
	}
	
	public void draw(Graphics g) {
		if(stepCount % STEPS_TOTAL == 0) {
			getRandomTankDir();
			stepCount = 0;
		}
		
		if(stepCount % (STEPS_TOTAL / 2) == 0) {
			getRandomMissle();
		}
		stepCount ++;
		super.draw(g);
	}
}

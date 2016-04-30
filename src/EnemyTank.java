import java.awt.Color;
import java.awt.Graphics;

public class EnemyTank extends Tank{
	
	private final int STEPS_TOTAL = 20;
	private int stepCount = 0;
	public EnemyTank(int x, int y, int width, int height, Color c, ClientFrame clientFrame) {
		super(x, y, width, height, false, c, clientFrame);
	}
	
	public void getDir() {
		Missle firedMissle = null;
		int tankDirIndex = generateRandom(0, 4);
		int missleDirIndex = generateRandom(0, 8);
		
		if(tankDirIndex < 4) {
			xDir = Tank.tankDir.get(tankDirIndex).x;
			yDir = Tank.tankDir.get(tankDirIndex).y;
		}
		if(missleDirIndex < 8) {
			firedMissle = fire(missleDir.get(missleDirIndex).x, missleDir.get(missleDirIndex).y);
		}
		if( firedMissle != null) {
			barrel.add(firedMissle);
		} else {
			this.xBarrelDirection = (int) Math.signum(xDir) * FriendTank.TANK_WIDTH / 2;
			this.yBarrelDirection = (int) Math.signum(yDir) * FriendTank.TANK_HEIGHT / 2;
		}
	}
	
	// Generate the random number [0, 7]
	public static int generateRandom(int min, int max) {
		int randomNum = (int)(Math.random() * ((max - min) + 1) + min);
		return randomNum;
	}
	
	public void draw(Graphics g) {
		if(stepCount % STEPS_TOTAL == 0) {
			getDir();
			stepCount = 0;
		}
		stepCount ++;
		super.draw(g);
	}
}

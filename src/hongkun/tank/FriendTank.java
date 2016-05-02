package hongkun.tank;

import java.awt.*;
import java.awt.event.*;

public class FriendTank extends Tank{
	
	public FriendTank(int x, int y, int width, int height, boolean role, Color c, int life, ClientFrame clientFrame) {
		super(x, y, width, height, role, c, life, clientFrame);
	}
	
	public FriendTank(int x, int y, int width, int height, boolean role, Color c, int life) {
		super(x, y, width, height, role, c, life);
	}

	public void keyPressed(KeyEvent e) {
		int direction = e.getKeyCode();
		Missle firedMissle = null;
		Direction dir = null;
		boolean isFireMissle = false;
		
		if(direction == KeyEvent.VK_UP) {
			dir = DirectionGenerator.getDirection(Compass.U, TANK_STEP);
		} else if(direction == KeyEvent.VK_DOWN) {
			dir = DirectionGenerator.getDirection(Compass.D, TANK_STEP);
		} else if(direction == KeyEvent.VK_LEFT) {
			dir = DirectionGenerator.getDirection(Compass.L, TANK_STEP);
		} else if(direction == KeyEvent.VK_RIGHT) {
			dir = DirectionGenerator.getDirection(Compass.R, TANK_STEP);
		} else if(direction == KeyEvent.VK_F2){
			// restart
			if(!this.isLive()) {
				this.isLive = true;
				this.life = 100;
			}
			return;
		} else if(direction == KeyEvent.VK_NUMPAD5) {
			superFire();
			return;
		} else if(KeyEvent.VK_NUMPAD1 <= direction && direction <= KeyEvent.VK_NUMPAD9){
			isFireMissle = true;
			dir = DirectionGenerator.getDirection(direction - KeyEvent.VK_NUMPAD1, MISSLE_STEP);
		} else {
			return;
		}
		
		if(isFireMissle) {
			firedMissle = fire(dir.x, dir.y);
			barrel.add(firedMissle);
		} else {
			xDir = dir.x;
			yDir = dir.y;
			this.xBarrelDirection = (int) Math.signum(xDir) * FriendTank.TANK_WIDTH / 2;
			this.yBarrelDirection = (int) Math.signum(yDir) * FriendTank.TANK_HEIGHT / 2;
		}
	}
	public void superFire() {
		for(int i = 1; i < 10; i++) {
			if(i == 5) 
				continue;
			Direction dir = DirectionGenerator.getDirection(i - 1, MISSLE_STEP);
			this.barrel.add(fire(dir.x, dir.y));
		}
	}
	public void keyReleased(KeyEvent e) {
		int direction = e.getKeyCode();

		if(direction == KeyEvent.VK_UP || direction == KeyEvent.VK_DOWN || direction == KeyEvent.VK_LEFT || direction == KeyEvent.VK_RIGHT) {
			xDir = yDir = 0;
		}		
	}
}

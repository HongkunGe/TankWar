
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class FriendTank extends Tank{
	
	public FriendTank(int x, int y, int width, int height, boolean role, Color c, ClientFrame clientFrame) {
		super(x, y, width, height, role, c, clientFrame);
	}
	
	public FriendTank(int x, int y, int width, int height, boolean role, Color c) {
		super(x, y, width, height, role, c);
	}

	public void keyPressed(KeyEvent e) {
		int direction = e.getKeyCode();
		Missle firedMissle = null;

		if(direction == KeyEvent.VK_UP) {
			xDir = 0;
			yDir = -TANK_STEP;
		} else if(direction == KeyEvent.VK_DOWN) {
			xDir = 0;
			yDir = TANK_STEP;
		} else if(direction == KeyEvent.VK_LEFT) {
			xDir = -TANK_STEP;
			yDir = 0;
		} else if(direction == KeyEvent.VK_RIGHT) {
			xDir = TANK_STEP;
			yDir = 0;
		} else if(direction == KeyEvent.VK_NUMPAD1) {
			firedMissle = fire(-MISSLE_STEP, MISSLE_STEP);
		} else if(direction == KeyEvent.VK_NUMPAD2) {
			firedMissle = fire(0, MISSLE_STEP);
		} else if(direction == KeyEvent.VK_NUMPAD3) {
			firedMissle = fire(MISSLE_STEP, MISSLE_STEP);
		} else if(direction == KeyEvent.VK_NUMPAD4) {
			firedMissle = fire(-MISSLE_STEP, 0);
		} else if(direction == KeyEvent.VK_NUMPAD6) {
			firedMissle = fire(MISSLE_STEP, 0);
		} else if(direction == KeyEvent.VK_NUMPAD7) {
			firedMissle = fire(-MISSLE_STEP, -MISSLE_STEP);
		} else if(direction == KeyEvent.VK_NUMPAD8) {
			firedMissle = fire(0, -MISSLE_STEP);
		} else if(direction == KeyEvent.VK_NUMPAD9) {
			firedMissle = fire(MISSLE_STEP, -MISSLE_STEP);
		}
		if( firedMissle != null) {
			barrel.add(firedMissle);
		} else {
			this.xBarrelDirection = (int) Math.signum(xDir) * FriendTank.TANK_WIDTH / 2;
			this.yBarrelDirection = (int) Math.signum(yDir) * FriendTank.TANK_HEIGHT / 2;
		}
	}

	public void keyReleased(KeyEvent e) {
		int direction = e.getKeyCode();

		if(direction == KeyEvent.VK_UP || direction == KeyEvent.VK_DOWN || direction == KeyEvent.VK_LEFT || direction == KeyEvent.VK_RIGHT) {
			xDir = yDir = 0;
		}		
	}
}

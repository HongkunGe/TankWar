
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Tank extends ObjectInTankWar{
	private static final int TANK_STEP = 5;
	private static final int MISSLE_STEP = 10;
	public static final int TANK_WIDTH = 30;
	public static final int TANK_HEIGHT = 30;
	
	boolean role;
	int xBarrelDirection, yBarrelDirection;
	
	ArrayList<Missle> barrel = new ArrayList<Missle>();
	public void draw(Graphics g) {
		
		// Used to restore the front-color
		Color cOriginal = g.getColor();
		g.setColor(c);
		
		// Draw the tank
		g.fillOval(x, y, TANK_WIDTH, TANK_HEIGHT);
		
		// Draw the barrel
		int xStart = x + TANK_WIDTH / 2, yStart = y + TANK_HEIGHT / 2;
		g.setColor(Color.BLACK);
		g.drawLine(xStart, yStart, xStart + xBarrelDirection, yStart + yBarrelDirection);
		g.setColor(cOriginal);
		
		x += xDir;
		y += yDir;
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
		}
		
	}

	public void keyReleased(KeyEvent e) {
		int direction = e.getKeyCode();

		if(direction == KeyEvent.VK_UP || direction == KeyEvent.VK_DOWN || direction == KeyEvent.VK_LEFT || direction == KeyEvent.VK_RIGHT) {
			xDir = yDir = 0;
		}		
	}
	
	private Missle fire(int xMissleDir, int yMissleDir) {
		int xMissle = x + Tank.TANK_WIDTH / 2 - Missle.MISSLE_WIDTH / 2;
		int yMissle = y + Tank.TANK_HEIGHT / 2 - Missle.MISSLE_HEIGHT / 2;
		
		this.xBarrelDirection = (int) Math.signum(xMissleDir) * Tank.TANK_WIDTH / 2;
		this.yBarrelDirection = (int) Math.signum(yMissleDir) * Tank.TANK_HEIGHT / 2;
		return new Missle(xMissle, yMissle, xMissleDir, yMissleDir);
	}
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param role
	 * @param c
	 */
	public Tank(int x, int y, int width, int height, boolean role, Color c) {
		super(x, y, width, height ,0 ,0 ,c);
		this.role = role;
		this.xBarrelDirection = 0;
		this.yBarrelDirection = Tank.TANK_HEIGHT / 2;
	}
}

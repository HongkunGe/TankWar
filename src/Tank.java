
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Tank extends ObjectInTankWar{
	private static final int TANK_STEP = 5;
	private static final int MISSLE_STEP = 10;
	boolean role;
	
	ArrayList<Missle> barrel = new ArrayList<Missle>();
	public void draw(Graphics g) {
		
		// Used to restore the front-color
		Color cOriginal = g.getColor();
		g.setColor(c);
		
		// Draw the tank
		g.fillOval(x, y, width, height);
		g.setColor(cOriginal);
		
		x += xDir;
		y += yDir;
	}
	
	public void keyControl(KeyEvent e) {
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
			firedMissle = new Missle(x + width / 2, y + height / 2, -MISSLE_STEP, MISSLE_STEP);
		} else if(direction == KeyEvent.VK_NUMPAD2) {
			firedMissle = new Missle(x + width / 2, y + height / 2, 0, MISSLE_STEP);
		} else if(direction == KeyEvent.VK_NUMPAD3) {
			firedMissle = new Missle(x + width / 2, y + height / 2, MISSLE_STEP, MISSLE_STEP);
		} else if(direction == KeyEvent.VK_NUMPAD4) {
			firedMissle = new Missle(x + width / 2, y + height / 2, -MISSLE_STEP, 0);
		} else if(direction == KeyEvent.VK_NUMPAD6) {
			firedMissle = new Missle(x + width / 2, y + height / 2, MISSLE_STEP, 0);
		} else if(direction == KeyEvent.VK_NUMPAD7) {
			firedMissle = new Missle(x + width / 2, y + height / 2, -MISSLE_STEP, -MISSLE_STEP);
		} else if(direction == KeyEvent.VK_NUMPAD8) {
			firedMissle = new Missle(x + width / 2, y + height / 2, 0, -MISSLE_STEP);
		} else if(direction == KeyEvent.VK_NUMPAD9) {
			firedMissle = new Missle(x + width / 2, y + height / 2, MISSLE_STEP, -MISSLE_STEP);
		}
		if( firedMissle != null) {
			barrel.add(firedMissle);
		}
		
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
	}
}

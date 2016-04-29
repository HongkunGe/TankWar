
import java.awt.*;
import java.awt.event.*;

public class Tank {
	private final int STEP = 5;
	int x, y, width, height, xDir, yDir;
	boolean role;
	Color c;
	
	public void drawTank(Graphics g) {
		
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
		if(direction == KeyEvent.VK_UP) {
			xDir = 0;
			yDir = -STEP;
		} else if(direction == KeyEvent.VK_DOWN) {
			xDir = 0;
			yDir = STEP;
		} else if(direction == KeyEvent.VK_LEFT) {
			xDir = -STEP;
			yDir = 0;
		} else if(direction == KeyEvent.VK_RIGHT) {
			xDir = STEP;
			yDir = 0;
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
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.role = role;
		this.c = c;
		this.xDir = 0;
		this.yDir = 0;
	}
}

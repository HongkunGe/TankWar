package hongkun.tank;
import java.awt.Color;
import java.awt.Graphics;

// The ancester of all objects in Tank War.
public abstract class ObjectInTankWar {
	int x, y, width, height, xDir, yDir;
	Color c;
	
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param xDir
	 * @param yDir
	 * @param c
	 */
	public ObjectInTankWar(int x, int y, int width, int height, int xDir, int yDir, Color c) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xDir = xDir;
		this.yDir = yDir;
		this.c = c;
	}
	
	public abstract void draw(Graphics g);
}

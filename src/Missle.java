import java.awt.Color;
import java.awt.Graphics;

public class Missle extends ObjectInTankWar{
	
	public static final int MISSLE_WIDTH = 10;
	public static final int MISSLE_HEIGHT = 10;
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param xDir
	 * @param yDir
	 * @param c
	 */
	public Missle(int x, int y, int xDir, int yDir) {
		super(x, y, MISSLE_WIDTH, MISSLE_HEIGHT, xDir, yDir, Color.BLACK);
	}

	@Override
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
}

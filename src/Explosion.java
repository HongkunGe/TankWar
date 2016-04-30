import java.awt.Color;
import java.awt.Graphics;

public class Explosion {
	int x, y, stepsCount;
	ClientFrame clientFrame;
	Color c = Color.ORANGE;
	boolean isLive = true;
	
	int[] diameter = {8, 12, 16, 26, 36, 56, 66, 26, 16, 6};
	
	/**
	 * @param x
	 * @param y
	 */
	public Explosion(int x, int y, ClientFrame clientFrame) {
		this.x = x;
		this.y = y;
		this.stepsCount = 0;
		this.clientFrame = clientFrame;
	}
	
	public void draw(Graphics g) {
		if(stepsCount == diameter.length) {
//			this.clientFrame.explosionEvents.remove(this);
			isLive = false;
			return;
		}
		Color cOriginal = g.getColor();
		
		g.setColor(c);
		g.fillOval(x - diameter[stepsCount] / 2, y - diameter[stepsCount] / 2, diameter[stepsCount], diameter[stepsCount]);
		g.setColor(cOriginal);
		stepsCount ++;
	}

	/**
	 * @return the isLive
	 */
	public boolean isLive() {
		return isLive;
	}
}

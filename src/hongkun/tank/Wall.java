package hongkun.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

public class Wall {
	
	public static int WALL_SIZE = 30;
	private static int LEFT_WALL = 80;
	private static int RIGHT_WALL = 700;
	Color c = Color.getHSBColor(35, (float) 0.84, (float) 0.38); // BROWN
	ArrayList<Position> branketCollection = new ArrayList<Position>();
	
	ClientFrame clientFrame;
	public Wall(ClientFrame clientFrame) {
		this.clientFrame = clientFrame;
		
		buildWall();
	}
	
	private void buildWall() {
		for(int yAxis = 90; yAxis <= 520; yAxis += 46) {
			branketCollection.add(new Position(Wall.LEFT_WALL, yAxis));
		}
		
		for(int yAxis = 90; yAxis <= 520; yAxis += 46) {
			branketCollection.add(new Position(Wall.RIGHT_WALL, yAxis));
		}
		
		for(int yAxis = 90; yAxis <= 500; yAxis += 80) {
			for(int xAxis = 150; xAxis <= 630; xAxis += 30) {
				branketCollection.add(new Position(xAxis, yAxis));
			}
		}
	}
	
	public void draw(Graphics g) {
		Color cOriginal = g.getColor();
		
		g.setColor(c);
		for(Iterator<Position> it = this.branketCollection.iterator(); it.hasNext();) {
			Position pos = it.next();
			g.fillRect(pos.x, pos.y, WALL_SIZE, WALL_SIZE);
		}
		g.setColor(cOriginal);
	}
	
	public boolean isHitByObject(ObjectInTankWar objectInTankWar) {
		boolean isHit = false;
		for(Iterator<Position> it = this.branketCollection.iterator(); it.hasNext();) {
			Position pos = it.next();
			Rectangle rec = new Rectangle(pos.x, pos.y, WALL_SIZE, WALL_SIZE);
			isHit = isHit || rec.intersects(new Rectangle(objectInTankWar.x, objectInTankWar.y, objectInTankWar.width, objectInTankWar.height));
			if(isHit) {
				break;
			}
		}
		return isHit;
	}
}



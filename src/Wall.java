import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

public class Wall {
	
	public static int WALL_SIZE = 30;
	Color c = Color.getHSBColor(35, (float) 0.84, (float) 0.38); // BROWN
	ArrayList<Position> branketCollection = new ArrayList<Position>();
	
	ClientFrame clientFrame;
	public Wall(ClientFrame clientFrame) {
		this.clientFrame = clientFrame;
		
		int[][] indexes = {{90, 90}, {90,120}, {90,150}, {90,180}, {90,210}, {90,240}, {90,270}, {90,300}, {90,330}, {90,360}, {90,390}, {90,420}};
		for(int i = 0; i < indexes.length; i++) {
			branketCollection.add(new Position(indexes[i][0], indexes[i][1]));
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



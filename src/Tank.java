import java.util.*;
import java.awt.Color;
import java.awt.Graphics;

public class Tank extends ObjectInTankWar{
	public static final int TANK_STEP = 5;
	public static final int MISSLE_STEP = 10;
	public static final int TANK_WIDTH = 30;
	public static final int TANK_HEIGHT = 30;
	public static final HashMap<Integer, Direction> tankDir = createTankDirMap();
	public static final HashMap<Integer, Direction> missleDir = createMissleDirMap();
	boolean role;
	int xBarrelDirection, yBarrelDirection;
	ClientFrame clientFrame = null;
	ArrayList<Missle> barrel = new ArrayList<Missle>();
	
	public static HashMap<Integer, Direction> createTankDirMap() {
		HashMap<Integer, Direction> tankTempDir = new HashMap<Integer, Direction>();
		tankTempDir.put(0, new Direction(0, -TANK_STEP));
		tankTempDir.put(1, new Direction(0, TANK_STEP));
		tankTempDir.put(2, new Direction(-TANK_STEP, 0));
		tankTempDir.put(3, new Direction(TANK_STEP, 0));
		return tankTempDir;
	}
	
	public static HashMap<Integer, Direction> createMissleDirMap() {
		HashMap<Integer, Direction> missleTempDir = new HashMap<Integer, Direction>();
		missleTempDir.put(0, new Direction(-MISSLE_STEP, MISSLE_STEP));
		missleTempDir.put(1, new Direction(0, MISSLE_STEP));
		missleTempDir.put(2, new Direction(MISSLE_STEP, MISSLE_STEP));
		missleTempDir.put(3, new Direction(-MISSLE_STEP, 0));
		missleTempDir.put(4, new Direction(MISSLE_STEP, 0));
		missleTempDir.put(5, new Direction(-MISSLE_STEP, -MISSLE_STEP));
		missleTempDir.put(6, new Direction(0, -MISSLE_STEP));
		missleTempDir.put(7, new Direction(MISSLE_STEP, -MISSLE_STEP));
		return missleTempDir;
	}
	
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
		
		if(x < 0) x = 0;
		if(y < 20) y = 20;
		if(x > ClientFrame.GAME_WIDTH - FriendTank.TANK_WIDTH) x = ClientFrame.GAME_WIDTH - FriendTank.TANK_WIDTH;
		if(y > ClientFrame.GAME_HEIGHT - FriendTank.TANK_HEIGHT) y = ClientFrame.GAME_HEIGHT - FriendTank.TANK_HEIGHT;
	}
	
	// Randomly setting xDir, yDir. firedMissle with random:
	// firedMissle = fire(-MISSLE_STEP, MISSLE_STEP); 
	// xBarrelDirection yBarrelDirection
	
	public Missle fire(int xMissleDir, int yMissleDir) {
		int xMissle = x + FriendTank.TANK_WIDTH / 2 - Missle.MISSLE_WIDTH / 2;
		int yMissle = y + FriendTank.TANK_HEIGHT / 2 - Missle.MISSLE_HEIGHT / 2;
		
		this.xBarrelDirection = (int) Math.signum(xMissleDir) * FriendTank.TANK_WIDTH / 2;
		this.yBarrelDirection = (int) Math.signum(yMissleDir) * FriendTank.TANK_HEIGHT / 2;
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
		this.yBarrelDirection = FriendTank.TANK_HEIGHT / 2;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param role
	 * @param c
	 * @param clientFrame
	 */
	public Tank(int x, int y, int width, int height, boolean role, Color c, ClientFrame clientFrame) {
		this(x, y, width, height, role, c);
		this.clientFrame = clientFrame;
	}
}

class Direction{
	public int x, y;
	public Direction(int x, int y){
		this.x = x;
		this.y = y;
	}
}

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;

public class Tank extends ObjectInTankWar{
	public static final int TANK_STEP = 5;
	public static final int MISSLE_STEP = 10;
	public static final int TANK_WIDTH = 30;
	public static final int TANK_HEIGHT = 30;
	boolean role, isLive;
	int xBarrelDirection, yBarrelDirection;
	ClientFrame clientFrame = null;
	ArrayList<Missle> barrel = new ArrayList<Missle>();
	BloodBar bloodBar = new BloodBar();
	int life, wholeLife;
	
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
		bloodBar.draw(g);
		g.setColor(cOriginal);
		
		x += xDir;
		y += yDir;
		
		if(x < 0) x = 0;
		if(y < 20) y = 20;
		if(x > ClientFrame.GAME_WIDTH - FriendTank.TANK_WIDTH) x = ClientFrame.GAME_WIDTH - FriendTank.TANK_WIDTH;
		if(y > ClientFrame.GAME_HEIGHT - FriendTank.TANK_HEIGHT) y = ClientFrame.GAME_HEIGHT - FriendTank.TANK_HEIGHT;
		
		// Tanks hit wall or two tanks in the same team ram into each other, they stay here.
		if(clientFrame.wall.isHitByObject(this) || isRamByTeamTank()) {
			x -= xDir;
			y -= yDir;
		}
		
		for(Iterator<Missle> it = barrel.iterator(); it.hasNext();) {
			Missle firedMissle = it.next();
			firedMissle.draw(g);
			if(clientFrame.isOutOfBound(firedMissle.x, firedMissle.y) || clientFrame.wall.isHitByObject(firedMissle)) {
				it.remove();
				continue;
			}
			
			// hit tanks	
			if(this.role) {
				for(Iterator<EnemyTank> itEt = clientFrame.enemyTanks.iterator(); itEt.hasNext();) {
					EnemyTank et = itEt.next();
					if(firedMissle.hitTank(et) && et.isLive()) {
						et.isHitByMissle(firedMissle);
						it.remove();
					}
				}
			} else {
				if(firedMissle.hitTank(clientFrame.tank1) && clientFrame.tank1.isLive()) {
					clientFrame.tank1.isHitByMissle(firedMissle);
					it.remove();
				}
			}
		}
	}
	
	public void isHitByMissle(Missle firedMissle) {
		this.clientFrame.explosionEvents.add(new Explosion(firedMissle.x, firedMissle.y, this.clientFrame));
		this.life -= Missle.LIFE_MINUS_PER_HIT;
		if(this.life <= 0) {
			this.isLive = false;
		}
	}
	
	public boolean isRamByTeamTank() {
		boolean isRam = false;
		if(this.role) {
			// TODO
		} else {
			for(Iterator<EnemyTank> itEt = clientFrame.enemyTanks.iterator(); itEt.hasNext();) {
				EnemyTank et = itEt.next();
				if(et == this)
					continue;
				isRam = isRam || isRamByOtherTank(et);
				if(isRam) {
					break;
				}
			}
		}
		return isRam;
	}
	
	public boolean isRamByOtherTank(Tank tank) {
		int tx1 = x + TANK_WIDTH / 2, ty1 = y + TANK_HEIGHT / 2, tr1 = TANK_WIDTH / 2;
		int tx2 = tank.x + Tank.TANK_WIDTH / 2, ty2 = tank.y + Tank.TANK_HEIGHT / 2, tr2 = Tank.TANK_WIDTH / 2;
		return TankMath.isCircleCut(tx1, ty1, tr1, tx2, ty2, tr2);
	}
	
	public Missle fire(int xMissleDir, int yMissleDir) {
		int xMissle = x + FriendTank.TANK_WIDTH / 2 - Missle.MISSLE_WIDTH / 2;
		int yMissle = y + FriendTank.TANK_HEIGHT / 2 - Missle.MISSLE_HEIGHT / 2;
		
		this.xBarrelDirection = (int) Math.signum(xMissleDir) * FriendTank.TANK_WIDTH / 2;
		this.yBarrelDirection = (int) Math.signum(yMissleDir) * FriendTank.TANK_HEIGHT / 2;
		return new Missle(xMissle, yMissle, xMissleDir, yMissleDir);
	}
	
	public class BloodBar {
		public void draw(Graphics g) {
			Color cOriginal = g.getColor();
			g.setColor(Color.YELLOW);
			g.drawRect(x, y - 10, TANK_WIDTH / 2, 6);
			
			int widthOfBlood = TANK_WIDTH / 2 * life / wholeLife;
			g.setColor(Color.RED);
			g.fillRect(x, y - 9, widthOfBlood, 5);
			g.setColor(cOriginal);
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
	public Tank(int x, int y, int width, int height, boolean role, Color c, int life) {
		super(x, y, width, height ,0 ,0 ,c);
		
		this.life = life;
		this.wholeLife = life;
		this.isLive = true;
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
	public Tank(int x, int y, int width, int height, boolean role, Color c, int life, ClientFrame clientFrame) {
		this(x, y, width, height, role, c, life);
		this.clientFrame = clientFrame;
	}

	/**
	 * @return the isLive
	 */
	public boolean isLive() {
		return isLive;
	}
}


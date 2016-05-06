package hongkun.tank;
import java.util.*;
import java.awt.Color;
import java.awt.Graphics;

public class Tank extends ObjectInTankWar{
	public static final int TANK_STEP = 5;
	public static final int MISSLE_STEP = 10;
	public static final int TANK_WIDTH = 30;
	public static final int TANK_HEIGHT = 30;
	public static final int WHOLE_LIFE = 100;
	int id = 0;
	boolean role;
	int life, wholeLife = WHOLE_LIFE;
	int xBarrelDirection, yBarrelDirection;
	ArrayList<Missle> barrel = new ArrayList<Missle>();
	
	ClientFrame clientFrame = null;
	BloodBar bloodBar = new BloodBar();
	
	
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
		
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(this.id), x + TANK_WIDTH / 2, y + TANK_HEIGHT / 2);
		g.setColor(cOriginal);
		
		x += xDir;
		y += yDir;
			
		if(x < 0) x = 0;
		if(y < 20) y = 20;
		if(x > ClientFrame.GAME_WIDTH - TankByHuman.TANK_WIDTH) x = ClientFrame.GAME_WIDTH - TankByHuman.TANK_WIDTH;
		if(y > ClientFrame.GAME_HEIGHT - TankByHuman.TANK_HEIGHT) y = ClientFrame.GAME_HEIGHT - TankByHuman.TANK_HEIGHT;
		
		
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
			for(HashMap.Entry<Integer, TankByHuman> tankByHuman: this.clientFrame.tanksByHumanOnline.entrySet()) {
				// the tank will not hit itself or the teammate.
				TankByHuman tankByHumanOnlineForHit = tankByHuman.getValue();
				if(tankByHuman.getKey() != this.id && tankByHumanOnlineForHit.role != this.role) {
					if(firedMissle.hitTank(tankByHumanOnlineForHit) && tankByHumanOnlineForHit.isLive()) {
						tankByHumanOnlineForHit.isHitByMissle(firedMissle);
						it.remove();
					}
				}
			}
			
			// this tank is the one in tanksByHumanOnline, it may hit the host tank
			if(this.clientFrame.tanksByHumanOnline.containsKey(this.id)) {
				if(this.role != clientFrame.tank0.role) {
					if(firedMissle.hitTank(clientFrame.tank0) && clientFrame.tank0.isLive()) {
						clientFrame.tank0.isHitByMissle(firedMissle);
						it.remove();
					}
				}
			}
//			
//			if(this.role) {
//				for(Iterator<TankByRobot> itEt = clientFrame.tankByRobots.iterator(); itEt.hasNext();) {
//					TankByRobot et = itEt.next();
//					if(firedMissle.hitTank(et) && et.isLive()) {
//						et.isHitByMissle(firedMissle);
//						it.remove();
//					}
//				}
//			} else {
//				if(firedMissle.hitTank(clientFrame.tank0) && clientFrame.tank0.isLive()) {
//					clientFrame.tank0.isHitByMissle(firedMissle);
//					it.remove();
//				}
//			}
		}
	}
	
	public void isHitByMissle(Missle firedMissle) {
		this.clientFrame.explosionEvents.add(new Explosion(firedMissle.x, firedMissle.y, this.clientFrame));
		this.life -= Missle.LIFE_MINUS_PER_HIT;
//		if(this.life <= 0) {
//			this.isLive = false;
//		}
	}
	
	public boolean isRamByTeamTank() {
		boolean isRam = false;
		
		// this tank are not ram into other tankByRoots.
		for(Iterator<TankByRobot> itEt = clientFrame.tankByRobots.iterator(); itEt.hasNext();) {
			TankByRobot et = itEt.next();
			if(et == this)
				continue;
			isRam = isRam || isRamByOtherTank(et);
			if(isRam) {
				return true;
			}
		}
		
		// this tank are not ram into other tankByHumans.
		for(HashMap.Entry<Integer, TankByHuman> tankByHuman: this.clientFrame.tanksByHumanOnline.entrySet()) {
			if(tankByHuman.getKey() != this.id){
				isRam = isRam || isRamByOtherTank(tankByHuman.getValue());
				if(isRam) {
					return true;
				}
			}
		}
		
		// When otherTankByHumanOnline is moving, we should detect if it will ram into the tank0.
		if(this.clientFrame.tanksByHumanOnline.containsKey(this.id)) {
			isRam = isRam || isRamByOtherTank(clientFrame.tank0);
		}

		return isRam;
	}
	
	public boolean isRamByOtherTank(Tank tank) {
		int tx1 = x + TANK_WIDTH / 2, ty1 = y + TANK_HEIGHT / 2, tr1 = TANK_WIDTH / 2;
		int tx2 = tank.x + Tank.TANK_WIDTH / 2, ty2 = tank.y + Tank.TANK_HEIGHT / 2, tr2 = Tank.TANK_WIDTH / 2;
		return TankMath.isCircleCut(tx1, ty1, tr1, tx2, ty2, tr2);
	}
	
	public Missle fire(int xMissleDir, int yMissleDir) {
		int xMissle = x + TankByHuman.TANK_WIDTH / 2 - Missle.MISSLE_WIDTH / 2;
		int yMissle = y + TankByHuman.TANK_HEIGHT / 2 - Missle.MISSLE_HEIGHT / 2;
		
		this.xBarrelDirection = (int) Math.signum(xMissleDir) * TankByHuman.TANK_WIDTH / 2;
		this.yBarrelDirection = (int) Math.signum(yMissleDir) * TankByHuman.TANK_HEIGHT / 2;
		return new Missle(xMissle, yMissle, xMissleDir, yMissleDir);
	}
	
	public class BloodBar {
		public void draw(Graphics g) {
			Color cOriginal = g.getColor();
			g.setColor(Color.YELLOW);
			g.drawRect(x, y - 10, TANK_WIDTH / 2, 6);
			
			int widthOfBlood = (TANK_WIDTH / 2 * life) / wholeLife;
			
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
		this.wholeLife = WHOLE_LIFE;
		this.role = role;
		this.xBarrelDirection = 0;
		this.yBarrelDirection = TankByHuman.TANK_HEIGHT / 2;
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
		return this.life > 0;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(boolean role) {
		this.role = role;
	}

	/**
	 * @return the life
	 */
	public int getLife() {
		return life;
	}
}


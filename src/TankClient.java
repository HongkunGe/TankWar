import java.util.*;
import java.awt.*;
import java.awt.event.*;


public class TankClient {

	public static void main(String[] args) {
		new ClientFrame();
		
	}
}

class ClientFrame extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	public static final int GAME_X_LOC = 400;
	public static final int GAME_Y_LOC = 300;
	
	private final int INTERVAL = 30; // ms
	
	FriendTank tank1 = new FriendTank(500, 500, 30, 30, true, Color.RED, this);
	public ArrayList<Explosion> explosionEvents = new ArrayList<Explosion>();
	
	public ArrayList<EnemyTank> enemyTanks = new ArrayList<EnemyTank>();
	
	Image offScreenImage = null;
	
	public ClientFrame() {
		super("TankClient");
		for(int i = 6; i < 10; i++){
			enemyTanks.add(new EnemyTank(50, i * 10 + 30, 30, 30, Color.BLUE, this));
		}		
		
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setLocation(GAME_X_LOC, GAME_Y_LOC);
		this.setVisible(true);
		this.addWindowListener(new WindowMonitor());
		this.addKeyListener(new KeyMonitor());
		this.setResizable(false);
		this.setBackground(Color.gray);
		
		new Thread(new RepaintThread()).start();
	}

	@Override
	public void paint(Graphics g) {
		if(tank1.isLive()) {
			tank1.draw(g);
		}
		
		for(Iterator<EnemyTank> itEt = enemyTanks.iterator(); itEt.hasNext();) {
			EnemyTank et = itEt.next();
			et.draw(g);
			if(!et.isLive()) {
				itEt.remove();
			}
		}
		
		Color cOriginal = g.getColor();
		g.setColor(Color.BLACK);
		g.drawString("Explosion Count: " + explosionEvents.size(), 10, 40);
		g.drawString("Tanks Count: " + enemyTanks.size(), 10, 60);
		g.setColor(cOriginal);
		
		for(Iterator<Explosion> it = explosionEvents.iterator(); it.hasNext();) {
			Explosion xps = it.next();
			xps.draw(g);
			if(!xps.isLive()) {
				it.remove();
			}
		}
	}
	
	private class RepaintThread implements Runnable {

		@Override
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			tank1.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			tank1.keyReleased(e);
		}
	}
	
	class WindowMonitor extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			setVisible(false);
			System.exit(0);
		}
	}

	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		
		Graphics gOffScreenImage = offScreenImage.getGraphics();
		Color c = gOffScreenImage.getColor();
		
		gOffScreenImage.setColor(Color.GRAY);
		gOffScreenImage.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		paint(gOffScreenImage);
		g.drawImage(offScreenImage, 0, 0, null);
		
		gOffScreenImage.setColor(c);
	}
	
	public boolean isOutOfBound(int xIndex, int yIndex) {
		if(0 > xIndex || xIndex > GAME_WIDTH || 0 > yIndex || yIndex > GAME_HEIGHT){
			return true;
		}
		return false;
	}
}


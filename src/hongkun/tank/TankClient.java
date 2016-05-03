package hongkun.tank;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


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
	public static final int INTERVAL = 30; // ms
	
	public TankByHuman tank1 = new TankByHuman(500, 500, 30, 30, true, Color.RED, 100, this);
	public ArrayList<TankByHuman> tanksByHumanOnline = new ArrayList<TankByHuman>();
	public ArrayList<Explosion> explosionEvents = new ArrayList<Explosion>();
	public ArrayList<TankByRobot> tankByRobots = new ArrayList<TankByRobot>();
	public Wall wall = new Wall(this);
	
	private TankClientNetAgent clientNetAgent = new TankClientNetAgent(this);
	
	Image offScreenImage = null;
	
	public ClientFrame() {
		super("TankClient");
		
		int initTankCount = Integer.parseInt(PropertyManager.getPropertyByName("initTankCount"));
		
		for(int i = 0; i < initTankCount; i++){
			tankByRobots.add(new TankByRobot(50, i * 40 + 30, 30, 30, false, Color.BLUE, 40, this));
		}		
		
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setLocation(GAME_X_LOC, GAME_Y_LOC);
		this.setVisible(true);
		this.addWindowListener(new WindowMonitor());
		this.addKeyListener(new KeyMonitor());
		this.setResizable(false);
		this.setBackground(Color.gray);
		
		new Thread(new RepaintThread()).start();
		
		clientNetAgent.connect("127.0.0.1", TankServer.TCP_PORT);
	}

	@Override
	public void paint(Graphics g) {
		
		wall.draw(g);
		if(tank1.isLive()) {
			tank1.draw(g);
		}
		
		for(Iterator<TankByHuman> it = tanksByHumanOnline.iterator(); it.hasNext();) {
			TankByHuman t = it.next();
			t.draw(g);
			if(!t.isLive()) {
				it.remove();
			}
		}
		
		for(Iterator<TankByRobot> itEt = tankByRobots.iterator(); itEt.hasNext();) {
			TankByRobot et = itEt.next();
			et.draw(g);
			if(!et.isLive()) {
				itEt.remove();
			}
		}
		
		Color cOriginal = g.getColor();
		g.setColor(Color.BLACK);
		g.drawString("Explosion Count: " + explosionEvents.size(), 10, 40);
		g.drawString("Tanks Count: " + tankByRobots.size(), 10, 60);
		g.drawString("Tank Life: " + tank1.life, 10, 80);
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


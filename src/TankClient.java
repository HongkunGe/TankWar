
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;


public class TankClient {

	public static void main(String[] args) {
		new ClientFrame();
		
	}
	


}

class ClientFrame extends Frame {
	private static final int GAME_WIDTH = 800;
	private static final int GAME_HEIGHT = 600;
	private static final int GAME_X_LOC = 400;
	private static final int GAME_Y_LOC = 300;
	
	private final int INTERVAL = 30; // ms
	
	Tank tank1 = new Tank(50, 50, 30, 30, true, Color.RED);
	
	Image offScreenImage = null;
	
	public ClientFrame() {
		super("TankClient");
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
		tank1.draw(g);
		for(Iterator<Missle> it = tank1.barrel.iterator(); it.hasNext();) {
			Missle firedMissle = it.next();
			firedMissle.draw(g);
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
			tank1.keyControl(e);
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
}


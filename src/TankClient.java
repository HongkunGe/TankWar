
import java.awt.*;
import java.awt.event.*;


public class TankClient {

	public static void main(String[] args) {
		new ClientFrame();

	}
	


}

class ClientFrame extends Frame {
	private final int GAME_WIDTH = 800;
	private final int GAME_HEIGHT = 600;
	private final int GAME_X_LOC = 400;
	private final int GAME_Y_LOC = 300;
	private final int STEP = 5;
	private final int INTERVAL = 30; // ms
	
	int x = 50, y = 50;
	int width = 30, height = 30;
	
	Image offScreenImage = null;
	int xDir = STEP, yDir = STEP;
	
	public ClientFrame() {
		super("TankClient");
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setLocation(GAME_X_LOC, GAME_Y_LOC);
		this.setVisible(true);
		this.addWindowListener(new WindowMonitor());
		this.addKeyListener(new KeyMonitor());
		this.setResizable(false);
		this.setBackground(Color.gray);
		
		new Thread(new RepaintThread()).start();;
	}

	@Override
	public void paint(Graphics g) {
		
		// Used to restore the front-color
		Color c = g.getColor();
		g.setColor(Color.RED);
		
		// Draw the tank
		g.fillOval(x, y, width, height);
		g.setColor(c);
		
		x += xDir;
		y += yDir;
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
			int direction = e.getKeyCode();
			if(direction == KeyEvent.VK_UP) {
				xDir = 0;
				yDir = -STEP;
			} else if(direction == KeyEvent.VK_DOWN) {
				xDir = 0;
				yDir = STEP;
			} else if(direction == KeyEvent.VK_LEFT) {
				xDir = -STEP;
				yDir = 0;
			} else if(direction == KeyEvent.VK_RIGHT) {
				xDir = STEP;
				yDir = 0;
			}
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


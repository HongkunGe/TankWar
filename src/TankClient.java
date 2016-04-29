
import java.awt.*;
import java.awt.event.*;

public class TankClient {

	public static void main(String[] args) {
		new ClientFrame();

	}
	


}

class ClientFrame extends Frame {
	
	int x = 50, y = 50;
	int width = 30, height = 30;
	public ClientFrame() {
		super("TankClient");
		this.setSize(800, 650);
		this.setLocation(400, 300);
		this.setVisible(true);
		this.addWindowListener(new WindowMonitor());
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
		
		x += 5;
		y += 5;
	}
	
	private class RepaintThread implements Runnable {

		@Override
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	class WindowMonitor extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.out.println("Window is Closing.");
			setVisible(false);
			System.exit(0);
		}
	}
}


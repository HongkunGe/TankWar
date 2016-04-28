
import java.awt.*;
import java.awt.event.*;

public class TankClient {

	public static void main(String[] args) {
		new ClientFrame();

	}
	


}

class ClientFrame extends Frame {
	public ClientFrame() {
		super("TankClient");
		this.setSize(800, 650);
		this.setLocation(400, 300);
		this.setVisible(true);
		this.addWindowListener(new WindowMonitor());
	}
	
	class WindowMonitor extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.out.println("Window is Closing.");
			setVisible(false);
			System.exit(0);
		}
	}
}


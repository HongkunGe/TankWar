package hongkun.tank;

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
	public static final int INTERVAL = 30; // ms
	private static final int DIALOG_X_LOC = GAME_X_LOC + GAME_WIDTH / 3;
	private static final int DIALOG_Y_LOC = GAME_Y_LOC + GAME_HEIGHT / 3;
	public static final int INITIAL_TANK_X_LEFT_LOC = 40;
	public static final int INITIAL_TANK_X_RIGHT_LOC = 750;
	public static final int INITIAL_TANK_Y_LOC = 35;

	public TankByHuman tank0 = new TankByHuman(-100, -100, 30, 30, false, Color.RED, 100, this);;
	public HashMap<Integer, TankByHuman> tanksByHumanOnline = new HashMap<Integer, TankByHuman>();
	public ArrayList<Explosion> explosionEvents = new ArrayList<Explosion>();
	public ArrayList<TankByRobot> tankByRobots = new ArrayList<TankByRobot>();
	public Wall wall = new Wall(this);

	public TankClientNetAgent clientNetAgent = new TankClientNetAgent(this);
	ConnectDialog dialog = new ConnectDialog();

	public int teamACount = 0, teamBCount = 0;

	Image offScreenImage = null;

	Thread paintThread = new Thread(new RepaintThread());

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

		this.dialog.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {

		wall.draw(g);
		tank0.draw(g);

		for(Iterator<HashMap.Entry<Integer, TankByHuman>> it = tanksByHumanOnline.entrySet().iterator(); it.hasNext();) {
			HashMap.Entry<Integer, TankByHuman> tankByHumanOnline = it.next();
			TankByHuman t = tankByHumanOnline.getValue();

			t.draw(g);
		}

		for(Iterator<TankByRobot> itEt = tankByRobots.iterator(); itEt.hasNext();) {
			TankByRobot et = itEt.next();

			et.draw(g);
		}

		Color cOriginal = g.getColor();
		g.setColor(Color.BLACK);
//		g.drawString("Explosion Count: " + explosionEvents.size(), 10, 40);
		g.drawString("Team A Count: " + teamACount, 10, 40);
		g.drawString("Team B Count: " + teamBCount, 680, 40);
		g.drawString("Player Count: " + (tanksByHumanOnline.size() + (tank0.isLive() ? 1: 0)), 680, 60);
		g.drawString("#" + this.tank0.id + " Tank Life: " + tank0.life, 10, 60);

		g.drawString("----------------", 10, 80);
		int count = 1;
		for(HashMap.Entry<Integer, TankByHuman> tankByHuman: this.tanksByHumanOnline.entrySet()) {
			g.drawString("#" + tankByHuman.getKey() + " Tank Life: " + tankByHuman.getValue().life, 10, 80 + 20 * count);
			count ++;
		}
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
			tank0.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			tank0.keyReleased(e);
		}
	}

	class WindowMonitor extends WindowAdapter {
		public void windowClosing(WindowEvent e) {

			// send server the message that I am going to quit
			TankMessage msg = new TankKeyEventMessage(ClientFrame.this.tank0, TankMessage.TANK_QUITMESSAGE);
			clientNetAgent.send(msg);
System.out.println("Client#" + ClientFrame.this.tank0.id + " : A quitting message sent to server.");
			setVisible(false);
			System.exit(0);
		}
	}

	class ConnectDialog extends Dialog {
		Button button = new Button("OK");
		TextField tfIP = new TextField("127.0.0.1", 12);
		TextField tfTCPPort = new TextField("" + TankServer.TCP_PORT, 4);
		TextField tfUDPPort = new TextField("2226", 4);
		TextField tfRole = new TextField("A", 1);
		public ConnectDialog() {
			super(ClientFrame.this, true);

			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);

			this.add(new Label("Port:"));
			this.add(tfTCPPort);

			this.add(new Label("UDP Port:"));
			this.add(tfUDPPort);

			this.add(new Label("Team(A or B):"));
			this.add(tfRole);
			this.add(button);
			this.setLocation(DIALOG_X_LOC, DIALOG_Y_LOC);

			this.pack();

			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String IP = ConnectDialog.this.tfIP.getText().trim();
					int port = Integer.parseInt(ConnectDialog.this.tfTCPPort.getText().trim());
					int UDPport = Integer.parseInt(ConnectDialog.this.tfUDPPort.getText().trim());
					TankClientNetAgent.setUDP_PORT(UDPport);

					setVisible(false);

					String role = ConnectDialog.this.tfRole.getText().trim().toLowerCase();
					if(role.equals("a")) {
						ClientFrame.this.tank0.setRole(true);
					} else {
						ClientFrame.this.tank0.setRole(false);
					}

					ClientFrame.this.teamStatistics(role.equals("a"), 1);
					ClientFrame.this.clientNetAgent.connect(IP, port);
					ClientFrame.this.tank0.setInitialLocationDirection(0, 0);
					ClientFrame.this.paintThread.start();
				}
			});
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

	public void teamStatistics(boolean role, int wave) {
		if(role) {
			teamACount += wave;
		}
		else {
			teamBCount += wave;
		}
	}
}


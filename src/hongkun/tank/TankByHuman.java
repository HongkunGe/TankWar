package hongkun.tank;

import java.awt.*;
import java.awt.event.*;

public class TankByHuman extends Tank{
	
	public int keyPressedCode = 0;
	public int keyReleasedCode = 0;
	
	/*
	 * Control keys to manage the behavior of press and release. 
	 * */
	private boolean afterRelease = true;
	private boolean isValidKey = true;
	
	public TankByHuman(int x, int y, int width, int height, boolean role, Color c, int life, ClientFrame clientFrame) {
		super(x, y, width, height, role, c, life, clientFrame);
	}
	
	public TankByHuman(int x, int y, int width, int height, boolean role, Color c, int life) {
		super(x, y, width, height, role, c, life);
	}
	
	// copy construction 
	public TankByHuman(TankByHuman originalTank) {
		super(originalTank.x, originalTank.y, originalTank.width, originalTank.height, originalTank.role, originalTank.c, originalTank.life, originalTank.clientFrame);
		this.id = originalTank.id;
		this.xDir = originalTank.xDir;
		this.yDir = originalTank.yDir;
		this.xBarrelDirection = originalTank.xBarrelDirection;
		this.yBarrelDirection = originalTank.yBarrelDirection;
		this.keyPressedCode = originalTank.keyPressedCode;
		this.keyReleasedCode = originalTank.keyReleasedCode;
	}
	
	public void keyPressed(KeyEvent e) {
		int oldKeyEventCode = keyPressedCode;
		keyPressedCode = e.getKeyCode();
		
		// keyEventCode is updated.
		isValidKey = keyPressedManager();
		
		/*
		 * Send key event as message to server and transfer the message to all other clients.
		 * When user hold the key, only one message is needed to be sent, otherwise the keyEventMessage
		 * is always sent.
		 * */
		if((keyPressedCode != oldKeyEventCode || afterRelease) && isValidKey) {
			TankMessage msg = new TankKeyEventMessage(this, TankMessage.TANK_KEYPRESSEDMESSAGE);
			clientFrame.clientNetAgent.send(msg);
			afterRelease = false;
		}
	}
	
	public void onlineKeyPressed(int keyPressedCodeReceivedOnline) {
		keyPressedCode = keyPressedCodeReceivedOnline;
		
		// keyEventCode is updated.
		keyPressedManager();
	}
	
	private boolean keyPressedManager() {
		Missle firedMissle = null;
		Direction dir = null;
		boolean isFireMissle = false;
		
		if(keyPressedCode == KeyEvent.VK_UP) {
			dir = DirectionGenerator.getDirection(Compass.U, TANK_STEP);
		} else if(keyPressedCode == KeyEvent.VK_DOWN) {
			dir = DirectionGenerator.getDirection(Compass.D, TANK_STEP);
		} else if(keyPressedCode == KeyEvent.VK_LEFT) {
			dir = DirectionGenerator.getDirection(Compass.L, TANK_STEP);
		} else if(keyPressedCode == KeyEvent.VK_RIGHT) {
			dir = DirectionGenerator.getDirection(Compass.R, TANK_STEP);
		} else if(keyPressedCode == KeyEvent.VK_F2){
			// restart
			if(!this.isLive()) {
				this.life = 100;
			}
			return false;
		} else if(keyPressedCode == KeyEvent.VK_NUMPAD5) {
//			superFire();
			return false;
		} else if(KeyEvent.VK_NUMPAD1 <= keyPressedCode && keyPressedCode <= KeyEvent.VK_NUMPAD9){
			isFireMissle = true;
			dir = DirectionGenerator.getDirection(keyPressedCode - KeyEvent.VK_NUMPAD1, MISSLE_STEP);
		} else {
			return false;
		}
		
		if(isFireMissle) {
			firedMissle = fire(dir.x, dir.y);
			barrel.add(firedMissle);
		} else {
			xDir = dir.x;
			yDir = dir.y;
			this.xBarrelDirection = (int) Math.signum(xDir) * TankByHuman.TANK_WIDTH / 2;
			this.yBarrelDirection = (int) Math.signum(yDir) * TankByHuman.TANK_HEIGHT / 2;
		}
		
		return true;
	}
	
	public void superFire() {
		for(int i = 1; i < 10; i++) {
			if(i == 5) 
				continue;
			Direction dir = DirectionGenerator.getDirection(i - 1, MISSLE_STEP);
			this.barrel.add(fire(dir.x, dir.y));
		}
	}
	
	public void keyReleased(KeyEvent e) {
		keyReleasedCode = e.getKeyCode();
		keyReleasedManager();
		
		if(this.isValidKey) {
			TankMessage msg = new TankKeyEventMessage(this, TankMessage.TANK_KEYRELEASEDMESSAGE);
			clientFrame.clientNetAgent.send(msg);
			afterRelease = true;
		}		
	}
	
	public void onlineKeyReleased(int keyReleasedCodeReceivedOnline) {
		keyReleasedCode = keyReleasedCodeReceivedOnline;
		
		// keyEventCode is updated.
		keyReleasedManager();
	}
	
	private void keyReleasedManager() {
		if(keyReleasedCode == KeyEvent.VK_UP || keyReleasedCode == KeyEvent.VK_DOWN || keyReleasedCode == KeyEvent.VK_LEFT || keyReleasedCode == KeyEvent.VK_RIGHT) {
			xDir = yDir = 0;
		}
	}

	public void setInitialLocationDirection(int xDir, int yDir) {
		if(this.role) {
			this.x = ClientFrame.INITIAL_TANK_X_LEFT_LOC;
			this.y = this.id * ClientFrame.INITIAL_TANK_Y_LOC;
		} else {
			this.x = ClientFrame.INITIAL_TANK_X_RIGHT_LOC;
			this.y = this.id * ClientFrame.INITIAL_TANK_Y_LOC;
		}
		this.xDir = xDir;
		this.yDir = yDir;
	}
	
	public void setXY(int x, int y, int xDir, int yDir) {
		this.x = x;
		this.y = y;
		this.xDir = xDir;
		this.yDir = yDir;
	}
}

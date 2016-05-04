package hongkun.tank;

import java.awt.*;
import java.awt.event.*;

public class TankByHuman extends Tank{
	
	public int keyEventCode = 0;
	
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
		this.keyEventCode = originalTank.keyEventCode;
	}
	
	public void keyPressed(KeyEvent e) {
		int oldKeyEventCode = keyEventCode;
		keyEventCode = e.getKeyCode();
		
		/*
		 * Send key event as message to server and transfer the message to all other clients.
		 * */
		if(keyEventCode != oldKeyEventCode) {
			TankMessage msg = new TankKeyEventMessage(this, TankMessage.TANK_KEYEVENTMESSAGE);
			clientFrame.clientNetAgent.send(msg);
		}
		
		// keyEventCode is updated.
		keyPressedManager();
	}
	
	public void onlineKeyPressed(int keyCodeReceivedOnline) {
		keyEventCode = keyCodeReceivedOnline;
		
		// keyEventCode is updated.
		keyPressedManager();
	}
	
	private void keyPressedManager() {
		Missle firedMissle = null;
		Direction dir = null;
		boolean isFireMissle = false;
		
		if(keyEventCode == KeyEvent.VK_UP) {
			dir = DirectionGenerator.getDirection(Compass.U, TANK_STEP);
		} else if(keyEventCode == KeyEvent.VK_DOWN) {
			dir = DirectionGenerator.getDirection(Compass.D, TANK_STEP);
		} else if(keyEventCode == KeyEvent.VK_LEFT) {
			dir = DirectionGenerator.getDirection(Compass.L, TANK_STEP);
		} else if(keyEventCode == KeyEvent.VK_RIGHT) {
			dir = DirectionGenerator.getDirection(Compass.R, TANK_STEP);
		} else if(keyEventCode == KeyEvent.VK_F2){
			// restart
			if(!this.isLive()) {
				this.isLive = true;
				this.life = 100;
			}
			return;
		} else if(keyEventCode == KeyEvent.VK_NUMPAD5) {
			superFire();
			return;
		} else if(KeyEvent.VK_NUMPAD1 <= keyEventCode && keyEventCode <= KeyEvent.VK_NUMPAD9){
			isFireMissle = true;
			dir = DirectionGenerator.getDirection(keyEventCode - KeyEvent.VK_NUMPAD1, MISSLE_STEP);
		} else {
			return;
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
		int direction = e.getKeyCode();

		if(direction == KeyEvent.VK_UP || direction == KeyEvent.VK_DOWN || direction == KeyEvent.VK_LEFT || direction == KeyEvent.VK_RIGHT) {
			xDir = yDir = 0;
		}
	}
}

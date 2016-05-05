package hongkun.tank;

/**
 * @author Hongkun Ge
 * The Class includes all needed math methods needed in other classes.
 */
public class TankMath {
	public static boolean isCircleCut(int p1, int p2, int r1,
										  int q1, int q2, int r2) {
		double distTwoCenter = ((double)((p1 - q1) * (p1 - q1) + (p2 - q2) * (p2 - q2)));
		double addRadius = (double)((r1 + r2) * (r1 + r2));
		return addRadius > distTwoCenter;
	}
	
	// Generate the random number [0, 7]
	public static int generateRandom(int min, int max) {
		int randomNum = (int)(Math.random() * ((max - min) + 1) + min);
		return randomNum;
	}
}


class DirectionGenerator{
	
	public static Direction getDirection(int index, int stepLength) {
		Compass[] comps = Compass.values();
		return getDirection(comps[index], stepLength);
	}
	
	public static Direction getDirection(Compass dir, int stepLength) {
		int xRes = 0, yRes = 0;
		switch(dir){
			case DL: 
				xRes = -1;
				yRes = 1;
				break;
			case D:
				xRes = 0;
				yRes = 1;
				break;
			case DR:
				xRes = 1;
				yRes = 1;
				break;
			case L:
				xRes = -1;
				yRes = 0;
				break;
			case STOP:
				xRes = 0;
				yRes = 0;
			case R:
				xRes = 1;
				yRes = 0;
				break;
			case UL:
				xRes = -1;
				yRes = -1;
				break;
			case U:
				xRes = 0;
				yRes = -1;
				break;
			case UR:
				xRes = 1;
				yRes = -1;
				break;
			default:
				break;
		}
			return new Direction(xRes * stepLength, yRes * stepLength);
	}
}

enum Compass {DL, D, DR, L, STOP, R, UL, U, UR};

class Direction {
	public int x, y;
	public Direction(int x, int y){
		this.x = x;
		this.y = y;
	}
}

class Position{
	public int x, y;
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
}
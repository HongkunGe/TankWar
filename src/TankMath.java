
/**
 * @author Hongkun Ge
 * The Class includes all needed math methods needed in other classes.
 */
public class TankMath {
	public static boolean isCircleCut(int p1, int p2, int r1,
										  int q1, int q2, int r2) {
		double distTwoCenter = Math.sqrt((double)((p1 - q1) * (p1 - q1) + (p2 - q2) * (p2 - q2)));
		double addRadius = (double)(r1 + r2);
		return addRadius > distTwoCenter;
	}
}

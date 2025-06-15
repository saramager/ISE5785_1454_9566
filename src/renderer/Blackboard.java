/**
 * 
 */
package renderer;

import static primitives.Util.isZero;

import java.util.LinkedList;
import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Blackboard {
	private Point p0;
	private Vector vUp;
	private Vector vRight;
	private Vector vTo;
	private static double distance = 100;
	private double lenght;
	private static int resolution = 9;

	public Blackboard(Ray ray, double size) {
		p0 = ray.getHead();
		vTo = ray.getDir();
		vRight = vTo.createOrthogonalVector();
		vUp = vRight.crossProduct(vTo);
		lenght = size * 2;
	}

//	public void setSize(double size) {
//		lenght = size;
//	}

	public void setDistance(double distance) {
		lenght = distance;
	}

	public static void setResolution(int resolution) {
		Blackboard.resolution = resolution;
	}

	private Ray constructRay(int j, int i) {
		Point pIJ = p0.add(vTo.scale(distance));
		double jitterX = Math.random() - 0.5;
		double jitterY = Math.random() - 0.5;
		// Calculate distance on x,y axes to the designated point
		double yI = (((resolution - 1) / 2.0) - i + jitterY) * (lenght / resolution);
		double xJ = (j - ((resolution - 1) / 2.0 + jitterX)) * (lenght / resolution);
		// Avoiding creation of zero vector (which is unnecessary anyway)
		if (!isZero(xJ))
			pIJ = pIJ.add(vRight.scale(xJ));
		if (!isZero(yI))
			pIJ = pIJ.add(vUp.scale(yI));
		return new Ray(p0, pIJ.subtract(p0));
	}

	/**
	 * Constructs a grid of rays in the target area
	 * 
	 * @return list of rays
	 */
	public List<Ray> constructRayBeamGrid() {
		List<Ray> rays = new LinkedList<>();
		for (int i = 0; i < resolution; ++i)
			for (int j = 0; j < resolution; j++)
				rays.add(constructRay(j, i));
		return rays;
	}
}

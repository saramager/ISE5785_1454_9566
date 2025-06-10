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
	private double width;
	private double height;
	private double size;
	private static int resolution = 17;

//for testing
	public Blackboard(Ray ray, double size) {
		p0 = ray.getHead();
		vTo = ray.getDir();
		vRight = vTo.createOrthogonalVector();
		vUp = vRight.crossProduct(vTo);
		this.size = size;
		width = size;
		height = size;
	}

//for camera
	public Blackboard(Point p0, Vector vTo, Vector vUp) {
		this.vTo = vTo.normalize();
		this.vUp = vUp.normalize();
		this.vRight = vTo.crossProduct(vUp);
		this.p0 = p0;
	}

	public void setSize(double size) {
		this.size = size;
		width = size;
		height = size;
	}

	public void setDistance(double distance) {
		this.size = distance;
		width = distance;
		height = distance;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	private Ray constructRay(int j, int i) {
		Point pIJ = p0.add(vTo.scale(distance));
		double jitterX = Math.random() - 0.5;
		double jitterY = Math.random() - 0.5;
		// Calculate distance on x,y axes to the designated point
		double yI = (((resolution - 1) / 2.0) - i + jitterY) * (height / resolution);
		double xJ = (j - ((resolution - 1) / 2.0 + jitterX)) * (width / resolution);
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

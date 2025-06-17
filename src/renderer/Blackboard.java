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

/**
 * Class representing a blackboard for ray tracing, which generates a grid of
 * rays in a specified area.
 * 
 * The grid is defined by a resolution and can be adjusted to create a specific
 * number of rays.
 */
public class Blackboard {
	/** The origin point of the grid */
	private Point p0;
	/** The up vector of the grid */
	private Vector vUp;
	/** The right vector of the grid */
	private Vector vRight;
	/** The direction vector of the grid */
	private Vector vTo;
	/**
	 * The distance from the origin to the target area, default is 100
	 */
	private double distance = 100;
	/**
	 * The length of the grid,
	 */
	private double length;
	/**
	 * The resolution of the grid, default is 9
	 */
	private int resolution;

	/**
	 * Default constructor initializes the blackboard with default values.
	 */
	public Blackboard() {
	}

	/**
	 * Constructor that initializes the blackboard with a specified number of beams.
	 * 
	 * @param numOfBeam the number of beams to create in the grid
	 */
	public Blackboard(Ray ray, double size, int numOfBeam) {
		p0 = ray.getHead();
		vTo = ray.getDir();
		vRight = vTo.createOrthogonalVector();
		vUp = vRight.crossProduct(vTo);
		length = size * 2;
		resolution = (int) Math.sqrt(numOfBeam);

	}

	/**
	 * Constructor that initializes the blackboard with a specified resolution.
	 * 
	 * @param distance the resolution of the grid
	 */
	public void setDistance(double distance) {
		length = distance;
	}

	/**
	 * Sets the distance from the origin to the target area.
	 * 
	 * @param resolution the distance to set
	 */
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	/**
	 * Constructs a ray from the origin point to a specific point in the grid.
	 * 
	 * @param j the column index of the grid
	 * @param i the row index of the grid
	 * @return a Ray object representing the ray from the origin to the specified
	 *         point
	 */
	private Ray constructRay(int j, int i) {
		Point pIJ = p0.add(vTo.scale(distance));
		double jitterX = Math.random() - 0.5;
		double jitterY = Math.random() - 0.5;
		// Calculate distance on x,y axes to the designated point
		double yI = (((resolution - 1) / 2.0) - i + jitterY) * (length / resolution);
		double xJ = (j - ((resolution - 1) / 2.0 + jitterX)) * (length / resolution);
		// Avoiding creation of zero vector (which is unnecessary anyway)
		if (!isZero(xJ))
			pIJ = pIJ.add(vRight.scale(xJ));
		if (!isZero(yI))
			pIJ = pIJ.add(vUp.scale(yI));
		return new Ray(p0, pIJ.subtract(p0));
	}

	/**
	 * Constructs a grid of rays based on the specified ray and size.
	 * 
	 * @param ray  the base ray from which to construct the grid
	 * @param size the size of the grid area
	 * @return a list of Ray objects representing the rays in the grid
	 */
	public List<Ray> constructRayBeamGrid() {
		if (resolution <= 1 || isZero(length))
			return List.of(new Ray(p0, vTo));
		List<Ray> rays = new LinkedList<>();
		for (int i = 0; i < resolution; ++i)
			for (int j = 0; j < resolution; j++)
				rays.add(constructRay(j, i));
		return rays;
	}
}

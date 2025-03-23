package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

	/**
	 * The center point of the sphere.
	 */
	private final Point center;

	/**
	 * Constructs a sphere with a given center point and radius.
	 *
	 * @param p      the center point of the sphere
	 * @param radius the radius of the sphere
	 */
	public Sphere(Point p, Double radius) {
		super(radius);
		this.center = p;
	}

	@Override
	public Vector getNormal(Point p) {
		// TODO Auto-generated method stub
		return null;
	}
}

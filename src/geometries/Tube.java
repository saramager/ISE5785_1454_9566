package geometries;

import static primitives.Util.alignZero;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents an infinite tube in 3D space, defined by a central axis (a ray)
 * and a radius.
 */
public class Tube extends RadialGeometry {

	/**
	 * The central axis of the tube, represented as a ray.
	 */
	protected final Ray axis;

	/**
	 * Constructs a tube with a given radius and axis ray.
	 *
	 * @param radius the radius of the tube
	 * @param ray    the central axis of the tube
	 */
	public Tube(double radius, Ray ray) {
		super(radius);
		axis = ray;
	}

	@Override
	public Vector getNormal(Point p) {
		double t = alignZero(axis.getDir().dotProduct(p.subtract(axis.getHead())));
		Point o = axis.getPoint(t);
		return p.subtract(o).normalize();

	}

	@Override
	protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
		return null;
	}

}

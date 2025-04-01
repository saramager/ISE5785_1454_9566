package geometries;

import java.util.List;

import primitives.*;

/**
 * Represents a triangle in 3D space. A triangle is a specific case of a
 * {@link Polygon} with exactly three vertices.
 */
public class Triangle extends Polygon {

	/**
	 * Constructs a triangle using three points.
	 * 
	 * @param p1 the first vertex of the triangle
	 * @param p2 the second vertex of the triangle
	 * @param p3 the third vertex of the triangle
	 */
	public Triangle(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);
	}

	@Override
	public List<Point> findIntersections(Ray ray) {

		List lPoints = super.findIntersections(ray);
		if (lPoints == null)
			return null;
		Vector v1 = this.vertices.get(0).subtract(ray.getHead());
		Vector v2 = this.vertices.get(1).subtract(ray.getHead());
		Vector v3 = this.vertices.get(2).subtract(ray.getHead());

		Vector normal12 = v1.crossProduct(v2).normalize();
		Vector normal13 = v1.crossProduct(v3).normalize();
		Vector normal23 = v2.crossProduct(v3).normalize();

		Vector rdir = ray.getDir();
		double nv12 = Util.alignZero(normal12.dotProduct(rdir));
		double nv13 = Util.alignZero(normal13.dotProduct(rdir));
		double nv23 = Util.alignZero(normal23.dotProduct(rdir));

		if (nv12 > 0 && nv13 > 0 && nv23 > 0)
			return lPoints;
		return null;
	}
}

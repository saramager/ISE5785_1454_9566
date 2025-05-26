package primitives;

import java.util.List;

import geometries.Intersectable.Intersection;

/**
 * Represents a ray in 3D space, defined by a starting point (head) and a
 * direction vector. The direction vector is always normalized upon creation.
 */
public class Ray {

	/** The starting point of the ray. */
	private final Point head;

	/** The direction vector of the ray (normalized). */
	private final Vector direction;
	/**
	 * A small constant used to avoid floating-point precision issues.
	 */
	private static final double DELTA = 0.1;

	/**
	 * Constructs a ray with a given starting point and direction vector. The
	 * direction vector is normalized upon initialization.
	 *
	 * @param point  the starting point of the ray
	 * @param vector the direction vector of the ray (will be normalized)
	 */
	public Ray(Point point, Vector vector) {
		direction = vector.normalize();
		head = point;
	}

	/**
	 * Constructs a ray with a given starting point, direction vector, and normal
	 * vector. The direction vector is normalized, and the head of the ray is
	 * adjusted slightly away from the surface to avoid floating-point precision
	 * issues.
	 *
	 * @param head      the starting point of the ray
	 * @param direction the direction vector of the ray (will be normalized)
	 * @param normal    the normal vector at the surface point (used to adjust the
	 *                  head)
	 */
	public Ray(Point head, Vector direction, Vector normal) {
		this.direction = direction.normalize();
		double nv = normal.dotProduct(this.direction);

		// Add a small delta to the ray's origin to avoid floating-point precision
		// issues

		this.head = !Util.isZero(nv) ? head.add(normal.scale(nv > 0 ? DELTA : -DELTA)) : head;
	}

	/**
	 * Returns the head of the object (typically the leading point).
	 * 
	 * @return The head of the object as a Point.
	 */
	public Point getHead() {
		return head;
	}

	/**
	 * Returns the direction of the object as a vector.
	 * 
	 * @return The direction of the object as a Vector.
	 */
	public Vector getDir() {
		return direction;
	}

	/**
	 * Get point on the ray at a given distance from the head.
	 * 
	 * @param t the distance from the head of the ray
	 * @return The point of the ray at distance t from the head.
	 */
	public Point getPoint(double t) {
		try {
			return head.add(direction.scale(t));
		} catch (IllegalArgumentException ignored) {
			return head;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (obj instanceof Ray other) && head.equals(other.head) && direction.equals(other.direction);
	}

	@Override
	public String toString() {
		return "" + head + direction;
	}

	/**
	 * Finds the closest point to the ray's head from a list of points.
	 *
	 * @param points the list of points to search
	 * @return the closest point to the ray's head, or null if the list is empty
	 */
	public Point findClosestPoint(List<Point> points) {
		return points == null ? null
				: findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
	}

	/**
	 * Finds the closest intersection point from a list of intersections.
	 *
	 * @param points the list of intersections to search
	 * @return the closest intersection point to the ray's head, or null if the list
	 *         is empty
	 */
	public Intersection findClosestIntersection(List<Intersection> points) {
		if (points == null) {
			return null;
		}

		Intersection closestIntersection = null;
		double minDistance = Double.POSITIVE_INFINITY;

		for (Intersection intersection : points) {
			double distance = head.distance(intersection.point);
			if (distance < minDistance) {
				minDistance = distance;
				closestIntersection = intersection;
			}
		}

		return closestIntersection;
	}

}

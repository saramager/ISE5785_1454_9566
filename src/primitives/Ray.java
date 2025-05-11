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
	 * refrecser help claclution
	 * 
	 * @param t the number to help calclution
	 * @return The point of the ray + t*ray direction
	 */
	public Point getPoint(double t) {
		Point toReturn = head;
		if (!Util.isZero(t))
			toReturn = toReturn.add(direction.scale(t));
		return toReturn;

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
				: findClosestIntersection(points.stream().map(p -> new Intersection(null, p, null)).toList()).point;
	}

	/**
	 * Finds the closest intersection point from a list of intersections.
	 *
	 * @param points the list of intersections to search
	 * @return the closest intersection point to the ray's head, or null if the list
	 *         is empty
	 */
	public Intersection findClosestIntersection(List<Intersection> points) {
		if (points == null || points.isEmpty()) {
			return null;
		}

		Intersection closestIntersection = null;
		double minDistance = Double.MAX_VALUE;

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

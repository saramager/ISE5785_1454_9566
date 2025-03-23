package primitives;

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
		this.direction = vector.normalize();
		this.head = point;
	}

	/**
	 * Checks if this ray is equal to another object.
	 *
	 * @param obj the object to compare
	 * @return {@code true} if the object is a {@link Ray} with the same head and
	 *         direction, otherwise {@code false}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (obj instanceof Ray other) && this.head.equals(other.head) && this.direction.equals(other.direction);
	}

	/**
	 * Returns a string representation of the ray.
	 *
	 * @return a string containing the ray's head and direction
	 */
	@Override
	public String toString() {
		return this.head.toString() + this.direction.toString();
	}
}

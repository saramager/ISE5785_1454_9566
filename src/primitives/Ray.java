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
	 * Returns the head of the object (typically the leading point).
	 * @return The head of the object as a Point.
	 */
	public Point getHead() {
	    return this.head;
	}

	/**
	 * Returns the direction of the object as a vector.
	 * @return The direction of the object as a Vector.
	 */
	public Vector getDir() {
	    return this.direction;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (obj instanceof Ray other) && this.head.equals(other.head) && this.direction.equals(other.direction);
	}

	@Override
	public String toString() {
		return "" + this.head + this.direction;
	}
}

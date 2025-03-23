package primitives;

/**
 * Represents a point in 3D space. A point is immutable and defined by three
 * coordinates (x, y, z).
 */
public class Point {

	/** The three coordinates of the point. */
	final protected Double3 xyz;

	/** Constant for the origin point (0,0,0). */
	public static final Point ZERO = new Point(0, 0, 0);

	/**
	 * Constructs a point with given x, y, and z coordinates.
	 *
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param z the z-coordinate
	 */
	public Point(double x, double y, double z) {
		xyz = new Double3(x, y, z);
	}

	/**
	 * Constructs a point from a {@link Double3} object.
	 *
	 * @param d3 the {@link Double3} containing the coordinates
	 */
	public Point(Double3 d3) {
		xyz = d3;
	}

	/**
	 * Checks if this point is equal to another object.
	 *
	 * @param obj the object to compare
	 * @return {@code true} if the object is a {@link Point} with the same
	 *         coordinates, otherwise {@code false}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (obj instanceof Point other) && this.xyz.equals(other.xyz);
	}

	/**
	 * Returns a string representation of the point.
	 *
	 * @return a string in the format "Point:x,y,z"
	 */
	@Override
	public String toString() {
		return "Point:" + this.xyz.toString();
	}

	/**
	 * Adds a vector to this point and returns a new point.
	 *
	 * @param vec the vector to add
	 * @return a new point resulting from the addition
	 */
	public Point add(Vector vec) {
		return new Point(xyz.add(vec.xyz));
	}

	/**
	 * Subtracts another point from this point and returns the resulting vector.
	 *
	 * @param p the point to subtract
	 * @return the vector from the given point to this point
	 */
	public Vector subtract(Point p) {
		return new Vector(xyz.subtract(p.xyz));
	}

	/**
	 * Computes the Euclidean distance between this point and another point.
	 *
	 * @param p the other point
	 * @return the distance between the two points
	 */
	public double distance(Point p) {
		return Math.sqrt(this.distanceSquared(p));
	}

	/**
	 * Computes the squared Euclidean distance between this point and another point.
	 * This method avoids the cost of computing the square root.
	 *
	 * @param p the other point
	 * @return the squared distance between the two points
	 */
	public double distanceSquared(Point p) {
		return (this.xyz.d1() - p.xyz.d1()) * (this.xyz.d1() - p.xyz.d1())
				+ (this.xyz.d2() - p.xyz.d2()) * (this.xyz.d2() - p.xyz.d2())
				+ (this.xyz.d3() - p.xyz.d3()) * (this.xyz.d3() - p.xyz.d3());
	}
}

package geometries;

import primitives.*;

/**
 * Represents a cylinder, which extends the {@link Tube} class.
 */
public class Cylinder extends Tube {
	/**
	 * The height of the cylinder.
	 */
	private final double height;

	/**
	 * Constructs a Cylinder object with a given radius, axis ray, and height.
	 * 
	 * @param radius the radius of the cylinder
	 * @param ray    the axis ray of the cylinder
	 * @param height the height of the cylinder
	 */
	public Cylinder(double radius, Ray ray, double height) {
		super(radius, ray);
		this.height = height;
	}

	@Override
	public Vector getNormal(Point p) {
		/**
		 * Returns the normal vector to the cylinder at a given point. Assumes the point
		 * is on the cylinder's surface.
		 * 
		 * @param p the point on the cylinder
		 * @return the normal vector at the given point
		 * @throws IllegalArgumentException if a zero vector is encountered
		 */

		Vector axisDir = axis.getDir();
		Point base1 = axis.getHead();
		Point base2 = base1.add(axisDir.scale(height));
		boolean onBase1;
		boolean onBase2;

		// Check if the point is on one of the bases
		try {
			onBase1 =Util.isZero(p.subtract(base1).dotProduct(axisDir));

			onBase2 = Util.isZero(p.subtract(base2).dotProduct(axisDir));
		} catch (IllegalArgumentException e) {
			return axis.getDir().normalize();
		}

		if (onBase1 || onBase2) {
			// If at the center, return the axis direction
			if (p.equals(base1))
				return axisDir.scale(-1);
			if (p.equals(base2))
				return axisDir;

			// If on the edge, treat it as a lateral point
			if (p.subtract(onBase1 ? base1 : base2).lengthSquared() == this.radiusPow2)
				return super.getNormal(p);

			// Otherwise, return the base normal
			return onBase1 ? axisDir.scale(-1) : axisDir;
		}

		// Default case: point is on the lateral surface
		return super.getNormal(p);
	}
}

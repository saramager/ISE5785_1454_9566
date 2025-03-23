package geometries;

import primitives.Ray;

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
}

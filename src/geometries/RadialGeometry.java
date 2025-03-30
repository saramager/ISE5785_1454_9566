package geometries;

/**
 * Represents a radial geometric shape in 3D space, characterized by a radius.
 * This is an abstract class that serves as a base for shapes like spheres,
 * cylinders, and tubes.
 */
public abstract class RadialGeometry extends Geometry {

	/**
	 * The radius of the geometric shape.
	 */
	protected final double radius;
	/**
	 * the radius by the power of 2
	 */
	protected final double radiusPow2;

	/**
	 * Constructs a radial geometry object with a given radius.
	 *
	 * @param radius the radius of the shape
	 */
	public RadialGeometry(double radius) {
		this.radius = radius;
		radiusPow2 = radius * radius;
	}

}

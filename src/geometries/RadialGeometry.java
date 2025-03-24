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
	 * Constructs a radial geometry object with a given radius.
	 *
	 * @param radius the radius of the shape
	 */
	public RadialGeometry(double radius) {
		this.radius = radius;
	}

}

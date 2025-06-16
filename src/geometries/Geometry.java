package geometries;

import primitives.*;

/**
 * Abstract class representing a geometric shape in 3D space.
 */
public abstract class Geometry extends Intersectable {

	/**
	 * The material of the geometry, which defines its surface properties.
	 */
	private Material material = new Material();

	/**
	 * The emission color of the geometry, which is the color emitted by the
	 * geometry itself.
	 */
	Color emission = Color.BLACK;

	/**
	 * Calculates and returns the normal vector to the geometry at a given point on
	 * the geometry surface .
	 *
	 * @param p the point at which to calculate the normal
	 * @return the normal vector to the geometry at the given point
	 */
	public abstract Vector getNormal(Point p);

	/**
	 * Sets the emission color of the geometry.
	 *
	 * @param emission the new emission color
	 * @return the current geometry object with the updated emission color
	 */
	public Geometry setEmission(Color emission) {
		this.emission = emission;
		return this;
	}

	/**
	 * Set the material of the geometry.
	 * 
	 * @param material the new material
	 * @return the material
	 */
	public Geometry setMaterial(Material material) {
		this.material = material;
		return this;
	}

	/**
	 * Gets the emission color of the geometry.
	 * 
	 * @return the emission color
	 */
	public Color getEmission() {
		return emission;
	}

	/**
	 * Gets the material of the geometry.
	 * 
	 * @return the material
	 */
	public Material getMaterial() {
		return material;
	}

}

/**
 * 
 */
package geometries;

import primitives.*;
import java.util.*;

/**
 * The Geometries class implements the Intersectable interface using the
 * Composite design pattern. It represents a collection of geometric objects
 * that can be intersected by a ray.
 */
public class Geometries implements Intersectable {
	/**
	 * A private, immutable list of intersectable geometries. The list is
	 * initialized as an empty LinkedList at declaration.
	 */
	private final List<Intersectable> geometries = new LinkedList<>();

	/**
	 * Default constructor. Initializes an empty collection of geometries.
	 */
	public Geometries() {
	}

	/**
	 * Constructor that initializes the collection with given geometries. Uses the
	 * add method to avoid code duplication.
	 *
	 * @param geometries One or more Intersectable objects to be added to the
	 *                   collection.
	 */
	public Geometries(Intersectable... geometries) {
		add(geometries);
	}

	/**
	 * Adds one or more geometries to the collection.
	 *
	 * @param geometries One or more Intersectable objects to be added.
	 */
	public void add(Intersectable... geometries) {
		for (Intersectable geo : geometries) {
			this.geometries.add(geo);
		}
	}

	/**
	 * Finds intersections of a given ray with the geometries in the collection.
	 * Currently, this method returns null as a placeholder implementation.
	 *
	 * @param ray The ray to check for intersections.
	 * @return A list of intersection points or null (placeholder implementation).
	 */
	@Override
	public List<Point> findIntersections(Ray ray) {
		return null; // Placeholder implementation
	}
}

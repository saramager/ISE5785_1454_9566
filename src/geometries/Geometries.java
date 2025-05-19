/**
 * 
 */
package geometries;

import java.util.LinkedList;
import java.util.List;

import primitives.Ray;

/**
 * The Geometries class implements the Intersectable interface using the
 * Composite design pattern. It represents a collection of geometric objects
 * that can be intersected by a ray.
 */
public class Geometries extends Intersectable {
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
		this.geometries.addAll(List.of(geometries));
	}

	@Override
	protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
		List<Intersection> intersections = null;
		for (Intersectable geo : geometries) {
			var geometryIntersections = geo.calculateIntersections(ray, maxDistance);
			if (geometryIntersections != null) {
				if (intersections == null)
					intersections = new LinkedList<>(geometryIntersections);
				else
					intersections.addAll(geometryIntersections);
			}

		}
		return intersections;
	}
}

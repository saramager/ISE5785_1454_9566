/**
 * 
 */
package geometries;

import java.util.LinkedList;
import java.util.List;

import primitives.Double3;
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
		Double3 globalMin = new Double3(Double.POSITIVE_INFINITY);
		Double3 globalMax = new Double3(Double.NEGATIVE_INFINITY);
		for (Intersectable element : this.geometries) {
			List<Double3> edges = element.edges;
			Double3 min = edges.get(0);
			Double3 max = edges.get(1);
			if (min.d1() == Double.NEGATIVE_INFINITY || min.d2() == Double.NEGATIVE_INFINITY
					|| min.d3() == Double.NEGATIVE_INFINITY)
				continue;
			if (max.d1() == Double.POSITIVE_INFINITY || max.d2() == Double.POSITIVE_INFINITY
					|| max.d3() == Double.POSITIVE_INFINITY)
				continue;
			globalMin = new Double3(Math.min(globalMin.d1(), min.d1()), Math.min(globalMin.d2(), min.d2()),
					Math.min(globalMin.d3(), min.d3()));

			globalMax = new Double3(Math.max(globalMax.d1(), max.d1()), Math.max(globalMax.d2(), max.d2()),
					Math.max(globalMax.d3(), max.d3()));
		}
		this.edges = List.of(globalMin, globalMax);
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

	public List<Intersectable> getGeometries() {
		return geometries;
	}

}

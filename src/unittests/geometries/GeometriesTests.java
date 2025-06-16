/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * test for the Geometries class
 */
class GeometriesTests {

	/**
	 * Initialize geometries for testing Triangle is placed at (1,1,1), (2,1,1),
	 * (1,2,1)
	 */
	Triangle triangle = new Triangle(new Point(1, 1, 1), new Point(2, 1, 1), new Point(1, 2, 1));
	/**
	 * Plane is located at (1,1,0) with a normal (0,0,1)
	 */
	Plane plane = new Plane(new Point(1, 1, 0), new Vector(0, 0, 1));
	/**
	 * Sphere is centered at (3,3,3) with radius 1
	 */
	Sphere sphere = new Sphere(new Point(3, 3, 3), 1.0);
	/**
	 * the gematry of all shaps
	 */

	Geometries geometries = new Geometries(triangle, plane, sphere);

	/*
	 * /** S Test method for {@link
	 * geometries.Geometries#add(geometries.Intersectable[])}.
	 */
	/*
	 * @Test void testAdd() { fail("Not yet implemented"); }
	 */

	/**
	 * Test method for
	 * {@link geometries.Geometries#findIntersections(primitives.Ray)}.
	 */
	@Test
	void testFindIntersections() {

		// ============ Equivalence Partitions Tests (EP) ==============

		// TC01: Ray intersects two geometries (the plane and the sphere, but not the
		// triangle)

		Ray ray = new Ray(new Point(0, 0, -1), new Vector(1, 1, 1));
		List<Point> intersections = geometries.findIntersections(ray);
		assertEquals(3, intersections.size(), "Expected two intersections");
		// assertEquals(List.of(new Point(3, 3, 2), new Point(3.65, 3.65, 2.65), new
		// Point(1, 1, 0)), intersections,
		// "the point are not the same");

		// ============ Boundary Value Analysis (BVA) ==============

		// TC11: - Empty collection (no intersections expected)
		Geometries emptyGeometries = new Geometries();
		ray = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
		assertNull(emptyGeometries.findIntersections(ray), "Expected no intersections for an empty collection");

		// TC12: Ray does not intersect any geometry
		ray = new Ray(new Point(5, 5, 5), new Vector(1, 1, 1));
		assertNull(geometries.findIntersections(ray), "Expected no intersections when ray misses all geometries");

		// TC13: Ray intersects only the sphere
		ray = new Ray(new Point(3, 3, 0), new Vector(0, 0, 1));
		intersections = geometries.findIntersections(ray);
		assertEquals(2, intersections.size(), "Expected two intersections (entry and exit)");
		assertEquals(List.of(new Point(3, 3, 2), new Point(3, 3, 4)), intersections, "the point are not the same");

		// TC14: Ray intersects all
		// TODO:

		// .geometries = new Geometries(triangle, plane, sphere);
		/*
		 * assertEquals(4, geometries.findIntersections(new Ray(new Point(0.6, 0.6, -2),
		 * new Vector(0, 0, 1))).size(), "all geometries are intersected");
		 */
		var result = geometries.findIntersections(new Ray(new Point(0.06, -0.38, -1), new Vector(1, 1.13, 1.39)));
		assertEquals(4, result.size(), "all geometries are intersected");
	}

}

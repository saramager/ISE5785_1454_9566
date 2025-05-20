/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.Intersectable.Intersection;
import geometries.Triangle;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Test Junit for geometries.Triangle
 */
class TriangleTests {

	/**
	 * Test method for {@link geometries.Triangle#getNormal(Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		Point p1 = new Point(1, 0, 0);
		Point p2 = new Point(0, 2, 0);
		Point p3 = new Point(0, 0, 3);

		Triangle triangle = new Triangle(p1, p2, p3);

		// ============ TC01: Test that the normal vector is calculated correctly
		Vector expectedNormal = new Vector(6, 3, 2);

		assertThrows(IllegalArgumentException.class,
				() -> triangle.getNormal(new Point(0, 1, 1)).crossProduct(expectedNormal),
				"The normal vector is not in the right direction");

		assertEquals(1, triangle.getNormal(new Point(1, 0, 0)).length(), 1e-10, "The normal vector  isn't normalize  ");

	}

	/**
	 * Test method for {@link geometries.Triangle#findIntersections(Ray)}
	 */
	@Test
	public void testfindIntersections() {

		// Create a triangle that is not aligned with any axis
		Triangle triangle = new Triangle(new Point(1, 1, 1), new Point(3, 2, 0), new Point(2, 3, 2));

		// Expected intersection point inside the triangle
		final Point p1 = new Point(2, 2, 1);
		final List<Point> expectedIntersection = List.of(p1);

		// ============ Equivalence Partitions Tests ==============

		// TC01: Ray intersects inside the triangle (1 point)
		final Ray ray1 = new Ray(new Point(2, 2, -1), new Vector(0, 0, 1));
		final var result1 = triangle.findIntersections(ray1);
		assertEquals(1, result1.size(), "Wrong number of intersection points");
		assertEquals(expectedIntersection, result1, "Incorrect intersection point");

		// TC02: Ray misses the triangle - in front of an edge (0 points)
		assertNull(triangle.findIntersections(new Ray(new Point(3, 0, -1), new Vector(0, 1, 1))),
				"Ray should miss the triangle when facing an edge");

		// TC03: Ray misses the triangle - in front of a vertex (0 points)
		assertNull(triangle.findIntersections(new Ray(new Point(4, 2, -1), new Vector(0, 1, 1))),
				"Ray should miss the triangle when facing a vertex");

		// =============== Boundary Values Tests ================

		// TC04: Ray intersects exactly on an edge (0 or 1 point depending on
		// definition)
		final Ray ray4 = new Ray(new Point(2.5, 1.5, -1), new Vector(0, 0, 1));
		assertNull(triangle.findIntersections(ray4), "Ray hitting an edge should not be considered an intersection");

		// TC05: Ray intersects exactly on a vertex (0 points)
		assertNull(triangle.findIntersections(new Ray(new Point(3, 2, -1), new Vector(0, 0, 1))),
				"Ray hitting a vertex should not be considered an intersection");

		// TC06: Ray intersects on the extension of an edge (0 points)
		assertNull(triangle.findIntersections(new Ray(new Point(4, 3, -1), new Vector(-1, -1, 1))),
				"Ray hitting the extension of an edge should not be considered an intersection");

	}

	/**
	 * Test method for {@link geometries.Triangle#calculateIntersections(Ray)}.
	 */
	@Test
	void testTriangleCalculateIntersectionsWithMaxDistance() {
		// Create a triangle in the XY plane
		Triangle triangle = new Triangle(new Point(0, 0, 0), new Point(2, 0, 0), new Point(0, 2, 0));

		Vector dir = new Vector(0, 0, 1); // Direction straight towards the triangle (from -Z)

		// TC1: Ray starts exactly at the intersection point (distance = 0) → 1 point
		Ray ray1 = new Ray(new Point(0.5, 0.5, 0), dir);
		List<Intersection> result1 = triangle.calculateIntersections(ray1, 0);
		assertNull(result1, "TC1: Expected 0 intersection");

		// TC2: Ray intersects just beyond max distance (point at 0.11, maxDistance =
		// 0.1) → no intersection
		Ray ray2 = new Ray(new Point(0.5, 0.5, -0.11), dir);
		assertNull(triangle.calculateIntersections(ray2, 0.1), "TC2: Expected no intersection just beyond maxDistance");

		// TC3: Ray intersects at exact max distance → 1 point
		Ray ray3 = new Ray(new Point(0.5, 0.5, -1), dir);
		List<Intersection> result3 = triangle.calculateIntersections(ray3, 1.0);
		assertEquals(1, result3.size(), "TC3: Expected 1 intersection at exact maxDistance");

		// TC4: Ray intersects just below max distance → 1 point
		Ray ray4 = new Ray(new Point(0.5, 0.5, -0.99), dir);
		List<Intersection> result4 = triangle.calculateIntersections(ray4, 1.0);
		assertEquals(1, result4.size(), "TC4: Expected 1 intersection just below maxDistance");

		// TC5: Ray intersects just above max distance → no intersection
		Ray ray5 = new Ray(new Point(0.5, 0.5, -1.01), dir);
		assertNull(triangle.calculateIntersections(ray5, 1.0), "TC5: Expected no intersection just above maxDistance");

		// TC6: Ray intersects at a very far point, maxDistance is small → no
		// intersection
		Ray ray6 = new Ray(new Point(0.5, 0.5, -10), dir);
		assertNull(triangle.calculateIntersections(ray6, 1.0),
				"TC6: Expected no intersection due to very large distance");
	}
}

/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;
import geometries.Triangle;
import java.util.List;

import org.junit.jupiter.api.Test;

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
}

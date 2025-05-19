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
import geometries.Plane;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Test Junit for geometries.Plane
 */
class PlaneTests {

	/**
	 * Test method for
	 * {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
	 */
	@Test
	void testConstructorThreePoints() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Correct plane creation from three valid points
		Point p1 = new Point(1, 2, 3);
		Point p2 = new Point(2, 3, 5);
		Point p3 = new Point(4, 0, 6);

		Plane plane = new Plane(p1, p2, p3);
		Vector normal = plane.getNormal(p1);

		// Check if normal is perpendicular to at least two different vectors
		Vector v1 = p2.subtract(p1);
		Vector v2 = p3.subtract(p1);

		assertEquals(0, normal.dotProduct(v1), 1e-10, "Normal is not perpendicular to first vector");
		assertEquals(0, normal.dotProduct(v2), 1e-10, "Normal is not perpendicular to second vector");

		// Check if normal length is 1
		assertEquals(1, normal.length(), "Normal is not a unit vector");

		// =============== Boundary Values Tests ==================

		// TC11: Two points coincide (first and second)
		assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p3),
				"Constructor does not throw exception when first and second points coincide");

		// TC12: Two points coincide (first and third)
		assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p1),
				"Constructor does not throw exception when first and third points coincide");

		// TC13: Two points coincide (second and third)
		assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p2),
				"Constructor does not throw exception when second and third points coincide");

		// TC14: All three points coincide
		assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1),
				"Constructor does not throw exception when all three points coincide");

		// TC15: All three points are collinear
		Point pCol2 = new Point(2, 4, 6); // collinear to P1
		Point pCol3 = new Point(3, 6, 9); // collinear to P1

		assertThrows(IllegalArgumentException.class, () -> new Plane(p1, pCol2, pCol3),
				"Constructor does not throw exception when all three points are collinear");
	}

	/**
	 * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		Point p1 = new Point(1, 0, 0);
		Point p2 = new Point(0, 2, 0);
		Point p3 = new Point(0, 0, 3);
		Plane plane = new Plane(p1, p2, p3);

		// ============ TC01: Test that the normal vector is calculated correctly
		Vector expectedNormal = new Vector(6, 3, 2);
		assertThrows(IllegalArgumentException.class,
				() -> plane.getNormal(new Point(1, 0, 0)).crossProduct(expectedNormal),
				"The normal vector is not in the right direction");

		assertEquals(1, plane.getNormal(new Point(1, 0, 0)).length(), 1e-10, "The normal vector isn't normalized");
	}

	/**
	 * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
	 */
	@Test
	public void testFindIntsersections() {

		Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 3)); // XY Plane

		// ============ Equivalence Partitions Tests ==============

		// TC01: Ray's line is outside the plane (0 points)
		assertNull(plane.findIntersections(new Ray(new Point(1, 1, -1), new Vector(0, 0, -1))),
				"Ray should not intersect the plane");

		// TC02: Ray starts before and crosses the plane (1 point)
		final Ray ray1 = new Ray(new Point(1, 1, -1), new Vector(0, 0, 1));
		final var result1 = plane.findIntersections(ray1);
		assertEquals(1, result1.size(), "Wrong number of points");
		assertEquals(new Point(1, 1, 1), result1.get(0), "Ray crosses the plane at wrong point");

		// =============== Boundary Values Tests ================

		// TC03: Ray is parallel to the plane (no intersection, 0 points)
		assertNull(plane.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, 0))),
				"Parallel ray should not intersect the plane");

		// TC04: Ray coincides with the plane (should return null or empty list)
		assertNull(plane.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 1, 0))),
				"Ray lying in the plane should return null");

		// TC05: Ray is perpendicular to the plane and starts before it (1 point)
		final Ray ray5 = new Ray(new Point(1, 1, -1), new Vector(0, 0, 1));
		final var result5 = plane.findIntersections(ray5);
		assertEquals(1, result5.size(), "Wrong number of points");
		assertEquals(new Point(1, 1, 1), result5.get(0), "Perpendicular ray intersects at wrong point");

		// TC06: Ray is perpendicular to the plane and starts on it (0 points)
		assertNull(plane.findIntersections(new Ray(new Point(1, 1, 1), new Vector(0, 0, 1))),
				"Perpendicular ray starting from the plane should return null");

		// TC07: Ray is perpendicular to the plane and starts after it (0 points)
		assertNull(plane.findIntersections(new Ray(new Point(1, 1, 3), new Vector(0, 0, 1))),
				"Perpendicular ray starting after the plane should not intersect");

		// TC08: Ray starts in the plane but is not perpendicular (0 points)
		assertNull(plane.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 1, 0))),
				"Ray starting in the plane but not perpendicular should return null");

		// TC09: Ray starts from a reference point on the plane and moves in any
		// direction
		final Ray ray9 = new Ray(new Point(0, 0, 0), new Vector(1, 1, -1)); // Starts from the plane
		assertNull(plane.findIntersections(ray9), "Ray starting from the plane should return null");

	}

	@Test
	void testPlaneCalculateIntersectionsWithMaxDistance() {
		// Plane in XY plane (Z = 0)
		Plane plane = new Plane(new Point(0, 0, 0), new Vector(0, 0, 1));

		Vector dir = new Vector(0, 0, 1); // Upwards toward the plane

		// TC1: Intersection exactly at distance = 1 → 1 point
		Ray ray1 = new Ray(new Point(0, 0, -1), dir);
		List<Intersection> result1 = plane.calculateIntersections(ray1, 1.0);
		assertEquals(1, result1.size(), "TC1: Expected 1 intersection at exact maxDistance");

		// TC2: Intersection just below max distance → 1 point
		Ray ray2 = new Ray(new Point(0, 0, -0.99), dir);
		List<Intersection> result2 = plane.calculateIntersections(ray2, 1.0);
		assertEquals(1, result2.size(), "TC2: Expected 1 intersection just below maxDistance");

		// TC3: Intersection just above max distance → no intersection
		Ray ray3 = new Ray(new Point(0, 0, -1.01), dir);
		assertNull(plane.calculateIntersections(ray3, 1.0), "TC3: Expected no intersection just above maxDistance");

		// TC4: Ray starts exactly on the plane → distance 0
		Ray ray4 = new Ray(new Point(0, 0, 0), dir);
		List<Intersection> result4 = plane.calculateIntersections(ray4, 0);
		assertNull(result4, "TC4: Expected 0 intersection at distance 0");

		// TC5: Ray intersects far beyond distance → no intersection
		Ray ray5 = new Ray(new Point(0, 0, -10), dir);
		assertNull(plane.calculateIntersections(ray5, 1.0), "TC5: Expected no intersection (intersection too far)");

		// TC6: Ray opposite to plane normal → no intersection (backwards)
		Ray ray6 = new Ray(new Point(0, 0, -1), new Vector(0, 0, -1));
		assertNull(plane.calculateIntersections(ray6, 5.0), "TC6: Expected no intersection (wrong direction)");
	}
}

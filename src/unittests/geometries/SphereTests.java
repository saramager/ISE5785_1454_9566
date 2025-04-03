/**
 * 
 */
package unittests.geometries;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import geometries.Sphere;
import primitives.*;
import primitives.Vector;
import org.junit.jupiter.api.Test;

/**
 * Test Junit for geometries.Sphere
 *
 */
class SphereTests {

	/** A point used in some tests */
	private final Point p001 = new Point(0, 0, 1);
	/** A point used in some tests */
	private final Point p100 = new Point(1, 0, 0);
	/** A vector used in some tests */
	private final Vector v001 = new Vector(0, 0, 1);

	/**
	 * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Correct normal to the Sphere
		Sphere sphere = new Sphere(new Point(1, 1, 1), 10.0);
		assertEquals(1, sphere.getNormal(new Point(1, 1, 11)).length(), "get Normal doen't work as expected ");
	}

	/**
	 * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
	 */
	@Test
	public void testFindIntersections() {
		Sphere sphere = new Sphere(p100, 1d);
		final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
		final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
		final var exp = List.of(gp1, gp2);
		final Vector v310 = new Vector(3, 1, 0);
		final Vector v110 = new Vector(1, 1, 0);
		final Point p01 = new Point(-1, 0, 0);

		// ============ Equivalence Partitions Tests ==============
		// TC01: Ray's line is outside the sphere (0 points)
		assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");

		// TC02: Ray starts before and crosses the sphere (2 points)
		var result = sphere.findIntersections(new Ray(p01, v310));
		assertEquals(2, result.size(), "Wrong number of points");
		assertEquals(exp, result, "Ray crosses sphere");

		// TC03: Ray starts inside the sphere (1 point)
		final Point pInside = new Point(0.5, 0, 0); // Inside the sphere
		final Vector vInside = new Vector(1, 1, 0); // Moving outwards
		result = sphere.findIntersections(new Ray(pInside, vInside));
		assertEquals(1, result.size(), "Wrong number of points");
		assertEquals(new Point(1.4114378277661475, 0.9114378277661476, 0), result.getFirst(),
				"Ray starts inside and goes out");

		// TC04: Ray starts after the sphere (0 points)
		final Point pAfter = new Point(3, 0, 0); // Ray starts far from the sphere
		final Vector vAfter = new Vector(1, 0, 0); // Going away from the sphere
		assertNull(sphere.findIntersections(new Ray(pAfter, vAfter)), "Ray should not intersect");

		// =============== Boundary Values Tests ==================
		// **** Group 1: Ray's line crosses the sphere (but not the center)
		// TC11: Ray starts at sphere and goes inside (1 point)
		final Point pOnSphere = new Point(2, 0, 0); // On the surface of the sphere
		final Vector vInward = new Vector(-1, 1, 0); // Going inside
		result = sphere.findIntersections(new Ray(pOnSphere, vInward));
		assertEquals(1, result.size(), "Wrong number of points");
		assertEquals(List.of(new Point(1, 1, 0)), result, "Wrong number of points");

		// TC12: Ray starts at sphere and goes outside (0 points)
		final Vector vOutward = new Vector(1, -1, 0); // Going outside the sphere
		assertNull(sphere.findIntersections(new Ray(pOnSphere, vOutward)), "Ray should not intersect");

		// **** Group 2: Ray's line goes through the center
		// TC21: Ray starts before the sphere (2 points)
		final Point pBeforeCenter = new Point(-2, 0, 0); // Before the sphere
		final Vector vThroughCenter = new Vector(1, 0, 0); // Going through the center
		result = sphere.findIntersections(new Ray(pBeforeCenter, vThroughCenter));
		assertEquals(2, result.size(), "Wrong number of points");
		assertEquals(List.of(new Point(0, 0, 0), new Point(2, 0, 0)), result, "Ray crosses sphere");

		// TC22: Ray starts at sphere and goes inside (1 point)
		final Vector vInCenter = new Vector(-1, 0, 0);
		result = sphere.findIntersections(new Ray(pOnSphere, vInCenter));
		assertEquals(1, result.size(), "Wrong number of points");
		assertEquals(List.of(new Point(0, 0, 0)), result, "Ray crosses sphere");

		// TC23: Ray starts inside (1 point)
		final Vector vAway = new Vector(1, 0, 0); // Moving away from the sphere
		result = sphere.findIntersections(new Ray(pInside, vAway));
		assertEquals(1, result.size(), "Wrong number of points");
		assertEquals(List.of(new Point(2, 0, 0)), result, "Ray crosses sphere");

		// TC24: Ray starts at the center (1 point)
		final Point pCenter = new Point(1, 0, 0); // At the center
		result = sphere.findIntersections(new Ray(pCenter, vAway));
		assertEquals(1, result.size(), "Wrong number of points");
		assertEquals(List.of(new Point(2, 0, 0)), result, "Ray crosses sphere");

		// TC25: Ray starts at sphere and goes outside (0 points)
		final var result8 = sphere.findIntersections(new Ray(pOnSphere, vAway));
		assertNull(result8, "Ray should not intersect");

		// TC26: Ray starts after sphere (0 points)
		final Point pAfter2 = new Point(3, 0, 0); // After the sphere
		assertNull(sphere.findIntersections(new Ray(pAfter2, vAway)), "Ray should not intersect");

		// **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
		// TC31: Ray starts before the tangent point
		final Point pBeforeTangent = new Point(2, -1, 0); // Tangent point on bottom
		final Vector vTangentBefore = new Vector(0, 1, 0); // Moving upwards
		assertNull(sphere.findIntersections(new Ray(pBeforeTangent, vTangentBefore)), "Ray should not intersect");

		// TC32: Ray starts at the tangent point
		final Point pTangent = new Point(2, 0, 0); // Tangent point
		final Vector vTangent = new Vector(0, 1, 0); // Moving upward
		assertNull(sphere.findIntersections(new Ray(pTangent, vTangent)), "Ray should not intersect");

		// TC33: Ray starts after the tangent point
		final Point pAfterTangent = new Point(2, 2, 0); // After the tangent point
		final Vector vTangentAfter = new Vector(0, -1, 0); // Moving downward
		assertNull(sphere.findIntersections(new Ray(pAfterTangent, vTangentAfter)), "Ray should not intersect");

		// **** Group 4: Special cases
		// TC41: Ray's line is outside sphere, ray is orthogonal to ray start to
		// sphere's center line
		final Point pOutside = new Point(3, 0, 0); // Outside the sphere
		final Vector vOrthogonal = new Vector(0, 1, 0); // Orthogonal to center line
		assertNull(sphere.findIntersections(new Ray(pOutside, vOrthogonal)), "Ray should not intersect");

		// TC42: Ray starts inside, ray is orthogonal to ray start to sphere's center
		// line
		final Vector vOrthogonalInside = new Vector(0, 1, 0); // Moving orthogonal to center line
		result = sphere.findIntersections(new Ray(pInside, vOrthogonalInside));
		assertEquals(1, result.size(), "Wrong number of points");
		assertEquals(List.of(new Point(0.5, 0.8660254037844386, 0)), result, "Ray crosses sphere");

	}

}

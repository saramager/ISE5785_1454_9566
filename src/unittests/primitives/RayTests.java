/**
 * 
 */
package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * test for the Ray methodes
 */
class RayTests {

	/**
	 * Test method for {@link primitives.Ray#getPoint(double)}.
	 */
	@Test
	void testGetPoint() {
		Ray ray = new Ray(new Point(1, 2, 3), new Vector(0, 1, 0));

		// ============ Equivalence Partitions Tests ==============

		// TC01:Double is possitive number
		assertEquals(new Point(1, 4, 3), ray.getPoint(2), "ERROR: The check of get point isnt good ");

		// TC02: Double is negative number
		assertEquals(new Point(1, 1, 3), ray.getPoint(-1), "ERROR: The check of get point isnt good ");

		// =============== Boundary Values Tests ==================

		// TC10: t is Zero - return the head
		assertEquals(new Point(1, 2, 3), ray.getPoint(0), "ERROR: The check of get point isnt good ");

	}

	/**
	 * Test method for {@link primitives.Ray#findClosestPoint(List)}. This test
	 * includes one equivalence partition (EP) case and three boundary value
	 * analysis (BVA) cases.
	 */
	@Test
	public void testFindClosestPoint() {
		Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
		Point result = new Point(0.5, 0, 0);

		// ============ EP: Equivalence Partition ==============
		// TC01: The closest point is in the middle of the list
		List<Point> pointsEP = List.of(new Point(2, 0, 0), new Point(0.5, 0, 0), new Point(3, 0, 0));
		assertEquals(result, ray.findClosestPoint(pointsEP), "EP case - closest point in the middle");

		// ============ BVA: Boundary Value Analysis ==============

		// TC02: Null list -> expect null
		assertNull(ray.findClosestPoint(null), "BVA case - null list");

		// TC03: Closest point is the first in the list
		List<Point> pointsFirstClosest = List.of(new Point(0.5, 0, 0), new Point(2, 0, 0), new Point(3, 0, 0));
		assertEquals(result, ray.findClosestPoint(pointsFirstClosest), "BVA case - closest point is first");

		// TC04: Closest point is the last in the list
		List<Point> pointsLastClosest = List.of(new Point(3, 0, 0), new Point(4, 0, 0), new Point(0.5, 0, 0));
		assertEquals(result, ray.findClosestPoint(pointsLastClosest), "BVA case - closest point is last");
	}
}

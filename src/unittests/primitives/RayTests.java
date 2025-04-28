/**
 * 
 */
package unittests.primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
	 * Test method for {@link primitives.Ray#findClosestPoint(List)}.
	 */
	@Test
	public void testFindClosestPoint() {
		Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

		// Case 1: Multiple points
		List<Point> points = List.of(new Point(1, 0, 0), new Point(2, 0, 0), new Point(-1, 0, 0), new Point(0.5, 0, 0));
		assertEquals(new Point(0.5, 0, 0), ray.findClosestPoint(points));

		// Case 2: Empty list
		assertNull(ray.findClosestPoint(List.of()));

		// Case 3: Null list
		assertNull(ray.findClosestPoint(null));

		// Case 4: Single point
		List<Point> singlePoint = List.of(new Point(1, 1, 1));
		assertEquals(new Point(1, 1, 1), ray.findClosestPoint(singlePoint));

		// Case 5: Equidistant points
		List<Point> equidistantPoints = List.of(new Point(1, 1, 0), new Point(-1, -1, 0));
		assertEquals(new Point(1, 1, 0), ray.findClosestPoint(equidistantPoints));
	}
}

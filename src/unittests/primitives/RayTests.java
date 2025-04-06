/**
 * 
 */
package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.Cylinder;
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

}

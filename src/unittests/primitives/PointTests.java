/**
 * 
 */
package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
/**
 * 
 */
class PointTests {

	/**
	 * Test method for {@link primitives.Point#add(primitives.Vector)}.
	 */
	@Test
	void testAdd() {
		 // ============ Equivalence Partitions Tests ==============

        // TC01: Adding a vector to a point (regular case)
        Point p = new Point(1, 2, 3);
        Vector v = new Vector(2, 3, 4);
        assertEquals(new Point(3, 5, 7), p.add(v), "ERROR: Point + Vector does not work correctly");
	}

	/**
	 * Test method for {@link primitives.Point#subtract(primitives.Point)}.
	 */
	@Test
	void testSubtract() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link primitives.Point#distance(primitives.Point)}.
	 */
	@Test
	void testDistance() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
	 */
	@Test
	void testDistanceSquared() {
		fail("Not yet implemented");
	}

}

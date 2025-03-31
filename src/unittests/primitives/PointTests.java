package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

/**
 * Test Junit for primitives.Point
 */
class PointTests {
	/**
	 * p1 - point for testing
	 */
	Point p1 = new Point(5, 6, 7);
	/**
	 * p2 - point for testing
	 */
	Point p2 = new Point(2, 3, 4);
	/**
	 * v - Vector for testing
	 */
	Vector v = new Vector(2, 3, 4);
	/**
	 * vOpposite the opposite vector to p1
	 */
	Vector vOpposite = new Vector(-5, -6, -7);

	/**
	 * Test method for {@link primitives.Point#add(primitives.Vector)}.
	 */
	@Test
	void testAdd() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Adding a vector to a point (regular case)
		assertEquals(new Point(7, 9, 11), p1.add(v), "ERROR: Point + Vector does not work correctly");
		// =============== Boundary Values Tests ==================

		// TC02: Adding a opossite vector to a point
		assertEquals(Point.ZERO, p1.add(vOpposite), "ERROR: adding a point from itself need return Zero");
	}

	/**
	 * Test method for {@link primitives.Point#subtract(primitives.Point)}.
	 */
	@Test
	void testSubtract() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Subtracting one point from another (regular case)

		assertEquals(new Vector(3, 3, 3), p1.subtract(p2), "ERROR: Point - Point does not work correctly");

		// =============== Boundary Values Tests ==================

		// TC02: Subtracting a point from itself should throw an exception
		assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
				"ERROR: Subtracting a point from itself should throw an exception");
	}

	/**
	 * Test method for {@link primitives.Point#distance(primitives.Point)}.
	 */
	@Test
	void testDistance() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Distance between two different points
		assertEquals(Math.sqrt(27), p1.distance(p2), "ERROR: distance between two points is incorrect");

		// =============== Boundary Values Tests ==================

		// TC02: Distance between a point and itself should be zero
		assertEquals(0, p1.distance(p1), "ERROR: distance of a point from itself should be zero");

	}

	/**
	 * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
	 */
	@Test
	void testDistanceSquared() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Squared distance between two different points
		assertEquals(27, p1.distanceSquared(p2), "ERROR: squared distance between two points is incorrect");

		// =============== Boundary Values Tests ==================

		// TC02: Squared distance between a point and itself should be zero
		assertEquals(0, p1.distanceSquared(p1), "ERROR: squared distance of a point from itself should be zero");
	}

}

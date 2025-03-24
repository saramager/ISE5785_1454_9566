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
class VectorTests {
	
	
    Vector v1 = new Vector(2, 3, 4);
    Vector v2 = new Vector(1, 2, 3);

    Vector v1Opposite  = new Vector(-2,-3,-4);

	/**
	 * Test method for {@link primitives.Vector#add(primitives.Vector)}.
	 */
	@Test
	void testAddVector() {
		 // ============ Equivalence Partitions Tests ==============
        // TC01: Adding two vectors
        assertEquals(new Vector(3, 5, 7), v1.add(v2), "ERROR: Vector addition does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Adding a vector and its opposite should throw an exception
        assertThrows(IllegalArgumentException.class, () -> v1.add(v1Opposite), "ERROR: Adding a vector and its opposite should throw an exception");

	}

	/**
	 * Test method for {@link primitives.Vector#scale(double)}.
	 */
	@Test
	void testScale() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
	 */
	@Test
	void testDotProduct() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
	 */
	@Test
	void testCrossProduct() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link primitives.Vector#lengthSquared()}.
	 */
	@Test
	void testLengthSquared() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link primitives.Vector#length()}.
	 */
	@Test
	void testLength() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link primitives.Vector#normalize()}.
	 */
	@Test
	void testNormalize() {
		fail("Not yet implemented");
	}

}

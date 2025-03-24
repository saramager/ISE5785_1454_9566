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
		  // ============ Equivalence Partitions Tests ==============
        // TC01: Scaling a vector by a positive scalar
        assertEquals(new Vector(4, 6, 8), v1.scale(2), "ERROR: Vector scaling does not work correctly");

        // TC02: Scaling a vector by a negative scalar
        assertEquals(new Vector(-4, -6, -8), v1.scale(-2), "ERROR: Vector scaling by negative scalar is incorrect");

        // =============== Boundary Values Tests ==================
        // TC11: Scaling by zero should throw an exception
        assertThrows(IllegalArgumentException.class, () -> v1.scale(0), "ERROR: Scaling by zero should throw an exception");
	}

	/**
	 * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
	 */
	@Test
	void testDotProduct() {
		  // ============ Equivalence Partitions Tests ==============
        // TC01: Checking the dot product of two non-orthogonal vectors
        assertEquals(20, v1.dotProduct(v2), "ERROR: Dot product calculation is incorrect");

        // TC02: Checking the dot product of orthogonal vectors
        assertEquals(0, new Vector(1, 0, 0).dotProduct(new Vector(0, 1, 0)), "ERROR: Dot product of orthogonal vectors should be zero");
	}

	/**
	 * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
	 */
	@Test
	void testCrossProduct() {
		 // ============ Equivalence Partitions Tests ==============
        // TC01: Checking the cross product of two non-parallel vectors
        Vector cross = v1.crossProduct(v2);
        assertEquals(new Vector(1, -2, 1), cross, "ERROR: Cross product calculation is incorrect");

        // =============== Boundary Values Tests ==================
        // TC11: Cross product of parallel vectors should throw an exception
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v1.scale(2)), "ERROR: Cross product of parallel vectors should throw an exception");
	}

	/**
	 * Test method for {@link primitives.Vector#lengthSquared()}.
	 */
	@Test
	void testLengthSquared() {
		  // ============ Equivalence Partitions Tests ==============
        // TC01: Checking the squared length of a vector
        assertEquals(29, v1.lengthSquared(), "ERROR: Length squared calculation is incorrect");
        // =============== Boundary Values Tests ==================
       
	}
	/**
	 * Test method for {@link primitives.Vector#length()}.
	 */
	@Test
	void testLength() {
		  // ============ Equivalence Partitions Tests ==============
        // TC01: Checking the length of a vector
        assertEquals(Math.sqrt(29), v1.length(), "ERROR: Length calculation is incorrect");
	}

	/**
	 * Test method for {@link primitives.Vector#normalize()}.
	 */
	@Test
	void testNormalize() {
		   // ============ Equivalence Partitions Tests ==============
        // TC01: Normalizing a vector should result in a unit vector
        Vector normalized = v1.normalize();
        assertEquals(1, normalized.length(), "ERROR: Normalized vector is not of length 1");

        // TC02: Normalized vector should be in the same direction
        assertThrows(IllegalArgumentException.class,()->v1.crossProduct(normalized).lengthSquared(), "ERROR: Normalized vector is not in the same direction");
      

	}

}

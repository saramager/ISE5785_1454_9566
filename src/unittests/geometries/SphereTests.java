/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;
import geometries.Sphere;
import primitives.Point;
import primitives.Vector;
import geometries.Plane;
import org.junit.jupiter.api.Test;

/**
 * Test Junit for geometries.Sphere
 *
 */
class SphereTests {

	/**
	 * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Correct normal to the Sphere
		Sphere sphere = new Sphere(new Point(1, 1, 1), 10.0);
		assertEquals(new Vector(0, 0, 1), sphere.getNormal(new Point(1, 1, 11)), "get Normal doen't work as expected ");
	}

}

/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;
import geometries.Tube;
import org.junit.jupiter.api.Test;

/***
 * Test Junit for geometries.Tube
 */
class TubeTests {

	/**
	 * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Computing the normal at a general point on the tube's surface
		Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		Tube tube = new Tube(1, axis);
		Point p1 = new Point(1, 1, 1);
		assertEquals(new Vector(1, 1, 0).normalize(), tube.getNormal(p1),
				"ERROR: The computed normal is incorrect for a general point on the tube");

		// =============== Boundary Values Tests ==================

		// TC02: Computing the normal at a point where the normal is aligned with one of
		// the axes
		Point p2 = new Point(0, 1, 0);
		Vector normal = tube.getNormal(p2);

		assertEquals(new Vector(0, 1, 0), normal,
				"ERROR: The computed normal is incorrect for a point directly perpendicular to the tube's axis");
	}

}

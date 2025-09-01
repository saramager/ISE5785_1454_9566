/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import geometries.Cylinder;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Test Junit for geometries.Cylinder
 */
class CylinderTest {

	/**
	 * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Normal at a general point on the lateral surface of the cylinder
		Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		Cylinder cylinder = new Cylinder(1, axis, 5); // Radius = 1, Height = 5
		Point p1 = new Point(1, 0, 2); // Point on the side surface

		assertEquals(new Vector(1, 0, 0), cylinder.getNormal(p1),
				"ERROR: The normal is incorrect for a point on the lateral surface");

		// TC02: Normal at a point inside the bottom base.
		Point p2 = new Point(0.5, 0.5, 0); // Inside the base
		assertEquals(new Vector(0, 0, -1), cylinder.getNormal(p2),
				"ERROR: The normal is incorrect for a point inside the bottom base");

		// TC03: Normal at a point inside the top base
		Point p3 = new Point(-0.5, -0.5, 5); // Inside the top base
		assertEquals(new Vector(0, 0, 1), cylinder.getNormal(p3),
				"ERROR: The normal is incorrect for a point inside the top base");

		// =============== Boundary Values Tests ==================

		// TC04: Normal at the center of the bottom base
		Point p4 = new Point(0, 0, 0);
		assertEquals(new Vector(0, 0, 1), cylinder.getNormal(p4),
				"ERROR: The normal is incorrect at the center of the bottom base");

		// TC05: Normal at the center of the top base
		Point p5 = new Point(0, 0, 5);
		assertEquals(new Vector(0, 0, 1), cylinder.getNormal(p5),
				"ERROR: The normal is incorrect at the center of the top base");

		// TC06: Normal at the transition between bottom base and lateral surface
		Point p6 = new Point(1, 0, 0); // Edge point
		assertEquals(new Vector(1, 0, 0), cylinder.getNormal(p6),
				"ERROR: The normal at the bottom base edge should match the lateral surface normal");

		// TC07: Normal at the transition between top base and lateral surface
		Point p7 = new Point(1, 0, 5); // Edge point
		assertEquals(new Vector(1, 0, 0), cylinder.getNormal(p7),
				"ERROR: The normal at the top base edge should match the lateral surface normal");
	}

}

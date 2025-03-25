/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;
 import primitives.*;
 import geometries.Triangle;

import org.junit.jupiter.api.Test;

/**
 * 
 */
class TriangleTests {

	/**
	 * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
	 */
	@Test
	void testGetNormal() {
		   // ============ Equivalence Partitions Tests ==============
    Point p1 = new Point(1, 0, 0);
    Point p2 = new Point(0, 2, 0);
    Point p3 = new Point(0, 0, 3);
    
    Triangle triangle = new Triangle(p1, p2, p3);

    // ============ TC01: Test that the normal vector is calculated correctly ==============
    Vector expectedNormal = new Vector(6, 3, 2);

    assertThrows(IllegalArgumentException.class,()-> triangle.getNormal(new Point(0, 1, 1)).crossProduct(expectedNormal), "The normal vector is not in the right direction");
    
    assertEquals(1, triangle.getNormal(new Point(1,0,0)).length(),1e-10, "The normal vector  isn't normalize  ");

}

}

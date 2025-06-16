package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.Intersectable.Intersection;
import geometries.Polygon;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Testing Polygons
 * 
 * @author Dan
 */
class PolygonTests {
	/**
	 * Delta value for accuracy when comparing the numbers of type 'double' in
	 * assertEquals
	 */
	private static final double DELTA = 0.000001;

	/** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
	@Test
	void testConstructor() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Correct concave quadrangular with vertices in correct order
		assertDoesNotThrow(
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)),
				"Failed constructing a correct polygon");

		// TC02: Wrong vertices order
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
				"Constructed a polygon with wrong order of vertices");

		// TC03: Not in the same plane
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
				"Constructed a polygon with vertices that are not in the same plane");

		// TC04: Concave quadrangular
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
						new Point(0.5, 0.25, 0.5)), //
				"Constructed a concave polygon");

		// =============== Boundary Values Tests ==================

		// TC10: Vertex on a side of a quadrangular
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0.5, 0.5)),
				"Constructed a polygon with vertix on a side");

		// TC11: Last point = first point
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
				"Constructed a polygon with vertice on a side");

		// TC12: Co-located points
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
				"Constructed a polygon with vertice on a side");

	}

	/** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: There is a simple single test here - using a quad
		Point[] pts = { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
		Polygon pol = new Polygon(pts);
		// ensure there are no exceptions
		assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
		// generate the test result
		Vector result = pol.getNormal(new Point(0, 0, 1));
		// ensure |result| = 1
		assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
		// ensure the result is orthogonal to all the edges
		for (int i = 0; i < 3; ++i)
			assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
					"Polygon's normal is not orthogonal to one of the edges");
	}

	/** Test method for {@link geometries.Polygon#findIntersections(Ray)}. */

	@Test
	void testFindIntersections() {
		final Polygon mesh = new Polygon(new Point(1, 1, 0), new Point(1, 0, 0), new Point(-1, -1, 0),
				new Point(0, 1, 0));
		// ============ Equivalence Partitions Tests ==============
		// TC01: the intersection point is inside the Polygon
		assertEquals(1, mesh.findIntersections(new Ray(new Point(-0.5, -0.5, 1), new Vector(0, 0, -1))).size(),
				"Failed to find the intersection point when the intersection point is inside the Polygon");

		// TC02: the intersection point is outside the Polygon and against an edge
		assertNull(mesh.findIntersections(new Ray(new Point(0.5, 2, 1), new Vector(0, 0, -1))),
				"Failed to find the intersection point when the intersection point is outside the Polygon and against an edge");

		// TC03: the intersection point is outside the Polygon and against a vertex
		assertNull(mesh.findIntersections(new Ray(new Point(2, 2, 1), new Vector(0, 0, -1))),
				"Failed to find the intersection point when the intersection point is outside the Polygon and against an edge");

		// ================= Boundary Values Tests =================
		// TC11: the intersection point is on the edge of the Polygon
		assertNull(mesh.findIntersections(new Ray(new Point(0.5, 1, -1), new Vector(0, 0, 1))),
				"Failed to find the intersection point when the intersection point is on the edge of the Polygon");

		// TC12: the intersection point is on the vertex of the Polygon
		assertNull(mesh.findIntersections(new Ray(new Point(1, 1, 1), new Vector(0, 0, -1))),
				"Failed to find the intersection point when the intersection point is on the vertex of the Polygon");

		// TC13: the intersection point is outside the Polygon but in the path of the
		// edge
		assertNull(mesh.findIntersections(new Ray(new Point(2, 1, -1), new Vector(0, 0, 1))),
				"Failed to find the intersection point when the intersection point is outside the Polygon but in the path of the edge");

		// TC14: the Polygon is in an angle
		Polygon mesh2 = new Polygon(new Point(0, 1, 1), new Point(1, 1, 0), new Point(1, 0, 1), new Point(-1, -1, 4));
		assertEquals(1, mesh2.findIntersections(new Ray(new Point(-1, -1, -1), new Vector(1, 1, 1))).size(),
				"Failed to find the intersection point when the intersection point is inside the Polygon");

		// TC15: the Polygon with 6 vertices
		Polygon mesh3 = new Polygon(new Point(1, 0, 0), new Point(1, 1, 0), new Point(0, 1, 0), new Point(-1, 0, 0),
				new Point(0, -1, 0));
		assertEquals(1, mesh3.findIntersections(new Ray(new Point(-1, -1, -1), new Vector(1, 1, 1))).size(),
				"Failed to find the intersection point when the intersection point is inside the Polygon");
	}

	/**
	 * Test method for
	 * {@link geometries.Polygon#calculateIntersections(primitives.Ray, double)}.
	 */
	@Test
	void testPolygonCalculateIntersectionsWithMaxDistance() {
		// Polygon in XY plane: square from (0,0,0) to (1,1,0)
		Polygon polygon = new Polygon(new Point(0, 0, 0), new Point(1, 0, 0), new Point(1, 1, 0), new Point(0, 1, 0));

		Vector dir = new Vector(0, 0, 1); // Direction towards the polygon from below

		// TC1: Intersection exactly at distance = 1 → 1 point
		Ray ray1 = new Ray(new Point(0.5, 0.5, -1), dir);
		List<Intersection> result1 = polygon.calculateIntersections(ray1, 1.0);
		assertEquals(1, result1.size(), "TC1: Expected 1 intersection at exact maxDistance");

		// TC2: Intersection just below max distance → 1 point
		Ray ray2 = new Ray(new Point(0.5, 0.5, -0.99), dir);
		List<Intersection> result2 = polygon.calculateIntersections(ray2, 1.0);
		assertEquals(1, result2.size(), "TC2: Expected 1 intersection just below maxDistance");

		// TC3: Intersection just above max distance → no intersection
		Ray ray3 = new Ray(new Point(0.5, 0.5, -1.01), dir);
		assertNull(polygon.calculateIntersections(ray3, 1.0), "TC3: Expected no intersection just above maxDistance");

		// TC4: Intersection at far distance, maxDistance small → no intersection
		Ray ray4 = new Ray(new Point(0.5, 0.5, -5), dir);
		assertNull(polygon.calculateIntersections(ray4, 1.0), "TC4: Expected no intersection due to large distance");

		// TC5: Ray starts on the plane → intersection at distance 0
		Ray ray5 = new Ray(new Point(0.5, 0.5, 0), dir);
		List<Intersection> result5 = polygon.calculateIntersections(ray5, 0);
		assertNull(result5, "TC5: Expected 0 intersection at distance 0");

		// TC6: Ray misses the polygon → no intersection regardless of distance
		Ray ray6 = new Ray(new Point(2, 2, -1), dir);
		assertNull(polygon.calculateIntersections(ray6, 5.0), "TC6: Expected no intersection (ray misses polygon)");
	}
}

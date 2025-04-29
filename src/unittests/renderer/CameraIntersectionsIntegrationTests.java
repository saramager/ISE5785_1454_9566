/**
 * 
 */
package unittests.renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.Geometry;
import geometries.Sphere;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import renderer.Camera;

/**
 * Testing Camera Class Integration with geometries
 * 
 */
class CameraIntersectionsIntegrationTests {

	/**
	 * The camera builder object used for testing.
	 */
	private final Camera.Builder cameraBuilder = Camera.getBuilder()
			.setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0)).setVpDistance(1).setVpSize(3, 3)
			.setResolution(10, 10);
	/**
	 * The camera object used for testing. based on the cameraBuilder It is
	 * initialized with a location at (0, 0, 0.5)
	 */

	private final Camera camera = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

	/**
	 * Helper method to check the amount of intersections between a ray and a
	 * geometry.
	 * 
	 * @param camera         the camera
	 * @param geometry       the geometry
	 * @param expectedAmount the expected amount of intersections
	 * @throws IllegalArgumentException if the expected amount is negative
	 */
	private void amountOfIntersections(Camera camera, Geometry geometry, int expectedAmount) {
		int intersections = 0;
		for (int j = 0; j < 3; j++)
			for (int i = 0; i < 3; i++) {
				Ray ray = camera.constructRay(3, 3, j, i);
				List<Point> intersectionsList = geometry.findIntersections(ray);
				intersections += intersectionsList != null ? intersectionsList.size() : 0;
			}

		assertEquals(expectedAmount, intersections, "Wrong amount of intersections");
	}

	/**
	 * Test method for {@link renderer.Camera#constructRay(int, int, int, int)}.
	 * with sphere
	 */
	@Test
	void testSphereIntersection() {
		// TC01: 2 intersections
		amountOfIntersections(cameraBuilder.setLocation(Point.ZERO).build(), new Sphere(new Point(0, 0, -3), 1.0), 2);

		// TC02: 18 intersections
		amountOfIntersections(camera, new Sphere(new Point(0, 0, -2.5), 2.5), 18);

		// TC03: 10 intersections
		amountOfIntersections(camera, new Sphere(new Point(0, 0, -2), 2.0), 10);

		// TC04: 9 intersections
		amountOfIntersections(camera, new Sphere(new Point(0, 0, -1), 4.0), 9);

		// TC05: 0 intersections
		amountOfIntersections(camera, new Sphere(new Point(0, 0, 1), 0.5), 0);
	}

	/**
	 * Test method for {@link renderer.Camera#constructRay(int, int, int, int)}.
	 */
	@Test
	void testPlaneIntersection() {
		// TC01: 9 intersections
		amountOfIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, 0, -1)), 9);

		// TC02: 9 intersections
		amountOfIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, 1, -10)), 9);

		// TC03: 6 intersections
		amountOfIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, -1, -1)), 6);
	}

	/**
	 * Test method for {@link renderer.Camera#constructRay(int, int, int, int)}.
	 */
	@Test
	void testTriangleIntersection() {
		// TC01: 1 intersections
		amountOfIntersections(camera,
				new geometries.Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 1);

		// TC02: 2 intersections
		amountOfIntersections(camera,
				new geometries.Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 2);
	}

}

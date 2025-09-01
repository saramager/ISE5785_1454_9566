package unittests.renderer;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

/**
 * Test class for complex regular grid performance improvement in rendering
 * scenes with multiple geometries and light sources. This test creates a
 * complex scene with a checkerboard floor, various geometries, and enhanced
 * lighting to demonstrate the efficiency of the REGULAR GRID ray tracer.
 */
class complexRegularGridTest {

	/**
	 * Complex scene test to demonstrate REGULAR GRID performance improvement.
	 * Creates a checkerboard floor with hundreds of small cubes, spheres,
	 * triangles, and a reflective plane, with enhanced lighting and material
	 * properties.
	 */
	private final Camera.Builder camera = Camera.getBuilder().setLocation(new Point(0, 15, 40)) // Closer to the scene
			.setDirection(new Point(00, -50, -100), Vector.AXIS_Y) // Focused on central sphere
			.setVpDistance(600) // Reduced for a more intimate view
			.setVpSize(500, 500);

	/**
	 * Test method to create a complex regular grid scene with various geometries
	 * and light sources, including transparency and reflectivity.
	 */
	private static Scene sceneBuild() {
		Scene scene = new Scene("Complex Regular Grid Test").setBackground(new Color(10, 20, 50)) // Subtle
																									// gradient-like
																									// dark blue
				.setAmbientLight(new AmbientLight(new Color(4, 4, 5))); // Softer ambient light

		// Materials for different elements
		Material blackMatte = new Material().setKD(0.8).setKS(0.2).setShininess(30).setKR(0.1);
		Material whiteMatte = new Material().setKD(0.7).setKS(0.3).setShininess(50).setKR(0.2);
		Material redShiny = new Material().setKD(0.4).setKS(0.8).setShininess(100).setKR(0.4).setKT(0.3);
		Material blueReflective = new Material().setKD(0.3).setKS(0.7).setShininess(80).setKR(0.6).setKT(0.4);
		Material glassLike = new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.5).setKT(0.8);
		Material planeMaterial = new Material().setKD(0.2).setKS(0.8).setShininess(100).setKR(0.7).setKT(0.5);

		// Create checkerboard floor with small cubes (20x20 = 400 cubes)
		int floorSize = 20;
		double cubeSize = 8;
		double floorLevel = -150;

		for (int i = 0; i < floorSize; i++) {
			for (int j = 0; j < floorSize; j++) {
				double x = (i - floorSize / 2) * cubeSize;
				double z = (j - floorSize / 2) * cubeSize - 200; // Push floor back

				// Checkerboard pattern
				boolean isBlack = (i + j) % 2 == 0;
				Material cubeMaterial = isBlack ? blackMatte : whiteMatte;
				Color cubeColor = isBlack ? new Color(20, 20, 20) : new Color(220, 220, 220);

				// Create cube using 6 polygons (faces)
				createCube(scene, new Point(x, floorLevel, z), cubeSize, cubeColor, cubeMaterial);
			}
		}

		// Add a reflective and semi-transparent plane as a floor
		scene.geometries.add(new Plane(new Point(0, floorLevel - 0.1, -200), new Vector(0, 1, 0))
				.setEmission(new Color(10, 10, 20)).setMaterial(planeMaterial));

		// Add spheres at different heights and positions with enhanced properties
		scene.geometries.add(
				// Large central sphere
				new Sphere(new Point(0, -50, -200), 25.0).setEmission(new Color(100, 50, 50)).setMaterial(redShiny),

				// Medium spheres with increased transparency/reflectivity
				new Sphere(new Point(-40, -70, -180), 15.0).setEmission(new Color(50, 50, 100))
						.setMaterial(blueReflective),

				new Sphere(new Point(40, -60, -220), 18.0).setEmission(new Color(50, 100, 50)).setMaterial(glassLike),

				new Sphere(new Point(-30, -40, -240), 12.0).setEmission(new Color(100, 100, 50)).setMaterial(redShiny),

				new Sphere(new Point(35, -45, -160), 14.0).setEmission(new Color(100, 50, 100))
						.setMaterial(blueReflective),

				// Additional smaller sphere for variety
				new Sphere(new Point(20, -30, -190), 10.0).setEmission(new Color(80, 80, 100)).setMaterial(glassLike));

		// Add triangular decorations
		scene.geometries.add(
				// Pyramid-like structure
				new Triangle(new Point(-60, -80, -150), new Point(-40, -80, -150), new Point(-50, -40, -150))
						.setEmission(new Color(80, 80, 20))
						.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60).setKR(0.3)),

				new Triangle(new Point(40, -80, -250), new Point(60, -80, -250), new Point(50, -40, -250))
						.setEmission(new Color(20, 80, 80))
						.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60).setKR(0.3)),

				// Additional triangle for variety
				new Triangle(new Point(0, -60, -230), new Point(20, -60, -230), new Point(10, -20, -230))
						.setEmission(new Color(100, 50, 80))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(70).setKT(0.4)));

		// Add light sources
		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(1, -1, -1)));
		scene.lights.add(new PointLight(new Color(10, 10, 20), new Point(-50, 0, -100)).setKl(0.0001).setKq(0.00001));
		scene.lights.add(new PointLight(new Color(20, 10, 10), new Point(50, 0, -300)).setKl(0.0001).setKq(0.00001));
		// New light sources
		scene.lights.add(new SpotLight(new Color(20, 20, 10), new Point(0, 50, -100), new Vector(0, -1, -1))
				.setKl(0.0001).setKq(0.00001).setNarrowBeam(10));
		scene.lights.add(new PointLight(new Color(10, 20, 10), new Point(80, 20, -150)).setKl(0.0001).setKq(0.00001));

		return scene;
	}

	/**
	 * Helper method to create a cube in the scene at a specified center point with
	 * a given size, color, and material.
	 *
	 * @param scene    the scene to add the cube to
	 * @param center   the center point of the cube
	 * @param size     the size of the cube
	 * @param color    the color of the cube
	 * @param material the material of the cube
	 */
	private static void createCube(Scene scene, Point center, double size, Color color, Material material) {
		double half = size / 2;
		double x = center.getX();
		double y = center.getY();
		double z = center.getZ();

		// Bottom face
		scene.geometries
				.add(new Polygon(new Point(x - half, y - half, z - half), new Point(x + half, y - half, z - half),
						new Point(x + half, y - half, z + half), new Point(x - half, y - half, z + half))
						.setEmission(color).setMaterial(material));

		// Top face
		scene.geometries
				.add(new Polygon(new Point(x - half, y + half, z - half), new Point(x - half, y + half, z + half),
						new Point(x + half, y + half, z + half), new Point(x + half, y + half, z - half))
						.setEmission(color).setMaterial(material));

		// Front face
		scene.geometries
				.add(new Polygon(new Point(x - half, y - half, z + half), new Point(x + half, y - half, z + half),
						new Point(x + half, y + half, z + half), new Point(x - half, y + half, z + half))
						.setEmission(color).setMaterial(material));

		// Back face
		scene.geometries
				.add(new Polygon(new Point(x + half, y - half, z - half), new Point(x - half, y - half, z - half),
						new Point(x - half, y + half, z - half), new Point(x + half, y + half, z - half))
						.setEmission(color).setMaterial(material));

		// Left face
		scene.geometries
				.add(new Polygon(new Point(x - half, y - half, z - half), new Point(x - half, y - half, z + half),
						new Point(x - half, y + half, z + half), new Point(x - half, y + half, z - half))
						.setEmission(color).setMaterial(material));

		// Right face
		scene.geometries
				.add(new Polygon(new Point(x + half, y - half, z + half), new Point(x + half, y - half, z - half),
						new Point(x + half, y + half, z - half), new Point(x + half, y + half, z + half))
						.setEmission(color).setMaterial(material));
	}

	@Test
	public void complexGridTest() {
		Scene scene = sceneBuild();
		camera.setRayTracer(scene, RayTracerType.GRID).setResolution(1000, 1000).setMultithreading(-2)
				.setDebugPrint(0.1).build().renderImage().writeToImage("Complex Regular g&m Performance Test Enhanced");
	}

	@Test
	public void complexSimpleThreadTest() {
		Scene scene = sceneBuild();
		// Render with higher resolution to stress test the grid
		camera.setRayTracer(scene, RayTracerType.SIMPLE).setResolution(1000, 1000) // Increased for sharper output
				.setMultithreading(-2).setDebugPrint(0.1).build().renderImage()
				.writeToImage("Complex Regular s&m Performance Test Enhanced");
	}

	@Test
	public void complexeGridest() {
		Scene scene = sceneBuild();
		camera.setRayTracer(scene, RayTracerType.GRID).setResolution(1000, 1000).setDebugPrint(0.1).build()
				.renderImage().writeToImage("Complex Regular g Performance Test Enhanced ");
	}

	@Test
	public void complexSimpleNOThreadTest() {
		Scene scene = sceneBuild();
		camera.setRayTracer(scene, RayTracerType.SIMPLE).setResolution(800, 800).setDebugPrint(0.1).build()
				.renderImage().writeToImage("Complex Regular no Performance Test Enhanced ");
	}

}
package unittests.renderer;

import org.junit.jupiter.api.Test;

import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

class complexRegularGridTest {

	/**
	 * Complex scene test to demonstrate REGULAR GRID performance improvement
	 * Creates a checkerboard floor with hundreds of small cubes and spheres above
	 * This test showcases the efficiency gain when using spatial partitioning
	 */
//	private final Camera.Builder camera = Camera.getBuilder().setLocation(new Point(0, 50, 100)) // מעל הסצנה ומעט קדימה
//			.setDirection(new Point(0, -60, -200), Vector.AXIS_Y) // מסתכל מעט למטה ואחורה
//			.setVpDistance(1000).setVpSize(500, 500);
	private final Camera.Builder camera = Camera.getBuilder().setLocation(new Point(0, 20, 50)) // יותר קרוב לסצנה
			.setDirection(new Point(0, -80, -200), Vector.AXIS_Y) // מסתכל למטה לכיוון מרכז הרצפה
			.setVpDistance(500).setVpSize(500, 500);

	@Test
	public void RegularGridTest() {
		Scene scene = new Scene("Complex Regular Grid Test").setBackground(new Color(25, 25, 40))
				.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		// Materials for different elements
		Material blackMatte = new Material().setKD(0.8).setKS(0.2).setShininess(30);
		Material whiteMatte = new Material().setKD(0.7).setKS(0.3).setShininess(50);
		Material redShiny = new Material().setKD(0.4).setKS(0.8).setShininess(100).setKR(0.3);
		Material blueReflective = new Material().setKD(0.3).setKS(0.7).setShininess(80).setKR(0.5);
		Material glassLike = new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.4).setKT(0.7);

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

		// Add spheres at different heights and positions
		scene.geometries.add(
				// Large central sphere
				new Sphere(new Point(0, -50, -200), 25.0).setEmission(new Color(100, 50, 50)).setMaterial(redShiny),

				// Medium spheres around
				new Sphere(new Point(-40, -70, -180), 15.0).setEmission(new Color(50, 50, 100))
						.setMaterial(blueReflective),

				new Sphere(new Point(40, -60, -220), 18.0).setEmission(new Color(50, 100, 50)).setMaterial(glassLike),

				new Sphere(new Point(-30, -40, -240), 12.0).setEmission(new Color(100, 100, 50)).setMaterial(redShiny),

				new Sphere(new Point(35, -45, -160), 14.0).setEmission(new Color(100, 50, 100))
						.setMaterial(blueReflective));

		// Add some triangular decorations
		scene.geometries.add(
				// Pyramid-like structure
				new Triangle(new Point(-60, -80, -150), new Point(-40, -80, -150), new Point(-50, -40, -150))
						.setEmission(new Color(80, 80, 20))
						.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60)),

				new Triangle(new Point(40, -80, -250), new Point(60, -80, -250), new Point(50, -40, -250))
						.setEmission(new Color(20, 80, 80))
						.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60)));

		// Add light sources
		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(1, -1, -1)));
		scene.lights
				.add(new PointLight(new Color(100, 100, 200), new Point(-50, 0, -100)).setKl(0.0001).setKq(0.00001));
		scene.lights.add(new PointLight(new Color(200, 100, 100), new Point(50, 0, -300)).setKl(0.0001).setKq(0.00001));

		// Render with high resolution to stress test the grid
		camera.setRayTracer(scene, RayTracerType.GRID) // Use REGULAR_GRID instead of SIMPLE
				.setResolution(800, 800).setMultithreading(-1).setDebugPrint(0.1).build().renderImage()
				.writeToImage("Complex Regular Grid Performance Test");
	}

	/**
	 * Helper method to create a cube using 6 polygons
	 */
	private void createCube(Scene scene, Point center, double size, Color color, Material material) {
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

}

package unittests.renderer;

import org.junit.jupiter.api.Test;

import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import lighting.*;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

/**
 * Test class for rendering a snow globe scene with multiple geometries and
 * light sources, optimized for testing a regular grid ray tracer.
 */
class SnowTest {

	private final Camera.Builder camera = Camera.getBuilder().setLocation(new Point(0, 30, 80))
			.setDirection(new Point(0, -50, -150), Vector.AXIS_Y).setVpDistance(600).setVpSize(500, 500)
			.setAntiAliasingRays(1);

	/**
	 * Static method to build the snow globe scene with enhanced geometries.
	 *
	 * @return the constructed Scene object
	 */
	private static Scene buildSnowGlobeScene() {
		Scene scene = new Scene("Snow Globe Test").setBackground(new Color(10, 10, 20))
				.setAmbientLight(new AmbientLight(new Color(15, 15, 15)));

		// Materials for different elements
		Material transparentGlobe = new Material().setKD(0.1).setKS(0.9).setShininess(100).setKT(0.8).setTAngle(1);
		Material reflectiveBase = new Material().setKD(0.5).setKS(0.5).setShininess(40).setKR(0.1).setKT(0);
		Material snowMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(30);
		Material decorationMaterial = new Material().setKD(0.4).setKS(0.7).setShininess(80).setKR(0.2);
		Material sledMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(25);
		Material polarBearMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(20);
		Material snowmanMaterial = new Material().setKD(0.8).setKS(0.2).setShininess(15);
		Material coalMaterial = new Material().setKD(0.9).setKS(0.1).setShininess(5);
		Material carrotMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(10);
		Material globeBaseMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(30).setKR(0.2);
		Material wallMaterial1 = new Material().setKD(0.5).setKS(0.5).setShininess(50).setKT(0.2);
		Material wallMaterial2 = new Material().setKD(0.6).setKS(0.4).setShininess(30);
		Material tableMaterial = new Material().setKD(0.6).setKS(0.3).setShininess(20);

		// Snow globe base
		scene.geometries.add(new Sphere(new Point(0, -100, -150), 44.0).setEmission(new Color(80, 70, 60))
				.setMaterial(globeBaseMaterial));

		// Snow globe dome
		scene.geometries.add(new Sphere(new Point(0, -50, -150), 50.0).setEmission(new Color(10, 15, 25))
				.setMaterial(transparentGlobe));

		// Dense checkered floor
		Color checkerColor1 = new Color(200, 200, 200); // Light gray
		Color checkerColor2 = new Color(100, 100, 100); // Dark gray
		int gridSize = 40;
		double squareSize = 2.5;
		double xStart = -50.0;
		double zStart = -200.0;
		double yFloor = -100.0;

		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				double x0 = xStart + i * squareSize;
				double z0 = zStart + j * squareSize;
				Point p1 = new Point(x0, yFloor, z0);
				Point p2 = new Point(x0 + squareSize, yFloor, z0);
				Point p3 = new Point(x0 + squareSize, yFloor, z0 + squareSize);
				Point p4 = new Point(x0, yFloor, z0 + squareSize);
				Color emission = ((i + j) % 2 == 0) ? checkerColor1 : checkerColor2;
				scene.geometries.add(new Polygon(p1, p2, p3, p4).setEmission(emission).setMaterial(reflectiveBase));
			}
		}

		// Intricate back wall (20x20 grid of 2-unit cubes)
		Color wallColor1 = new Color(150, 200, 255); // Icy blue
		Color wallColor2 = new Color(220, 220, 220); // White
		int wallGridSize = 20;
		double cubeSize = 2.0;
		double wallXStart = -20.0;
		double wallYStart = -80.0;
		double wallZ = -200.0;

		for (int i = 0; i < wallGridSize; i++) {
			for (int j = 0; j < wallGridSize; j++) {
				double x = wallXStart + i * cubeSize;
				double y = wallYStart + j * cubeSize;
				// Cube as six polygons
				// Front face
				scene.geometries.add(new Polygon(new Point(x, y, wallZ), new Point(x + cubeSize, y, wallZ),
						new Point(x + cubeSize, y + cubeSize, wallZ), new Point(x, y + cubeSize, wallZ))
						.setEmission((i + j) % 2 == 0 ? wallColor1 : wallColor2)
						.setMaterial((i + j) % 2 == 0 ? wallMaterial1 : wallMaterial2));
				// Back face
				scene.geometries.add(
						new Polygon(new Point(x, y, wallZ - cubeSize), new Point(x + cubeSize, y, wallZ - cubeSize),
								new Point(x + cubeSize, y + cubeSize, wallZ - cubeSize),
								new Point(x, y + cubeSize, wallZ - cubeSize))
								.setEmission((i + j) % 2 == 0 ? wallColor1 : wallColor2)
								.setMaterial((i + j) % 2 == 0 ? wallMaterial1 : wallMaterial2));
				// Left face
				scene.geometries.add(new Polygon(new Point(x, y, wallZ), new Point(x, y, wallZ - cubeSize),
						new Point(x, y + cubeSize, wallZ - cubeSize), new Point(x, y + cubeSize, wallZ))
						.setEmission((i + j) % 2 == 0 ? wallColor1 : wallColor2)
						.setMaterial((i + j) % 2 == 0 ? wallMaterial1 : wallMaterial2));
				// Right face
				scene.geometries.add(
						new Polygon(new Point(x + cubeSize, y, wallZ), new Point(x + cubeSize, y, wallZ - cubeSize),
								new Point(x + cubeSize, y + cubeSize, wallZ - cubeSize),
								new Point(x + cubeSize, y + cubeSize, wallZ))
								.setEmission((i + j) % 2 == 0 ? wallColor1 : wallColor2)
								.setMaterial((i + j) % 2 == 0 ? wallMaterial1 : wallMaterial2));
				// Top face
				scene.geometries.add(
						new Polygon(new Point(x, y + cubeSize, wallZ), new Point(x + cubeSize, y + cubeSize, wallZ),
								new Point(x + cubeSize, y + cubeSize, wallZ - cubeSize),
								new Point(x, y + cubeSize, wallZ - cubeSize))
								.setEmission((i + j) % 2 == 0 ? wallColor1 : wallColor2)
								.setMaterial((i + j) % 2 == 0 ? wallMaterial1 : wallMaterial2));
				// Bottom face
				scene.geometries.add(new Polygon(new Point(x, y, wallZ), new Point(x + cubeSize, y, wallZ),
						new Point(x + cubeSize, y, wallZ - cubeSize), new Point(x, y, wallZ - cubeSize))
						.setEmission((i + j) % 2 == 0 ? wallColor1 : wallColor2)
						.setMaterial((i + j) % 2 == 0 ? wallMaterial1 : wallMaterial2));
			}
		}

		// Table (positioned to the right of the snow globe)
		double tableX = 30.0;
		double tableY = -80.0;
		double tableZ = -160.0;
		// Tabletop
		scene.geometries.add(
				new Polygon(new Point(tableX - 10, tableY, tableZ - 10), new Point(tableX + 10, tableY, tableZ - 10),
						new Point(tableX + 10, tableY, tableZ + 10), new Point(tableX - 10, tableY, tableZ + 10))
						.setEmission(new Color(139, 69, 19)).setMaterial(tableMaterial));
		// Legs
		double legHeight = 10.0;
		double legWidth = 1.0;
		scene.geometries.add(new Polygon(new Point(tableX - 9, tableY, tableZ - 9),
				new Point(tableX - 8, tableY, tableZ - 9), new Point(tableX - 8, tableY - legHeight, tableZ - 9),
				new Point(tableX - 9, tableY - legHeight, tableZ - 9)).setEmission(new Color(139, 69, 19))
				.setMaterial(tableMaterial));
		scene.geometries.add(new Polygon(new Point(tableX + 8, tableY, tableZ - 9),
				new Point(tableX + 9, tableY, tableZ - 9), new Point(tableX + 9, tableY - legHeight, tableZ - 9),
				new Point(tableX + 8, tableY - legHeight, tableZ - 9)).setEmission(new Color(139, 69, 19))
				.setMaterial(tableMaterial));
		scene.geometries.add(new Polygon(new Point(tableX - 9, tableY, tableZ + 8),
				new Point(tableX - 8, tableY, tableZ + 8), new Point(tableX - 8, tableY - legHeight, tableZ + 8),
				new Point(tableX - 9, tableY - legHeight, tableZ + 8)).setEmission(new Color(139, 69, 19))
				.setMaterial(tableMaterial));
		scene.geometries.add(new Polygon(new Point(tableX + 8, tableY, tableZ + 8),
				new Point(tableX + 9, tableY, tableZ + 8), new Point(tableX + 9, tableY - legHeight, tableZ + 8),
				new Point(tableX + 8, tableY - legHeight, tableZ + 8)).setEmission(new Color(139, 69, 19))
				.setMaterial(tableMaterial));
		// Table decorations (small spheres)
		for (int i = 0; i < 5; i++) {
			double x = tableX - 8 + i * 4;
			scene.geometries.add(new Sphere(new Point(x, tableY + 1, tableZ), 1.0).setEmission(new Color(255, 215, 0))
					.setMaterial(decorationMaterial));
		}

		// Increased snow particles
		int snowCount = 600;
		for (int i = 0; i < snowCount; i++) {
			double x = Math.random() * 80 - 40;
			double y = Math.random() * 40 - 90;
			double z = Math.random() * 80 - 190;
			double radius = Math.random() * 1.5 + 0.5;
			double distanceFromCenter = Math.sqrt(x * x + (y + 50) * (y + 50) + (z + 150) * (z + 150));
			if (distanceFromCenter < 45) {
				scene.geometries.add(new Sphere(new Point(x, y, z), radius).setEmission(new Color(240, 245, 250))
						.setMaterial(snowMaterial));
			}
		}

		// Polar bear
		scene.geometries.add(new Sphere(new Point(-25, -60, -145), 5.0).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-22, -53, -140), 3.8).setEmission(new Color(245, 245, 225))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-20, -50, -138), 1.2).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-24, -50, -138), 1.2).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-28, -64, -148), 2.0).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-22, -64, -148), 2.0).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-28, -64, -142), 2.0).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-22, -64, -142), 2.0).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-20, -52, -136), 0.5).setEmission(new Color(20, 20, 20))
				.setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(-21, -50, -136), 0.3).setEmission(new Color(20, 20, 20))
				.setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(-23, -50, -136), 0.3).setEmission(new Color(20, 20, 20))
				.setMaterial(coalMaterial));

		// Snowman
		scene.geometries.add(new Sphere(new Point(25, -58, -145), 7.0).setEmission(new Color(220, 220, 220))
				.setMaterial(snowmanMaterial));
		scene.geometries.add(new Sphere(new Point(25, -48, -145), 5.5).setEmission(new Color(220, 220, 220))
				.setMaterial(snowmanMaterial));
		scene.geometries.add(new Sphere(new Point(25, -40, -145), 4.0).setEmission(new Color(220, 220, 220))
				.setMaterial(snowmanMaterial));
		scene.geometries.add(
				new Sphere(new Point(25, -50, -140), 0.8).setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(
				new Sphere(new Point(25, -46, -140), 0.8).setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(
				new Sphere(new Point(25, -55, -140), 0.8).setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(
				new Sphere(new Point(23, -39, -142), 0.7).setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(
				new Sphere(new Point(27, -39, -142), 0.7).setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(25, -41, -141), 0.6).setEmission(new Color(255, 140, 0))
				.setMaterial(carrotMaterial));
		scene.geometries.add(new Sphere(new Point(23, -43, -141.5), 0.4).setEmission(new Color(15, 15, 15))
				.setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(25, -44, -141.5), 0.4).setEmission(new Color(15, 15, 15))
				.setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(27, -43, -141.5), 0.4).setEmission(new Color(15, 15, 15))
				.setMaterial(coalMaterial));

		// Snow house
		scene.geometries.add(new Polygon(new Point(-5, -60, -150), new Point(5, -60, -150), new Point(5, -45, -150),
				new Point(-5, -45, -150)).setEmission(new Color(240, 240, 240)).setMaterial(snowmanMaterial));
		scene.geometries.add(new Polygon(new Point(5, -60, -150), new Point(5, -60, -160), new Point(5, -45, -160),
				new Point(5, -45, -150)).setEmission(new Color(230, 230, 230)).setMaterial(snowmanMaterial));
		scene.geometries.add(new Polygon(new Point(-5, -60, -150), new Point(-5, -60, -160), new Point(-5, -45, -160),
				new Point(-5, -45, -150)).setEmission(new Color(220, 220, 220)).setMaterial(snowmanMaterial));
		scene.geometries.add(new Polygon(new Point(-5, -60, -160), new Point(5, -60, -160), new Point(5, -45, -160),
				new Point(-5, -45, -160)).setEmission(new Color(210, 210, 210)).setMaterial(snowmanMaterial));
		scene.geometries.add(new Triangle(new Point(-5, -45, -150), new Point(5, -45, -150), new Point(0, -35, -155))
				.setEmission(new Color(200, 50, 50)).setMaterial(decorationMaterial));
		scene.geometries.add(new Triangle(new Point(5, -45, -150), new Point(5, -45, -160), new Point(0, -35, -155))
				.setEmission(new Color(180, 40, 40)).setMaterial(decorationMaterial));
		scene.geometries.add(new Triangle(new Point(5, -45, -160), new Point(-5, -45, -160), new Point(0, -35, -155))
				.setEmission(new Color(160, 30, 30)).setMaterial(decorationMaterial));
		scene.geometries.add(new Triangle(new Point(-5, -45, -160), new Point(-5, -45, -150), new Point(0, -35, -155))
				.setEmission(new Color(190, 45, 45)).setMaterial(decorationMaterial));
		scene.geometries.add(new Polygon(new Point(-2, -60, -149), new Point(2, -60, -149), new Point(2, -50, -149),
				new Point(-2, -50, -149)).setEmission(new Color(80, 60, 40)).setMaterial(coalMaterial));
		scene.geometries
				.add(new Polygon(new Point(1.5, -52, -149), new Point(3.5, -52, -149), new Point(3.5, -48, -149),
						new Point(1.5, -48, -149)).setEmission(new Color(100, 150, 200)).setMaterial(transparentGlobe));

		// Tree
		scene.geometries.add(new Polygon(new Point(25, -75, -135), new Point(27, -75, -135), new Point(27, -68, -135),
				new Point(25, -68, -135)).setEmission(new Color(101, 67, 33)).setMaterial(sledMaterial));
		scene.geometries.add(new Triangle(new Point(22, -68, -135), new Point(30, -68, -135), new Point(26, -62, -135))
				.setEmission(new Color(34, 139, 34)).setMaterial(decorationMaterial));
		scene.geometries.add(new Triangle(new Point(23, -64, -135), new Point(29, -64, -135), new Point(26, -58, -135))
				.setEmission(new Color(50, 150, 50)).setMaterial(decorationMaterial));
		scene.geometries.add(new Triangle(new Point(24, -60, -135), new Point(28, -60, -135), new Point(26, -54, -135))
				.setEmission(new Color(60, 160, 60)).setMaterial(decorationMaterial));

		// Sled (corrected)
		scene.geometries
				.add(new Polygon(new Point(-28, -64, -140), new Point(-21, -64, -140), new Point(-21, -64, -135),
						new Point(-28, -64, -135)).setEmission(new Color(139, 69, 19)).setMaterial(sledMaterial));
		scene.geometries
				.add(new Polygon(new Point(-28, -65, -141), new Point(-27, -65, -141), new Point(-27, -65, -134),
						new Point(-28, -65, -134)).setEmission(new Color(100, 50, 20)).setMaterial(sledMaterial));
		scene.geometries
				.add(new Polygon(new Point(-22, -65, -141), new Point(-21, -65, -141), new Point(-21, -65, -134),
						new Point(-22, -65, -134)).setEmission(new Color(100, 50, 20)).setMaterial(sledMaterial));

		// Icicles
		scene.geometries.add(new Triangle(new Point(-10, -10, -140), new Point(-8, -10, -140), new Point(-9, -25, -140))
				.setEmission(new Color(200, 220, 255)).setMaterial(transparentGlobe));
		scene.geometries.add(new Triangle(new Point(8, -10, -155), new Point(10, -10, -155), new Point(9, -20, -155))
				.setEmission(new Color(200, 220, 255)).setMaterial(transparentGlobe));

		// Lights
		scene.lights.add(new DirectionalLight(new Color(70, 70, 70), new Vector(0, -1, -0.5)));
		scene.lights.add(new PointLight(new Color(75, 50, 25), new Point(0, 50, -100)).setKl(0.0001).setKq(0.00001));
		scene.lights.add(new SpotLight(new Color(25, 50, 75), new Point(-50, 20, -50), new Vector(1, -1, -1))
				.setKl(0.0001).setKq(0.00001));
		scene.lights.add(new SpotLight(new Color(50, 75, 25), new Point(50, 20, -50), new Vector(-1, -1, -1))
				.setKl(0.0001).setKq(0.00001));
		scene.lights.add(new PointLight(new Color(100, 25, 25), new Point(0, -30, -200)).setKl(0.0001).setKq(0.00001));

		return scene;
	}

	/**
	 * Test method to render the snow globe scene using a regular grid ray tracer.
	 */
	@Test
	public void SnowGlobeSceneTest() {
		Scene scene = buildSnowGlobeScene();
		camera.setRayTracer(scene, RayTracerType.GRID).setResolution(800, 800).setGlossyAndDiffuseRays(81)
				.setMultithreading(-2).setDebugPrint(0.1).build().renderImage()
				.writeToImage("Snow Globe Scene with Regular Grid");
	}

	@Test
	public void SnowGlobeSceneSimpleThreadTest() {
		Scene scene = buildSnowGlobeScene();
		camera.setRayTracer(scene, RayTracerType.SIMPLE).setResolution(800, 800).setGlossyAndDiffuseRays(81)
				.setMultithreading(-2).setDebugPrint(0.1).build().renderImage()
				.writeToImage("Snow Globe Scene with SIMPLE + threadths ");
	}

	@Test
	public void SnowGlobeSceneGridest() {
		Scene scene = buildSnowGlobeScene();
		camera.setRayTracer(scene, RayTracerType.GRID).setResolution(800, 800).setGlossyAndDiffuseRays(81)
				.setDebugPrint(0.1).build().renderImage()
				.writeToImage("Snow Globe Scene with Regular Grid No threads ");
	}

	@Test
	public void SnowGlobeSceneSimpleNOThreadTest() {
		Scene scene = buildSnowGlobeScene();
		camera.setRayTracer(scene, RayTracerType.SIMPLE).setResolution(800, 800).setGlossyAndDiffuseRays(81)
				.setDebugPrint(0.1).build().renderImage().writeToImage("Snow Globe Scene with SIMPLE ");
	}
}
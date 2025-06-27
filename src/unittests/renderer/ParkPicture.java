package unittests.renderer;

import java.util.Random;

import org.junit.jupiter.api.Test;

import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

/**
 * ParkPicture class contains a complex park scene to demonstrate REGULAR GRID
 * performance Features: trees, grass, hexagonal lake, benches, flowers, and
 * paths This scene contains hundreds of small geometric elements
 * 
 */
class ParkPicture {

	/**
	 * Complex park scene to demonstrate REGULAR GRID performance Features: trees,
	 * grass, hexagonal lake, benches, flowers, and paths This scene contains
	 * hundreds of small geometric elements
	 */
	@Test
	public void parkSceneRegularGridTest() {
		Scene scene = new Scene("Park Scene Test").setBackground(new Color(135, 206, 235)) // Sky blue
				.setAmbientLight(new AmbientLight(new Color(50, 50, 50)));

		// Materials
		Material grassMaterial = new Material().setKD(0.8).setKS(0.1).setShininess(10);
		Material trunkMaterial = new Material().setKD(0.7).setKS(0.2).setShininess(20);
		Material leafMaterial = new Material().setKD(0.6).setKS(0.3).setShininess(30);
		Material waterMaterial = new Material().setKD(0.2).setKS(0.8).setShininess(100).setKR(0.6).setKT(0.7);
		Material benchMaterial = new Material().setKD(0.5).setKS(0.4).setShininess(50);
		Material flowerMaterial = new Material().setKD(0.7).setKS(0.5).setShininess(40);
		Material pathMaterial = new Material().setKD(0.9).setKS(0.1).setShininess(5);

		// Ground plane (large grass area)
		scene.geometries
				.add(new Polygon(new Point(-200, -100, -400), new Point(200, -100, -400), new Point(200, -100, -50),
						new Point(-200, -100, -50)).setEmission(new Color(34, 139, 34)).setMaterial(grassMaterial));

		// Create hexagonal lake in the center
		createHexagonalLake(scene, new Point(0, -99, -200), 40, waterMaterial);

		// Create trees around the park
		createTree(scene, new Point(-80, -100, -120), 8, 12, trunkMaterial, leafMaterial, true); // Mixed tree
		createTree(scene, new Point(70, -100, -150), 6, 10, trunkMaterial, leafMaterial, false); // Sphere crown
		createTree(scene, new Point(-60, -100, -280), 7, 11, trunkMaterial, leafMaterial, true); // Mixed tree
		createTree(scene, new Point(90, -100, -300), 5, 9, trunkMaterial, leafMaterial, false); // Sphere crown
		createTree(scene, new Point(-120, -100, -200), 9, 14, trunkMaterial, leafMaterial, true); // Mixed tree
		createTree(scene, new Point(100, -100, -100), 6, 8, trunkMaterial, leafMaterial, false); // Sphere crown
		createTree(scene, new Point(-30, -100, -80), 4, 7, trunkMaterial, leafMaterial, true); // Mixed tree
		createTree(scene, new Point(40, -100, -350), 8, 13, trunkMaterial, leafMaterial, false); // Sphere crown

		// Create detailed grass patches (small polygons)
		createGrassPatches(scene, 150, grassMaterial);

		// Create park benches
		createBench(scene, new Point(-50, -100, -160), benchMaterial);
		createBench(scene, new Point(60, -100, -250), benchMaterial);
		createBench(scene, new Point(20, -100, -120), benchMaterial);

		// Create flower beds
		createFlowerBed(scene, new Point(-40, -100, -100), 15, flowerMaterial);
		createFlowerBed(scene, new Point(80, -100, -180), 12, flowerMaterial);
		createFlowerBed(scene, new Point(-90, -100, -320), 18, flowerMaterial);

		// Create winding path
		createPath(scene, pathMaterial);

		// Add some birds (small spheres in the air)
		scene.geometries.add(
				new Sphere(new Point(-30, -40, -180), 2d).setEmission(new Color(100, 100, 100))
						.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(30)),
				new Sphere(new Point(50, -35, -220), 2d).setEmission(new Color(80, 80, 80))
						.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(30)),
				new Sphere(new Point(-70, -45, -160), 2d).setEmission(new Color(120, 120, 120))
						.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(30)));

		// Lighting setup
		// scene.lights.add(new DirectionalLight(new Color(200, 200, 150), new Vector(1,
		// -1, -1))); // Sun
		scene.lights
				.add(new PointLight(new Color(100, 150, 200), new Point(-100, 50, -100)).setKl(0.0001).setKq(0.00001));// Sky
		// reflection
		scene.lights
				.add(new PointLight(new Color(150, 200, 100), new Point(100, 20, -300)).setKl(0.0001).setKq(0.00001)); // Forest
		// ambience

		// Camera positioned as a park visitor
		Camera.Builder camera = Camera.getBuilder().setLocation(new Point(-150, -50, 0))
				.setDirection(new Point(50, -80, -200), Vector.AXIS_Y).setVpDistance(750).setVpSize(500, 500);

		camera.setRayTracer(scene, RayTracerType.GRID).setMultithreading(-1).setDebugPrint(0.1).setResolution(800, 800)
				.build().renderImage().writeToImage("Park Scene Regular Grid Test");
	}

	/**
	 * Creates a hexagonal lake at the specified center with a given radius
	 * 
	 * @param scene         the scene to add the lake to
	 * @param center        the center point of the lake
	 * @param radius        the radius of the hexagon
	 * @param waterMaterial the material for the water surface
	 */
	private void createHexagonalLake(Scene scene, Point center, double radius, Material waterMaterial) {
		double x = center.getX();
		double y = center.getY();
		double z = center.getZ();

		// Create hexagon vertices
		Point[] vertices = new Point[6];
		for (int i = 0; i < 6; i++) {
			double angle = i * Math.PI / 3;
			vertices[i] = new Point(x + radius * Math.cos(angle), y, z + radius * Math.sin(angle));
		}

		// Create hexagonal surface using triangles
		Point centerPoint = new Point(x, y, z);
		for (int i = 0; i < 6; i++) {
			scene.geometries.add(new Triangle(centerPoint, vertices[i], vertices[(i + 1) % 6])
					.setEmission(new Color(0, 100, 200)).setMaterial(waterMaterial));
		}
	}

	/**
	 * Creates a tree with a trunk and a crown
	 * 
	 * @param scene         the scene to add the tree to
	 * @param basePoint     the base point of the tree trunk
	 * @param trunkWidth    the width of the trunk
	 * @param trunkHeight   the height of the trunk
	 * @param trunkMaterial the material for the trunk
	 * @param leafMaterial  the material for the leaves
	 * @param mixedCrown    if true, creates a mixed crown (sphere + triangles), if
	 *                      false, creates a sphere crown only
	 */
	private void createTree(Scene scene, Point basePoint, double trunkWidth, double trunkHeight, Material trunkMaterial,
			// TODO בשניייה בלי כתר
			Material leafMaterial, boolean mixedCrown) {
		double x = basePoint.getX();
		double y = basePoint.getY();
		double z = basePoint.getZ();

		// Create trunk (cube made of polygons)
		createCube(scene, new Point(x, y + trunkHeight / 2, z), trunkWidth, new Color(101, 67, 33), trunkMaterial);

		// Create crown
		if (mixedCrown) {
			// Mixed crown: sphere + triangles
			scene.geometries.add(new Sphere(new Point(x, y + trunkHeight + 8, z), 12d)
					.setEmission(new Color(34, 139, 34)).setMaterial(leafMaterial));

			// Add some triangular leaves
			for (int i = 0; i < 8; i++) {
				double angle = i * Math.PI / 4;
				double leafX = x + 15 * Math.cos(angle);
				double leafZ = z + 15 * Math.sin(angle);
				scene.geometries.add(new Triangle(new Point(leafX, y + trunkHeight + 5, leafZ),
						new Point(leafX + 3, y + trunkHeight + 12, leafZ + 3),
						new Point(leafX - 3, y + trunkHeight + 12, leafZ - 3)).setEmission(new Color(50, 150, 50))
						.setMaterial(leafMaterial));
			}
		} else {
			// Sphere crown only
			scene.geometries.add(new Sphere(new Point(x, y + trunkHeight + 10, z), 15d)
					.setEmission(new Color(34, 139, 34)).setMaterial(leafMaterial));
		}
	}

	/**
	 * ` Creates random grass patches in the park area
	 * 
	 * @param scene         the scene to add the grass patches to
	 * @param count         number of grass patches to create
	 * @param grassMaterial material for the grass patches
	 */
	private void createGrassPatches(Scene scene, int count, Material grassMaterial) {
		Random random = new Random(42); // Fixed seed for reproducible results

		for (int i = 0; i < count; i++) {
			double x = -180 + random.nextDouble() * 360; // Random x between -180 and 180
			double z = -380 + random.nextDouble() * 310; // Random z between -380 and -70

			// Skip area around the lake
			if (Math.sqrt(x * x + (z + 200) * (z + 200)) > 50) {
				double size = 2 + random.nextDouble() * 3; // Random size between 2 and 5
				scene.geometries.add(new Triangle(new Point(x, -99, z), new Point(x + size, -99, z),
						new Point(x + size / 2, -99, z + size))
						.setEmission(
								new Color(20 + random.nextInt(40), 120 + random.nextInt(60), 20 + random.nextInt(40)))
						.setMaterial(grassMaterial));
			}
		}
	}

	/**
	 * Creates a park bench at the specified position
	 * 
	 * @param scene         the scene to add the bench to
	 * @param position      the position of the bench
	 * @param benchMaterial material for the bench
	 */
	private void createBench(Scene scene, Point position, Material benchMaterial) {
		double x = position.getX();
		double y = position.getY();
		double z = position.getZ();

		// Bench seat
		createCube(scene, new Point(x, y + 5, z), 25, 3, 8, new Color(139, 69, 19), benchMaterial);

		// Bench back
		createCube(scene, new Point(x, y + 12, z - 3), 25, 3, 12, new Color(139, 69, 19), benchMaterial);

		// Bench legs
		createCube(scene, new Point(x - 10, y + 2, z - 2), 2, 2, 4, new Color(139, 69, 19), benchMaterial);
		createCube(scene, new Point(x + 10, y + 2, z - 2), 2, 2, 4, new Color(139, 69, 19), benchMaterial);
		createCube(scene, new Point(x - 10, y + 2, z + 2), 2, 2, 4, new Color(139, 69, 19), benchMaterial);
		createCube(scene, new Point(x + 10, y + 2, z + 2), 2, 2, 4, new Color(139, 69, 19), benchMaterial);
	}

	/**
	 * Creates a flower bed with random flowers around a center point
	 * 
	 * @param scene          the scene to add the flower bed to
	 * @param center         the center point of the flower bed
	 * @param flowerCount    number of flowers in the bed
	 * @param flowerMaterial material for the flowers
	 */
	private void createFlowerBed(Scene scene, Point center, int flowerCount, Material flowerMaterial) {
		Random random = new Random(center.hashCode()); // Different seed per flower bed

		for (int i = 0; i < flowerCount; i++) {
			double x = center.getX() + (random.nextDouble() - 0.5) * 20;
			double z = center.getZ() + (random.nextDouble() - 0.5) * 20;

			// Flower stem
			scene.geometries
					.add(new Triangle(new Point(x, -100, z), new Point(x + 0.5, -95, z), new Point(x - 0.5, -95, z))
							.setEmission(new Color(34, 139, 34)).setMaterial(flowerMaterial));

			// Flower head (small sphere)
			Color[] flowerColors = { new Color(255, 20, 147), // Pink
					new Color(255, 255, 0), // Yellow
					new Color(255, 69, 0), // Red-orange
					new Color(138, 43, 226), // Blue-violet
					new Color(255, 255, 255) // White
			};

			scene.geometries.add(new Sphere(new Point(x, -94, z), 1.5)
					.setEmission(flowerColors[random.nextInt(flowerColors.length)]).setMaterial(flowerMaterial));
		}
	}

	/**
	 * Creates a winding path using rectangles as segments
	 * 
	 * @param scene        the scene to add the path to
	 * @param pathMaterial the material for the path segments
	 */

	private void createPath(Scene scene, Material pathMaterial) {
		// Path segments as rectangles
		Point[] pathPoints = { new Point(-150, -99.5, -80), new Point(-100, -99.5, -120), new Point(-50, -99.5, -140),
				new Point(0, -99.5, -160), new Point(50, -99.5, -180), new Point(100, -99.5, -220),
				new Point(120, -99.5, -280), new Point(80, -99.5, -320) };

		for (int i = 0; i < pathPoints.length - 1; i++) {
			Point p1 = pathPoints[i];
			Point p2 = pathPoints[i + 1];

			// Create path segment
			Vector direction = new Vector(p2.getX() - p1.getX(), 0, p2.getZ() - p1.getZ()).normalize();
			Vector perpendicular = new Vector(-direction.getZ(), 0, direction.getX()).scale(4); // Path width

			scene.geometries.add(new Polygon(
					new Point(p1.getX() + perpendicular.getX(), p1.getY(), p1.getZ() + perpendicular.getZ()),
					new Point(p1.getX() - perpendicular.getX(), p1.getY(), p1.getZ() - perpendicular.getZ()),
					new Point(p2.getX() - perpendicular.getX(), p2.getY(), p2.getZ() - perpendicular.getZ()),
					new Point(p2.getX() + perpendicular.getX(), p2.getY(), p2.getZ() + perpendicular.getZ()))
					.setEmission(new Color(160, 160, 160)).setMaterial(pathMaterial));
		}
	}

	/**
	 * Creates a cube with specified width, height, and depth
	 * 
	 * @param scene    the scene to add the cube to
	 * @param center   the center point of the cube
	 * @param width    the width of the cube
	 * @param height   the height of the cube
	 * @param depth    the depth of the cube
	 * @param color    the color of the cube
	 * @param material the material of the cube
	 */

	private void createCube(Scene scene, Point center, double width, double height, double depth, Color color,
			Material material) {
		double halfW = width / 2;
		double halfH = height / 2;
		double halfD = depth / 2;
		double x = center.getX();
		double y = center.getY();
		double z = center.getZ();

		// 6 faces of the cube
		scene.geometries.add(
				// Bottom
				new Polygon(new Point(x - halfW, y - halfH, z - halfD), new Point(x + halfW, y - halfH, z - halfD),
						new Point(x + halfW, y - halfH, z + halfD), new Point(x - halfW, y - halfH, z + halfD))
						.setEmission(color).setMaterial(material),
				// Top
				new Polygon(new Point(x - halfW, y + halfH, z - halfD), new Point(x - halfW, y + halfH, z + halfD),
						new Point(x + halfW, y + halfH, z + halfD), new Point(x + halfW, y + halfH, z - halfD))
						.setEmission(color).setMaterial(material),
				// Front
				new Polygon(new Point(x - halfW, y - halfH, z + halfD), new Point(x + halfW, y - halfH, z + halfD),
						new Point(x + halfW, y + halfH, z + halfD), new Point(x - halfW, y + halfH, z + halfD))
						.setEmission(color).setMaterial(material),
				// Back
				new Polygon(new Point(x + halfW, y - halfH, z - halfD), new Point(x - halfW, y - halfH, z - halfD),
						new Point(x - halfW, y + halfH, z - halfD), new Point(x + halfW, y + halfH, z - halfD))
						.setEmission(color).setMaterial(material),
				// Left
				new Polygon(new Point(x - halfW, y - halfH, z - halfD), new Point(x - halfW, y - halfH, z + halfD),
						new Point(x - halfW, y + halfH, z + halfD), new Point(x - halfW, y + halfH, z - halfD))
						.setEmission(color).setMaterial(material),
				// Right
				new Polygon(new Point(x + halfW, y - halfH, z + halfD), new Point(x + halfW, y - halfH, z - halfD),
						new Point(x + halfW, y + halfH, z - halfD), new Point(x + halfW, y + halfH, z + halfD))
						.setEmission(color).setMaterial(material));
	}

	/**
	 * Creates a cube with equal width, height, and depth
	 * 
	 * @param scene    the scene to add the cube to
	 * @param center   the center point of the cube
	 * @param size     the size of the cube (width = height = depth)
	 * @param color    the color of the cube
	 * @param material the material of the cube
	 */
	private void createCube(Scene scene, Point center, double size, Color color, Material material) {
		createCube(scene, center, size, size, size, color, material);
	}
}

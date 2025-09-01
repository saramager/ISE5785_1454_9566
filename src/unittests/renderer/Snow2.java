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

class Snow2 {

//	/**
//	 * Helper method to create a simple house using six polygons (a cube with a
//	 * pyramid-like roof).
//	 *
//	 * @param scene      The scene to add the house to.
//	 * @param center     The center point of the house's base.
//	 * @param width      The width of the house's base.
//	 * @param height     The height of the house's walls.
//	 * @param roofHeight The height of the roof.
//	 * @param houseColor The color of the house's walls.
//	 * @param roofColor  The color of the roof.
//	 * @param material   The material properties for the house.
//	 */
//	private static void createHouse(Scene scene, Point center, double width, double height, double roofHeight,
//			Color houseColor, Color roofColor, Material material) {
//		double halfWidth = width / 2;
//		double halfHeight = height / 2;
//		double x = center.getX();
//		double y = center.getY();
//		double z = center.getZ();
//
//		// Base cube (walls)
//		// Back face
//		scene.geometries.add(new Polygon(new Point(x - halfWidth, y - halfHeight, z - halfWidth),
//				new Point(x + halfWidth, y - halfHeight, z - halfWidth),
//				new Point(x + halfWidth, y + halfHeight, z - halfWidth),
//				new Point(x - halfWidth, y + halfHeight, z - halfWidth)).setEmission(houseColor).setMaterial(material));
//		// Front face
//		scene.geometries.add(new Polygon(new Point(x - halfWidth, y - halfHeight, z + halfWidth),
//				new Point(x - halfWidth, y + halfHeight, z + halfWidth),
//				new Point(x + halfWidth, y + halfHeight, z + halfWidth),
//				new Point(x + halfWidth, y - halfHeight, z + halfWidth)).setEmission(houseColor).setMaterial(material));
//		// Right face
//		scene.geometries.add(new Polygon(new Point(x + halfWidth, y - halfHeight, z - halfWidth),
//				new Point(x + halfWidth, y - halfHeight, z + halfWidth),
//				new Point(x + halfWidth, y + halfHeight, z + halfWidth),
//				new Point(x + halfWidth, y + halfHeight, z - halfWidth)).setEmission(houseColor).setMaterial(material));
//		// Left face
//		scene.geometries.add(new Polygon(new Point(x - halfWidth, y - halfHeight, z + halfWidth),
//				new Point(x - halfWidth, y - halfHeight, z - halfWidth),
//				new Point(x - halfWidth, y + halfHeight, z - halfWidth),
//				new Point(x - halfWidth, y + halfHeight, z + halfWidth)).setEmission(houseColor).setMaterial(material));
//
//		// Roof (two triangles)
//		scene.geometries.add(new Triangle(new Point(x - halfWidth, y + halfHeight, z - halfWidth),
//				new Point(x + halfWidth, y + halfHeight, z - halfWidth), new Point(x, y + halfHeight + roofHeight, z))
//				.setEmission(roofColor).setMaterial(material));
//
//		scene.geometries.add(new Triangle(new Point(x - halfWidth, y + halfHeight, z + halfWidth),
//				new Point(x + halfWidth, y + halfHeight, z + halfWidth), new Point(x, y + halfHeight + roofHeight, z))
//				.setEmission(roofColor).setMaterial(material));
//	}
//
//	/**
//	 * Helper method to create a cone-shaped tree.
//	 *
//	 * @param scene    The scene to add the tree to.
//	 * @param center   The center point of the tree's base.
//	 * @param radius   The radius of the tree's base.
//	 * @param height   The height of the tree.
//	 * @param color    The color of the tree.
//	 * @param material The material properties for the tree.
//	 */
//	private static void createTree(Scene scene, Point center, double radius, double height, Color color,
//			Material material) {
//		// A simple cone can be represented by a single triangle from the top to two
//		// points on the base.
//		// We will make the house's roof use this helper to create a more triangular
//		// roof as seen in the image.
//		scene.geometries.add(new Triangle(new Point(center.getX() - radius, center.getY(), center.getZ() + radius), // Left
//																													// point
//																													// on
//																													// the
//																													// base
//				new Point(center.getX() + radius, center.getY(), center.getZ() + radius), // Right point on the base
//				new Point(center.getX(), center.getY() + height, center.getZ())) // Top point
//				.setEmission(color).setMaterial(material));
//	}
//
//	/**
//	 * Main test method to build and render the snowglobe scene.
//	 */
//	@Test
//	public void snowglobeTest() {
//		// Scene setup
//		Scene scene = new Scene("Snowglobe Scene").setBackground(new Color(20, 20, 30));
//		scene.setAmbientLight(new AmbientLight(new Color(10, 10, 12))); // Further reduced ambient light
//
//		// Materials
//		// Changed to have transparency only, no reflection
//		Material glassMaterial = new Material().setKD(0.1).setKS(0.9).setShininess(200).setKT(0.95);
//		Material snowMaterial = new Material().setKD(0.8).setKS(0.2).setShininess(30).setKR(0.1);
//		Material woodMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(50);
//		Material redConeMat = new Material().setKD(0.8).setKS(0.2).setShininess(30);
//		Material greenSphereMat = new Material().setKD(0.8).setKS(0.2).setShininess(30);
//		Material orangeSphereMat = new Material().setKD(0.8).setKS(0.2).setShininess(30);
//		Material blueCubeMat = new Material().setKD(0.8).setKS(0.2).setShininess(30).setKT(0.6);
//		Material lightSourceMat = new Material().setKD(0.0).setKS(0.0).setShininess(0); // For the light emitting object
//
//		// Geometries
//		// The snowglobe itself (a sphere that is transparent)
//		scene.geometries.add(
//				new Sphere(new Point(0, -2, -20), 10.0).setEmission(new Color(25, 25, 25)).setMaterial(glassMaterial));
//
//		// The base of the snowglobe
//		createCube(scene, new Point(0, -14, -20), 12, new Color(150, 100, 70), woodMaterial);
//
//		// Inside the snowglobe
//		// A mound of snow
//		scene.geometries.add(new Sphere(new Point(0, -7.5, -20), 5.0) // Adjusted snow position to be higher
//				.setEmission(new Color(255, 255, 255)) // Changed snow color to a bright white
//				.setMaterial(snowMaterial));
//
//		// The house - made larger and positioned more centrally and higher up
//		createHouse(scene, new Point(0, -6.5, -20), 6, 6, 4, new Color(180, 150, 120), new Color(255, 255, 255),
//				snowMaterial); // adjusted position and color
//
//		// The trees - made larger and positioned differently
//		// Left tree - adjusted position to be higher
//		scene.geometries.add(new Sphere(new Point(-4, -7, -23), 2.5).setEmission(new Color(0, 150, 0)) // Brighter tree
//																										// color
//				.setMaterial(snowMaterial));
//		// Right tree - adjusted position to be higher
//		scene.geometries.add(new Sphere(new Point(4, -7, -17), 2.5).setEmission(new Color(0, 150, 0)) // Brighter tree
//																										// color
//				.setMaterial(snowMaterial));
//
//		// Other geometries outside the snowglobe
//		// Pinkish cone on the left
//		scene.geometries.add(new Triangle(new Point(-18, -12, -20), new Point(-18, -12, -22), new Point(-18, -8, -21))
//				.setEmission(new Color(200, 100, 100)).setMaterial(redConeMat));
//
//		// Green sphere on the left
//		scene.geometries.add(new Sphere(new Point(-14, -10, -20), 2.0).setEmission(new Color(0, 150, 0))
//				.setMaterial(greenSphereMat));
//
//		// Orange sphere on the right
//		scene.geometries.add(new Sphere(new Point(14, -10, -20), 2.0).setEmission(new Color(255, 128, 0))
//				.setMaterial(orangeSphereMat));
//
//		// Cyan cube on the right
//		createCube(scene, new Point(18, -10, -20), 4, new Color(0, 200, 200), blueCubeMat);
//
//		// Light sources
//		// A main light source from the top-left
//		scene.lights.add(new PointLight(new Color(63, 63, 63), new Point(-20, 20, 0)) // Reduced intensity
//				.setKl(0.0001).setKq(0.00001));
//
//		// A second, warmer light from the top-right
//		scene.lights.add(new PointLight(new Color(50, 37, 25), new Point(20, 20, -5)) // Reduced intensity
//				.setKl(0.0001).setKq(0.00001));
//
//		// A small light source to create the bright reflection inside the globe
//		scene.lights.add(new SpotLight(new Color(63, 63, 63), new Point(0, 10, -10), new Vector(0, -1, 0)) // Reduced
//																											// intensity
//				.setKl(0.0001).setKq(0.00001).setNarrowBeam(1));
//
//		// The "light" object inside the snowglobe - it's just a sphere with high
//		// emission and no material properties
//		scene.geometries.add(new Sphere(new Point(0, -2, -20), 1.0).setEmission(new Color(255, 255, 255))
//				.setMaterial(lightSourceMat));
//
//		// Camera setup
//		Camera camera = Camera.getBuilder().setLocation(new Point(0, 5, 25)) // Changed location to prevent extreme
//																				// distortion
//				.setDirection(new Point(0, -5, -20), Vector.AXIS_Y).setVpDistance(20).setVpSize(20, 20)
//				.setRayTracer(scene, RayTracerType.GRID).setResolution(1000, 1000).setMultithreading(-2)
//				.setDebugPrint(0.1).build();
//
//		camera.renderImage().writeToImage("Snowglobe_Render_V8");
//	}
//
//	/**
//	 * Helper method to create a cube using 6 polygons (faces).
//	 * 
//	 * @param scene    the scene to add the cube to
//	 * @param center   the center point of the cube
//	 * @param size     the size of the cube
//	 * @param color    the color of the cube
//	 * @param material the material of the cube
//	 */
//	private static void createCube(Scene scene, Point center, double size, Color color, Material material) {
//		double half = size / 2;
//		double x = center.getX();
//		double y = center.getY();
//		double z = center.getZ();
//
//		// Bottom face
//		scene.geometries
//				.add(new Polygon(new Point(x - half, y - half, z - half), new Point(x + half, y - half, z - half),
//						new Point(x + half, y - half, z + half), new Point(x - half, y - half, z + half))
//						.setEmission(color).setMaterial(material));
//
//		// Top face
//		scene.geometries
//				.add(new Polygon(new Point(x - half, y + half, z - half), new Point(x - half, y + half, z + half),
//						new Point(x + half, y + half, z + half), new Point(x + half, y + half, z - half))
//						.setEmission(color).setMaterial(material));
//
//		// Front face
//		scene.geometries
//				.add(new Polygon(new Point(x - half, y - half, z + half), new Point(x + half, y - half, z + half),
//						new Point(x + half, y + half, z + half), new Point(x - half, y + half, z + half))
//						.setEmission(color).setMaterial(material));
//
//		// Back face
//		scene.geometries
//				.add(new Polygon(new Point(x + half, y - half, z - half), new Point(x - half, y - half, z - half),
//						new Point(x - half, y + half, z - half), new Point(x + half, y + half, z - half))
//						.setEmission(color).setMaterial(material));
//
//		// Left face
//		scene.geometries
//				.add(new Polygon(new Point(x - half, y - half, z - half), new Point(x - half, y - half, z + half),
//						new Point(x - half, y + half, z + half), new Point(x - half, y + half, z - half))
//						.setEmission(color).setMaterial(material));
//
//		// Right face
//		scene.geometries
//				.add(new Polygon(new Point(x + half, y - half, z + half), new Point(x + half, y - half, z - half),
//						new Point(x + half, y + half, z - half), new Point(x + half, y + half, z + half))
//						.setEmission(color).setMaterial(material));
//	}

	private final Camera.Builder camera = Camera.getBuilder().setLocation(new Point(0, 30, 80))
			.setDirection(new Point(0, -50, -150), Vector.AXIS_Y).setVpDistance(600).setVpSize(500, 500);

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
		Material snowMaterial = new Material().setKD(0.4).setKS(0.3).setShininess(20);
		Material decorationMaterial = new Material().setKD(0.4).setKT(0).setKS(0.7).setShininess(80).setKR(0.2);
		Material sledMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(25).setKT(0);
		Material snowmanMaterial = new Material().setKD(0.8).setKS(0.2).setShininess(10);
		Material coalMaterial = new Material().setKD(0.9).setKS(0.1).setShininess(5);
		Material carrotMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(10);
		Material brownMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(40);

		// Snow globe base - Adjusted size and position for better fit
		double baseHeight = 30.0;
		double baseWidth = 80.0;
		double baseDepth = 75.0;
		double baseY = -95.0;
		double baseZ = -150.0;
		// Top face
		scene.geometries.add(new Polygon(new Point(-baseWidth / 2, baseY, baseZ - baseDepth / 2),
				new Point(baseWidth / 2, baseY, baseZ - baseDepth / 2),
				new Point(baseWidth / 2, baseY, baseZ + baseDepth / 2),
				new Point(-baseWidth / 2, baseY, baseZ + baseDepth / 2)).setEmission(new Color(139, 69, 19))
				.setMaterial(brownMaterial));
		// Front face
		scene.geometries.add(new Polygon(new Point(-baseWidth / 2, baseY, baseZ + baseDepth / 2),
				new Point(baseWidth / 2, baseY, baseZ + baseDepth / 2),
				new Point(baseWidth / 2, baseY - baseHeight, baseZ + baseDepth / 2),
				new Point(-baseWidth / 2, baseY - baseHeight, baseZ + baseDepth / 2))
				.setEmission(new Color(120, 50, 10)).setMaterial(brownMaterial));
		// Back face
		scene.geometries.add(new Polygon(new Point(baseWidth / 2, baseY, baseZ - baseDepth / 2),
				new Point(-baseWidth / 2, baseY, baseZ - baseDepth / 2),
				new Point(-baseWidth / 2, baseY - baseHeight, baseZ - baseDepth / 2),
				new Point(baseWidth / 2, baseY - baseHeight, baseZ - baseDepth / 2)).setEmission(new Color(110, 45, 5))
				.setMaterial(brownMaterial));
		// Right face
		scene.geometries.add(new Polygon(new Point(baseWidth / 2, baseY, baseZ + baseDepth / 2),
				new Point(baseWidth / 2, baseY, baseZ - baseDepth / 2),
				new Point(baseWidth / 2, baseY - baseHeight, baseZ - baseDepth / 2),
				new Point(baseWidth / 2, baseY - baseHeight, baseZ + baseDepth / 2)).setEmission(new Color(110, 45, 5))
				.setMaterial(brownMaterial));
		// Left face
		scene.geometries.add(new Polygon(new Point(-baseWidth / 2, baseY, baseZ - baseDepth / 2),
				new Point(-baseWidth / 2, baseY, baseZ + baseDepth / 2),
				new Point(-baseWidth / 2, baseY - baseHeight, baseZ + baseDepth / 2),
				new Point(-baseWidth / 2, baseY - baseHeight, baseZ - baseDepth / 2))
				.setEmission(new Color(120, 50, 10)).setMaterial(brownMaterial));

		// Snow globe dome - Lowered significantly to sit "in" the base
		scene.geometries.add(new Sphere(new Point(0, -70, -150), 42.0).setEmission(new Color(10, 15, 25))
				.setMaterial(transparentGlobe));

		// Snow particles - Adjusted y-range to fit new sphere position
		int snowCount = 3000;
		for (int i = 0; i < snowCount; i++) {
			double x = Math.random() * 80 - 40;
			double y = Math.random() * 70 - 40;
			double z = Math.random() * 80 - 190;
			double radius = Math.random() * 0.3 + 0.05;
			double distanceFromCenter = Math.sqrt(x * x + (y + 70) * (y + 70) + (z + 150) * (z + 150));
			if (distanceFromCenter < 41) { // Adjusted boundary for smaller globe
				scene.geometries.add(new Sphere(new Point(x, y, z), radius).setEmission(new Color(240, 245, 250))
						.setMaterial(snowMaterial));
			}
		}

		// Snow house - Positioned correctly on the base
		double houseWidth = 25.0;
		double houseDepth = 15.0;
		double houseHeight = 20.0;
		double houseY = -95.0; // Lowered significantly
		double houseX = 0.0;
		double houseZ = -150.0;
		// Front face
		scene.geometries.add(new Polygon(new Point(houseX - houseWidth / 2, houseY, houseZ),
				new Point(houseX + houseWidth / 2, houseY, houseZ),
				new Point(houseX + houseWidth / 2, houseY + houseHeight, houseZ),
				new Point(houseX - houseWidth / 2, houseY + houseHeight, houseZ)).setEmission(new Color(200, 200, 255))
				.setMaterial(snowmanMaterial));
		// Right face
		scene.geometries.add(new Polygon(new Point(houseX + houseWidth / 2, houseY, houseZ),
				new Point(houseX + houseWidth / 2, houseY, houseZ - houseDepth),
				new Point(houseX + houseWidth / 2, houseY + houseHeight, houseZ - houseDepth),
				new Point(houseX + houseWidth / 2, houseY + houseHeight, houseZ)).setEmission(new Color(190, 190, 245))
				.setMaterial(snowmanMaterial));
		// Left face
		scene.geometries.add(new Polygon(new Point(houseX - houseWidth / 2, houseY, houseZ),
				new Point(houseX - houseWidth / 2, houseY, houseZ - houseDepth),
				new Point(houseX - houseWidth / 2, houseY + houseHeight, houseZ - houseDepth),
				new Point(houseX - houseWidth / 2, houseY + houseHeight, houseZ)).setEmission(new Color(180, 180, 235))
				.setMaterial(snowmanMaterial));
		// Back face
		scene.geometries.add(new Polygon(new Point(houseX - houseWidth / 2, houseY, houseZ - houseDepth),
				new Point(houseX + houseWidth / 2, houseY, houseZ - houseDepth),
				new Point(houseX + houseWidth / 2, houseY + houseHeight, houseZ - houseDepth),
				new Point(houseX - houseWidth / 2, houseY + houseHeight, houseZ - houseDepth))
				.setEmission(new Color(100, 87, 119)).setMaterial(snowmanMaterial));
		// Roof
		double roofHeight = 15.0;
		double roofOffset = 8.0;
		scene.geometries.add(new Triangle(new Point(houseX - houseWidth / 2 - roofOffset, houseY + houseHeight, houseZ),
				new Point(houseX + houseWidth / 2 + roofOffset, houseY + houseHeight, houseZ),
				new Point(houseX, houseY + houseHeight + roofHeight, houseZ - houseDepth / 2))
				.setEmission(new Color(200, 50, 50)).setMaterial(decorationMaterial));
		scene.geometries.add(new Triangle(new Point(houseX + houseWidth / 2 + roofOffset, houseY + houseHeight, houseZ),
				new Point(houseX + houseWidth / 2 + roofOffset, houseY + houseHeight, houseZ - houseDepth),
				new Point(houseX, houseY + houseHeight + roofHeight, houseZ - houseDepth / 2))
				.setEmission(new Color(180, 40, 40)).setMaterial(decorationMaterial));
		scene.geometries.add(
				new Triangle(new Point(houseX + houseWidth / 2 + roofOffset, houseY + houseHeight, houseZ - houseDepth),
						new Point(houseX - houseWidth / 2 - roofOffset, houseY + houseHeight, houseZ - houseDepth),
						new Point(houseX, houseY + houseHeight + roofHeight, houseZ - houseDepth / 2))
						.setEmission(new Color(160, 30, 30)).setMaterial(decorationMaterial));
		scene.geometries.add(
				new Triangle(new Point(houseX - houseWidth / 2 - roofOffset, houseY + houseHeight, houseZ - houseDepth),
						new Point(houseX - houseWidth / 2 - roofOffset, houseY + houseHeight, houseZ),
						new Point(houseX, houseY + houseHeight + roofHeight, houseZ - houseDepth / 2))
						.setEmission(new Color(190, 45, 45)).setMaterial(decorationMaterial));

		// Snowman - Positioned correctly on the base
		double snowmanX = 25.0;
		double snowmanY = -93.0;
		double snowmanZ = -145.0;
		double snowmanRadiusBase = 5.0;
		double snowmanRadiusMid = 4.0;
		double snowmanRadiusHead = 3.0;

		scene.geometries.add(new Sphere(new Point(snowmanX, snowmanY, snowmanZ), snowmanRadiusBase)
				.setEmission(new Color(220, 220, 220)).setMaterial(snowmanMaterial));
		scene.geometries
				.add(new Sphere(new Point(snowmanX, snowmanY + snowmanRadiusBase + snowmanRadiusMid - 1, snowmanZ),
						snowmanRadiusMid).setEmission(new Color(220, 220, 220)).setMaterial(snowmanMaterial));
		scene.geometries.add(new Sphere(new Point(snowmanX,
				snowmanY + snowmanRadiusBase + 2 * snowmanRadiusMid + snowmanRadiusHead - 2.5, snowmanZ),
				snowmanRadiusHead).setEmission(new Color(220, 220, 220)).setMaterial(snowmanMaterial));

		// Snowman details - Adjusted positions
		scene.geometries.add(new Sphere(new Point(snowmanX, snowmanY + 3, snowmanZ - 3), 0.5)
				.setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(snowmanX, snowmanY + 7, snowmanZ - 3), 0.5)
				.setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(snowmanX, snowmanY + 11, snowmanZ - 3), 0.5)
				.setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(snowmanX - 1, snowmanY + 13, snowmanZ - 2.5), 0.5)
				.setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(snowmanX + 1, snowmanY + 13, snowmanZ - 2.5), 0.5)
				.setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(snowmanX, snowmanY + 12.5, snowmanZ - 2), 1.0)
				.setEmission(new Color(255, 140, 0)).setMaterial(carrotMaterial));

		// Tree - Positioned correctly on the base
		double treeX = -25.0;
		double treeY = -87.0; // Lowered significantly
		double treeZ = -150.0;
		double treeTrunkHeight = 27.0;
		double trunkWidth = 5.0;
		// Trunk (as polygons)
		scene.geometries.add(new Polygon(new Point(treeX - trunkWidth / 2, treeY, treeZ + trunkWidth / 2),
				new Point(treeX + trunkWidth / 2, treeY, treeZ + trunkWidth / 2),
				new Point(treeX + trunkWidth / 2, treeY - treeTrunkHeight, treeZ + trunkWidth / 2),
				new Point(treeX - trunkWidth / 2, treeY - treeTrunkHeight, treeZ + trunkWidth / 2))
				.setEmission(new Color(101, 67, 33)).setMaterial(sledMaterial));
		scene.geometries.add(new Polygon(new Point(treeX + trunkWidth / 2, treeY, treeZ + trunkWidth / 2),
				new Point(treeX + trunkWidth / 2, treeY, treeZ - trunkWidth / 2),
				new Point(treeX + trunkWidth / 2, treeY - treeTrunkHeight, treeZ - trunkWidth / 2),
				new Point(treeX + trunkWidth / 2, treeY - treeTrunkHeight, treeZ + trunkWidth / 2))
				.setEmission(new Color(101, 67, 33)).setMaterial(sledMaterial));
		scene.geometries.add(new Polygon(new Point(treeX - trunkWidth / 2, treeY, treeZ - trunkWidth / 2),
				new Point(treeX + trunkWidth / 2, treeY, treeZ - trunkWidth / 2),
				new Point(treeX + trunkWidth / 2, treeY - treeTrunkHeight, treeZ - trunkWidth / 2),
				new Point(treeX - trunkWidth / 2, treeY - treeTrunkHeight, treeZ - trunkWidth / 2))
				.setEmission(new Color(101, 67, 33)).setMaterial(sledMaterial));
		scene.geometries.add(new Polygon(new Point(treeX - trunkWidth / 2, treeY, treeZ - trunkWidth / 2),
				new Point(treeX - trunkWidth / 2, treeY, treeZ + trunkWidth / 2),
				new Point(treeX - trunkWidth / 2, treeY - treeTrunkHeight, treeZ + trunkWidth / 2),
				new Point(treeX - trunkWidth / 2, treeY - treeTrunkHeight, treeZ - trunkWidth / 2))
				.setEmission(new Color(101, 67, 33)).setMaterial(sledMaterial));

		// Cones for the tree - removed transparency issue
		double treeConeHeight1 = 15.0;
		double treeConeRadius1 = 15.0;
		double treeConeHeight2 = 10.0;
		double treeConeRadius2 = 10.0;
		double treeConeHeight3 = 8.0;
		double treeConeRadius3 = 8.0;

		scene.geometries.add(new Triangle(new Point(treeX - treeConeRadius1, treeY, treeZ),
				new Point(treeX + treeConeRadius1, treeY, treeZ), new Point(treeX, treeY + treeConeHeight1, treeZ))
				.setEmission(new Color(34, 139, 34)).setMaterial(decorationMaterial));
		scene.geometries.add(new Triangle(new Point(treeX - treeConeRadius2, treeY + treeConeHeight1 - 5, treeZ),
				new Point(treeX + treeConeRadius2, treeY + treeConeHeight1 - 5, treeZ),
				new Point(treeX, treeY + treeConeHeight1 + treeConeHeight2 - 5, treeZ))
				.setEmission(new Color(50, 150, 50)).setMaterial(decorationMaterial));
		scene.geometries.add(
				new Triangle(new Point(treeX - treeConeRadius3, treeY + treeConeHeight1 + treeConeHeight2 - 8, treeZ),
						new Point(treeX + treeConeRadius3, treeY + treeConeHeight1 + treeConeHeight2 - 8, treeZ),
						new Point(treeX, treeY + treeConeHeight1 + treeConeHeight2 + treeConeHeight3 - 8, treeZ))
						.setEmission(new Color(60, 160, 60)).setMaterial(decorationMaterial));

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
	public void SnowGlobeSceneSimpleThreadTest() {
		Scene scene = buildSnowGlobeScene();
		camera.setRayTracer(scene, RayTracerType.GRID).setResolution(800, 800).setMultithreading(-2).setDebugPrint(0.1)
				.build().renderImage().writeToImage("Snow Globe222 ");
	}
}

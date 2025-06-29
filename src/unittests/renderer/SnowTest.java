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
 * light sources. The scene includes a transparent sphere (snow globe), a plane
 * as the base, triangles for decorations, and multiple small spheres for snow
 * particles, demonstrating transparency and reflection effects.
 */
class SnowTest {

	private final Camera.Builder camera = Camera.getBuilder().setLocation(new Point(0, 30, 80)) // Positioned above and
																								// slightly forward
			.setDirection(new Point(0, -50, -150), Vector.AXIS_Y) // Looking slightly downward at the globe
			.setVpDistance(600).setVpSize(500, 500).setAntiAliasingRays(1);

	/**
	 * Test method to create a snow globe scene with various geometries and light
	 * sources.
	 */
	@Test
	public void SnowGlobeSceneTest() {
		Scene scene = new Scene("Snow Globe Test").setBackground(new Color(10, 10, 20))
				.setAmbientLight(new AmbientLight(new Color(15, 15, 15)));

		// Materials for different elements
		Material transparentGlobe = new Material().setKD(0.1).setKS(0.9).setShininess(100).setKT(0.8).setTAngle(1);
		Material reflectiveBase = new Material().setKD(0.5).setKS(0.5).setShininess(40).setKR(0.1).setKT(0);
		Material snowMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(30);
		Material decorationMaterial = new Material().setKD(0.4).setKS(0.7).setShininess(80).setKR(0.2);
		Material sledMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(25);

		// Materials for new characters
		Material polarBearMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(20); // White/cream for polar
																							// bear
		Material snowmanMaterial = new Material().setKD(0.8).setKS(0.2).setShininess(15); // Pure white for snowman
		Material coalMaterial = new Material().setKD(0.9).setKS(0.1).setShininess(5); // Dark for coal buttons/eyes
		Material carrotMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(10); // Orange for carrot nose
		Material starMaterial = new Material().setKD(0.8).setKS(0.3).setShininess(40);

		// Create the snow globe BASE (solid circular base)
		Material globeBaseMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(30).setKR(0.2);

		// Circular base of the snow globe (smaller thick disk)
		scene.geometries.add(new Sphere(new Point(0, -100, -150), 44.0).setEmission(new Color(80, 70, 60))
				.setMaterial(globeBaseMaterial)); // Smaller base

		// Create the snow globe DOME (transparent hemisphere - upper half only)
		scene.geometries.add(new Sphere(new Point(0, -50, -150), 50.0).setEmission(new Color(10, 15, 25))
				.setMaterial(transparentGlobe));

		// Create the outer base plane (floor where globe sits)
		scene.geometries
				.add(new Polygon(new Point(-100, -100, -300), new Point(100, -100, -300), new Point(100, -100, 0),
						new Point(-100, -100, 0)).setEmission(new Color(50, 40, 30)).setMaterial(reflectiveBase));

		// Add small spheres as snow particles inside the globe - FIXED CONDITIONS
		int snowCount = 150;
		for (int i = 0; i < snowCount; i++) {
			double x = Math.random() * 80 - 40; // Random x within globe (-40 to +40)
			double y = Math.random() * 40 - 90; // Random y within dome (-90 to -50)
			double z = Math.random() * 80 - 190; // Random z within globe (-190 to -110)
			double radius = Math.random() * 1.5 + 0.5; // Random radius between 0.5 and 2

			// Check if the snow particle is inside the sphere (distance from center <
			// radius)
			double distanceFromCenter = Math.sqrt(x * x + (y + 50) * (y + 50) + (z + 150) * (z + 150));
			if (distanceFromCenter < 45) { // Inside the globe (radius 50, but leave some margin)
				scene.geometries.add(new Sphere(new Point(x, y, z), radius).setEmission(new Color(240, 245, 250))
						.setMaterial(snowMaterial));
			}
		}

		// === POLAR BEAR === (moved to left side)
		scene.geometries.add(new Sphere(new Point(-30, -55, -140), 8.0).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));

		// head
		scene.geometries.add(new Sphere(new Point(-25, -45, -132), 6.0).setEmission(new Color(245, 245, 225))
				.setMaterial(polarBearMaterial));

		// ears
		scene.geometries.add(new Sphere(new Point(-22, -40, -130), 2.0).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-28, -40, -130), 2.0).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));

		// legs
		scene.geometries.add(new Sphere(new Point(-35, -58, -145), 3.5).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-25, -58, -145), 3.5).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-35, -58, -135), 3.5).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));
		scene.geometries.add(new Sphere(new Point(-25, -58, -135), 3.5).setEmission(new Color(220, 220, 210))
				.setMaterial(polarBearMaterial));

		// nose and eyes
		scene.geometries.add(new Sphere(new Point(-22, -44, -127), 0.8).setEmission(new Color(20, 20, 20))
				.setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(-24, -42, -127), 0.6).setEmission(new Color(20, 20, 20))
				.setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(-26, -42, -127), 0.6).setEmission(new Color(20, 20, 20))
				.setMaterial(coalMaterial));

		// === SNOWMAN === (moved to right side)
		// base
		scene.geometries.add(new Sphere(new Point(25, -58, -145), 7.0).setEmission(new Color(220, 220, 220))
				.setMaterial(snowmanMaterial));

		// middle
		scene.geometries.add(new Sphere(new Point(25, -48, -145), 5.5).setEmission(new Color(220, 220, 220))
				.setMaterial(snowmanMaterial));

		// head
		scene.geometries.add(new Sphere(new Point(25, -40, -145), 4.0).setEmission(new Color(220, 220, 220))
				.setMaterial(snowmanMaterial));

		// buttons
		scene.geometries.add(
				new Sphere(new Point(25, -50, -140), 0.8).setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(
				new Sphere(new Point(25, -46, -140), 0.8).setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(
				new Sphere(new Point(25, -55, -140), 0.8).setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));

		// eyes
		scene.geometries.add(
				new Sphere(new Point(23, -39, -142), 0.7).setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));
		scene.geometries.add(
				new Sphere(new Point(27, -39, -142), 0.7).setEmission(new Color(15, 15, 15)).setMaterial(coalMaterial));

		// nose
		scene.geometries.add(new Sphere(new Point(25, -41, -141), 0.6).setEmission(new Color(255, 140, 0))
				.setMaterial(carrotMaterial));

		// smile
		scene.geometries.add(new Sphere(new Point(23, -43, -141.5), 0.4).setEmission(new Color(15, 15, 15))
				.setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(25, -44, -141.5), 0.4).setEmission(new Color(15, 15, 15))
				.setMaterial(coalMaterial));
		scene.geometries.add(new Sphere(new Point(27, -43, -141.5), 0.4).setEmission(new Color(15, 15, 15))
				.setMaterial(coalMaterial));

		// === SNOW HOUSE === (moved to center)
		// Front wall
		scene.geometries.add(new Polygon(new Point(-5, -60, -150), new Point(5, -60, -150), new Point(5, -45, -150),
				new Point(-5, -45, -150)).setEmission(new Color(240, 240, 240)).setMaterial(snowmanMaterial));

		// Right wall
		scene.geometries.add(new Polygon(new Point(5, -60, -150), new Point(5, -60, -160), new Point(5, -45, -160),
				new Point(5, -45, -150)).setEmission(new Color(230, 230, 230)).setMaterial(snowmanMaterial));

		// Left wall
		scene.geometries.add(new Polygon(new Point(-5, -60, -150), new Point(-5, -60, -160), new Point(-5, -45, -160),
				new Point(-5, -45, -150)).setEmission(new Color(220, 220, 220)).setMaterial(snowmanMaterial));

		// Back wall
		scene.geometries.add(new Polygon(new Point(-5, -60, -160), new Point(5, -60, -160), new Point(5, -45, -160),
				new Point(-5, -45, -160)).setEmission(new Color(210, 210, 210)).setMaterial(snowmanMaterial));

		// Roof - 4 triangles forming a pyramid
		// Front triangle
		scene.geometries.add(new Triangle(new Point(-5, -45, -150), new Point(5, -45, -150), new Point(0, -35, -155))
				.setEmission(new Color(200, 50, 50)).setMaterial(decorationMaterial));
		// Right triangle
		scene.geometries.add(new Triangle(new Point(5, -45, -150), new Point(5, -45, -160), new Point(0, -35, -155))
				.setEmission(new Color(180, 40, 40)).setMaterial(decorationMaterial));
		// Back triangle
		scene.geometries.add(new Triangle(new Point(5, -45, -160), new Point(-5, -45, -160), new Point(0, -35, -155))
				.setEmission(new Color(160, 30, 30)).setMaterial(decorationMaterial));
		// Left triangle
		scene.geometries.add(new Triangle(new Point(-5, -45, -160), new Point(-5, -45, -150), new Point(0, -35, -155))
				.setEmission(new Color(190, 45, 45)).setMaterial(decorationMaterial));

		// Door
		scene.geometries.add(new Polygon(new Point(-2, -60, -149), new Point(2, -60, -149), new Point(2, -50, -149),
				new Point(-2, -50, -149)).setEmission(new Color(80, 60, 40)).setMaterial(coalMaterial));

		// Window
		scene.geometries
				.add(new Polygon(new Point(1.5, -52, -149), new Point(3.5, -52, -149), new Point(3.5, -48, -149),
						new Point(1.5, -48, -149)).setEmission(new Color(100, 150, 200)).setMaterial(transparentGlobe));

		// === IMPROVED TREE === (repositioned and scaled to fit inside globe)
		// Tree trunk
		scene.geometries.add(new Polygon(new Point(25, -75, -135), new Point(27, -75, -135), new Point(27, -68, -135),
				new Point(25, -68, -135)).setEmission(new Color(101, 67, 33)).setMaterial(sledMaterial));

		// Tree layers (3 layers getting smaller towards top)
		// Bottom layer
		scene.geometries.add(new Triangle(new Point(22, -68, -135), new Point(30, -68, -135), new Point(26, -62, -135))
				.setEmission(new Color(34, 139, 34)).setMaterial(decorationMaterial));
		// Middle layer
		scene.geometries.add(new Triangle(new Point(23, -64, -135), new Point(29, -64, -135), new Point(26, -58, -135))
				.setEmission(new Color(50, 150, 50)).setMaterial(decorationMaterial));
		// Top layer
		scene.geometries.add(new Triangle(new Point(24, -60, -135), new Point(28, -60, -135), new Point(26, -54, -135))
				.setEmission(new Color(60, 160, 60)).setMaterial(decorationMaterial));

		// === slider === (keep original position)
		scene.geometries
				.add(new Polygon(new Point(-35, -58, -130), new Point(-25, -58, -130), new Point(-25, -58, -120),
						new Point(-35, -58, -120)).setEmission(new Color(139, 69, 19)).setMaterial(sledMaterial));
		scene.geometries
				.add(new Polygon(new Point(-35, -59, -131), new Point(-34, -59, -131), new Point(-34, -59, -119),
						new Point(-35, -59, -119)).setEmission(new Color(100, 50, 20)).setMaterial(sledMaterial));
		scene.geometries
				.add(new Polygon(new Point(-26, -59, -131), new Point(-25, -59, -131), new Point(-25, -59, -119),
						new Point(-26, -59, -119)).setEmission(new Color(100, 50, 20)).setMaterial(sledMaterial));

		// Add some icicles hanging from the top of the globe
		scene.geometries.add(new Triangle(new Point(-10, -10, -140), new Point(-8, -10, -140), new Point(-9, -25, -140))
				.setEmission(new Color(200, 220, 255)).setMaterial(transparentGlobe));
		scene.geometries.add(new Triangle(new Point(8, -10, -155), new Point(10, -10, -155), new Point(9, -20, -155))
				.setEmission(new Color(200, 220, 255)).setMaterial(transparentGlobe));

		// Add five light sources
		scene.lights.add(new DirectionalLight(new Color(70, 70, 70), new Vector(0, -1, -0.5))); // Sun-like light
																								// from above
		scene.lights.add(new PointLight(new Color(75, 50, 25), new Point(0, 50, -100)).setKl(0.0001).setKq(0.00001)); // Warm
																														// light
																														// from
																														// above
		scene.lights.add(new SpotLight(new Color(25, 50, 75), new Point(-50, 20, -50), new Vector(1, -1, -1))
				.setKl(0.0001).setKq(0.00001));// Blue spotlight from left
		scene.lights.add(new SpotLight(new Color(50, 75, 25), new Point(50, 20, -50), new Vector(-1, -1, -1))
				.setKl(0.0001).setKq(0.00001));// Green spotlight from right
		scene.lights.add(new PointLight(new Color(100, 25, 25), new Point(0, -30, -200)).setKl(0.0001).setKq(0.00001)); // Red

		// Render with high resolution and regular grid ray tracer
		camera.setRayTracer(scene, RayTracerType.GRID).setResolution(800, 800).setGlossyAndDiffuseRays(11)
				.setMultithreading(-2).setDebugPrint(0.1).build().renderImage().writeToImage("Snow Globe Scene");
	}
}
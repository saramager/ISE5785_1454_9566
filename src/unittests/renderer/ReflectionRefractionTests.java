package unittests.renderer;

import static java.awt.Color.BLUE;
import static java.awt.Color.RED;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial shadows
 * (with transparency)
 * 
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
	/** Default constructor to satisfy JavaDoc generator */
	ReflectionRefractionTests() {
	}

	/** Scene for the tests */
	private final Scene scene = new Scene("Test scene");
	/** Camera builder for the tests with triangles */
	private final Camera.Builder cameraBuilder = Camera.getBuilder() //
			.setRayTracer(scene, RayTracerType.SIMPLE);

	/** Produce a picture of a sphere lighted by a spot light */
	@Test
	void twoSpheres() {
		scene.geometries.add( //
				new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
						.setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)), //
				new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
		scene.lights.add( //
				new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
						.setKl(0.0004).setKq(0.0000006));

		cameraBuilder.setLocation(new Point(0, 0, 1000)) //
				.setDirection(Point.ZERO, Vector.AXIS_Y) //
				.setVpDistance(1000).setVpSize(150, 150) //
				.setResolution(500, 500) //
				.build() //
				.renderImage() //
				.writeToImage("refractionTwoSpheres");
	}

	/** Produce a picture of a sphere lighted by a spot light */
	@Test
	void twoSpheresOnMirrors() {
		scene.geometries.add( //
				new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100)) //
						.setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
								.setKT(new Double3(0.5, 0, 0))), //
				new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20)) //
						.setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
				new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
						new Point(670, 670, 3000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKR(1)), //
				new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
						new Point(-1500, -1500, -2000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
		scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
		scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
				.setKl(0.00001).setKq(0.000005));

		cameraBuilder.setLocation(new Point(0, 0, 10000)) //
				.setDirection(Point.ZERO, Vector.AXIS_Y) //
				.setVpDistance(10000).setVpSize(2500, 2500) //
				.setResolution(500, 500) //
				.build() //
				.renderImage() //
				.writeToImage("reflectionTwoSpheresMirrored");
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light with a partially
	 * transparent Sphere producing partial shadow
	 */
	@Test
	void trianglesTransparentSphere() {
		scene.geometries.add(
				new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
				new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
				new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
						.setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
		scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
		scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1)).setKl(4E-5)
				.setKq(2E-7));

		cameraBuilder.setLocation(new Point(0, 0, 1000)) //
				.setDirection(Point.ZERO, Vector.AXIS_Y) //
				.setVpDistance(1000).setVpSize(200, 200) //
				.setResolution(600, 600) //
				.build() //
				.renderImage() //
				.writeToImage("refractionShadow");
	}

	/**
	 * Test for transparency, reflection, and shadow with a complex scene. This test
	 * includes a transparent polygon, a blue sphere that casts a shadow, a
	 * reflective triangle, and a shiny plane.
	 */

	@Test
	void transparencyReflectionShadow1Test() {
		scene.geometries.add(
				// Transparent polygon (high KT)
				new Polygon(new Point(-60, 0, -50), new Point(-30, 60, -50), new Point(30, 60, -50),
						new Point(60, 0, -50)).setEmission(new Color(0, 100, 150))
						.setMaterial(new Material().setKD(0.2).setKS(0.3).setShininess(100).setKT(0.7)),

				// Opaque blue sphere - will cast shadowל
				new Sphere(new Point(0, 30, -20), 20d).setEmission(new Color(BLUE))
						.setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(100).setKT(0.7)),

				// Reflective triangle (high KR)
				new Triangle(new Point(-70, -50, -40), new Point(70, -50, -40), new Point(0, 50, -40))
						.setEmission(new Color(100, 0, 100))
						.setMaterial(new Material().setKD(0.2).setKS(0.7).setShininess(300).setKR(0.8)),

				// Glossy plane
				new Plane(new Point(0, 0, -60), new Vector(0, 0, 1)).setEmission(new Color(10, 10, 10))
						.setMaterial(new Material().setKD(0.3).setKS(0.6).setShininess(100).setKR(0.6)));

		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		// Spot light - will emphasize shadow
		scene.lights.add(new SpotLight(new Color(1000, 600, 600), new Point(0, 100, 200), new Vector(0, -1, -1))
				.setKl(0.0001).setKq(0.00005));

		cameraBuilder.setLocation(new Point(0, 0, 300)) //
				.setDirection(Point.ZERO, Vector.AXIS_Y) //
				.setVpDistance(300).setVpSize(200, 200).setResolution(600, 600).build().renderImage()
				.writeToImage("transparencyReflectionShadow");
	}

	/**
	 * Test for transparency, reflection, and shadow with subtle changes to the
	 * materials and lighting.
	 */
	@Test
	void transparencyReflectionShadowTest() {
		scene.geometries.add(
				// Transparent polygon - we’ll turn it into relatively dark glass
				new Polygon(
						new Point(-60, 0, -50), new Point(-30, 60, -50), new Point(30, 60, -50), new Point(60, 0, -50))
						.setEmission(new Color(0, 30, 45)) // Soft blue-greenish tint, not strongly self-emissive
						.setMaterial(new Material().setKD(0.05) // Almost no diffusion – glass doesn’t scatter much
																// light
								.setKS(0.7).setShininess(250).setKT(0.8) // High transparency
								.setKR(0.1)), // Slight reflection to simulate glass surface

				// Blue sphere – we’ll make it completely opaque like a plastic ball
				new Sphere(new Point(0, 30, -20), 20d).setEmission(new Color(BLUE))
						.setMaterial(new Material().setKD(0.6).setKS(0.5).setShininess(100) // Plastic-like shine
								.setKT(0.0)), // Opaque – no light passes through it

				// Reflective triangle – more like a real mirror
				new Triangle(new Point(-70, -50, -40), new Point(70, -50, -40), new Point(0, 50, -40))
						.setEmission(new Color(0, 0, 0)) // Mirror does not emit light
						.setMaterial(new Material().setKD(0.01) // Almost no diffusion (not important for mirror)
								.setKS(0.05).setShininess(500).setKR(0.9)), // Very strong mirror reflectio

				// Glossy plane – polished floor
				new Plane(new Point(0, 0, -60), new Vector(0, 0, 1)).setEmission(new Color(20, 20, 20)) // צבע אפור כהה
																										// בסיסי של
																										// הרצפה
						.setMaterial(new Material().setKD(0.4) // Diffuses light (color comes from emission)
								.setKS(0.6).setShininess(100).setKR(0.3))); // Softer reflection, like polished floor
																			// not mirror
		scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));

		// Spot light – with slightly more attenuation
		scene.lights.add(new SpotLight(new Color(1000, 600, 600), new Point(0, 100, 200), new Vector(0, -1, -1))
				.setKl(0.001).setKq(0.0001)); // Slightly higher attenuation factors – light fades faster
		// causing it to be more focused around the source

		cameraBuilder.setLocation(new Point(0, 0, 300)).setDirection(Point.ZERO, Vector.AXIS_Y).setVpDistance(300)
				.setVpSize(200, 200).setResolution(600, 600).build().renderImage()
				.writeToImage("transparencyReflectionShadow_subtle_changes");
	}

	/**
	 * Test for transparency, reflection, and shadow with improved transparency
	 * settings.
	 */

	@Test
	void transparencyReflectionShadow2Test() {
		scene.geometries.add(
				// Transparent polygon – we turn it into glass with higher transparency and less
				// "light emission"ר
				new Polygon(
						new Point(-60, 0, -50), new Point(-30, 60, -50), new Point(30, 60, -50), new Point(60, 0, -50))
						.setEmission(new Color(0, 10, 15)) // Very subtle emission color, almost black, to not interfere
															// with transparency
						.setMaterial(new Material().setKD(0.02).setKS(0.8).setShininess(300) // Sharp gloss
								.setKT(0.95) // **Very high transparency – almost 100% transparent**
								.setKR(0.1)), // Slight mirror reflection on the glass surface
				// Blue sphere – made completely opaque like a plastic ball
				new Sphere(new Point(0, 30, -20), 20d).setEmission(new Color(BLUE)) // The sphere is blue because of its
																					// color
						.setMaterial(new Material().setKD(0.6) // Diffuses blue light
								.setKS(0.5).setShininess(100).setKT(0.0)), // Completely opaque– no light passes through

				// Reflective triangle – more like a real mirror
				new Triangle(new Point(-70, -50, -40), new Point(70, -50, -40), new Point(0, 50, -40))
						.setEmission(new Color(0, 0, 0)) // A mirror does not emit light
						.setMaterial(new Material().setKD(0.01).setKS(0.05).setShininess(500).setKR(0.9)), // Very
																											// strong
																											// mirror
																											// reflection

				// hiny plane – polished floor
				new Plane(new Point(0, 0, -60), new Vector(0, 0, 1)).setEmission(new Color(150, 150, 150)) // Light gray
																											// color

						.setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(100).setKR(0.2)),
				// Additional green sphere – to emphasize transparency behind the polygon
				new Sphere(new Point(0, 0, -80), 15d).setEmission(new Color(0, 200, 0)) // Bright green color
						.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)));

		scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));

		// Spot light – with a bit more attenuation
		scene.lights.add(new SpotLight(new Color(1000, 600, 600), new Point(0, 100, 200), new Vector(0, -1, -1))
				.setKl(0.001).setKq(0.0001)); // Slightly higher attenuation coefficients – the light fades faster,
		// making it more concentrated around the source.

		cameraBuilder.setLocation(new Point(0, 0, 300)).setDirection(Point.ZERO, Vector.AXIS_Y).setVpDistance(300)
				.setVpSize(200, 200).setResolution(600, 600).build().renderImage()
				.writeToImage("transparencyReflectionShadow_improved_transparency");
	}
}

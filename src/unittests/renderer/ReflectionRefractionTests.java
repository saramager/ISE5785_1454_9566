package unittests.renderer;

import static java.awt.Color.BLUE;
import static java.awt.Color.RED;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
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

	/**
	 * Builds and renders a complex scene with various geometries and lighting
	 * effects.
	 */
	@Test
	void renderComplexRoomNEW() {
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
				.setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(50).setKR(0.2)));

		scene.geometries.add(new Plane(new Point(0, 0, -80), new Vector(0, 0, 1)).setEmission(new Color(60, 60, 60))
				.setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30)));

		scene.geometries.add(new Plane(new Point(0, 0, 100), new Vector(0, 0, -1)).setEmission(new Color(50, 50, 50))
				.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20)));

		// Sphere - transparent green sphere
		scene.geometries.add(new Sphere(new Point(45, 15, 10), 10d).setEmission(new Color(0, 30, 0))
				.setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.9)));

		// points for the cube
		Point r1 = new Point(-60, 30, 0);
		Point r2 = new Point(-30, 60, 0);
		Point r3 = new Point(-10, 40, 0);
		Point r4 = new Point(-40, 10, 0);

		Point r5 = new Point(-60, 30, 30);
		Point r6 = new Point(-30, 60, 30);
		Point r7 = new Point(-10, 40, 30);
		Point r8 = new Point(-40, 10, 30);

		Material cubeMirrorMaterial = new Material().setKD(0.05).setKS(0.9).setShininess(150).setKR(0.95);
		Color cubeEmissionColor = new Color(0, 0, 40);

		scene.geometries
				.add(new Polygon(r1, r2, r3, r4).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial));

		scene.geometries
				.add(new Polygon(r5, r6, r7, r8).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial));
		scene.geometries
				.add(new Polygon(r1, r2, r6, r5).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial));

		scene.geometries
				.add(new Polygon(r4, r3, r7, r8).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial));

		scene.geometries
				.add(new Polygon(r1, r4, r8, r5).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial));
		scene.geometries
				.add(new Polygon(r2, r3, r7, r6).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial));

		int moveY = -66;
		int moveX = 10;

		Material baseMaterial = new Material().setKD(0.5).setKS(0.3).setShininess(60);
		Material transparentMaterial = new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.8);
		Material shinyMaterial = new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.5);

		// points for the base of the sculpture
		Point pb1 = new Point(-20 + moveX, 0 + moveY, 0);
		Point pb2 = new Point(20 + moveX, 0 + moveY, 0);
		Point pb3 = new Point(25 + moveX, 10 + moveY, 0);
		Point pb4 = new Point(20 + moveX, 25 + moveY, 0);
		Point pb5 = new Point(-20 + moveX, 25 + moveY, 0);
		Point pb6 = new Point(-25 + moveX, 10 + moveY, 0);

		Point pt1 = new Point(-20 + moveX, 0 + moveY, 15);
		Point pt2 = new Point(20 + moveX, 0 + moveY, 15);
		Point pt3 = new Point(25 + moveX, 10 + moveY, 15);
		Point pt4 = new Point(20 + moveX, 25 + moveY, 15);
		Point pt5 = new Point(-20 + moveX, 25 + moveY, 15);
		Point pt6 = new Point(-25 + moveX, 10 + moveY, 15);

		scene.geometries.add(new Polygon(pb1, pb2, pb3, pb4, pb5, pb6).setEmission(new Color(120, 90, 40))
				.setMaterial(baseMaterial));

		scene.geometries.add(new Polygon(pt1, pt2, pt3, pt4, pt5, pt6).setEmission(new Color(120, 90, 40))
				.setMaterial(baseMaterial));

		scene.geometries
				.add(new Polygon(pb1, pb2, pt2, pt1).setEmission(new Color(100, 70, 30)).setMaterial(baseMaterial));
		scene.geometries
				.add(new Polygon(pb2, pb3, pt3, pt2).setEmission(new Color(100, 70, 30)).setMaterial(baseMaterial));
		scene.geometries
				.add(new Polygon(pb3, pb4, pt4, pt3).setEmission(new Color(100, 70, 30)).setMaterial(baseMaterial));
		scene.geometries
				.add(new Polygon(pb4, pb5, pt5, pt4).setEmission(new Color(100, 70, 30)).setMaterial(baseMaterial));
		scene.geometries
				.add(new Polygon(pb5, pb6, pt6, pt5).setEmission(new Color(100, 70, 30)).setMaterial(baseMaterial));
		scene.geometries
				.add(new Polygon(pb6, pb1, pt1, pt6).setEmission(new Color(100, 70, 30)).setMaterial(baseMaterial));

		scene.geometries.add(new Triangle(new Point(-10 + moveX, 5 + moveY, 20), new Point(10 + moveX, 5 + moveY, 20),
				new Point(0 + moveX, 15 + moveY, 45)).setEmission(new Color(20, 0, 50))
				.setMaterial(transparentMaterial));

		scene.geometries.add(new Triangle(new Point(-5 + moveX, 10 + moveY, 30), new Point(5 + moveX, 10 + moveY, 30),
				new Point(0 + moveX, 0 + moveY, 50)).setEmission(new Color(50, 50, 0)).setMaterial(shinyMaterial));

		scene.geometries.add(
				new Sphere(new Point(-20 + moveX + 5, 0 + moveY + 5, 15 + 2), 2d).setEmission(new Color(255, 100, 0))

						.setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(10)));
		scene.geometries.add(
				new Sphere(new Point(25 + moveX - 5, 10 + moveY - 5, 15 + 2), 2d).setEmission(new Color(0, 200, 200))

						.setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(10)));
		scene.geometries.add(new Sphere(new Point(45, 15, 10), 4d).setEmission(new Color(200, 150, 0))
				.setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(10).setKT(0.3)));

		scene.geometries.add(new Plane(new Point(0, 50, -78), new Vector(0, 0, 1)).setEmission(new Color(70, 30, 0))
				.setMaterial(new Material().setKD(0.3).setKS(0.1).setShininess(20)));

		scene.geometries.add(new Sphere(new Point(-50, -25, 15), 8d).setEmission(new Color(150, 0, 0))
				.setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(10)));

		scene.lights.add(new PointLight(new Color(400, 250, 0), new Point(0, 0, 80)).setKl(0.0008).setKq(0.00008));
		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7)));
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8))
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));

		Camera c = cameraBuilder.setLocation(new Point(0, -140, 50))
				.setDirection(new Point(0, 0, 20), new Vector(0, 0, 1)).setVpDistance(100).setVpSize(150, 150)
				.setResolution(500, 500).build().renderImage().writeToImage("complexRoomScenePolygonsAndCube");
		new Camera.Builder(c).setRotation(45).build().renderImage().writeToImage("complexRoomScenePolygonsAndCubeRO");
		new Camera.Builder(c).setRotation(135).build().renderImage()
				.writeToImage("complexRoomScenePolygonsAndCubeRO_Y");

		new Camera.Builder(c).setTranslation(new Vector(30, 0, 0)).build().renderImage()
				.writeToImage("complexRoomScenePolygonsAndCubeMOVE");
		new Camera.Builder(c).setTranslation(new Vector(0, 0, 30)).build().renderImage()
				.writeToImage("complexRoomScenePolygonsAndCubeMOVE1");
		new Camera.Builder(c).setTranslation(new Vector(0, 0, 30)).setRotation(32).build().renderImage()
				.writeToImage("complexRoomScenePolygonsAndCubeMOVE2");

	}

}

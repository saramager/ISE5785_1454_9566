package unittests.renderer;

import static java.awt.Color.BLUE;
import static java.awt.Color.RED;

import org.junit.jupiter.api.Test;

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
		/* to satisfy JavaDoc generator */ }

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

	@Test
	void mySceneTest() {
		Scene scene = new Scene("My Test Scene");

		// הגדרות תאורה כללית
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		// גופים בסצנה
		scene.geometries.add(
				// כדור שקוף בלבד
				new Sphere(new Point(0, 0, -80), 30.0).setEmission(new Color(30, 144, 255)) // צבע תכלת בוהק
						.setMaterial(new Material().setKD(0.2).setKS(0.3).setShininess(100).setKT(0.6)),

				// מצלוע מחזיר אור
				new geometries.Polygon(new Point(60, -40, -100), new Point(90, 0, -100), new Point(60, 40, -100),
						new Point(30, 0, -100)).setEmission(new Color(255, 105, 180)) // ורוד-פוקסיה
						.setMaterial(new Material().setKR(0.6).setKS(0.4).setShininess(150)),

				// משולש רגיל עם צבע עז
				new Triangle(new Point(-80, -50, -120), new Point(-30, 50, -100), new Point(-60, 60, -130))
						.setEmission(new Color(0, 255, 127)) // ירוק-מנטה
						.setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(120)));

		// מקור אור בולט
		scene.lights.add(new SpotLight(new Color(1000, 600, 400), new Point(0, 100, 100), new Vector(0, -1, -2))
				.setKl(0.0004).setKq(0.0000006));

		// הגדרת מצלמה
		Camera.getBuilder().setRayTracer(scene, RayTracerType.SIMPLE).setLocation(new Point(0, 0, 1000))
				.setDirection(Point.ZERO, Vector.AXIS_Y).setVpDistance(1000).setVpSize(150, 150).setResolution(500, 500)
				.build().renderImage().writeToImage("mySceneTest");
	}

	@Test
	void mySceneTest_centerSphere() {
		Scene scene = new Scene("My Scene - Sphere Center");

		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		scene.geometries.add(
				// כדור שקוף במרכז מול המראה
				new Sphere(new Point(0, 0, -120), 40.0).setEmission(new Color(0, 255, 255)) // טורקיז זוהר
						.setMaterial(new Material().setKD(0.2).setKS(0.3).setShininess(100).setKT(0.6)),

				// מצלוע - מראה צבעונית
				new geometries.Polygon(new Point(-100, -60, -160), new Point(100, -60, -160), new Point(100, 60, -160),
						new Point(-100, 60, -160)).setEmission(new Color(255, 105, 180)) // ורוד-פוקסיה
						.setMaterial(new Material().setKR(1).setShininess(300)),

				// משולש בצד ימין, מוסיף צבע
				new Triangle(new Point(60, -50, -100), new Point(100, 0, -100), new Point(60, 50, -100))
						.setEmission(new Color(50, 255, 50))
						.setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(120)),

				// רקע כהה
				new geometries.Polygon(new Point(-300, -150, -200), new Point(300, -150, -200),
						new Point(300, 150, -200), new Point(-300, 150, -200)).setEmission(new Color(10, 10, 10)));

		scene.lights.add(new SpotLight(new Color(1000, 600, 400), new Point(150, 150, 200), new Vector(-1, -1, -3))
				.setKl(0.0003).setKq(0.0000004));

		Camera.getBuilder().setRayTracer(scene, RayTracerType.SIMPLE).setLocation(new Point(0, 0, 1000))
				.setDirection(Point.ZERO, Vector.AXIS_Y).setVpDistance(1000).setVpSize(300, 300).setResolution(500, 500)
				.build().renderImage().writeToImage("mySceneTest_centerSphere");
	}

	@Test
	void mySceneTest_sideSphere() {
		Scene scene = new Scene("My Scene - Sphere Side");

		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		scene.geometries.add(
				// כדור שקוף מימין (לא מסתיר את המראה)
				new Sphere(new Point(80, 0, -120), 40.0).setEmission(new Color(0, 255, 255))
						.setMaterial(new Material().setKD(0.2).setKS(0.3).setShininess(100).setKT(0.6)),

				// מצלוע - מראה צבעונית
				new geometries.Polygon(new Point(-100, -60, -160), new Point(100, -60, -160), new Point(100, 60, -160),
						new Point(-100, 60, -160)).setEmission(new Color(255, 105, 180))
						.setMaterial(new Material().setKR(1).setShininess(300)),

				// משולש בצד שמאל
				new Triangle(new Point(-100, -50, -100), new Point(-60, 0, -100), new Point(-100, 50, -100))
						.setEmission(new Color(50, 255, 50))
						.setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(120)),

				// רקע כהה
				new geometries.Polygon(new Point(-300, -150, -200), new Point(300, -150, -200),
						new Point(300, 150, -200), new Point(-300, 150, -200)).setEmission(new Color(10, 10, 10)));

		scene.lights.add(new SpotLight(new Color(1000, 600, 400), new Point(150, 150, 200), new Vector(-1, -1, -3))
				.setKl(0.0003).setKq(0.0000004));

		Camera.getBuilder().setRayTracer(scene, RayTracerType.SIMPLE).setLocation(new Point(0, 0, 1000))
				.setDirection(Point.ZERO, Vector.AXIS_Y).setVpDistance(1000).setVpSize(300, 300).setResolution(500, 500)
				.build().renderImage().writeToImage("mySceneTest_sideSphere");
	}

	@Test
	void threeNestedSpheres() {
		Scene scene = new Scene("My Scene - Sphere Center3");

		Point center = new Point(0, 0, -100);
		scene.geometries.add(
				new Sphere(center, 50.0).setEmission(new Color(BLUE))
						.setMaterial(new Material().setKD(0.2).setKS(0.3).setShininess(100).setKT(0.7)),

				new Sphere(center, 35.0).setEmission(new Color(BLUE))
						.setMaterial(new Material().setKD(0.3).setKS(0.3).setShininess(100).setKT(0.7)), // החזר
																											// חלקי

				new Sphere(center, 20.0).setEmission(new Color(RED))
						.setMaterial(new Material().setKD(0.2).setKS(0.3).setShininess(100).setKT(0.8))); // החזר גבוה

		scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20))); // תאורה סביבתית עם עוצמה נמוכה

		scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point(100, 100, 300), new Vector(-1, -1, -2))
				.setKl(0.0001).setKq(0.000005));

//		cameraBuilder.setLocation(new Point(0, 0, 100)).setDirection(Point.ZERO, Vector.AXIS_Y).setVpDistance(100)
//				.setVpSize(200, 200).setResolution(600, 600).build().renderImage().writeToImage("threeNestedSpheres");

		Camera.getBuilder().setRayTracer(scene, RayTracerType.SIMPLE).setLocation(new Point(0, 0, 1000))
				.setDirection(Point.ZERO, Vector.AXIS_Y).setVpDistance(1000).setVpSize(200, 200).setResolution(600, 600)
				.build().renderImage().writeToImage("threeNestedSpheres");
	}

	@Test
	void mySceneTest_reflectionAndTransparency() {
		Scene scene = new Scene("My Scene - Reflection & Transparency");

		scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20))); // הפחתת תאורה סביבתית

		scene.geometries.add(
				// מראה צבעונית במרכז
				new geometries.Polygon(new Point(-100, -60, -160), new Point(100, -60, -160), new Point(100, 60, -160),
						new Point(-100, 60, -160)).setEmission(new Color(255, 105, 180)) // פוקסיה
						.setMaterial(new Material().setKR(1).setShininess(300)),

				// כדור שקוף מימין – נראה דרכו
				new Sphere(new Point(80, 0, -120), 40.0).setEmission(new Color(0, 255, 255)) // טורקיז
						.setMaterial(new Material().setKD(0.2).setKS(0.3).setShininess(100).setKT(0.6)),

				// כדור אדום קטן מאחור (ייראה דרך השקיפות)
				new Sphere(new Point(80, 0, -180), 15.0).setEmission(new Color(255, 0, 0)) // אדום
						.setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(80)),

				// משולש כהה מעל המראה – יראה בהשתקפות
				new Triangle(new Point(-30, 70, -100), new Point(0, 90, -100), new Point(30, 70, -100))
						.setEmission(new Color(30, 30, 30))
						.setMaterial(new Material().setKD(0.3).setKS(0.4).setShininess(100)),

				// משולש ירוק משמאל – יוסיף צבע וצל
				new Triangle(new Point(-100, -50, -100), new Point(-60, 0, -100), new Point(-100, 50, -100))
						.setEmission(new Color(50, 255, 50))
						.setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(120)),

				// רקע כהה
				new geometries.Polygon(new Point(-300, -150, -200), new Point(300, -150, -200),
						new Point(300, 150, -200), new Point(-300, 150, -200)).setEmission(new Color(10, 10, 10)));

		// תאורה – עוצמה רכה יותר
		scene.lights.add(new SpotLight(new Color(500, 300, 200), new Point(150, 150, 200), new Vector(-1, -1, -3))
				.setKl(0.0005).setKq(0.0000005));

		Camera.getBuilder().setRayTracer(scene, RayTracerType.SIMPLE).setLocation(new Point(0, 0, 1000))
				.setDirection(Point.ZERO, Vector.AXIS_Y).setVpDistance(1000).setVpSize(300, 300).setResolution(500, 500)
				.build().renderImage().writeToImage("mySceneTest_reflectionAndTransparency");

	}
}

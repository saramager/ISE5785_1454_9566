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

	/**
	 * Test for transparency, reflection, and shadow with a complex scene. This test
	 * includes a transparent polygon, a blue sphere that casts a shadow, a
	 * reflective triangle, and a shiny plane.
	 */

	@Test
	void transparencyReflectionShadow1Test() {
		scene.geometries.add(
				// מצלוע שקוף (KT גבוה)
				new Polygon(new Point(-60, 0, -50), new Point(-30, 60, -50), new Point(30, 60, -50),
						new Point(60, 0, -50)).setEmission(new Color(0, 100, 150))
						.setMaterial(new Material().setKD(0.2).setKS(0.3).setShininess(100).setKT(0.7)),

				// כדור כחול לא שקוף - יטיל צל
				new Sphere(new Point(0, 30, -20), 20d).setEmission(new Color(BLUE))
						.setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(100).setKT(0.7)),

				// משולש מחזיר אור (KR גבוה)
				new Triangle(new Point(-70, -50, -40), new Point(70, -50, -40), new Point(0, 50, -40))
						.setEmission(new Color(100, 0, 100))
						.setMaterial(new Material().setKD(0.2).setKS(0.7).setShininess(300).setKR(0.8)),

				// מישור מבריק
				new Plane(new Point(0, 0, -60), new Vector(0, 0, 1)).setEmission(new Color(10, 10, 10))
						.setMaterial(new Material().setKD(0.3).setKS(0.6).setShininess(100).setKR(0.6)));

		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		// תאורת ספוט - תדגיש צל
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
				// מצולע שקוף - נהפוך אותו לזכוכית כהה יחסית
				new Polygon(
						new Point(-60, 0, -50), new Point(-30, 60, -50), new Point(30, 60, -50), new Point(60, 0, -50))
						.setEmission(new Color(0, 30, 45)) // גוון כחול-ירקרק עדין, לא מאיר בעצמו חזק
						.setMaterial(new Material().setKD(0.05) // כמעט ללא פיזור - זכוכית לא מפזרת אור
								.setKS(0.7) // ברק ספקולרי חזק על פני השטח
								.setShininess(250) // ברק חד
								.setKT(0.8) // שקוף מאוד
								.setKR(0.1)), // מעט השתקפות מראה כמו זכוכית

				// כדור כחול - נהפוך אותו לאטום לחלוטין כמו כדור פלסטיק
				new Sphere(new Point(0, 30, -20), 20d).setEmission(new Color(BLUE)) // הכדור כחול בגלל צבעו
						.setMaterial(new Material().setKD(0.6) // מפזר אור כחול
								.setKS(0.5) // מבריק
								.setShininess(100) // ברק של פלסטיק
								.setKT(0.0)), // **אטום לחלוטין** - אין מעבר אור דרכו

				// משולש מחזיר אור - מראה אמיתית יותר
				new Triangle(new Point(-70, -50, -40), new Point(70, -50, -40), new Point(0, 50, -40))
						.setEmission(new Color(0, 0, 0)) // **מראה אינה פולטת אור**
						.setMaterial(new Material().setKD(0.01) // כמעט ללא פיזור (לא משנה אם זה מראה)
								.setKS(0.05) // מינימלי, רוב ההשתקפות היא KR
								.setShininess(500) // חד מאוד אם בכל זאת יש KS
								.setKR(0.9)), // **השתקפות מראה חזקה מאוד**

				// מישור מבריק - רצפה מלוטשת
				new Plane(new Point(0, 0, -60), new Vector(0, 0, 1)).setEmission(new Color(20, 20, 20)) // צבע אפור כהה
																										// בסיסי של
																										// הרצפה
						.setMaterial(new Material().setKD(0.4) // מפזר אור (הצבע יבוא מהאמישן)
								.setKS(0.6) // מבריק
								.setShininess(100) // ברק סביר
								.setKR(0.3))); // השתקפות עדינה יותר כמו רצפה מלוטשת ולא מראה

		scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40))); // תאורת סביבה מעט חזקה יותר

		// תאורת ספוט - קצת יותר דעיכה
		scene.lights.add(new SpotLight(new Color(1000, 600, 600), new Point(0, 100, 200), new Vector(0, -1, -1))
				.setKl(0.001).setKq(0.0001)); // מקדמי דעיכה מעט גבוהים יותר - האור ידעך מהר יותר
												// מה שגורם לו להיות מרוכז יותר סביב המקור.

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
				// מצולע שקוף - נהפוך אותו לזכוכית שקופה יותר ופחות "פולטת" אור
				new Polygon(
						new Point(-60, 0, -50), new Point(-30, 60, -50), new Point(30, 60, -50), new Point(60, 0, -50))
						.setEmission(new Color(0, 10, 15)) // צבע אמישן עדין מאוד, כמעט שחור, כדי לא להפריע לשקיפות
						.setMaterial(new Material().setKD(0.02) // כמעט ללא פיזור
								.setKS(0.8) // השתקפות ספקולרית חזקה (ברק על הזכוכית)
								.setShininess(300) // ברק חד
								.setKT(0.95) // **שקיפות חזקה מאוד - כמעט 100% שקיפות**
								.setKR(0.1)), // מעט השתקפות מראה על פני הזכוכית

				// כדור כחול - נהפוך אותו לאטום לחלוטין כמו כדור פלסטיק
				new Sphere(new Point(0, 30, -20), 20d).setEmission(new Color(BLUE)) // הכדור כחול בגלל צבעו
						.setMaterial(new Material().setKD(0.6) // מפזר אור כחול
								.setKS(0.5) // מבריק
								.setShininess(100) // ברק של פלסטיק
								.setKT(0.0)), // **אטום לחלוטין** - אין מעבר אור דרכו

				// משולש מחזיר אור - מראה אמיתית יותר
				new Triangle(new Point(-70, -50, -40), new Point(70, -50, -40), new Point(0, 50, -40))
						.setEmission(new Color(0, 0, 0)) // **מראה אינה פולטת אור**
						.setMaterial(new Material().setKD(0.01) // כמעט ללא פיזור (לא משנה אם זה מראה)
								.setKS(0.05) // מינימלי, רוב ההשתקפות היא KR
								.setShininess(500) // חד מאוד אם בכל זאת יש KS
								.setKR(0.9)), // **השתקפות מראה חזקה מאוד**

				// מישור מבריק - רצפה מלוטשת
				new Plane(new Point(0, 0, -60), new Vector(0, 0, 1)).setEmission(new Color(150, 150, 150)) // צבע אפור
																											// בהיר
																											// בסיסי של
																											// הרצפה
						.setMaterial(new Material().setKD(0.4) // מפזר אור
								.setKS(0.6) // מבריק
								.setShininess(100) // ברק סביר
								.setKR(0.2)), // השתקפות עדינה יותר כמו רצפה מלוטשת ולא מראה

				// **כדור ירוק נוסף - כדי להדגיש את השקיפות מאחורי המצולע**
				new Sphere(new Point(0, 0, -80), 15d) // מיקום: X=0, Y=0, Z=-80 (מאחורי המצולע שנמצא ב-Z=-50)
						.setEmission(new Color(0, 200, 0)) // צבע ירוק בוהק
						.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)) // חומר רגיל
		);

		scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40))); // תאורת סביבה מעט חזקה יותר

		// תאורת ספוט - קצת יותר דעיכה
		scene.lights.add(new SpotLight(new Color(1000, 600, 600), new Point(0, 100, 200), new Vector(0, -1, -1))
				.setKl(0.001).setKq(0.0001)); // מקדמי דעיכה מעט גבוהים יותר - האור ידעך מהר יותר
												// מה שגורם לו להיות מרוכז יותר סביב המקור.

		cameraBuilder.setLocation(new Point(0, 0, 300)).setDirection(Point.ZERO, Vector.AXIS_Y).setVpDistance(300)
				.setVpSize(200, 200).setResolution(600, 600).build().renderImage()
				.writeToImage("transparencyReflectionShadow_improved_transparency");
	}
//	@Test
//	void transparencyReflectionShadow() {
//		scene.geometries.add(
//				// מישור אחורי מחזיר אור
//				new Polygon(new Point(-100, -100, -150), new Point(100, -100, -150), new Point(100, 100, -150),
//						new Point(-100, 100, -150)).setEmission(new Color(173, 216, 230)) // תכלת בהיר
//						.setMaterial(new Material().setKD(0.3).setKS(0.6).setShininess(100).setKR(0.6)),
//
//				// משולש / מצלוע קדמי שקוף למחצה
//				new Polygon(new Point(-80, -100, -50), new Point(80, -100, -50), new Point(0, 50, -50),
//						new Point(0, 50, -50)) // מקרב לטרפז
//						.setEmission(new Color(180, 50, 150))
//						.setMaterial(new Material().setKD(0.3).setKS(0.4).setShininess(60).setKT(0.4)),
//
//				// כדור כחול – אטום
//				new Sphere(new Point(0, 0, -20), 30d).setEmission(new Color(30, 30, 255))
//						.setMaterial(new Material().setKD(0.2).setKS(0.5).setShininess(100)),
//
//				// רצפה כהה מחזירה
//				new Polygon(new Point(-120, -120, -100), new Point(120, -120, -100), new Point(120, -120, -300),
//						new Point(-120, -120, -300)).setEmission(new Color(20, 20, 20))
//						.setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(100).setKR(0.5)));
//
//		scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
//
//		scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point(0, 100, 100), new Vector(0, -1, -1))
//				.setKl(1E-4).setKq(1E-5));
//
//		cameraBuilder.setLocation(new Point(0, 0, 300)).setDirection(Point.ZERO, Vector.AXIS_Y).setVpDistance(300)
//				.setVpSize(200, 200).setResolution(600, 600).build().renderImage()
//				.writeToImage("transparencyReflectionShadow");
//	}

}

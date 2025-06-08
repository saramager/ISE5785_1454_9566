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
	void renderComplexRoom() {
		// --- הגדרות סצנה בסיסיות ---
		scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25))); // אור סביבה עדין יותר

		// --- הוספת גופים (לפחות 10 גופים שונים) ---

		// 1. רצפה (מישור) - חומר מחזיר אור מעט, עם צבע עמום יותר
		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30)) // צבע
																													// פליטה
																													// כהה
																													// יותר
				.setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(50).setKR(0.2)));

		// 2. קיר אחורי (מישור) - חומר עמום
		scene.geometries.add(new Plane(new Point(0, 0, -80), new Vector(0, 0, 1)) // קיר קרוב יותר
				.setEmission(new Color(60, 60, 60)).setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30)));

		// 3. תקרה (מישור) - כדי שהחלק העליון לא יהיה שחור
		scene.geometries.add(new Plane(new Point(0, 0, 100), new Vector(0, 0, -1)) // תקרה בגובה Z=100
				.setEmission(new Color(50, 50, 50)).setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20)));

		// 4. ספרת זכוכית (שקיפות) - מיקום מעט שונה, צבע כהה יותר
		scene.geometries.add(new Sphere(new Point(30, 15, 10), 10d) // מיקום ורדיוס קטנים יותר
				.setEmission(new Color(0, 30, 0)) // גוון ירוק כהה עדין
				.setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.7))); // שקיפות גבוהה
																									// יותר

		// 5. ספרת מראה (השתקפות) - מיקום מעט שונה, צבע כהה יותר
		scene.geometries.add(new Sphere(new Point(-30, 15, 10), 10d).setEmission(new Color(0, 0, 30)) // גוון כחול
																										// כהה עדין
				.setMaterial(new Material().setKD(0.05).setKS(0.9).setShininess(150).setKR(0.9))); // החזרת אור
																									// גבוהה יותר

		// 6. מצולע (מנסרה משושה) - כבסיס לפסל
		scene.geometries.add(new Polygon(new Point(-10, -5, 0), new Point(10, -5, 0), new Point(13, 0, 0),
				new Point(10, 5, 0), new Point(-10, 5, 0), new Point(-13, 0, 0)).setEmission(new Color(100, 70, 30)) // גוון
																														// חום-זהוב
																														// עמום
																														// יותר
				.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(60)));

		// 7. משולש עליון (חלק מפסל גאומטרי)
		scene.geometries.add(new Triangle(new Point(-5, 0, 0), new Point(5, 0, 0), new Point(0, 8, 15)) // גובה קטן
																										// יותר
				.setEmission(new Color(70, 70, 70)).setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)));

		// 8. משולש נוסף (חלק מפסל גאומטרי) - מיקום וצבע שונים
		scene.geometries.add(new Triangle(new Point(0, -3, 8), new Point(8, 3, 8), new Point(-8, 3, 8))
				.setEmission(new Color(90, 60, 110)) // סגול עמוק יותר
				.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(70)));

		// 9. ספרה קטנה זוהרת (כמו נורת לד קטנה על השולחן) - פחות זוהרת, מיקום שונה
		scene.geometries.add(new Sphere(new Point(10, -8, 3), 2d).setEmission(new Color(200, 150, 0)) // צהוב-כתום
																										// פחות זוהר
				.setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(10).setKT(0.3))); // שקיפות קלה

		// 10. מישור קטן (כמו מסגרת תמונה על הקיר) - מיקום שונה
		scene.geometries.add(new Plane(new Point(0, 40, -79), new Vector(0, 0, 1)) // קרוב יותר לקיר האחורי
				.setEmission(new Color(70, 30, 0)) // חום עמוק יותר
				.setMaterial(new Material().setKD(0.3).setKS(0.1).setShininess(20)));

		// 11. ספרה נוספת (בצד, עם חומר מט) - מיקום שונה
		scene.geometries.add(new Sphere(new Point(-40, -25, 7), 8d).setEmission(new Color(150, 0, 0)) // אדום עמוק
				.setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(10)));

		// יש לנו עכשיו 11 גופים - אפילו יותר מהמינימום!

		// --- הוספת תאורה (עוצמות מופחתות) ---

		// 1. אור נקודתי (כמו נברשת בתקרה) - עוצמה נמוכה יותר
		scene.lights.add(new PointLight(new Color(400, 250, 0), new Point(0, 0, 80)) // צהוב חם, מיקום גבוה
				.setKl(0.0008).setKq(0.00008)); // דעיכה מהירה יותר

		// 2. אור כיווני (כמו שמש נכנסת מחלון) - עדיין יחסית חזק אבל עם צבע שונה
		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7))); // אפור בהיר,
																										// באלכסון
																										// מלמעלה

		// 3. אור ספוט (ממוקד על כדור הזכוכית) - עוצמה נמוכה יותר, צבע עדין
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8)) // אדום
																														// עדין
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15)); // קצת פחות ממוקד

		// --- הגדרת מצלמה ורינדור ---
		cameraBuilder.setLocation(new Point(0, -120, 40)) // מיקום המצלמה (קצת רחוק יותר ונמוך יותר)
				.setDirection(new Point(0, 0, 10), new Vector(0, 0, 1)) // כיוון מבט אל מרכז הסצנה
				.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500).build().renderImage()
				.writeToImage("complexRoomSceneOptimized"); // שם קובץ חדש
	}

	@Test
	void renderComplexRoomNEWTEST() {
		// --- הגדרות סצנה בסיסיות ---
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		// --- הוספת גופים ---

		// 1. רצפה (מישור)
		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
				.setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(50).setKR(0.2)));

		// 2. קיר אחורי (מישור)
		scene.geometries.add(new Plane(new Point(0, 0, -80), new Vector(0, 0, 1)).setEmission(new Color(60, 60, 60))
				.setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30)));

		// 3. תקרה (מישור)
		scene.geometries.add(new Plane(new Point(0, 0, 100), new Vector(0, 0, -1)).setEmission(new Color(50, 50, 50))
				.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20)));

		// 4. ספרת זכוכית (שקיפות) - מיקום מעט שונה כדי לא לחפוף
		scene.geometries.add(new Sphere(new Point(45, 15, 10), 10d).setEmission(new Color(0, 30, 0))
				.setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.9)));

		// --- קוביית מראה בנויה ממצולעים - גדולה יותר, רחוקה יותר, ומוטה ---
		// 5. קוביית מראה
		// מיקום וגודל כפי שנקבע בגרסה הקודמת
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

		// --- פירמידה במקום הפסל ---
		// נמקם את הפירמידה במרכז הפריים, קרובה למצלמה, כך שניתן לראות את השתקפותה
		// בקובייה.
		// בסיס הפירמידה (מצולע מרובע)
		Point pBase1 = new Point(-20, 20, 0); // קדמית-שמאלית
		Point pBase2 = new Point(20, 20, 0); // קדמית-ימנית
		Point pBase3 = new Point(20, 60, 0); // אחורית-ימנית
		Point pBase4 = new Point(-20, 60, 0); // אחורית-שמאלית
		// קודקוד עליון של הפירמידה
		Point pApex = new Point(0, 40, 60); // גובה 60, במרכז הבסיס (X=0, Y=40)

		Material pyramidMaterial = new Material().setKD(0.7).setKS(0.5).setShininess(80);
		Color pyramidEmissionColorBase = new Color(150, 80, 0); // חום-כתום לבסיס
		Color pyramidEmissionColorSides = new Color(100, 100, 100); // אפור בהיר לצדדים

		// 6. בסיס הפירמידה (Polygon)
		scene.geometries.add(new Polygon(pBase1, pBase2, pBase3, pBase4).setEmission(pyramidEmissionColorBase)
				.setMaterial(pyramidMaterial));

		// 7. פאה קדמית (Triangle)
		scene.geometries.add(new Triangle(pBase1, pBase2, pApex).setEmission(pyramidEmissionColorSides)
				.setMaterial(pyramidMaterial));

		// 8. פאה ימנית (Triangle)
		scene.geometries.add(new Triangle(pBase2, pBase3, pApex).setEmission(pyramidEmissionColorSides)
				.setMaterial(pyramidMaterial));

		// 9. פאה אחורית (Triangle)
		scene.geometries.add(new Triangle(pBase3, pBase4, pApex).setEmission(pyramidEmissionColorSides)
				.setMaterial(pyramidMaterial));

		// 10. פאה שמאלית (Triangle)
		scene.geometries.add(new Triangle(pBase4, pBase1, pApex).setEmission(pyramidEmissionColorSides)
				.setMaterial(pyramidMaterial));
		// סך הכל 1 Polygon + 4 Triangles = 5 גופים לפירמידה

		// 11. ספרה קטנה זוהרת (כמו נורת לד קטנה על השולחן) - מיקום שונה
		// נמקם אותה ליד הפירמידה, אולי על אחד מקודקודי הבסיס
		scene.geometries.add(new Sphere(new Point(20 + 5, 20 - 5, 5), 4d).setEmission(new Color(200, 150, 0))
				.setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(10).setKT(0.3)));

		// 12. מישור קטן (כמו מסגרת תמונה על הקיר)
		scene.geometries.add(new Plane(new Point(0, 50, -78), new Vector(0, 0, 1)).setEmission(new Color(70, 30, 0))
				.setMaterial(new Material().setKD(0.3).setKS(0.1).setShininess(20)));

		// 13. ספרה נוספת (בצד, עם חומר מט) - מיקום שונה
		scene.geometries.add(new Sphere(new Point(-50, -25, 15), 8d).setEmission(new Color(150, 0, 0))
				.setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(10)));

		// --- מצולעים נוספים להדגמה (ללא שינוי, למען ההדגמה) ---
		// 14. מצולע רצפתי מואר (כמו אריח דקורטיבי)
		scene.geometries.add(new Polygon(new Point(-20, -10, 0.1), new Point(20, -10, 0.1), new Point(20, 10, 0.1),
				new Point(-20, 10, 0.1)).setEmission(new Color(RED))
				.setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(50)));

		// 15. מצולע תלוי (כמו ציור מודרני או לוח)
		scene.geometries.add(
				new Polygon(new Point(30, 30, 50), new Point(50, 30, 50), new Point(50, 40, 50), new Point(30, 40, 50))
						.setEmission(new Color(BLUE))
						.setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30)));

		// --- הוספת תאורה ---
		scene.lights.add(new PointLight(new Color(400, 250, 0), new Point(0, 0, 80)).setKl(0.0008).setKq(0.00008));
		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7)));
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8))
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));

		// --- הגדרת מצלמה ורינדור ---
		// נתאים את כיוון המבט של המצלמה כדי להתמקד בפירמידה ובקובייה
		cameraBuilder.setLocation(new Point(0, -140, 50)).setDirection(new Point(0, 40, 20), new Vector(0, 0, 1)) // כיוון
																													// מבט
																													// אל
																													// מרכז
																													// הפירמידה
				.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500).build().renderImage()
				.writeToImage("complexRoomScenePyramidAndCube"); // שם קובץ חדש
	}

	/**
	 * Builds and renders a complex scene with various geometries and lighting
	 * effects.
	 */
	@Test
	void renderComplexRoomNEW() {
		// --- הגדרות סצנה בסיסיות ---
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		// --- הוספת גופים ---

		// 1. רצפה (מישור)
		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
				.setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(50).setKR(0.2)));

		// 2. קיר אחורי (מישור)
		scene.geometries.add(new Plane(new Point(0, 0, -80), new Vector(0, 0, 1)).setEmission(new Color(60, 60, 60))
				.setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30)));

		// 3. תקרה (מישור)
		scene.geometries.add(new Plane(new Point(0, 0, 100), new Vector(0, 0, -1)).setEmission(new Color(50, 50, 50))
				.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20)));

		// 4. ספרת זכוכית (שקיפות) - מיקום מעט שונה כדי לא לחפוף
		scene.geometries.add(new Sphere(new Point(45, 15, 10), 10d).setEmission(new Color(0, 30, 0))
				.setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.9)));

//		// --- קוביית מראה בנויה ממצולעים (6 אובייקטים מסוג Polygon) ---
//		// 5. קוביית מראה
//		// מרכז הקובייה: Point(-30, 15, 10)
//		// אורך צלע הקובייה: 20 יחידות
//		Point c1 = new Point(-40, 5, 0); // פינה קדמית-שמאלית-תחתונה
//		Point c2 = new Point(-20, 5, 0); // פינה קדמית-ימנית-תחתונה
//		Point c3 = new Point(-20, 25, 0); // פינה אחורית-ימנית-תחתונה
//		Point c4 = new Point(-40, 25, 0); // פינה אחורית-שמאלית-תחתונה
//
//		Point c5 = new Point(-40, 5, 20); // פינה קדמית-שמאלית-עליונה
//		Point c6 = new Point(-20, 5, 20); // פינה קדמית-ימנית-עליונה
//		Point c7 = new Point(-20, 25, 20); // פינה אחורית-ימנית-עליונה
//		Point c8 = new Point(-40, 25, 20); // פינה אחורית-שמאלית-עליונה
//
//		Material cubeMirrorMaterial = new Material().setKD(0.05).setKS(0.9).setShininess(150).setKR(0.95);
//		Color cubeEmissionColor = new Color(0, 0, 40);
//
//		// פאות הקובייה כ-Polygons
//		scene.geometries
//				.add(new Polygon(c1, c2, c3, c4).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
//																													// תחתונה
//		scene.geometries
//				.add(new Polygon(c5, c6, c7, c8).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
//																													// עליונה
//		scene.geometries
//				.add(new Polygon(c1, c2, c6, c5).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
//																													// קדמית
//		scene.geometries
//				.add(new Polygon(c4, c3, c7, c8).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
//																													// אחורית
//		scene.geometries
//				.add(new Polygon(c1, c4, c8, c5).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
//																													// שמאלית
//		scene.geometries
//				.add(new Polygon(c2, c3, c7, c6).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
//																													// ימנית
//		// (6 אובייקטים מסוג Polygon עבור הקובייה)
		// --- קוביית מראה בנויה ממצולעים - גדולה יותר, רחוקה יותר, ומוטה ---
		// 5. קוביית מראה
		// מיקום חדש: מרכז הקובייה בערך Point(-40, 50, 15)
		// אורך צלע הקובייה: 30 יחידות (הוגדל מ-20)

		// נגדיר את הנקודות תוך כדי הטיה (רוטציה סביב ציר Z) והזזה
		// הטיה של 45 מעלות סביב ציר Z
		// נקודות בסיס לקובייה לא מוטה בגודל 30: (-15,-15,0) עד (15,15,30) סביב מרכז
		// (0,0,15)
		// נזיז את המרכז ל- (-40, 30, 15)

		// הטיה פשוטה של 45 מעלות (כדי לפשט, נמקם את הקובייה כך שתיראה מוטה)
		// שינוי נקודות הקודקודים כדי ליצור רוטציה והגדלה
		Point r1 = new Point(-60, 30, 0); // פינה קדמית-שמאלית-תחתונה (מוזז ומוגדל, מוטה)
		Point r2 = new Point(-30, 60, 0); // פינה קדמית-ימנית-תחתונה
		Point r3 = new Point(-10, 40, 0); // פינה אחורית-ימנית-תחתונה
		Point r4 = new Point(-40, 10, 0); // פינה אחורית-שמאלית-תחתונה

		Point r5 = new Point(-60, 30, 30); // פינה קדמית-שמאלית-עליונה (מוזז ומוגדל, מוטה)
		Point r6 = new Point(-30, 60, 30); // פינה קדמית-ימנית-עליונה
		Point r7 = new Point(-10, 40, 30); // פינה אחורית-ימנית-עליונה
		Point r8 = new Point(-40, 10, 30); // פינה אחורית-שמאלית-עליונה

		Material cubeMirrorMaterial = new Material().setKD(0.05).setKS(0.9).setShininess(150).setKR(0.95);
		Color cubeEmissionColor = new Color(0, 0, 40);

		// פאות הקובייה כ-Polygons
		scene.geometries
				.add(new Polygon(r1, r2, r3, r4).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
																													// תחתונה
		scene.geometries
				.add(new Polygon(r5, r6, r7, r8).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
																													// עליונה
		scene.geometries
				.add(new Polygon(r1, r2, r6, r5).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
																													// קדמית
		scene.geometries
				.add(new Polygon(r4, r3, r7, r8).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
																													// אחורית
		scene.geometries
				.add(new Polygon(r1, r4, r8, r5).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
																													// שמאלית
		scene.geometries
				.add(new Polygon(r2, r3, r7, r6).setEmission(cubeEmissionColor).setMaterial(cubeMirrorMaterial)); // פאה
																													// ימנית
		// (6 אובייקטים מסוג Polygon עבור הקובייה)

//		// --- פסל מורכב ומוגדל (קרוב יותר למצלמה) ---
//		// נזיז את הפסל קדימה (Y חיובי) ב-20 יחידות, ובציר X מעט שמאלה.
//		int moveY = -66; // מרחק ההזזה קדימה
//		int moveX = 10; // מרחק ההזזה שמאלה
//
//		// 6. מצולע (מנסרה משושה כבסיס) - מיקום חדש
//		scene.geometries.add(new Polygon(new Point(-15 + moveX, 0 + moveY, 0), new Point(15 + moveX, 0 + moveY, 0),
//				new Point(20 + moveX, 10 + moveY, 0), new Point(15 + moveX, 20 + moveY, 0),
//				new Point(-15 + moveX, 20 + moveY, 0), new Point(-20 + moveX, 10 + moveY, 0))
//				.setEmission(new Color(120, 90, 40))
//				.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(60)));
//
//		// 7. משולש עליון (ראשון בפסל) - מיקום חדש
//		scene.geometries.add(new Triangle(new Point(-8 + moveX, 8 + moveY, 10), new Point(8 + moveX, 8 + moveY, 10),
//				new Point(0 + moveX, 25 + moveY, 40)).setEmission(new Color(90, 90, 90))
//				.setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(40)));
//
//		// 8. משולש נוסף (שני בפסל) - מיקום חדש
//		scene.geometries.add(new Triangle(new Point(0 + moveX, 5 + moveY, 20), new Point(12 + moveX, 18 + moveY, 20),
//				new Point(-12 + moveX, 18 + moveY, 20)).setEmission(new Color(110, 70, 130))
//				.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(70)));
//
//		// 9. משולש שלישי (חלק מפסל) - מיקום חדש
//		scene.geometries.add(new Triangle(new Point(-5 + moveX, 10 + moveY, 15), new Point(5 + moveX, 10 + moveY, 15),
//				new Point(0 + moveX, 0 + moveY, 30)).setEmission(new Color(40, 100, 40))
//				.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(50)));

		// --- פסל משודרג: מגדל גאומטרי עם נפח ושילוב חומרים ---
		// נזיז את הפסל קדימה (Y חיובי) ב-20 יחידות, ובציר X מעט שמאלה.
		int moveY = -66; // מרחק ההזזה קדימה
		int moveX = 10; // מרחק ההזזה שמאלה

		// חומרים לפסל החדש
		Material baseMaterial = new Material().setKD(0.5).setKS(0.3).setShininess(60);
		Material transparentMaterial = new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.8); // חומר שקוף
		Material shinyMaterial = new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.5); // חומר מבריק/מחזיר
																									// אור חלקית

		// 6. בסיס הפסל (מנסרה משושה עם גובה)
		// נקודות הבסיס התחתון (Z=0) - הגדלתי מעט את הבסיס
		Point pb1 = new Point(-20 + moveX, 0 + moveY, 0);
		Point pb2 = new Point(20 + moveX, 0 + moveY, 0);
		Point pb3 = new Point(25 + moveX, 10 + moveY, 0);
		Point pb4 = new Point(20 + moveX, 25 + moveY, 0);
		Point pb5 = new Point(-20 + moveX, 25 + moveY, 0);
		Point pb6 = new Point(-25 + moveX, 10 + moveY, 0);

		// נקודות הבסיס העליון (Z=15) - יצירת נפח
		Point pt1 = new Point(-20 + moveX, 0 + moveY, 15);
		Point pt2 = new Point(20 + moveX, 0 + moveY, 15);
		Point pt3 = new Point(25 + moveX, 10 + moveY, 15);
		Point pt4 = new Point(20 + moveX, 25 + moveY, 15);
		Point pt5 = new Point(-20 + moveX, 25 + moveY, 15);
		Point pt6 = new Point(-25 + moveX, 10 + moveY, 15);

		// בסיס תחתון
		scene.geometries.add(new Polygon(pb1, pb2, pb3, pb4, pb5, pb6).setEmission(new Color(120, 90, 40))
				.setMaterial(baseMaterial));

		// בסיס עליון
		scene.geometries.add(new Polygon(pt1, pt2, pt3, pt4, pt5, pt6).setEmission(new Color(120, 90, 40))
				.setMaterial(baseMaterial));

		// פאות צדדיות (מלבניות / Polygons)
		scene.geometries.add(new Polygon(pb1, pb2, pt2, pt1).setEmission(new Color(100, 70, 30)) // גוון מעט שונה
				.setMaterial(baseMaterial));
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

		// 7. חלק אמצעי (משולש שקוף) - ממוקם מעל הבסיס
		scene.geometries.add(new Triangle(new Point(-10 + moveX, 5 + moveY, 20), // Y קטן יותר מ-pt1/pt2
				new Point(10 + moveX, 5 + moveY, 20), new Point(0 + moveX, 15 + moveY, 45)) // גבוה יותר
				.setEmission(new Color(20, 0, 50)) // כחול-סגול כהה
				.setMaterial(transparentMaterial));

		// 8. חלק עליון (משולש מבריק/מחזיר אור) - ממוקם גבוה יותר
		scene.geometries.add(new Triangle(new Point(-5 + moveX, 10 + moveY, 30), new Point(5 + moveX, 10 + moveY, 30),
				new Point(0 + moveX, 0 + moveY, 50)) // קודקוד עליון מעט נמוך יותר ב-Y כדי להראות את הפאה
				.setEmission(new Color(50, 50, 0)) // זהב-צהוב עמוק
				.setMaterial(shinyMaterial));

		// 9. כדוריות זוהרות על הבסיס העליון
		scene.geometries.add(
				new Sphere(new Point(-20 + moveX + 5, 0 + moveY + 5, 15 + 2), 2d).setEmission(new Color(255, 100, 0)) // כתום
																														// זוהר
						.setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(10)));
		scene.geometries.add(
				new Sphere(new Point(25 + moveX - 5, 10 + moveY - 5, 15 + 2), 2d).setEmission(new Color(0, 200, 200)) // טורקיז
																														// זוהר
						.setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(10)));
		// 10. ספרה קטנה זוהרת (כמו נורת לד קטנה על השולחן)
		scene.geometries.add(new Sphere(new Point(45, 15, 10), 4d).setEmission(new Color(200, 150, 0))
				.setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(10).setKT(0.3)));

		// 11. מישור קטן (כמו מסגרת תמונה על הקיר)
		scene.geometries.add(new Plane(new Point(0, 50, -78), new Vector(0, 0, 1)).setEmission(new Color(70, 30, 0))
				.setMaterial(new Material().setKD(0.3).setKS(0.1).setShininess(20)));

		// 12. ספרה נוספת (בצד, עם חומר מט)
		scene.geometries.add(new Sphere(new Point(-50, -25, 15), 8d).setEmission(new Color(150, 0, 0))
				.setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(10)));

		// --- מצולעים נוספים להדגמה ---

//		// 13. מצולע רצפתי מואר (כמו אריח דקורטיבי)
//		scene.geometries.add(new Polygon(new Point(-20, -40, 0.1), new Point(20, -40, 0.1), new Point(20, -20, 0.1),
//				new Point(-20, -20, 0.1)).setEmission(new Color(RED)) // אדום בולט
//				.setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(50)));
//
//		// 14. מצולע תלוי (כמו ציור מודרני או לוח)
//		scene.geometries.add(
//				new Polygon(new Point(30, 30, 50), new Point(50, 30, 50), new Point(50, 40, 50), new Point(30, 40, 50))
//						.setEmission(new Color(BLUE)) // כחול
//						.setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30)));

		// --- הוספת תאורה ---
		scene.lights.add(new PointLight(new Color(400, 250, 0), new Point(0, 0, 80)).setKl(0.0008).setKq(0.00008));
		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7)));
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8))
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));

		// --- הגדרת מצלמה ורינדור ---
		Camera c = cameraBuilder.setLocation(new Point(0, -140, 50))
				.setDirection(new Point(0, 0, 20), new Vector(0, 0, 1)).setVpDistance(100).setVpSize(150, 150)
				.setResolution(500, 500).build().renderImage().writeToImage("complexRoomScenePolygonsAndCube"); //
		new Camera.Builder(c).setRotation(45).build().renderImage().writeToImage("complexRoomScenePolygonsAndCubeRO"); // ;
		new Camera.Builder(c).setRotation(45).build().renderImage().writeToImage("complexRoomScenePolygonsAndCubeRO_Y"); // ;

		new Camera.Builder(c).setTranslation(new Vector(30, 0, 0)).build().renderImage()
				.writeToImage("complexRoomScenePolygonsAndCubeMOVE"); // ;
		new Camera.Builder(c).setTranslation(new Vector(0, 0, 30)).build().renderImage()
				.writeToImage("complexRoomScenePolygonsAndCubeMOVE1");

	}

	/**
	 * Builds and renders a complex scene with various geometries and lighting
	 * effects.
	 */
}

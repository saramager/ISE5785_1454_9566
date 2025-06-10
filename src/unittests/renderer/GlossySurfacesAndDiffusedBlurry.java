package unittests.renderer;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

class GlossySurfacesAndDiffusedBlurry {
	/** Scene for the tests */
	private final Scene scene = new Scene("Test scene");
	/** Camera builder for the tests with triangles */
	private final Camera.Builder cameraBuilder = Camera.getBuilder() //
			.setRayTracer(scene, RayTracerType.SIMPLE);

	/**
	 * Builds and renders a scene with a single large sphere and a reflective plane.
	 */
	@Test
	void renderSingleSphereReflection() {
		// --- הגדרות סצנה בסיסיות ---
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		// --- הוספת גופים ---

		// 1. מישור מחזיר אור (לצורך ההשתקפות) - נחזיר רצפה פשוטה עם חומר מראה
		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
				.setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.7))); // חומר מחזיר אור

		// 2. הספרה (עיגול) - מוגדלת ומוצבת במרכז
		// מיקום: קרוב למרכז הבמה וקצת מעל הרצפה (Z=30)
		// רדיוס: מוגדל ל-25 יחידות (היה 10)
		scene.geometries.add(new Sphere(new Point(0, 0, 25), 25d) // מוגדל וממוקם במרכז
				.setEmission(new Color(0, 100, 200)) // כחול יפה
				.setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(150).setKR(0.9))); // חומר מראה חזק

		// --- כל שאר הגופים (קובייה, פירמידה, ספרות נוספות, מצולעים נוספים) - הוסרו ---

		// --- הוספת תאורה ---
		// נשמור על תאורה שמתאימה להדגשת השתקפויות
		scene.lights.add(new PointLight(new Color(600, 400, 0), new Point(0, -50, 70)).setKl(0.0008).setKq(0.00008)); // אור
																														// נקודתי
																														// חזק
																														// יותר
		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7)));
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8))
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));

		// --- הגדרת מצלמה ורינדור ---
		// נמקם את המצלמה כך שתתמקד בספרה וברצפה כדי לראות את ההשתקפות
		cameraBuilder.setLocation(new Point(0, -120, 40)) // קרובה יותר, קצת מעל
				.setDirection(new Point(0, 0, 20), new Vector(0, 0, 1)) // כיוון מבט אל מרכז הספרה
				.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500).build().renderImage()
				.writeToImage("singleSphereReflection"); // שם קובץ חדש
	}

	@Test
	void renderSingleSphereReflectionWith() {
		// --- הגדרות סצנה בסיסיות ---
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		// --- הוספת גופים ---

		// 1. מישור מחזיר אור (לצורך ההשתקפות) - נחזיר רצפה פשוטה עם חומר מראה
		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
				.setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.7).setRAngle(100))); // חומר
																													// מחזיר
																													// אור

		// 2. הספרה (עיגול) - מוגדלת ומוצבת במרכז
		// מיקום: קרוב למרכז הבמה וקצת מעל הרצפה (Z=30)
		// רדיוס: מוגדל ל-25 יחידות (היה 10)
		scene.geometries.add(new Sphere(new Point(0, 0, 25), 25d) // מוגדל וממוקם במרכז
				.setEmission(new Color(0, 100, 200)) // כחול יפה
				.setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(150).setKR(0.9))); // חומר מראה חזק

		// --- כל שאר הגופים (קובייה, פירמידה, ספרות נוספות, מצולעים נוספים) - הוסרו ---

		// --- הוספת תאורה ---
		// נשמור על תאורה שמתאימה להדגשת השתקפויות
		scene.lights.add(new PointLight(new Color(600, 400, 0), new Point(0, -50, 70)).setKl(0.0008).setKq(0.00008)); // אור
																														// נקודתי
																														// חזק
																														// יותר
		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7)));
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8))
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));

		// --- הגדרת מצלמה ורינדור ---
		// נמקם את המצלמה כך שתתמקד בספרה וברצפה כדי לראות את ההשתקפות
		cameraBuilder.setLocation(new Point(0, -120, 40)) // קרובה יותר, קצת מעל
				.setDirection(new Point(0, 0, 20), new Vector(0, 0, 1)) // כיוון מבט אל מרכז הספרה
				.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500).setMultithreading(2).setDebugPrint(1)
				.build().renderImage().writeToImage("si"); // שם קובץ חדש
	}

	@Test
	public void testBlurryGlass() {

		Vector vTo = new Vector(0, 1, 0);

		scene.setAmbientLight(new AmbientLight(new Color(150, 150, 150).reduce(2)));

		for (int i = -4; i < 6; i += 2) {
			scene.geometries.add(
					new Sphere(new Point(5 * i, -1.50, -3), 3.0).setEmission(new Color(0, 275, 0).reduce(4).reduce(2))
							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0d)),

					new Sphere(new Point(5 * i, 5, 3), 3.0).setEmission(new Color(40, 80, 230).reduce(2))
							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0d)),
					new Sphere(new Point(5 * i, -8, -8), 3.0).setEmission(new Color(230, 40, 200).reduce(2))
							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0d)),

					new Polygon(new Point(5 * i - 4, -5, -11), new Point(5 * i - 4, -5, 5), new Point(5 * i + 4, -5, 5),
							new Point(5 * i + 4, -5, -11)).setEmission(new Color(250, 235, 215).reduce(2))
							.setMaterial(new Material().setKD(0.001).setKS(0.002).setShininess(1).setKT(0.95)
									.setTAngle(i * 10 + 80))

			);
		}

		scene.geometries.add(new Plane(new Point(1, 10, 1), new Point(2, 10, 1), new Point(5, 10, 0))
				.setEmission(new Color(255, 255, 255).reduce(3))
				.setMaterial(new Material().setKD(0.2).setKS(0).setShininess(0).setKT(0d))

		);

		// scene.lights.add(new PointLight(new Color(100, 100, 150), new Point(0, 6,
		// 0)));
		scene.lights.add(new DirectionalLight(new Color(255, 255, 255).reduce(1), new Vector(-0.4, 1, 0)));
		scene.lights.add(new SpotLight(new Color(255, 255, 255).reduce(2), new Point(20.43303, -7.37104, 13.77329),
				new Vector(-20.43, 7.37, -13.77)).setKl(0.6));
		cameraBuilder.setResolution(500, 500).setLocation(new Point(0, -50, 0)).setDirection(vTo, new Vector(0, 0, 1))
				.setVpSize(200d, 200).setVpDistance(1000).setVpDistance(100).setVpSize(150, 150).setResolution(500, 500)
				.setMultithreading(2).setDebugPrint(1).build().renderImage().writeToImage("blurryGlass2");

	}
}

package unittests.renderer;

import org.junit.jupiter.api.Test;

import geometries.Plane;
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

//	@Test
//	void transparencyReflectionShadow2Test() {
//
//		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
//				.setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(50).setKR(0.8)));
//
//		scene.geometries.add(new Plane(new Point(0, 0, -80), new Vector(0, 0, 1)).setEmission(new Color(60, 60, 60))
//				.setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30)));
//
//		scene.geometries.add(new Plane(new Point(0, 0, 100), new Vector(0, 0, -1)).setEmission(new Color(50, 50, 50))
//				.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20)));
//
//		scene.geometries.add(
//				// Transparent polygon – we turn it into glass with higher transparency and less
//				// "light emission"ר
//
//				new Sphere(new Point(0, 30, -20), 20d).setEmission(new Color(BLUE)) // The sphere is blue because of its
//																					// color
//						.setMaterial(new Material().setKD(0.6) // Diffuses blue light
//								.setKS(0.5).setShininess(100).setKT(0.0))); // Completely opaque– no light passes
//																			// through
//
//		// Reflective triangle – more like a real mirror
//		// mirror
//		// reflection
//
//		// hiny plane – polished floor
//
//		// Additional green sphere – to emphasize transparency behind the polygon
//
//		scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
//
//		// Spot light – with a bit more attenuation
//		scene.lights.add(new SpotLight(new Color(1000, 600, 600), new Point(0, 100, 200), new Vector(0, -1, -1))
//				.setKl(0.001).setKq(0.0001)); // Slightly higher attenuation coefficients – the light fades faster,
//		// making it more concentrated around the source.
//
//		cameraBuilder.setLocation(new Point(0, 300, 0)).setDirection(Point.ZERO, new Vector(0, 0, -1)) // תיקון כיוון
//				.setVpDistance(300).setVpSize(200, 200).setResolution(600, 600).build().renderImage()
//				.writeToImage("transparency");
//
//	}

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
				.setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.7).setRAngle(4))); // חומר
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
				.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500).build().renderImage()
				.writeToImage("si"); // שם קובץ חדש
	}

}
